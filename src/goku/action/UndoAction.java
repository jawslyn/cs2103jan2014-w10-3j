package goku.action;

import goku.GOKU;
import goku.Result;
import goku.TaskList;

public class UndoAction extends Action {
  private static final String MSG_SUCCESS = "Command undone!";
  private static final String ERR_FAIL = "Failed to undo.";

  public UndoAction(GOKU goku) {
    super(goku);
    list = goku.getTaskList();
    // TODO Auto-generated constructor stub
  }

  @Override
  public Result doIt() {
    return undoCommand();
  }

  public Result undoCommand() {
    if (goku.getUndoList().isEmpty()) {
      return new Result(false, null, ERR_FAIL, null);
    }

    goku.getRedoList().offer(goku.getTaskList());
    TaskList prevList = goku.getUndoList().pollLast();
    goku.setTaskList(prevList);

    /*
     * System.out.println(commandToUndo.getCommand()); if
     * (commandToUndo.getCommand().equals(DELETE)) { int id =
     * commandToUndo.getTask().getId(); list.deleteTaskById(id); } else if
     * (commandToUndo.getCommand().equals(ADD)) {
     * System.out.println(commandToUndo.getTask());
     * list.addTask(commandToUndo.getTask()); } else if
     * (commandToUndo.getCommand().equals(EDIT)) { int id =
     * commandToUndo.getTask().getId(); list.deleteTaskById(id);
     * list.addTask(commandToUndo.getTask()); }
     */
    return new Result(true, MSG_SUCCESS, null, null);
  }
}
