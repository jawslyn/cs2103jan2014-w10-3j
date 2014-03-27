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
  private static final Paint ERROR_COLOUR = Color.RED;
  private static final Paint IMPT_COLOUR = Color.RED;
  private static final Paint SUCCESS_COLOUR = Color.GREEN;

  private VBox output;

  public FeedbackController(VBox outputArea) {
    output = outputArea;
  }

  /*
   * Displays the Result of an Action.
   * Shows the success/error message, then displays
   * any tasks that are in the result.
   */
  public void displayResult(Result result) {
    clearArea();

    HBox hbox = new HBox();
    hbox.setSpacing(10);
    Text status, message;

    if (result.isSuccess()) {
      status = new Text("Yay!");
      status.setFill(SUCCESS_COLOUR);
      message = new Text(result.getSuccessMsg());
    } else {
      status = new Text("Error!");
      message = new Text(result.getErrorMsg());
      status.setFill(ERROR_COLOUR);
    }
    hbox.getChildren().addAll(status, message);
    displayLine(hbox);
    displayLine("");
    displayTasks(result.getTasks());
  }

  /*
   * Displays a simple error message to the user
   * @param message error message to be displayed
   */
  public void displayErrorMessage(String message) {
    clearArea();
    HBox hbox = new HBox();
    Text t = new Text("Error: " + message);
    t.setFill(ERROR_COLOUR);
    hbox.getChildren().add(t);
    displayLine(hbox);
  }

  public void clearArea() {
    output.getChildren().clear();
  }

  /*
   * Displays a list of task.
   * Shows header, and then list the tasks under that header.
   * This is very coupled to TaskListDisplayer, as the header
   * that is displayed depends on it.
   */
  public void displayTasks(List<Task> tasks) {
    if (tasks == null) {
      return;
    }
    TaskListDisplayer tld = new TaskListDisplayer(System.out);
    Hashtable<String, List<Task>> ht = tld.build(tasks);
    String[] headers = { "today", "tomorrow", "remaining" };
    for (String header : Arrays.asList(headers)) {
      if (ht.get(header).size() != 0) {
        displayLine(makeDateHeader(header));
        for (Task task : ht.get(header)) {
          displayLine(makeDisplayBoxForTask(task));
        }
      }
    }
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

  /*
   * Builds a Text that shows the ID of the task.
   * The id will be in square brackets: [1]
   * @param task task to be shown
   */
  public Text makeId(Task task) {
    Text id = new Text("[" + String.valueOf(task.getId()) + "]");
    id.getStyleClass().addAll("task-id");
    return id;
  }

  /*
   * Builds a Text that indicates if a task is important or not.
   * This is indicated by a (!), which is in bright red
   * @param task task to be shown
   */
  public Text makeImpt(Task task) {
    Text impt = new Text(task.getImpt() ? "(!)" : "   ");
    impt.setFill(IMPT_COLOUR);
    return impt;
  }

  /*
   * Builds a Text that shows the title of a task.
   * @param task task to be shown
   */
  public Text makeTitle(Task task) {
    Text title = new Text(task.getTitle());
    title.getStyleClass().addAll("task-title");
    return title;
  }

  /*
   * Builds a separator that is then inserted between the task title and the
   * due date or period.
   * This helps the user to align the proper task title to the date.
   * HBox is set to grow and fill up the extra space in the parent HBox.
   */
  private HBox makeSeparator() {
    HBox separator = new HBox();
    HBox.setHgrow(separator, Priority.ALWAYS); // IMPT don't change
    separator.getStyleClass().add("separator");
    return separator;
  }

  /*
   * Builds a Text that represents either the deadline or the period of the task.
   * @param task t to be shown
   */
  private Text makeDate(Task t) {
    if (t.getDeadline() != null) {
      return makeDeadline(t);
    } else if (t.getDateRange() != null) {
      return makeDateRange(t);
    } else {
      return new Text();
    }
  }

  /*
   * Builds a Text that represents the period of a task.
   * @param t task to be shown
   */
  private Text makeDateRange(Task t) {
    Text range = new Text();
    DateRange period = t.getDateRange();
    range.getStyleClass().addAll("task-date-range");
    range.setText("from " + DateOutput.format(period.getStartDate()) + "\nto "
        + DateOutput.format(period.getEndDate()));
    return range;
  }

  /*
   * Builds a Text that represents the deadline of a task.
   * @param t task to be shown
   */
  private Text makeDeadline(Task t) {
    Text deadline = new Text();
    deadline.getStyleClass().addAll("task-deadline");
    deadline.setText("by " + DateOutput.format(t.getDeadline()));
    return deadline;
  }

  /*
   * Builds a header used when displaying tasks.
   * A header is used to categorize the task by the date, so that
   * users can see the most urgent tasks first.
   */
  private HBox makeDateHeader(String header) {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.BASELINE_CENTER);
    Text t = new Text(header.toUpperCase());
    t.setUnderline(true);
    hbox.getChildren().add(t);
    return hbox;
  }

  /*
   * Prints a string to output
   * @param message the string to be printed
   */
  public void displayLine(String message) {
    HBox hbox = new HBox();
    hbox.getChildren().add(new Text(message));
    output.getChildren().add(hbox);

  }

  /*
   * Prints a complicated message to output.
   * This HBox needs to be built by the caller
   * @param message message to be printed
   */
  public void displayLine(HBox message) {
    output.getChildren().add(message);
  }

  public void sayGoodbye() {
    clearArea();
    displayLine("Goodbye!");
  }

}
