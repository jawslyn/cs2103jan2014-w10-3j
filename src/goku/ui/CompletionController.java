package goku.ui;

import goku.autocomplete.WordAutocomplete;

import java.util.List;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CompletionController {
  private static enum Mode {
    INSERT, COMPLETION
  };

  private Mode mode = Mode.INSERT;

  private WordAutocomplete autoComplete;
  private TextField inputField;
  private StackPane suggestionBox;
  private VBox suggestionList;

  public CompletionController(TextField inputField, StackPane suggestionBox,
      VBox suggestionList) {
    autoComplete = new WordAutocomplete();
    this.inputField = inputField;
    this.suggestionBox = suggestionBox;
    this.suggestionList = suggestionList;
  }

  public void handle(KeyEvent event) {
    KeyCode code = event.getCode();
    if (code == KeyCode.ENTER || code == KeyCode.TAB) {
      Text suggestedText = (Text) suggestionList.getChildren().get(0);
      inputField.setText(suggestedText.getText());
      mode = Mode.INSERT;
      inputField.insertText(inputField.getLength(), " ");
      inputField.end();
      hideSuggestions();
      return;
    } else if (code.isWhitespaceKey()) {
      mode = Mode.INSERT;
      clearSuggestions();
      hideSuggestions();
    } else if (event.getCode().isDigitKey() || event.getCode().isLetterKey()) {
      clearSuggestions();
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
        clearSuggestions();
        hideSuggestions();
      } else {
        for (String completion : completions) {
          String comp = completion.substring(pos - 1 - w);
          addSuggestionToBeShown(inputField.getText() + comp);
        }
        mode = Mode.COMPLETION;
        showSuggestions();
      }
    }
  }

  private void showSuggestions() {
    suggestionBox.setVisible(true);
  }

  private void addSuggestionToBeShown(String string) {
    suggestionList.getChildren().add(new Text(string));
  }

  private void clearSuggestions() {
    suggestionList.getChildren().clear();
  }

  private void hideSuggestions() {
    suggestionBox.setVisible(false);
  }
}
