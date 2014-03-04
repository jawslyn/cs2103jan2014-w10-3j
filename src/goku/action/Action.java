package goku.action;

import goku.GOKU;
import goku.Result;
import goku.TaskList;

public abstract class Action {
  GOKU goku;
  TaskList list;

  public Action(GOKU goku) {
    this.goku = goku;
    this.list = goku.getTaskList();
  }

  public abstract Result doIt();
}
