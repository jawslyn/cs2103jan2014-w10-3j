package goku.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import goku.DateRange;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;
import goku.util.DateUtil;
import hirondelle.date4j.DateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EditActionTest {
  GOKU goku;
  TaskList list;

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
  //This tests whether editing works by checking the edited results
  public void editTask_returnsEditedResult() throws Exception {
    Task toEdit, changedTask;
    Integer id;
    EditAction editAction;
    Result result;

    toEdit = new Task();
    toEdit.setTitle("hello");
    id = list.addTask(toEdit);

    changedTask = new Task(toEdit);
    changedTask.setTitle("byebye");

    editAction = new EditAction(goku);
    editAction.id = id;
    editAction.title = "byebye";
    result = editAction.doIt();

    assertTrue(result.isSuccess());
    assertEquals("byebye", toEdit.getTitle());

    editAction = new EditAction(goku);
    editAction.id = id;
    editAction.isComplete = true;
    assertNull(toEdit.isDone());
    result = editAction.doIt();
    assertTrue(result.isSuccess());
    assertEquals("byebye", toEdit.getTitle());
    assertTrue(toEdit.isDone());

    editAction = new EditAction(goku);
    editAction.id = id;
    DateTime deadline = DateUtil.getNow();
    editAction.dline = deadline;
    assertNull(toEdit.getDeadline());
    result = editAction.doIt();
    assertTrue(result.isSuccess());
    assertEquals("byebye", toEdit.getTitle());
    assertTrue(toEdit.isDone());
    assertEquals(deadline, toEdit.getDeadline());
  }
  
  @Test
  //This tests whether removing deadline from tasks works
  public void doIt_removesDeadlineFromTask_returnsTaskWithoutDeadline()
      throws Exception {
    Task toEdit;
    toEdit = new Task();
    toEdit.setTitle("hello");
    DateTime deadline = DateUtil.getNow();
    toEdit.setDeadline(deadline);
    Integer id = list.addTask(toEdit);

    EditAction editAction = new EditAction(goku);
    editAction.id = id;
    editAction.removeDeadline = true;

    assertNotNull(toEdit.getDeadline());
    Result result = editAction.doIt();
    assertTrue(result.isSuccess());
    assertEquals("hello", toEdit.getTitle());
    assertNull(toEdit.getDeadline());
  }

  @Test
  //This tests whether removing time period from tasks works
  public void doIt_removesPeriodFromTask_returnsTaskWithoutPeriod()
      throws Exception {
    Task toEdit;
    toEdit = new Task();
    toEdit.setTitle("hello");
    DateTime deadline = DateUtil.getNow();
    toEdit.setPeriod(new DateRange(deadline, deadline.plusDays(1)));
    Integer id = list.addTask(toEdit);

    EditAction editAction = new EditAction(goku);
    editAction.id = id;
    editAction.removePeriod = true;

    assertNotNull(toEdit.getDateRange());
    Result result = editAction.doIt();
    assertTrue(result.isSuccess());
    assertEquals("hello", toEdit.getTitle());
    assertNull(toEdit.getDateRange());
  }

  @Test
//This tests whether removing importance from tasks works
  public void doIt_removesImportantFromTask_returnsTaskWithoutImportant()
      throws Exception {
    Task toEdit;
    toEdit = new Task();
    toEdit.setTitle("hello");
    toEdit.setImpt(true);
    Integer id = list.addTask(toEdit);

    EditAction editAction = new EditAction(goku);
    editAction.id = id;
    editAction.removeImportant = true;

    assertTrue(toEdit.getImpt());
    Result result = editAction.doIt();
    assertTrue(result.isSuccess());
    assertEquals("hello", toEdit.getTitle());
    assertTrue(!toEdit.getImpt());
  }

}
