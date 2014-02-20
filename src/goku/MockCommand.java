package goku;

import goku.Command.SortOrder;
import goku.Command.Type;

/*
 * Behaves the same was as Command, but with public fields. This is to
 * facilitate testing. MockCommand needs to be kept in sync with Command for
 * tests to be correct.
 */
public class MockCommand {
  public String source;
  public Type type;
  public Task task;
  public SortOrder sortOrder;

  public boolean isEquals(Command c) {
    return attributeIsEqual(source, c.getSource())
        && attributeIsEqual(type, c.getType())
        && attributeIsEqual(task, c.getTask())
        && attributeIsEqual(sortOrder, c.getSortOrder());
  }

  public boolean attributeIsEqual(Object a, Object b) {
    if (a == null) {
      return b == null;
    } else {
      return a.equals(b);
    }
  }
}
