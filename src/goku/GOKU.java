package goku;

import java.util.LinkedList;

import javafx.collections.ObservableList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
public class GOKU {

  private static TaskList _list;
  private LinkedList<TaskList> undoList;
  private LinkedList<TaskList> redoList;

  public GOKU() {
    _list = new TaskList();
    undoList = new LinkedList<TaskList>();
    redoList = new LinkedList<TaskList>();
  }

  public LinkedList<TaskList> getRedoList() {
    return redoList;
  }

  public TaskList getTaskList() {
    return _list;
  }

  public static ObservableList<Task> getObservableStatic() {
    return _list.getObservable();
  }

  public ObservableList<Task> getObservable() {
    return _list.getObservable();
  }

  public LinkedList<TaskList> getUndoList() {
    return undoList;
  }

  public void setTaskList(TaskList list) {
    _list = list;
  }

}
