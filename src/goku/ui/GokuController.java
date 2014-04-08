package goku.ui;

import goku.GOKU;
import goku.Result;
import goku.action.Action;
import goku.action.ExitAction;
import goku.action.MakeActionException;
import goku.storage.Storage;
import goku.storage.StorageFactory;
import goku.util.InvalidDateRangeException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * GokuController is the middle-man between the GUI and GOKU. It beings with the
 * press of an Enter key. The sequence of events then happen: 1. User presses
 * Enter key 2. Calls on InputParser to parse the user's input into an Action
 * 3a. Close application if the user wants to exit 3b. Executes the command by
 * calling doAction(Action). 4. Feedback to the user the Result of the Action,
 * via the FeedbackController
 */
public class GokuController extends Controller {

  @FXML
  private TextField inputField;

  private GOKU goku;
  private InputParser parser;
  private Storage storage;
  private static final Logger LOGGER = Logger
      .getLogger(Logger.GLOBAL_LOGGER_NAME);
  private FeedbackPane feedback;

  public GokuController(GOKU goku, TextField inputField,
      FeedbackPane feedbackController) {
    this.inputField = inputField;
    this.goku = goku;
    parser = new InputParser(goku);
    storage = StorageFactory.getDefaultStorage();
    this.feedback = feedbackController;
  }

  @Override
  boolean isHandling(KeyEvent key) {
    return key.getCode() == KeyCode.ENTER || isUndoOrRedo(key);
  }

  /*
   * An undo or redo is indicated by the Ctrl+Z or Ctrl+Y
   * key combination, respectively
   */
  boolean isUndoOrRedo(KeyEvent key) {
    return key.isControlDown()
        && (key.getCode() == KeyCode.Z || key.getCode() == KeyCode.Y);
  }

  @Override
  public void handle(KeyEvent key) {
    if (isUndoOrRedo(key)) {
      inputField.setText(key.getCode() == KeyCode.Z ? "undo" : "redo");
    }
    commitInput();
  }

  private void commitInput() {
    if (inputField.getText().isEmpty()) {
      return;
    }
    try {
      Action action = parser.parse(getUserInput());
      if (action instanceof ExitAction) {
        feedback.sayGoodbye();
        Platform.exit();
      }
      Result result = doAction(action);
      feedBack(result);
    } catch (MakeActionException | InvalidDateRangeException e) {
      LOGGER.log(Level.WARNING, "Bad user command.");
      feedback.displayErrorMessage(e.getMessage());
    }
    clearInput();
  }

  /*
   * Retrieves user's input from the TextField
   */
  private String getUserInput() {
    return inputField.getText().trim();
  }

  /*
   * Clears the TextField where user can enter input
   */
  private void clearInput() {
    inputField.clear();
  }

  Result doAction(Action action) {
    Result result = action.doIt();
    if (action.shouldSave()) {
      save();
    }
    return result;
  }

  /*
   * Informs the user of a result of doing an action
   */
  void feedBack(Result result) {
    if (result == null) {
      return;
    } else {
      feedback.displayResult(result);
    }
  }

  public void save() {
    try {
      storage.saveAll(goku.getTaskList());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error saving tasks.");
    }
  }

}
