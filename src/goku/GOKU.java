package goku;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
public class GOKU {

  private static TaskList _list;

  public GOKU() {
    _list = new TaskList();
  }

  public void setTaskList(TaskList list) {
    _list = list;
  }

  public Result executeCommand(Command command) {
    Action action = ActionFactory.buildAction(command);
    Result result = action.doIt();
    return result;
  }

  public static TaskList getTaskList() {
    return _list;
  }

}
