package goku.action;

import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;
import hirondelle.date4j.DateTime;

public class DisplayAction extends Action {

  private static final String MSG_SUCCESS = "displaying list of task:";
  DateTime byDeadline;
  public boolean viewComplete;

  public DisplayAction(GOKU goku) {
    super(goku);
  }

  public Result displayAll() {
    return new Result(true, MSG_SUCCESS, null, list.asList());
  }

  public Result displayComplete() {
    return new Result(true, MSG_SUCCESS, null, list.getAllCompleted().asList());
  }

  public Result displayDate() {
    assert (byDeadline != null);
    DateTime deadline = byDeadline;
    Task t = new Task();
    t.setDeadline(deadline);
    TaskList result = list.findTaskByDeadline(t);
    return new Result(true, null, null, result.asList());
  }

  public Result displayIncomplete() {
    return new Result(true, MSG_SUCCESS, null, list.getAllIncomplete().asList());
  }

  @Override
  public Result doIt() {
    if (viewComplete == false) {
      return displayIncomplete();
    } else {
      return displayComplete();
    }
  }

}
