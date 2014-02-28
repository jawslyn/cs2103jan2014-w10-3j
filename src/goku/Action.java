package goku;

public abstract class Action {
  Command command;

  abstract Result doIt();

  abstract String getSuccessMsg(String msg, Object... args);

  abstract String getErrorMsg(String msg, Object... args);
}
