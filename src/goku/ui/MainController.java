//@author A0099903R
package goku.ui;

import goku.DateRange;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.action.Action;
import goku.action.SearchAction;
import goku.storage.LoadTasksException;
import goku.storage.Storage;
import goku.storage.StorageFactory;
import goku.util.DateUtil;
import goku.util.InvalidDateRangeException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * MainController is the main coordinator of all user actions that happen on the
 * GUI. It knows of other controllers and their duties. It also knows the
 * mapping between Events and Controllers that handle them. Most of what
 * GokuController is to look at an Event, and delegate it to the appropriate
 * Controller to handle. For example, when the user presses the up arrow key,
 * this event is sent to the HistoryController, which responsibility is to
 * remember what the user has entered and to allow the user to scroll through
 * the history.
 */
public class MainController {
  private static final Logger LOGGER = Logger
      .getLogger(Logger.GLOBAL_LOGGER_NAME);
  private static final String ERR_PARSE_FILE_FAIL = "Error parsing JSON, loaded tasks to the best of my ability.";
  private static final String ERR_LOAD_FILE_FAIL = "Error loading file, no tasks loaded.";
  private static final String MSG_NEW_USER = "Seems like you're new!\nA file called \"store.goku\" has been created to save your tasks!\nType \"help\" to get a quick guide.";
  private static final String MSG_FILE_NOT_FOUND = "File cannot be found, no tasks loaded.";

  /* used for windows dragging */
  private static double mousePressX;
  private static double mousePressY;

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

  private GOKU goku;
  private Storage storage;
  private FeedbackPane feedbackPane;
  private List<Controller> controllers;

  @FXML
  void initialize() {
    goku = FXGUI.getGokuInstance();
    controllers = new ArrayList<>();
    feedbackPane = new FeedbackPane(scrollPane);
    storage = StorageFactory.getDefaultStorage();
    setupControllers();
    loadSavedFiled();
  }

  private void loadSavedFiled() {
    try {
      goku.addToTaskList(storage.loadStorage());
      greetUser(new GreetAction(goku));
    } catch (FileNotFoundException e) {
      LOGGER.warning(MSG_FILE_NOT_FOUND);
      feedbackPane.displayLine(MSG_NEW_USER);
    } catch (IOException e) {
      LOGGER.warning(ERR_LOAD_FILE_FAIL);
      feedbackPane.displayErrorMessage(ERR_LOAD_FILE_FAIL);
    } catch (LoadTasksException e) {
      LOGGER.warning(ERR_PARSE_FILE_FAIL);
      feedbackPane.displayErrorMessage(e.getMessage());
      goku.addToTaskList(e.getLoadedTasks());
    }
  }

  void greetUser(GreetAction greetAction) {
    Result result = greetAction.doIt();
    feedbackPane.displayResult(result);
  }

  private void notifyControllers(KeyEvent key) {
    for (Controller c : controllers) {
      if (c.isHandling(key)) {
        c.handle(key);
      }
    }
  }

  private void setupControllers() {
    CompletionController cc = new CompletionController(inputField,
        suggestionBox, suggestionList);
    // this listeners for tasks added to the list and extracts the title
    // so that they can be autocompleted next time
    goku.getObservable().addListener(cc.getCompletionListener());
    controllers.add(cc);
    controllers.add(new HistoryController(inputField));
    controllers.add(new GokuController(goku, inputField, feedbackPane));
    controllers.add(new LiveSearch(goku, inputField, feedbackPane));
    controllers.add(new ScrollController(scrollPane));
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
    notifyControllers(event);
  }

  /*
   * Called when the user clicks on the title bar, store the position of the
   * mouse to allow for moving the entire window.
   */
  public void onTitleBarMousePress(MouseEvent event) {
    mousePressX = event.getSceneX();
    mousePressY = event.getSceneY();
  }

  /*
   * Called when user drags on the title bar, then moves according to where the
   * user drags
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

  /**
   * Greets the user by showing upcoming tasks and events that are due or
   * happening now.
   * 
   * @author ZhiAn
   * 
   */
  private class GreetAction extends Action {
    private static final String MSG = "Welcome to GOKU!\nHere are your tasks for today!";
    private static final String NO_TASK_MSG = "Welcome to GOKU!\nYou have no tasks today, be free! :D";

    public GreetAction(GOKU goku) {
      super(goku);
    }

    @Override
    public Result doIt() {
      SearchAction sa = new SearchAction(goku);
      try {
        sa.period = new DateRange(DateUtil.getNow(), DateUtil.getNow()
            .getEndOfDay());
        Result result = sa.doIt();
        List<Task> tasks = result.getTasks();
        if (tasks == null || tasks.size() == 0) {
          return new Result(true, NO_TASK_MSG, null, null);
        }

        return new Result(true, MSG, null, tasks);
      } catch (InvalidDateRangeException e) {
        return new Result(false, MSG, null, null);
      }
    }
  }
}
