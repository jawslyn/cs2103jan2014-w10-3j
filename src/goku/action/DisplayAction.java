package goku.action;

import goku.GOKU;
import goku.Result;
import goku.Task;
import hirondelle.date4j.DateTime;

import java.util.List;

public class DisplayAction extends Action {

  private static final String MSG_SUCCESS = "Here are your tasks!";
  private static final String MSG_SUCCESS_OVERDUE = "Here are your overdue tasks!";
  private static final String MSG_HAS_OVERDUE = "You have overdue tasks, \"view overdue\" to see them.";
  DateTime byDeadline;
  public boolean viewComplete;
  public boolean viewOverdue;

  public DisplayAction(GOKU goku) {
    super(goku);
    shouldSaveAfter = false;
  }

  public Result displayOverdue() {
    return new Result(true, MSG_SUCCESS_OVERDUE, null, list.getOverdue());
  }

  public Result displayComplete() {
    return new Result(true, MSG_SUCCESS, null, list.getAllCompleted());
  }

  public Result displayDate() {
    assert (byDeadline != null);
    DateTime deadline = byDeadline;
    Task t = new Task();
    t.setDeadline(deadline);
    List<Task> result = list.findTaskByDeadline(deadline);
    return new Result(true, null, null, result);
  }

  public Result displayIncomplete() {
    String message = MSG_SUCCESS;
    if (list.getOverdue().size() != 0) {
      message += System.lineSeparator() + MSG_HAS_OVERDUE;
    }
    return new Result(true, message, null, list.getAllIncomplete());
  }

  @Override
  public Result doIt() {
    if (viewComplete == true) {
      return displayComplete();
    } else if (viewOverdue == true) {
      return displayOverdue();
    } else {
      return displayIncomplete();
    }
  }

}
