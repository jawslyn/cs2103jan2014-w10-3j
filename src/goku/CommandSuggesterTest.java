//@author A0099903R
package goku;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author ZhiAn
 * 
 */
public class CommandSuggesterTest {

  @Test
  public void getSuggestion_success() {
    assertEquals("add", CommandSuggester.getSuggestion("ad"));
    assertEquals("add!", CommandSuggester.getSuggestion("addd"));
    assertEquals("delete", CommandSuggester.getSuggestion("delete"));
    assertEquals("delete", CommandSuggester.getSuggestion("delet"));
    assertEquals("delete", CommandSuggester.getSuggestion("deleet"));
    assertEquals("search", CommandSuggester.getSuggestion("saerch"));
    assertEquals("search", CommandSuggester.getSuggestion("searhc"));
    assertEquals("undo", CommandSuggester.getSuggestion("udno"));
  }

}
