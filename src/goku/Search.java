package goku;


/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Search extends Action {
  private static final String MSG_SUCCESS = "Found tasks!";
  private Task taskToSearchFor;

  /*
   * Called by ActionFactory on all actions to build the needed objects for this
   * Action
   */
  @Override
  public void construct() {
    taskToSearchFor = command.getTask();
  }

  @Override
  Result doIt() {
    if (taskToSearchFor.getTitle() != null) {
      return searchTitle();
    } else {
      return searchTag();
    }
  }

  public Result searchTag() {
    TaskList foundTasks = list.findTaskByTags(taskToSearchFor);
    return new Result(true, MSG_SUCCESS, null, foundTasks);
  }

  public Result searchTitle() {
    TaskList foundTasks = list.findTaskByTitle(taskToSearchFor);
    return new Result(true, MSG_SUCCESS, null, foundTasks);
  }

}