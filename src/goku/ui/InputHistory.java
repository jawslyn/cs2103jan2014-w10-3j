package goku.ui;

import java.util.ArrayList;
import java.util.List;

public class InputHistory {
  List<String> history;
  public int current; // index of current history pointed to
  boolean pointing;

  public InputHistory() {
    history = new ArrayList<>();
    current = -1;
    pointing = false;
  }

  public int size() {
    return history.size();
  }

  public void write(String string) {
    history.add(string);
    current = history.size() - 1;
    pointing = false;
  }

  public String getPrevious() {
    String prev = "";
    if (pointing) {
      if (current > 0) {
        current--;
        prev = history.get(current);
      } else {
        pointing = false;
      }
    } else {
      if (current != 0) {
        prev = history.get(current);
        pointing = true;
      }
    }
    return prev;
  }

  public String getNext() {
    String next = "";
    if (pointing) {
      if (current < size() - 1) {
        current++;
        next = history.get(current);
      } else {
        pointing = false;
      }
    } else {
      next = history.get(current);
      pointing = true;
    }
    return next;
  }
}
