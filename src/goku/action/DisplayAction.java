package goku.action;

import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;

import java.util.Date;

public class DisplayAction extends Action {

  private static final String MSG_EMPTY = "there are no tasks";
  private static final String MSG_SUCCESS = "displaying list of task:";
  private static final String MSG_FAILURE = "unable to display";
  private boolean displayAll = false;
  Date byDeadline;

  public DisplayAction(GOKU goku) {
    super(goku);
  }

  @Override
  public Result doIt() {
    return displayAll();
  }

  public Result displayAll() {
    return new Result(true, MSG_SUCCESS, null, list);
  }

  public Result displayComplete() {
    return new Result(true, MSG_SUCCESS, null, list.getAllCompleted());
  }

  public Result displayDate() {
    Date deadline = byDeadline;
    Task t = new Task();
    t.setDeadline(deadline);
    TaskList result = list.findTaskByDeadline(t);
    return new Result(true, null, null, result);
  }

}
