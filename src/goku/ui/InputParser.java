package goku.ui;

import goku.Commands;
import goku.DateRange;
import goku.GOKU;
import goku.action.Action;
import goku.action.AddAction;
import goku.action.DeleteAction;
import goku.action.DisplayAction;
import goku.action.EditAction;
import goku.action.ExitAction;
import goku.action.MakeActionException;
import goku.action.NoAction;
import goku.action.RedoAction;
import goku.action.SearchAction;
import goku.action.UndoAction;
import goku.action.UnknownAction;
import goku.util.DateUtil;
import hirondelle.date4j.DateTime;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/*
 * InputParser parses an input String into an {@link Action}.
 * The steps taken are:
 * 1. Split the input into tokens. Each token is guaranteed to be
 * non-empty and trimmed
 * 2. The first token is checked to see if it is a command.
 * This check is done by iterating through the arrays of keywords.
 * 3. The appropriate subclass of Action is made using the
 * makeXAction methods.
 * 4. In the event when any of the Actions are null, a NoAction is returned.
 * 
 */
public class InputParser {
  /*
   * Keywords that are used to associate an input to a particular action.
   */
  private String[] addKeywords = Commands.addKeywords;
  private String[] deleteKeywords = Commands.deleteKeywords;
  private String[] editKeywords = Commands.editKeywords;
  private String[] completeKeywords = Commands.completeKeywords;
  private String[] displayKeywords = Commands.displayKeywords;
  private String[] searchKeywords = Commands.searchKeywords;
  private String[] exitKeywords = Commands.exitKeywords;
  private String[] undoKeywords = Commands.undoKeywords;
  private String[] redoKeywords = Commands.redoKeywords;
  private String[] helpKeywords = Commands.helpKeywords;

  private GOKU goku;
  String[] params;
  Integer paramsByIndex, paramsFromIndex;

  public InputParser(GOKU goku) {
    this.goku = goku;
    paramsByIndex = null;
    paramsFromIndex = null;
  }

  /*
   * extractDeadline() Specifics
   * 1) Contains date and time => returns DateTime with date and time
   * 2) Contains date only => returns DateTime with date, time initialised to 23:59:59
   * 3) Contains time only => returns DateTime with today as date and time
   * 4) Returns null if input is not valid
   * 5) Nanoseconds are truncated
   */
  DateTime extractDeadline() {
    int indexOfBy = Arrays.asList(params).indexOf("by");
    if (indexOfBy < 0) {
      return null;
    } else {
      paramsByIndex = indexOfBy;
    }
    // get the rest of the params after "by"
    String[] candidates = Arrays.copyOfRange(params, indexOfBy + 1,
        params.length);
    DateTime parsed = DateUtil.parse(candidates);
    if (parsed == null) {
      return parsed;
    }

    // add time 23:59 to deadline if no time was specified
    if (parsed.getHour() == null) {
      return initTimeToEndOfDay(parsed);
    } else {
      return parsed;
    }
  }

  private DateTime parseDate() {
    if (params.length < 1) {
      return null;
    }

    return DateUtil.parse(params);
  }

  /*
   * A period is identified by the presence of the keywords "from" and "to".
   * Assumption: the period is always at the end of the input,
   * 
   * i.e. whatever follows "from" will parsed into a date representing the
   * start, and what follows "to" will be parsed into a date representing the
   * end.
   * 
   * Valid inputs are ["from", "2pm 12/2", "to" "3pm 12/2"]. When the parse is
   * successful @param params is modified such that the parsed strings,
   * including "from" and "to", is removed - essentially params is shortened.
   * 
   * extractPeriod() Specifics
   * 1) Start: date+time End: date+time => Start: date+time End: date+time
   * 2) Any uninitialised date will be set to today
   * 3) Any uninitialised start time will be 00:00:00 (without nanoseconds)
   * 4) Any uninitialised end time will be 23:59:59 (without nanoseconds)
   * 5) If end date before start date, return null
   */
  DateRange extractPeriod() throws MakeActionException{
    DateRange dr = null;
    int indexOfFrom = Arrays.asList(params).indexOf("from");
    int indexOfTo = Arrays.asList(params).indexOf("to");
    if (indexOfFrom >= 0 && indexOfTo >= 0) {
      paramsFromIndex = indexOfFrom;
      if (indexOfTo + 1 < params.length) {
        /*
         * Parse start date
         */
        String[] startCandidates = Arrays.copyOfRange(params, indexOfFrom + 1,
            indexOfTo);

        DateTime start = DateUtil.parse(startCandidates);

        if (start != null && start.getHour() == null) {
          start = initTimeToStartOfDay(start);
        }

        /*
         * Parsed end date
         */
        String[] endCandidates = Arrays.copyOfRange(params, indexOfTo + 1,
            params.length);
        DateTime end = DateUtil.parse(endCandidates);

        if (end != null && end.getHour() == null) {
          end = initTimeToEndOfDay(end);
        }

        if (start != null && end != null) {
          dr = new DateRange(start, end);
        }
      }
    }
    return dr;
  }

