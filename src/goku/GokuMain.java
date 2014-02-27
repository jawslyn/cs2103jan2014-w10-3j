package goku;

import goku.clui.CLUserInterface;
import goku.clui.UserInterface;

import java.util.Scanner;

public class GokuMain {
  static UserInterface ui;
  static boolean shouldRun = true;

  public static void main(String[] args) {
    ui = makeUserInterface(args);
    runTillExit();
  }

  private static UserInterface makeUserInterface(String[] args) {
    if (args.length < 1 || args[0].equalsIgnoreCase("cli")) {
    } else if (args[0].equalsIgnoreCase("gui")) {
      System.out
          .println("GUI not implemented yet, shall fire up CLI instead. Press Enter");
      String nextLine = new Scanner(System.in).nextLine();
    }
    return new CLUserInterface();
  }

  private static void runTillExit() {
    System.out.println("HI");
    while (shouldRun) {
      Command c = ui.makeCommand(ui.getUserInput());
      Result result = executeCommand(c);
      ui.feedBack(result);
    }
  }

  private static Result executeCommand(Command c) {
    GOKU.executeCommand(c);
    // TODO Auto-generated method stub
    return null;
  }

  public void stop() {
    this.shouldRun = false;
  }
}
