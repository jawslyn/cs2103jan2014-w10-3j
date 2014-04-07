package goku.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeleteActionTest {
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
  //This is to test whether deleting a task works
  public void deleteTask_withIdFound_returnsSuccess() throws Exception {
    Task aTask = new Task();
    int id = list.addTask(aTask);
    DeleteAction delete = new DeleteAction(goku);
    delete.id = id;
    Result result = delete.doIt();
    assertEquals(0, list.size());
    assertTrue(result.isSuccess());
  }

  @Test
  //This is to test whether deleting a task with the wrong ID fails
  public void deleteTask_withIdNotFound_returnsFailure() throws Exception {
    Task aTask = new Task();
    int id = list.addTask(aTask);

    DeleteAction delete = new DeleteAction(goku);
    delete.id = id + 1;
    Result result = delete.doIt();
    delete.title = null;

    assertEquals(1, list.size());
    assertFalse(result.isSuccess());
  }

  @Test
  //This is to test whether deleting a task with the wrong ID and title fails
  public void deleteTask_withIdNotFoundTitleNotFound_returnsFailure()
      throws Exception {
    Task aTask = new Task();
    aTask.setTitle("abc");
    int id = list.addTask(aTask);

    DeleteAction delete = new DeleteAction(goku);
    delete.id = id + 1;
    Result result = delete.doIt();
    delete.title = null;

    assertEquals(1, list.size());
    assertFalse(result.isSuccess());
  }

  @Test
  //This is to test whether deleting a task with wrong ID but correct title works
  public void deleteTask_withIdNotFoundButTitleFound_returnsSuccess()
      throws Exception {
    Task aTask = new Task();
    aTask.setTitle("abc");
    list.addTask(aTask);
    assertEquals(1, list.size());

    DeleteAction delete = new DeleteAction(goku);
    delete.title = "abc";
    Result result = delete.doIt();

    assertEquals(0, list.size());
    assertTrue(result.isSuccess());
  }

  @Test
  //This is to test whether deleting with a term that matches multiple tasks fail
  public void deleteTask_withIdNotFoundAndMultipleMatchesInTitle_returnsFailure()
      throws Exception {
    Task aTask = new Task();
    aTask.setTitle("abcdef");
    list.addTask(aTask);
    Task otherTask = new Task();
    otherTask.setTitle("abc123");
    list.addTask(otherTask);

    DeleteAction delete = new DeleteAction(goku);
    delete.title = "abc";
    Result result = delete.doIt();

    assertEquals(2, list.size());
    assertFalse(result.isSuccess());
  }
  
  @Test
  //This is to test whether deleting a task with a null ID and title fails
  public void deleteTask_withNullIdAndTitle_returnsFailure()
      throws Exception {
    Task aTask = new Task();
    aTask.setTitle("abc");
    list.addTask(aTask);

    DeleteAction delete = new DeleteAction(goku);
    delete.id = null; 
    delete.title = null;
    Result result = delete.doIt();
   
    assertEquals(1, list.size());
    assertFalse(result.isSuccess());
  }

@Test
//This is to test whether deleting a task with a null title fails
public void deleteTask_withNullTitle_returnsFailure()
    throws Exception {
  Task aTask = new Task();
  aTask.setTitle(null);
  list.addTask(aTask);

  DeleteAction delete = new DeleteAction(goku);
  delete.title = null;
  Result result = delete.doIt();
 
  assertEquals(1, list.size());
  assertFalse(result.isSuccess());
  }
}