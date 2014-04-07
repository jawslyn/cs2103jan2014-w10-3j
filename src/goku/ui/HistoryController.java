package goku.ui;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Takes care of the user's input history, allowing the user to recall previous
 * inputs.
 * 
 * @author ZhiAn
 * 
 */
public class HistoryController extends Controller {
  private InputHistory history;
  private TextField inputField;

  public HistoryController(TextField inputField) {
    this.history = new InputHistory();
    this.inputField = inputField;
  }

  @Override
  boolean isHandling(KeyEvent key) {
    return key.getCode() == KeyCode.UP || key.getCode() == KeyCode.DOWN
        || key.getCode() == KeyCode.ENTER;
  }

  @Override
  public void handle(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      addInput(inputField.getText());
    } else if (event.getCode() == KeyCode.UP) {
      inputField.setText(history.getPrevious());
      inputField.end();
    } else if (event.getCode() == KeyCode.DOWN) {
      inputField.setText(history.getNext());
      inputField.end();
    }
  }

  /**
   * Adds a string to the underlying structure that keeps the user's input
   * history
   * 
   * @param input
   *          user's input to be stored in history
   */
  public void addInput(String input) {
    history.write(input);
  }

}
