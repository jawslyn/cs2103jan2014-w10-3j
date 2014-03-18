package goku;

import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskList implements Iterable<Task> {
  private static Integer count = 0;

  private ObservableList<Task> _list;

  public ObservableList<Task> getObservable() {
    return _list;
  }

  public TaskList() {
    _list = FXCollections.observableArrayList();
  }

  public int addTask(Task task) {
    task.setId(++count);
    boolean success = _list.add(task);
    return success ? task.getId() : -1;
  }
  
  public int addTaskDummy(Task task) {
    boolean success = _list.add(task);
    return success ? task.getId() : -1;
  }

  public boolean addTaskWithoutSettingId(Task task) {
    if (getTaskById(task.getId()) == null) {
      return _list.add(task);
    }
    return false;
  }

  public boolean appendTask(Task task) {
    return _list.add(task);
  }

  public void addUndoTask(Task task) {
    _list.add(task);
  }

  public void clear() {
    _list.clear();
  }

  private TaskList deleteTask(TaskList matches) {
    if (matches.size() == 1) {
      deleteTaskById(matches.getTaskByIndex(0).getId());
      return new TaskList();
    } else {
      return matches;
    }
  }

  public Task deleteTaskById(int id) {
    int index = getIndexOfTaskById(id);
    if (index < 0) {
      return null;
    } else {
      return deleteTaskByIndex(index);
    }
  }

  public Task deleteTaskByIndex(int index) {
    return _list.remove(index);
  }

  public TaskList deleteTaskByTitle(Task toDelete) {
    TaskList matches = findTaskByTitle(toDelete);
    return deleteTask(matches);
  }

  public TaskList findTaskByDeadline(Task toFind) {
    TaskList matches = new TaskList();
    for (Task task : _list) {
      if (task.getDeadline() == null) {
        continue;
      }
      if (task.isDueOn(toFind)) {
        matches.appendTask(task);
      }
    }
    return matches;
  }

  public TaskList findTaskByPeriod(Task toFind) {
    TaskList matches = new TaskList();
    for (Task task : _list) {
      if (task.getDateRange() == null && task.getDeadline() == null) {
        continue;
      } else if (task.getDeadline() != null
          && toFind.inPeriod(task.getDeadline())) { // deadline falls within
                                                    // period
        matches.appendTask(task);
        continue;
      } else if (task.getDateRange() != null
          && (toFind.inPeriod(task.getDateRange().startDate) || toFind
              .inPeriod(task.getDateRange().endDate))) { // start date of
        // period falls
        // within search
        // period
        matches.appendTask(task);
        continue;
      }
    }

    return matches;
  }

  public TaskList findTaskByTitle(Task toFind) {
    TaskList matches = new TaskList();
    for (Task task : _list) {
      if (task.titleMatches(toFind)) {
        matches.appendTask(task);
      }
    }
    return matches;
  }

  public TaskList getAll() {
    return this;
  }

  public TaskList getAllCompleted() {
    TaskList result = new TaskList();
    for (Task task : _list) {
      if (task.getStatus()) {
        result.addTaskDummy(task);
      }
    }
    return result;
  }

  public TaskList getAllIncomplete() {
    TaskList result = new TaskList();
    for (Task task : _list) {
      if ((task.getStatus()) == null || !task.getStatus()) {
        result.addTaskDummy(task);
      }
    }
    return result;
  }

  public ObservableList<Task> getArrayList() {
    return _list;
    // return (ArrayList<Task>) Arrays
    // .asList(_list.toArray(new Task[_list.size()]));
  }

  private int getIndexOfTaskById(int id) {
    return _list.indexOf(getTaskById(id));
  }

  /*
   * @param id
   * 
   * @returns the task with specified id
   */
  public Task getTaskById(int id) {
    for (Task task : _list) {
      if (task.getId() == id) {
        return task;
      }
    }
    return null;
  }

  protected Task getTaskByIndex(int index) {
    return _list.get(index);
  }

  @Override
  public Iterator<Task> iterator() {
    return _list.listIterator();
  }

  public int size() {
    return _list.size();
  }

  public TaskList findTaskByTitle(String title) {
    Task toFind = new Task();
    toFind.setTitle(title);
    TaskList matches = new TaskList();
    for (Task task : _list) {
      if (task.titleMatches(toFind)) {
        matches.appendTask(task);
      }
    }
    return matches;
  }

}
