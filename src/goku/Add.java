package goku;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Add extends Action {
  private static final String SUCCESS_MSG = "added \"%s\"";
  private static final String FAILURE_MSG = "fail add \"%s\"";
  private Task task;

  /*
   * Called by ActionFactory on all actions to build the needed objects for this
   * Action
   */
  @Override
  public void construct() {
    task = command.getTask();
  }

  @Override
  Result doIt() {
    return addTask(task);
  }

  public Result addTask(Task task) {
    GOKU.getAllTasks().add(task);
    return new Result(true, getSuccessMsg(SUCCESS_MSG, task.getTitle()), null,
        GOKU.getAllTasks());
  }

}