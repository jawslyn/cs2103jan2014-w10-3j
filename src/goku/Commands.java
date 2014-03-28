package goku;

import java.util.Arrays;
import java.util.List;

public class Commands {
  public static String[] addKeywords = { "add", "a", "add!" };
  public static String[] deleteKeywords = { "delete", "d", "remove", "r" };
  public static String[] editKeywords = { "edit", "e", "update", "u" };
  public static String[] completeKeywords = { "done", "complete", "do",
      "finish", "fin" };
  public static String[] displayKeywords = { "display", "view", "show", "v" };
  public static String[] searchKeywords = { "search", "find", "f", "free" };
  public static String[] exitKeywords = { "quit", "exit", "q" };
  public static String[] undoKeywords = { "undo", "revert", "rollback" };
  public static String[] redoKeywords = { "redo" };

  public static List<String> getAllKeywords() {
    return Arrays.asList(mergeArrays(addKeywords, deleteKeywords, editKeywords,
        completeKeywords, displayKeywords, searchKeywords, exitKeywords,
        undoKeywords, redoKeywords));

  }

  public static String[] mergeArrays(String[]... arrays) {
    String[] merged = {};
    for (String[] array : arrays) {
      String[] merged2 = new String[merged.length + array.length];
      System.arraycopy(merged, 0, merged2, 0, merged.length);
      System.arraycopy(array, 0, merged2, merged.length, array.length);
      merged = merged2;
    }
    return merged;
  }
}
