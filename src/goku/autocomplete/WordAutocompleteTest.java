package goku.autocomplete;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WordAutoCompleteTest {
  private WordAutoComplete auto;
  private static final String[] TEST_CORPUS_STRING_ARRAY = { "add", "edit",
      "update", "delete", "assignment", "activity" };
  private static final List<String> TEST_CORPUS = Arrays
      .asList(TEST_CORPUS_STRING_ARRAY);

  private static final String PREFIX_WITH_COMPLETION_1 = "a";
  private static final String[] EXPECTED_COMPLETION_1_STRING = { "add",
      "assignment", "activity" };
  private static final String PREFIX_WITH_COMPLETION_2 = "A";
  private static final String[] EXPECTED_COMPLETION_2_STRING = { "add",
      "assignment", "activity" };
  private static final String PREFIX_WITH_COMPLETION_3 = "Ad";
  private static final String[] EXPECTED_COMPLETION_3_STRING = { "add" };
  private static final String PREFIX_WITHOUT_COMPLETION_1 = "i";
  private static final String PREFIX_WITHOUT_COMPLETION_2 = "dd";

  @Before
  public void setup() {
    auto = new WordAutoComplete(TEST_CORPUS);
  }

  @Test
  public void complete_null_returnsEmptyArray() {
    String[] expected = {};
    String[] actual = listToArray(auto.complete(null));
    assertArraysHaveSameElements(expected, actual);
  }

  @Test
  public void complete_emptyString_returnsEmptyArray() throws Exception {
    String[] expected = {};
    String[] actual = listToArray(auto.complete(""));
    assertArraysHaveSameElements(expected, actual);
  }

  @Test
  public void complete_prefixWithCompletion_returnsArrayWithValidCompletion()
      throws Exception {
    String[] expected, actual;
    expected = EXPECTED_COMPLETION_1_STRING;
    actual = listToArray(auto.complete(PREFIX_WITH_COMPLETION_1));
    assertArraysHaveSameElements(expected, actual);
    expected = EXPECTED_COMPLETION_2_STRING;
    actual = listToArray(auto.complete(PREFIX_WITH_COMPLETION_2));
    assertArraysHaveSameElements(expected, actual);
    expected = EXPECTED_COMPLETION_3_STRING;
    actual = listToArray(auto.complete(PREFIX_WITH_COMPLETION_3));
    assertArraysHaveSameElements(expected, actual);
  }

  @Test
  public void complete_prefixWithoutCompletion_returnsEmptyArray()
      throws Exception {
    String[] expected = {}, actual;
    actual = listToArray(auto.complete(PREFIX_WITHOUT_COMPLETION_1));
    assertArraysHaveSameElements(expected, actual);
    actual = listToArray(auto.complete(PREFIX_WITHOUT_COMPLETION_2));
    assertArraysHaveSameElements(expected, actual);

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
