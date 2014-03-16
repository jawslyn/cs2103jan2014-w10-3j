package goku.ui;

import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.action.Action;
import goku.action.DeleteAction;
import goku.action.DisplayAction;
import goku.action.ExitAction;
import goku.action.SearchAction;
import goku.autocomplete.WordAutocomplete;
import goku.storage.Storage;
import goku.storage.StorageFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class GokuController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private AnchorPane page;

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

  private static enum Mode {
    INSERT, COMPLETION
  };

  private List<String> words;
  private Mode mode = Mode.INSERT;

  @FXML
  void initialize() {
    assert inputField != null : "fx:id=\"inputField\" was not injected: check your FXML file 'Main.fxml'.";
    assert listView != null : "fx:id=\"listView\" was not injected: check your FXML file 'Main.fxml'.";
    assert feedbackField != null : "fx:id=\"feedbackField\" was not injected: check your FXML file 'Main.fxml'.";

    autoComplete = new WordAutocomplete();
    words = new ArrayList<String>(5);
    words.add("spark");
    words.add("special");
    words.add("spectacles");
    words.add("spectacular");
    words.add("swing");

    goku = FXGUI.getGokuInstance();
    if (goku == null) {
      System.out.println("NULL");
    }

    parser = new InputParser(goku);
    tasks = goku.getObservable();
    listView.setItems(tasks);
    storage = StorageFactory.getDefaultStorage();

    inputField.addEventFilter(KeyEvent.KEY_RELEASED,
        new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
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
              System.out.println("prefix: " + prefix);
              List<String> completions = autoComplete.complete(prefix);
              if (completions.size() == 0) {
                mode = Mode.INSERT;
              } else {
                System.out.println("suggesting");
                String match = completions.get(0);
                System.out.println(match);
                String completion = match.substring(pos - 1 - w);
                // System.out.println(completion);
                inputField.insertText(pos, completion);
                inputField.selectRange(w + 1 + match.length(),
                    w + 1 + prefix.length());
                mode = Mode.COMPLETION;
              }
            }

          }
        });

    inputField.addEventFilter(KeyEvent.KEY_PRESSED,
        new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
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
                  System.out.println(e.getMessage());
                }
                if (action instanceof ExitAction) {
                  return;
                }
                inputField.setText("");
                return;
              }
            }
          }
        });

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
            System.out.println("TASKCHANGE");
            System.out.println(tasks.size());
          }
        });
    listView.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
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
        }

      }
    });
  }

  static class TaskCell extends ListCell<Task> {
    @Override
    protected void updateItem(Task item, boolean empty) {
      // TODO Auto-generated method stub
      super.updateItem(item, empty);
      if (item != null) {
        this.setFocusTraversable(false);
        // setText(String.valueOf(item.getId()));
        HBox hbox = new HBox();
        hbox.getStyleClass().add("task-cell");
        Label id = new Label(String.valueOf(item.getId()));
        Label title = new Label(item.getTitle());
        title.focusedProperty().addListener(new ChangeListener<Boolean>() {
          @Override
          public void changed(ObservableValue<? extends Boolean> observable,
              Boolean oldValue, Boolean newValue) {
            System.out.println("CHANGE IN FOCUS FOR TITLE");
          }
        });

        hbox.getChildren().addAll(id, title);
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
