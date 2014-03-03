package goku.ui;

import goku.Command;
import goku.Command.SortOrder;
import goku.Command.Type;
import goku.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Parser interface to parse String input and create a relevant Task object
 * 
 * @author jchiam
 **/
public abstract class JParser {
  protected static String SORT_ERROR = "Invalid sort.";

  protected String restOfInput = new String();

  /** GLOBAL VARIABLES **/
  protected Type commandType = null;

  protected SortOrder sortOrder = null;

  protected String[] taskTags = new String[10];

  protected Date taskDeadline = null;

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
        cmdType = null;
        break;
    }
    return cmdType;
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
   * Method: findFirstValidSort
   * 
   * @param sort
   *          to assign valid sort to
   * @param tokenBuffer
   *          tokenized string
   * @return
   */
  protected Command.SortOrder findFirstValidSort(Command.SortOrder sort,
      String[] tokenBuffer) {

    // find first occurrence of sort and register, subsequent ones are
    // discarded
    boolean sortFound = false;
    for (int i = 0; i < tokenBuffer.length; i++) {

      // check if string starts with "sort:"
      boolean containsSortHeader = false;
      try {
        containsSortHeader = tokenBuffer[i].substring(0, 5).equals("sort:");
      } catch (StringIndexOutOfBoundsException e) {
        continue;
      }

      // determine sort type
      if (containsSortHeader == true && sortFound == false) {
        try {
          tokenBuffer[i] = tokenBuffer[i].substring(5); // remove "sort:"
        } catch (StringIndexOutOfBoundsException e) {
          tokenBuffer[i] = "";
          continue;
        }

        // determine sort type
        if (tokenBuffer[i].equals("EDF")) {
          sort = Command.SortOrder.EARLIEST_DEADLINE_FIRST;
          tokenBuffer[i] = "";
          sortFound = true;
        } else if (tokenBuffer[i].equals("HPF")) {
          sort = Command.SortOrder.HIGHEST_PRIORITY_FIRST;
          tokenBuffer[i] = "";
          sortFound = true;
        } else {
          sort = Command.SortOrder.EARLIEST_DEADLINE_FIRST;
        }
      }
      // remove all subsequent sort options found
      else if (containsSortHeader == true && sortFound == true) {
        tokenBuffer[i] = "";
      }
    }
    return sort;
  }

  /**
   * Method: reconstructInput
   * 
   * @param tokenBuffer
   *          Reconstructs remain rest of input from array of string tokens
   */
  protected void reconstructInput(String[] tokenBuffer) {
    // concatenate remaining input back into restOfInput
    restOfInput = "";
    for (String token : tokenBuffer) {
      if (!token.equals("")) {
        restOfInput = restOfInput.concat(token + ' ');
      }
    }

    // remove last space at tail
    restOfInput = restOfInput.trim();
  }

  /**
   * Method: determineSortOrder
   */
  protected Command.SortOrder determineSortOrder() {

    Command.SortOrder sort = null;

    // split string by spaces
    String[] tokenBuffer = restOfInput.split(" ");

    // if input string is empty
    if (tokenBuffer.length == 1 && tokenBuffer[0] == "") {
      return null;
    }

    sort = findFirstValidSort(sort, tokenBuffer);

    reconstructInput(tokenBuffer);

    return sort;
  }

  /**
   * Method: findAddRemoveTags
   * 
   * @param tags
   * @param tokenBuffer
   *          Iterates through token array, find, add and remove any tags
   */
  protected void findAddRemoveTags(ArrayList<String> tags, String[] tokenBuffer) {
    // find tags in buffer
    int tagCount = 0; // track number of tags
    for (int i = 0; i < tokenBuffer.length && tagCount < 10; i++) {
      // if token is a tag
      if (tokenBuffer[i].charAt(0) == '#') {
        tokenBuffer[i] = tokenBuffer[i].substring(1); // remove '#'
        tags.add(tokenBuffer[i]); // add tag
        tokenBuffer[i] = ""; // empty token from buffer
        tagCount++;
      }
    }
  }

  /**
   * Method: extractTags
   * 
   * @return string array containing tags
   */
  protected String[] extractTags() {

    ArrayList<String> tags = new ArrayList<String>();

    // split string by spaces
    String[] tokenBuffer = restOfInput.split(" ");

    // if input string is empty
    if (tokenBuffer.length == 1 && tokenBuffer[0] == "") {
      return new String[10];
    }

    findAddRemoveTags(tags, tokenBuffer);

    reconstructInput(tokenBuffer);

    return tags.toArray(new String[10]);
  }

  /**
   * Method: findDeadlineDate
   * 
   * @param tokenBuffer
   * @return date in string format
   * @return null if no due: found
   */
  protected String findDeadlineDate(String[] tokenBuffer) {

    String dateString = null;

    // if input string is empty
    if (tokenBuffer.length == 1 && tokenBuffer[0] == "") {
      return null;
    }

    // find first occurrence of due and register, subsequent ones are
    // discarded
    boolean dueFound = false;
    for (int i = 0; i < tokenBuffer.length; i++) {

      // check if string starts with "sort:"
      boolean containsDueHeader = false;
      try {
        containsDueHeader = tokenBuffer[i].substring(0, 4).equals("due:");
      } catch (StringIndexOutOfBoundsException e) {
        continue;
      }

      // determine sort type
      if (containsDueHeader == true && dueFound == false) {
        try {
          tokenBuffer[i] = tokenBuffer[i].substring(4); // remove "due:"
        } catch (StringIndexOutOfBoundsException e) {
          tokenBuffer[i] = "";
          continue;
        }

        if (tokenBuffer[i].length() > 0) {
          dateString = tokenBuffer[i]; // store string containing date
          tokenBuffer[i] = ""; // remove from input
          dueFound = true;
        } else {
          dateString = null;
        }
      }
      // remove all subsequent due options found
      else if (containsDueHeader == true && dueFound == true) {
        tokenBuffer[i] = "";
      }
    }

    return dateString;
  }

  /**
   * Method: extractDeadline
   * 
   * @return deadline Returns null if no deadline found
   */
  protected Date extractDeadline() {

    Date deadline = null;

    String[] tokenBuffer = restOfInput.split(" ");

    // TODO to improve formatter in future revisions
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/M/yyyy");

    String preParsedDate = findDeadlineDate(tokenBuffer);

    reconstructInput(tokenBuffer);

    // if deadline date cannot be found
    if (preParsedDate == null) {
      return null;
    }

    // parse date string
    try {
      deadline = dateFormatter.parse(preParsedDate);
    } catch (ParseException e) {
      deadline = null;
    }

    return deadline;

  }

  /**
   * Method: createTask Creates task with appropriate parameters TODO modify to
   * add more params in future revisions
   */
  protected Task createTask(boolean status, Date deadline, String[] tags,
      String title) {

    Task toDo = new Task();
    toDo.setStatus(status);
    toDo.setDeadline(deadline);
    toDo.setTags(tags);
    toDo.setTitle(title);

    return toDo;
  }

  /**
   * Method: parseString
   * 
   * @param input
   *          string to be parsed
   * @return created task
   */
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

  public Command parseToCommand(String input) {
    Task toDo = parseString(input);
    if (sortOrder == null) {
      return new Command(input, commandType, toDo);
    } else {
      return new Command(input, commandType, toDo, sortOrder);
    }
  }
}
