//@author A0101232H
package goku.action;

import goku.GOKU;
import goku.Result;
import goku.TaskList;

public class RedoAction extends Action {
  private static final String MSG_SUCCESS = "Command executed: \"%s\"";
  private static final String ERR_FAIL = "Failed to redo.";
  private static final String MSG_HAS_OVERDUE = "[!] You have overdue tasks, \"view overdue\" to see them.";

  private boolean isEmpty = false;

  public RedoAction(GOKU goku) {
    super(goku);
    list = goku.getTaskList();
    // TODO Auto-generated constructor stub
  }

  private String editMsgIfHaveOverdue(String msg) {
    if (goku.getTaskList().getOverdue().size() != 0) {
      msg += System.lineSeparator() + MSG_HAS_OVERDUE;
    }
    return msg;
  }

  @Override
  public Result doIt() {
    if (canRedo() == false) {
      return new Result(false, null, editMsgIfHaveOverdue(ERR_FAIL), goku
          .getTaskList().getAllIncomplete());
    } else {
      return new Result(true, editMsgIfHaveOverdue(String.format(MSG_SUCCESS,
          goku.getRedoInputList().pollLast())), null, goku.getTaskList()
          .getAllIncomplete());
    }
  }

  private void addToUndoList(String input) {
    TaskList currList = new TaskList();
    currList = list.clone();

    goku.getUndoList().offer(currList);
    goku.getUndoInputList().offer(input);
  }

  private boolean canRedo() {
    if (goku.getRedoList().isEmpty() || goku.getRedoInputList().isEmpty()) {
      return isEmpty;
    }

    addToUndoList(goku.getRedoInputList().getLast());
    TaskList prevList = goku.getRedoList().pollLast();
    goku.setTaskList(prevList);

    return !isEmpty;
  }
}
