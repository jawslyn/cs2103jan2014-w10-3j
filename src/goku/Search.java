package goku;

import java.io.IOException;
import java.util.ArrayList;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
class Search {

  public static void main(String[] args) throws IOException {

  }

  public Search() {

  }

  public Result searchTag(Command command) {
    ArrayList<Task> result = new ArrayList<Task>();
    String tempString = command.getTask().getTags().toString();
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      String[] tempArray = GOKU.getAllTasks().get(i).getTags();
      for (String element : tempArray) {
        if (element.contains(tempString)) {
          result.add(GOKU.getAllTasks().get(i));
        }
      }
    }

    return new Result(true, null, null, result);
  }

  public Result searchTitle(Command command) {
    ArrayList<Task> result = new ArrayList<Task>();
    for (int i = 0; i < GOKU.getAllTasks().size(); i++) {
      if (GOKU.getAllTasks().get(i).getTitle()
          .contains(command.getTask().getTitle())) {
        result.add(GOKU.getAllTasks().get(i));
      }
    }

    return new Result(true, null, null, result);
  }

}