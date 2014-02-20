package goku;

/*
 * An immutable data object which represents a user intent to engage in some
 * action with G.O.K.U.
 */
public final class Command {
  enum Type {
    ADD, DISPLAY, EDIT, DELETE, SEARCH
  };

  enum SortOrder {
    EARLIEST_DEADLINE_FIRST, HIGHEST_PRIORITY_FIRST
  }

  private String source;
  private Type type;
  private Task task;
  private SortOrder sortOrder;

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

  public static Builder builder() {
    return new Builder();
  }

  /*
   * A builder for creating immutable command instances. The various setX
   * methods permits setting the same fields multiple times. The actual Command
   * built will only depend on the latest values of those fields.
   */
  public static class Builder {
    enum POS {
      SOURCE, TYPE, TITLE, DEADLINE, STARTDATE, ENDDATE
    };

    private static final int POS_SOURCE = 0;
    private static final int POS_TYPE = 1;
    private static final int POS_TASK = 2;
    private static final int POS_SORT_ORDER = 3;

    Object[] attributes;

    public Builder() {
      attributes = new Object[4];
    }

    public Builder setSource(String source) {
      attributes[POS_SOURCE] = source;
      return this;
    }

    public Builder setType(Type type) {
      attributes[POS_TYPE] = type;
      return this;
    }

    public Builder setTask(Task task) {
      attributes[POS_TASK] = new Task(task);
      return this;
    }

    public Builder setSortOrder(SortOrder order) {
      attributes[POS_SORT_ORDER] = order;
      return this;
    }

    public Command build() {
      Command c = new Command();
      c.source = (String) attributes[POS_SOURCE];
      c.type = (Type) attributes[POS_TYPE];
      c.task = (Task) attributes[POS_TASK];
      c.sortOrder = (SortOrder) attributes[POS_SORT_ORDER];
      return c;
    }
  }

}
