package goku.autocomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WordAutocomplete {
  private final List<String> DEFAULT_CORPUS = makeDefaultCorpus();
  private List<String> corpus;

  public WordAutocomplete() {
    setDefaultCorpus();
  }

  public WordAutocomplete(List<String> corpus) {
    setCorpus(corpus);
  }

  public WordAutocomplete(List<String> corpus, boolean addToDefault) {
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
    defaultCorpus.addAll(Arrays.asList((new String[] { "add", "edit", "delete",
        "remove", "update", "done", "completed", "do", "finish", "display",
        "view", "show", "search", "find", "quit", "exit", "undo", "revert",
        "rollback" })));

    return defaultCorpus;
  }

  private void setCorpus(List<String> corpus) {
    this.corpus = corpus;
    Collections.sort(corpus);
  }

  private void setDefaultCorpus() {
    setCorpus(DEFAULT_CORPUS);
  }
}
