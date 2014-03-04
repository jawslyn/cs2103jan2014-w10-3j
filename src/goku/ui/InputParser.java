package goku.ui;

import goku.DateRange;
import goku.DateUtil;
import goku.GOKU;
import goku.action.Action;
import goku.action.AddAction;
import goku.action.DeleteAction;
import goku.action.DisplayAction;
import goku.action.EditAction;
import goku.action.NoAction;
import goku.action.SearchAction;
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
  private String[] displayKeywords = { "display", "view", "show", "v", "s" };
  private String[] searchKeywords = { "search", "find", "f" };
  private String[] exitKeywords = { "quit", "exit", "q" };

  private GOKU goku;

  public InputParser(GOKU goku) {
    this.goku = goku;
  }

  /*
   * @return NoAction if the input cannot be parsed, else a subclass of Action
   */
  public Action parse(String input) {
    Action action = null;

    if (input == null || input.isEmpty()) {
      return new NoAction(goku);
    }

    List<String> inputList = Splitter.on(' ').omitEmptyStrings().trimResults()
        .splitToList(input);
    String[] inputArray = inputList.toArray(new String[inputList.size()]);
    String command = inputArray[0];
    String[] params = inputArray.length > 1 ? Arrays.copyOfRange(inputArray, 1,
        inputArray.length) : new String[0];

    if (Arrays.asList(addKeywords).contains(command)) {
      action = makeAddAction(params);
    } else if (Arrays.asList(deleteKeywords).contains(command)) {
      action = makeDeleteAction(params);
    } else if (Arrays.asList(editKeywords).contains(command)) {
      action = makeEditAction(params);
    } else if (Arrays.asList(displayKeywords).contains(command)) {
      action = makeDisplayAction(params);
    } else if (Arrays.asList(searchKeywords).contains(command)) {
      action = makeSearchAction(params);
    } else if (Arrays.asList(exitKeywords).contains(command)) {
      action = new ExitAction(goku);
    }
    if (action == null) {
      return new NoAction(goku);
    }
    return action;
  }

  /*
   * Add expects to have parameters, minimally params[0] should be the title of
   * the task.
   * 
   * @param params array of parameters that accompany the add action
   * 
   * @return null if no parameters are specified, else an AddAction with the
   * title, due date, or deadline.
   */
  private AddAction makeAddAction(String[] params) {
    if (params.length == 0) {
      return null;
    } else {
      AddAction a = new AddAction(goku);
      params = extractDeadline(params, a);
      params = extractPeriod(params, a);
      a.title = Joiner.on(" ").join(params);
      return a;
    }
  }

  private SearchAction makeSearchAction(String[] params) {
    if (params.length == 0) {
      return null;
    }
    SearchAction sa = new SearchAction(goku);
    sa.title = Joiner.on(" ").join(params);
    return sa;
  }

  private DisplayAction makeDisplayAction(String[] params) {
    DisplayAction da = new DisplayAction(goku);
    return da;
  }

  private EditAction makeEditAction(String[] params) {
    if (params.length < 2) {
      return null;
    }
    EditAction ea = new EditAction(goku);
    try {
      int id = Integer.parseInt(params[0]);
      ea.id = id;
      String[] taskParams = Arrays.copyOfRange(params, 1, params.length);
      AddAction aaa = makeAddAction(taskParams);
      ea.deadline = aaa.deadline;
      ea.from = aaa.from;
      ea.to = aaa.to;
      ea.title = aaa.title;
    } catch (NumberFormatException e) {
    }
    return ea;
  }

  private DeleteAction makeDeleteAction(String[] params) {
    if (params.length == 0) {
      return null;
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

  private String[] extractPeriod(String[] params, AddAction a) {
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
          a.from = Joiner.on(" ").join(startCandidates);
          a.to = Joiner.on(" ").join(endCandidates);
          DateRange dr = new DateRange(DateUtil.toDate(start),
              DateUtil.toDate(end));
          a.period = dr;
          params = Arrays.copyOfRange(params, 0, indexOfFrom);
        }
      }
    }
    return params;
  }

  /*
   * A deadline is identified by the presence of the keyword "by".
   * Assumption: the deadline is always at the end of the input,
   * i.e. whatever follows "by" will parsed into a date.
   * Valid inputs are ["by", "2pm", "tomorrow"] or ["by", "1:45pm"].
   * When the parse is successful @param params is modified such that
   * the parsed strings, including "by", is removed - essentially params
   * is shortened.
   */
  private String[] extractDeadline(String[] params, AddAction a) {
    int indexOfBy = Arrays.asList(params).indexOf("by");
    if (indexOfBy < 0) {
      return params;
    }
    // get the rest of the params after "by"
    String[] candidates = Arrays.copyOfRange(params, indexOfBy + 1,
        params.length);
    DateTime parsed = DateUtil.parse(candidates);
    if (parsed != null) {
      a.dline = DateUtil.toDate(parsed);
      a.deadline = Joiner.on(" ").join(candidates);
      params = Arrays.copyOfRange(params, 0, indexOfBy);
    }
    return params;
  }
}
