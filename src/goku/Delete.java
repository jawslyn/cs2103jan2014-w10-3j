package goku;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Delete extends Action {
  private static final String DELETE_SUCCESS = "deleted \"%s\"";
  private static final String DELETE_FAILURE = "fail to delete \"%s\"";

  public Delete() {
  }

  public Result deleteTask(Task task) {
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      if (GOKU.getAllTasks().get(i) == task) {
        GOKU.getAllTasks().remove(i);
      }
    }

    return new Result(true, String.format(DELETE_SUCCESS, task.getTitle()),
        null, GOKU.getAllTasks());
  }

  @Override
  Result doIt() {
    return deleteTask(command.getTask());
  }

  @Override
  String getSuccessMsg(String msg, Object... args) {
    return String.format(msg, args);
  }

  @Override
  String getErrorMsg(String msg, Object... args) {
    return String.format(msg, args);
  }

  public void setCommand(Command command) {
    this.command = command;
  }

}