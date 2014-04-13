//@author A0101232H
package goku;

import java.util.Deque;
import java.util.LinkedList;

import javafx.collections.ObservableList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
public class GOKU {

  private static TaskList _list;
  private Deque<TaskList> undoList;
  private Deque<String> undoInputList;
  private Deque<TaskList> redoList;
  private Deque<String> redoInputList;

  public GOKU() {
    _list = new TaskList();
    undoList = new LinkedList<TaskList>();
    undoInputList = new LinkedList<String>();
    redoList = new LinkedList<TaskList>();
    redoInputList = new LinkedList<String>();
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

  public Deque<String> getUndoInputList() {
    return undoInputList;
  }

  public Deque<String> getRedoInputList() {
    return redoInputList;
  }

  public void setTaskList(TaskList list) {
    _list = list;
  }

  public void addToTaskList(TaskList list) {
    for (Task task : list) {
      _list.addTask(task);
    }
  }
}
