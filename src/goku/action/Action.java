package goku.action;

import goku.GOKU;
import goku.Result;
import goku.TaskList;

public abstract class Action {
  GOKU goku;
  TaskList list;
  boolean shouldSaveAfter;

  public Action(GOKU goku) {
    this.goku = goku;
    this.list = goku.getTaskList();
    this.shouldSaveAfter = true;
  }

  public boolean shouldSave() {
    return shouldSaveAfter;
  }

  public abstract Result doIt() throws MakeActionException;
}
