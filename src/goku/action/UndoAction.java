package goku.action;

import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UndoAction extends Action {
  private static final String MSG_SUCCESS = "Command undone!";
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
    if (undoCommand() == false) {
      return new Result(false, null, editMsgIfHaveOverdue(ERR_FAIL), goku
          .getTaskList().getAllIncomplete());
    } else {
      return new Result(true, editMsgIfHaveOverdue(MSG_SUCCESS), null, goku
          .getTaskList().getAllIncomplete());
    }
  }

  public String editMsgIfHaveOverdue(String msg) {
    if (list.getOverdue().size() != 0) {
      msg += System.lineSeparator() + MSG_HAS_OVERDUE;
    }
    return msg;
  }

  public void addToRedoList() {
    TaskList currList = new TaskList();
    for (Task t : list.getArrayList()) {
      currList.addTaskWithoutSettingId(t);
    }

    List<Integer> idList = new ArrayList<Integer>();
    for (Integer id : list.getUnusedId()) {
      idList.add(id);
    }

    Collections.sort(idList);
    currList.setRunningId(list.getRunningId());
    currList.setUnusedId(idList);
    goku.getRedoList().offer(currList);
  }

  public boolean undoCommand() {
    if (goku.getUndoList().isEmpty()) {
      return isEmpty;
    }

    addToRedoList();
    TaskList prevList = goku.getUndoList().pollLast();
    goku.setTaskList(prevList);

    return !isEmpty;
  }
}
