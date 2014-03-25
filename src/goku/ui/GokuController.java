package goku.ui;

import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.action.Action;
import goku.action.DeleteAction;
import goku.action.DisplayAction;
import goku.action.EditAction;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.Duration;

public class GokuController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private AnchorPane page;

  @FXML
  private StackPane editPane;

  @FXML
  private TextField editPaneTitle;

  @FXML
  private CheckBox editComplete;

  @FXML
  private TextField inputField;

  @FXML
  private ListView<Task> listView;

  @FXML
  private TextField feedbackField;

  private ObservableList<Task> tasks;
  private GOKU goku;

  private InputParser parser;

  private Storage storage;

  private WordAutocomplete autoComplete;

  private Integer selectedTaskId;

  private static enum Mode {
    INSERT, COMPLETION
  };

  private Mode mode = Mode.INSERT;

  private static final Logger LOGGER = Logger
      .getLogger(Logger.GLOBAL_LOGGER_NAME);

  @FXML
  void initialize() {
    assert inputField != null : "fx:id=\"inputField\" was not injected: check your FXML file 'Main.fxml'.";
    assert listView != null : "fx:id=\"listView\" was not injected: check your FXML file 'Main.fxml'.";
    assert feedbackField != null : "fx:id=\"feedbackField\" was not injected: check your FXML file 'Main.fxml'.";

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
    tasks = goku.getObservable();
    listView.setItems(tasks);

    listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
      @Override
      public ListCell<Task> call(ListView<Task> list) {
        return new TaskCell();
      }
    });

    listView.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable,
          Boolean oldValue, Boolean newValue) {
        if (newValue == false) {
          listView.getSelectionModel().clearSelection();
        }
      }
    });

    listView.setEditable(true);

    listView.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<Task>() {
          @Override
          public void changed(ObservableValue<? extends Task> observable,
              Task oldValue, Task newValue) {
          }
        });

  }

  public void clickOnTask(MouseEvent arg0) {
    Task selectedTask = listView.getSelectionModel().getSelectedItem();
    if (selectedTask != null && selectedTask.getTitle() != null) {
      editPaneTitle.setText(selectedTask.getTitle());
      editPane.setVisible(true);
      showEditPane();
      selectedTaskId = selectedTask.getId();
      editComplete.setSelected(selectedTask.getStatus() == null ? false
          : selectedTask.getStatus());
      editPaneTitle.requestFocus();
    }
  }

  public void keyPressOnEditPane(KeyEvent arg0) {
    if (arg0.getCode() == KeyCode.ENTER) {
      commitEdit();
      hideEditPane();
      inputField.requestFocus();
    }
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
        }
        if (action instanceof ExitAction) {
          return;
        }
        inputField.setText("");
        return;
      }
    }
  }

  public void keyPressOnListView(KeyEvent event) {
    if (event.getCode() == KeyCode.BACK_SPACE
        || event.getCode() == KeyCode.DELETE) {
      Task selected = listView.getSelectionModel().getSelectedItem();
      if (selected == null) {
        return;
      }
      DeleteAction deleteAction = new DeleteAction(goku);
      deleteAction.id = selected.getId();
      try {
        doAction(deleteAction);
      } catch (MakeActionException e) {
      }
    } else if (event.getCode() == KeyCode.ENTER) {
      editPane.setVisible(true);
    }

  }

  void showEditPane() {
    final Timeline timeline = new Timeline();
    final KeyValue kv = new KeyValue(editPane.translateXProperty(), -500,
        Interpolator.EASE_IN);
    final KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
    timeline.getKeyFrames().add(kf);
    timeline.play();
  }

  void hideEditPane() {
    final Timeline timeline = new Timeline();
    final KeyValue kv = new KeyValue(editPane.translateXProperty(), 0,
        Interpolator.EASE_BOTH);
    final KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
    timeline.getKeyFrames().add(kf);
    timeline.play();
  }

  void commitEdit() {
    String title = editPaneTitle.getText();
    Boolean completed = editComplete.selectedProperty().getValue();
    EditAction edit = new EditAction(goku);
    edit.id = selectedTaskId;
    edit.title = title;
    edit.isComplete = completed;
    edit.doIt();
    try {
      doAction(edit);
    } catch (MakeActionException e) {
      e.printStackTrace();
    }
    tasks = goku.getObservable();
    listView.setItems(null);
    listView.setItems(tasks);
  }

  static class TaskCell extends ListCell<Task> {
    @Override
    protected void updateItem(Task item, boolean empty) {
      super.updateItem(item, empty);
      if (item != null) {
        this.setFocusTraversable(false);
        HBox hbox = new HBox();
        hbox.getStyleClass().add("task-cell");
        hbox.setSpacing(20f);

        Label id = new Label(String.valueOf(item.getId()));

        id.getStyleClass().add("task-cell-id");
        Label title = new Label(item.getTitle());

        if (item.isDone() != null && item.isDone()) {
          hbox.getStyleClass().add("completed");
        } else {
        }

        HBox.setHgrow(title, Priority.ALWAYS);

        Label deadline, start, end;
        hbox.getChildren().addAll(id, title);
        HBox dates = new HBox();
        dates.setSpacing(5);
        if (item.getDeadline() != null) {
          deadline = new Label(DateOutput.format(item.getDeadline()));
          dates.getChildren().addAll(new Label("by"), deadline);
        } else if (item.getDateRange() != null) {
          start = new Label(DateOutput.format(item.getStartDate()));
          end = new Label(DateOutput.format(item.getEndDate()));
          dates.getChildren().addAll(new Label("from"), start, new Label("to"),
              end);
        }
        hbox.getChildren().add(dates);
        setGraphic(hbox);
      }
    }
  }

  private void doAction(Action action) throws MakeActionException {
    clearFeedback();
    if (action instanceof DisplayAction) {
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
    }
    if (result.isSuccess()) {
      if (result.getSuccessMsg() != null) {
        feedbackField.setText(result.getSuccessMsg());
      }
      if (result.getTasks() != null) {
      }
    } else {
      if (result.getErrorMsg() != null) {
        feedbackField.setText(result.getErrorMsg());
      }
      if (result.getTasks() != null) {
      }
    }
  }

  private void clearFeedback() {
    if (feedbackField == null) {
      return;
    }
    feedbackField.setText("");
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
