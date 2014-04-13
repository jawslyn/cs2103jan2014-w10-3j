//@author A0096444X
package goku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import goku.util.DateUtil;
import goku.util.InvalidDateRangeException;
import hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TaskListTest {
  private TaskList list;
  private List<Task> returnList;

  @Before
  public void setup() {
    list = new TaskList();
    returnList = new ArrayList<>();
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
  public void addTask_willAlwaysMaintainSequentialId() {
    list.addTask(new Task());
    int second = list.addTask(new Task());
    int third = list.addTask(new Task());
    list.addTask(new Task());
    list.deleteTaskById(third);
    list.deleteTaskById(second);
    int shouldBeSecond = list.addTask(new Task());
    int shouldBeThird = list.addTask(new Task());
    assertEquals(second, shouldBeSecond);
    assertEquals(third, shouldBeThird);
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
    returnList = list.findTaskByTitle("123");
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
    returnList = list.findTaskByTitle("abc");
    assertReturnListIsSize(2);
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
    returnList = list.findTaskByDeadline(dueTask.getDeadline());
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
    returnList = list.findTaskByDeadline(dueTask.getDeadline());
    assertReturnListIsSize(1);
  }

  @Test
  public void findTaskByDeadLine_noMatch_returnsEmptyList() throws Exception {
    Task aTask = makeTaskWithTitleAndDeadline("abc", 3, 8, 2014);
    Task otherTask = makeTaskWithTitleAndDeadline("def", 5, 8, 2014);
    list.addTask(aTask);
    list.addTask(otherTask);

    Task dueTask = makeTaskWithTitleAndDeadline("123", 2, 8, 2014);
    returnList = list.findTaskByDeadline(dueTask.getDeadline());
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

    // Task toDelete = makeTaskWithTitle("xyz");
    returnList = list.deleteTaskByTitle("xyz");
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

    returnList = list.deleteTaskByTitle("ab");
    assertListIsSize(1);
    assertReturnListIsSize(1);
  }

  // @author A0099585Y
  @Test
  public void deleteTaskByTitle_multipleMatches_doesNotChangeList() {
    Task aTask = makeTaskWithTitle("abc");
    Task otherTask = makeTaskWithTitle("abcdef");
    list.addTask(aTask);
    list.addTask(otherTask);
    assertListIsSize(2);

    returnList = list.deleteTaskByTitle("abc");
    assertReturnListIsSize(2);

  }

  @Test
  public void hasClash_StartLaterEndEarlier_ReturnsTrueIfClash()
      throws InvalidDateRangeException {
    Task a = new Task();
    a.setPeriod(new DateRange(DateUtil.parse("1pm"), DateUtil.parse("5pm")));
    Task b = new Task();
    b.setPeriod(new DateRange(DateUtil.parse("3pm"), DateUtil.parse("4pm")));
    list.addTask(a);
    assertTrue(list.hasClash(b));
  }

  @Test
  public void hasClash_StartEarlierEndEarlier_ReturnsTrueIfClash()
      throws InvalidDateRangeException {
    Task a = new Task();
    a.setPeriod(new DateRange(DateUtil.parse("1pm"), DateUtil.parse("5pm")));
    Task b = new Task();
    b.setPeriod(new DateRange(DateUtil.parse("12pm"), DateUtil.parse("3pm")));
    list.addTask(a);
    assertTrue(list.hasClash(b));
  }

  @Test
  public void hasClash_StartLaterEndLater_ReturnsTrueIfClash()
      throws InvalidDateRangeException {
    Task a = new Task();
    a.setPeriod(new DateRange(DateUtil.parse("1pm"), DateUtil.parse("5pm")));
    Task b = new Task();
    b.setPeriod(new DateRange(DateUtil.parse("4pm"), DateUtil.parse("7pm")));
    list.addTask(a);
    assertTrue(list.hasClash(b));
  }

  @Test
  public void hasClash_NoClash_ReturnsTrueIfClash()
      throws InvalidDateRangeException {
    Task a = new Task();
    a.setPeriod(new DateRange(DateUtil.parse("1pm"), DateUtil.parse("5pm")));
    Task b = new Task();
    b.setPeriod(new DateRange(DateUtil.parse("12pm"), DateUtil.parse("1pm")));
    list.addTask(a);
    assertFalse(list.hasClash(b));
    list.addTask(b);
    Task c = new Task();
    c.setPeriod(new DateRange(DateUtil.parse("7am"), DateUtil.parse("11pm")));
    assertTrue(list.hasClash(c));
  }

  // @author A0096444X
  @Test
  public void findFreeSlots_DateHasNoTasks() throws InvalidDateRangeException {
    DateTime today = DateUtil.getNowDate();
    assertListIsSize(0);

    List<String> expected = new ArrayList<String>();
    expected.add("[00:00 - 23:59]");

    assertEquals(expected, list.findFreeSlots(today));
  }

  @Test
  public void findFreeSlots_DateHasTaskThatOccupiesWholeDay()
      throws InvalidDateRangeException {
    DateTime today = DateUtil.getNowDate();
    Task aTask = makeTaskWithTitleAndTodayPeriod("a");
    list.addTask(aTask);

    assertEquals(new ArrayList<String>(), list.findFreeSlots(today));
  }

  @Test
  public void findFreeSlots_TwoPeriodsNonOverlapping()
      throws InvalidDateRangeException {
    DateTime today = DateUtil.getNowDate();
    Task aTask = makeTaskWithTitle("a");
    Task bTask = makeTaskWithTitle("b");
    aTask.setPeriod(new DateRange(today.plus(0, 0, 0, 1, 0, 0, 0,
        DateTime.DayOverflow.Spillover), today.plus(0, 0, 0, 5, 0, 0, 0,
        DateTime.DayOverflow.Spillover)));
    bTask.setPeriod(new DateRange(today.plus(0, 0, 0, 9, 0, 0, 0,
        DateTime.DayOverflow.Spillover), today.plus(0, 0, 0, 17, 0, 0, 0,
        DateTime.DayOverflow.Spillover)));
    list.addTask(aTask);
    list.addTask(bTask);

    List<String> expected = new ArrayList<String>();
    expected.add("[00:00 - 00:59]");
    expected.add("[05:00 - 08:59]");
    expected.add("[17:00 - 23:59]");

    assertEquals(expected, list.findFreeSlots(today));
  }

  @Test
  public void findFreeSlots_ThreePeriodsOverlapping()
      throws InvalidDateRangeException {
    DateTime today = DateUtil.getNowDate();
    Task aTask = makeTaskWithTitle("a");
    Task bTask = makeTaskWithTitle("b");
    Task cTask = makeTaskWithTitle("c");
    aTask.setPeriod(new DateRange(today.plus(0, 0, 0, 11, 0, 0, 0,
        DateTime.DayOverflow.Spillover), today.plus(0, 0, 0, 15, 0, 0, 0,
        DateTime.DayOverflow.Spillover)));
    bTask.setPeriod(new DateRange(today.plus(0, 0, 0, 13, 0, 0, 0,
        DateTime.DayOverflow.Spillover), today.plus(0, 0, 0, 17, 0, 0, 0,
        DateTime.DayOverflow.Spillover)));
    cTask.setPeriod(new DateRange(today.plus(0, 0, 0, 16, 0, 0, 0,
        DateTime.DayOverflow.Spillover), today.plus(0, 0, 0, 20, 0, 0, 0,
        DateTime.DayOverflow.Spillover)));
    list.addTask(aTask);
    list.addTask(bTask);
    list.addTask(cTask);

    List<String> expected = new ArrayList<String>();
    expected.add("[00:00 - 10:59]");
    expected.add("[20:00 - 23:59]");

    assertEquals(expected, list.findFreeSlots(today));
  }

  @Test
  public void findFreeSlots_ThreePeriodsSemiOverlapping()
      throws InvalidDateRangeException {
    DateTime today = DateUtil.getNowDate();
    Task aTask = makeTaskWithTitle("a");
    Task bTask = makeTaskWithTitle("b");
    Task cTask = makeTaskWithTitle("c");
    aTask.setPeriod(new DateRange(today.plus(0, 0, 0, 11, 0, 0, 0,
        DateTime.DayOverflow.Spillover), today.plus(0, 0, 0, 15, 0, 0, 0,
        DateTime.DayOverflow.Spillover)));
    bTask.setPeriod(new DateRange(today.plus(0, 0, 0, 13, 0, 0, 0,
        DateTime.DayOverflow.Spillover), today.plus(0, 0, 0, 17, 0, 0, 0,
        DateTime.DayOverflow.Spillover)));
    cTask.setPeriod(new DateRange(today.plus(0, 0, 0, 21, 0, 0, 0,
        DateTime.DayOverflow.Spillover), today.plus(0, 0, 0, 23, 0, 0, 0,
        DateTime.DayOverflow.Spillover)));
    list.addTask(aTask);
    list.addTask(bTask);
    list.addTask(cTask);

    List<String> expected = new ArrayList<String>();
    expected.add("[00:00 - 10:59]");
    expected.add("[17:00 - 20:59]");
    expected.add("[23:00 - 23:59]");

    assertEquals(expected, list.findFreeSlots(today));
  }

  @Test
  public void findFreeSlots_StartingPeriodSpillOver()
      throws InvalidDateRangeException {
    DateTime today = DateUtil.getNowDate();
    Task aTask = makeTaskWithTitle("a");
    aTask.setPeriod(new DateRange(today.minusDays(1).plus(0, 0, 0, 20, 0, 0, 0,
        DateTime.DayOverflow.Spillover), today.plus(0, 0, 0, 15, 0, 0, 0,
        DateTime.DayOverflow.Spillover)));
    list.addTask(aTask);

    List<String> expected = new ArrayList<String>();
    expected.add("[15:00 - 23:59]");

    assertEquals(expected, list.findFreeSlots(today));
  }

  @Test
  public void findFreeSlots_EndingPeriodSpillOver()
      throws InvalidDateRangeException {
    DateTime today = DateUtil.getNowDate();
    Task aTask = makeTaskWithTitle("a");
    aTask.setPeriod(new DateRange(today.plus(0, 0, 0, 20, 0, 0, 0,
        DateTime.DayOverflow.Spillover), today.plusDays(1).plus(0, 0, 0, 15, 0,
        0, 0, DateTime.DayOverflow.Spillover)));
    list.addTask(aTask);

    List<String> expected = new ArrayList<String>();
    expected.add("[00:00 - 19:59]");

    assertEquals(expected, list.findFreeSlots(today));
  }

  private Task makeTaskWithTitle(String title) {
    Task t = new Task();
    t.setTitle(title);
    return t;
  }

  private Task makeTaskWithTitleAndDeadline(String title, int d, int m, int y) {
    Task t = new Task();
    t.setTitle(title);
    t.setDeadline(DateTime.forDateOnly(y, m, d));
    return t;
  }

  private Task makeTaskWithTitleAndTodayPeriod(String title)
      throws InvalidDateRangeException {
    Task t = new Task();
    t.setTitle(title);
    DateRange period = new DateRange(DateUtil.getNow().getStartOfDay()
        .truncate(DateTime.Unit.SECOND), DateUtil.getNow().getEndOfDay()
        .truncate(DateTime.Unit.SECOND));
    t.setPeriod(period);
    return t;
  }

  private void assertListIsSize(int size) {
    assertEquals(size, list.size());
  }

  private void assertReturnListIsSize(int size) {
    assertEquals(size, returnList.size());
  }
}
