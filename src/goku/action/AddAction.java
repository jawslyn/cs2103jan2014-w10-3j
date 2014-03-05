package goku.action;

import goku.DateRange;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;

import java.util.Date;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
public class AddAction extends Action {
  private static final String MSG_SUCCESS = "Added: \"%s\"";
  private static final String ERR_FAIL = "Fail to add: \"%s\"";

  public String title;
  public String deadline;
  public String from;
  public String to;
  public TaskList list;
  public Date dline;
  public DateRange period;

  public AddAction(GOKU goku) {
    super(goku);
    list = goku.getTaskList();
  }

  private Result addTask() {
    addToUndoList();
    Task task = makeTask();
    boolean success = list.addTask(task);
    if (success) {
      return successAddTask();
    } else {
      return failedToAddTask();
    }
  }

  public void addToUndoList() {
    TaskList beforeAddList = new TaskList();
    for (Task t : list.getArrayList()) {
      beforeAddList.addUndoTask(t);
    }
    goku.getUndoList().offer(beforeAddList);
  }

  @Override
  public Result doIt() {
    return addTask();
  }

  private Result failedToAddTask() {
    Result result = Result.makeFailureResult();
    result.setErrorMsg(String.format(ERR_FAIL, title));
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
    task.setTitle(title);
    task.setDeadline(dline);
    task.setPeriod(period);
    return task;
  }

  private Result successAddTask() {
    Result result = Result.makeSuccessResult();
    result.setSuccessMsg(String.format(MSG_SUCCESS, title));
    result.setTasks(list);
    return result;
  }
}