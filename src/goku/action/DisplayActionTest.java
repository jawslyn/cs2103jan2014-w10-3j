//@A0099858Y
package goku.action;

import static org.junit.Assert.assertEquals;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;
import goku.util.DateUtil;

import org.junit.Before;
import org.junit.Test;

public class DisplayActionTest {
  private GOKU goku;
  private TaskList list;

  @Before
  public void setup() {
    goku = new GOKU();
    list = goku.getTaskList();
  }

  @Test
  // This tests whether selectively displaying incomplete and/or complete tasks
  // works
  public void doIt_success() {
    Task completedTask = new Task();
    completedTask.setTitle("complete task");
    completedTask.setStatus(true);

    Task overdueTask = new Task();
    overdueTask.setTitle("overdue task");
    overdueTask.setDeadline(DateUtil.getNow().minusDays(3));

    Task previousTask = new Task();
    previousTask.setTitle("previous task");
    previousTask.setStatus(true);
    previousTask.setDeadline(DateUtil.getNow().minusDays(3));

    Task task = new Task();
    task.setTitle("task");

    list.addTask(completedTask);
    list.addTask(overdueTask);
    list.addTask(previousTask);
    list.addTask(task);

    DisplayAction da = new DisplayAction(goku);
    da.viewComplete = true;
    da.viewOverdue = false;
    Result onlyComplete = da.doIt();
    assertEquals(2, onlyComplete.getTasks().size());

    da.viewComplete = false;
    da.viewOverdue = true;
    Result onlyOverdue = da.doIt();
    assertEquals(1, onlyOverdue.getTasks().size());

    da.viewComplete = false;
    da.viewOverdue = false;
    Result onlyIncomplete = da.doIt();
    assertEquals(1, onlyIncomplete.getTasks().size());

  }
}
