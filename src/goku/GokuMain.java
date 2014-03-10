package goku;

import goku.ui.CLUserInterface;
import goku.ui.GUserInterface;
import goku.ui.UserInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class GokuMain {
  static UserInterface ui;
  static GOKU goku = new GOKU();

  public static void main(String[] args) {
    try {
      TaskList tasklist = tryToLoadFile();
      goku.setTaskList(tasklist);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    ui = setUserInterface(args);
    ui.run();
  }

  private static TaskList tryToLoadFile() throws IOException {
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

  private static UserInterface setUserInterface(String[] args) {
    if (shouldRunGui(args)) {
      return new GUserInterface(goku);
    } else {
      return new CLUserInterface(goku);
    }
  }

  private static boolean shouldRunGui(String[] args) {
    if (args.length == 0) {
      return false;
    }
    return args[0].equalsIgnoreCase("gui");
  }

}
