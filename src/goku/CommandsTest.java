package goku;

import java.util.List;

import org.junit.Test;

/**
 * @author ZhiAn
 * 
 */
public class CommandsTest {

  @Test
  public void getAllKeywords_success() {
    List<String> all = Commands.getAllKeywords();
    System.out.println(all.size());
  }

}
