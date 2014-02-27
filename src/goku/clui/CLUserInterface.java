package goku.clui;

import goku.Command;
import goku.Command.SortOrder;
import goku.Command.Type;
import goku.Result;
import goku.Task;

import java.util.Scanner;

/**
 * User interface that acts as the intermediate component between the user
 * interface and the main logic of GOKU.
 * 
 * @author jchiam
 */
public class CLUserInterface implements UserInterface {
  /*** STRING CONSTANTS ***/
  private static String INPUT_ERROR = "Input cannot be recognised.";

  private CLUIParser parser;
  private Scanner sc;
  private String input;

  public CLUserInterface() {
    parser = new CLUIParser();
    sc = new Scanner(System.in);
    input = "";
  }

  public CLUIParser getParser() {
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
    } else {
      System.out.println(result.getErrorMsg());
    }
  }

  /**
   * Parser that deals with String input from user to extract necessary
   * information to create Command object
   * 
   * @author jchiam
   */
  protected class CLUIParser implements Parser {

    /** GLOBAL VARIABLES **/
    private Task toDo = null;
    private Type commandType = null;
    private SortOrder sortOrder = null;
    String restOfInput = new String(); // string that holds remaining
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
     * @param input
     *          string to be parsed
     * @return created task
     */
    @Override
    public Task parseString(String input) {

      String firstWord = splitCmdAndParam(input);

      // determine commandType
      commandType = determineCommandType(firstWord);

      // FIND STRING FOR SORTORDER
      sortOrder = determineSortOrder();

      return null;
    }

    /**
     * Method: determineSortOrder
     * 
     * @param input
     */
    protected Command.SortOrder determineSortOrder() {

      Command.SortOrder sort = null;

      if (restOfInput.contains("sort:")) {

        // find out what sort it is
        int indexOfSortCmd = 0;
        if (sortFound("sort:EDF")) {
          sort = Command.SortOrder.EARLIEST_DEADLINE_FIRST;
          indexOfSortCmd = restOfInput.indexOf("sort:EDF");

          restOfInput = removeSortInput(indexOfSortCmd);
        } else if (sortFound("sort:HPF")) {
          sort = Command.SortOrder.HIGHEST_PRIORITY_FIRST;
          indexOfSortCmd = restOfInput.indexOf("sort:EDF");

          restOfInput = removeSortInput(indexOfSortCmd);
        } else {
          // TODO throw error message for invalid sort order
        }
      }

      return sort;
    }

    /**
     * Method: sortFound
     * 
     * @param sortString
     *          to search for
     * @return true or false if precise sort string is found
     */
    private boolean sortFound(String sortString) {

      // boolean variables to check for clearance on left and right ends of
      // string
      boolean leftClear = false;
      boolean rightClear = false;

      if (restOfInput.contains(sortString)) {
        int indexOfSortFound = restOfInput.indexOf(sortString);

        // check left
        try {
          if (restOfInput.charAt(indexOfSortFound - 1) == ' ') {
            leftClear = true;
          }
        } catch (IndexOutOfBoundsException e) {
          leftClear = true;
        }

        // check right
        try {
          if (restOfInput.charAt(indexOfSortFound + sortString.length()) == ' ') {
            rightClear = true;
          }
        } catch (IndexOutOfBoundsException e) {
          rightClear = true;
        }
      }

      return leftClear && rightClear;
    }

    /**
     * Method: removeSortInput
     * 
     * @param restOfInput
     * @param indexOfSortCmd
     * @return resultant string without sort input
     */
    private String removeSortInput(int indexOfSortCmd) {

      String leftEnd = new String();
      String rightEnd = new String();

      try {
        leftEnd = restOfInput.substring(0, indexOfSortCmd - 1);
      } catch (IndexOutOfBoundsException e) {
        leftEnd = "";
      }
      try {
        rightEnd = restOfInput.substring(indexOfSortCmd + 9);
      } catch (IndexOutOfBoundsException e) {
        rightEnd = "";
      }

      return leftEnd.concat(rightEnd);
    }

    /**
     * Method: splitCmdAndParam
     * 
     * @param input
     *          string
     * @return word containing command
     */
    protected String splitCmdAndParam(String input) {
      String cmd = new String();

      try {
        int indexOfFirstSpace = input.indexOf(' ');

        restOfInput = input.substring(indexOfFirstSpace + 1);
        cmd = input.substring(0, indexOfFirstSpace);
      } catch (IndexOutOfBoundsException e) {
        restOfInput = "";
        cmd = input;
      }

      return cmd;
    }

    /**
     * Method: determineCommandType
     * 
     * @param firstWord
     */
    protected Command.Type determineCommandType(String firstWord) {

      Command.Type cmdType = null;

      switch (firstWord) {

      // Use Case: add
        case "add" :
        case "a" :
          cmdType = Command.Type.ADD;
          break;
        // Use Case: view
        case "view" :
          cmdType = Command.Type.DISPLAY;
          break;
        // Use Case: update
        case "update" :
          cmdType = Command.Type.EDIT;
          break;
        // Use Case: delete
        case "delete" :
          cmdType = Command.Type.DELETE;
          break;
        // Use Case: search
        case "search" :
          cmdType = Command.Type.SEARCH;
          break;
        default :
          return null;
      }
      return cmdType;
    }

  }

}
