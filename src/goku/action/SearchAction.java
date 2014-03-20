package goku.action;

import goku.DateRange;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;
import goku.util.DateUtil;
import hirondelle.date4j.DateTime;

public class SearchAction extends Action {

  public String title;
  public String deadline;
  public String from;
  public String to;
  public DateTime dline;
  public DateRange period;
  public boolean testFree = false;
  public DateTime dateQuery;

  public SearchAction(GOKU goku) {
    super(goku);
  }

  private static final String MSG_SUCCESS = "Found tasks!";
  private static final String MSG_FAIL = "No relevant tasks.";
  private static final String IS_FREE = "Specified datetime is available.";
  private static final String NOT_FREE = "Specified datetime is not available.";
  public static final String ERR_INSUFFICIENT_ARGS = "Can't search! Try \"search title\"";
  public static final String ERR_NO_VALID_DATE_FOUND = "Can't search! Try entering a valid date after \"free\"";
  private static final String ERR_DEADLINE_PERIOD_CONFLICT = "Can't search! Conflicting deadline and period.";

  public Result searchTitle() {
    Task task = new Task();
    task.setTitle(title);
    TaskList foundTasks = list.findTaskByTitle(task);
    return new Result(true, MSG_SUCCESS, null, foundTasks);
  }

  public Result searchByDeadline() {
    Task task = new Task();

    assert (dline != null);
    task.setDeadline(dline);
    TaskList foundTasks = list.findTaskByDeadline(task);
    if (foundTasks.size() != 0) {
      return new Result(true, MSG_SUCCESS, null, foundTasks);
    } else {
      return new Result(false, null, MSG_FAIL, null);
    }
  }

  /*
   * Searches for tasks that fall within a specified period.
   * Tasks that start/end, or has deadline within the period is a match.
   * Example:
   * ---  Time line -------------------------------->
   *   1    2  3  4   5   6    7    8   9  10   11 
   *           ##########################
   *   |    |-----|   |---|    |    |------|    |
   *   A       B        C      D       E        F 
   *   
   *   | - a task with a deadline at that point in time
   *   |-----| a task with a period starting at the first | and ending at second |
   *   ### - the period we wish to query for
   * Search should then return tasks, B, C, D, E 
   */
  public Result searchByPeriod() {
    Task task = new Task();

    assert (period != null);
    task.setPeriod(period);
    TaskList foundTasks = list.findTaskByPeriod(task);
    if (foundTasks.size() != 0) {
      return new Result(true, MSG_SUCCESS, null, foundTasks);
    } else {
      return new Result(false, null, MSG_FAIL, null);
    }
  }

  public Result checkFreeTime() {

    assert (dateQuery != null);

    if (list.isFree(dateQuery) == true) {
      return new Result(true, IS_FREE, null, null);
    } else {
      return new Result(false, null, NOT_FREE, null);
    }
  }

  @Override
  public Result doIt() throws MakeActionException {

    Result result = null;

    if (dateQuery != null) {
      result = checkFreeTime();
    } else if (dline != null && period != null) {
      result = searchByDeadlineInPeriod();
    } else if (dline != null) {
      result = searchByDeadline();
    } else if (period != null) {
      result = searchByPeriod();
    } else {
      result = searchTitle();
    }

    return result;
  }

  /*
   * Searches for tasks within a given period that has the specified deadline.
   */
  private Result searchByDeadlineInPeriod() throws MakeActionException {
    // check for conflicting deadline and period
    if (DateUtil.isEarlierThan(dline, period.getStartDate())
        || DateUtil.isLaterThan(dline, period.getEndDate())) {
      throw new MakeActionException(ERR_DEADLINE_PERIOD_CONFLICT);
    }

    Result byPeriod = searchByPeriod();
    TaskList tasksDueInPeriod = new TaskList();

    for (Task task : byPeriod.getTasks()) {
      if (task.getDeadline() != null) {
        tasksDueInPeriod.addTaskWithoutSettingId(task);
      }
    }

    return new Result(true, MSG_SUCCESS, null, tasksDueInPeriod);
  }

  public String getDeadline() {
    return deadline;
  }

  public String getTitle() {
    return title;
  }
}
