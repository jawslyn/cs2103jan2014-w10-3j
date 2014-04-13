//@author A0099585Y
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;
import goku.action.Action;
import goku.action.AddAction;
import goku.action.DeleteAction;
import goku.action.DisplayAction;
import goku.action.EditAction;
import goku.action.MakeActionException;
import goku.action.SearchAction;
import goku.action.UnknownAction;
import goku.ui.InputParser;
import goku.util.InvalidDateRangeException;

import org.junit.Before;
import org.junit.Test;

public class IntegrationTest {
  private static final EditAction EditAction = null;

  String userInput;
  InputParser parser;
  private GOKU goku;
  private TaskList list;

  @Before
  public void setup() {
    goku = new GOKU();
    list = goku.getTaskList();
    userInput = "";
    parser = new InputParser(goku);
  }

  /*
   * Integration test for user adding a task. This will test our parser, action,
   * task list
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
  // This tests whether adding a single task increases task list size
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
  // This tests whether adding an important task works
  public void user_addImportantTask() throws MakeActionException,
      InvalidDateRangeException {
    AddAction aa = (AddAction) parser.parse("add! important task");
    aa.doIt();
    assertTrue(list.getTaskById(1).isImpt());

    EditAction ea = (EditAction) parser.parse("edit! 1");
    ea.doIt();
    assertFalse(list.getTaskById(1).isImpt());
  }

  @Test
  // This tests whether adding multiple tasks works
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
  // This tests whether editing an existing task's title works
  public void user_addAndEditTasksTitle() throws Exception {
    AddAction aa = (AddAction) parser.parse("add original by today");
    aa.doIt();
    assertEquals(list.size(), 1);
    assertEquals(list.getTaskById(1).getTitle(), "original");

    EditAction ea = (EditAction) parser.parse("edit 1 edited");
    ea.doIt();
    assertEquals(list.getTaskById(1).getTitle(), "edited");

    assertNotNull(list.getTaskById(1));
    ea = (EditAction) parser.parse("done 1");
    ea.doIt();
  }

  @Test
  // This tests whether editing a task's deadline and period works
  public void user_EditTasksDeadlineAndPeriod() throws Exception {
    AddAction aa = (AddAction) parser.parse("add deadline by today");
    aa.doIt();

    SearchAction sa = (SearchAction) parser.parse("search by today");
    Result deadline = sa.doIt();
    assertEquals(deadline.getTasks().size(), 1);

    EditAction ea = (EditAction) parser.parse("edit 1 by tmr");
    ea.doIt();
    deadline = sa.doIt();
    assertNull(deadline.getTasks());

    ea = (EditAction) parser.parse("edit 1 by today");
    ea.doIt();
    deadline = sa.doIt();
    assertEquals(deadline.getTasks().size(), 1);

    ea = (EditAction) parser.parse("edit 1 remove deadline");
    ea.doIt();
    deadline = sa.doIt();
    assertNull(deadline.getTasks());

    DeleteAction da = (DeleteAction) parser.parse("delete 1");
    da.doIt();

    aa = (AddAction) parser.parse("add period from 1pm to 5pm");
    aa.doIt();

    sa = (SearchAction) parser.parse("search from 1pm to 5pm");
    Result period = sa.doIt();
    assertEquals(period.getTasks().size(), 1);

    ea = (EditAction) parser.parse("edit 1 from 6pm to 9pm");
    ea.doIt();
    period = sa.doIt();
    assertNull(period.getTasks());

    ea = (EditAction) parser.parse("edit 1 from 1pm to 5pm");
    ea.doIt();
    period = sa.doIt();
    assertEquals(period.getTasks().size(), 1);

    ea = (EditAction) parser.parse("edit 1 remove period");
    ea.doIt();
    period = sa.doIt();
    assertNull(deadline.getTasks());
  }

  @Test
  // This tests whether deleting tasks works
  public void user_deleteTasks() throws Exception {
    AddAction aa = (AddAction) parser.parse("add task one");
    aa.doIt();
    aa = (AddAction) parser.parse("add task two");
    aa.doIt();
    aa = (AddAction) parser.parse("add task three");
    aa.doIt();
    assertEquals(list.size(), 3);

    DeleteAction da = (DeleteAction) parser.parse("delete 1");
    da.doIt();
    assertEquals(list.size(), 2);

    da = (DeleteAction) parser.parse("delete 3");
    da.doIt();
    assertEquals(list.size(), 1);

    da = (DeleteAction) parser.parse("delete 2");
    da.doIt();
    assertEquals(list.size(), 0);

  }

  @Test
  // This tests whether viewing all/completed tasks works
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
  // This tests whether searching by title works
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

  @Test
  // This tests whether searching by deadline and period works
  public void user_searchDeadlineAndPeriod() throws Exception {
    AddAction aa = (AddAction) parser.parse("add deadline by today");
    aa.doIt();
    aa = (AddAction) parser.parse("add period from 1pm to 5pm");
    aa.doIt();
    aa = (AddAction) parser
        .parse("add deadline and period by tmr from tmr 8am to tmr 10am");
    aa.doIt();

    Result search;

    SearchAction sa = (SearchAction) parser.parse("search by today");
    search = sa.doIt();
    assertEquals(search.getTasks().size(), 1);

    sa = (SearchAction) parser.parse("search from 10am to 9pm");
    search = sa.doIt();
    assertEquals(search.getTasks().size(), 1);

    sa = (SearchAction) parser.parse("search by tmr ");
    search = sa.doIt();
    assertEquals(search.getTasks().size(), 2);

    sa = (SearchAction) parser.parse("search from tmr 8am to tmr 11pm");
    search = sa.doIt();
    assertEquals(search.getTasks().size(), 1);

    DeleteAction da = (DeleteAction) parser.parse("delete 1");
    da.doIt();
    sa = (SearchAction) parser.parse("search by today");
    search = sa.doIt();
    assertNull(search.getTasks());

    da = (DeleteAction) parser.parse("delete 2");
    da.doIt();
    sa = (SearchAction) parser.parse("search by tmr");
    search = sa.doIt();
    assertEquals(search.getTasks().size(), 1);

    da = (DeleteAction) parser.parse("delete 3");
    da.doIt();
    sa = (SearchAction) parser.parse("search by tmr");
    search = sa.doIt();
    assertNull(search.getTasks());
  }

  @Test
  // This tests whether an invalid add input fails
  public void user_InvalidCommand() throws Exception {
    UnknownAction ua = (UnknownAction) parser.parse("addd fail");
    Result fail = ua.doIt();
    assertNull(fail.getTasks());
  }

  // Invalid period detected while running this test
  // This tests whether adding a task with invalid period fails
  // public void user_addInvalidPeriod() throws MakeActionException {
  // AddAction aa = (AddAction) parser.parse("add fail from 9pm to 5pm") throw
  // InvalidDateRangeException,;
  // Result fail = aa.doIt();
  // assertNull(fail.getTasks());
  // }
}
