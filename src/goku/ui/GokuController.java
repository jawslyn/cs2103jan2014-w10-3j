package goku.ui;

import goku.GOKU;
import goku.Result;
import goku.action.Action;
import goku.action.ExitAction;
import goku.action.MakeActionException;
import goku.action.SearchAction;
import goku.storage.LoadTasksException;
import goku.storage.Storage;
import goku.storage.StorageFactory;
import goku.util.DateUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GokuController {

  private class GreetAction extends Action {
    private static final String MSG = "Welcome to GOKU! Here are your tasks for today!";

    public GreetAction(GOKU goku) {
      super(goku);
    }

    @Override
    public Result doIt() {
      SearchAction sa = new SearchAction(goku);
      sa.dline = DateUtil.getNow().plusDays(1);
      Result result = sa.doIt();
      return new Result(true, MSG, null, result.getTasks());
    }

  }

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private AnchorPane page;

  @FXML
  private HBox titleBar;

  @FXML
  private Label minimizeButton;

  @FXML
  private Label closeButton;

  @FXML
  private ScrollPane scrollPane;

  @FXML
  private AnchorPane scrollAnchorPane;

  @FXML
  private TextField inputField;

  @FXML
  private StackPane suggestionBox;

  @FXML
  private VBox suggestionList;

  @FXML
  private GridPane outputField;

  private static double mousePressX;
  private static double mousePressY;

  private GOKU goku;

  private InputParser parser;

  private Storage storage;

  private static final Logger LOGGER = Logger
      .getLogger(Logger.GLOBAL_LOGGER_NAME);
  private CompletionController completionController;
  private FeedbackController feedbackController;

  private HistoryController historyController;

  private void clearInput() {
    inputField.clear();
    ;
  }

  private void commitInput() {
    if (inputField.getText().isEmpty()) {
      return;
    }
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

  private void doAction(Action action) {
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

  private void hideSuggestions() {
    suggestionBox.setVisible(false);
  }

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
    feedbackController = new FeedbackController(scrollPane);
    // feedbackController.setOutputBox(outputBox);
    historyController = new HistoryController(inputField);
    parser = new InputParser(goku);
    storage = StorageFactory.getDefaultStorage();

    // this listeners for tasks added to the list and extracts the title
    // so that they can be autocompleted next time
    goku.getObservable().addListener(
        completionController.getCompletionListener());

    try {
      goku.addToTaskList(storage.loadStorage());
      doAction(new GreetAction(goku));
      // feedbackController.displayLine("HIHI");
      // feedbackController
      // .displayLine("LKAJDFLalskdfjaskljdflkasjdfjasdfjaklsjfdlKj");
    } catch (FileNotFoundException e) {
      LOGGER.warning("File cannot be found, no tasks loaded.");
      feedbackController
          .displayLine("Seems like you're new!\nA file called \"store.goku\" has been created to save your tasks!\nType \"help\" to get a quick guide.");
    } catch (IOException e) {
      LOGGER.warning("Error loading file, no tasks loaded.");
    } catch (LoadTasksException e) {
      LOGGER
          .warning("Error parsing JSON, loaded tasks to the best of my ability.");
      feedbackController.displayErrorMessage(e.getMessage());
      goku.addToTaskList(e.getLoadedTasks());
    }
  }

  /*
   * This method is called when a keypress in inputField is detected. A "Enter"
   * keypress means the user wants to run the command. Any other key will be
   * handled by CompletionController, which will suggestion completions. User
   * can then use tab to *select* a completion and have it fill up the
   * inputField. Ctrl + Z and Ctrl + Y is a shortcut for Undo and Redo,
   * respectively.
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

  public void save() {
    try {
      storage.saveAll(goku.getTaskList());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error saving tasks.");
    }
  }

  public void onTitleBarMousePress(MouseEvent event) {
    mousePressX = event.getSceneX();
    mousePressY = event.getSceneY();
  }

  public void onTitleBarDrag(MouseEvent event) {
    Stage stage = FXGUI.getStage();
    stage.setX(event.getScreenX() - mousePressX);
    stage.setY(event.getScreenY() - mousePressY);
  }

  public void exitButtonPress(MouseEvent event) {
    Platform.exit();
  }

  public void minimizeButtonPress(MouseEvent event) {
    Stage stage = FXGUI.getStage();
    stage.setIconified(true);
  }

}
