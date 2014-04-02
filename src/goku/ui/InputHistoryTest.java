package goku.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InputHistoryTest {
  InputHistory history = new InputHistory();

  @Test
  public void history_remembersHistory() {
    history.write("1");
    history.write("2");
    history.write("3");
    assertEquals(3, history.size());
  }

  @Test
  public void history_getLastestFew() throws Exception {
    history.write("1");
    history.write("2");
    history.write("3");
    assertEquals("3", history.getPrevious());
    assertEquals("2", history.getPrevious());
    assertEquals("1", history.getPrevious());
    assertEquals("", history.getPrevious());
    assertEquals("", history.getPrevious());
    assertEquals("1", history.getNext());
    assertEquals("2", history.getNext());
    assertEquals("3", history.getNext());
    assertEquals("", history.getNext());
    assertEquals("3", history.getPrevious());
    history.write("4");
    assertEquals("4", history.getPrevious());
    assertEquals("3", history.getPrevious());
  }
}
