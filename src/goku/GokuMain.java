package goku;

import goku.clui.CLUserInterface;
import goku.clui.GUserInterface;
import goku.clui.UserInterface;

public class GokuMain {
  static UserInterface ui;
  static boolean shouldRun = true;
  public static GOKU goku = new GOKU();

  public static void main(String[] args) {
    setUserInterface(args);
    ui.run();
  }

  private static void setUserInterface(String[] args) {
    if (shouldRunCli(args)) {
      ui = new CLUserInterface(goku);
    } else if (shouldRunGui(args)) {
      ui = new GUserInterface(goku);
      goku.setTaskList(new ObservableTaskList());
    } else {
      System.exit(0);
    }
  }

  private static boolean shouldRunGui(String[] args) {
    return args[0].equalsIgnoreCase("gui");
  }

  private static boolean shouldRunCli(String[] args) {
    return args.length < 1 || args[0].equalsIgnoreCase("cli");
  }

}
