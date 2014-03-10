package goku.action;

import goku.DateRange;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;

import java.util.Date;

public class SearchAction extends Action {

  public String title;
  public String deadline;
  public String from;
  public String to;
  public Date dline;
  public DateRange period;

  public SearchAction(GOKU goku) {
    super(goku);

    // initialise params to null
    deadline = null;
    from = null;
    to = null;
    dline = null;
    period = null;
  }

  private static final String MSG_SUCCESS = "Found tasks!";
  private static final String MSG_FAIL = "No relevant tasks.";
  public static final String ERR_INSUFFICIENT_ARGS = "Can't search! Try \"search title\"";

  public Result searchTitle() {
    Task task = new Task();
    task.setTitle(title);
    TaskList foundTasks = list.findTaskByTitle(task);
    return new Result(true, MSG_SUCCESS, null, foundTasks);
  }

  public Result searchByDeadline() {
    Task task = new Task();
    task.setDeadline(dline);
    TaskList foundTasks = list.findTaskByDeadline(task);
    if (foundTasks.size() != 0) {
      return new Result(true, MSG_SUCCESS, null, foundTasks);
    } else {
      return new Result(false, null, MSG_FAIL, null);
    }
  }

  @Override
  public Result doIt() {
    if (dline != null) {
      return searchByDeadline();
    }
    return searchTitle();
  }

  public String getDeadline() {
    return deadline;
  }

  public String getTitle() {
    return title;
  }
}
