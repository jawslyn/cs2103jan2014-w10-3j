package goku;

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
class Delete extends Action {
  private static final String MSG_SUCCESS = "Deleted \"%s\"";
  private static final String ERR_FAILURE = "Many matches found for \"%s\".";
  private static final String ERR_NOT_FOUND = "Cannot find \"%s\".";
  private Task task;

  /*
   * Called by ActionFactory on all actions to build the needed objects for this
   * Action
   */
  @Override
  public void construct() {
    this.task = command.getTask();
  }

  @Override
  Result doIt() {
    return deleteTask();
  }

  public Result deleteTask() {
    boolean success;
    success = tryDeleteById();
    if (success) {
      return new Result(true, String.format(MSG_SUCCESS, task.getId()
          .toString()), null, null);
    }
    if (task.getTitle() == null) {
      return new Result(false, null, String.format(ERR_NOT_FOUND, task.getId()
          .toString()), null);
    }
    TaskList possibleDeletion = list.deleteTaskByTitle(task);
    if (possibleDeletion.size() == 0) {
      return new Result(true, String.format(MSG_SUCCESS, task.getTitle()),
          null, null);
    } else {
      return new Result(false, null,
          String.format(ERR_FAILURE, task.getTitle()), possibleDeletion);
    }
  }

  private TaskList tryDeleteByTitle() {
    TaskList possibleDeletion = list.deleteTaskByTitle(task);
    return possibleDeletion;
  }

  private boolean tryDeleteById() {
    Task deleted = list.deleteTaskById(task.getId());
    return deleted != null;
  }

}