package goku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchTest {
  TaskList list;

  @Before
  public void setup() {
    list = GOKU.getTaskList();
  }

  @After
  public void cleanUp() {
    GOKU.getTaskList().clear();
  }

  @Test
  public void searchTitle_returnsTasksWithSameTitle() throws Exception {
    Task a = new Task();
    Task b = new Task();
    Task c = new Task();

    int idA = a.getId();

    a.setTitle("hello world");
    b.setTitle("byebye");
    c.setTitle("hello");

    list.addTask(a);
    list.addTask(b);

    Command command = makeSearchCommandWithTask("search hello", c);
    Action search = ActionFactory.buildAction(command);
    Result result = search.doIt();
    assertTrue(result.isSuccess());
    assertEquals(1, result.getTasks().size());
    assertEquals("hello world", result.getTasks().getTaskById(idA).getTitle());
  }

  @Test
  public void searchTag_returnsTasksWithSameTags() throws Exception {
    Task a = new Task();
    Task b = new Task();
    Task c = new Task();

    int idA = a.getId();

    a.setTitle("hello");
    b.setTitle("byebye");

    String[] aTags = { "this", "that" };
    String[] bTags = { "here", "there" };
    String[] cTags = { "this" };
    a.setTags(aTags);
    b.setTags(bTags);
    c.setTags(cTags);

    list.addTask(a);
    list.addTask(b);

    Command command = makeSearchCommandWithTask("search #this", c);
    Action search = ActionFactory.buildAction(command);
    Result result = search.doIt();
    assertTrue(result.isSuccess());
    assertEquals(1, result.getTasks().size());
    assertEquals("hello", result.getTasks().getTaskById(idA).getTitle());
  }

  private Command makeSearchCommandWithTask(String source, Task task) {
    return new Command(source, Command.Type.SEARCH, task);
  }
}
