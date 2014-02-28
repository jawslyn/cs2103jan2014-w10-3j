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
        add.setCommand(command);
        add.doIt();
        break;
      case DELETE :
        Delete delete = new Delete();
        delete.setCommand(command);
        delete.doIt();
        // delete.deleteTask(command.getTask());
        break;
      case DISPLAY :
        Display display = new Display();
        display.setCommand(command);
        display.doIt();
        // if (command.getTask().getDeadline() != null) {
        // display.displayDate();
        // } else {
        // display.displayAll();
        // }
        break;
      case SEARCH :
        Search search = new Search();
        search.setCommand(command);
        search.doIt();
        break;
      case EDIT :
        Edit edit = new Edit();
        edit.setCommand(command);
        edit.doIt();
        break;
    }
  }

  public static ArrayList<Task> getAllTasks() {
    return allTasks;
  }

  public GOKU() {
  }

}
