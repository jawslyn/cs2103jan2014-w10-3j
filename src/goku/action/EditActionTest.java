package goku.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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

}
