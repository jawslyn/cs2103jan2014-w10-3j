package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import goku.GOKU;
import goku.Task;
import goku.TaskList;
import goku.action.Action;
import goku.action.AddAction;
import goku.action.MakeActionException;
import goku.ui.InputParser;

import org.junit.Before;
import org.junit.Test;

public class IntegrationTest {
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
   * This will test our parser, action, tasklist
   */

  /*
   * User adding a task without title
   */
  @Test(expected = MakeActionException.class)
  public void user_addTaskWithNoTitle_throwsMakeActionException()
      throws MakeActionException {
    userInput = "add";
    parser.parse(userInput);
  }

  @Test
  public void user_addTaskWithTitle_increaseSizeOfList()
      throws MakeActionException {
    Action a = parser.parse("add asdf");
    assertTrue(a instanceof AddAction);
    AddAction aa = (AddAction) a;
    assertEquals("asdf", aa.title);
    aa.doIt();
    assertEquals(list.size(), 1);
  }

  @Test
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
}
