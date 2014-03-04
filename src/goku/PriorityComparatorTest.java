package goku;

import static org.junit.Assert.assertEquals;
import goku.Task.Importance;

import org.junit.Before;
import org.junit.Test;

public class PriorityComparatorTest {
  public PriorityComparator comp;
  private static final Importance HIGH_IMPT = Importance.HIGH;
  private static final Importance MEDIUM_IMPT = Importance.MEDIUM;
  private static final Importance LOW_IMPT = Importance.LOW;

  @Before
  public void setup() {
    comp = new PriorityComparator();
  }

  @Test(expected = NullPointerException.class)
  public void compare_twoNullTasks_throwsException() {
    comp.compare(null, null);
  }

  @Test
  public void compare_twoIdenticalTasks_returnsZero() throws Exception {
    Task a = new Task();
    a.setImportance(HIGH_IMPT);
    int result = comp.compare(a, a);
    assertEquals(0, result);
  }

  @Test
  public void compare_twoTasksWithoutPriority_returnsCompareById()
      throws Exception {
    Task a = new Task();
    int result = comp.compare(a, a);
    assertEquals(0, result);
  }

  @Test
  public void compare_twoTasksWithSamePriority_returnsCompareById()
      throws Exception {
    Task a = new Task();
    a.setImportance(HIGH_IMPT);
    Task b = new Task();
    b.setImportance(MEDIUM_IMPT);
    int result = comp.compare(a, b);
    assertEquals(-1, result);
    result = comp.compare(b, a);
    assertEquals(1, result);
  }

  @Test
  public void compare_twoTasksWithDifferentPriority_returnsHigherPriority()
      throws Exception {
    Task a = new Task();
    a.setImportance(LOW_IMPT);
    Task b = new Task();
    b.setImportance(HIGH_IMPT);
    int result = comp.compare(a, b);
    assertEquals(1, result);
    result = comp.compare(b, a);
    assertEquals(-1, result);
  }

}
