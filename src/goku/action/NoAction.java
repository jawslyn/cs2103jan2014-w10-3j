//@author A0099858Y
package goku.action;

import goku.GOKU;
import goku.Result;

/*
 * Represents an empty Action, an Action that does nothing,
 * has no feedback, but is successful.
 */
public class NoAction extends Action {
  public NoAction(GOKU goku) {
    super(goku);
  }

  @Override
  public Result doIt() {
    return new Result(true, null, null, null);
  }
}
