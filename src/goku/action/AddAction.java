package goku.action;

import goku.DateRange;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;
import hirondelle.date4j.DateTime;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
public class AddAction extends Action {
  private static final String MSG_SUCCESS = "Added: \"%s\"";
  private static final String ERR_FAIL = "Fail to add: \"%s\"";
  public static final String ERR_INSUFFICIENT_ARGS = "Can't add! Need title. Try \"add my task! by tomorrow\"";
  private static final String MSG_HAS_OVERDUE = "[!] You have overdue tasks, \"view overdue\" to see them.";

  public String title;
  public String deadline;
  public String from;
  public String to;
  public TaskList list;
  public DateTime dline;
  public DateRange period;
  public boolean isImpt;

  public AddAction(GOKU goku) {
    super(goku);
    list = goku.getTaskList();
  }

  private Result addTask() {
    addToUndoList();
    Task task = makeTask();
    int newId = list.addTask(task);
    if (newId > 0) {
      return successAddTask();
    } else {
      return failedToAddTask();
    }
  }

  public void addToUndoList() {
    TaskList currList = new TaskList();
    for (Task t : list.getArrayList()) {
      currList.addTaskWithoutSettingId(t);
    }
    goku.getUndoList().offer(currList);
  }

  @Override
  public Result doIt() {
    return addTask();
  }

  public String editMsgIfHaveOverdue(String msg) {
    if (list.getOverdue().size() != 0) {
      msg += System.lineSeparator() + MSG_HAS_OVERDUE;
    }
    return msg;
  }

  private Result failedToAddTask() {
    Result result = Result.makeFailureResult();
    result.setErrorMsg(editMsgIfHaveOverdue(String.format(ERR_FAIL, title)));
    result.setTasks(list.getAllIncomplete());
    return result;
  }

  public String getDeadline() {
    return deadline;
  }

  public String getTitle() {
    return title;
  }

  private Task makeTask() {
    Task task = new Task();
    assert (title != null);
    task.setTitle(title);
    task.setDeadline(dline);
    task.setPeriod(period);
    task.setImpt(isImpt);
    return task;
  }

  private Result successAddTask() {
    Result result = Result.makeSuccessResult();
    result
        .setSuccessMsg(editMsgIfHaveOverdue(String.format(MSG_SUCCESS, title)));
    result.setTasks(list.getAllIncomplete());
    return result;
  }
}