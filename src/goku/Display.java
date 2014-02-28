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

  public Display() {

  }

  @Override
  Result doIt() {
    if (command.getTask().getDeadline() != null) {
      return displayDate();
    } else {
      return displayAll();
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

    Date deadline = new Date();

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

  public void setCommand(Command command) {
    this.command = command;
  }

  private Result makeEmptyListResult() {
    return new Result(true, MSG_EMPTY, null, GOKU.getAllTasks());
  }

}