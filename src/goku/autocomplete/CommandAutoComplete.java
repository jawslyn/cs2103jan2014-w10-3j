package goku.autocomplete;

import goku.Commands;

import java.util.ArrayList;
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
    defaultCorpus.addAll(Commands.getAllKeywords());
    return defaultCorpus;
  }
}
