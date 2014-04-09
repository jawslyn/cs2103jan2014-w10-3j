package goku.storage;

import goku.TaskList;

import java.util.List;

/**
 * @author ZhiAn
 * 
 */
@SuppressWarnings("serial")
public class LoadTasksException extends Exception {
  private TaskList loadedTasks;
  private String message;

  public LoadTasksException(List<Integer> errorLines, TaskList tasklist) {
    StringBuilder sb = new StringBuilder();
    sb.append("Failed to load some tasks at lines\n");
    for (Integer lineNumber : errorLines) {
      sb.append(lineNumber + ",");
    }
    sb.append(" please look at \"store.goku\".\n");
    sb.append("A backup has been made at store.goku.backup");
    message = sb.toString();
    loadedTasks = tasklist;
  }

  public TaskList getLoadedTasks() {
    return loadedTasks;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
