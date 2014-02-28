package goku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TaskListTest {
  private TaskList list;
  private TaskList returnList;

  @Before
  public void setup() {
    list = new TaskList();
    returnList = new TaskList();
  }

  @Test
  public void addTask_multipleTasks_returnsCorrectSize() {
    assertListIsSize(0);
    list.addTask(new Task());
    assertListIsSize(1);
    list.addTask(new Task());
    assertListIsSize(2);
  }

  @Test
  public void getTaskById_IdNotFound_returnsNull() {
    Task aTask = makeTaskWithTitle("abc");
    Task otherTask = makeTaskWithTitle("abcdef");
    list.addTask(aTask);
    list.addTask(otherTask);

    int idNotFound = aTask.getId() + otherTask.getId() + 1;
    Task returned = list.getTaskById(idNotFound);
    assertNull(returned);
    assertListIsSize(2);
  }

  @Test
  public void getTaskById_IdFound_returnsTaskWithId() {
    Task aTask = makeTaskWithTitle("abc");
    Task otherTask = makeTaskWithTitle("abcdef");
    list.addTask(aTask);
    list.addTask(otherTask);

    Task returned = list.getTaskById(otherTask.getId());
    assertEquals(otherTask.getId(), returned.getId());
    assertEquals(otherTask.getTitle(), returned.getTitle());
    assertListIsSize(2);
  }

  @Test
  public void findTaskByTitle_noMatchFound_returnsEmptyTaskList() {
    Task aTask = makeTaskWithTitle("abc");
    Task otherTask = makeTaskWithTitle("abcdef");
    list.addTask(aTask);
    list.addTask(otherTask);
    assertListIsSize(2);

    Task toFind = new Task();
    toFind.setTitle("123");
    returnList = list.findTaskByTitle(toFind);
    assertReturnListIsSize(0);
    assertListIsSize(2);
  }

  @Test
  public void findTaskByTitle_matchesFound_returnsTaskListWithMatches() {
    Task aTask = makeTaskWithTitle("abc");
    Task otherTask = makeTaskWithTitle("abcdef");
    list.addTask(aTask);
    list.addTask(otherTask);
    assertListIsSize(2);

    Task toFind = new Task();
    toFind.setTitle("abc");
    returnList = list.findTaskByTitle(toFind);
    assertReturnListIsSize(2);
    assertListIsSize(2);
  }

  @Test
  public void findTaskByTags_noMatch_returnsTaskListWithMultipleMatches() {
    String[] aTags = { "cs2103t", "cs2101" };
    Task aTask = makeTaskWithTitleAndTags("abc", aTags);
    String[] otherTags = { "cs2103t" };
    Task otherTask = makeTaskWithTitleAndTags("abcdef", otherTags);
    list.addTask(aTask);
    list.addTask(otherTask);
    assertListIsSize(2);

    String[] toFindTags = { "cs1010" };
    Task toFind = makeTaskWithTitleAndTags("abc", toFindTags);
    returnList = list.findTaskByTags(toFind);
    assertReturnListIsSize(0);
    assertListIsSize(2);
  }

  @Test
  public void findTaskByTags_multipleMatches_returnsTaskListWithMultipleMatches() {

    String[] aTags = { "cs2103t", "cs2101" };
    Task aTask = makeTaskWithTitleAndTags("abc", aTags);
    String[] otherTags = { "cs2103t" };
    Task otherTask = makeTaskWithTitleAndTags("abcdef", otherTags);

    list.addTask(aTask);
    list.addTask(otherTask);
    assertListIsSize(2);

    String[] toFindTags = { "cs2103t" };
    Task toFind = makeTaskWithTitleAndTags("abc", toFindTags);
    returnList = list.findTaskByTags(toFind);
    assertReturnListIsSize(2);
    assertListIsSize(2);

    String[] otherToFindTags = { "cs2101" };
    Task otherToFind = makeTaskWithTitleAndTags("abc", otherToFindTags);
    returnList = list.findTaskByTags(otherToFind);
    assertReturnListIsSize(1);
    assertListIsSize(2);
  }

  @Test
  public void findTaskByDeadLine_multipleMatches_returnsListWithMultipleMatches()
      throws Exception {
    Task aTask = makeTaskWithTitleAndDeadline("abc", 3, 8, 2014);
    Task otherTask = makeTaskWithTitleAndDeadline("def", 4, 8, 2014);
    list.addTask(aTask);
    list.addTask(otherTask);

    Task dueTask = makeTaskWithTitleAndDeadline("123", 5, 8, 2014);
    returnList = list.findTaskByDeadline(dueTask);
    assertReturnListIsSize(2);
  }

  @Test
  public void findTaskByDeadLine_uniqueMatch_returnsListWithOneMatch()
      throws Exception {
    Task aTask = makeTaskWithTitleAndDeadline("abc", 3, 8, 2014);
    Task otherTask = makeTaskWithTitleAndDeadline("def", 5, 8, 2014);
    list.addTask(aTask);
    list.addTask(otherTask);

    Task dueTask = makeTaskWithTitleAndDeadline("123", 4, 8, 2014);
    returnList = list.findTaskByDeadline(dueTask);
    assertReturnListIsSize(1);
  }

  @Test
  public void findTaskByDeadLine_noMatch_returnsEmptyList() throws Exception {
    Task aTask = makeTaskWithTitleAndDeadline("abc", 3, 8, 2014);
    Task otherTask = makeTaskWithTitleAndDeadline("def", 5, 8, 2014);
    list.addTask(aTask);
    list.addTask(otherTask);

    Task dueTask = makeTaskWithTitleAndDeadline("123", 2, 8, 2014);
    returnList = list.findTaskByDeadline(dueTask);
    assertReturnListIsSize(0);
  }

  @Test
  public void deleteTaskById_idNotFound_doesNotChangeList() throws Exception {
    Task aTask = makeTaskWithTitle("abc");
    Task otherTask = makeTaskWithTitle("abcdef");
    list.addTask(aTask);
    list.addTask(otherTask);

    int idNotFound = aTask.getId() + otherTask.getId() + 1;
    list.deleteTaskById(idNotFound);
    assertListIsSize(2);
  }

  @Test
  public void deleteTaskById_idFound_removesTaskFromList() {
    Task aTask = makeTaskWithTitle("abc");
    Task otherTask = makeTaskWithTitle("abcdef");
    list.addTask(aTask);
    list.addTask(otherTask);

    assertListIsSize(2);
    list.deleteTaskById(aTask.getId());
    assertListIsSize(1);
    list.deleteTaskById(otherTask.getId());
    assertListIsSize(0);
  }

  @Test
  public void deleteTaskByTitle_noMatch_doesNotChangeList() {
    Task aTask = makeTaskWithTitle("abc");
    Task otherTask = makeTaskWithTitle("123");
    list.addTask(aTask);
    list.addTask(otherTask);
    assertListIsSize(2);

    Task toDelete = makeTaskWithTitle("xyz");
    returnList = list.deleteTaskByTitle(toDelete);
    assertListIsSize(2);
    assertReturnListIsSize(0);
  }

  @Test
  public void deleteTaskByTitle_uniqueMatch_removesTaskFromList() {
    Task aTask = makeTaskWithTitle("abc");
    Task otherTask = makeTaskWithTitle("123");
    list.addTask(aTask);
    list.addTask(otherTask);
    assertListIsSize(2);

    Task toDelete = makeTaskWithTitle("ab");
    returnList = list.deleteTaskByTitle(toDelete);
    assertListIsSize(1);
    assertReturnListIsSize(0);
  }

  @Test
  public void deleteTaskByTitle_multipleMatches_doesNotChangeList() {
    Task aTask = makeTaskWithTitle("abc");
    Task otherTask = makeTaskWithTitle("abcdef");
    list.addTask(aTask);
    list.addTask(otherTask);
    assertListIsSize(2);

    Task toDelete = makeTaskWithTitle("abc");
    returnList = list.deleteTaskByTitle(toDelete);
    assertReturnListIsSize(2);

  }

  private Task makeTaskWithTitle(String title) {
    Task t = new Task();
    t.setTitle(title);
    return t;
  }

  private Task makeTaskWithTitleAndTags(String title, String[] tags) {
    Task t = new Task();
    t.setTitle(title);
    t.setTags(tags);
    return t;
  }

  private Task makeTaskWithTitleAndDeadline(String title, int d, int m, int y) {
    Task t = new Task();
    t.setTitle(title);
    t.setDeadline(DateUtil.makeDate(d, m, y));
    return t;
  }

  private void assertListIsSize(int size) {
    assertEquals(size, list.size());
  }

  private void assertReturnListIsSize(int size) {
    assertEquals(size, returnList.size());
  }
}
