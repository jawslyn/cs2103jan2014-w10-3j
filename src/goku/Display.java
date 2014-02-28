package goku;

import java.util.ArrayList;
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
    if (command.getTask() == null) {
      displayAll = true;
    } else {
      byDeadline = command.getTask().getDeadline();
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
    if (GOKU.getAllTasks().isEmpty()) {
      return makeEmptyListResult();
    }

    return new Result(true, null, null, GOKU.getAllTasks());
  }

  public Result displayComplete() {
    if (GOKU.getAllTasks().isEmpty()) {
      return makeEmptyListResult();
    }

    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      if (GOKU.getAllTasks().get(i).getStatus() == true) {
        result.add(GOKU.getAllTasks().get(i));
      }
    }

    return new Result(true, null, null, result);
  }

  public Result displayDate() {
    if (GOKU.getAllTasks().isEmpty()) {
      return makeEmptyListResult();
    }

    Date deadline = byDeadline;

    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      if (GOKU.getAllTasks().get(i).getDeadline().equals(deadline)) {
        result.add(GOKU.getAllTasks().get(i));
      }
    }

    return new Result(true, null, null, result);
  }

  public Result displayIncomplete(Command command) {
    if (GOKU.getAllTasks().isEmpty()) {
      return makeEmptyListResult();
    }

    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      if (GOKU.getAllTasks().get(i).getStatus() == false) {
        result.add(GOKU.getAllTasks().get(i));
      }
    }

    return new Result(true, null, null, result);
  }

  @Override
  public void setCommand(Command command) {
    this.command = command;
  }

  private Result makeEmptyListResult() {
    return new Result(true, MSG_EMPTY, null, GOKU.getAllTasks());
  }

}