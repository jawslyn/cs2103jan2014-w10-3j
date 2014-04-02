package goku.ui;

import goku.DateRange;
import goku.Result;
import goku.Task;
import goku.util.DateOutput;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/*
 * FeedbackController takes care of outputting feedback to user.
 * The area where feedback is shown to user is a JavaFX VBox.
 * The VBox acts like a console, printing lines of input.
 * Each line is a child of the VBox.
 */
public class FeedbackController {
  private static final Paint ERROR_COLOUR = Color.rgb(255, 10, 0);
  private static final Paint IMPT_COLOUR = Color.RED;
  private static final Paint SUCCESS_COLOUR = Color.rgb(13, 255, 166);
  private static final Paint NORMAL_COLOUR = Color.rgb(86, 255, 0);

  private VBox output;

  public FeedbackController(VBox outputArea) {
    output = outputArea;
  }

  public void clearArea() {
    output.getChildren().clear();
  }

  /*
   * Displays a simple error message to the user
   * 
   * @param message error message to be displayed
   */
  public void displayErrorMessage(String message) {
    clearArea();
    HBox hbox = new HBox();
    Text text = makeErrorText("Error: " + message);
    hbox.getChildren().add(text);
    displayLine(hbox);
  }

  /*
   * Prints a complicated message to output. This HBox needs to be built by the
   * caller
   * 
   * @param message message to be printed
   */
  public void displayLine(HBox message) {
    output.getChildren().add(message);
  }

  /*
   * Prints a string to output
   * 
   * @param message the string to be printed
   */
  public void displayLine(String message) {
    HBox hbox = new HBox();
    hbox.getChildren().add(makeNormalText(message));
    output.getChildren().add(hbox);

  }

  /*
   * Displays the Result of an Action. Shows the success/error message, then
   * displays any tasks that are in the result.
   */
  public void displayResult(Result result) {
    clearArea();

    HBox hbox = new HBox();
    HBox hboxOverdue = new HBox();
    hbox.setSpacing(10);
    Text status, message, overdueMsg = makeNormalText("       You have overdue tasks, \"view overdue\" to see them.");

    hboxOverdue.getChildren().add(overdueMsg);

    if (result.isSuccess()) {
      status = makeSuccessText("Yay!");
      message = makeNormalText(result.getSuccessMsg());
    } else {
      status = makeErrorText("Error!");
      message = makeNormalText(result.getErrorMsg());
    }
    hbox.getChildren().addAll(status, message);
    displayLine(hbox);
    displayLine("");
    displayTasks(result.getTasks());
  }

  /*
   * Displays a list of task. Shows header, and then list the tasks under that
   * header. This is very coupled to TaskListDisplayer, as the header that is
   * displayed depends on it.
   */
  public void displayTasks(List<Task> tasks) {
    if (tasks == null) {
      return;
    }
    TaskListDisplayer tld = new TaskListDisplayer(System.out);
    Hashtable<String, List<Task>> ht = tld.build(tasks);
    String[] headers = { "overdue", "today", "tomorrow", "remaining" };
    for (String header : Arrays.asList(headers)) {
      if (ht.get(header).size() != 0) {
        displayLine(makeDateHeader(header));
        for (Task task : ht.get(header)) {
          if (header.equals("remaining") || header.equals("overdue")) {
            displayLine(makeDisplayBoxForRemainingTask(task));
          } else {
            displayLine(makeDisplayBoxForTask(task));
          }
        }
      }
    }
  }

  /*
   * Builds a Text that represents either the deadline or the period of the
   * task.
   * 
   * @param task t to be shown
   */
  private Text makeDate(Task t) {
    if (t.getDeadline() != null) {
      return makeDeadline(t);
    } else if (t.getDateRange() != null) {
      return makeDateRange(t);
    } else {
      return makeNormalText("");
    }
  }

  private Text makeDateForRemaining(Task t) {
    if (t.getDeadline() != null) {
      return makeDeadlineForRemaining(t);
    } else if (t.getDateRange() != null) {
      return makeDateRange(t);
    } else {
      return makeNormalText("");
    }
  }

