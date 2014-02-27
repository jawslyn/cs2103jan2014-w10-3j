package goku;

import java.io.IOException;
import java.util.ArrayList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Display {
  private static final String EMPTY = "there are no tasks";

  public static void main(String[] args) throws IOException {

  }

  public Display() {

  }

  public void displayAll(Command command) {
    displayComplete(command);
    displayIncomplete(command);
  }

  public Result displayComplete(Command command) {
    if (GOKU.getAllTasks().isEmpty()) {
      return new Result(false, null, EMPTY, null);
    }

    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      if (GOKU.getAllTasks().get(i).getStatus() == true) {
        result.add(GOKU.getAllTasks().get(i));
      }
    }

    return new Result(true, null, null, result);
  }

  public Result displayDate(Command command) {
    if (GOKU.getAllTasks().isEmpty()) {
      return new Result(false, null, EMPTY, null);
    }

    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      if (GOKU.getAllTasks().get(i).getDeadline()
          .equals(command.getTask().getDeadline())) {
        ;
      }
      result.add(GOKU.getAllTasks().get(i));
    }

    return new Result(true, null, null, result);
  }

  public Result displayIncomplete(Command command) {
    if (GOKU.getAllTasks().isEmpty()) {
      return new Result(false, null, EMPTY, null);
    }

    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      if (GOKU.getAllTasks().get(i).getStatus() == false) {
        result.add(GOKU.getAllTasks().get(i));
      }
    }

    return new Result(true, null, null, result);
  }

}