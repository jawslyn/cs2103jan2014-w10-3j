//@author A0099903R
package goku.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ZhiAn
 * 
 */
public class MockStoreableList implements Iterable<Storeable> {
  private List<Storeable> _list = new ArrayList<Storeable>();

  public void add(MockStoreable storeable) {
    _list.add(storeable);
  }

  public void clear() {
    _list.clear();
  }

  /*
   * A simple concrete implementation of Storeable for testing.
   */
  class MockStoreable implements Storeable {
    private Integer id;
    private String title;

    public MockStoreable(Integer id, String title) {
      this.id = id;
      this.title = title;
    }

    @Override
    public String toStorageFormat() {
      return "" + this.id + " : " + this.title;
    }
  }

  @Override
  public Iterator<Storeable> iterator() {
    return _list.iterator();
  }
}
