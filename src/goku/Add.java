package goku;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Add extends Action {
  private static final String SUCCESS_MSG = "added \"%s\"";
  private static final String FAILURE_MSG = "fail add \"%s\"";

  public Add() {

  }

  public void setCommand(Command c) {
    this.command = c;
  }

  public Result addTask(Task task) {
    GOKU.getAllTasks().add(task);
    return new Result(true, getSuccessMsg(SUCCESS_MSG, task.getTitle()), null,
        GOKU.getAllTasks());
  }

  @Override
  Result doIt() {
    Task task = command.getTask();
    return addTask(task);
  }

  @Override
  String getSuccessMsg(Object... args) {
    return String.format(SUCCESS_MSG, args);
  }

  @Override
  String getErrorMsg(Object... args) {
    return String.format(FAILURE_MSG, args);
  }

}