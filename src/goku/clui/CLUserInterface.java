package goku.clui;

import goku.Command;
import goku.Command.SortOrder;
import goku.Command.Type;
import goku.Result;
import goku.Task;

/**
 * user interface that acts as the intermediate component being user interface and the main logic of GOKU 
 * @author jchiam
 */
public class CLUserInterface implements UserInterface{

	/*** GLOBAL VARIABLES ***/
	private Task toDo = null;
	private static Type commandType = null;
	private String source = null;
	private static SortOrder sortOrder = null;
	
	
	/**
	 * Method: getUserInput
	 * @param input string
	 * @return Command object
	 * Gets parser to modify necessary global variables then create Command object 
	 */
	public Command getUserInput(String input) {
		
		CLUIParser parser = new CLUIParser();
		
		// Parse, creates task modifies necessary Command object parameters
		toDo = parser.parseString(input);
		
		if(sortOrder == null) {
			return new Command(input, commandType, toDo);
		} else {
			return new Command(input, commandType, toDo, sortOrder);
		}
	}

	public String feedBack(Result result) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * Parser that deals with String input from user to extract necessary information to create Command object
	 * @author jchiam
	 */
	private static class CLUIParser implements Parser{
		
		/**
		 * Method: parseString
		 * @param input string to be parsed
		 * @return created task
		 */
		public Task parseString(String input) {
			
			String restOfInput = new String();	// string that holds remaining unprocessed input
			
			//EXTRACT COMMAND TYPE RETURN REMAINING STRING
			// extract first word
			String firstWord = splitCmdAndParam(input, restOfInput);
			
			// determine commandType
			determineCommandType(firstWord);
			
			//FIND STRING FOR SORTORDER
			determineSortOrder(restOfInput);
			
			return null;
		}

		/**
		 * Method: determineSortOrder
		 * @param input
		 */
		private void determineSortOrder(String input) {
			if(input.contains("sort:")) {
				
				// find out what sort it is
				int indexOfSortCmd = 0;
				if(input.contains("sort:EDF")) {
					sortOrder = Command.SortOrder.EARLIEST_DEADLINE_FIRST;
					indexOfSortCmd = input.indexOf("sort:EDF");
					
					input = removeSortInput(input, indexOfSortCmd);
				}
				else if(input.contains("sort:HPF")) {
					sortOrder = Command.SortOrder.HIGHEST_PRIORITY_FIRST;
					indexOfSortCmd = input.indexOf("sort:EDF");
					
					input = removeSortInput(input, indexOfSortCmd);
				}
				else {
					//TODO throw error message for invalid sort order
				}
				
				
			}
		}

		/**
		 * Method: removeSortInput
		 * @param restOfInput
		 * @param indexOfSortCmd
		 * @return resultant string without sort input
		 */
		private String removeSortInput(String restOfInput, int indexOfSortCmd) {
			// remove sort input
			// TODO catch index out of bounds
			return restOfInput.substring(0, indexOfSortCmd-1).concat(
					restOfInput.substring(indexOfSortCmd+9));
		}

		/**
		 * Method: splitCmdAndParam
		 * @param input string
		 * @param cmd to take command word
		 * @param param to take rest of string
		 * @return word containing command
		 */
		private String splitCmdAndParam(String input, String param) {
			int indexOfFirstSpace = input.indexOf(' ');
			param = input.substring(indexOfFirstSpace+1);
			String cmd = input.substring(0, indexOfFirstSpace+1);
			
			return cmd;
		}

		/**
		 * Method: determineCommandType
		 * @param firstWord
		 */
		private void determineCommandType(String firstWord) {
			switch(firstWord) {
			
				// Use Case: add
				case "add":
				case "a":
					commandType = Command.Type.ADD;
					break;
					
				// Use Case: view
				case "view":
					commandType = Command.Type.DISPLAY;
					break;
					
				// Use Case: update
				case "update":
					commandType = Command.Type.EDIT;
					break;
					
				// Use Case: delete
				case "delete":
					commandType = Command.Type.DELETE;
					break;
				
				// Use Case: search
				case "search":
					commandType = Command.Type.SEARCH;
					break;
					
				default:
					//TODO error handle invalid command
			}
		}
		
	}

}
