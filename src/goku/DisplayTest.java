package goku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DisplayTest {
  TaskList list;

  @Before
  public void setup() {
    list = GOKU.getTaskList();
  }

  @After
  public void cleanUp() {
    GOKU.getTaskList().clear();
  }

  @Test
  public void displayAll_returnsAllTasks() throws Exception {
    Task a = new Task();
    a.setTitle("hello");
    Task b = new Task();
    b.setTitle("byebye");
    int idA = a.getId();
    int idB = b.getId();
    list.addTask(a);
    list.addTask(b);

    Command command = makeDisplayCommandWithTask("display", new Task());
    Action display = ActionFactory.buildAction(command);
    Result actual = display.doIt();

    assertEquals(2, actual.getTasks().size());
    assertTrue(actual.isSuccess());
    assertEquals("hello", actual.getTasks().getTaskById(idA).getTitle());
    assertEquals("byebye", actual.getTasks().getTaskById(idB).getTitle());
  }

  private Command makeDisplayCommandWithTask(String source, Task task) {
    return new Command(source, Command.Type.DISPLAY, task);
  }

  @Test
  public void displayDate_returnsTasksDueBeforeDeadline() throws Exception {
    Task a = new Task();
    Task b = new Task();
    Task c = new Task();

    int idA = a.getId();

    Date aDate = DateUtil.makeDate(1, 1);
    Date otherDate = DateUtil.makeDate(3, 1);
    Date deadline = DateUtil.makeDate(3, 1);

    a.setDeadline(aDate);
    b.setDeadline(otherDate);
    c.setDeadline(deadline);

    a.setTitle("hello");
    b.setTitle("byebye");
    c.setTitle("DEADLINE");

    list.addTask(a);
    list.addTask(b);

    Command command = makeDisplayCommandWithTask("display", c);
    Action display = ActionFactory.buildAction(command);
    Result actual = display.doIt();

    assertTrue(actual.isSuccess());
    assertEquals(1, actual.getTasks().size());
    assertEquals("hello", actual.getTasks().getTaskById(idA).getTitle());
  }
}
