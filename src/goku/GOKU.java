package goku;

import java.io.IOException;
import java.util.ArrayList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class GOKU {
  private static final String ADDED = "task added taskid: [id]";
  private static final String COMPLETE = "task [id] marked as completed";
  private static final String DELETED = "task [id] deleted";
  private static final String DISPLAYED = "%d. %s\n";
  private static final String EMPTY = "%s is empty\n";

  private static GOKU goku;

  public static void main(String[] args) throws IOException {
    

  }

  private ArrayList<Task> allTasks;

  public GOKU(Task task) {
    goku = new GOKU(task);
    allTasks = new ArrayList<Task>();
    allTasks.add(task);
    System.out.println(ADDED);
  }
  
  private ArrayList<Task> createCompleted(ArrayList<Task> taskList) {
    ArrayList<Task> completed = new ArrayList<Task>();
    for (Task task : taskList) {
      if (task.getStatus() == true)
        completed.add(task);
    }
    
    return completed;
  }
  
  private ArrayList<Task> createIncomplete(ArrayList<Task> taskList) {
    ArrayList<Task> incomplete = new ArrayList<Task>();
    for (Task task : taskList) {
      if (task.getStatus())
        incomplete.add(task);
    }
    
    return incomplete;
  }
  
  private void displayAll(ArrayList<Task> taskList) {
	  for (Task task : taskList) {
		  
	  }	  
  }
  
private void executeCommand(Command command) {
	switch(command.getType()) {
	
	case ADD:
		addTask(command.getTask());
		break;
	case DELETE:
		deleteTask(command.getTask());
		break;
	}
}

private void addTask(Task task) {
	allTasks.add(task);
		
}

private void deleteTask(Task task) {
	for (int i = 0; i < allTasks.size(); i++) {
		if (allTasks.get(i) == task) {
			allTasks.remove(i);
		}
		
	}
}

private void displayAll(Command command) {
	displayComplete(command);
	displayIncomplete(command);
}

private void displayComplete(Command command) {
	for (int i = 0; i < allTasks.size(); i++) {
		if (allTasks.get(i).getStatus()) {
			display(allTasks.get(i));
		}
	}
}

private void displayIncomplete(Command command) {
	for (int i = 0; i < allTasks.size(); i++) {
		if !(allTasks.get(i).getStatus()) {
			display(allTasks.get(i));
		}
	}
}



private void searchTitle(Command command) {
	for (int i = 0; i < allTasks.size(); i++) {
		if (allTasks.get(i).getTitle().contains(command.getTask().getTitle())){
			display(allTasks.get(i));
		}
	}
}

private void searchTag(Command command) {
	String tempString = command.getTask().getTags().toString();
	for (int i = 0; i < allTasks.size(); i++) {
		String[] tempArray = allTasks.get(i).getTags();
		for (int j = 0; j < tempArray.length; j++) {
			if (tempArray[j].contains(tempString)){
			display(allTasks.get(i));
			}
		}
	}
}


private void display(Task task) {
	System.out.printf(DISPLAYED, task.getId(), task.getTitle());
}

  private void display(ArrayList<Task> taskList) {
    for (int i = 0; i < taskList.size(); i++) {
      System.out.printf(DISPLAYED, taskList.get(i).getId(), taskList.get(i).getTitle());
    }
  }
}



