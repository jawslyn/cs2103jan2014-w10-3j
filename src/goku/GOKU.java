package goku;

import java.util.ArrayList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class GOKU {

  private static ArrayList<Task> allTasks = new ArrayList<Task>();

  public static void executeCommand(Command command) {
    switch (command.getType()) {

      case ADD :
        Add add = new Add();
        add.addTask(command.getTask());
        break;
      case DELETE :
        Delete delete = new Delete();
        delete.deleteTask(command.getTask());
        break;
      case DISPLAY :
        Display display = new Display();
        if (command.getTask().getDeadline() != null) {
          display.displayDate(command.getTask());
        } else {
          display.displayAll();
        }
        break;
      case SEARCH :
        Search search = new Search();
        if (command.getTask().getTitle() != null) {
          search.searchTitle(command.getTask());
        } else {
          search.searchTag(command.getTask());
        }
        break;
      case EDIT :
        Edit edit = new Edit();
        edit.updateTask(command.getTask());
        break;
    }
  }

  public static ArrayList<Task> getAllTasks() {
    return allTasks;
  }

  public GOKU() {
  }

}
