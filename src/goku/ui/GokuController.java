package goku.ui;

import goku.DateRange;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.action.Action;
import goku.action.ExitAction;
import goku.action.MakeActionException;
import goku.action.SearchAction;
import goku.storage.LoadTasksException;
import goku.storage.Storage;
import goku.storage.StorageFactory;
import goku.util.DateUtil;
import goku.util.InvalidDateRangeException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
 * GokuController is the main coordinator of all user actions that happen
 * on the GUI. It knows of other controllers and their duties.
 * It also knows the mapping between Events and Controllers that handle them.
 * Most of what GokuController is to look at an Event, and delegate it to
 * the appropriate Controller to handle.
 * For example, when the user presses the up arrow key, this event is sent to the
 * HistoryController, which responsibility is to remember what the user has entered
 * and to allow the user to scroll through the history.
 * GokuController's most important responsibility is to handle the execution of user
 * commands. This is done again by listening on a press of the Enter key.
 * The following sequence of events then happen:
 *   1. User presses Enter key
 *   2. Calls on InputParser to parse the user's input into an Action
 *   3a. Close application if the user wants to exit
 *   3b. Executes the command by calling doAction(Action).
 *   4. Feedback to the user the Result of the Action, via the FeedbackController
 */
public class GokuController {

  private static final String ERR_PARSE_FILE_FAIL = "Error parsing JSON, loaded tasks to the best of my ability.";
  private static final String ERR_LOAD_FILE_FAIL = "Error loading file, no tasks loaded.";
  private static final String MSG_NEW_USER = "Seems like you're new!\nA file called \"store.goku\" has been created to save your tasks!\nType \"help\" to get a quick guide.";
  private static final String MSG_FILE_NOT_FOUND = "File cannot be found, no tasks loaded.";
  /* JavaFX scene objects */
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
  private TextField inputField;
  @FXML
  private StackPane suggestionBox;
  @FXML
  private VBox suggestionList;

  /* used for windows dragging */
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

  @FXML
  void initialize() {
    goku = FXGUI.getGokuInstance();
    completionController = new CompletionController(inputField, suggestionBox,
        suggestionList);
    feedbackController = new FeedbackController(scrollPane);
    historyController = new HistoryController(inputField);
    parser = new InputParser(goku);
    storage = StorageFactory.getDefaultStorage();

    // this listeners for tasks added to the list and extracts the title
    // so that they can be autocompleted next time
    goku.getObservable().addListener(
        completionController.getCompletionListener());

    loadSavedFiled();
  }

  private void loadSavedFiled() {
    try {
      goku.addToTaskList(storage.loadStorage());
      doAction(new GreetAction(goku));
    } catch (FileNotFoundException e) {
      LOGGER.warning(MSG_FILE_NOT_FOUND);
      feedbackController.displayLine(MSG_NEW_USER);
    } catch (IOException e) {
      LOGGER.warning(ERR_LOAD_FILE_FAIL);
      feedbackController.displayErrorMessage(ERR_LOAD_FILE_FAIL);
    } catch (LoadTasksException e) {
      LOGGER.warning(ERR_PARSE_FILE_FAIL);
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
  public void keyPressOnInputField(KeyEvent event)
      throws InvalidDateRangeException {
    if (event.isControlDown()) {
      handleControlKeypress(event);
    } else if (event.getCode() == KeyCode.ENTER) {
      commitInput();
    } else if (isHistoryKey(event)) {
      historyController.handle(event);
    } else {
      completionController.handle(event);
    }
  }

  /*
   * Called when user presses a Ctrl + ? combination,
   * where ? is any key.
   */
  private void handleControlKeypress(KeyEvent event)
      throws InvalidDateRangeException {
    if (event.getCode() == KeyCode.Z) {
      inputField.setText("undo");
      commitInput();
    } else if (event.getCode() == KeyCode.Y) {
      inputField.setText("redo");
      commitInput();
    }
  }

  private boolean isHistoryKey(KeyEvent event) {
    return event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN;
  }

  /*
   * Called when the user clicks on the title bar,
   * store the position of the mouse to allow for moving the entire window.
   */
  public void onTitleBarMousePress(MouseEvent event) {
    mousePressX = event.getSceneX();
    mousePressY = event.getSceneY();
  }

  /*
   * Called when user drags on the title bar,
   * then moves according to where the user drags
   */
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

  private void commitInput() throws InvalidDateRangeException {
    if (inputField.getText().isEmpty()) {
      return;
    }
    try {
      historyController.addInput(inputField.getText());
      Action action = parser.parse(getUserInput());
      if (action instanceof ExitAction) {
        feedbackController.sayGoodbye();
        Platform.exit();
      }
      Result result = doAction(action);
      feedBack(result);
    } catch (MakeActionException e) {
      feedbackController.displayErrorMessage(e.getMessage());
    }
    clearInput();
    hideSuggestions();
  }

  /*
   * Retrieves user's input from the TextField
   */
  private String getUserInput() {
    return inputField.getText().toLowerCase().trim();
  }

  /*
   * Clears the TextField where user can enter input
   */
  private void clearInput() {
    inputField.clear();
  }

  private Result doAction(Action action) {
    Result result = action.doIt();
    if (action.shouldSave()) {
      save();
    }
    return result;
  }

  /*
   * Informs the user of a result of doing an action
   */
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

  public void save() {
    try {
      storage.saveAll(goku.getTaskList());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error saving tasks.");
    }
  }

  private class GreetAction extends Action {
    private static final String MSG = "Welcome to GOKU!\nHere are your tasks for today!";

    public GreetAction(GOKU goku) {
      super(goku);
    }

    @Override
    public Result doIt() {
      SearchAction sa = new SearchAction(goku);
      try {
        sa.period = new DateRange(DateUtil.getNow(), DateUtil.getNow()
            .getEndOfDay());
      } catch (InvalidDateRangeException e) {
      }
      Result result2 = sa.doIt();
      List<Task> list2 = result2.getTasks();

      return new Result(true, MSG, null, list2);
    }
  }
}
