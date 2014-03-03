package goku.ui;

import goku.Task;
import goku.TaskList;

import java.io.PrintStream;

public class TaskListDisplayer {
  PrintStream ps;

  public TaskListDisplayer(PrintStream ps) {
    this.ps = ps;
  }

  public void display(TaskList list) {
    for (Task task : list) {
      ps.println(task.toString());
    }
  }
}
