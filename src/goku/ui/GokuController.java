package goku.ui;

import goku.DateRange;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.action.Action;
import goku.action.DisplayAction;
import goku.action.ExitAction;
import goku.action.MakeActionException;
import goku.action.SearchAction;
import goku.autocomplete.WordAutocomplete;
import goku.storage.Storage;
import goku.storage.StorageFactory;
import goku.util.DateOutput;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
  private VBox outputField;

  private GOKU goku;

  private InputParser parser;

  private Storage storage;

  private WordAutocomplete autoComplete;

  private static enum Mode {
    INSERT, COMPLETION
  };

  private Mode mode = Mode.INSERT;

  private static final Logger LOGGER = Logger
      .getLogger(Logger.GLOBAL_LOGGER_NAME);

  @FXML
  void initialize() {
    assert inputField != null : "fx:id=\"inputField\" was not injected: check your FXML file 'Main.fxml'.";
    assert outputField != null : "fx:id=\"outputField\" was not injected: check your FXML file 'Main.fxml'.";
    assert scrollPane != null : "fx:id=\"scrollPane\" was not injected: check your FXML file 'Main.fxml'.";

    goku = FXGUI.getGokuInstance();

    autoComplete = new WordAutocomplete();

    parser = new InputParser(goku);
    storage = StorageFactory.getDefaultStorage();
    try {
      goku.setTaskList(storage.loadStorage());
    } catch (FileNotFoundException e) {
      LOGGER.warning("File cannot be found, no tasks loaded.");
    } catch (IOException e) {
      LOGGER.warning("Error loading file, no tasks loaded.");
    }

    scrollPane.widthProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> width,
          Number oldValue, Number newValue) {
        outputField.setMaxWidth(newValue.doubleValue() - 10);
        outputField.setMinWidth(newValue.doubleValue() - 10);
        outputField.setPrefWidth(newValue.doubleValue() - 10);
      }
    });
  }

  public Text makeId(Task task) {
    Text id = new Text("[" + String.valueOf(task.getId()) + "]");
    id.getStyleClass().addAll("task-id");
    return id;
  }

  public Text makeTitle(Task task) {
    Text title = new Text(task.getTitle());
    title.getStyleClass().addAll("task-title");
    return title;
  }

  public Text makeImpt(Task task) {
    Text impt = new Text(task.getImpt() ? "(!)" : "   ");
    impt.setFill(Color.RED);
    return impt;
  }

  public HBox makeDisplayBoxForTask(Task t) {
    HBox hbox = new HBox();
    hbox.getStyleClass().add("task");
    hbox.setSpacing(5);
    Text id = makeId(t);
    Text impt = makeImpt(t);
    Text title = makeTitle(t);
    HBox separator = new HBox();
    HBox.setHgrow(separator, Priority.ALWAYS);
    separator.getStyleClass().add("separator");
    HBox date = makeDate(t);
    hbox.getChildren().addAll(id, impt, title, separator, date);
    return hbox;
  }

  private HBox makeDate(Task t) {
    HBox hbox = new HBox();
    // hbox.setAlignment(Pos.TOP_RIGHT);
    // HBox.setHgrow(hbox, Priority.ALWAYS);
    Text date = new Text();
    // date.setTextAlignment(TextAlignment.RIGHT);
    if (t.getDeadline() != null) {
      date = makeDeadline(t);
    } else if (t.getDateRange() != null) {
      date = makeDateRange(t);
    }
    hbox.getChildren().add(date);
    return hbox;
  }

  private Text makeDateRange(Task t) {
    Text range = new Text();
    DateRange period = t.getDateRange();
    range.getStyleClass().addAll("task-date-range");
    range.setText("from " + DateOutput.format(period.getStartDate()) + "\nto "
        + DateOutput.format(period.getEndDate()));
    return range;
  }

  private Text makeDeadline(Task t) {
    Text deadline = new Text();
    deadline.getStyleClass().addAll("task-deadline");
    deadline.setText("by " + DateOutput.format(t.getDeadline()));
    return deadline;
  }

  public void addNewLine(HBox hbox) {
    outputField.getChildren().add(hbox);
  }

  public void addNewLine(String output) {
    HBox hbox = new HBox();
    hbox.getChildren().add(new Text(output));
    outputField.getChildren().add(hbox);
    // outputField.getChildren().add(new Text(output));
  }

  public void addNewLineCentered(String output) {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.BASELINE_CENTER);
    hbox.getChildren().add(new Text(output));
    outputField.getChildren().add(hbox);
  }

  public void keyPressOnInputField(KeyEvent event) {
    if (event.getCode().isDigitKey() || event.getCode().isLetterKey()) {
      int pos = inputField.getCaretPosition();
      String content = inputField.getText();
      int w;
      for (w = pos - 1; w >= 0; w--) {
        if (!Character.isLetter(content.charAt(w))) {
          break;
        }
      }
      String prefix = content.substring(w + 1, pos).toLowerCase();
      List<String> completions = autoComplete.complete(prefix);
      if (completions.size() == 0) {
        mode = Mode.INSERT;
      } else {
        String match = completions.get(0);
        String completion = match.substring(pos - 1 - w);
        inputField.insertText(pos, completion);
        inputField.selectRange(w + 1 + match.length(), w + 1 + prefix.length());
        mode = Mode.COMPLETION;
      }
    } else if (event.getCode() == KeyCode.ENTER) {
      if (mode == Mode.COMPLETION) {
        int pos = inputField.getLength();
        inputField.insertText(pos, " ");
        mode = Mode.INSERT;
        inputField.end();
        return;
      } else {
        Action action = null;
        String input = null;
        try {
          input = inputField.getText();
          action = parser.parse(input);
          if (action instanceof ExitAction) {
          }
          doAction(action);
        } catch (MakeActionException e) {
          addNewLine(e.getMessage());
        }
        if (action instanceof ExitAction) {
          addNewLine("Goodbye!");
          Platform.exit();
          return;
        }
        inputField.setText("");
        return;
      }
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
    scrollToBottom();
  }

  private void scrollToBottom() {
    AnimationTimer timer = new AnimationTimer() {
      long lng = 0;

      @Override
      public void handle(long l) {
        if (lng == 0) {
          lng = l;
        }
        if (l > lng + 100000000) {
          scrollPane.setVvalue(scrollPane.getVvalue() + 0.05);
          if (scrollPane.getVvalue() == 1) {
            this.stop();
          }
        }
      }
    };
    timer.start();
  }

  private void feedBack(Result result) {
    if (result == null) {
      return;
    }
    if (result.isSuccess()) {
      if (result.getSuccessMsg() != null) {
        addNewLine(result.getSuccessMsg());
      }
      displayTasks(result.getTasks());
    } else {
      if (result.getErrorMsg() != null) {
        addNewLine(makeErrorMessage(result));
        // addNewLine(result.getErrorMsg());
      }
      if (result.getTasks() != null) {
        displayTasks(result.getTasks());
      }
    }
  }

  public HBox makeErrorMessage(Result result) {
    HBox hbox = new HBox();
    // hbox.setAlignment(Pos.BASELINE_CENTER);
    Text t = new Text("Error!");
    t.setStrokeWidth(10);
    t.setStroke(Color.RED);
    hbox.getChildren().add(t);
    return hbox;
  }

  public void displayTasks(List<Task> tasks) {
    if (tasks == null) {
      return;
    }
    TaskListDisplayer tld = new TaskListDisplayer(System.out);
    Hashtable<String, List<Task>> ht = tld.build(tasks);
    for (Map.Entry<String, List<Task>> entry : ht.entrySet()) {
      System.out.println(entry.getKey());
      addNewLine(makeDateHeader(entry.getKey()));
      for (Task task : ht.get(entry.getKey())) {
        addNewLine(makeDisplayBoxForTask(task));
      }
    }
  }

  private HBox makeDateHeader(String header) {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.BASELINE_CENTER);
    Text t = new Text(header.toUpperCase());
    t.setUnderline(true);
    hbox.getChildren().add(t);
    return hbox;
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
