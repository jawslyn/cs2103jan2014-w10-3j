package goku;

import goku.util.DateUtil;
import hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskList implements Iterable<Task> {
  private Integer runningId = 1;
  private List<Integer> unusedId = new ArrayList<Integer>();

  private ObservableList<Task> _list;

  public TaskList() {
    _list = FXCollections.observableArrayList();
  }

  public int addTask(Task task) {
    task.setId(makeId());
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
      Task deleted = deleteTaskById(matches.get(0).getId());
      List<Task> results = new ArrayList<>();
      results.add(deleted);
      return results;
    } else {
      return matches;
    }
  }

  public Task deleteTaskById(int id) {
    int index = getIndexOfTaskById(id);
    return index < 0 ? null : deleteTaskByIndex(index);
  }

  public Task deleteTaskByIndex(int index) {
    Task t = getTaskByIndex(index);
    unusedId.add(t.getId());
    Collections.sort(unusedId);
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
    DateTime now = DateUtil.getNow();
    for (Task task : _list) {
      if ((((task.getStatus()) == null || !task.getStatus()))
          && (task.getDeadline() == null || DateUtil.isEarlierOrOn(now,
              task.getDeadline()))) {
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

  public List<Task> getOverdue() {
    List<Task> incomplete = new ArrayList<Task>();
    DateTime now = DateUtil.getNow();
    for (Task task : _list) {
      if ((((task.getStatus()) == null || !task.getStatus()))) {
        incomplete.add(task);
      }
    }

    List<Task> overdue = new ArrayList<>();
    for (Task task : incomplete) {
      if (DateUtil.isEarlierOrOn(task.getDeadline(), now)) {
        overdue.add(task);
      }
    }
    return overdue;
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

  private int makeId() {
    if (unusedId.size() > 0) {
      return unusedId.remove(0);
    }
    return runningId++;
  }

  public int size() {
    return _list.size();
  }
}
