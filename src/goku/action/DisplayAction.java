package goku.action;

import goku.GOKU;
import goku.Result;
import goku.Task;
import hirondelle.date4j.DateTime;

import java.util.List;

public class DisplayAction extends Action {

  private static final String MSG_SUCCESS = "Here are your tasks!";
  private static final String MSG_SUCCESS_COMPLETED = "Here are your completed tasks!";
  private static final String MSG_SUCCESS_OVERDUE = "Here are your overdue tasks!";
  private static final String MSG_HAS_OVERDUE = "[!] You have overdue tasks, \"view overdue\" to see them.";
  private static final String MSG_NO_COMPLETED = "You have not completed any tasks!\nEnter \"done [ID]\" to mark a task as completed!";
  private static final String MSG_NO_INCOMPLETE = "There is currently no upcoming tasks!\nAdd some tasks now :)";
  private static final String MSG_NO_OVERDUE = "No overdue tasks!";
  public static final String ERR_INVALID_DISPLAY = "Invalid display option!";
  DateTime byDeadline;
  public boolean viewComplete;
  public boolean viewOverdue;
  public boolean viewAll;

  public DisplayAction(GOKU goku) {
    super(goku);
    shouldSaveAfter = false;
  }

  public Result displayComplete() {
    if (list.getAllCompleted().isEmpty()) {
      return new Result(false, null, MSG_NO_COMPLETED, null);
    } else {
      return new Result(true, MSG_SUCCESS_COMPLETED, null,
          list.getAllCompleted());
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

  public Result displayAll() {
    if (list.size() == 0) {
      return new Result(false, null, MSG_NO_INCOMPLETE, null);
    } else {
      return new Result(true, MSG_SUCCESS, null, list.asList());
    }
  }

  @Override
  public Result doIt() {
    if (viewComplete == true) {
      return displayComplete();
    } else if (viewOverdue == true) {
      return displayOverdue();
    } else if (viewAll == true) {
      return displayAll();
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
