package goku.storage;

/*
 * A simple concrete implementation of Storeable for testing.
 */
public class MockStoreable implements Storeable {
  private Integer id;
  private String title;

  public MockStoreable(Integer id, String title) {
    this.id = id;
    this.title = title;
  }

  @Override
  public String toStringFormat() {
    return "" + this.id + " : " + this.title;
  }

}
