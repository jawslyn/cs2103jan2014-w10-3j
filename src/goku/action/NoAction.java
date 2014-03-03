package goku.action;

import goku.GOKU;
import goku.Result;

public class NoAction extends Action {

  public NoAction(GOKU goku) {
    super(goku);
  }

  @Override
  public Result doIt() {
    return null;
  }

}
