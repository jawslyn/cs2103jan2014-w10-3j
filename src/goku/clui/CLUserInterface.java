package goku.clui;

import goku.Command;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;

import java.util.ListIterator;
import java.util.Scanner;

/**
 * User interface that acts as the intermediate component between the user
 * interface and the main logic of GOKU.
 * 
 * @author jchiam
 */
public class CLUserInterface implements UserInterface {
  Parser parser;
  private GOKU goku;
  private Scanner sc;
  private String input;

  public CLUserInterface(GOKU goku) {
    this.goku = goku;
    parser = new CLUIParser();
    sc = new Scanner(System.in);
    input = "";
  }

  @Override
  public void run() {
    boolean shouldRun = true;
    while (shouldRun) {
      Command c = makeCommand(getUserInput());
      if (c.isStopCommand()) {
        shouldRun = false;
      } else {
        Result result = goku.executeCommand(c);
        feedBack(result);
      }
    }
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
  }
}
