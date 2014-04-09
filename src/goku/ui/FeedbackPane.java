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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

import com.google.common.base.Splitter;

import de.jensd.fx.fontawesome.AwesomeDude;
import de.jensd.fx.fontawesome.AwesomeIcon;

/**
 * FeedbackController takes care of outputting feedback to user. The area where
 * feedback is shown to user is a JavaFX VBox. The VBox acts like a console,
 * printing lines of input. Each line is a child of the VBox.
 * 
 * @author ZhiAn
 * 
 */
public class FeedbackPane {
  private static final Paint ERROR_COLOUR = Color.rgb(255, 10, 0);
  private static final Paint IMPT_COLOUR = Color.rgb(235, 40, 30);
  private static final Paint DONE_COLOUR = Color.LIME;
  private static final Paint SUCCESS_COLOUR = Color.rgb(13, 255, 166);
  private static final Paint NORMAL_COLOUR = Color.web("F9F8F1");
  private static final Paint HEADER_COLOUR = Color.web("EE3474");
  private static final Paint DATE_COLOUR = Color.web("F8981C");
  private static final Paint ID_COLOUR = Color.web("73CEE4");

  private static final int numColumns = 3;
  public static double width = 800 - 20;
  private static int lines = 0;

  private ScrollPane scrollPane;

  private GridPane output;

  public FeedbackPane(ScrollPane scrollPane) {
    this.scrollPane = scrollPane;
    clearArea();
  }

  public void clearArea() {
    this.output = makeNewPage();
    scrollPane.setContent(output);
    lines = 0;
  }

  public GridPane makeNewPage() {
    GridPane grid = new GridPane();
    AnchorPane.setLeftAnchor(grid, 40.0);
    AnchorPane.setRightAnchor(grid, 40.0);
    grid.setPrefWidth(800 - 40);
    grid.setPadding(new Insets(0, 10, 50, 40));
    grid.setVgap(5);

    ColumnConstraints idColumn = new ColumnConstraints();
    idColumn.setPercentWidth(12);
    ColumnConstraints titleColumn = new ColumnConstraints();
    titleColumn.setPercentWidth(100 - 12 - 23);
    ColumnConstraints dateColumn = new ColumnConstraints();
    dateColumn.setPercentWidth(23);

    grid.getColumnConstraints().addAll(idColumn, titleColumn, dateColumn);
    return grid;
  }

  public void displayLines(List<String> messages) {
    for (String message : messages) {
      displayLine(message);
    }
  }

  public void displayLine(String message) {
    if (message.contains("\n")) {
      displayLines(Splitter.on('\n').trimResults().omitEmptyStrings()
          .splitToList(message));
    } else {
      displayLine(message, NORMAL_COLOUR);
    }
  }

  /*
   * Prints a string to output
   * 
   * @param message the string to be printed
   */
  public void displayLine(String message, Paint color) {
    if (message.length() <= 60) {
      Label l = new Label(message);
      l.setTextFill(color);
      output.add(l, 0, lines, numColumns + 1, 1);
      lines++;
    } else {
      displayLine(message.substring(0, 60));
      displayLine(message.substring(61));
    }
  }

  /*
   * Displays a simple error message to the user
   * 
   * @param message error message to be displayed
   */
  public void displayErrorMessage(String message) {
    clearArea();
    displayLine("Error! ", ERROR_COLOUR);
    displayLine(message, NORMAL_COLOUR);
  }

  /*
   * Displays the Result of an Action. Shows the success/error message, then
   * displays any tasks that are in the result.
   */
  public void displayResult(Result result) {
    clearArea();

    if (result.isSuccess()) {
      displayLine("Yay!", SUCCESS_COLOUR);
      displayLine(result.getSuccessMsg());
      if (result.getListMsg() != null) {
        displayLines(result.getListMsg());
      }
    } else {
      displayLine("Error!", ERROR_COLOUR);
      displayLine(result.getErrorMsg());
    }
    displayTasks(result.getTasks());
  }

