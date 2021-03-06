//@author A0096444X
package goku.action;

import static org.junit.Assert.*;
import goku.DateRange;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;
import goku.util.DateUtil;
import goku.util.InvalidDateRangeException;
import hirondelle.date4j.DateTime;

import java.io.IOException;

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
  public void cleanUp() throws IOException {
    list.clear();
  }

  @Test
  public void searchTitle_returnsTasksWithSameTitle() throws Exception {
    Task a = makeTaskWithTitle("hello world");
    Task b = makeTaskWithTitle("byebye");
    Task c = makeTaskWithTitle("thanks");

    addAllTasks(a, b, c);

    SearchAction search = new SearchAction(goku);
    search.title = "hello";

    Result result = search.doIt();
    assertTrue(result.isSuccess());
    assertEquals(1, result.getTasks().size());
  }

  /*
   * This test isn't really deterministic.
   * It is just to ensure that a misspelled search title will return
   * some sane results.
   */
  @Test
  public void searchTitle_fuzzy_returnsTaskWithCloseTitles() throws Exception {
    Task a = makeTaskWithTitle("hello world");
    Task b = makeTaskWithTitle("byebye");
    Task c = makeTaskWithTitle("thanks");

    addAllTasks(a, b, c);

    SearchAction search = new SearchAction(goku);
    search.title = "byebey";

    Result result = search.doIt();
    assertTrue(result.isSuccess());
    assertEquals(1, result.getTasks().size());
  }

  /*
   * No Time Involved => Only compare by dates
   */
  @Test
  public void searchByDeadline_returnsTasksWithDeadlineBeforeWithoutTime()
      throws Exception {
    Task a = makeTaskWithDeadlineDaysLater("task a", 1);
    Task b = makeTaskWithDeadlineDaysLater("task b", 2);
    Task c = makeTaskWithDeadlineDaysLater("task c", 3);
    Task d = makeTaskWithPeriodDaysRelative("task d", 1, 3);

    addAllTasks(a, b, c, d);

    Task dueTask = makeTaskWithDeadlineDaysLater("due task", 2);

    SearchAction search = new SearchAction(goku);
    search.dline = dueTask.getDeadline();

    Result result = search.doIt();
    assertTrue(result.isSuccess());
    assertEquals(2, result.getTasks().size());
  }

  @Test
  public void searchByDeadline_returnsTaskWithDeadlineBeforeWithTime()
      throws Exception {
    Task a = makeTaskWithDeadlineDaysLaterWithTime("task a", 1, 10, 0, 0);
    Task b = makeTaskWithDeadlineDaysLaterWithTime("task b", 2, 14, 0, 0);
    Task c = makeTaskWithDeadlineDaysLater("task c", 3);
    Task d = makeTaskWithPeriodDaysRelative("task d", 1, 3);

    addAllTasks(a, b, c, d);

    Task dueTask = makeTaskWithDeadlineDaysLaterWithTime("due task", 2, 14, 0,
        0);

    SearchAction search = new SearchAction(goku);
    search.dline = dueTask.getDeadline();

    Result result = search.doIt();
    assertTrue(result.isSuccess());
    assertEquals(2, result.getTasks().size());
  }

  /*
   * ---  Time line -------------------------------->
   *   1    2  3  4   5   6    7    8   9  10   11 
   *           ##########################
   *   |    |-----|   |---|    |    |------|    |
   *   A       B        C      D       E        F 
   *   
   *   | - a task with a deadline at that point in time
   *   |-----| a task with a period starting at the first | and ending at second |
   *   ### - the period we wish to query for
   * Search should then return tasks, B, C, D, E.
   * B - end date is within period
   * C - entirely within period
   * D - deadline is within period
   * E - start date is within period
   */
  @Test
  public void searchByPeriod_returnsTasksWithinPeriodWithoutTime()
      throws Exception {
    Task a = makeTaskWithDeadlineDaysLater("task a", 1);
    Task b = makeTaskWithPeriodDaysRelative("task b", 2, 4);
    Task c = makeTaskWithPeriodDaysRelative("task c", 5, 6);
    Task d = makeTaskWithDeadlineDaysLater("task d", 7);
    Task e = makeTaskWithPeriodDaysRelative("task e", 8, 10);
    Task f = makeTaskWithDeadlineDaysLater("task f", 11);

    addAllTasks(a, b, c, d, e, f);

    Task periodTask = makeTaskWithPeriodDaysRelative("periodTask", 3, 9);

    SearchAction search = new SearchAction(goku);
    search.period = periodTask.getDateRange();

    Result result = search.doIt();
    assertTrue(result.isSuccess());
    assertEquals(4, result.getTasks().size());
  }

  /*
   * Similar test to previous, but involves search period with time
   */
  @Test
  public void searchByPeriod_returnsTasksWithinPeriodWithTime()
      throws Exception {
    Task a = makeTaskWithDeadlineDaysLater("task a", 1);
    Task b = makeTaskWithPeriodDaysRelative("task b", 2, 4);
    Task c = makeTaskWithPeriodDaysRelative("task c", 5, 6);
    Task d = makeTaskWithDeadlineDaysLater("task d", 7);
    Task e = makeTaskWithDeadlineDaysLaterWithTime("task e", 9, 15, 30, 0);
    Task f = makeTaskWithPeriodDaysRelative("task f", 8, 10);
    Task g = makeTaskWithDeadlineDaysLaterWithTime("task g", 11, 10, 0, 0);

    addAllTasks(a, b, c, d, e, f, g);

    Task periodTask = makeTaskWithPeriodDaysRelativeWithTime("periodTask", 3,
        10, 0, 0, 9, 15, 30, 0);

    SearchAction search = new SearchAction(goku);
    search.period = periodTask.getDateRange();

    Result result = search.doIt();
    assertTrue(result.isSuccess());
    assertEquals(5, result.getTasks().size());
  }

  /*
   * ---  Time line -------------------------------->
   *   1    2  3  4   5   6    7    8   9  10   11 
   *           ##########################
   *   |    |-----|   |---|    |    |------|    |
   *   A       B        C      D       E        F 
   *   
   *   | - a task with a deadline at that point in time
   *   |-----| a task with a period starting at the first | and ending at second |
   *   ### - the period we wish to query for
   * Search should then return task D only.
   * B - is within the period but does not have a deadline
   * C - same as B
   * E - same as C
   * D - within the period, and has a deadline
   */
  @Test
  public void searchByPeriodAndDeadline_returnsTasksWithDeadlineWithinPeriodWithoutTime()
      throws Exception {
    Task a = makeTaskWithDeadlineDaysLater("task a", 1);
    Task b = makeTaskWithPeriodDaysRelative("task b", 2, 4);
    Task c = makeTaskWithPeriodDaysRelative("task c", 5, 6);
    Task d = makeTaskWithDeadlineDaysLater("task d", 7);
    Task e = makeTaskWithPeriodDaysRelative("task e", 8, 10);
    Task f = makeTaskWithDeadlineDaysLater("task f", 11);

    addAllTasks(a, b, c, d, e, f);

    Task periodTask = makeTaskWithPeriodDaysRelative("periodTask", 3, 9);
    Task dueTask = makeTaskWithDeadlineDaysLater("dueTask", 8);

    SearchAction search = new SearchAction(goku);
    search.dline = dueTask.getDeadline();
    search.period = periodTask.getDateRange();

    Result result = search.doIt();
    assertTrue(result.isSuccess());
    assertEquals(1, result.getTasks().size());
  }

  /*
   * Similar to previous test, except deadline query now indicates time
   */
  @Test
  public void searchByPeriodAndDeadline_returnsTasksWithDeadlineWithinPeriodWithDeadlineTime()
      throws Exception {
    Task a = makeTaskWithDeadlineDaysLater("task a", 1);
    Task b = makeTaskWithPeriodDaysRelative("task b", 2, 4);
    Task c = makeTaskWithPeriodDaysRelative("task c", 5, 6);
    Task d = makeTaskWithDeadlineDaysLaterWithTime("task d", 8, 23, 50, 0);
    Task e = makeTaskWithPeriodDaysRelative("task e", 8, 10);
    Task f = makeTaskWithDeadlineDaysLater("task f", 11);

    addAllTasks(a, b, c, d, e, f);

    Task periodTask = makeTaskWithPeriodDaysRelative("periodTask", 3, 9);
    Task dueTask = makeTaskWithDeadlineDaysLater("dueTask", 8);

    SearchAction search = new SearchAction(goku);
    search.dline = dueTask.getDeadline();
    search.period = periodTask.getDateRange();

    Result result = search.doIt();
    assertTrue(result.isSuccess());
    assertEquals(1, result.getTasks().size());
  }

  /*
   * Query date well within period of a task
   * Returns false
   */
  @Test
  public void checkIfFree_dateQueryWithinPeriodOfTask() throws Exception {
    Task task = makeTaskWithPeriodDaysRelativeWithTime("task a", 0, 0, 0, 0, 5, 23, 59, 59);
    addAllTasks(task);

    SearchAction search = new SearchAction(goku);
    search.freeDateQuery = DateUtil.getNow().plusDays(1);
    assertNotNull(search.freeDateQuery.getHour());

    Result result = search.doIt();
    assertFalse(result.isSuccess());
  }

  /*
   * Query date on boundary of period of a task
   * Returns false
   */
  @Test
  public void checkIfFree_dateQueryOnBoundaryOfPeriodOfTask() throws Exception {
    Task task = makeTaskWithPeriodDaysRelativeWithTime("task a", 0, 0, 0, 0, 5, 23, 59, 59);
    addAllTasks(task);

    SearchAction search = new SearchAction(goku);
    search.freeDateQuery = task.getDateRange().getEndDate();
    assertNotNull(search.freeDateQuery.getHour());

    Result result = search.doIt();
    assertFalse(result.isSuccess());
  }

  /*
   * Query date outside period of task
   * Returns true
   */
  @Test
  public void checkIfFree_dateQueryOutsidePeriodOfTask() throws Exception {
    Task task = makeTaskWithPeriodDaysRelativeWithTime("task a", 1, 0, 0, 0, 5, 23, 59, 59);
    addAllTasks(task);

    SearchAction search = new SearchAction(goku);
    search.freeDateQuery = DateUtil.getNow();
    assertNotNull(search.freeDateQuery.getHour());

    Result result = search.doIt();
    assertTrue(result.isSuccess());
  }

  /*
   * Query date in system where task has no period
   * Returns true always
   */
  @Test
  public void checkIfFree_dateQueryOnTasksWithNoPeriods() throws Exception {
    Task task = makeTaskWithTitle("task a");
    addAllTasks(task);

    SearchAction search = new SearchAction(goku);
    search.freeDateQuery = DateUtil.getNow();
    assertNotNull(search.freeDateQuery.getHour());

    Result result = search.doIt();
    assertTrue(result.isSuccess());
  }
  
  /*
   * Returns free time slots given day
   */
  @Test
  public void checkIfFree_dateQueryFindTimeSlots() throws Exception {
    Task task = makeTaskWithPeriodDaysRelativeWithTime("task a", 0, 0, 0, 0, 5, 23, 59, 59);
    addAllTasks(task);

    SearchAction search = new SearchAction(goku);
    search.freeDateQuery = DateUtil.getNow().plusDays(1);

    Result result = search.doIt();
    assertFalse(result.isSuccess());
  }

  private Task makeTaskWithTitle(String title) {
    Task task = new Task();
    task.setTitle(title);
    return task;
  }

  private Task makeTaskWithPeriodDaysRelative(String title, int startDaysLater,
      int endDaysLater) throws MakeActionException, InvalidDateRangeException {
    Task task = new Task();
    task.setTitle(title);
    DateTime start = DateUtil.getNowDate().plusDays(startDaysLater);
    DateTime end = DateUtil.getNowDate().plusDays(endDaysLater);
    DateRange period = new DateRange(start, end);
    task.setPeriod(period);
    return task;
  }

  private Task makeTaskWithPeriodDaysRelativeWithTime(String title,
      int startDaysLater, int startHour, int startMin, int startSec,
      int endDaysLater, int endHour, int endMin, int endSec) throws MakeActionException, InvalidDateRangeException {
    Task task = new Task();
    task.setTitle(title);
    DateTime start = DateUtil.getNowDate().plus(0, 0, startDaysLater,
        startHour, startMin, startSec, 0, DateTime.DayOverflow.Spillover);
    DateTime end = DateUtil.getNowDate().plus(0, 0, endDaysLater, endHour,
        endMin, endSec, 0, DateTime.DayOverflow.Spillover);
    DateRange period = new DateRange(start, end);
    task.setPeriod(period);
    return task;
  }

  private Task makeTaskWithDeadlineDaysLater(String title, int daysLater) {
    Task task = new Task();
    task.setTitle(title);
    DateTime deadline = DateUtil.getNowDate().plusDays(daysLater);
    task.setDeadline(deadline);
    return task;
  }

  private Task makeTaskWithDeadlineDaysLaterWithTime(String title,
      int daysLater, int hour, int min, int sec) {
    Task task = new Task();
    task.setTitle(title);
    DateTime deadline = DateUtil.getNowDate().plusDays(daysLater)
        .plus(0, 0, 0, hour, min, sec, 0, DateTime.DayOverflow.Spillover);
    task.setDeadline(deadline);
    return task;
  }

  private void addAllTasks(Task... task) {
    for (Task t : task) {
      list.addTask(t);
    }
  }

}