  /*
   * Similar to extractDate and extractPeriod, extractTitle finds the relevant title
   * content in params and returns the title string.
   * 
   * Variables paramsByIndex and paramsFromIndex indicates the possible cutoff point
   * in the input string between title and date time inputs. extractTitle cuts off from
   * the earliest inde indicated by the two variables.
   */
  String extractTitle() {
    int indexToSplit;

    if (paramsByIndex != null && paramsFromIndex != null) {
      assert paramsByIndex > 0;
      assert paramsFromIndex > 0;
      indexToSplit = Math.min(paramsByIndex, paramsFromIndex);
    } else if (paramsByIndex != null) {
      indexToSplit = paramsByIndex;
    } else if (paramsFromIndex != null) {
      indexToSplit = paramsFromIndex;
    } else {
      return Joiner.on(" ").join(params);
    }

    return Joiner.on(" ").join(Arrays.copyOfRange(params, 0, indexToSplit));
  }

  private DateTime initTimeToStartOfDay(DateTime date) {
    String parsedString = date.format("YYYY-MM-DD");
    parsedString = parsedString.concat(" 00:00:00");

    return new DateTime(parsedString);
  }

  private DateTime initTimeToEndOfDay(DateTime date) {
    String parsedString = date.format("YYYY-MM-DD");
    parsedString = parsedString.concat(" 23:59:59");

    return new DateTime(parsedString);
  }

  /*
   * Add expects to have parameters, minimally params[0] should be the title
   * of the task.
   * 
   * @param importance of task (true or false)
   * 
   * @return null if no parameters are specified, else an AddAction with the
   * title, due date, or deadline.
   */
  private AddAction makeAddAction(boolean impt) throws MakeActionException {
    if (params.length == 0) {
      throw new MakeActionException(AddAction.ERR_INSUFFICIENT_ARGS);
    }
    AddAction addAction = new AddAction(goku);
    if (impt == true) {
      addAction.isImpt = true;
    }
    DateTime dl = extractDeadline();
    DateRange dr = extractPeriod();
    addAction.dline = dl;
    addAction.period = dr;
    addAction.title = extractTitle();
    return addAction;
  }

  private EditAction makeCompleteAction() throws MakeActionException {
    if (params.length < 1) {
      throw new MakeActionException(
          EditAction.ERR_INSUFFICIENT_ARGS_FOR_COMPLETION);
    }
    EditAction editAction = new EditAction(goku);
    try {
      int id = Integer.parseInt(params[0]);
      editAction.id = id;
      editAction.isComplete = true;
    } catch (NumberFormatException e) {
      return null;
    }
    return editAction;
  }

  /*
   * Delete can take in a single parameter which can be parsed into an
   * integer, this is then assumed to be the ID of the Task to be deleted.
   * Else the inputs will be taken as the title of the task to be deleted.
   */
  private DeleteAction makeDeleteAction() throws MakeActionException {
    if (params.length == 0) {
      throw new MakeActionException(DeleteAction.ERR_INSUFFICIENT_ARGS);
    }
    DeleteAction da = new DeleteAction(goku);
    if (params.length == 1) {
      try {
        int id = Integer.parseInt(params[0]);
        da.id = id;
      } catch (NumberFormatException e) {
        da.id = null;
        da.title = params[0];
      }
    } else {
      da.title = Joiner.on(" ").join(params);
    }
    return da;
  }

  private DisplayAction makeDisplayAction() {

    DisplayAction da = new DisplayAction(goku);

    String nonArrayParams = Joiner.on(" ").join(params).toLowerCase();
    if (nonArrayParams.contains("completed")) {
      da.viewComplete = true;
    } else if (nonArrayParams.contains("overdue")) {
      da.viewOverdue = true;
    }

    return da;
  }

