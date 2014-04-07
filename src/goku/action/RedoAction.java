package goku.action;

import goku.GOKU;
import goku.Result;
import goku.TaskList;

public class RedoAction extends Action {
  private static final String MSG_SUCCESS = "Command undone!";
  private static final String ERR_FAIL = "Failed to undo.";
  private static final String MSG_HAS_OVERDUE = "[!] You have overdue tasks, \"view overdue\" to see them.";

  private boolean isEmpty = false;

  public RedoAction(GOKU goku) {
    super(goku);
    list = goku.getTaskList();
    // TODO Auto-generated constructor stub
  }

  public String editMsgIfHaveOverdue(String msg) {
    if (list.getOverdue().size() != 0) {
      msg += System.lineSeparator() + MSG_HAS_OVERDUE;
    }
    return msg;
  }

  @Override
  public Result doIt() {
    if (redoCommand() == false) {
      return new Result(false, null, editMsgIfHaveOverdue(ERR_FAIL), goku
          .getTaskList().getAllIncomplete());
    } else {
      return new Result(true, editMsgIfHaveOverdue(MSG_SUCCESS), null, goku
          .getTaskList().getAllIncomplete());
    }
  }

  public void addToUndoList() {
    TaskList currList = new TaskList();
    currList = list.clone();

    goku.getUndoList().offer(currList);
  }

  public boolean redoCommand() {
    if (goku.getRedoList().isEmpty()) {
      return isEmpty;
    }

    addToUndoList();
    TaskList prevList = goku.getRedoList().pollLast();
    goku.setTaskList(prevList);

    return !isEmpty;
  }
}
