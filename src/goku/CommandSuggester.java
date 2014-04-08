package goku;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author ZhiAn
 * 
 */
public class CommandSuggester {
  private static List<String> allCommands = Commands.getAllKeywords();

  public static String getSuggestion(String toSuggest) {
    SortedSet<Suggestion> suggestions = getDistances(toSuggest);
    return suggestions.first().word;
  }

  private static SortedSet<Suggestion> getDistances(String toSuggest) {
    SortedSet<Suggestion> suggestions = new TreeSet<>();
    for (String candidate : allCommands) {
      suggestions.add(new Suggestion(distance(toSuggest, candidate), candidate,
          toSuggest.length() - candidate.length()));
    }
    return suggestions;
  }

  // from
  // http://www.jdepths.com/2012/11/levenshtein-edit-distance-algorithm-in.html
  public static int distance(String s1, String s2) {
    int edits[][] = new int[s1.length() + 1][s2.length() + 1];
    for (int i = 0; i <= s1.length(); i++) {
      edits[i][0] = i;
    }
    for (int j = 1; j <= s2.length(); j++) {
      edits[0][j] = j;
    }
    for (int i = 1; i <= s1.length(); i++) {
      for (int j = 1; j <= s2.length(); j++) {
        int u = (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1);
        edits[i][j] = Math.min(edits[i - 1][j] + 1,
            Math.min(edits[i][j - 1] + 1, edits[i - 1][j - 1] + u));
        // this is useful for transposition but I need to get it to work first
        // if (i > 1 && j > 1 && s1.charAt(i) == s2.charAt(j - 1)
        // && s1.charAt(i - 1) == s2.charAt(j)) {
        // edits[i][j] = Math.min(edits[i][j], edits[i - 2][j - 2] + 1);
        // }
      }
    }
    return edits[s1.length()][s2.length()];
  }

  private static class Suggestion implements Comparable<Suggestion> {
    int distance;
    String word;
    int diff;

    public Suggestion(int distance, String word, int diff) {
      this.distance = distance;
      this.word = word;
      this.diff = diff;
    }

    @Override
    public int compareTo(Suggestion s) {
      if ((distance - s.distance) == 0) {
        return Math.abs(diff) - Math.abs(s.diff);
      } else {
        return distance - s.distance;
      }
    }
  }

}
