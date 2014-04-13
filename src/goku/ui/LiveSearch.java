//@author A0099903R
package goku.ui;

import goku.Commands;
import goku.GOKU;
import goku.Result;
import goku.action.Action;
import goku.action.MakeActionException;
import goku.action.SearchAction;
import goku.util.InvalidDateRangeException;

import java.util.Arrays;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * Provides live search capability to GOKU's GUI. LiveSearch handles every key
 * press, but only reacts when the user is in a searching context. It identifies
 * the context by looking at the first word in the input field. If that first
 * word is part of a keyword that results in a SearchAction, it will then
 * greedily parse the text in the input field and show the search results to the
 * user.
 * 
 * @author ZhiAn
 * 
 */
public class LiveSearch extends Controller {
  private GOKU goku;
  private TextField input;
  private FeedbackPane feedback;
  public StringBuilder query;

  public LiveSearch(GOKU goku, TextField inputField, FeedbackPane feedbackPane) {
    this.goku = goku;
    this.input = inputField;
    this.feedback = feedbackPane;
  }

  void search(String command) {
    InputParser parser = new InputParser(goku);
    Action a;
    try {
      a = parser.parse(command);
    } catch (MakeActionException e) {
      return;
    } catch (InvalidDateRangeException e) {
      return;
    }
    if (!(a instanceof SearchAction)) {
      return;
    }
    SearchAction sa = (SearchAction) a;
    Result r = sa.doIt();
    feedback.displayResult(r);
  }

  @Override
  boolean isHandling(KeyEvent key) {
    return true;
  }

  @Override
  void handle(KeyEvent key) {
    String[] tokens = input.getText().split(" ");
    if (tokens.length > 0
        && Arrays.asList(Commands.searchKeywords).contains(tokens[0])) {
      search(input.getText());
    }

  }
}
