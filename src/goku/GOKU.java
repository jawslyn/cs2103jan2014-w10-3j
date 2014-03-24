package goku;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.ObservableList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
public class GOKU {

  private static TaskList _list;
  private Deque<TaskList> undoList;
  private Deque<TaskList> redoList;

  public GOKU() {
    _list = new TaskList();
    undoList = new LinkedList<TaskList>();
    redoList = new LinkedList<TaskList>();
  }

  public TaskList getTaskList() {
    return _list;
  }

  public ObservableList<Task> getObservable() {
    return _list.getObservable();
  }

  public Deque<TaskList> getUndoList() {
    return undoList;
  }

  public Deque<TaskList> getRedoList() {
    return redoList;
  }

  public void setTaskList(TaskList list) {
    _list = list;
  }

}
