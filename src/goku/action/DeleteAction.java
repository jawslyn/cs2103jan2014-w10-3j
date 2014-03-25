package goku.action;

import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;

import java.util.List;

/* Delete removes a task from GOKU.
 * It does so in 2 steps:
 * 1 - Tries to find a unique match based on the given Task,
 *    the match can be based on Title, or Id
 * 2a- If a unique match is received, it removes the Task,
 *      returning a success Result with TaskList.size() == 1
 * 2b- If no match or multiple matches,
 *      it returns a failure Result with TaskList.size() == 0
 * 2c- If multiple matches,
 *      returns a failure Result with TaskList.size() > 1
 */
public class DeleteAction extends Action {
  private static final String MSG_SUCCESS = "Deleted \"%s\"";
  private static final String NO_MATCHES = "No matches found";
  private static final String ERR_FAILURE = "Many matches found for \"%s\".";
  private static final String ERR_NOT_FOUND = "Cannot find \"%s\".";
  public static final String ERR_INSUFFICIENT_ARGS = "Can't delete. Need an ID. Try \"delete 1\"";

  public int id;

  public String title;

  public DeleteAction(GOKU goku) {
    super(goku);
  }

  public void addToUndoList() {
    TaskList currList = new TaskList();
    for (Task t : list.getArrayList()) {
      currList.addTaskWithoutSettingId(t);
    }
    goku.getUndoList().offer(currList);
  }

  public Result deleteTask() {
    addToUndoList();
    boolean success;
    success = tryDeleteById();
    if (success) {
      return new Result(true, String.format(MSG_SUCCESS, id), null, null);
    }
    if (title == null) {
      return new Result(false, null, String.format(ERR_NOT_FOUND, id), null);
    }
    Task task = new Task();
    task.setTitle(title);
    if (list.findTaskByTitle(title).size() == 0) {
      return new Result(false, null, NO_MATCHES, null);
    }
    List<Task> possibleDeletion = list.deleteTaskByTitle(title);
    if (possibleDeletion.size() == 0) {
      return new Result(true, String.format(MSG_SUCCESS, task.getTitle()),
          null, null);
    } else {
      return new Result(false, null, String.format(ERR_FAILURE, title),
          possibleDeletion);
    }

  }

  @Override
  public Result doIt() {
    return deleteTask();
  }

  private boolean tryDeleteById() {
    Task deleted = list.deleteTaskById(id);
    return deleted != null;
  }

}
