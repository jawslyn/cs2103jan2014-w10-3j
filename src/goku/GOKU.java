package goku;

import java.util.LinkedList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
public class GOKU {

  private TaskList _list;
  private LinkedList<TaskList> undoList;

  public GOKU() {
    _list = new TaskList();
    undoList = new LinkedList<TaskList>();
  }

  public TaskList getTaskList() {
    return _list;
  }

  public LinkedList<TaskList> getUndoList() {
    return undoList;
  }

  public void setTaskList(TaskList list) {
    _list = list;
  }

}
