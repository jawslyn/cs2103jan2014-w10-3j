package goku.ui;

import goku.DateRange;
import goku.DateUtil;
import goku.Task;
import goku.TaskList;
import hirondelle.date4j.DateTime;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class TaskListDisplayer {
  PrintStream ps;

  public TaskListDisplayer(PrintStream ps) {
    this.ps = ps;
  }

  DateTime now = DateUtil.getNow();
  DateTime tmr = now.plusDays(1);

  public void display(TaskList list) {
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

  private boolean isTomorrow(Task task) {
    return isTomorrow(task.getDeadline()) || isTomorrow(task.getDateRange());
  }

  private boolean isTomorrow(DateRange dateRange) {
    if (dateRange == null) {
      return false;
    }
    return tmr.isSameDayAs(DateUtil.date4j(dateRange.getStartDate()));
  }

  private boolean isTomorrow(Date date) {
    if (date == null) {
      return false;
    }
    return tmr.isSameDayAs(DateUtil.date4j(date));

  }

  private boolean isToday(Task task) {
    return isToday(task.getDeadline()) || isToday(task.getDateRange());
  }

  private boolean isToday(DateRange dateRange) {
    if (dateRange == null) {
      return false;
    }
    return isToday(dateRange.getEndDate());
  }

  private boolean isToday(Date date) {
    if (date == null) {
      return false;
    }
    return now.isSameDayAs(DateUtil.date4j(date));
  }

  private boolean isOver(Task task) {
    return isOver(task.getDateRange()) || isOver(task.getDeadline());
  }

  private boolean isOver(DateRange dateRange) {
    if (dateRange == null) {
      return false;
    }
    return isOver(dateRange.getEndDate());
  }

  public boolean isOver(Date date) {
    if (date == null) {
      return false;
    }
    DateTime dt = DateUtil.date4j(date);
    return dt.isInThePast(TimeZone.getDefault());
  }
}
