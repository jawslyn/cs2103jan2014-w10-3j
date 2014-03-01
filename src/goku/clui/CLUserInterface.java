package goku.clui;

import goku.Command;
import goku.Command.SortOrder;
import goku.Command.Type;
import goku.Result;
import goku.Task;
import goku.TaskList;

import java.util.Date;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * User interface that acts as the intermediate component between the user
 * interface and the main logic of GOKU.
 * 
 * @author jchiam
 */
public class CLUserInterface implements UserInterface {
  private CLUIParser parser;
  private Scanner sc;
  private String input;

  public CLUserInterface() {
    parser = new CLUIParser();
    sc = new Scanner(System.in);
    input = "";
  }

  public Parser getParser() {
    return this.parser;
  }

  /**
   * @return string that user entered
   */
  @Override
  public String getUserInput() {
    return sc.nextLine();
  }

  /**
   * @param input
   *          string that a user has entered
   * @return a command representing that the user wants to do
   */
  @Override
  public Command makeCommand(String input) {
    return parser.parseToCommand(input);
  }

  @Override
  public void feedBack(Result result) {
    if (result.isSuccess()) {
      System.out.println(result.getSuccessMsg());
      if (result.getTasks() != null) {
        printTaskList(result.getTasks());
      }
    } else {
      System.out.println(result.getErrorMsg());
    }
  }

  public void printTaskList(TaskList list) {
    ListIterator<Task> it = (ListIterator<Task>) list.iterator();
    while (it.hasNext()) {
      Task task = it.next();
      System.out.println(task);
    }
  }

  /**
   * Parser that deals with String input from user to extract necessary
   * information to create Command object
   * 
   * @author jchiam
   */
  // TODO
  protected class CLUIParser extends Parser {

    public CLUIParser() {
      super();
    }

    /** GLOBAL VARIABLES **/
    private Type commandType = null;
    private SortOrder sortOrder = null;
    private String[] taskTags = new String[10]; // holds 10 tags at most
    private Date taskDeadline = null;

    // unprocessed input

    /*
     * Parses
     */
    public Command parseToCommand(String input) {
      Task toDo = parser.parseString(input);
      if (sortOrder == null) {
        return new Command(input, commandType, toDo);
      } else {
        return new Command(input, commandType, toDo, sortOrder);
      }
    }

    /**
     * Method: parseString
     * 
     * @param input
     *          string to be parsed
     * @return created task
     */
    @Override
    public Task parseString(String input) {

      String firstWord = splitCmdAndParam(input);

      commandType = determineCommandType(firstWord);

      sortOrder = determineSortOrder();

      // PULL OUT NECESSARY INFO FOR TASK
      // 3. remainder as title

      taskTags = extractTags();

      taskDeadline = extractDeadline();

      return createTask(false, taskDeadline, taskTags, restOfInput);
    }

  }

}
