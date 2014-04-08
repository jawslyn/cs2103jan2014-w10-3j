package goku.ui;

import goku.GOKU;
import goku.Result;
import goku.action.Action;
import goku.action.MakeActionException;
import goku.action.SearchAction;
import goku.util.InvalidDateRangeException;

public class LiveSearch {
  private GOKU goku;
  private FeedbackPane feedback;
  public StringBuilder query;

  public LiveSearch(GOKU goku, FeedbackPane feedbackPane) {
    this.goku = goku;
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
}
