package goku.autocomplete;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author ZhiAn
 * 
 */
public class AutoCompleteEngineTest {
  public AutoCompleteEngine auto = new AutoCompleteEngine();

  @Test
  public void complete_CommandContext() {
    String[] expecteds = { "add" };
    String[] actuals = listToArray(auto.complete("a",
        AutoCompleteEngine.commandContext));
    assertArraysHaveSameElements(expecteds, actuals);
  }

  String[] listToArray(List<String> list) {
    return list.toArray(new String[list.size()]);
  }

  void assertArraysHaveSameElements(String[] expecteds, String[] actuals) {
    Arrays.sort(expecteds);
    Arrays.sort(actuals);
    try {
      Assert.assertArrayEquals(expecteds, actuals);
    } catch (AssertionError e) {
      Assert.fail(e.getMessage());
    }
  }

}
