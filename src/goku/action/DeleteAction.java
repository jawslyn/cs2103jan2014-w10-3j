package goku.action;

import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;

import java.util.ArrayList;
import java.util.Collections;
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
  private static final String MSG_SUCCESS = "Deleted [%s] %s. *hint* undo to undo ;)";
  private static final String NO_MATCHES = "No matches found!";
  private static final String ERR_FAILURE = "Many matches found for \"%s\".";
  private static final String ERR_NOT_FOUND = "Cannot find \"%s\".";
  public static final String ERR_INSUFFICIENT_ARGS = "Can't delete. Need an ID. Try \"delete 1\"";
  private static final String MSG_HAS_OVERDUE = "[!] You have overdue tasks, \"view overdue\" to see them.";

  public Integer id;

  public String title;

  public DeleteAction(GOKU goku) {
    super(goku);
  }

  public void addToUndoList() {
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
    goku.getUndoList().offer(currList);
  }

  public Result deleteTask() {
    addToUndoList();
    Task deletedTask = tryDeleteById();
    if (deletedTask != null) {
      return new Result(true, editMsgIfHaveOverdue(String.format(MSG_SUCCESS,
          id, deletedTask.getTitle())), null, list.getAllIncomplete());
    }
    List<Task> possibleDeletion = list.deleteTaskByTitle(title);
    if (possibleDeletion.size() == 0) {
      return new Result(false, null, editMsgIfHaveOverdue(NO_MATCHES),
          list.getAllIncomplete());
    } else if (possibleDeletion.size() == 1) {
      deletedTask = possibleDeletion.get(0);
      return new Result(true, editMsgIfHaveOverdue(String.format(MSG_SUCCESS,
          deletedTask.getId(), deletedTask.getTitle())), null,
          list.getAllIncomplete());
    } else {
      return new Result(false, null, editMsgIfHaveOverdue(String.format(
          ERR_FAILURE, title)), possibleDeletion);
    }

  }

  @Override
  public Result doIt() {
    return deleteTask();
  }

  public String editMsgIfHaveOverdue(String msg) {
    if (list.getOverdue().size() != 0) {
      msg += System.lineSeparator() + MSG_HAS_OVERDUE;
    }
    return msg;
  }

  private Task tryDeleteById() {
    if (id == null) {
      return null;
    }
    return list.deleteTaskById(id);
  }

}
