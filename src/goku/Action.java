package goku;

public abstract class Action {
  Command command;
  String SUCCESS_MSG;
  String FAILURE_MSG;

  abstract Result doIt();

  String getSuccessMsg(Object... args) {
    return String.format(SUCCESS_MSG, args);
  }

  String getErrorMsg(Object... args) {
    return String.format(FAILURE_MSG, args);
  }
}
