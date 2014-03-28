package goku.autocomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * CommandAutoComplete completes prefixes that is likely to be a command.
 * This words exactly like a @see WordAutoComplete but just with
 * a corpus that has all the commands GOKU accepts and recognizes
 */
public class CommandAutoComplete extends WordAutoComplete {

  private final static List<String> DEFAULT_CORPUS = makeCommandCorpus();

  public CommandAutoComplete() {
    super(DEFAULT_CORPUS);
  }

  private static List<String> makeCommandCorpus() {
    List<String> defaultCorpus = new ArrayList<String>();
    defaultCorpus.addAll(Arrays.asList((new String[] { "add", "edit", "delete",
        "update", "completed", "do", "finish", "display", "view", "show",
        "search", "find", "quit", "exit", "undo" })));
    return defaultCorpus;
  }
}
