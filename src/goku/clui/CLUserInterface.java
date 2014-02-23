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
	private Type commandType = null;
	//TODO what is source???
	private String source = null;
	private SortOrder sortOrder = null;
	
	
	/**
	 * Method: getUserInput
	 * @param input string
	 * @return Command object
	 * Gets parser to modify necessary global variables then create Command object 
	 */
	public Command getUserInput(String input) {
		
		CLUIParser parser = new CLUIParser();
		
		// Parse, creates task modifies necessary Command object parameters
		Task toDo = parser.parseString(input);
		
		return new Command(source, commandType, toDo, sortOrder);
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
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
