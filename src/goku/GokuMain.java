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
    if (shouldRunGui(args)) {
      return new FXGUI(goku);
    } else {
      return new CLUserInterface(goku);
    }
  }

  private static boolean shouldRunGui(String[] args) {
    if (args.length == 0) {
      return false;
    }
    return args[0].equalsIgnoreCase("gui");
  }

}
