package goku;

public abstract class Action {
  GOKU goku;
  Command command;
  String SUCCESS_MSG;
  String FAILURE_MSG;

  abstract Result doIt();

  public void setCommand(Command command) {
    this.command = command;
  }

  public abstract void construct();
}
