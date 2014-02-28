package goku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

public class AddTest {
  @After
  public void cleanUp() {
    GOKU.getAllTasks().clear();
  }

  @Test
  public void doIt_taskWithNoName_returnsFailureResult() throws Exception {
    Task task = new Task();
    Command command = makeAddCommandWithTask("add", task);
    Action add = ActionFactory.buildAction(command);

    Result actualResult = add.doIt();
    // assertEquals(false, actualResult.isSuccess());
    assertFalse(actualResult.isSuccess());
  }

  @Test
  public void doIt_TaskWithName_returnsSuccessfulResult() throws Exception {
    Task task = new Task();
    task.setTitle("hello");
    Command command = makeAddCommandWithTask("add hello", task);
    Action add = ActionFactory.buildAction(command);

    Result actual = add.doIt();
    assertTrue(actual.isSuccess());
    assertEquals(1, actual.getTasks().size());
    assertEquals("hello", actual.getTasks().get(0).getTitle());

  }

  @Test
  public void doIt_MultipleTasksWithName() throws Exception {
    Task task;
    Command command;
    Action action;
    Result result;

    task = new Task();
    task.setTitle("ABC");
    command = makeAddCommandWithTask("add ABC", task);
    action = ActionFactory.buildAction(command);
    result = action.doIt();
    assertTrue(result.isSuccess());
    assertEquals(1, result.getTasks().size());
    assertEquals("ABC", result.getTasks().get(0).getTitle());

    task = new Task();
    task.setTitle("DEF");
    command = makeAddCommandWithTask("add DEF", task);
    action = ActionFactory.buildAction(command);
    result = action.doIt();
    assertTrue(result.isSuccess());
    assertEquals(2, result.getTasks().size());
    assertEquals("DEF", result.getTasks().get(1).getTitle());
  }

  private Command makeAddCommandWithTask(String source, Task t) {
    return new Command(source, Command.Type.ADD, t);
  }
}
