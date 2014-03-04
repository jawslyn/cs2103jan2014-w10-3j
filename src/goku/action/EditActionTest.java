package goku.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;

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
    Task a = new Task();
    a.setTitle("hello");
    int idA = a.getId();
    list.addTask(a);

    Task b = new Task(a);
    b.setTitle("byebye");

    EditAction edit = new EditAction(goku);
    edit.id = idA;
    edit.title = "byebye";
    Result result = edit.doIt();

    Task editedTask = list.getTaskById(idA);
    assertTrue(result.isSuccess());
    assertEquals("byebye", editedTask.getTitle());
  }

}
