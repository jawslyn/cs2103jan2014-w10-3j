package goku.ui;

import goku.autocomplete.CommandAutoComplete;
import goku.autocomplete.WordAutocomplete;

import java.util.List;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CompletionController {
  private WordAutocomplete autoComplete;
  private CommandAutoComplete commandAutoComplete;
  private TextField inputField;
  private StackPane suggestionBox;
  private VBox suggestionList;

  public CompletionController(TextField inputField, StackPane suggestionBox,
      VBox suggestionList) {
    autoComplete = new WordAutocomplete();
    commandAutoComplete = new CommandAutoComplete();
    this.inputField = inputField;
    this.suggestionBox = suggestionBox;
    this.suggestionList = suggestionList;
  }

  /*
   * Handle various KeyEvents.
   * 1. Tab - will insert the best suggestion, if there are
   * 2. Space - will cancel the suggestion
   * 3. Any other character that can be displayed, plus backspace -
   *    will show possible completions
   */
  public void handle(KeyEvent event) {
    KeyCode code = event.getCode();
    if (isCompletionCommitKey(code)) {
      insertSuggestedText(getBestSuggestion());
    } else if (isCancelCompletionKey(code)) {
      cancelSuggestion();
    } else if (shouldGetCompletion(event)) {
      List<String> completions = retrieveCompletions();
      showCompletions(completions);
    }
  }

  private List<String> retrieveCompletions() {
    // TODO add context aware stuff here
    // something like check the location of caret, if i'm completing
    // the first run, run the command completion engine
    // else run the title engine
    int posStartOfWord = getCaretPositionOfNearestWhitespaceBefore();
    if (posStartOfWord < 0) {
      System.out.println("command complete");
      return commandAutoComplete.complete(getPrefixToBeCompleted());
    }
    return autoComplete.complete(getPrefixToBeCompleted());
  }

  private boolean isCompletionCommitKey(KeyCode code) {
    return code == KeyCode.TAB;
  }

  private boolean isCancelCompletionKey(KeyCode code) {
    return code.isWhitespaceKey();
  }

  private boolean shouldGetCompletion(KeyEvent event) {
    return event.getCode().isDigitKey() || event.getCode().isLetterKey()
        || event.getCode() == KeyCode.BACK_SPACE;
  }

  private void insertSuggestedText(Text suggestedText) {
    if (suggestedText == null) {
      return;
    }
    inputField.setText(suggestedText.getText() + " ");
    inputField.end();
    hideSuggestions();
  }

  private Text getBestSuggestion() {
    if (suggestionList.getChildren().size() == 0) {
      return null;
    }
    Text suggestedText = (Text) suggestionList.getChildren().get(0);
    return suggestedText;
  }

  private void showCompletions(List<String> completions) {
    clearSuggestions();
    if (completions.size() == 0) {
      cancelSuggestion();
    } else {
      for (String completion : completions) {
        String completionSuffix = completion.substring(getPrefixToBeCompleted()
            .length());
        addSuggestionToBeShown(inputField.getText() + completionSuffix);
      }
      showSuggestions();
    }
  }

  private String getPrefixToBeCompleted() {
    int w = getCaretPositionOfNearestWhitespaceBefore();
    int caretPos = inputField.getCaretPosition();
    String content = inputField.getText();
    return content.substring(w + 1, caretPos).toLowerCase();
  }

  /*
   * Gets the index of the nearest whitespace just before the current caret position.
   * @returns w -1 if there is no whitespace before, else the index of the whitespace
   * in the content string
   */
  private int getCaretPositionOfNearestWhitespaceBefore() {
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

  /*
   * Shows the list of suggestions
   */
  private void showSuggestions() {
    suggestionBox.setVisible(true);
  }

  /*
   * Clears all suggestions from the list of suggestions
   */
  private void clearSuggestions() {
    suggestionList.getChildren().clear();
  }

  /*
   * Hides the list of suggestion
   */
  private void hideSuggestions() {
    suggestionBox.setVisible(false);
  }

  private void cancelSuggestion() {
    clearSuggestions();
    hideSuggestions();
  }

  /*
   * Adds a suggestion to the list of suggestions
   */
  private void addSuggestionToBeShown(String suggestion) {
    suggestionList.getChildren().add(new Text(suggestion));
  }

}
