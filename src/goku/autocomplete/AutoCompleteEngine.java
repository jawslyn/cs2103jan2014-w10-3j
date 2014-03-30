package goku.autocomplete;

import java.util.List;

public class AutoCompleteEngine {
  private WordAutoComplete wordAuto;
  private CommandAutoComplete commandAuto;
  public static final int commandContext = -1;

  public AutoCompleteEngine() {
    wordAuto = new WordAutoComplete();
    commandAuto = new CommandAutoComplete();
  }

  /*
   * Completes a prefix based on a specified completionContext.
   * A completion context indicates where the completion is for.
   * For example, a completion when the user is typing in a command is
   * different from when the user is entering the title of a task.
   * In this case, -1 represents the context of entering a command.
   */
  public List<String> complete(String prefix, int completionContext) {
    switch (completionContext) {
      case -1 :
        return commandAuto.complete(prefix);
      default :
        return wordAuto.complete(prefix);
    }
  }

  public int getCommandContext() {
    return -1;
  }

  public int getNormalContext() {
    return 0;
  }

}
