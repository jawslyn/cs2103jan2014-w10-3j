package goku.action;

import goku.GOKU;
import goku.Result;

import com.google.common.base.Joiner;

public class HelpAction extends Action {
  private static final String ADD_MSG = "use \"add\" to add tasks:\n\tadd task name";
  private static final String ADD_DEADLINE_MSG = "specify deadline:\n\tadd task by tomrrow 3pm";
  private static final String ADD_PERIOD_MSG = "specify period:\n\tadd task from 3/4 3pm to 7/4 1pm";
  private static final String EDIT_MSG = "use \"edit\" to edit tasks:\n\tedit 1 new name";
  private static final String DELETE_MSG = "use \"delete\" to delete tasks:\n\tdelete 1";
  private static final String SEARCH_MSG = "use \"search\" to look for tasks:\n\tsearch by friday ";
  private static final String UNDO_MSG = "use \"undo\" to undo your last action";

  public HelpAction(GOKU goku) {
    super(goku);
  }

  @Override
  public Result doIt() {
    String MSG = Joiner.on('\n').join(ADD_MSG, ADD_DEADLINE_MSG,
        ADD_PERIOD_MSG, EDIT_MSG, DELETE_MSG, SEARCH_MSG, UNDO_MSG);
    return new Result(true, MSG, null, null);
  }
}
