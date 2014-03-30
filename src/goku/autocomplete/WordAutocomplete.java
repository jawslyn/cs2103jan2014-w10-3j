package goku.autocomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * Completes a prefix that has been entered by the user.
 * For completing command keywords, @see CommandAutoComplete
 * WordAutoComplete makes a default corpus and completion suggestions are
 * based of this default corpus.
 * User has the ability to add more words to this corpus for
 * WordAutoComplete to be able to suggest completions from them as well
 */
public class WordAutoComplete {
  private final List<String> DEFAULT_CORPUS = makeDefaultCorpus();
  private List<String> corpus;

  public WordAutoComplete() {
    setDefaultCorpus();
  }

  public WordAutoComplete(List<String> corpus) {
    setCorpus(corpus);
  }

  public WordAutoComplete(List<String> corpus, boolean addToDefault) {
    if (addToDefault) {
      corpus.addAll(DEFAULT_CORPUS);
      setCorpus(corpus);
    }
  }

  public List<String> complete(String prefix) {
    ArrayList<String> suggestions = new ArrayList<String>();
    if (prefix == null || prefix.isEmpty()) {
      return suggestions;
    }
    prefix = prefix.toLowerCase();
    int index = Collections.binarySearch(corpus, prefix);
    if (isCompleted(index)) {
      // what if i type a single letter 'd' which is itself a completed word
      // but i want to see other words that start with d as well?
      return suggestions;
    } else {
      int insertionPoint = getInsertionPoint(index);
      List<String> possibleCompletion = getPossibleCompletions(insertionPoint);
      for (String possibility : possibleCompletion) {
        if (isPossibleCompletion(possibility, prefix)) {
          suggestions.add(corpus.get(insertionPoint));
          insertionPoint++;
        }
      }
      return suggestions;
    }
  }

  /*
   * The insertion point is where a item that is not found in the Collections
   * would be inserted if it was added. Refer to Collections.binarySearch for
   * more information.
   */
  private int getInsertionPoint(int index) {
    return -(index + 1);
  }

  private List<String> getPossibleCompletions(int insertionPoint) {
    return corpus.subList(insertionPoint, corpus.size());
  }

  /*
   * Returns if the prefix is a completed word. In this case no completion is
   * necessary.
   */
  private boolean isCompleted(int indexPrefixIsFound) {
    return indexPrefixIsFound >= 0;
  }

  private boolean isPossibleCompletion(String possibility, String prefix) {
    return possibility.startsWith(prefix);
  }

  private List<String> makeDefaultCorpus() {
    List<String> defaultCorpus = new ArrayList<String>();
    defaultCorpus.addAll(Arrays.asList((new String[] { "the", "and", "with",
        "next", "monday", "tuesday", "wednesday", "thursday", "friday",
        "saturday", "sunday", "week", "from", "by", "on", "do", "find", "play",
        "meet", "meeting", "homework", "project", "completed", "overdue",
        "remove", "period", "deadline", "important" })));

    return defaultCorpus;
  }

  private void setCorpus(List<String> corpus) {
    this.corpus = corpus;
    Collections.sort(corpus);
  }

  private void setDefaultCorpus() {
    setCorpus(DEFAULT_CORPUS);
  }

  public void addToCorpus(String... words) {
    for (String word : words) {
      corpus.add(word);
    }
    Collections.sort(corpus);
  }
}
