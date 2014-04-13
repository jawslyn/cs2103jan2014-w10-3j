package goku.action;

import goku.GOKU;
import goku.Result;
import goku.TaskList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
public class ClearAction extends Action {
  private static final String MSG_SUCCESS = "Completed tasks cleared!";
  private static final String ERR_FAIL = "Fail to clear, no completed tasks!";
  private static final String MSG_HAS_OVERDUE = "[!] You have overdue tasks, \"view overdue\" to see them.";

  public ClearAction(GOKU goku) {
    super(goku);
    list = goku.getTaskList();
  }

  private Result clear() {
    if (list.getAllCompleted().isEmpty()) {
      return new Result(false, null, editMsgIfHaveOverdue(ERR_FAIL), null);
    }
    addToUndoList();
    list.clearCompleted();
    return new Result(true, editMsgIfHaveOverdue(MSG_SUCCESS), null, null);
  }

  private void addToUndoList() {
    TaskList currList = new TaskList();
    currList = list.clone();

    goku.getUndoList().offer(currList);
  }

  @Override
  public Result doIt() {
    return clear();
  }

  public String editMsgIfHaveOverdue(String msg) {
    if (list.getOverdue().size() != 0) {
      msg += System.lineSeparator() + MSG_HAS_OVERDUE;
    }
    return msg;
  }
}