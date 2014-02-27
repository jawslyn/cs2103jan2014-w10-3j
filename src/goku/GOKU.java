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
  
  
  private void display(ArrayList<Task> taskList) {
    for (int i = 0; i < taskList.size(); i++) {
      System.out.printf(DISPLAYED, i + 1, taskList.get(i).getTitle());
    }
  }
  


}



