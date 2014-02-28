package goku;

import java.util.ArrayList;

public class TaskList {
  private ArrayList<Task> _list;

  public TaskList() {
    _list = new ArrayList<Task>();
  }

  public boolean addTask(Task task) {
    return _list.add(task);
  }

  public Task getTaskById(int id) {
    for (Task task : _list) {
      if (task.getId() == id) {
        return task;
      }
    }
    return null;
  }

  public TaskList findTaskByTitle(Task toFind) {
    TaskList matches = new TaskList();
    for (Task task : _list) {
      if (task.titleMatches(toFind)) {
        matches.addTask(task);
      }
    }
    return matches;
  }

  public TaskList findTaskByTags(Task toFind) {
    TaskList matches = new TaskList();
    for (Task task : _list) {
      if (task.tagsMatch(toFind)) {
        matches.addTask(task);
      }
    }
    return matches;
  }

  public TaskList findTaskByDeadline(Task toFind) {
    return null;

  }

  public Task deleteTaskById(int id) {
    int index = getIndexOfTaskById(id);
    if (index < 0) {
      return null;
    } else {
      return _list.remove(index);
    }
  }

  public TaskList deleteTaskByTitle(Task toDelete) {
    TaskList matches = findTaskByTitle(toDelete);
    return deleteTask(matches);
  }

  public int size() {
    return _list.size();
  }

  public TaskList getAll() {
    return this;
  }

  private TaskList deleteTask(TaskList matches) {
    if (matches.size() == 1) {
      deleteTaskById(matches.getTaskByIndex(0).getId());
      return new TaskList();
    } else {
      return matches;
    }
  }

  private int getIndexOfTaskById(int id) {
    return _list.indexOf(getTaskById(id));
  }

  private Task getTaskByIndex(int index) {
    return _list.get(index);
  }
}
