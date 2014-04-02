package goku.action;

import goku.GOKU;
import goku.Result;
import goku.Task;
import hirondelle.date4j.DateTime;

import java.util.List;

public class DisplayAction extends Action {

  private static final String MSG_SUCCESS = "Here are your tasks!";
  private static final String MSG_SUCCESS_OVERDUE = "Here are your overdue tasks!";
  private static final String MSG_HAS_OVERDUE = "[!] You have overdue tasks, \"view overdue\" to see them.";
  private static final String MSG_NO_COMPLETED = "You have not completed any tasks!\nTry \"done [ID]\" to mark a task as completed!";
  private static final String MSG_NO_INCOMPLETE = "There is currently no upcoming tasks!\nTry \"add [TITLE] by [TIME] [DAY]\" or\n\"add [TITLE] from [TIME] [DAY] to [TIME] [DAY]\" to add a task now!\n";
  private static final String MSG_NO_OVERDUE = "No overdue tasks!";
  DateTime byDeadline;
  public boolean viewComplete;
  public boolean viewOverdue;

  public DisplayAction(GOKU goku) {
    super(goku);
    shouldSaveAfter = false;
  }

  public Result displayComplete() {
    if (list.getAllCompleted().isEmpty()) {
      return new Result(false, null, MSG_NO_COMPLETED, null);
    } else {
      return new Result(true, MSG_SUCCESS, null, list.getAllCompleted());
    }
  }

  public Result displayDate() {
    assert (byDeadline != null);
    DateTime deadline = byDeadline;
    Task t = new Task();
    t.setDeadline(deadline);
    List<Task> result = list.findTaskByDeadline(deadline);
    return new Result(true, null, null, result);
  }

  public Result displayIncomplete() {
    if (list.getAllIncomplete().isEmpty()) {
      return new Result(true, editMsgIfHaveOverdue(MSG_NO_INCOMPLETE), null,
          null);
    } else {
      return new Result(true, editMsgIfHaveOverdue(MSG_SUCCESS), null,
          list.getAllIncomplete());
    }
  }

  public Result displayOverdue() {
    if (list.getOverdue().isEmpty()) {
      return new Result(true, MSG_NO_OVERDUE, null, null);
    } else {
      return new Result(true, MSG_SUCCESS_OVERDUE, null, list.getOverdue());
    }
  }

  @Override
  public Result doIt() {
    if (viewComplete == true) {
      return displayComplete();
    } else if (viewOverdue == true) {
      return displayOverdue();
    } else {
      return displayIncomplete();
    }
  }

  public String editMsgIfHaveOverdue(String msg) {
    if (list.getOverdue().size() != 0) {
      msg += System.lineSeparator() + MSG_HAS_OVERDUE;
    }
    return msg;
  }

}
