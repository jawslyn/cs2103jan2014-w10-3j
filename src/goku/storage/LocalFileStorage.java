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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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
  public TaskList loadStorage() throws FileNotFoundException, IOException,
      LoadTasksException {
    List<Integer> errorLines = new ArrayList<Integer>();
    int currentLine = 1;
    LOGGER.info("Loading from file: " + file.getName());
    TaskList tasklist = new TaskList();
    try (BufferedReader br = new BufferedReader(new FileReader("store.goku"))) {
      String line = br.readLine();
      while (line != null) {
        Gson gson = new Gson();
        try {
          Task task = gson.fromJson(line, Task.class);
          tasklist.addTask(task);
        } catch (JsonSyntaxException e) {
          LOGGER.warning("Error loading on json on line " + currentLine + ": "
              + line + "\n" + e.getMessage());
          errorLines.add(currentLine);
        }
        line = br.readLine();
        currentLine++;
      }
    }
    if (errorLines.size() != 0) {
      makeBackupFile();
      throw new LoadTasksException(errorLines, tasklist);
    }
    return tasklist;
  }

  private void makeBackupFile() {
    File backup = new File("store.goku.backup");
    LOGGER.info("Making a backup for " + file.getName() + " at "
        + backup.getName());
    try (BufferedReader br = new BufferedReader(new FileReader("store.goku"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(backup))) {
      String line = br.readLine();
      while (line != null) {
        bw.write(line);
        bw.write(System.lineSeparator());
        line = br.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
