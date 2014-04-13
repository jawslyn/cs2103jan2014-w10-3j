//@author A0099903R
package goku.autocomplete;

import org.junit.Test;

/**
 * @author ZhiAn
 * 
 */
public class CommandAutoCompleteTest extends WordAutoCompleteTest {
  CommandAutoComplete auto = new CommandAutoComplete();

  @Test
  public void complete_a_success() {
    String[] expecteds = { "add" };
    String[] actuals = listToArray(auto.complete("a"));
    assertArraysHaveSameElements(expecteds, actuals);
  }

  @Test
  public void complete_d_success() throws Exception {
    String[] expecteds = { "delete", "display", "do" };
    String[] actuals = listToArray(auto.complete("d"));
    assertArraysHaveSameElements(expecteds, actuals);
  }

  @Test
  public void complete_e_success() throws Exception {
    String[] expecteds = { "edit", "exit" };
    String[] actuals = listToArray(auto.complete("e"));
    assertArraysHaveSameElements(expecteds, actuals);
  }

  @Test
  public void complete_f_success() throws Exception {
    String[] expecteds = { "finish", "find" };
    String[] actuals = listToArray(auto.complete("f"));
    assertArraysHaveSameElements(expecteds, actuals);
  }

  @Test
  public void complete_q_success() throws Exception {
    String[] expecteds = { "quit" };
    String[] actuals = listToArray(auto.complete("q"));
    assertArraysHaveSameElements(expecteds, actuals);
  }

  @Test
  public void complete_s_success() throws Exception {
    String[] expecteds = { "show", "search" };
    String[] actuals = listToArray(auto.complete("s"));
    assertArraysHaveSameElements(expecteds, actuals);
  }

  @Test
  public void complete_u_success() throws Exception {
    String[] expecteds = { "undo", "update" };
    String[] actuals = listToArray(auto.complete("u"));
    assertArraysHaveSameElements(expecteds, actuals);
  }
}
