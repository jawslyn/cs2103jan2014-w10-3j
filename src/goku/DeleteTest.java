package goku;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DeleteTest {
  Delete delete = new Delete();
  private static final String DELETED = "deleted \"%s\"";

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

    Result actual = delete.deleteTask(b);
    Result result = new Result(true, String.format(DELETED, "byebye"), null,
        GOKU.getAllTasks());
    assertEquals(result.getSuccessMsg(), actual.getSuccessMsg());
    assertEquals(result.getErrorMsg(), actual.getErrorMsg());
    assertEquals(result.isSuccess(), actual.isSuccess());
    assertEquals(result.getTasks(), actual.getTasks());

  }

}
