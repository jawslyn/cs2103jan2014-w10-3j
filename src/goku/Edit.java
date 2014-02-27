package goku;

import java.io.IOException;
import java.util.ArrayList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Edit {

  public static void main(String[] args) throws IOException {

  }

  public Edit() {

  }

  public Result updateTask(Command command) {
    int ID = command.getTask().getId();
    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      if (GOKU.getAllTasks().get(i).getId() == ID) {
        result.add(GOKU.getAllTasks().get(i));
        if (command.getTask().getTitle() != null) {
          GOKU.getAllTasks().get(i).setTitle(command.getTask().getTitle());
        }
        if (command.getTask().getDeadline() != null) {
          GOKU.getAllTasks().get(i)
              .setDeadline(command.getTask().getDeadline());
        }
        if (command.getTask().getDateRange() != null) {
          GOKU.getAllTasks().get(i).setPeriod(command.getTask().getDateRange());
        }
        if (command.getTask().getTags() != null) {
          GOKU.getAllTasks().get(i).setTags(command.getTask().getTags());
        }
        if (command.getTask().getNotes() != null) {
          GOKU.getAllTasks().get(i).setNotes(command.getTask().getNotes());
        }
        if (command.getTask().getImportance() != null) {
          GOKU.getAllTasks().get(i)
              .setImportance(command.getTask().getImportance());
        }
      }

    }

    return new Result(true, null, null, result);

  }

}