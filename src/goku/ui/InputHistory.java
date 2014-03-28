package goku.ui;

import java.util.ArrayList;
import java.util.List;

public class InputHistory {
  List<String> history;
  public int counter; // index of current history pointed to

  public InputHistory() {
    history = new ArrayList<>();
    // history.add("");
    // counter = 0;
    counter = -1;
  }

  public int size() {
    return history.size();
  }

  public void write(String string) {
    history.add(string);
    counter = history.size() - 1;
  }

  public String getLatest() {
    counter = history.size() - 1;
    return history.get(counter);
  }

  public String getPrevious() {
    if (counter < 0) {
      return "";
      // return history.get(0);
    } else {
      return history.get(counter--);
    }
  }

  public String getNext() {
    if (counter == history.size() - 1) {
      return "";
    } else {
      counter++;
      return history.get(counter);
    }
  }
}
