package goku.storage;

import goku.TaskList;

import java.io.IOException;

/*
 * A Storage is an abstraction of a place where Storeable items can be stored.
 */
public interface Storage {
  public void save(Storeable s) throws IOException;

  public void saveAllArr(Storeable[] array) throws IOException;

  public void saveAll(TaskList list) throws IOException;

  public void delete();
}
