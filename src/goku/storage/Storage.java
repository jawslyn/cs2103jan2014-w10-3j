//@author A0099903R
package goku.storage;

import goku.TaskList;

import java.io.IOException;

/**
 * A Storage is an abstraction of a place where Storeable items can be stored.
 * 
 * @author ZhiAn
 * 
 */
public interface Storage {

  public String getName();

  public void saveAll(Iterable<Storeable> list) throws IOException;

  /*
   * Saves all tasks in the TaskList to the underlying storage.
   */
  public void saveAll(TaskList tasklist) throws IOException;

  public void delete();

  /*
   * Loads information from a Storage into a TaskList.
   * An IOException is thrown when somehow the loading is interrupted.
   * A LoadTaskException occurs when the underlying storage is corrupted and
   * Storage is not able to read in the tasks accurately. In that case,
   * loadStorag() does a best effort recovery:
   * 1. Any lines that are corrupted are skipped over
   * 2. Lines that are fine are loaded into the TaskList
   * 3. Line numbers of the lines that are corrupted are recorded
   * 4. A backup of the old storage medium, with corrupted data, is made
   * 5. Line numbers of corrupted tasks are reported to the user, together
   * with the file name of the backup file
   * The backup file allows users to manually recover the corrupted tasks,
   * and the line numbers allow them to easily see which lines are corrupted.
   * 
   */
  public TaskList loadStorage() throws LoadTasksException, IOException;
}
