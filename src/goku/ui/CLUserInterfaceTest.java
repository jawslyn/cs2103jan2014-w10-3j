package goku.ui;
//package goku.clui;
//
//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertEquals;
//import goku.Command;
//import goku.GOKU;
//import goku.Task;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//public class CLUserInterfaceTest {
//
//  /** GLOBAL TEST VARIABLES AND OBJECTS **/
//  GOKU goku;
//  CLUserInterface ui;
//  Parser parser;
//  String[] tags;
//  ByteArrayOutputStream outContent;
//  String NEWLINE = System.lineSeparator();
//
//  @Before
//  public void initObjects() {
//    goku = new GOKU();
//    ui = new CLUserInterface(goku);
//    parser = ui.getParser();
//    parser.restOfInput = new String();
//    tags = new String[10];
//  }
//
//  @Before
//  public void setOutputStreams() {
//    outContent = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outContent));
//  }
//
//  @After
//  public void closeStreams() throws IOException {
//    outContent.close();
//  }
//
//  /*------------------------------------------/
//  /********** splitCmdAndParam Tests **********/
//  /*-----------------------------------------*/
//
//  @Test
//  public void splitFirstWord_WithOneWord() {
//    String cmd = new String();
//
//    cmd = parser.splitCmdAndParam("add");
//    assertEquals("add", cmd);
//    assertEquals("", parser.restOfInput);
//  }
//
//  @Test
//  public void splitFirstWord_WithOneWordAndSpace() {
//    String cmd = new String();
//
//    cmd = parser.splitCmdAndParam("add ");
//    assertEquals("add", cmd);
//    assertEquals("", parser.restOfInput);
//  }
//
//  @Test
//  public void splitFirstWord_WithNormalString() {
//    String cmd = new String();
//
//    cmd = parser.splitCmdAndParam("add tasks");
//    assertEquals("add", cmd);
//    assertEquals("tasks", parser.restOfInput);
//  }
//
//  /*----------------------------------------------*/
//  /********** determineCommandType Tests **********/
//  /*----------------------------------------------*/
//
//  @Test
//  public void determineAddCommand() {
//    assertEquals(Command.Type.ADD, parser.determineCommandType("add"));
//  }
//
//  @Test
//  public void determineAddCommand_a_shortcut() {
//    assertEquals(Command.Type.ADD, parser.determineCommandType("a"));
//  }
//
//  @Test
//  public void determineDisplayCommand() {
//    assertEquals(Command.Type.DISPLAY, parser.determineCommandType("view"));
//  }
//
//  @Test
//  public void determineEditCommand() {
//    assertEquals(Command.Type.EDIT, parser.determineCommandType("update"));
//  }
//
//  @Test
//  public void determineDeleteCommand() {
//    assertEquals(Command.Type.DELETE, parser.determineCommandType("delete"));
//  }
//
//  @Test
//  public void determineSearchCommand() {
//    assertEquals(Command.Type.SEARCH, parser.determineCommandType("search"));
//  }
//
//  @Test
//  public void determineCommand_InvalidCommand() throws IOException {
//    assertEquals(null, parser.determineCommandType("gibberish"));
//    assertEquals("", outContent.toString());
//  }
//
//  /*--------------------------------------------*/
//  /********** determineSortOrder Tests **********/
//  /*--------------------------------------------*/
//
//  @Test
//  public void determineSortOrder_EDF() {
//    parser.restOfInput = "sort:EDF";
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST,
//        parser.determineSortOrder());
//  }
//
//  @Test
//  public void determineSortOrder_HPF() {
//    parser.restOfInput = "sort:HPF";
//    assertEquals(Command.SortOrder.HIGHEST_PRIORITY_FIRST,
//        parser.determineSortOrder());
//  }
//
//  @Test
//  public void determineSortOrder_WithStringLeftEnd() {
//    parser.restOfInput = "task is... sort:EDF";
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST,
//        parser.determineSortOrder());
//  }
//
//  @Test
//  public void determineSortOrder_WithStringRightEnd() {
//    parser.restOfInput = "sort:EDF testtest";
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST,
//        parser.determineSortOrder());
//  }
//
//  @Test
//  public void determineSortOrder_WithStringBothEnds() {
//    parser.restOfInput = "task is... sort:EDF testtest";
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST,
//        parser.determineSortOrder());
//  }
//
//  @Test
//  public void determineSortOrder_WithStringLeftEndNoSpace() {
//    parser.restOfInput = "task is...sort:EDF";
//    assertEquals(null, parser.determineSortOrder());
//  }
//
//  @Test
//  public void determineSortOrder_WithStringRightEndNoSpace() {
//    parser.restOfInput = "sort:EDFtesttest";
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST,
//        parser.determineSortOrder());
//  }
//
//  @Test
//  public void determineSortOrder_WithStringBothEndsNoSpace() {
//    parser.restOfInput = "task is...sort:EDFtesttest";
//    assertEquals(null, parser.determineSortOrder());
//  }
//
//  @Test
//  public void determineSortOrder_InvalidWithCorrectLength() {
//    parser.restOfInput = "task is... sort:HMM testtest";
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST,
//        parser.determineSortOrder());
//  }
//
//  @Test
//  public void determineSortOrder_EmptySortInput() {
//    parser.restOfInput = "sort:";
//    parser.determineSortOrder();
//    // assertEquals(Parser.SORT_ERROR + NEWLINE, outContent.toString());
//    assertEquals("", outContent.toString());
//  }
//
//  @Test
//  public void determineSortOrder_InvalidWithIncorrectLength() {
//    parser.restOfInput = "task is... sort:OOPS testtest";
//    parser.determineSortOrder();
//    // assertEquals(Parser.SORT_ERROR + NEWLINE, outContent.toString());
//    assertEquals("", outContent.toString());
//  }
//
//  /*------------------------------------------*/
//  /********** reconstructInput Tests **********/
//  /*------------------------------------------*/
//
//  @Test
//  public void reconstructInput_EmptyTokens() {
//    String[] tokens = { "", "", "" };
//    parser.reconstructInput(tokens);
//    assertEquals("", parser.restOfInput);
//  }
//
//  @Test
//  public void reconstructInput_Only1Token() {
//    String[] tokens = { "", "test", "" };
//    parser.reconstructInput(tokens);
//    assertEquals("test", parser.restOfInput);
//  }
//
//  public void reconstructInput_MultipleTokens() {
//    String[] tokens = { "this", "is", "the", "test" };
//    parser.reconstructInput(tokens);
//    assertEquals("this is the test", parser.restOfInput);
//
//  }
//
//  /*-------------------------------------*/
//  /********** extractTags Tests **********/
//  /*-------------------------------------*/
//
//  @Test
//  public void extractTags_NoTags() {
//    tags = new String[10];
//    parser.restOfInput = "add task";
//    assertArrayEquals(tags, parser.extractTags());
//    assertEquals("add task", parser.restOfInput);
//  }
//
//  @Test
//  public void extractTags_OneTag() {
//    tags = new String[10];
//    parser.restOfInput = "add task #homework";
//    tags[0] = "homework";
//    assertArrayEquals(tags, parser.extractTags());
//    assertEquals("add task", parser.restOfInput);
//  }
//
//  @Test
//  public void extractTags_TwoTags() {
//    tags = new String[10];
//    parser.restOfInput = "add task #homework #urgent";
//    tags[0] = "homework";
//    tags[1] = "urgent";
//    assertArrayEquals(tags, parser.extractTags());
//    assertEquals("add task", parser.restOfInput);
//  }
//
//  @Test
//  public void extractTags_MultipleTags() {
//    tags = new String[10];
//    parser.restOfInput = "add task #homework #urgent #busy";
//    tags[0] = "homework";
//    tags[1] = "urgent";
//    tags[2] = "busy";
//    assertArrayEquals(tags, parser.extractTags());
//    assertEquals("add task", parser.restOfInput);
//  }
//
//  @Test
//  public void extractTags_NoTagDetectedDueToNoSpacing() {
//    tags = new String[10];
//    parser.restOfInput = "add task#homework";
//    assertArrayEquals(tags, parser.extractTags());
//    assertEquals("add task#homework", parser.restOfInput);
//  }
//
//  @Test
//  public void extractTags_MixtureOfGoodAndBadTagsDueToSpacing() {
//    tags = new String[10];
//    parser.restOfInput = "add task#homework #urgent";
//    tags[0] = "urgent";
//    assertArrayEquals(tags, parser.extractTags());
//    assertEquals("add task#homework", parser.restOfInput);
//  }
//
//  @Test
//  public void extractTags_ExceedLimitOfTenTags() {
//    tags = new String[10];
//    parser.restOfInput = "add task #homework #homework #homework "
//        + "#homework #homework #homework #homework #homework "
//        + "#homework #homework #homework";
//    for (int i = 0; i < 10; i++) {
//      tags[i] = "homework";
//    }
//    assertArrayEquals(tags, parser.extractTags());
//    assertEquals("add task #homework", parser.restOfInput);
//
//  }
//
//  /*------------------------------------------*/
//  /********** findDeadlineDate Tests **********/
//  /*------------------------------------------*/
//
//  @Test
//  public void findDeadlineDate_NoDueFound() {
//    String[] tokens = { "test", "test", "test" };
//    assertEquals(null, parser.findDeadlineDate(tokens));
//  }
//
//  @Test
//  public void findDeadlineDate_ValidString() {
//    String[] tokens = { "test", "due:14/02/2014", "test" };
//    assertEquals("14/02/2014", parser.findDeadlineDate(tokens));
//  }
//
//  @Test
//  public void findDeadlineDate_EmptyDateString() {
//    String[] tokens = { "test", "due:", "test" };
//    assertEquals(null, parser.findDeadlineDate(tokens));
//  }
//
//  @Test
//  public void findDeadlineDate_InvalidDueToNoSpacing() {
//    String[] tokens = { "testdue:14/2/2014", "test" };
//    assertEquals(null, parser.findDeadlineDate(tokens));
//  }
//
//  /*-----------------------------------------*/
//  /********** extractDeadline Tests **********/
//  /*-----------------------------------------*/
//
//  @Test
//  public void extractDeadline_NoDueFound() {
//    parser.restOfInput = "add task";
//    assertEquals(null, parser.extractDeadline());
//  }
//
//  @Test
//  public void extractDeadline_InvalidDateFound() {
//    parser.restOfInput = "due: test";
//    parser.extractDeadline();
//    // assertEquals(CLUserInterface.DATE_ERROR + NEWLINE,
//    // outContent.toString());
//    assertEquals("", outContent.toString());
//  }
//
//  @Test
//  public void extractDeadline_InvalidDateDueToNoSpace() throws ParseException {
//    parser.restOfInput = "due: 14/2/2014";
//    assertEquals(null, parser.extractDeadline());
//  }
//
//  @Test
//  public void extractDeadline_ValidDateFound() throws ParseException {
//    SimpleDateFormat dFormat = new SimpleDateFormat("dd/M/yyyy");
//    Date testDate = dFormat.parse("14/2/2014");
//
//    parser.restOfInput = "due:14/2/2014";
//    assertEquals(testDate, parser.extractDeadline());
//  }
//
//  /*-------------------------------------*/
//  /********** makeCommand Tests **********/
//  /*-------------------------------------*/
//
//  @Test
//  public void makeCommand_InvalidInput() {
//    ui.makeCommand("addtasksort:EDF");
//    assertEquals("", outContent.toString());
//  }
//
//  @Test
//  public void makeCommand_OnlyCommandWord() {
//    Command test = ui.makeCommand("add");
//    assertEquals("add", test.getSource());
//    assertEquals(Command.Type.ADD, test.getType());
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST, test.getSortOrder());
//  }
//
//  @Test
//  public void makeCommand_CommandWord__Description() {
//    Command test = ui.makeCommand("add task");
//    Task testTask = test.getTask();
//
//    assertEquals("add task", test.getSource());
//    assertEquals(Command.Type.ADD, test.getType());
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST, test.getSortOrder());
//    assertEquals("task", testTask.getTitle());
//    assertEquals(false, testTask.getStatus());
//  }
//
//  @Test
//  public void makeCommand_CommandWord__Description__EDF() {
//    Command test = ui.makeCommand("add task sort:EDF");
//    Task testTask = test.getTask();
//
//    assertEquals("add task sort:EDF", test.getSource());
//    assertEquals(Command.Type.ADD, test.getType());
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST, test.getSortOrder());
//    assertEquals("task", testTask.getTitle());
//    assertEquals(false, testTask.getStatus());
//  }
//
//  @Test
//  public void makeCommand_CommandWord__Description__HPF() {
//    Command test = ui.makeCommand("delete task sort:HPF");
//    Task testTask = test.getTask();
//
//    assertEquals("delete task sort:HPF", test.getSource());
//    assertEquals(Command.Type.DELETE, test.getType());
//    assertEquals(Command.SortOrder.HIGHEST_PRIORITY_FIRST, test.getSortOrder());
//    assertEquals("task", testTask.getTitle());
//    assertEquals(false, testTask.getStatus());
//  }
//
//  @Test
//  public void makeCommand_CommandWord__Description__InvalidSortDueToNoSpacing() {
//    Command test = ui.makeCommand("add tasksort:EDF");
//    Task testTask = test.getTask();
//
//    assertEquals("add tasksort:EDF", test.getSource());
//    assertEquals(Command.Type.ADD, test.getType());
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST, test.getSortOrder());
//    assertEquals("tasksort:EDF", testTask.getTitle());
//    assertEquals(false, testTask.getStatus());
//  }
//
//  @Test
//  public void makeCommand_CommandWord__Description__NonexistentSortOrder() {
//    ui.makeCommand("add task sort:ABC");
//    // assertEquals(Parser.SORT_ERROR + NEWLINE, outContent.toString());
//    assertEquals("", outContent.toString());
//  }
//
//  @Test
//  public void makeCommand_CommandWord__Description__EDF__OneTag() {
//    Command test = ui.makeCommand("add task sort:EDF #homework");
//    Task testTask = test.getTask();
//    tags[0] = "homework";
//
//    assertEquals("add task sort:EDF #homework", test.getSource());
//    assertEquals(Command.Type.ADD, test.getType());
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST, test.getSortOrder());
//    assertEquals("task", testTask.getTitle());
//    assertArrayEquals(tags, testTask.getTags());
//    assertEquals(false, testTask.getStatus());
//  }
//
//  @Test
//  public void makeCommand_CommandWord__Description__EDF__TwoTags() {
//    Command test = ui.makeCommand("add task sort:EDF #homework #urgent");
//    Task testTask = test.getTask();
//    tags[0] = "homework";
//    tags[1] = "urgent";
//
//    assertEquals("add task sort:EDF #homework #urgent", test.getSource());
//    assertEquals(Command.Type.ADD, test.getType());
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST, test.getSortOrder());
//    assertEquals("task", testTask.getTitle());
//    assertArrayEquals(tags, testTask.getTags());
//    assertEquals(false, testTask.getStatus());
//  }
//
//  @Test
//  public void makeCommand_CommandWord__Description__EDF__TwoTags__ValidDeadline()
//      throws ParseException {
//    Command test = ui
//        .makeCommand("add task sort:EDF #homework #urgent due:14/2/2014");
//    Task testTask = test.getTask();
//    tags[0] = "homework";
//    tags[1] = "urgent";
//    SimpleDateFormat dFormat = new SimpleDateFormat("dd/M/yyyy");
//    Date testDate = dFormat.parse("14/2/2014");
//
//    assertEquals("add task sort:EDF #homework #urgent due:14/2/2014",
//        test.getSource());
//    assertEquals(Command.Type.ADD, test.getType());
//    assertEquals(Command.SortOrder.EARLIEST_DEADLINE_FIRST, test.getSortOrder());
//    assertEquals("task", testTask.getTitle());
//    assertArrayEquals(tags, testTask.getTags());
//    assertEquals(false, testTask.getStatus());
//    assertEquals(testDate, testTask.getDeadline());
//  }
//
// }
