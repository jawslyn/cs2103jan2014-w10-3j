package goku;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Add extends Action {
  private static final String SUCCESS_ADD = "added \"%s\"";
  private static final String FAILURE_ADD = "fail add \"%s\"";

  public Add() {

  }

  public void setCommand(Command c) {
    this.command = c;
  }

  public Result addTask(Task task) {
    // ArrayList<Task> list = GOKU.getAllTasks();
    // list.add(task);
    GOKU.getAllTasks().add(task);
    return new Result(true, getSuccessMsg(SUCCESS_ADD, task.getTitle()), null,
        GOKU.getAllTasks());

  }

  @Override
  Result doIt() {
    Task task = command.getTask();

    return addTask(task);
  }

  @Override
  String getSuccessMsg(String msg, Object... args) {
    return String.format(SUCCESS_ADD, args);
  }

  @Override
  String getErrorMsg(String msg, Object... args) {
    return String.format(FAILURE_ADD, args);
  }

}