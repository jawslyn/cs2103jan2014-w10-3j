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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * User interface that acts as the intermediate component between the user
 * interface and the main logic of GOKU.
 */
public class CLUserInterface implements UserInterface {
  private GOKU goku;
  private Scanner sc;
  private Storage storage;
  private InputParser parser;
  private static final Logger LOGGER = Logger
      .getLogger(Logger.GLOBAL_LOGGER_NAME);

  public CLUserInterface(GOKU goku) {
    this.goku = goku;
    sc = new Scanner(System.in);
    storage = StorageFactory.getDefaultStorage();
    parser = new InputParser(goku);
  }

  private void trytoloadfile() {
    try {
      TaskList tasklist = storage.loadStorage();
      goku.setTaskList(tasklist);
    } catch (FileNotFoundException e) {
      LOGGER.warning("File cannot be found, no tasks loaded.");
    } catch (IOException e) {
      LOGGER.warning("Error loading file, no tasks loaded.");
    }
    LOGGER.info("Successfully loaded file: " + storage.getName());
  }

  @Override
  public void run() {
    trytoloadfile();
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
    assert action != null;
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
