package goku;

import java.util.Date;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Display extends Action {
  // Result is a success, just that there are no tasks
  private static final String MSG_EMPTY = "there are no tasks";
  private static final String MSG_SUCCESS = "displaying list of task:";
  private static final String MSG_FAILURE = "unable to display";
  private boolean displayAll = false;
  private Date byDeadline;

  /*
   * Called by ActionFactory on all actions to build the needed objects for this
   * Action
   */
  @Override
  public void construct() {
    if (command.getTask().getDeadline() != null) {
      byDeadline = command.getTask().getDeadline();
    } else {
      displayAll = true;
    }
  }

  @Override
  Result doIt() {
    if (displayAll) {
      return displayAll();
    } else {
      return displayDate();
    }
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

  public Result displayIncomplete(Command command) {
    return new Result(true, MSG_SUCCESS, null, list.getAllIncomplete());
  }

  @Override
  public void setCommand(Command command) {
    this.command = command;
  }
}