package goku.clui;

import goku.Command;
import goku.Result;

/**
 * This interface governs the interactions between the UI (be it CLI or GUI) and
 * GokuMain. It acts as the parser from UI to Main and processes the result from
 * Main to UI
 * 
 * @author jchiam
 **/
public interface UserInterface {

  /**
   * Method: getUserInput
   * 
   * @param input
   *          string from UI
   * @return Command object
   */
  Command makeCommand(String input);

  String getUserInput();

  /**
   * Method: feedBack
   * 
   * @param result
   *          obtained from Main
   */
  void feedBack(Result result);

}
