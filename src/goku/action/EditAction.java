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
public class EditAction extends Action {
  public static final String ERR_INSUFFICIENT_ARGS = "Can't edit, need ID! Try \"edit 1 new title!\"";

  public static final String ERR_INSUFFICIENT_ARGS_FOR_COMPLETION = "Can't complete, need ID! Try \"do 1\"";

  private final String MSG_SUCCESS = "edited task %d";

  public int id;

  public String title;
  public Date dline;
  public DateRange period;
  public Boolean isComplete;

  public EditAction(GOKU goku) {
    super(goku);
    // TODO Auto-generated constructor stub
  }

  public void addToUndoList() {
    TaskList currList = new TaskList();
    for (Task t : list.getArrayList()) {
      currList.addUndoTask(t);
    }
    goku.getUndoList().offer(currList);
  }

  @Override
  public Result doIt() {
    return updateTask();
  }

  public Result updateTask() {
    addToUndoList();

    Task taskWithEdits = new Task();
    taskWithEdits.setTitle(title);
    taskWithEdits.setDeadline(dline);
    taskWithEdits.setPeriod(period);
    taskWithEdits.setComplete(isComplete);

    Task t = list.getTaskById(id);
    t.updateWith(taskWithEdits);

    return new Result(true, String.format(MSG_SUCCESS, id), null, null);
  }

}
