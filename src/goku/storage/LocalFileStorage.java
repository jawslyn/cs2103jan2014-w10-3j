package goku.storage;

import goku.Task;
import goku.TaskList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * LocalFileStorage saves objects into a file on the local file system. By
 * default the file has the name "store.goku".
 */
public class LocalFileStorage implements Storage {
  private static final String ERR_NULL_STOREABLE_ARRAY = "Storeable[] cannot be null!";
  private static final String ERR_NULL_STOREABLE = "Storeable cannot be null!";

  private static final String DEFAULT_FILENAME = "store.goku";
  private static File file;

  public LocalFileStorage() {
    file = new File(DEFAULT_FILENAME);
  }

  public LocalFileStorage(String filename) {
    file = new File(filename);
  }

  /*
   * Saves a single Storeable into file
   */
  @Override
  public void save(Storeable s) throws IOException {
    if (s == null) {
      throw new NullPointerException(ERR_NULL_STOREABLE);
    }
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
      bw.write(s.toStorageFormat());
      bw.write(System.lineSeparator());
    }
  }

  /*
   * Saves an array of Storeable into file
   */
  @Override
  public void saveAllArr(Storeable[] array) throws IOException {
    if (array == null) {
      throw new NullPointerException(ERR_NULL_STOREABLE_ARRAY);
    }
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
      for (Storeable s : array) {
        bw.write(s.toStorageFormat());
        bw.write(System.lineSeparator());
      }
    }
  }

  @Override
  public void saveAll(TaskList list) throws IOException {
    if (list == null) {
      throw new NullPointerException(ERR_NULL_STOREABLE_ARRAY);
    }
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
      for (Task t : list) {
        bw.write(t.toStorageFormat());
        bw.write(System.lineSeparator());
      }
    }

  }

  @Override
  public void delete() {
    if (file != null) {
      file.delete();
    }
  }
}
