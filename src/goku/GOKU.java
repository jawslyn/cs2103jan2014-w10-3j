package goku;

import java.util.ArrayList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class GOKU {

  private static ArrayList<Task> allTasks = new ArrayList<Task>();
  private static TaskList _list = new TaskList();

  public Result executeCommand(Command command) {
    Action action = ActionFactory.buildAction(command);
    Result result = action.doIt();
    return result;
  }

  public static ArrayList<Task> getAllTasks() {
    return allTasks;
  }

  public static TaskList getTaskList() {
    return _list;
  }

}
