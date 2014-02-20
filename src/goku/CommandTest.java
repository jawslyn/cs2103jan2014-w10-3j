package goku;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import goku.Command.SortOrder;
import goku.Command.Type;
import goku.Task.Importance;
import goku.Task.Status;

import java.sql.Date;

import org.junit.Test;

public class CommandTest {

  private static final String TEST_SOURCE = "add do cs2101";
  private static final String TEST_TITLE = "do cs2101";
  private static final String TEST_TITLE_EDITED = "do cs2103t";

  @Test
  public void build_noArguments_returnsNullCommand() {
    MockCommand expected = new MockCommand();
    Command c = Command.builder().build();
    assertTrue(expected.isEquals(c));
  }

  @Test
  public void build_withSource_returnsCommandWithSource() {
    MockCommand expected = new MockCommand();
    expected.source = TEST_SOURCE;
    Command c = Command.builder().setSource(TEST_SOURCE).build();
    assertTrue(expected.isEquals(c));
    assertNull(c.getSortOrder());
    assertNull(c.getTask());
    assertNull(c.getType());
  }

  @Test
  public void build_withSortOrder_returnsCommandWithSortOrder() {
    MockCommand expected = new MockCommand();
    expected.sortOrder = SortOrder.EARLIEST_DEADLINE_FIRST;
    Command c = Command.builder()
        .setSortOrder(SortOrder.EARLIEST_DEADLINE_FIRST).build();
    assertTrue(expected.isEquals(c));
  }

  @Test
  public void build_withEmptyTask_returnsCommandWithTask() throws Exception {
    Task testTask = new Task();
    MockCommand expected = new MockCommand();
    expected.task = testTask;
    Command c = Command.builder().setTask(testTask).build();
    assertTrue(expected.isEquals(c));
  }

  @Test
  public void build_withTaskWithTitle_returnsImmutableCommandWithTaskWithTitle()
      throws Exception {
    MockCommand expected = new MockCommand();
    Task testTask = new Task();
    testTask.setTitle(TEST_TITLE);
    expected.task = testTask;
    Command command = Command.builder().setTask(testTask).build();
    assertTrue(expected.isEquals(command));
    expected.task.setTitle(TEST_TITLE_EDITED);
    assertNotEquals(expected.task.getTitle(), command.getTask().getTitle());
    assertTrue(expected.isEquals(command));
  }

  @Test
  public void build_fullCommand_returnsImmutableCommand() throws Exception {
    Task testTask = makeTaskWithTitleTagsImportanceTimePeriod();
    MockCommand expected = new MockCommand();
    expected.source = TEST_SOURCE;
    expected.sortOrder = SortOrder.EARLIEST_DEADLINE_FIRST;
    expected.task = testTask;
    expected.type = Type.ADD;
    Command c = Command.builder().setSource(TEST_SOURCE)
        .setSortOrder(SortOrder.EARLIEST_DEADLINE_FIRST).setTask(testTask)
        .setType(Type.ADD).build();
    assertTrue(expected.isEquals(c));

    // test for immutability of Command
    testTask.setEndDate(new Date(System.currentTimeMillis()));
    testTask.setImportance(Importance.LOW);
    testTask.setTags(new String[] { "play" });
    testTask.setStatus(Status.COMPLETED);
    assertNotEquals(expected.task.getEndDate(), c.getTask().getEndDate());
    assertNotEquals(expected.task.getImportance(), c.getTask().getImportance());
    assertNotEquals(expected.task.getTags(), c.getTask().getTags());
    assertNotEquals(expected.task.getStatus(), c.getTask().getStatus());
    assertTrue(expected.isEquals(c));
  }

  private Task makeTaskWithTitleTagsImportanceTimePeriod() {
    Task task = new Task();
    task.setTitle(TEST_TITLE);
    task.setTags(new String[] { "cs2101", "homework" });
    task.setImportance(Importance.HIGH);
    task.setStatus(Status.INCOMPLETE);
    Date currentDate = new Date(System.currentTimeMillis());
    task.setStartDate(currentDate);
    task.setEndDate(new Date(currentDate.getTime() + 1000000000));
    return task;
  }
}
