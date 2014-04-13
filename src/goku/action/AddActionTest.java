//@author A0099858Y
package goku.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AddActionTest {
  TaskList list;
  GOKU goku;

  @Before
  public void setup() {
    goku = new GOKU();
    list = goku.getTaskList();
  }

  @After
  public void cleanUp() {
    list.clear();
  }

  @Test
  public void doIt_taskWithNoName_returnsFailureResult() throws Exception {
  }

  @Test
  // This tests whether adding a task to the task list works
  public void doIt_TaskWithName_returnsSuccessfulResult() throws Exception {
    AddAction add = new AddAction(goku);
    add.title = "hi";
    Result actualResult = add.doIt();
    assertTrue(actualResult.isSuccess());

    assertEquals(1, list.size());
    Task task = new Task();
    task.setTitle("hi");
    List<Task> addedTask = list.findTaskByTitle("hi");
    assertEquals(1, addedTask.size());
  }

  @Test
  // This tests whether adding tasks with unusual names to the task list works
  public void doIt_TaskWithNullName_returnsSuccessfulResult() throws Exception {
    AddAction add = new AddAction(goku);
    add.title = null;
    Result actualResult = add.doIt();
    assertTrue(actualResult.isSuccess());
    assertEquals(1, list.size());
  }

  @Test
  // This tests whether adding tasks with long names to the task list works
  public void doIt_TaskWithLongName_returnsSuccessfulResult() throws Exception {
    AddAction add = new AddAction(goku);
    add.title = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    Result actualResult = add.doIt();
    assertTrue(actualResult.isSuccess());
    assertEquals(1, list.size());
  }

  @Test
  // This tests whether adding different tasks works
  public void doIt_MultipleTasksWithName() throws Exception {
    AddAction add1 = new AddAction(goku);
    add1.title = "hi abc";
    AddAction add2 = new AddAction(goku);
    add2.title = "hi abcdef";
    AddAction add3 = new AddAction(goku);
    add3.title = "hi def";

    Task task1 = new Task();
    task1.setTitle("abc");
    Task task2 = new Task();
    task2.setTitle("def");

    add1.doIt();
    add2.doIt();
    add3.doIt();
    assertEquals(3, list.size());

    List<Task> results;
    results = list.findTaskByTitle("abc");
    assertEquals(2, results.size());

    results = list.findTaskByTitle("def");
    assertEquals(2, results.size());
  }

}
