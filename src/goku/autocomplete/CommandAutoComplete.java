package goku.autocomplete;

import goku.Commands;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * CommandAutoComplete completes prefixes that is likely to be a command. This
 * words exactly like a @see WordAutoComplete but just with a corpus that has
 * all the commands GOKU accepts and recognizes
 * 
 * @author ZhiAn
 * 
 */
public class CommandAutoComplete extends WordAutoComplete {

  private final static Set<String> DEFAULT_CORPUS = makeCommandCorpus();

  public CommandAutoComplete() {
    super(DEFAULT_CORPUS);
  }

  private static SortedSet<String> makeCommandCorpus() {
    SortedSet<String> defaultCorpus = new TreeSet<String>();
    defaultCorpus.addAll(Commands.getAllKeywords());
    return defaultCorpus;
  }
}
