//@author A0099858Y
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
  Task toEdit;
  Integer toEditId;
  EditAction editAction;
  Result result;

  @Before
  public void setup() {
    goku = new GOKU();
    list = goku.getTaskList();
    toEdit = new Task();
    editAction = new EditAction(goku);
  }

  @After
  public void cleanUp() {
    list.clear();
  }

  @Test
  public void editTask_changeTaskName() throws Exception {
    toEdit.setTitle("hello");
    toEditId = list.addTask(toEdit);

    editAction.id = toEditId;
    editAction.title = "byebye";
    result = editAction.doIt();

    assertTrue(result.isSuccess());
    assertEquals("byebye", toEdit.getTitle());
  }

  @Test
  public void editTask_makeComplete() throws Exception {
    toEdit.setTitle("hello");
    toEditId = list.addTask(toEdit);

    editAction.id = toEditId;
    editAction.isComplete = true;
    assertNull(toEdit.isDone());
    result = editAction.doIt();

    assertTrue(result.isSuccess());
    assertEquals("hello", toEdit.getTitle());
    assertTrue(toEdit.isDone());
  }

  @Test
  // This tests whether editing works by checking the edited results
  public void editTask_changeDeadline() throws Exception {
    toEdit.setTitle("hello");
    toEditId = list.addTask(toEdit);

    editAction.id = toEditId;
    DateTime deadline = DateUtil.getNow();
    editAction.dline = deadline;
    assertNull(toEdit.getDeadline());
    result = editAction.doIt();
    assertTrue(result.isSuccess());
    assertEquals("hello", toEdit.getTitle());
    assertEquals(deadline, toEdit.getDeadline());
  }

  @Test
  public void editTaskWithDeadline_addPeriod_returnsTaskWithPeriod()
      throws Exception {
    Task toEdit, changedTask;
    Integer id;
    EditAction editAction;
    Result result;

    toEdit = new Task();
    toEdit.setTitle("hello");
    toEdit.setDeadline(DateUtil.getNow().plusDays(1));
    id = list.addTask(toEdit);

    editAction = new EditAction(goku);
    editAction.id = id;
    editAction.period = new DateRange(DateUtil.getNow().plusDays(1), DateUtil
        .getNow().plusDays(2));
    result = editAction.doIt();

    assertTrue(result.isSuccess());
    assertEquals("hello", toEdit.getTitle());
    assertNull(toEdit.getDeadline());
    assertEquals(toEdit.getDateRange(), editAction.period);
  }

  @Test
  public void editTaskWithPeriod_addDeadline_returnsTaskWithDeadline()
      throws Exception {
    Task toEdit, changedTask;
    Integer id;
    EditAction editAction;
    Result result;

    toEdit = new Task();
    toEdit.setTitle("hello");
    toEdit.setPeriod(new DateRange(DateUtil.getNow().plusDays(1), DateUtil
        .getNow().plusDays(2)));
    id = list.addTask(toEdit);

    editAction = new EditAction(goku);
    editAction.id = id;
    editAction.dline = DateUtil.getNow().plusDays(1);
    result = editAction.doIt();

    assertTrue(result.isSuccess());
    assertEquals("hello", toEdit.getTitle());
    assertNull(toEdit.getDateRange());
    assertEquals(toEdit.getDeadline(), editAction.dline);
  }

  @Test
  // This tests whether removing deadline from tasks works
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
    editAction.shouldRemoveDeadline = true;

    assertNotNull(toEdit.getDeadline());
    Result result = editAction.doIt();
    assertTrue(result.isSuccess());
    assertEquals("hello", toEdit.getTitle());
    assertNull(toEdit.getDeadline());
  }

  @Test
  // This tests whether removing time period from tasks works
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
    editAction.shouldRemovePeriod = true;

    assertNotNull(toEdit.getDateRange());
    Result result = editAction.doIt();
    assertTrue(result.isSuccess());
    assertEquals("hello", toEdit.getTitle());
    assertNull(toEdit.getDateRange());
  }

  @Test
  // This tests whether removing importance from tasks works
  public void doIt_toggleImportantFromTask_returnsTaskWithoutImportant()
      throws Exception {
    Task toEdit;
    toEdit = new Task();
    toEdit.setTitle("hello");
    toEdit.setImpt(true);
    Integer id = list.addTask(toEdit);

    EditAction editAction = new EditAction(goku);
    editAction.id = id;
    editAction.shouldToggleImportant = true;

    assertTrue(toEdit.isImpt());
    Result result = editAction.doIt();
    assertTrue(result.isSuccess());
    assertEquals("hello", toEdit.getTitle());
    assertTrue(!toEdit.isImpt());
  }

}
