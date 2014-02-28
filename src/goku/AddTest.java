package goku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AddTest {
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
  public void doIt_taskWithNoName_returnsFailureResult() throws Exception {
    Task task = new Task();
    Command command = makeAddCommandWithTask("add", task);
    Action add = ActionFactory.buildAction(command);

    Result actualResult = add.doIt();
    assertFalse(actualResult.isSuccess());
  }

  @Test
  public void doIt_TaskWithName_returnsSuccessfulResult() throws Exception {
    Task task = new Task();
    int id = task.getId();
    task.setTitle("hello");
    Command command = makeAddCommandWithTask("add hello", task);
    Action add = ActionFactory.buildAction(command);

    Result actual = add.doIt();
    assertTrue(actual.isSuccess());
    assertEquals(1, actual.getTasks().size());
    Task addedTask = list.getTaskById(id);
    assertEquals("hello", addedTask.getTitle());
  }

  @Test
  public void doIt_MultipleTasksWithName() throws Exception {
    Task task;
    Task addedTask;
    int id;
    Command command;
    Action action;
    Result result;

    task = new Task();
    task.setTitle("ABC");
    id = task.getId();
    command = makeAddCommandWithTask("add ABC", task);
    action = ActionFactory.buildAction(command);
    result = action.doIt();
    assertTrue(result.isSuccess());
    assertEquals(1, result.getTasks().size());
    addedTask = list.getTaskById(id);
    assertEquals("ABC", addedTask.getTitle());

    task = new Task();
    task.setTitle("DEF");
    id = task.getId();
    command = makeAddCommandWithTask("add DEF", task);
    action = ActionFactory.buildAction(command);
    result = action.doIt();
    assertTrue(result.isSuccess());
    assertEquals(2, result.getTasks().size());
    addedTask = list.getTaskById(id);
    assertEquals("DEF", addedTask.getTitle());
  }

  private Command makeAddCommandWithTask(String source, Task t) {
    return new Command(source, Command.Type.ADD, t);
  }
}
