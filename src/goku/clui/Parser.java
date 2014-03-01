package goku.clui;

import goku.Task;

/**
 * Parser interface to parse String input and create a relevant Task object
 * 
 * @author jchiam
 **/
public interface Parser {

  /**
   * Method: parseString
   * 
   * @param input
   *          command string
   * @return Task object
   */
  Task parseString(String input);
}
