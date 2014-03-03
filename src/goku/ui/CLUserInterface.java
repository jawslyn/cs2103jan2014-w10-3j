package goku.ui;

import goku.GOKU;
import goku.Result;
import goku.TaskList;
import goku.action.Action;
import goku.action.DisplayAction;
import goku.storage.Storage;
import goku.storage.StorageFactory;

import java.io.IOException;
import java.util.Scanner;

/**
 * User interface that acts as the intermediate component between the user
 * interface and the main logic of GOKU.
 * 
 * @author jchiam
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
    loop();
    System.out.println("Thanks for using G.O.K.U.!");
  }

  public void loop() {
    while (true) {
      Action action = parser.parse(getUserInput());
      if (action instanceof ExitAction) {
        return;
      } else if (action instanceof DisplayAction) {
        printTaskList(goku.getTaskList());
      } else {
        Result result = action.doIt();
        feedBack(result);
        save();
      }
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
    if (result.isSuccess()) {
      System.out.println(result.getSuccessMsg());
      if (result.getTasks() != null) {
        printTaskList(result.getTasks());
      }
    } else {
      System.out.println(result.getErrorMsg());
      if (result.getTasks() != null) {
        printTaskList(result.getTasks());
      }
    }
  }

  public void printTaskList(TaskList list) {
    TaskListDisplayer tld = new TaskListDisplayer(System.out);
    tld.display(list);
  }
}
