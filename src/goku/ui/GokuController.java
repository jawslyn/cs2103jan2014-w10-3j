package goku.ui;

import goku.GOKU;
import goku.Result;
import goku.action.Action;
import goku.action.DisplayAction;
import goku.action.ExitAction;
import goku.action.MakeActionException;
import goku.action.SearchAction;
import goku.storage.Storage;
import goku.storage.StorageFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GokuController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private AnchorPane page;

  @FXML
  private ScrollPane scrollPane;

  @FXML
  private TextField inputField;

  @FXML
  private StackPane suggestionBox;

  @FXML
  private VBox suggestionList;

  @FXML
  private VBox outputField;

  private GOKU goku;

  private InputParser parser;

  private Storage storage;

  private static final Logger LOGGER = Logger
      .getLogger(Logger.GLOBAL_LOGGER_NAME);

  private CompletionController completionController;
  private FeedbackController feedbackController;

  @FXML
  void initialize() {
    assert page != null : "fx:id=\"page\" was not injected: check your FXML file 'Main.fxml'.";
    assert scrollPane != null : "fx:id=\"scrollPane\" was not injected: check your FXML file 'Main.fxml'.";
    assert inputField != null : "fx:id=\"inputField\" was not injected: check your FXML file 'Main.fxml'.";
    assert suggestionBox != null : "fx:id=\"suggestionBox\" was not injected: check your FXML file 'Main.fxml'.";
    assert suggestionList != null : "fx:id=\"suggestionList\" was not injected: check your FXML file 'Main.fxml'.";
    assert outputField != null : "fx:id=\"outputField\" was not injected: check your FXML file 'Main.fxml'.";

    goku = FXGUI.getGokuInstance();
    completionController = new CompletionController(inputField, suggestionBox,
        suggestionList);
    feedbackController = new FeedbackController(outputField);
    parser = new InputParser(goku);
    storage = StorageFactory.getDefaultStorage();

    try {
      goku.setTaskList(storage.loadStorage());
    } catch (FileNotFoundException e) {
      LOGGER.warning("File cannot be found, no tasks loaded.");
    } catch (IOException e) {
      LOGGER.warning("Error loading file, no tasks loaded.");
    }
  }

  /*
   * This method is called when a keypress in inputField is detected.
   * A "Enter" keypress means the user wants to run the command.
   * Any other key will be handled by CompletionController,
   * which will suggestion completions.
   * User can then use tab to *select* a completion and have it 
   * fill up the inputField.
   */
  public void keyPressOnInputField(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      Action action = null;
      String input = null;
      try {
        input = inputField.getText().toLowerCase().trim();
        action = parser.parse(input);
        if (action instanceof ExitAction) {
          feedbackController.sayGoodbye();
          Platform.exit();
        }
        doAction(action);
      } catch (MakeActionException e) {
        feedbackController.displayErrorMessage(e.getMessage());
      }
      inputField.setText("");
      hideSuggestions();
      return;
    } else if (event.isControlDown()) {
      if (event.getCode() == KeyCode.Z) {
        inputField.setText("undo");
        try {
          doAction(parser.parse("undo"));
        } catch (MakeActionException e) {
          feedbackController.displayErrorMessage(e.getMessage());
        }
        inputField.clear();
      } else if (event.getCode() == KeyCode.Y) {
        inputField.setText("redo");
        try {
          doAction(parser.parse("redo"));
        } catch (MakeActionException e) {
          feedbackController.displayErrorMessage(e.getMessage());
        }
        inputField.clear();
      }
    } else {
      completionController.handle(event);
    }
  }

  private void doAction(Action action) throws MakeActionException {
    if (action instanceof DisplayAction) {
      Result result = action.doIt();
      feedBack(result);
    } else if (action instanceof SearchAction) {
      Result result = action.doIt();
      feedBack(result);
    } else {
      Result result = action.doIt();
      feedBack(result);
      save();
    }
  }

  private void feedBack(Result result) {
    if (result == null) {
      return;
    } else {
      feedbackController.displayResult(result);
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

  private void hideSuggestions() {
    suggestionBox.setVisible(false);
  }
}
