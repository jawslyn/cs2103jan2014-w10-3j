//@author A0101232H
package goku.action;

import goku.GOKU;
import goku.Result;
import goku.TaskList;

public class UndoAction extends Action {
  private static final String MSG_SUCCESS = "Command undone: \"%s\"";
  private static final String ERR_FAIL = "Failed to undo.";
  private static final String MSG_HAS_OVERDUE = "[!] You have overdue tasks, \"view overdue\" to see them.";

  private boolean isEmpty = false;

  public UndoAction(GOKU goku) {
    super(goku);
    list = goku.getTaskList();
    // TODO Auto-generated constructor stub
  }

  @Override
  public Result doIt() {
    if (canUndo() == false) {
      return new Result(false, null, editMsgIfHaveOverdue(ERR_FAIL), goku
          .getTaskList().getAllIncomplete());
    } else {
      return new Result(true, editMsgIfHaveOverdue(String.format(MSG_SUCCESS,
          goku.getUndoInputList().pollLast())), null, goku.getTaskList()
          .getAllIncomplete());
    }
  }

  private String editMsgIfHaveOverdue(String msg) {
    if (goku.getTaskList().getOverdue().size() != 0) {
      msg += System.lineSeparator() + MSG_HAS_OVERDUE;
    }
    return msg;
  }

  private void addToRedoList(String input) {
    TaskList currList = new TaskList();
    currList = list.clone();

    goku.getRedoInputList().offer(input);
    goku.getRedoList().offer(currList);
  }

  private boolean canUndo() {
    if (goku.getUndoList().isEmpty() || goku.getUndoInputList().isEmpty()) {
      return isEmpty;
    }

    addToRedoList(goku.getUndoInputList().getLast());
    TaskList prevList = goku.getUndoList().pollLast();
    goku.setTaskList(prevList);

    return !isEmpty;
  }
}
