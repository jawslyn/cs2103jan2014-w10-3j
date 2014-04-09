package goku;

import goku.util.DateUtil;
import goku.util.InvalidDateRangeException;
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
    if (task.getId() == null) {
      task.setId(makeId());
    } else if (task.getId() == 0) {
      task.setId(0);
    } else {
      task.setId(makeId());
    }
    boolean success = _list.add(task);
    return success ? task.getId() : -1;
  }

  public boolean addTaskWithoutSettingId(Task task) {
    // if (getTaskById(task.getId()) == null) {
    return _list.add(task);
    // }
    // return false;
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

    cloned.runningId = runningId;
    cloned.unusedId = unusedId;

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

  public void editCompletedTaskById(int id) {
    unusedId.add(id);
    Collections.sort(unusedId);
  }

  public List<Task> findTasksOnDay(DateTime dateQuery) {
    List<Task> matches = new ArrayList<>();
    for (Task task : _list) {
      if (task.isOn(dateQuery)) {
        matches.add(task);
      }
    }
    return matches;
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

  public List<Task> findTaskByTitleExactly(String title) {
    List<Task> matches = new ArrayList<>();
    for (Task task : _list) {
      if (task.titleMatchesExactly(title)) {
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
          && ((task.getDeadline() == null || DateUtil.isEarlierOrOn(now,
              task.getDeadline())) && (task.getDateRange() == null || DateUtil
              .isEarlierOrOn(now, task.getEndDate())))) {
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
      if ((task.getDeadline() != null && DateUtil.isEarlierOrOn(
          task.getDeadline(), now))
          || (task.getDateRange() != null && (DateUtil.isEarlierOrOn(
              task.getEndDate(), now)))) {
        overdue.add(task);
      }
    }
    return overdue;
  }

  public Integer getRunningId() {
    return runningId;
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

  public List<Integer> getUnusedId() {
    return unusedId;
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

  public List<String> findFreeSlots(DateTime date)
      throws InvalidDateRangeException {
    assert date.getHour() == null;

    List<String> resultList = new ArrayList<String>();
    ArrayList<DateRange> periodListOfDate = new ArrayList<DateRange>();

    for (Task task : _list) {
      if (DateUtil.periodClashesWithDay(task.getDateRange(), date)) {
        periodListOfDate.add(task.getDateRange());
      }
    }

    if (periodListOfDate.size() == 0) {
      resultList
          .add(timeSlotFormatter(date.getStartOfDay(), date.getEndOfDay()));
    } else {
      resultList = findFreeSlots(periodListOfDate, date);
    }

    return resultList;
  }

  private List<String> findFreeSlots(ArrayList<DateRange> periodList,
      DateTime date) throws InvalidDateRangeException {
    List<String> result = new ArrayList<String>();
    List<DateTime> periodTokens = new ArrayList<DateTime>();

    periodList = mergeOverlapPeriods(periodList);

    for (DateRange period : periodList) {
      if (DateUtil.isSameDay(period.getStartDate(), date)) {
        periodTokens.add(period.getStartDate());
      } else {
        periodTokens.add(date.getStartOfDay());
      }
      if (DateUtil.isSameDay(date, period.getEndDate())) {
        periodTokens.add(period.getEndDate());
      } else {
        periodTokens.add(date.getEndOfDay());
      }
    }
    assert periodTokens.size() % 2 == 0;

    Collections.sort(periodTokens);

    for (int i = -1; i <= periodTokens.size(); i = i + 2) {
      // boundary case 1 (first iteration)
      if (i == -1) {
        if (DateUtil
            .isSameDayAndTime(date.getStartOfDay(), periodTokens.get(0))) {
          continue;
        }
        result.add(timeSlotFormatter(date.getStartOfDay(), periodTokens.get(0)
            .minus(0, 0, 0, 0, 1, 0, 0, DateTime.DayOverflow.Spillover)));
      } else if (i == periodTokens.size() - 1) { // boundary case (end case)
        if (DateUtil.isSameDayAndTime(periodTokens.get(i), date.getEndOfDay())) {
          continue;
        }
        result.add(timeSlotFormatter(periodTokens.get(i), date.getEndOfDay()));
      } else {
        result.add(timeSlotFormatter(
            periodTokens.get(i),
            periodTokens.get(i + 1).minus(0, 0, 0, 0, 1, 0, 0,
                DateTime.DayOverflow.Spillover)));
      }
    }

    return result;
  }

  private ArrayList<DateRange> mergeOverlapPeriods(
      ArrayList<DateRange> periodList) throws InvalidDateRangeException {
    for (int i = 0; i < periodList.size() - 1; i++) {
      for (int j = i + 1; j < periodList.size(); j++) {
        DateRange periodA = periodList.get(i);
        DateRange periodB = periodList.get(j);
        assert periodA != null && periodB != null;

        if (periodA.intersectsWith(periodB)) {
          DateTime start, end;
          if (DateUtil.isEarlierOrOn(periodA.getStartDate(),
              periodB.getStartDate())) {
            start = periodA.getStartDate();
          } else {
            start = periodB.getStartDate();
          }

          if (DateUtil
              .isEarlierOrOn(periodB.getEndDate(), periodA.getEndDate())) {
            end = periodA.getEndDate();
          } else {
            end = periodB.getEndDate();
          }

          periodList.add(new DateRange(start, end));
          periodList.remove(periodA);
          periodList.remove(periodB);

          if (periodList.size() == 1) {
            return periodList;
          } else {
            i = -1;
            break;
          }
        }
      }
    }

    return periodList;
  }

  private String timeSlotFormatter(DateTime start, DateTime end) {
    assert DateUtil.isEarlierOrOn(start, end);

    String formatStart = String.format("%02d", start.getHour()) + ":"
        + String.format("%02d", start.getMinute());
    String formatEnd = String.format("%02d", end.getHour()) + ":"
        + String.format("%02d", end.getMinute());

    return "[" + formatStart + " - " + formatEnd + "]";
  }

  public boolean hasClash(Task task) {
    if (task.getDateRange() != null) {
      for (Task taskFromList : _list) {
        if (taskFromList.getDateRange() != null) {
          if (DateUtil.isLaterThan(task.getEndDate(),
              taskFromList.getStartDate())
              && DateUtil.isEarlierThan(task.getStartDate(),
                  taskFromList.getEndDate())) {
            return true;
          }
        }
      }
    }
    return false;
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

  public void setRunningId(Integer id) {
    runningId = id;
  }

  public void setUnusedId(List<Integer> idList) {
    unusedId = idList;
  }

  public int size() {
    return _list.size();
  }
}
