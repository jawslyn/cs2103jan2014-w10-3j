package goku.action;

import goku.GOKU;
import goku.Result;
import goku.TaskList;

public class RedoAction extends Action {
  private static final String MSG_SUCCESS = "Command undone!";
  private static final String ERR_FAIL = "Failed to undo.";

  public RedoAction(GOKU goku) {
    super(goku);
    list = goku.getTaskList();
    // TODO Auto-generated constructor stub
  }

  @Override
  public Result doIt() {
    return redoCommand();
  }

  public Result redoCommand() {
    if (goku.getRedoList().isEmpty()) {
      return new Result(false, null, ERR_FAIL, null);
    }

    goku.getUndoList().offer(goku.getTaskList());
    TaskList prevList = goku.getRedoList().pollLast();
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
