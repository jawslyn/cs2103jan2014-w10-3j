package goku;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Add extends Action {
  private static final String MSG_SUCCESS = "Added: \"%s\"";
  private static final String ERR_FAIL = "Fail to add: \"%s\"";
  private static final String ERR_TASK_NO_NAME = "Task must have a name!";
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
  public Result doIt() {
    if (!task.isValid()) {
      return unableToAddTaskWithoutName();
    } else {
      return addTask();
    }

  }

  private Result addTask() {
    boolean success = list.addTask(task);
    if (success) {
      return successAddTask();
    } else {
      return failedToAddTask();
    }
  }

  private Result successAddTask() {
    Result result = Result.makeSuccessResult();
    result.setSuccessMsg(String.format(MSG_SUCCESS, task.getTitle()));
    result.setTasks(list);
    return result;
  }

  private Result unableToAddTaskWithoutName() {
    Result result = Result.makeFailureResult();
    result.setErrorMsg(ERR_TASK_NO_NAME);
    return result;
  }

  private Result failedToAddTask() {
    Result result = Result.makeFailureResult();
    result.setErrorMsg(String.format(ERR_FAIL, task.getTitle()));
    return result;
  }
}