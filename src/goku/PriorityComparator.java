package goku;

import goku.Task.Importance;

import java.util.Comparator;

public class PriorityComparator implements Comparator<Task> {

  @Override
  public int compare(Task a, Task b) {
    // First we don't compare with tasks are null
    if (a == null || b == null) {
      throw new NullPointerException();
    }
    Importance imptA = a.getImportance();
    Importance imptB = b.getImportance();

    // Since both tasks do not have deadline, we compare base on their id, which
    // is analogous to the date of creation of task
    if (imptA == null && imptB == null) {
      return Integer.compare(a.getId(), b.getId());
    } else if (imptA == null && imptB != null) {
      return 1;
    } else if (imptA != null && imptB == null) {
      return -1;
    } else if (imptA.ordinal() == imptB.ordinal()) {
      return Integer.compare(a.getId(), b.getId());
    } else {
      return Integer.compare(imptA.ordinal(), imptB.ordinal());
    }
  }
}
