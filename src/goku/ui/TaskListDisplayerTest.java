package goku.ui;

import goku.DateUtil;
import goku.Task;
import goku.TaskList;
import hirondelle.date4j.DateTime;

import java.util.Date;

import org.junit.Test;

public class TaskListDisplayerTest {
  TaskListDisplayer tld = new TaskListDisplayer(System.out);
  TaskList list = new TaskList();

  @Test
  public void testDisplay() {
    Task a = makeTaskWithDeadline("task tmr", 1);
    Task b = makeTaskWithDeadline("task today", 0);
    Task c = makeTaskWithDeadline("task ytd", -1);
    Task d = makeTaskWithDeadline("task next week", 7);
    list.addTask(a);
    list.addTask(b);
    list.addTask(c);
    list.addTask(d);
    tld.display(list);
    tld.display(list);
  }

  private Task makeTaskWithDeadline(String title, int daysAway) {
    Task task = new Task();
    task.setTitle(title);
    if (daysAway < 0) {
      Date deadline = DateUtil.toDate(DateUtil.getNow().minus(0, 0, -daysAway,
          2, 0, 0, 0, DateTime.DayOverflow.LastDay));
      task.setDeadline(deadline);
    } else {
      Date deadline = DateUtil.toDate(DateUtil.getNow().plus(0, 0, daysAway, 2,
          0, 0, 0, DateTime.DayOverflow.LastDay));
      task.setDeadline(deadline);
    }
    return task;
  }
}
