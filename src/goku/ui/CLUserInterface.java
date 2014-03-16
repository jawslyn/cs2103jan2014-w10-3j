package goku.ui;

import goku.GOKU;
import goku.Result;
import goku.TaskList;
import goku.action.Action;
import goku.action.DisplayAction;
import goku.action.ExitAction;
import goku.action.MakeActionException;
import goku.action.SearchAction;
import goku.storage.Storage;
import goku.storage.StorageFactory;

import java.io.IOException;
import java.util.Scanner;

/**
 * User interface that acts as the intermediate component between the user
 * interface and the main logic of GOKU.
 */
public class CLUserInterface implements UserInterface {
  private GOKU goku;
  private Scanner sc;
  private Storage storage;
  private InputParser parser;

  public CLUserInterface(GOKU goku) {
    this.goku = goku;
    sc = new Scanner(System.in);
    storage = StorageFactory.getDefaultStorage();
    parser = new InputParser(goku);
  }

  @Override
  public void run() {
    System.out.println("This is GOKU. How can I help?");
    loop();
    System.out.println("Thanks for using G.O.K.U.!");
  }

  public void loop() {
    while (true) {
      System.out.print(">> ");
      Action action = null;
      String input = null;
      try {
        input = getUserInput();
        action = parser.parse(input);
        doAction(action);
      } catch (MakeActionException e) {
        System.out.println(e.getMessage());
      }
      if (action instanceof ExitAction) {
        return;
      }
    }
  }

  private void doAction(Action action) throws MakeActionException {
    if (action instanceof DisplayAction) {
      printTaskList(action.doIt().getTasks());
    } else if (action instanceof SearchAction) {
      Result result = action.doIt();
      feedBack(result);
    } else {
      Result result = action.doIt();
      feedBack(result);
      save();
    }
  }

  public void save() {
    try {
      storage.saveAll(goku.getTaskList());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error saving tasks.");
    }
  }

  /**
   * @return string that user entered
   */
  @Override
  public String getUserInput() {
    return sc.nextLine();
  }

  @Override
  public void feedBack(Result result) {
    if (result == null) {
      return;
    }
    if (result.isSuccess()) {
      System.out.println(result.getSuccessMsg());
      if (result.getTasks() != null) {
        printTaskList(result.getTasks());
      }
    } else {
      System.out.println(result.getErrorMsg());
      if (result.getTasks() != null) {
        // printTaskList(result.getTasks());
      }
    }
  }

  public void printTaskList(TaskList list) {
    TaskListDisplayer tld = new TaskListDisplayer(System.out);
    tld.display(list);
  }
}
