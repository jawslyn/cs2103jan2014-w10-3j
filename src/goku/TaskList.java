package goku;

import goku.util.DateUtil;
import hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskList implements Iterable<Task> {
  private static Integer count = 0;

  private ObservableList<Task> _list;

  public TaskList() {
    _list = FXCollections.observableArrayList();
  }

  public int addTask(Task task) {
    task.setId(++count);
    boolean success = _list.add(task);
    return success ? task.getId() : -1;
  }

  public boolean addTaskWithoutSettingId(Task task) {
    if (getTaskById(task.getId()) == null) {
      return _list.add(task);
    }
    return false;
  }

  public List<Task> asList() {
    return _list;
  }

  public void clear() {
    _list.clear();
  }

  @Override
  public TaskList clone() {
    TaskList cloned = new TaskList();
    for (Task task : _list) {
      cloned.addTaskWithoutSettingId(task);
    }
    return cloned;
  }

  private List<Task> deleteTask(List<Task> matches) {
    if (matches.size() == 1) {
      deleteTaskById(matches.get(0).getId());
      return new ArrayList<Task>();
    } else {
      return matches;
    }
  }

  public Task deleteTaskById(int id) {
    int index = getIndexOfTaskById(id);
    return index < 0 ? null : deleteTaskByIndex(index);
  }

  public Task deleteTaskByIndex(int index) {
    return _list.remove(index);
  }

  public List<Task> deleteTaskByTitle(String title) {
    return deleteTask(findTaskByTitle(title));
  }

  public List<Task> findTaskByDeadline(DateTime deadline) {
    List<Task> matches = new ArrayList<>();
    for (Task task : _list) {
      if (task.isDueOn(deadline)) {
        matches.add(task);
      }
    }
    return matches;
  }

  public List<Task> findTaskByPeriod(DateRange range) {
    List<Task> matches = new ArrayList<>();
    for (Task task : _list) {
      if (range.containsDate(task.getDeadline())
          || range.intersectsWith(task.getDateRange())) {
        matches.add(task);
      }
    }
    return matches;
  }

  public List<Task> findTaskByTitle(String title) {
    List<Task> matches = new ArrayList<>();
    for (Task task : _list) {
      if (task.titleMatches(title)) {
        matches.add(task);
      }
    }
    return matches;
  }

  public List<Task> getAllCompleted() {
    List<Task> completed = new ArrayList<Task>();
    for (Task task : _list) {
      if (task.getStatus() != null && task.getStatus()) {
        completed.add(task);
      }
    }
    return completed;
  }

  public List<Task> getAllIncomplete() {
    List<Task> incomplete = new ArrayList<Task>();
    for (Task task : _list) {
      if ((task.getStatus()) == null || !task.getStatus()) {
        incomplete.add(task);
      }
    }
    return incomplete;
  }

  public ObservableList<Task> getArrayList() {
    return _list;
  }

  private int getIndexOfTaskById(int id) {
    return _list.indexOf(getTaskById(id));
  }

  public ObservableList<Task> getObservable() {
    return _list;
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

  public boolean isFree(DateTime dateTime) {

    boolean result = true;

    assert (dateTime != null);

    for (Task task : _list) {
      if (task.getDateRange() != null) {
        DateTime taskStartDate = task.getDateRange().getStartDate();
        DateTime taskEndDate = task.getDateRange().getEndDate();

        if (DateUtil.isEarlierOrOn(dateTime, taskEndDate)
            && DateUtil.isLaterOrOn(dateTime, taskStartDate)) {
          result = false;
          break;
        }
      }
    }

    return result;
  }

  @Override
  public Iterator<Task> iterator() {
    return _list.listIterator();
  }

  public int size() {
    return _list.size();
  }
}
