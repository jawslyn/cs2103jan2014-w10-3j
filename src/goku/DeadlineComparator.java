package goku;

import java.util.Comparator;
import java.util.Date;

/*
 * Used to sort tasks according to EARLIEST_DEADLINE_FIRST order
 */
public class DeadlineComparator implements Comparator<Task> {

  @Override
  public int compare(Task a, Task b) throws NullPointerException {
    // First we don't compare with tasks are null
    if (a == null || b == null) {
      throw new NullPointerException();
    }
    Date deadlineA = a.getDeadline();
    Date deadlineB = b.getDeadline();

    // Since both tasks do not have deadline, we compare base on their id, which
    // is analogous to the date of creation of task
    if (deadlineA == null && deadlineB == null) {
      return Integer.compare(a.getId(), b.getId());
    } else if (deadlineA == null && deadlineB != null) {
      return 1;
    } else if (deadlineA != null && deadlineB == null) {
      return -1;
    } else if (deadlineA.before(deadlineB)) {
      return -1;
    } else if (deadlineA.after(deadlineB)) {
      return 1;
    } else {
      return Integer.compare(a.getId(), b.getId());
    }
  }
}
