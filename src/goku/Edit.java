package goku;

import java.util.ArrayList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Edit extends Action {
  private final String MSG_SUCCESS = "edited task";
  private final String MSG_FAILURE = "unable to edit";

  private int taskId;
  private Task taskWithEdits;

  @Override
  Result doIt() {
    return updateTask();
  }

  /*
   * Called by ActionFactory on all actions to build the needed objects for this
   * Action
   */
  @Override
  public void construct() {
    // TODO Auto-generated method stub
    taskWithEdits = command.getTask();
    taskId = taskWithEdits.getId();
  }

  public Result updateTask() {
    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      if (GOKU.getAllTasks().get(i).getId() == taskId) {
        result.add(GOKU.getAllTasks().get(i));
        if (taskWithEdits.getTitle() != null) {
          GOKU.getAllTasks().get(i).setTitle(taskWithEdits.getTitle());
        }
        if (taskWithEdits.getDeadline() != null) {
          GOKU.getAllTasks().get(i).setDeadline(taskWithEdits.getDeadline());
        }
        if (taskWithEdits.getDateRange() != null) {
          GOKU.getAllTasks().get(i).setPeriod(taskWithEdits.getDateRange());
        }
        if (taskWithEdits.getTags() != null) {
          GOKU.getAllTasks().get(i).setTags(taskWithEdits.getTags());
        }
        if (taskWithEdits.getNotes() != null) {
          GOKU.getAllTasks().get(i).setNotes(taskWithEdits.getNotes());
        }
        if (taskWithEdits.getImportance() != null) {
          GOKU.getAllTasks().get(i)
              .setImportance(taskWithEdits.getImportance());
        }
      }
    }
    return new Result(true, null, null, result);
  }
}
