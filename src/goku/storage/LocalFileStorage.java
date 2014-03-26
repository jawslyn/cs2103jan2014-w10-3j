package goku.storage;

import goku.Task;
import goku.TaskList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import com.google.gson.Gson;

/*
 * LocalFileStorage saves objects into a file on the local file system. By
 * default the file has the name "store.goku".
 */
public class LocalFileStorage implements Storage {
  private static final String ERR_NULL_STOREABLE_ARRAY = "Storeable[] cannot be null!";
  private static final String DEFAULT_FILENAME = "store.goku";
  private static final Logger LOGGER = Logger
      .getLogger(Logger.GLOBAL_LOGGER_NAME);

  private static File file;

  public LocalFileStorage() {
    file = new File(DEFAULT_FILENAME);
  }

  public LocalFileStorage(String filename) {
    if (filename == null) {
      filename = DEFAULT_FILENAME;
    }
    file = new File(filename);
  }

  @Override
  public String getName() {
    return file.getAbsolutePath();
  }

  @Override
  public void saveAll(Iterable<Storeable> list) throws IOException {
    if (list == null) {
      throw new NullPointerException(ERR_NULL_STOREABLE_ARRAY);
    }
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
      for (Storeable t : list) {
        bw.write(t.toStorageFormat());
        bw.write(System.lineSeparator());
      }
    }
    LOGGER.info("Save to file: " + file.getName());
  }

  @Override
  public void saveAll(TaskList tasklist) throws IOException {
    if (tasklist == null) {
      throw new NullPointerException(ERR_NULL_STOREABLE_ARRAY);
    }
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
      for (Task t : tasklist) {
        bw.write(t.toStorageFormat());
        bw.write(System.lineSeparator());
      }
    }
    LOGGER.info("Save to file: " + file.getName());
  }

  @Override
  public void delete() {
    assert file != null;
    String name = file.getName();
    file.delete();
    LOGGER.warning("Delete file: " + name);
  }

  @Override
  public TaskList loadStorage() throws FileNotFoundException, IOException {
    LOGGER.info("Loading from file: " + file.getName());
    TaskList tasklist = new TaskList();
    try (BufferedReader br = new BufferedReader(new FileReader("store.goku"))) {
      String line = br.readLine();
      while (line != null) {
        Gson gson = new Gson();
        Task task = gson.fromJson(line, Task.class);
        tasklist.addTaskWithoutSettingId(task);
        line = br.readLine();
      }
    }
    return tasklist;
  }
}