  /*
   * EditAction requires minimally 2 parameters 1) id for task to edit 2) an
   * edit, which could be the title, deadline, period etc.
   * 
   * @return null if we cannot decide which task to edit
   */
  private EditAction makeEditAction() throws MakeActionException {
    if (params.length < 2) {
      throw new MakeActionException(EditAction.ERR_INSUFFICIENT_ARGS);
    }

    EditAction editAction = new EditAction(goku);
    try {
      int id = Integer.parseInt(params[0]);
      editAction.id = id;
      params = Arrays.copyOfRange(params, 1, params.length);

      if (params[0].equalsIgnoreCase("remove") && params.length > 1) {
        // removing a certain field of task
        switch (params[1]) {
          case "deadline" :
            editAction.removeDeadline = true;
            params = Arrays.copyOfRange(params, 2, params.length);
            break;
          case "period" :
            editAction.removePeriod = true;
            params = Arrays.copyOfRange(params, 2, params.length);
            break;
          case "important" :
            editAction.removeImportant = true;
            params = Arrays.copyOfRange(params, 2, params.length);
            break;
        }
      }

      DateTime dl = extractDeadline();
      DateRange dr = extractPeriod();
      editAction.dline = dl;
      editAction.period = dr;

      String title = extractTitle();
      if (!title.isEmpty()) {
        editAction.title = title;
      }
    } catch (NumberFormatException e) {
      throw new MakeActionException(EditAction.ERR_NO_ID_GIVEN);
    }
    return editAction;
  }

  /*
   * All inputs to search are taken to be the title of the Task to find
   */
  private SearchAction makeSearchAction(boolean testFree)
      throws MakeActionException {
    if (params.length == 0) {
      throw new MakeActionException(SearchAction.ERR_INSUFFICIENT_ARGS);
    }

    SearchAction searchAction = new SearchAction(goku);

    // determine if search is to find free slots
    if (testFree == true) {
      // test if datetime received is free of tasks
      searchAction.testFree = testFree;
      searchAction.dateQuery = parseDate();
      if (searchAction.dateQuery == null) {
        throw new MakeActionException(SearchAction.ERR_NO_VALID_DATE_FOUND);
      }
    } else {
      // search normally
      DateTime dl = extractDeadline();
      DateRange dr = extractPeriod();
      searchAction.dline = dl;
      searchAction.period = dr;
      String title = extractTitle();
      if (!title.isEmpty()) {
        searchAction.title = title;
      }
    }

    return searchAction;
  }

  /*
   * @return NoAction if there is no input, UnknownAction if the command is not known
   */
  public Action parse(String input) throws MakeActionException {
    Action action = null;

    if (input == null || input.isEmpty()) {
      return new NoAction(goku);
    }

    List<String> inputList = Splitter.on(' ').omitEmptyStrings().trimResults()
        .splitToList(input);
    String[] inputArray = inputList.toArray(new String[inputList.size()]);
    String command = inputArray[0];
    params = inputArray.length > 1 ? Arrays.copyOfRange(inputArray, 1,
        inputArray.length) : new String[0];

    if (Arrays.asList(addKeywords).contains(command)) {
      // determine importance
      if (command.equals("add!")) {
        action = makeAddAction(true);
      } else {
        action = makeAddAction(false);
      }
    } else if (Arrays.asList(deleteKeywords).contains(command)) {
      action = makeDeleteAction();
    } else if (Arrays.asList(editKeywords).contains(command)) {
      action = makeEditAction();
    } else if (Arrays.asList(completeKeywords).contains(command)) {
      action = makeCompleteAction();
    } else if (Arrays.asList(displayKeywords).contains(command)) {
      action = makeDisplayAction();
    } else if (Arrays.asList(searchKeywords).contains(command)) {
      if (command.equals("free")) {
        action = makeSearchAction(true);
      } else {
        action = makeSearchAction(false);
      }
    } else if (Arrays.asList(exitKeywords).contains(command)) {
      action = new ExitAction(goku);
    } else if (Arrays.asList(undoKeywords).contains(command)) {
      action = new UndoAction(goku);
    } else if (Arrays.asList(redoKeywords).contains(command)) {
      action = new RedoAction(goku);
    } else if (Arrays.asList(helpKeywords).contains(command)) {
      action = new HelpAction(goku);
    } else {
      action = new UnknownAction(goku, command);
    }
    if (action == null) {
      action = new NoAction(goku);
    }
    return action;
  }
}
