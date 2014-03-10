package goku.ui;

import goku.DateRange;
import goku.DateUtil;
import goku.GOKU;
import goku.action.Action;
import goku.action.AddAction;
import goku.action.DeleteAction;
import goku.action.DisplayAction;
import goku.action.EditAction;
import goku.action.ExitAction;
import goku.action.NoAction;
import goku.action.SearchAction;
import goku.action.UndoAction;
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
  private String[] addKeywords = { "add", "a" };
  private String[] deleteKeywords = { "delete", "d", "remove", "r" };
  private String[] editKeywords = { "edit", "e", "update", "u" };
  private String[] completeKeywords = { "done", "complete", "do", "finish",
      "fin" };
  private String[] displayKeywords = { "display", "view", "show", "v", "s" };
  private String[] searchKeywords = { "search", "find", "f" };
  private String[] exitKeywords = { "quit", "exit", "q" };
  private String[] undoKeywords = { "undo", "revert", "rollback" };

  private GOKU goku;
  private String[] params;

  public InputParser(GOKU goku) {
    this.goku = goku;
  }

  /*
   * @return NoAction if the input cannot be parsed, else a subclass of Action
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
      action = makeAddAction();
    } else if (Arrays.asList(deleteKeywords).contains(command)) {
      action = makeDeleteAction();
    } else if (Arrays.asList(editKeywords).contains(command)) {
      action = makeEditAction();
    } else if (Arrays.asList(completeKeywords).contains(command)) {
      action = makeCompleteAction();
    } else if (Arrays.asList(displayKeywords).contains(command)) {
      action = makeDisplayAction();
    } else if (Arrays.asList(searchKeywords).contains(command)) {
      action = makeSearchAction();
    } else if (Arrays.asList(exitKeywords).contains(command)) {
      action = new ExitAction(goku);
    } else if (Arrays.asList(undoKeywords).contains(command)) {
      action = new UndoAction(goku);
    }

    if (action == null) {
      return new NoAction(goku);
    }
    return action;
  }

  /*
   * Add expects to have parameters, minimally params[0] should be the title
   * of the task.
   * 
   * @param params array of parameters that accompany the add action
   * 
   * @return null if no parameters are specified, else an AddAction with the
   * title, due date, or deadline.
   */
  private AddAction makeAddAction() throws MakeActionException {
    if (params.length == 0) {
      throw new MakeActionException(AddAction.ERR_INSUFFICIENT_ARGS);
    }
    AddAction addAction = new AddAction(goku);
    DateTime dl = extractDeadline();
    DateRange dr = extractPeriod();
    addAction.dline = DateUtil.toDate(dl);
    addAction.period = dr;
    addAction.title = Joiner.on(" ").join(params);
    return addAction;
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

      DateTime dl = extractDeadline();
      DateRange dr = extractPeriod();
      editAction.dline = DateUtil.toDate(dl);
      editAction.period = dr;
      String title = Joiner.on(" ").join(params);
      if (!title.isEmpty()) {
        editAction.title = title;
      }
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
        da.title = params[0];
      }
    } else {
      da.title = Joiner.on(" ").join(params);
    }
    return da;
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
   * All inputs to search are taken to be the title of the Task to find
   */
  private SearchAction makeSearchAction() throws MakeActionException {
    if (params.length == 0) {
      throw new MakeActionException(SearchAction.ERR_INSUFFICIENT_ARGS);
    }

    SearchAction searchAction = new SearchAction(goku);
    DateTime dl = extractDeadline();
    DateRange dr = extractPeriod();
    searchAction.dline = DateUtil.toDate(dl);
    searchAction.period = dr;
    String title = Joiner.on(" ").join(params);
    if (!title.isEmpty()) {
      searchAction.title = title;
    }
    return searchAction;
  }

  private DisplayAction makeDisplayAction() {
    DisplayAction da = new DisplayAction(goku);
    return da;
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
   */
  private DateRange extractPeriod() {
    DateRange dr = null;
    int indexOfFrom = Arrays.asList(params).indexOf("from");
    int indexOfTo = Arrays.asList(params).indexOf("to");
    if (indexOfFrom >= 0 && indexOfTo >= 0) {
      if (indexOfTo + 1 < params.length) {
        String[] startCandidates = Arrays.copyOfRange(params, indexOfFrom + 1,
            indexOfTo);
        DateTime start = DateUtil.parse(startCandidates);

        String[] endCandidates = Arrays.copyOfRange(params, indexOfTo + 1,
            params.length);
        DateTime end = DateUtil.parse(endCandidates);

        if (start != null && end != null) {
          dr = new DateRange(DateUtil.toDate(start), DateUtil.toDate(end));
          params = Arrays.copyOfRange(params, 0, indexOfFrom);
        }
      }
    }
    return dr;
  }

  private DateTime extractDeadline() {
    int indexOfBy = Arrays.asList(params).indexOf("by");
    if (indexOfBy < 0) {
      return null;
    }
    // get the rest of the params after "by"
    String[] candidates = Arrays.copyOfRange(params, indexOfBy + 1,
        params.length);
    DateTime parsed = DateUtil.parse(candidates);
    if (parsed != null) {
      params = Arrays.copyOfRange(params, 0, indexOfBy);
    }
    return parsed;
  }
}
