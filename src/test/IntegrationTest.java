package test;


import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;
import goku.action.Action;
import goku.action.AddAction;
import goku.action.DisplayAction;
import goku.action.EditAction;
import goku.action.MakeActionException;
import goku.action.SearchAction;
import goku.ui.InputParser;
import goku.util.InvalidDateRangeException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IntegrationTest {
  private static final EditAction EditAction = null;
  private GOKU goku;
  private TaskList list;
  String userInput;
  InputParser parser;

  @Before
  public void setup() {
    goku = new GOKU();
    list = goku.getTaskList();
    userInput = "";
    parser = new InputParser(goku);
  }

  /*
   * Integration test for user adding a task.
   * This will test our parser, action, task list
   */

  /*
   * User adding a task without title
   */
  @Test(expected = MakeActionException.class)
  public void user_addTaskWithNoTitle_throwsMakeActionException()
      throws MakeActionException, InvalidDateRangeException {
    userInput = "add";
    parser.parse(userInput);
  }

  @Test
  //This tests whether adding a single task increases task list size
  public void user_addTaskWithTitle_increaseSizeOfList()
      throws MakeActionException, InvalidDateRangeException {
    Action a = parser.parse("add asdf");
    assertTrue(a instanceof AddAction);
    AddAction aa = (AddAction) a;
    assertEquals("asdf", aa.title);
    aa.doIt();
    assertEquals(list.size(), 1);
  }

  @Test
  //This tests whether adding multiple tasks works
  public void user_addMultipleTasks() throws Exception {
    AddAction aa = (AddAction) parser.parse("add task 1");
    aa.doIt();
    aa = (AddAction) parser.parse("add task 2 by 21/4");
    aa.doIt();
    aa = (AddAction) parser.parse("add task 3 from 22/4 to 28/4");
    aa.doIt();
    assertEquals(list.size(), 3);
    assertEquals(list.getTaskById(1).getTitle(), "task 1");
    Task taskTwo = list.getTaskById(2);
    assertEquals(taskTwo.getTitle(), "task 2");
    assertEquals(taskTwo.getDeadline().getDay(), new Integer(21));
    assertEquals(taskTwo.getDeadline().getMonth(), new Integer(4));

    Task taskThree = list.getTaskById(3);
    assertEquals(taskThree.getTitle(), "task 3");
    assertEquals(taskThree.getStartDate().getDay(), new Integer(22));
    assertEquals(taskThree.getStartDate().getMonth(), new Integer(4));
    assertEquals(taskThree.getEndDate().getDay(), new Integer(28));
    assertEquals(taskThree.getEndDate().getMonth(), new Integer(4));
  }
  
  @Test
  //This tests whether editing an existing task works 
  public void user_addAndEditTasks() throws Exception {
    AddAction aa = (AddAction) parser.parse("add original by today");
    aa.doIt();
    assertEquals(list.size(), 1);
    assertEquals(list.getTaskById(1).getTitle(), "original");
    
    EditAction ea = (EditAction) parser.parse("edit 1 edited");
    ea.doIt();
    assertEquals(list.getTaskById(1).getTitle(), "edited");
    
    assertNotNull(list.getTaskById(1)) ;
    ea = (EditAction) parser.parse("done 1");
    ea.doIt();
   // assertTrue(list.getTaskById(0).getStatus());

  }
  
  @Test
  //This tests whether viewing all/completed tasks  works
  public void user_addAndViewTasks() throws Exception {
    AddAction aa = (AddAction) parser.parse("add complete");
    aa.doIt();
    aa = (AddAction) parser.parse("add incomplete");
    aa.doIt();
    
    DisplayAction da = (DisplayAction) parser.parse("view");
    Result all = da.doIt();  
    
    assertEquals(all.getTasks().size(), 2);
    
    EditAction ea = (EditAction) parser.parse("do 1");
    ea.doIt();
    
    da = (DisplayAction) parser.parse("view");
    Result all2 = da.doIt();  
    da = (DisplayAction) parser.parse("view completed");
    Result completed = da.doIt();
    
    assertEquals(all2.getTasks().size(), 1);
    assertEquals(completed.getTasks().size(), 1);   
  }
  
  
  @Test
  //This tests whether searching for an existing task(s) works
  public void user_addAndSearchTasks() throws Exception {
    AddAction aa = (AddAction) parser.parse("add original by today");
    aa.doIt();
    assertEquals(list.size(), 1);
    assertEquals(list.getTaskById(1).getTitle(), "original");
    
    SearchAction sa = (SearchAction) parser.parse("search blah");
    Result zeroFound = sa.doIt();
    sa = (SearchAction) parser.parse("search origina");
    Result oneFound = sa.doIt();
    
    assertNull(zeroFound.getTasks());
    assertEquals(oneFound.getTasks().size(), 1);
  }
  
}