  /*
   * Displays a list of task. Shows header, and then list the tasks under that
   * header. This is very coupled to TaskListDisplayer, as the header that is
   * displayed depends on it.
   */
  private void displayTasks(List<Task> tasks) {
    if (tasks == null) {
      return;
    }
    TaskListDisplayer tld = new TaskListDisplayer(System.out);
    Hashtable<String, List<Task>> ht = tld.build(tasks);
    String[] headers = { "overdue", "today", "tomorrow", "remaining",
        "completed" };
    for (String header : Arrays.asList(headers)) {
      if (ht.get(header).size() != 0) {
        displayTaskListHeader(header);
        for (Task task : ht.get(header)) {
          if (header.equals("remaining") || header.equals("overdue")) {
            displayRemainingTask(task);
          } else if (header.equals("completed")) {
            displayCompleteTask(task);
          } else {
            displayTask(task);
          }
        }
      }
    }
  }

  private void displayCompleteTask(Task task) {
    Label tick = AwesomeDude.createIconLabel(AwesomeIcon.CHECK);
    Label b1 = new Label("[");
    Label b2 = new Label("]");

    tick.setTextFill(DONE_COLOUR);
    b1.setTextFill(ID_COLOUR);
    b2.setTextFill(ID_COLOUR);
    HBox tickHbox = new HBox();
    tickHbox.getChildren().addAll(b1, tick, b2);
    tickHbox.setAlignment(Pos.CENTER_LEFT);

    Label title = new Label(task.getTitle());
    title.setTextFill(NORMAL_COLOUR);
    title.setWrapText(true);
    VBox dateVBox = makeDateVbox(task, true);
    // dateVBox.setStyle("-fx-background-color:rgba(85, 255, 68,0.2)");

    output.add(tickHbox, 0, lines);
    output.add(title, 1, lines);
    output.add(dateVBox, 2, lines);
    lines++;
  }

  private void displayRemainingTask(Task task) {
    Label id = new Label("[" + task.getId().toString() + "]");
    if (!task.getImpt()) {
      id.setTextFill(ID_COLOUR);
    } else {
      id.setTextFill(IMPT_COLOUR);
    }

    HBox idWithImpt = new HBox();
    Label impt = AwesomeDude.createIconLabel(AwesomeIcon.EXCLAMATION);
    impt.setTextFill(IMPT_COLOUR);
    idWithImpt.getChildren().addAll(id, impt);
    idWithImpt.setAlignment(Pos.CENTER_LEFT);

    Label title = new Label(task.getTitle());
    title.setTextFill(NORMAL_COLOUR);
    title.setWrapText(true);
    title.setMinWidth(50.0);
    VBox dateVBox = makeDateVbox(task, true);

    if (!task.getImpt()) {
      output.add(id, 0, lines);
    } else {
      output.add(idWithImpt, 0, lines);
    }
    output.add(title, 1, lines);
    output.add(dateVBox, 2, lines);
    lines++;
  }

  private void displayTask(Task task) {
    Label id = new Label("[" + task.getId().toString() + "]");
    if (!task.getImpt()) {
      id.setTextFill(ID_COLOUR);
    } else {
      id.setTextFill(IMPT_COLOUR);
    }

    HBox idWithImpt = new HBox();
    Label impt = AwesomeDude.createIconLabel(AwesomeIcon.EXCLAMATION);
    impt.setTextFill(IMPT_COLOUR);
    idWithImpt.getChildren().addAll(id, impt);
    idWithImpt.setAlignment(Pos.CENTER_LEFT);
    // if (task.getImpt()) {
    // id.setTextFill(IMPT_COLOUR);
    // }
    Label title = new Label(task.getTitle());
    title.setTextFill(NORMAL_COLOUR);
    title.setWrapText(true);
    title.setMinWidth(50.0);
    VBox dateVBox = makeDateVbox(task, false);

    // output.add(id, 0, lines);
    if (!task.getImpt()) {
      output.add(id, 0, lines);
    } else {
      output.add(idWithImpt, 0, lines);
    }
    output.add(title, 1, lines);
    output.add(dateVBox, 2, lines);
    lines++;
  }

