package goku.ui;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class HistoryController {
  private InputHistory history;
  private TextField inputField;

  public HistoryController(TextField inputField) {
    this.history = new InputHistory();
    this.inputField = inputField;
  }

  public void handle(KeyEvent event) {
    System.out.println(history.counter);
    if (event.getCode() == KeyCode.UP) {
      inputField.setText(history.getPrevious());
      inputField.end();
      System.out.println(history.counter);
    } else if (event.getCode() == KeyCode.DOWN) {
      inputField.setText(history.getNext());
      inputField.end();
    }
  }

  public void addInput(String input) {
    history.write(input);
  }

}
