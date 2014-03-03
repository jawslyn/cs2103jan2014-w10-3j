package goku.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import goku.GOKU;
import goku.action.Action;
import goku.action.AddAction;
import goku.action.DeleteAction;
import goku.action.DisplayAction;
import goku.action.EditAction;
import goku.action.NoAction;
import goku.action.SearchAction;

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

  @Test
  public void parse_AddAction() throws Exception {
    a = p.parse("");
    assertTrue(a instanceof NoAction);
    a = p.parse(null);
    assertTrue(a instanceof NoAction);
    a = p.parse("add");
    assertTrue(a instanceof NoAction);
    a = p.parse("add this is a title");
    assertTrue(a instanceof AddAction);
    AddAction aa;
    aa = (AddAction) a;
    assertEquals("this is a title", aa.getTitle());
    a = p.parse("add this is a title         ");
    assertTrue(a instanceof AddAction);
    aa = (AddAction) a;
    assertEquals("this is a title", aa.getTitle());

    a = p.parse("add this is a task by tomorrow    ");
    assertTrue(a instanceof AddAction);
    aa = (AddAction) a;
    assertEquals("this is a task", aa.getTitle());

    a = p.parse("add this is a task from today to tomorrow    ");
    assertTrue(a instanceof AddAction);
    aa = (AddAction) a;
    assertEquals("this is a task", aa.getTitle());
    assertEquals("today", aa.from);
    assertEquals("tomorrow", aa.to);

    a = p.parse("add this is a task from 3pm to 4pm    ");
    assertTrue(a instanceof AddAction);
    aa = (AddAction) a;
    assertEquals("this is a task", aa.getTitle());
    assertEquals("3pm", aa.from);
    assertEquals("4pm", aa.to);
  }

  @Test
  public void parse_DeleteAction() throws Exception {
    DeleteAction da;
    a = p.parse("delete 1");
    assertTrue(a instanceof DeleteAction);
    da = (DeleteAction) a;
    assertEquals(1, da.id);

    a = p.parse("delete 1 task");
    assertTrue(a instanceof DeleteAction);
    da = (DeleteAction) a;
    assertEquals("1 task", da.title);

    a = p.parse("delete abc");
    assertTrue(a instanceof DeleteAction);
    da = (DeleteAction) a;
    assertEquals("abc", da.title);
  }

  @Test
  public void parse_EditAction() throws Exception {
    EditAction ea;

    a = p.parse("edit");
    assertTrue(a instanceof NoAction);

    a = p.parse("edit 1");
    assertTrue(a instanceof NoAction);

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

    a = p.parse("edit a abc");
    assertTrue(a instanceof EditAction);
    ea = (EditAction) a;
    assertEquals(null, ea.title);
  }

  @Test
  public void parse_DisplayAction() throws Exception {
    DisplayAction disa;

    a = p.parse("display");
    assertTrue(a instanceof DisplayAction);
    disa = (DisplayAction) a;
  }

  @Test
  public void parse_SearchAction() throws Exception {
    SearchAction sa;

    a = p.parse("search");
    assertTrue(a instanceof NoAction);

    a = p.parse("search abc");
    assertTrue(a instanceof SearchAction);
    sa = (SearchAction) a;
    assertEquals("abc", sa.title);
  }

  @Test
  public void parse_ExitAction() throws Exception {
    ExitAction ea;

    a = p.parse("exit");
    assertTrue(a instanceof ExitAction);

    a = p.parse("quit");
    assertTrue(a instanceof ExitAction);

    a = p.parse("q");
    assertTrue(a instanceof ExitAction);
  }
}
