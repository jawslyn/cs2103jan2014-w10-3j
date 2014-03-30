package goku.action;

import goku.GOKU;
import goku.Result;
import goku.TaskList;

public class UndoAction extends Action {
  private static final String MSG_SUCCESS = "Command undone!";
  private static final String ERR_FAIL = "Failed to undo.";

  private boolean isEmpty = false;

  public UndoAction(GOKU goku) {
    super(goku);
    list = goku.getTaskList();
    // TODO Auto-generated constructor stub
  }

  @Override
  public Result doIt() {
    if (undoCommand() == false) {
      return new Result(false, null, ERR_FAIL, goku.getTaskList()
          .getAllIncomplete());
    } else {
      return new Result(true, MSG_SUCCESS, null, goku.getTaskList()
          .getAllIncomplete());
    }
  }

  public boolean undoCommand() {
    if (goku.getUndoList().isEmpty()) {
      return isEmpty;
    }

    goku.getRedoList().offer(goku.getTaskList());
    TaskList prevList = goku.getUndoList().pollLast();
    goku.setTaskList(prevList);

    return !isEmpty;
  }
}
