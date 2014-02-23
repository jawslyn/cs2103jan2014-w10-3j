package goku.clui;

import goku.Command;
import goku.Result;

/*
 * This interface governs the interactions between the UI (be it CLI or GUI) and GokuMain.
 * It acts as the parser from UI to Main and processes the result from Main to UI
 */
public interface UserInterface {

	/**
	 * Method: getUserInput
	 * @param input string from UI
	 * @return Command object
	 */
	Command getUserInput(String input);
	
	/**
	 * Method: feedBack
	 * @param result obtained from Main
	 * @return string output to UI
	 */
	String feedBack(Result result);
	
}
