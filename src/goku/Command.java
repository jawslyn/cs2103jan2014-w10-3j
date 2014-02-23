package goku;

/*
 * An immutable data object which represents a user intent to engage in some
 * action with G.O.K.U.
 */
public final class Command {
  public enum Type {
    ADD, DISPLAY, EDIT, DELETE, SEARCH
  };

  public enum SortOrder {
    EARLIEST_DEADLINE_FIRST, HIGHEST_PRIORITY_FIRST
  }

  private final String source;
  private final Type type;
  private final Task task;
  private final SortOrder sortOrder;

  public Command(String source, Type type, Task task) {
    this(source, type, task, SortOrder.EARLIEST_DEADLINE_FIRST);
  }

  public Command(String source, Type type, Task task, SortOrder sortOrder) {
    this.source = source;
    this.type = type;
    this.task = task;
    this.sortOrder = sortOrder;
  }

  public String getSource() {
    return source;
  }

  public Type getType() {
    return type;
  }

  public SortOrder getSortOrder() {
    return sortOrder;
  }

  public Task getTask() {
    return task;
  }
}
