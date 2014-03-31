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
public class EditAction extends Action {
  public static final String ERR_INSUFFICIENT_ARGS = "Can't edit, need ID! Try \"edit 1 new title!\"";

  public static final String ERR_INSUFFICIENT_ARGS_FOR_COMPLETION = "Can't complete, need ID! Try \"do 1\"";

  private final String MSG_SUCCESS = "edited task %d";

  public int id;

  public String title;
  public DateTime dline;
  public DateRange period;
  public Boolean isComplete;
  public boolean removeDeadline;
  public boolean removePeriod;
  public boolean removeImportant;

  public EditAction(GOKU goku) {
    super(goku);
    // TODO Auto-generated constructor stub
  }

  public void addToUndoList() {
    TaskList currList = new TaskList();
    for (Task t : goku.getTaskList().getArrayList()) {
      currList.addTaskWithoutSettingId(t);
    }

    TaskList newCurrList = new TaskList();
    for (Task t : currList) {
      Task newT = new Task(t);
      newCurrList.addTaskWithoutSettingId(newT);
    }

    goku.getUndoList().offer(newCurrList);
  }

  @Override
  public Result doIt() {
    if (removeDeadline) {
      doRemoveDeadline();
    } else if (removeImportant) {
      doRemoveImportant();
    } else if (removePeriod) {
      doRemovePeriod();
    }
    addToUndoList();
    return updateTask();
  }

  private void doRemovePeriod() {
    Task t = list.getTaskById(id);
    t.setPeriod(null);
  }

  private void doRemoveImportant() {
    Task t = list.getTaskById(id);
    t.setImpt(false);
  }

  private void doRemoveDeadline() {
    Task t = list.getTaskById(id);
    t.setDeadline(null);
  }

  public Result updateTask() {
    Task taskWithEdits = new Task();
    taskWithEdits.setTitle(title);
    taskWithEdits.setDeadline(dline);
    taskWithEdits.setPeriod(period);
    taskWithEdits.setStatus(isComplete);

    Task t = list.getTaskById(id);
    t.updateWith(taskWithEdits);

    return new Result(true, String.format(MSG_SUCCESS, id), null, null);
  }

}
