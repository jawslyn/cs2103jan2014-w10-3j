package goku;

public abstract class Action {
  Command command;

  abstract Result doIt();

  abstract String getSuccessMsg(Object... args);

  abstract String getErrorMsg(Object... args);
}
