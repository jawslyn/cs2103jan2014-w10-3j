package goku;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class TaskTest {
  private Task task;
  private Task task1;
  private Task task2;
  private Task task3;
  private Task task4;
  private Task task5;
  private Task task6;
  private Task task7;
  
  private Gson gson;
  private String string;
  private String string1;
  private String[] arr;
  private String[] arr1;
  
  
  @Before
  public void setup() {
	  gson = new Gson();
	  
	  task = new Task();
	  task.setTitle("title");
	  task.setStatus(false);
	  
	  task1 = new Task();
	  task1.setTitle("title");
	  task1.setStatus(false);
	
	  task2 = new Task();
	  task2.setTags(arr);
	 
	  task3 = new Task();
	  task3.setTags(arr1);
	  
	  task4 = new Task();
	  task4.setNotes("notes");
	  
	  task5 = new Task();
	  task5.setNotes("notes");
	  
	  task6 = new Task();
	  task6.setId(10);
	  
	  task7 = new Task();
	  task7.setId(10);
  }
  
  @Test
  public void Gson_returnCorrectString() {
	  string = gson.toJson(task);
	  string1 = gson.toJson(task1);	  
	  assertEquals(string, string1);
	  
	  string = gson.toJson(task2);
	  string1 = gson.toJson(task3);	  
	  assertEquals(string, string1);
	  
	  string = gson.toJson(task4);
	  string1 = gson.toJson(task5);	  
	  assertEquals(string, string1);
	  
	  string = gson.toJson(task6);
	  string1 = gson.toJson(task7);	  
	  assertEquals(string, string1);
	  
	  task.setId(1);
	  task.setId(2);
	  
	  string = gson.toJson(task);
	  string1 = gson.toJson(task1);	  
	  assertNotSame(string, string1);
  }
}