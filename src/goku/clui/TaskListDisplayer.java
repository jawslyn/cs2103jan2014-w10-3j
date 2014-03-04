package goku.clui;

import goku.Task;
import goku.TaskList;

import java.io.PrintStream;

public class TaskListDisplayer {
  PrintStream ps;

  public TaskListDisplayer(PrintStream ps) {
    this.ps = ps;
  }

  public void display(TaskList list) {
    for (Task t : list) {
      ps.println(t.toString());
    }
  }
}
