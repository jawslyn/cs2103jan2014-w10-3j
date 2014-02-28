package goku;

import goku.clui.CLUserInterface;
import goku.clui.UserInterface;

import java.util.Scanner;

public class GokuMain {
  static UserInterface ui;
  static boolean shouldRun = true;
  static GOKU goku = new GOKU();

  public static void main(String[] args) {
    setUserInterface(args);
    runTillExit();
  }

  private static void setUserInterface(String[] args) {
    if (shouldRunCli(args)) {
      ui = new CLUserInterface();
    } else if (shouldRunGui(args)) {
      printImplementedGuiMsg();
      promptUserToContinue();
      ui = new CLUserInterface();
    } else {
      System.exit(0);
    }
  }

  private static void runTillExit() {
    while (shouldRun) {
      Command c = ui.makeCommand(ui.getUserInput());
      if (c.isStopCommand()) {
        shouldRun = false;
      } else {
        Result result = executeCommand(c);
        ui.feedBack(result);
      }
    }
  }

  private static Result executeCommand(Command c) {
    return goku.executeCommand(c);
  }

  private static void promptUserToContinue() {
    Scanner sc = new Scanner(System.in);
    sc.nextLine();
    sc.close();

  }

  private static void printImplementedGuiMsg() {
    System.out
        .println("GUI not implemented yet, shall fire up CLI instead. Press Enter");
  }

  private static boolean shouldRunGui(String[] args) {
    return args[0].equalsIgnoreCase("gui");
  }

  private static boolean shouldRunCli(String[] args) {
    return args.length < 1 || args[0].equalsIgnoreCase("cli");
  }

  public void stop() {
    this.shouldRun = false;
  }
}
