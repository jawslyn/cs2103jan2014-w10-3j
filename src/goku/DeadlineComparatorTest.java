package goku;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class DeadlineComparatorTest {
  DeadlineComparator comp;
  private static final long TEST_TIME = System.currentTimeMillis() + 1000000;
  private static final long TEST_TIME_ALT = TEST_TIME + 1000000;

  @Before
  public void setup() {
    comp = new DeadlineComparator();
  }

  @Test(expected = NullPointerException.class)
  public void compare_twoNullTasks_throwsException() {
    comp.compare(null, null);
  }

  @Test
  public void compare_twoIdenticalTask_returnsZero() throws Exception {
    Task task = new Task();
    int result = comp.compare(task, task);
    assertEquals(0, result);
    task.setDeadline(new Date(TEST_TIME));
    assertEquals(0, result);
  }

  @Test
  public void compare_twoTaskWithNoDeadline_returnsResultOfComparingId()
      throws Exception {
    Task a = new Task();
    Task b = new Task();
    int result = comp.compare(a, b);
    assertEquals(-1, result);
    result = comp.compare(b, a);
    assertEquals(1, result);
  }

  @Test
  public void compare_twoIdenticalTaskWithSameDeadline_reuturnsZero()
      throws Exception {
    Task task = new Task();
    task.setDeadline(new Date(TEST_TIME));
    int result = comp.compare(task, task);
    assertEquals(0, result);
  }

  @Test
  public void compare_twoTaskWithSameDeadline_returnsZero() throws Exception {
    Task a = new Task();
    Task b = new Task();
    a.setDeadline(new Date(TEST_TIME));
    b.setDeadline(new Date(TEST_TIME));
    int result = comp.compare(a, b);
    assertEquals(-1, result);
    result = comp.compare(b, a);
    assertEquals(1, result);
  }

  @Test
  public void compare_twoTaskWithDifferentDeadline_returns() throws Exception {
    Task a = new Task();
    Task b = new Task();
    a.setDeadline(new Date(TEST_TIME));
    b.setDeadline(new Date(TEST_TIME_ALT));
    int result = comp.compare(a, b);
    assertEquals(-1, result);
    result = comp.compare(b, a);
    assertEquals(1, result);
  }

}
