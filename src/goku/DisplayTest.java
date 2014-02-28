package goku;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

public class DisplayTest {
  Display display = new Display();
  private static final String EMPTY = "there are no tasks";

  @Test
  public void displayAll_returnsAllTasks() throws Exception {
    GOKU.getAllTasks().clear();

    Task a = new Task();
    Task b = new Task();

    a.setTitle("hello");
    b.setTitle("byebye");

    GOKU.getAllTasks().add(a);
    GOKU.getAllTasks().add(b);

    Result actual = display.displayAll();
    Result result = new Result(true, null, null, GOKU.getAllTasks());
    assertEquals(result.getSuccessMsg(), actual.getSuccessMsg());
    assertEquals(result.getErrorMsg(), actual.getErrorMsg());
    assertEquals(result.isSuccess(), actual.isSuccess());
    assertEquals(result.getTasks(), actual.getTasks());

  }

  @Test
  public void displayAll_returnsErrorWhenNoTasks() throws Exception {
    GOKU.getAllTasks().clear();

    Result actual = display.displayAll();
    Result result = new Result(true, EMPTY, null, new ArrayList<Task>());
    assertEquals(result.getSuccessMsg(), actual.getSuccessMsg());
    assertEquals(result.getErrorMsg(), actual.getErrorMsg());
    assertEquals(result.isSuccess(), actual.isSuccess());
    assertEquals(result.getTasks(), actual.getTasks());
  }

  @Test
  public void displayDate_returnsTasksWithSameDate() throws Exception {
    GOKU.getAllTasks().clear();

    Task a = new Task();
    Task b = new Task();
    Task c = new Task();

    a.setDeadline(new Date());
    b.setDeadline(new Date(3));
    c.setDeadline(a.getDeadline());

    a.setTitle("hello");
    b.setTitle("byebye");
    c.setTitle("world");

    GOKU.getAllTasks().add(a);
    GOKU.getAllTasks().add(b);
    GOKU.getAllTasks().add(c);

    ArrayList<Task> resultList = new ArrayList<Task>();
    resultList.add(a);
    resultList.add(c);
    Result actual = display.displayDate();
    Result result = new Result(true, null, null, resultList);
    assertEquals(result.getSuccessMsg(), actual.getSuccessMsg());
    assertEquals(result.getErrorMsg(), actual.getErrorMsg());
    assertEquals(result.isSuccess(), actual.isSuccess());
    assertEquals(result.getTasks(), actual.getTasks());

  }

}
