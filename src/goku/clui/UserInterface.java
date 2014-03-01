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

  /*** STRING CONSTANTS ***/
  public static final String INPUT_ERROR = "Input cannot be recognised.";
  public static final String DATE_ERROR = "Invalid date(s).";

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
