//@author A0099903R
package goku.ui;

import goku.GOKU;
import goku.Result;
import goku.action.Action;
import goku.action.ExitAction;
import goku.action.MakeActionException;
import goku.storage.LoadTasksException;
import goku.storage.Storage;
import goku.storage.StorageFactory;
import goku.util.InvalidDateRangeException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * User interface that acts as the intermediate component between the user
 * interface and the main logic of GOKU.
 */
public class CLUserInterface implements UserInterface {
  private static final String GOKU_PROMPT = ">> ";
  private static final String GOKU_WELCOME_MESSAGE = "This is GOKU. How can I help?";
  private static final String GOKU_EXIT_MESSAGE = "Thanks for using G.O.K.U.!";
  private static final Logger LOGGER = Logger
      .getLogger(Logger.GLOBAL_LOGGER_NAME);

  private GOKU goku;
  private InputParser parser;
  private Scanner sc = new Scanner(System.in);
  private Storage storage = StorageFactory.getDefaultStorage();

  public CLUserInterface(GOKU goku) {
    this.goku = goku;
    parser = new InputParser(goku);
  }

  private void doAction(Action action) throws MakeActionException {
    assert action != null;
    Result result = action.doIt();
    feedBack(result);
    save();
  }

  @Override
  public void feedBack(Result result) {
    if (result == null) {
      return;
    }
  }

  // @author A0096444X
  /**
   * @return string that user entered
   */
  @Override
  public String getUserInput() {
    return sc.nextLine();
  }

  public void getUserInputUntilExit() {
    while (true) {
      printPrompt();
      try {
        String input = getUserInput();
        Action action = parser.parse(input);
        if (action instanceof ExitAction) {
          return;
        }
        doAction(action);
      } catch (MakeActionException | InvalidDateRangeException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  private void printExitMessage() {
    System.out.println(GOKU_EXIT_MESSAGE);
  }

  private void printPrompt() {
    System.out.print(GOKU_PROMPT);
  }

  private void printWelcomeMessage() {
    System.out.println(GOKU_WELCOME_MESSAGE);
  }

  @Override
  public void run() {
    printWelcomeMessage();
    trytoloadfile();
    getUserInputUntilExit();
    printExitMessage();
  }

  public void save() {
    try {
      storage.saveAll(goku.getTaskList());
    } catch (IOException e) {
      System.out.println("Error saving tasks.");
    }
  }

  private void trytoloadfile() {
    try {
      goku.setTaskList(storage.loadStorage());
    } catch (FileNotFoundException e) {
      LOGGER.warning("File cannot be found, no tasks loaded.");
    } catch (IOException e) {
      LOGGER.warning("Error loading file, no tasks loaded.");
    } catch (LoadTasksException e) {
      e.printStackTrace();
    }
    LOGGER.info("Successfully loaded file: " + storage.getName());
  }
}
