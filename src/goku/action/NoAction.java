package goku.action;

import goku.GOKU;
import goku.Result;

public class NoAction extends Action {
  private static final String MSG_NONE = "Command invalid";

  public NoAction(GOKU goku) {
    super(goku);
  }

  @Override
  public Result doIt() {
    return new Result(true, MSG_NONE, null, null);
  }
}
