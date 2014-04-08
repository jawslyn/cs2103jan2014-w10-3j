package goku.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a history of user input as strings and allows callers to look through
 * this list in order.
 * 
 * @author ZhiAn
 * 
 */
public class InputHistory {
  List<String> history;
  public int current; // index of current history pointed to

  public InputHistory() {
    history = new ArrayList<>();
    history.add("");
    history.add("");
    current = 1;
  }

  public int size() {
    return history.size() - 2;
  }

  public void write(String string) {
    history.add(history.size() - 1, string);
    current = history.size() - 1;
  }

  public String getPrevious() {
    if (current == 0) {
      return "";
    } else {
      return history.get(--current);
    }
  }

  public String getNext() {
    if (current == history.size() - 1) {
      return "";
    } else {
      return history.get(++current);
    }
  }
}
