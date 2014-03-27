package goku.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import goku.GOKU;
import goku.action.Action;
import goku.action.AddAction;
import goku.action.DeleteAction;
import goku.action.DisplayAction;
import goku.action.EditAction;
import goku.action.ExitAction;
import goku.action.MakeActionException;
import goku.action.NoAction;
import goku.action.SearchAction;
import goku.util.DateUtil;
import hirondelle.date4j.DateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest {
  InputParser p;
  Action a;

  @Before
  public void setUp() throws Exception {
    GOKU goku = new GOKU();
    p = new InputParser(goku);
  }

  @After
  public void tearDown() throws Exception {
    p = null;
    a = null;
  }

  @Test(expected = MakeActionException.class)
  public void parse_AddAction_throwsException() throws Exception {
    a = p.parse("");
    assertTrue(a instanceof NoAction);
    a = p.parse(null);
    assertTrue(a instanceof NoAction);
    a = p.parse("add");
    assertTrue(a instanceof NoAction);
  }

  @Test
  public void parse_AddAction() throws Exception {
    a = p.parse("add this is a title");
    assertTrue(a instanceof AddAction);
    AddAction aa;
    aa = (AddAction) a;
    assertEquals("this is a title", aa.getTitle());
    a = p.parse("add this is a title         ");
    assertTrue(a instanceof AddAction);
    aa = (AddAction) a;
    assertEquals("this is a title", aa.getTitle());

    DateTime now = DateUtil.getNow();
    a = p.parse("add this is a task by    tomorrow    ");
    assertTrue(a instanceof AddAction);
    aa = (AddAction) a;
    assertEquals("this is a task", aa.getTitle());
    DateTime later = aa.dline;
    assertTrue(later.gt(now));

    a = p.parse("add this is a task from today to tomorrow    ");
    assertTrue(a instanceof AddAction);
    aa = (AddAction) a;
    assertEquals("this is a task", aa.getTitle());
    assertNotNull(aa.period);

    a = p.parse("add this is a task from 3pm to 4pm    ");
    assertTrue(a instanceof AddAction);
    aa = (AddAction) a;
    assertEquals("this is a task", aa.getTitle());
    assertNotNull(aa.period);

    a = p.parse("add this is a task by 3pm tomorrow");
    assertTrue(a instanceof AddAction);
    aa = (AddAction) a;
    assertEquals("this is a task", aa.getTitle());
    DateTime due = aa.dline;
    assertEquals(new Integer(15), due.getHour());

    a = p.parse("add this is a task by 1.45pm   tomorrow");
    assertTrue(a instanceof AddAction);
    aa = (AddAction) a;
    assertEquals("this is a task", aa.getTitle());
    due = aa.dline;
    assertEquals(new Integer(13), due.getHour());
    assertEquals(new Integer(45), due.getMinute());

    a = p.parse("add this is a task by 1:45pm   tomorrow");
    assertTrue(a instanceof AddAction);
    aa = (AddAction) a;
    assertEquals("this is a task", aa.getTitle());
    due = aa.dline;
    assertEquals(new Integer(13), due.getHour());
    assertEquals(new Integer(45), due.getMinute());

    a = p.parse("add this is a task from     3pm 12/3     to 1pm 20/8/14");
    assertTrue(a instanceof AddAction);
    aa = (AddAction) a;
    assertEquals("this is a task", aa.getTitle());
    DateTime from = aa.period.getStartDate();
    DateTime to = aa.period.getEndDate();
    assertTrue(from.getHour() == 15);
    assertTrue(from.getDay() == 12);
    assertTrue(from.getMonth() == 3);
    assertTrue(to.getHour() == 13);
    assertTrue(to.getDay() == 20);
    assertTrue(to.getMonth() == 8);
    assertTrue(to.getYear() == 2014);
    assertNotNull(aa.period);
    // assertEquals(aa.from, "3pm 12/3");
    // assertEquals(aa.to, "1pm 20/8/14");
  }

  @Test(expected = MakeActionException.class)
  public void parse_DeleteAction_throwsException() throws Exception {
    a = p.parse("delete");
    assertTrue(a instanceof NoAction);
  }

  @Test
  public void parse_DeleteAction() throws Exception {
    DeleteAction da;

    a = p.parse("delete 1");
    assertTrue(a instanceof DeleteAction);
    da = (DeleteAction) a;
    assertEquals(new Integer(1), da.id);
    assertNull(da.title);

    a = p.parse("delete 1 task");
    assertTrue(a instanceof DeleteAction);
    da = (DeleteAction) a;
    assertEquals("1 task", da.title);

    a = p.parse("delete abc");
    assertTrue(a instanceof DeleteAction);
    da = (DeleteAction) a;
    assertEquals("abc", da.title);
  }

  @Test(expected = MakeActionException.class)
  public void parse_EditAction_throwsException() throws Exception {
    a = p.parse("edit");
    assertTrue(a instanceof NoAction);

    a = p.parse("edit 1");
    assertTrue(a instanceof NoAction);
  }

  @Test
  public void parse_EditAction() throws Exception {
    EditAction ea;

    a = p.parse("edit 1 abc");
    assertTrue(a instanceof EditAction);
    ea = (EditAction) a;
    assertEquals("abc", ea.title);
    assertEquals(1, ea.id);

    a = p.parse("edit 1 123");
    assertTrue(a instanceof EditAction);
    ea = (EditAction) a;
    assertEquals("123", ea.title);
    assertEquals(1, ea.id);
    assertNull(ea.isComplete);
    assertNull(ea.dline);
    assertNull(ea.period);

    a = p.parse("edit 1 from 3pm to 6pm");
    assertTrue(a instanceof EditAction);
    ea = (EditAction) a;
    assertEquals(1, ea.id);
    assertNull(ea.isComplete);
    assertNull(ea.title);
    assertNull(ea.dline);
    assertNotNull(ea.period);

    a = p.parse("edit a abc");
    assertTrue(a instanceof NoAction);

    a = p.parse("do 1 abc");
    assertTrue(a instanceof EditAction);
    ea = (EditAction) a;
    assertEquals(1, ea.id);
    assertTrue(ea.isComplete);
    assertNull(ea.dline);
    assertNull(ea.period);

  }

  @Test
  public void parse_DisplayAction() throws Exception {
    a = p.parse("display");
    assertTrue(a instanceof DisplayAction);
  }

  @Test(expected = MakeActionException.class)
  public void parse_SearchAction_throwsException() throws Exception {
    a = p.parse("search");
    assertTrue(a instanceof NoAction);
  }

  @Test
  public void parse_SearchAction() throws Exception {
    SearchAction sa;

    a = p.parse("search abc");
    assertTrue(a instanceof SearchAction);
    sa = (SearchAction) a;
    assertEquals("abc", sa.title);

    a = p.parse("search from today to tomorrow");
    assertTrue(a instanceof SearchAction);
    sa = (SearchAction) a;
    assertNull(sa.title);
    assertNotNull(sa.period);

    a = p.parse("search by tomorrow");
    assertTrue(a instanceof SearchAction);
    sa = (SearchAction) a;
    assertNull(sa.title);
    assertNotNull(sa.dline);

    a = p.parse("search abc by tomorrow");
    assertTrue(a instanceof SearchAction);
    sa = (SearchAction) a;
    assertNotNull(sa.title);
    assertNotNull(sa.dline);
  }

  @Test(expected = MakeActionException.class)
  public void parse_SearchAction_CheckFree_throwsException() throws Exception {
    a = p.parse("free");
    assertTrue(a instanceof NoAction);
  }

  /*
   * SearchAction - Check Free Feature (multiple inputs all combinations)
   * At the parser level, search action is created first then the testFree parameter
   * is flagged true if "free" is the command word detected. The next two test cases
   * checks that the flagging is working properly. The next two test cases ensures
   * that the dates should only be parsed if they are valid. If it's not valid, the
   * action should not go through. There are only a total of 4 possible scenarios.
   */
  @Test
  public void parse_SearchAction_CheckFreeIsTrue() throws Exception {
    a = p.parse("free today");
    assertTrue(a instanceof SearchAction);

    SearchAction sa = (SearchAction) a;
    assertTrue(sa.testFree);
  }

  @Test
  public void parse_SearchAction_CheckFreeIsFalse() throws Exception {
    a = p.parse("search today");
    assertTrue(a instanceof SearchAction);

    SearchAction sa = (SearchAction) a;
    assertFalse(sa.testFree);
  }

  @Test
  public void parse_SearchAction_CheckFreeDateQueryValid() throws Exception {
    a = p.parse("free tmr 10am");
    assertTrue(a instanceof SearchAction);

    SearchAction sa = (SearchAction) a;
    assertNotNull(sa.dateQuery);
  }

  @Test(expected = MakeActionException.class)
  public void parse_SearchAction_CheckFreeDateQueryInvalid() throws Exception {
    a = p.parse("free meh");
    assertTrue(a instanceof NoAction);
  }

  @Test
  public void parse_ExitAction() throws Exception {
    a = p.parse("exit");
    assertTrue(a instanceof ExitAction);
    a = p.parse("quit");
    assertTrue(a instanceof ExitAction);
    a = p.parse("q");
    assertTrue(a instanceof ExitAction);
  }
}
