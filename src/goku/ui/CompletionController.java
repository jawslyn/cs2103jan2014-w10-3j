//@author A0099903R
package goku.ui;

import goku.Task;
import goku.autocomplete.AutoCompleteEngine;

import java.util.List;

import javafx.collections.ListChangeListener;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import com.google.common.base.Splitter;

/**
 * Handles the completion of user input
 */
public class CompletionController extends Controller {
  /* Engine to handle auto completion */
  private AutoCompleteEngine auto;
  /* TextField where the user enters input */
  private TextField inputField;
  /* Pane where the list of suggestion is displayed */
  private StackPane suggestionBox;
  /* VBox to insert suggestions into */
  private VBox suggestionList;
  private List<String> completions;
  private int selectedSuggestion = -1;
  private static final Paint SELECTED_SUGGESTION_FILL = Color.web("#8711BD");
  private static final Paint UNSELECTED_SUGGESTION_FILL = Color.web("#323232");

  public CompletionController(TextField inputField, StackPane suggestionBox,
      VBox suggestionList) {
    auto = new AutoCompleteEngine();
    this.inputField = inputField;
    this.suggestionBox = suggestionBox;
    this.suggestionList = suggestionList;
  }

  /**
   * Handle various KeyEvents. 1. Tab - will insert the best suggestion, if
   * there are 2. Space - will cancel the suggestion 3. Any other character that
   * can be displayed, plus backspace - will show possible completions
   */
  @Override
  public void handle(KeyEvent event) {
    KeyCode code = event.getCode();
    if (isCompletionCommitKey(code)) {
      cycleSuggestion(event.isShiftDown());
    } else if (isCancelCompletionKey(code)) {
      cancelSuggestion();
    } else if (shouldGetCompletion(code)) {
      completions = retrieveCompletions();
      showCompletions();
    }
  }

  private void cycleSuggestion(boolean reverse) {
    int numSuggestions = suggestionList.getChildrenUnmodifiable().size();
    // cycle through the list of suggestions
    if (numSuggestions == 0) {
      // no suggestions
      return;
    } else if (numSuggestions == 1) {
      // only 1 suggestion ,just fill it in
      Text sel = (Text) suggestionList.getChildren().get(0);
      fillSuggestion(sel.getText() + " ");
      cancelSuggestion();
    } else {
      // many suggestions, cycle through them
      if (selectedSuggestion < 0) {
        // no suggestion was selected, select the first suggestion
        selectedSuggestion = 0;
      } else {
        // cycle to the next available suggestion
        Text prev = (Text) suggestionList.getChildren().get(selectedSuggestion);
        int toAdd = reverse ? numSuggestions - 1 : 1;
        selectedSuggestion = (selectedSuggestion + toAdd)
            % suggestionList.getChildrenUnmodifiable().size();
        unhighlightSuggestion(prev);
      }
      Text sel = (Text) suggestionList.getChildren().get(selectedSuggestion);
      highglightSuggestion(sel);
      fillSuggestion(sel.getText());
    }
  }

  private void highglightSuggestion(Text sel) {
    sel.setFill(SELECTED_SUGGESTION_FILL);
    sel.setUnderline(true);
  }

  private void unhighlightSuggestion(Text sel) {
    sel.setFill(UNSELECTED_SUGGESTION_FILL);
    sel.setUnderline(false);
  }

  private void fillSuggestion(String suggestion) {
    inputField.setText(suggestion);
    inputField.end();
  }

  /**
   * Get all possible completions based on the current context. There are 2
   * different kinds of context, 1. Entering a command, 2. Others Commands must
   * be the first word that is entered, so the way to decide what is context is
   * to check if the current word is the first word.
   */
  private List<String> retrieveCompletions() {
    int context = 1;
    if (getPositionOfNearestWhitespaceBeforeCaret() < 0) {
      context = AutoCompleteEngine.commandContext;
    }
    return auto.complete(getPrefixToBeCompleted(), context);
  }

  private boolean isCompletionCommitKey(KeyCode code) {
    return code == KeyCode.TAB;
  }

  private boolean isCancelCompletionKey(KeyCode code) {
    return code.isWhitespaceKey() || code == KeyCode.ENTER;
  }

  private boolean shouldGetCompletion(KeyCode code) {
    return code.isDigitKey() || code.isLetterKey()
        || code == KeyCode.BACK_SPACE;
  }

  /**
   * Shows a list of completion to the user
   */
  private void showCompletions() {
    if (completions.size() == 0) {
      cancelSuggestion();
    } else {
      clearSuggestions();
      for (String completion : completions) {
        // completions are only word completions for the current prefix,
        // so we need to calculate and only add those letters that were not
        // typed
        String completionSuffix = completion.substring(getPrefixToBeCompleted()
            .length());
        addSuggestionToBeShown(inputField.getText() + completionSuffix);
      }
      showSuggestions();
    }
  }

  private String getPrefixToBeCompleted() {
    int start = getPositionOfNearestWhitespaceBeforeCaret() + 1;
    String content = inputField.getText();
    return content.substring(start, inputField.getCaretPosition())
        .toLowerCase();
  }

  /**
   * Gets the index of the nearest whitespace just before the current caret
   * position.
   * 
   * @returns w -1 if there is no whitespace before, else the index of the
   *          whitespace in the content string
   */
  private int getPositionOfNearestWhitespaceBeforeCaret() {
    int caretPos = inputField.getCaretPosition();
    String content = inputField.getText();
    int w;
    for (w = caretPos - 1; w >= 0; w--) {
      if (!Character.isLetter(content.charAt(w))) {
        break;
      }
    }
    return w;
  }

  /**
   * Shows the list of suggestions
   */
  private void showSuggestions() {
    suggestionBox.setVisible(true);
  }

  /**
   * Clears all suggestions from the list of suggestions
   */
  private void clearSuggestions() {
    suggestionList.getChildren().clear();
    selectedSuggestion = -1;
  }

  /**
   * Hides the list of suggestion
   */
  private void hideSuggestions() {
    suggestionBox.setVisible(false);
  }

  private void cancelSuggestion() {
    clearSuggestions();
    hideSuggestions();
  }

  /**
   * Adds a suggestion to the list of suggestions
   */
  private void addSuggestionToBeShown(String suggestion) {
    suggestionList.getChildren().add(new Text(suggestion));
  }

  /**
   * Listens to the list of tasks in GOKU for additions of Tasks. It then adds
   * the title to the corpus for auto completion.
   */
  public ListChangeListener<? super Task> getCompletionListener() {
    return new ListChangeListener<Task>() {
      @Override
      public void onChanged(ListChangeListener.Change<? extends Task> change) {
        while (change.next()) {
          if (change.wasAdded()) {
            addTasksToCompletion(change.getAddedSubList());
          }
        }
      }

      private void addTasksToCompletion(List<? extends Task> tasks) {
        for (Task task : tasks) {
          addTaskTitleToCompletion(task.getTitle());
        }
      }

      private void addTaskTitleToCompletion(String title) {
        List<String> titleList = Splitter.on(' ').omitEmptyStrings()
            .trimResults().splitToList(title);
        for (String tokens : titleList) {
          auto.addCompletion(tokens);
        }
      }
    };
  }

  @Override
  boolean isHandling(KeyEvent key) {
    KeyCode code = key.getCode();
    return isCompletionCommitKey(code) || isCancelCompletionKey(code)
        || shouldGetCompletion(code);
  }
}
