package goku.storage;

import goku.TaskList;

import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * A Storage is an abstraction of a place where Storeable items can be stored.
 */
public interface Storage {

  public String getName();

  public void saveAll(Iterable<Storeable> list) throws IOException;

  public void saveAll(TaskList tasklist) throws IOException;

  public void delete();

  public TaskList loadStorage() throws FileNotFoundException, IOException;
}
