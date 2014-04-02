package goku.ui;

import goku.DateRange;
import goku.Task;
import goku.util.DateUtil;
import hirondelle.date4j.DateTime;

import java.io.PrintStream;
import java.util.ArrayList;
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

    ht.put("completed", completed);
    ht.put("overdue", overdue);
    ht.put("today", today);
    ht.put("tomorrow", tomorrow);
    ht.put("remaining", remaining);
    return ht;
  }

  public void display(List<Task> list) {
    if (list == null) {
      return;
    }
    ArrayList<Task> past = new ArrayList<Task>();
    ArrayList<Task> today = new ArrayList<Task>();
    ArrayList<Task> tomorrow = new ArrayList<Task>();
    ArrayList<Task> remaining = new ArrayList<Task>();

    for (Task task : list) {
      if (isOver(task)) {
        past.add(task);
      } else if (isToday(task)) {
        today.add(task);
      } else if (isTomorrow(task)) {
        tomorrow.add(task);
      } else {
        remaining.add(task);
      }
    }

    System.out.println("---Today---");
    for (Task task : today) {
      System.out.println(task.toString());
    }
    System.out.println("---Tomorrow---");
    for (Task task : tomorrow) {
      System.out.println(task.toString());
    }
    System.out.println("---Coming Up---");
    for (Task task : remaining) {
      System.out.println(task.toString());
    }
    System.out.println("---Previously---");
    for (Task task : past) {
      System.out.println(task.toString());
    }
  }

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
    return dateRange.containsDate(DateUtil.getNow());
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
