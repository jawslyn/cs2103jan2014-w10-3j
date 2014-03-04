package goku;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
public class GOKU {

  private TaskList _list;

  public GOKU() {
    _list = new TaskList();
  }

  public void setTaskList(TaskList list) {
    _list = list;
  }

  public TaskList getTaskList() {
    return _list;
  }

}
