package goku;

import java.io.IOException;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Delete {
  private static final String DELETED = "deleted \"%s\"";

  public static void main(String[] args) throws IOException {

  }

  public Delete() {

  }

  public Result deleteTask(Task task) {
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      if (GOKU.getAllTasks().get(i) == task) {
        GOKU.getAllTasks().remove(i);
      }
    }

    return new Result(true, String.format(DELETED, task.getTitle()), null,
        GOKU.getAllTasks());
  }

}