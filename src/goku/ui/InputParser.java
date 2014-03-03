package goku.ui;

import goku.GOKU;
import goku.action.Action;
import goku.action.AddAction;
import goku.action.DeleteAction;
import goku.action.DisplayAction;
import goku.action.EditAction;
import goku.action.NoAction;
import goku.action.SearchAction;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class InputParser {
  public String[] addKeywords = { "add", "a" };
  public String[] deleteKeywords = { "delete", "d", "remove", "r" };
  public String[] editKeywords = { "edit", "e", "update", "u" };
  public String[] displayKeywords = { "display", "view", "show", "v", "s" };
  public String[] searchKeywords = { "search", "find", "f" };
  public String[] exitKeywords = { "quit", "exit", "q" };

  private GOKU goku;

  public InputParser(GOKU goku) {
    this.goku = goku;
  }

  public Action parse(String string) {
    Arrays.asList(addKeywords).contains("hi");
    if (string == null || string.isEmpty()) {
      return new NoAction(goku);
    }

    // String[] tokens = string.split(" ");
    List<String> l = Splitter.on(' ').omitEmptyStrings().trimResults()
        .splitToList(string);
    String[] tokens = l.toArray(new String[l.size()]);

    String command = tokens[0];
    String[] params = tokens.length > 1 ? Arrays.copyOfRange(tokens, 1,
        tokens.length) : new String[0];

    if (Arrays.asList(addKeywords).contains(command)) {
      AddAction ADDACT = makeAddActionADD(params);
      if (ADDACT == null) {
        return new NoAction(goku);
      }
      return ADDACT;
    } else if (Arrays.asList(deleteKeywords).contains(command)) {
      DeleteAction da = makeDeleteActionACT(params);
      if (da == null) {
        return new NoAction(goku);
      }
      return da;
    } else if (Arrays.asList(editKeywords).contains(command)) {
      EditAction ea = makeEditActionACT(params);
      if (ea == null) {
        return new NoAction(goku);
      }
      return ea;
    } else if (Arrays.asList(displayKeywords).contains(command)) {
      DisplayAction da = makeDisplayAction(params);
      if (da == null) {
        return new NoAction(goku);
      }
      return da;
    } else if (Arrays.asList(searchKeywords).contains(command)) {
      SearchAction sa = makeSearchAction(params);
      if (sa == null) {
        return new NoAction(goku);
      }
      return sa;
    } else if (Arrays.asList(exitKeywords).contains(command)) {
      return new ExitAction(goku);
    }
    return new NoAction(goku);
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

  private EditAction makeEditActionACT(String[] params) {
    if (params.length < 2) {
      return null;
    }
    EditAction ea = new EditAction(goku);
    try {
      int id = Integer.parseInt(params[0]);
      ea.id = id;
      String[] taskParams = Arrays.copyOfRange(params, 1, params.length);
      AddAction aaa = makeAddActionADD(taskParams);
      ea.deadline = aaa.deadline;
      ea.from = aaa.from;
      ea.to = aaa.to;
      ea.title = aaa.title;
    } catch (NumberFormatException e) {
    }
    return ea;
  }

  private DeleteAction makeDeleteActionACT(String[] params) {
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

  private AddAction makeAddActionADD(String[] params) {
    if (params.length == 0) {
      return null;
    } else {
      AddAction a = new AddAction(goku);
      int indexOfBy = Arrays.asList(params).indexOf("by");
      if (indexOfBy >= 0) {
        if (indexOfBy + 1 < params.length) {
          // by is a keyword, we expect a date-like to follow, but if it doesnt
          // by is treated as non-keyword
          String deadline = params[indexOfBy + 1];
          a.deadline = deadline;
          params = Arrays.copyOf(params, params.length - 2);
        }
      }
      int indexOfFrom = Arrays.asList(params).indexOf("from");
      int indexOfTo = Arrays.asList(params).indexOf("to");
      if (indexOfFrom >= 0 && indexOfTo >= 0) {
        if (indexOfTo + 1 < params.length) {
          String from = params[indexOfFrom + 1];
          String to = params[indexOfTo + 1];
          a.from = from;
          a.to = to;
          params = Arrays.copyOf(params, params.length - 4);
        }
      }
      a.title = Joiner.on(" ").join(params);
      return a;
    }
  }

  private void makeEditAction(String[] params, EditAction ea) {
    try {
      int id = Integer.parseInt(params[0]);
      ea.id = id;
      String[] taskParams = Arrays.copyOfRange(params, 1, params.length);
      AddAction aaa = makeAddActionADD(taskParams);
      ea.deadline = aaa.deadline;
      ea.from = aaa.from;
      ea.to = aaa.to;
      ea.title = aaa.title;
    } catch (NumberFormatException e) {
    }
  }

  private void makeDeleteAction(String[] params, DeleteAction da) {
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
  }
}
