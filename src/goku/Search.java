package goku;

import java.util.ArrayList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Search extends Action {
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
    ArrayList<Task> result = new ArrayList<Task>();
    String tempString = taskToSearchFor.getTags().toString();
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      String[] tempArray = GOKU.getAllTasks().get(i).getTags();
      for (String element : tempArray) {
        if (element.contains(tempString)) {
          result.add(GOKU.getAllTasks().get(i));
        }
      }
    }

    return new Result(true, null, null, result);
  }

  public Result searchTitle() {
    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      if (GOKU.getAllTasks().get(i).getTitle()
          .contains(taskToSearchFor.getTitle())) {
        result.add(GOKU.getAllTasks().get(i));
      }
    }

    return new Result(true, null, null, result);
  }

}