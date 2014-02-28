package goku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeleteTest {
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
  public void deleteTask_withIdFound_returnsSuccess() throws Exception {
    Task aTask = new Task();
    int id = aTask.getId();
    list.addTask(aTask);

    Task taskToDelete = new Task(aTask);
    Command command = makeDeleteCommandWithTask("delete 0", taskToDelete);
    Action delete = ActionFactory.buildAction(command);
    Result result = delete.doIt();
    assertEquals(0, list.size());
    assertTrue(result.isSuccess());
  }

  @Test
  public void deleteTask_withIdNotFound_returnsFailure() throws Exception {
    Task aTask = new Task();
    int id = aTask.getId();
    list.addTask(aTask);

    Task taskToDelete = new Task();
    Command command = makeDeleteCommandWithTask("delete 1", taskToDelete);
    Action delete = ActionFactory.buildAction(command);
    Result result = delete.doIt();
    assertEquals(1, list.size());
    assertFalse(result.isSuccess());
  }

  @Test
  public void deleteTask_withIdNotFoundButTitleFound_returnsSuccess()
      throws Exception {
    Task aTask = new Task();
    aTask.setTitle("abc");
    list.addTask(aTask);

    Task taskToDelete = new Task();
    taskToDelete.setTitle("abc");
    Command command = makeDeleteCommandWithTask("delete abc", taskToDelete);
    Action delete = ActionFactory.buildAction(command);
    Result result = delete.doIt();
    assertEquals(0, list.size());
    assertTrue(result.isSuccess());
  }

  @Test
  public void deleteTask_withIdNotFoundAndMultipleMatchesInTitle_returnsFailure()
      throws Exception {
    Task aTask = new Task();
    aTask.setTitle("abcdef");
    list.addTask(aTask);
    Task otherTask = new Task();
    otherTask.setTitle("abc123");
    list.addTask(otherTask);

    Task taskToDelete = new Task();
    taskToDelete.setTitle("abc");
    Command command = makeDeleteCommandWithTask("delete abc", taskToDelete);
    Action delete = ActionFactory.buildAction(command);
    Result result = delete.doIt();
    assertEquals(2, list.size());
    assertFalse(result.isSuccess());
  }

  @Test
  public void deleteTask_returnsDeletedResult() throws Exception {
    Task a = new Task();
    Task b = new Task();
    Task c = new Task();

    a.setTitle("hello");
    b.setTitle("byebye");
    c.setTitle("world");

    GOKU.getAllTasks().add(a);
    GOKU.getAllTasks().add(b);
    GOKU.getAllTasks().add(c);

    // Result actual = delete.deleteTask(b);
    // Result result = new Result(true, String.format(DELETED, "byebye"), null,
    // GOKU.getAllTasks());
    // assertEquals(result.getSuccessMsg(), actual.getSuccessMsg());
    // assertEquals(result.getErrorMsg(), actual.getErrorMsg());
    // assertEquals(result.isSuccess(), actual.isSuccess());
    // assertEquals(result.getTasks(), actual.getTasks());

  }

  private Command makeDeleteCommandWithTask(String source, Task t) {
    return new Command(source, Command.Type.DELETE, t);
  }

}
