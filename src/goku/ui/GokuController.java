package goku.ui;

import goku.GOKU;
import goku.Result;
import goku.action.Action;
import goku.action.ExitAction;
import goku.action.MakeActionException;
import goku.storage.LoadTasksException;
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
  private HistoryController historyController;

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
    historyController = new HistoryController(inputField);
    parser = new InputParser(goku);
    storage = StorageFactory.getDefaultStorage();

    try {
      goku.setTaskList(storage.loadStorage());
    } catch (FileNotFoundException e) {
      LOGGER.warning("File cannot be found, no tasks loaded.");
    } catch (IOException e) {
      LOGGER.warning("Error loading file, no tasks loaded.");
    } catch (LoadTasksException e) {
      feedbackController.displayErrorMessage(e.getMessage());
      System.out.println(e.getLoadedTasks().size());
      goku.setTaskList(e.getLoadedTasks());
    }
  }

  /*
   * This method is called when a keypress in inputField is detected.
   * A "Enter" keypress means the user wants to run the command.
   * Any other key will be handled by CompletionController,
   * which will suggestion completions.
   * User can then use tab to *select* a completion and have it 
   * fill up the inputField.
   * Ctrl + Z and Ctrl + Y is a shortcut for Undo and Redo, respectively.
   */
  public void keyPressOnInputField(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      commitInput();
    } else if (event.isControlDown()) {
      if (event.getCode() == KeyCode.Z) {
        inputField.setText("undo");
        commitInput();
      } else if (event.getCode() == KeyCode.Y) {
        inputField.setText("redo");
        commitInput();
      }
    } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
      historyController.handle(event);
    } else {
      completionController.handle(event);
    }
  }

  private void commitInput() {
    try {
      historyController.addInput(inputField.getText());
      String input = inputField.getText().toLowerCase().trim();
      Action action = parser.parse(input);
      if (action instanceof ExitAction) {
        feedbackController.sayGoodbye();
        Platform.exit();
      }
      doAction(action);
    } catch (MakeActionException e) {
      feedbackController.displayErrorMessage(e.getMessage());
    }
    clearInput();
    hideSuggestions();
  }

  private void doAction(Action action) throws MakeActionException {
    Result result = action.doIt();
    feedBack(result);
    if (action.shouldSave()) {
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

  private void clearInput() {
    inputField.clear();
    ;
  }

  private void hideSuggestions() {
    suggestionBox.setVisible(false);
  }
}
