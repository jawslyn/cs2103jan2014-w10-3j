package goku.action;

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
  private final String MSG_SUCCESS = "edited task";

  public int id;

  public String deadline;

  public String from;
  public String to;
  public String title;
  public Date dline;

  public EditAction(GOKU goku) {
    super(goku);
    // TODO Auto-generated constructor stub
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
    return updateTask();
  }

  public Result updateTask() {
    TaskList beforeAddList = new TaskList();
    for (Task t : list.getArrayList()) {
      beforeAddList.addUndoTask(new Task(t));
    }
    goku.getUndoList().offer(beforeAddList);
    Task taskWithEdits = new Task();
    taskWithEdits.setTitle(title);
    taskWithEdits.setDeadline(dline);
    Task t = list.getTaskById(id);
    t.updateWith(taskWithEdits);
    return new Result(true, MSG_SUCCESS, null, null);
  }

}
