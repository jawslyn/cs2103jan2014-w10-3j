package goku.ui;

import goku.GOKU;
import goku.Result;
import goku.action.Action;

import com.google.common.base.Joiner;

public class HelpAction extends Action {
  private static final String ADD_MSG = "use \"add\" to add tasks: add task name";
  private static final String ADD_DEADLINE_MSG = "specify deadline: add task by tomrrow 3pm";
  private static final String ADD_PERIOD_MSG = "specify period: add task from 3/4 3pm to 7/4 1pm";
  private static final String EDIT_MSG = "use \"edit\" to edit tasks: edit 1 new name";
  private static final String DELETE_MSG = "use \"delete\" to delete tasks: delete 1";
  private static final String UNDO_MSG = "use \"undo\" to undo your last action";
  private static final String SEARCH_MSG = "use \"search\" to look for tasks: search by friday ";

  public HelpAction(GOKU goku) {
    super(goku);
  }

  @Override
  public Result doIt() {
    String MSG = Joiner.on('\n').join(ADD_MSG, ADD_DEADLINE_MSG,
        ADD_PERIOD_MSG, EDIT_MSG, DELETE_MSG, UNDO_MSG, SEARCH_MSG);
    return new Result(true, MSG, null, null);
  }
}
