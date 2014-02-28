package goku;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EditTest {
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
  public void editTask_returnsEditedResult() throws Exception {
    Task a = new Task();

    a.setTitle("hello");
    a.setDeadline(new Date());
    a.setPeriod(new DateRange(new Date(), new Date()));
    String[] aTags = { "this", "that" };
    a.setTags(aTags);
    a.setNotes("a's notes");

    int idA = a.getId();

    Task b = new Task(a);
    b.setTitle("byebye");
    b.setDeadline(new Date());
    b.setPeriod(new Date(), new Date());
    String[] bTags = { "here", "there" };
    b.setTags(bTags);
    b.setNotes("b's notes");

    list.addTask(a);

    Command command = makeEditCommandWithTask("add", b);
    Action edit = ActionFactory.buildAction(command);

    Result result = edit.doIt();
    Task editedTask = list.getTaskById(idA);
    assertEquals("byebye", editedTask.getTitle());
  }

  private Command makeEditCommandWithTask(String source, Task task) {
    return new Command(source, Command.Type.EDIT, task);
  }
}
