package goku.action;

import goku.GOKU;
import goku.Result;
import goku.Task;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
public class EditAction extends Action {
  public EditAction(GOKU goku) {
    super(goku);
    // TODO Auto-generated constructor stub
  }

  private final String MSG_SUCCESS = "edited task";

  public int id;
  public String deadline;
  public String from;
  public String to;
  public String title;

  public Result updateTask() {
    Task taskWithEdits = new Task();
    taskWithEdits.setTitle(title);
    Task t = list.getTaskById(id);
    t.updateWith(taskWithEdits);
    return new Result(true, MSG_SUCCESS, null, null);
  }

  @Override
  public Result doIt() {
    return updateTask();
  }

}