  private VBox makeDateVbox(Task task, boolean remaining) {
    VBox vbox = new VBox();
    if (task.getDeadline() != null) {
      HBox dl = makeDeadline(task, remaining);
      vbox.getChildren().add(dl);
    } else if (task.getDateRange() != null) {
      HBox[] pr = makeDateRange(task, remaining);
      vbox.getChildren().addAll(pr[0], pr[1]);
    }
    return vbox;
  }

  private void displayTaskListHeader(String header) {
    HBox hbox = new HBox();
    Label label = new Label("-" + header.toUpperCase() + "-");
    label.setTextAlignment(TextAlignment.CENTER);
    label.setTextFill(HEADER_COLOUR);
    hbox.getChildren().add(label);
    hbox.setAlignment(Pos.BOTTOM_CENTER);
    output.add(hbox, 0, lines++, numColumns, 1);
  }

  /*
   * Builds a Text that represents the period of a task.
   * 
   * @param t task to be shown
   */
  private HBox[] makeDateRange(Task t, boolean remaining) {
    HBox[] hbox = new HBox[2];
    hbox[0] = new HBox();
    hbox[1] = new HBox();
    hbox[0].setAlignment(Pos.CENTER_LEFT);
    hbox[1].setAlignment(Pos.CENTER_LEFT);
    hbox[0].setSpacing(5);
    hbox[1].setSpacing(5);

    Label sicon = AwesomeDude.createIconLabel(AwesomeIcon.CALENDAR_ALT);
    Label eicon = AwesomeDude.createIconLabel(AwesomeIcon.LONG_ARROW_RIGHT);
    sicon.setTextFill(DATE_COLOUR);
    eicon.setTextFill(DATE_COLOUR);

    DateRange period = t.getDateRange();
    Label s, e;

    if (remaining) {
      s = new Label(
          DateOutput.formatDateTimeDayMonthHourMinIgnoreZeroMinutes(period
              .getStartDate()));
      e = new Label(
          DateOutput.formatDateTimeDayMonthHourMinIgnoreZeroMinutes(period
              .getEndDate()));
    } else {
      s = new Label(DateOutput.formatTimeOnly12hIgnoreZeroMinutes(period
          .getStartDate()));
      e = new Label(DateOutput.formatTimeOnly12hIgnoreZeroMinutes(period
          .getEndDate()));
    }
    s.setTextFill(DATE_COLOUR);
    e.setTextFill(DATE_COLOUR);
    hbox[0].getChildren().addAll(sicon, s);
    hbox[1].getChildren().addAll(eicon, e);
    return hbox;
  }

  /*
   * Builds a Text that represents the deadline of a task.
   * 
   * @param t task to be shown
   */
  private HBox makeDeadline(Task t, boolean remaining) {
    HBox hbox = new HBox();
    Label deadline;
    if (remaining) {
      deadline = new Label(""
          + DateOutput.formatDateTimeDayMonthHourMinIgnoreZeroMinutes(t
              .getDeadline()));
    } else {
      deadline = new Label(""
          + DateOutput.formatTimeOnly12hIgnoreZeroMinutes(t.getDeadline()));
    }
    deadline.setTextFill(DATE_COLOUR);
    Label icon = AwesomeDude.createIconLabel(AwesomeIcon.CLOCK_ALT);
    icon.setTextFill(DATE_COLOUR);
    hbox.getChildren().addAll(icon, deadline);
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.setSpacing(5);
    return hbox;
  }

  public void sayGoodbye() {
    clearArea();
    displayLine("Goodbye!");
  }
}
