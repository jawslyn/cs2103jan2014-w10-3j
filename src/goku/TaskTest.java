package goku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import goku.util.DateUtil;
import hirondelle.date4j.DateTime;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class TaskTest {
  private Task task1;
  private Task task2;
  private Task task3;

  private Gson gson;

  @Before
  public void setup() {
    gson = new Gson();

    task1 = new Task();
    task1.setId(1);
    task1.setTitle("title");
    task1.setStatus(false);

    task2 = new Task();
    task2.setId(10);

    task3 = new Task();
    task3.setId(11);
  }

  @Test
  public void isDueOn_aDateInFuture_returnsTrue() throws Exception {
    DateTime now = DateUtil.getNowDate();
    DateTime future = now.plusDays(10);
    Task task = new Task();
    task.setDeadline(now);
    assertTrue(task.isDueOn(future));
  }

  @Test
  public void isDueOn_aDateInPast_returnsFalse() throws Exception {
    DateTime now = DateUtil.getNowDate();
    DateTime past = now.minusDays(10);
    Task task = new Task();
    task.setDeadline(now);
    assertFalse(task.isDueOn(past));
  }

  @Test
  public void Gson_returnCorrectString() {
    String string1 = gson.toJson(task1);
    Task taskA = gson.fromJson(string1, Task.class);
    assertEquals(task1, taskA);
    assertEquals(task1.getTitle(), taskA.getTitle());
    String string2 = gson.toJson(task2);
    Task taskB = gson.fromJson(string2, Task.class);
    assertEquals(task2, taskB);
    String string3 = gson.toJson(task3);
    Task taskC = gson.fromJson(string3, Task.class);
    assertEquals(task3, taskC);
  }
}