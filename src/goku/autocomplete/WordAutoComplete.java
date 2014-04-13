//@author A0099903R
package goku.autocomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Completes a prefix that has been entered by the user. For completing command
 * keywords, @see CommandAutoComplete WordAutoComplete makes a default corpus
 * and completion suggestions are based of this default corpus. User has the
 * ability to add more words to this corpus for WordAutoComplete to be able to
 * suggest completions from them as well
 * 
 * @author ZhiAn
 */
public class WordAutoComplete {
  private final static SortedSet<String> DEFAULT_CORPUS = makeDefaultCorpus();
  SortedSet<String> corpus;

  public WordAutoComplete() {
    this(DEFAULT_CORPUS);
  }

  public WordAutoComplete(List<String> words) {
    this(new TreeSet<String>(words));
  }

  public WordAutoComplete(Set<String> words) {
    corpus = new TreeSet<>();
    addToCorpus(words);
  }

  public List<String> complete(String prefix) {
    List<String> suggestions = new ArrayList<String>();
    if (prefix == null || prefix.isEmpty()) {
      return suggestions;
    }
    prefix = prefix.toLowerCase();
    SortedSet<String> possible = corpus.tailSet(prefix);
    for (String possibility : possible) {
      if (isPossibleCompletion(possibility, prefix)) {
        suggestions.add(possibility);
      }
    }
    return suggestions;
  }

  private boolean isPossibleCompletion(String possibility, String prefix) {
    return possibility.startsWith(prefix);
  }

  private static SortedSet<String> makeDefaultCorpus() {
    SortedSet<String> defaultCorpus = new TreeSet<String>();
    defaultCorpus.addAll(Arrays.asList((new String[] { "the", "and", "with",
        "next", "monday", "tuesday", "wednesday", "thursday", "friday",
        "saturday", "sunday", "week", "from", "by", "on", "do", "find", "play",
        "meet", "meeting", "homework", "project", "completed", "overdue",
        "remove", "period", "deadline", "important", "tomorrow" })));
    return defaultCorpus;
  }

  public void addToCorpus(String word) {
    corpus.add(word);
  }

  public void addToCorpus(Set<String> words) {
    for (String word : words) {
      addToCorpus(word);
    }
  }
}
