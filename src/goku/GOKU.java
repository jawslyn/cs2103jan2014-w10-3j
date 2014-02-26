package goku;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class GOKU {
  private static final String ADDED = "task added taskid: [id]";
  private static final String COMPLETE = "task [id] marked as completed";
  private static final String DELETED = "task [id] deleted;
  private static final String DISPLAYED = "%d. %s\n";
  private static final String EMPTY = "%s is empty\n";
  
  private static GOKU goku;
  
  private static String addComplete(Task task)
      throws IOException {
    goku.completed.add(task);
    
    return String.format(COMPLETE);
  }
  private static void addIncomplete(Task task)
      throws IOException {
    goku.incomplete.add(task);
  }
  private static String addNoDate(Task task)
      throws IOException {
    goku.noDate.add(task);
    goku.allTasks.add(task);
    
    return String.format(ADDED);
  }
  private static String addWithDeadline(Task task)
      throws IOException {
    goku.withDeadline.add(task);
    goku.allTasks.add(task);
    
    return String.format(ADDED);
  }
  
  public static String addWithPeriod(Task task)
      throws IOException {
    goku.withPeriod.add(task);
    goku.allTasks.add(task);
    
    return String.format(ADDED);
  }
  private ArrayList<Task> allTasks;  
  
  private ArrayList<Task> withDeadline;
  
  
  private ArrayList<Task> withPeriod;
  
  private ArrayList<Task> noDate;
  
  private ArrayList<Task> completed;
  
  private ArrayList<Task> incomplete;
  
  public GOKU() {
    goku = new GOKU();
    allTasks = new ArrayList<Task>();
    withDeadline = new ArrayList<Task>();
    withPeriod = new ArrayList<Task>();
    noDate = new ArrayList<Task>();
    
    completed = new ArrayList<Task>();
    incomplete = new ArrayList<Task>();
    
  }
  
  
}


