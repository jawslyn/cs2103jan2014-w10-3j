package goku;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class EditTest {
  Edit edit = new Edit();

  @Test
  public void updateTask_returnsUpdatedResult() throws Exception {
    GOKU.getAllTasks().clear();

    Task a = new Task();

    a.setTitle("hello");
    a.setDeadline(new Date());
    a.setPeriod(new DateRange(new Date(), new Date()));
    String[] aTags = { "this", "that" };
    a.setTags(aTags);
    a.setNotes("a's notes");

    Task b = a;
    b.setTitle("byebye");
    b.setDeadline(new Date(1));
    b.setPeriod(new Date(2), new Date(3));
    String[] bTags = { "here", "there" };
    b.setTags(bTags);
    b.setNotes("b's notes");

    GOKU.getAllTasks().add(a);

    Result actual = edit.updateTask();
    Task actualTask = actual.getTasks().get(0);

    assertEquals(actualTask.getTitle(), b.getTitle());
    assertEquals(actualTask.getDeadline(), b.getDeadline());
    assertEquals(actualTask.getDateRange(), b.getDateRange());
    String[] actualTags = actualTask.getTags();
    assertEquals(actualTags[0], bTags[0]);
    assertEquals(actualTags[1], bTags[1]);
    assertEquals(actualTask.getNotes(), b.getNotes());

  }
}
