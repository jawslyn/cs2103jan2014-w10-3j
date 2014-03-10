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

public class SearchActionTest {
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
  public void searchTitle_returnsTasksWithSameTitle() throws Exception {
    Task a = new Task();
    Task b = new Task();
    Task c = new Task();

    a.setTitle("hello world");
    b.setTitle("byebye");
    c.setTitle("hello");

    int idA = list.addTask(a);
    int idB = list.addTask(b);

    SearchAction search = new SearchAction(goku);
    search.title = "hello";

    Result result = search.doIt();
    assertTrue(result.isSuccess());
    assertEquals(1, result.getTasks().size());
    assertEquals("hello world", result.getTasks().getTaskById(idA).getTitle());
  }

}
