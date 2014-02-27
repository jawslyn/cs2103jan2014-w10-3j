package goku;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AddTest {
  private static final String ADDED = "added \"%s\"";
  Add add = new Add();

  @Test
  public void addTask_returnsAddedResult() throws Exception {
    Task task = new Task();
    task.setTitle("hello");
    Result actual = add.addTask(task);
    Result result = new Result(true, String.format(ADDED, "hello"), null,
        GOKU.getAllTasks());
    assertEquals(result.getSuccessMsg(), actual.getSuccessMsg());
    assertEquals(result.getErrorMsg(), actual.getErrorMsg());
    assertEquals(result.isSuccess(), actual.isSuccess());
    assertEquals(result.getTasks(), actual.getTasks());
  }

}
