package goku.action;

import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;

public class SearchAction extends Action {
  public SearchAction(GOKU goku) {
    super(goku);
    // TODO Auto-generated constructor stub
  }

  private static final String MSG_SUCCESS = "Found tasks!";

  public String title;

  public Result searchTitle() {
    Task task = new Task();
    task.setTitle(title);
    TaskList foundTasks = list.findTaskByTitle(task);
    return new Result(true, MSG_SUCCESS, null, foundTasks);
  }

  @Override
  public Result doIt() {
    return searchTitle();
  }
}
