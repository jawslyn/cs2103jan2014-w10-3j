package goku.storage;

/*
 * A Storable is an object that can be placed into a Storage
 */
public interface Storeable {
  public String toStorageFormat();
}
