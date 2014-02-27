package goku;

import java.io.IOException;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Add {
  private static final String ADDED = "added \"%s\"";

  public static void main(String[] args) throws IOException {

  }

  public Add() {

  }

  public Result addTask(Task task) {
    // ArrayList<Task> list = GOKU.getAllTasks();
    // list.add(task);
    GOKU.getAllTasks().add(task);

    return new Result(true, String.format(ADDED, task.getTitle()), null,
        GOKU.getAllTasks());
  }

}