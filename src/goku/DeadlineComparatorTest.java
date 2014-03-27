package goku;

import static org.junit.Assert.assertEquals;
import goku.util.DateUtil;
import hirondelle.date4j.DateTime;

import org.junit.Before;
import org.junit.Test;

public class DeadlineComparatorTest {
  DeadlineComparator comp;

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
    task.setId(1);
    
    int result = comp.compare(task, task);
    assertEquals(0, result);
    task.setDeadline(DateUtil.getNow());
    assertEquals(0, result);
  }

  @Test
  public void compare_twoTaskWithNoDeadline_returnsResultOfComparingId()
      throws Exception {
    Task a = new Task();
    Task b = new Task();
    a.setId(1);
    b.setId(2);
    
    int result = comp.compare(a, b);
    assertEquals(-1, result);
    result = comp.compare(b, a);
    assertEquals(1, result);
  }

  @Test
  public void compare_twoIdenticalTaskWithSameDeadline_returnsZero()
      throws Exception {
    Task task = new Task();
    task.setId(1);
    task.setDeadline(DateUtil.getNow());
    
    int result = comp.compare(task, task);
    assertEquals(0, result);
  }

  @Test
  public void compare_twoTaskWithSameDeadline_returnsZero() throws Exception {
    Task a = new Task();
    Task b = new Task();
    DateTime testTime = DateUtil.getNow();
    a.setId(1);
    b.setId(2);;
    a.setDeadline(testTime);
    b.setDeadline(testTime);
    
    int result = comp.compare(a, b);
    assertEquals(-1, result);
    result = comp.compare(b, a);
    assertEquals(1, result);
  }

  @Test
  public void compare_twoTaskWithDifferentDeadline_returns() throws Exception {
    Task a = new Task();
    Task b = new Task();
    DateTime testTime = DateUtil.getNow();
    DateTime testTimeAlt = DateUtil.parse("tomorrow");
    
    a.setDeadline(testTime);
    b.setDeadline(testTimeAlt);
    int result = comp.compare(a, b);
    assertEquals(-1, result);
    result = comp.compare(b, a);
    assertEquals(1, result);
  }

}
