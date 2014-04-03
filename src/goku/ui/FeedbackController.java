package goku.ui;

import goku.DateRange;
import goku.Result;
import goku.Task;
import goku.util.DateOutput;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
  public static double width = 800 - 90;
  private static int lines = 0;
  private static final int numColumns = 12;

  private ScrollPane scrollPane;

  private GridPane output;

  public FeedbackController(ScrollPane scrollPane) {
    this.scrollPane = scrollPane;
    this.output = makeNewPage();
    scrollPane.setContent(output);
  }

  public GridPane makeNewPage() {
    lines = 0;
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(40));
    grid.setPrefWidth(scrollPane.getPrefWidth() - 20);
    grid.setMinWidth(scrollPane.getPrefWidth() - 20);
    grid.setMaxWidth(scrollPane.getPrefWidth() - 20);
    double percentWidth = scrollPane.getPrefWidth() / 12;
    double hgap = 15;
    ColumnConstraints column1 = new ColumnConstraints();
    column1.setPercentWidth(percentWidth);
    ColumnConstraints column2 = new ColumnConstraints();
    column2.setPercentWidth(percentWidth);
    ColumnConstraints column3 = new ColumnConstraints();
    column3.setPercentWidth(percentWidth);
    ColumnConstraints column4 = new ColumnConstraints();
    column4.setPercentWidth(percentWidth);
    ColumnConstraints column5 = new ColumnConstraints();
    column5.setPercentWidth(percentWidth);
    ColumnConstraints column6 = new ColumnConstraints();
    column6.setPercentWidth(percentWidth);
    ColumnConstraints column7 = new ColumnConstraints();
    column7.setPercentWidth(percentWidth);
    ColumnConstraints column8 = new ColumnConstraints();
    column8.setPercentWidth(percentWidth);
    ColumnConstraints column9 = new ColumnConstraints();
    column9.setPercentWidth(percentWidth);
    ColumnConstraints column10 = new ColumnConstraints();
    column10.setPercentWidth(percentWidth);
    ColumnConstraints column11 = new ColumnConstraints();
    column11.setPercentWidth(percentWidth);
    ColumnConstraints column12 = new ColumnConstraints();
    column12.setPercentWidth(percentWidth);
    grid.getColumnConstraints().addAll(column1, column2, column3, column4,
        column5, column6, column7, column8, column9, column10, column11,
        column12);
    grid.setGridLinesVisible(true);
    grid.setHgap(hgap);
    grid.setVgap(10);
    return grid;
  }

  public void clearArea() {
    this.output = makeNewPage();
    scrollPane.setContent(output);
    // lines = 0; output.getChildren().clear();
  }

  /*
   * Displays a simple error message to the user
   * 
   * @param message error message to be displayed
   */
  public void displayErrorMessage(String message) {
    clearArea();
    Label errorLabel = new Label("Error! ");
    errorLabel.setTextFill(ERROR_COLOUR);
    Label errorMessage = new Label(message);
    errorMessage.setTextFill(ERROR_COLOUR);
    displayLine(errorLabel);
    displayLine(errorMessage);
  }

  private void displayLine(Label label) {
    output.add(label, 0, lines++, numColumns, 1);
  }

  /*
   * Prints a string to output
   * 
   * @param message the string to be printed
   */
  public void displayLine(String message) {
    Label l = new Label(message);
    l.getStyleClass().add("normal-text");
    output.add(l, 0, lines++, numColumns, 1);
  }

  /*
   * Displays the Result of an Action. Shows the success/error message, then
   * displays any tasks that are in the result.
   */
  public void displayResult(Result result) {
    clearArea();

    HBox hbox = new HBox();
    hbox.setSpacing(10);
    Label status, message;
    String text = "";
    // hboxOverdue.getChildren().add(overdueMsg);
    displayLine("You have overdue tasks, \"view overdue\" to see them.");

    if (result.isSuccess()) {
      text += "Yay! ";
      text += result.getSuccessMsg();
      // status = new Label("Yay!");
      // status = makeSuccessText("Yay!");
      // message = makeResultMsg(result.getSuccessMsg());
    } else {
      text += "Error! ";
      text += result.getErrorMsg();
      // status = new Label("Error!");
      // status = makeErrorText("Error!");
      // message = makeResultMsg(result.getErrorMsg());
    }
    // hbox.getChildren().addAll(status, message);
    displayLine(text);
    // displayLine("");
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
    // for (Task t : tasks) {
    // displayLine(t.getTitle());
    // }
    TaskListDisplayer tld = new TaskListDisplayer(System.out);
    Hashtable<String, List<Task>> ht = tld.build(tasks);
    String[] headers = { "overdue", "today", "tomorrow", "remaining",
        "completed" };
    for (String header : Arrays.asList(headers)) {
      if (ht.get(header).size() != 0) {
        displayTaskListHeader(header);
        for (Task task : ht.get(header)) {
          if (header.equals("remaining") || header.equals("overdue")) {
            displayTaskOnLine(task);
            // displayLine(makeDisplayBoxForRemainingTask(task));
          } else {
            displayTaskOnLine(task);
            // displayLine(makeDisplayBoxForTask(task));
          }
        }
      }
    }
  }

  private void displayTaskListHeader(String header) {
    HBox hbox = new HBox();
    Label label = new Label(header);
    label.setTextAlignment(TextAlignment.CENTER);
    label.setTextFill(NORMAL_COLOUR);
    hbox.getChildren().add(label);
    hbox.setAlignment(Pos.BOTTOM_CENTER);
    hbox.setStyle("-fx-background-color:blue");
    output.add(hbox, 0, lines++, numColumns, 1);
  }

  private void displayTaskOnLine(Task task) {
    Label id = new Label("[" + task.getId().toString() + "]");
    id.setTextFill(NORMAL_COLOUR);
    Label title = new Label(task.getTitle());
    title.setTextFill(NORMAL_COLOUR);
    title.setWrapText(true);
    title.setMaxHeight(50);
    Label date = new Label(makeDate(task).getText());
    date.setTextFill(NORMAL_COLOUR);
    date.setWrapText(true);
    date.setMaxHeight(50);
    output.add(id, 0, lines, 2, 1);
    output.add(title, 2, lines, 7, 1);
    output.add(date, 9, lines, 3, 1);
    lines++;
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
    Text range = makeNormalText(""
        + DateOutput.formatDateTimeDayMonthHourMin(period.getStartDate())
        // + "\nto "
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
    id.setWrappingWidth(0.08 * width);
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
    impt.setWrappingWidth(0.05 * width);
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

  private Text makeResultMsg(String msg) {
    Text text = new Text(msg);
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
    title.setWrappingWidth(width - 300);
    hbox.getChildren().add(title);
    HBox.setHgrow(title, Priority.NEVER);
    return hbox;
  }

  public void sayGoodbye() {
    clearArea();
    displayLine("Goodbye!");
  }
}
