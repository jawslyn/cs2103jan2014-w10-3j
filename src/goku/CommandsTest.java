//@author A0099903R
package goku;

import java.util.List;

import org.junit.Test;

public class CommandsTest {

  @Test
  public void getAllKeywords_success() {
    List<String> all = Commands.getAllKeywords();
    System.out.println(all.size());
  }

}
