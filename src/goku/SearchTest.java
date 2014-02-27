package goku;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

public class SearchTest {
  Search search = new Search();

  @Test
  public void searchTag_returnsTasksWithSameTags() throws Exception {
    GOKU.getAllTasks().clear();

    Task a = new Task();
    Task b = new Task();
    Task c = new Task();

    a.setTitle("hello");
    b.setTitle("byebye");
    c.setTitle("world");

    String[] aTags = { "this", "that" };
    String[] bTags = { "here", "there" };
    a.setTags(aTags);
    b.setTags(bTags);
    c.setTags(aTags);

    GOKU.getAllTasks().add(a);
    GOKU.getAllTasks().add(b);
    GOKU.getAllTasks().add(c);

    Result actual = search.searchTag(a);
    ArrayList<Task> resultList = new ArrayList<Task>();
    resultList.add(a);
    resultList.add(c);
    Result result = new Result(true, null, null, resultList);
    assertEquals(result.getSuccessMsg(), actual.getSuccessMsg());
    assertEquals(result.getErrorMsg(), actual.getErrorMsg());
    assertEquals(result.isSuccess(), actual.isSuccess());
    assertEquals(result.getTasks(), actual.getTasks());

  }

  @Test
  public void searchTitle_returnsTasksWithSameTitle() throws Exception {
    GOKU.getAllTasks().clear();

    Task a = new Task();
    Task b = new Task();
    Task c = new Task();

    a.setTitle("hello");
    b.setTitle("byebye");
    c.setTitle("hello world");

    GOKU.getAllTasks().add(a);
    GOKU.getAllTasks().add(b);
    GOKU.getAllTasks().add(c);

    Result actual = search.searchTitle(a);
    ArrayList<Task> resultList = new ArrayList<Task>();
    resultList.add(a);
    resultList.add(c);
    Result result = new Result(true, null, null, resultList);
    System.out.println(actual.getTasks());
    System.out.println(result.getTasks());
    assertEquals(result.getSuccessMsg(), actual.getSuccessMsg());
    assertEquals(result.getErrorMsg(), actual.getErrorMsg());
    assertEquals(result.isSuccess(), actual.isSuccess());
    assertEquals(result.getTasks(), actual.getTasks());

  }
}
