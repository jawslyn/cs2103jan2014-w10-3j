package goku.action;

import goku.CommandSuggester;
import goku.GOKU;
import goku.Result;

/*
 * UnknownAction represents some action which GOKU does not know of.
 * This is likely to be a Command that is not understood by GOKU,
 * such as random gibberish.
 */
public class UnknownAction extends Action {
  public static final String ERR_UNKNOWN_ACTION = "Command \"%s\" is unknown.";
  public static final String ERR_SUGGEST_ACTION = "Did you mean \"%s\"?";
  public String command;

  public UnknownAction(GOKU goku, String command) {
    super(goku);
    this.command = command;
  }

  @Override
  public Result doIt() throws MakeActionException {
    String suggestion = getCommandSuggestion(command);
    return new Result(false, null, String.format(ERR_UNKNOWN_ACTION, command)
        + " " + String.format(ERR_SUGGEST_ACTION, suggestion), null);
  }

  private String getCommandSuggestion(String command) {
    return CommandSuggester.getSuggestion(command);
  }

}
