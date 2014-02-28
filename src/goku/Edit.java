package goku;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Edit extends Action {
  private final String MSG_SUCCESS = "edited task";
  private final String MSG_FAILURE = "unable to edit";

  private int taskId;
  private Task taskWithEdits;

  @Override
  Result doIt() {
    return updateTask();
  }

  /*
   * Called by ActionFactory on all actions to build the needed objects for this
   * Action
   */
  @Override
  public void construct() {
    taskWithEdits = command.getTask();
    taskId = taskWithEdits.getId();
  }

  public Result updateTask() {
    int id = taskWithEdits.getId();
    Task t = list.getTaskById(id);
    t.updateWith(taskWithEdits);
    return new Result(true, MSG_SUCCESS, null, null);
  }
}
