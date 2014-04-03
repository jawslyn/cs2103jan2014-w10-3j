package goku;

import goku.ui.CLUserInterface;
import goku.ui.FXGUI;
import goku.ui.UserInterface;

public class GokuMain {
  static UserInterface ui;
  static GOKU goku = new GOKU();

  public static void main(String[] args) {
    ui = setUserInterface(args);
    ui.run();
  }

  private static UserInterface setUserInterface(String[] args) {
    if (shouldRunCli(args)) {
      return new CLUserInterface(goku);
    } else {
      return new FXGUI(goku);
    }
  }

  private static boolean shouldRunCli(String[] args) {
    if (args.length == 0) {
      return false;
    }
    return args[0].equalsIgnoreCase("cli");
  }

}