  /*
   * Builds a header used when displaying tasks. A header is used to categorize
   * the task by the date, so that users can see the most urgent tasks first.
   */
  private HBox makeDateHeader(String header) {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.BASELINE_CENTER);
    Text t = makeNormalText(header.toUpperCase());
    t.setUnderline(true);
    hbox.getChildren().add(t);
    return hbox;
  }

  /*
   * Builds a Text that represents the period of a task.
   * 
   * @param t task to be shown
   */
  private Text makeDateRange(Task t) {
    DateRange period = t.getDateRange();
    Text range = makeNormalText("from "
        + DateOutput.formatDateTimeDayMonthHourMin(period.getStartDate())
        + "\nto "
        + DateOutput.formatDateTimeDayMonthHourMin(period.getEndDate()));
    range.getStyleClass().addAll("task-date-range");
    return range;
  }

  /*
   * Builds a Text that represents the deadline of a task.
   * 
   * @param t task to be shown
   */
  private Text makeDeadline(Task t) {
    Text deadline = makeNormalText("by "
        + DateOutput.formatTimeOnly12h(t.getDeadline()));
    deadline.getStyleClass().addAll("task-deadline");
    return deadline;
  }

  private Text makeDeadlineForRemaining(Task t) {
    Text deadline = makeNormalText("by "
        + DateOutput.formatDateTimeDayMonthHourMin(t.getDeadline()));
    deadline.getStyleClass().addAll("task-deadline");
    return deadline;
  }

  public HBox makeDisplayBoxForRemainingTask(Task t) {
    HBox hbox = new HBox();
    hbox.getStyleClass().add("task");
    hbox.setSpacing(5);
    hbox.getChildren().addAll(makeId(t), makeImpt(t), makeTitle(t),
        makeSeparator(), makeDateForRemaining(t));
    return hbox;
  }

  /*
   * Builds HBox that displays a single task.
   */
  public HBox makeDisplayBoxForTask(Task t) {
    HBox hbox = new HBox();
    hbox.getStyleClass().add("task");
    hbox.setSpacing(5);
    hbox.getChildren().addAll(makeId(t), makeImpt(t), makeTitle(t),
        makeSeparator(), makeDate(t));
    return hbox;
  }

  public Text makeErrorText(String message) {
    Text text = new Text(message);
    text.setFill(ERROR_COLOUR);
    return text;
  }

  /*
   * Builds a Text that shows the ID of the task. The id will be in square
   * brackets: [1]
   * 
   * @param task task to be shown
   */
  public Text makeId(Task task) {
    Text id = makeNormalText("[" + String.valueOf(task.getId()) + "]");
    id.getStyleClass().addAll("task-id");
    return id;
  }

  /*
   * Builds a Text that indicates if a task is important or not. This is
   * indicated by a (!), which is in bright red
   * 
   * @param task task to be shown
   */
  public Text makeImpt(Task task) {
    Text impt = makeImptText(task.getImpt() ? "(!)" : "   ");
    return impt;
  }

  public Text makeImptText(String message) {
    Text text = new Text(message);
    text.setFill(IMPT_COLOUR);
    return text;
  }

  public Text makeNormalText(String message) {
    Text text = new Text(message);
    text.setFill(NORMAL_COLOUR);
    return text;
  }

  /*
   * Builds a separator that is then inserted between the task title and the due
   * date or period. This helps the user to align the proper task title to the
   * date. HBox is set to grow and fill up the extra space in the parent HBox.
   */
  private HBox makeSeparator() {
    HBox separator = new HBox();
    HBox.setHgrow(separator, Priority.ALWAYS); // IMPT don't change
    separator.getStyleClass().add("separator");
    return separator;
  }

  public Text makeSuccessText(String message) {
    Text text = new Text(message);
    text.setFill(SUCCESS_COLOUR);
    return text;
  }

  /*
   * Builds a Text that shows the title of a task.
   * 
   * @param task task to be shown
   */
  public HBox makeTitle(Task task) {
    HBox hbox = new HBox();
    Text title = makeNormalText(task.getTitle());
    title.setWrappingWidth(output.getWidth() - 300);
    hbox.getChildren().add(title);
    HBox.setHgrow(title, Priority.NEVER);
    return hbox;
  }

  public void sayGoodbye() {
    clearArea();
    displayLine("Goodbye!");
  }
}
