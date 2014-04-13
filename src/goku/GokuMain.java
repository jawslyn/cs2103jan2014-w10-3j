//@author A0099903R
package goku;

import goku.ui.CLUserInterface;
import goku.ui.FXGUI;
import goku.ui.UserInterface;

/**
 * Entry point of GOKU
 * 
 * @author ZhiAn
 * 
 */
public class GokuMain {
  static UserInterface ui;
  static GOKU goku = new GOKU();

  /*
   * Runs either a Command Line or Graphical Interface polymorphically
   */
  public static void main(String[] args) {
    ui = setUserInterface(args);
    ui.run();
  }

  /*
   * Determines which UserInterface to run based on the arguments to
   * the program.
   */
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
