package goku;

import goku.ui.CLUserInterface;
import goku.ui.GUserInterface;
import goku.ui.UserInterface;

public class GokuMain {
  static UserInterface ui;

  public static void main(String[] args) {
    ui = setUserInterface(args);
    ui.run();
  }

  private static UserInterface setUserInterface(String[] args) {
    if (shouldRunGui(args)) {
      return new GUserInterface(new GOKU());
    } else {
      return new CLUserInterface(new GOKU());
    }
  }

  private static boolean shouldRunGui(String[] args) {
    return args[0].equalsIgnoreCase("gui");
  }

}
