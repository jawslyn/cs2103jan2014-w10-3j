package goku;

import java.io.IOException;
import java.util.ArrayList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class GOKU {
  private static final String ADDED = "task added, taskid: [id]";
  private static final String COMPLETE = "task [id] marked as completed";
  private static final String DELETED = "task [id] deleted";
  private static final String DISPLAYED = "%d. %s\n";
  private static final String EMPTY = "%s is empty\n";

  private static GOKU goku;

  public static void main(String[] args) throws IOException {

  }

  private ArrayList<Task> allTasks;

  public GOKU(Task task) {
    allTasks = new ArrayList<Task>();
  }

  private Result addTask(Task task) {
    allTasks.add(task);

    return new Result(true, ADDED, null, allTasks);
  }

  private Result deleteTask(Task task) {
    for (int i = 0; i < allTasks.size(); i++) {
      if (allTasks.get(i) == task) {
        allTasks.remove(i);
      }
    }

    return new Result(true, DELETED, null, allTasks);
  }

  private void displayAll(Command command) {
    displayComplete(command);
    displayIncomplete(command);
  }

  private Result displayComplete(Command command) {
    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < allTasks.size(); i++) {
      if (allTasks.get(i).getStatus() == true) {
        result.add(allTasks.get(i));
      }
    }

    return new Result(true, null, null, result);
  }

  private Result displayIncomplete(Command command) {
    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < allTasks.size(); i++) {
      if (allTasks.get(i).getStatus() == false) {
        result.add(allTasks.get(i));
      }
    }

    return new Result(true, null, null, result);
  }

  private void executeCommand(Command command) {
    switch (command.getType()) {

      case ADD :
        addTask(command.getTask());
        break;
      case DELETE :
        deleteTask(command.getTask());
        break;
      case SEARCH :
        if (command.getTask().getTitle() != null) {
          searchTitle(command);
        } else {
          searchTag(command);
        }
        break;
      case DISPLAY :

    }
  }

  private Result searchTag(Command command) {
    ArrayList<Task> result = new ArrayList<Task>();
    String tempString = command.getTask().getTags().toString();
    for (int i = 0; i < allTasks.size(); i++) {
      String[] tempArray = allTasks.get(i).getTags();
      for (String element : tempArray) {
        if (element.contains(tempString)) {
          result.add(allTasks.get(i));
        }
      }
    }

    return new Result(true, null, null, result);
  }

  private Result searchTitle(Command command) {
    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < allTasks.size(); i++) {
      if (allTasks.get(i).getTitle().contains(command.getTask().getTitle())) {
        result.add(allTasks.get(i));
      }
    }

    return new Result(true, null, null, result);
  }

  private Result updateTask(Command command) {
    int ID = command.getTask().getId();
    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < allTasks.size(); i++) {
      if (allTasks.get(i).getId() == ID) {
        result.add(allTasks.get(i));
        if (command.getTask().getTitle() != null) {
          allTasks.get(i).setTitle(command.getTask().getTitle());
        }
        if (command.getTask().getDeadline() != null) {
          allTasks.get(i).setDeadline(command.getTask().getDeadline());
        }
        if (command.getTask().getDateRange() != null) {
          allTasks.get(i).setPeriod(command.getTask().getDateRange());
        }
        if (command.getTask().getTags() != null) {
          allTasks.get(i).setTags(command.getTask().getTags());
        }
        if (command.getTask().getNotes() != null) {
          allTasks.get(i).setNotes(command.getTask().getNotes());
        }
        if (command.getTask().getImportance() != null) {
          allTasks.get(i).setImportance(command.getTask().getImportance());
        }
      }

    }

    return new Result(true, null, null, result);

  }

}
