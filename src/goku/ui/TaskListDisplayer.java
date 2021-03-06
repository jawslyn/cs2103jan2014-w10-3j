//@author A0099903R
package goku.ui;

import goku.DateRange;
import goku.Task;
import goku.util.DateUtil;
import hirondelle.date4j.DateTime;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

public class TaskListDisplayer {
  PrintStream ps;

  public TaskListDisplayer(PrintStream ps) {
    this.ps = ps;
  }

  public Hashtable<String, List<Task>> build(List<Task> list) {
    Hashtable<String, List<Task>> ht = new Hashtable<>();
    if (list == null) {
      return ht;
    }

    ArrayList<Task> overdue = new ArrayList<Task>();
    ArrayList<Task> today = new ArrayList<Task>();
    ArrayList<Task> tomorrow = new ArrayList<Task>();
    ArrayList<Task> remaining = new ArrayList<Task>();
    ArrayList<Task> completed = new ArrayList<Task>();

    for (Task task : list) {
      if (task.isDone() != null && task.isDone()) {
        completed.add(task);
      } else if (isOver(task)) {
        overdue.add(task);
      } else if (isToday(task)) {
        today.add(task);
      } else if (isTomorrow(task)) {
        tomorrow.add(task);
      } else {
        remaining.add(task);
      }
    }

    // sort lists for display
    Collections.sort(completed);
    Collections.sort(overdue);
    Collections.sort(today);
    Collections.sort(tomorrow);
    Collections.sort(remaining);

    ht.put("completed", completed);
    ht.put("overdue", overdue);
    ht.put("today", today);
    ht.put("tomorrow", tomorrow);
    ht.put("remaining", remaining);
    return ht;
  }

  // @author A0101232H
  private boolean isOver(DateRange dateRange) {
    if (dateRange == null) {
      return false;
    }
    return isOver(dateRange.getEndDate());
  }

  public boolean isOver(DateTime date) {
    if (date == null) {
      return false;
    }
    return date.isInThePast(TimeZone.getDefault());
  }

  private boolean isOver(Task task) {
    return isOver(task.getDateRange()) || isOver(task.getDeadline());
  }

  private boolean isToday(DateRange dateRange) {
    if (dateRange == null) {
      return false;
    }
    DateTime now = DateUtil.getNow();

    return DateUtil.isLaterOrOn(dateRange.getStartDate(), now.getStartOfDay())
        && DateUtil.isEarlierOrOn(dateRange.getEndDate(), now.getEndOfDay());
  }

  private boolean isToday(DateTime date) {
    if (date == null) {
      return false;
    }
    return DateUtil.getNow().isSameDayAs(date);
  }

  private boolean isToday(Task task) {
    return isToday(task.getDeadline()) || isToday(task.getDateRange());
  }

  private boolean isTomorrow(DateRange dateRange) {
    if (dateRange == null) {
      return false;
    }
    return DateUtil.getNow().plusDays(1).isSameDayAs(dateRange.getStartDate());
  }

  private boolean isTomorrow(DateTime date) {
    if (date == null) {
      return false;
    }
    return DateUtil.getNow().plusDays(1).isSameDayAs(date);

  }

  private boolean isTomorrow(Task task) {
    return isTomorrow(task.getDeadline()) || isTomorrow(task.getDateRange());
  }
}
