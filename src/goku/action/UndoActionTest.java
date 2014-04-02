package goku.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;
import goku.util.DateUtil;
import hirondelle.date4j.DateTime;

import java.util.Deque;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UndoActionTest {
  GOKU goku;
  TaskList list;
  Deque<TaskList> undoList;

  @After
  public void cleanUp() {
    list.clear();
  }

  @Test
  public void doIt_UndoAddAction() throws Exception {
    AddAction add1 = new AddAction(goku);
    add1.title = "hi abc";
    AddAction add2 = new AddAction(goku);
    add2.title = "hi abcdef";
    AddAction add3 = new AddAction(goku);
    add3.title = "hi def";
    UndoAction undo = new UndoAction(goku);

    Result result = undo.doIt();
    assertEquals("Failed to undo.", result.getErrorMsg());

    add1.doIt();
    assertEquals(1, undoList.size());
    add2.doIt();
    assertEquals(2, undoList.size());
    add3.doIt();
    assertEquals(3, undoList.size());
    undo.doIt();
    assertEquals(2, undoList.size());

    assertEquals(2, goku.getTaskList().size());

    assertEquals("hi abc", goku.getTaskList().getTaskById(1).getTitle());
    assertEquals("hi abcdef", goku.getTaskList().getTaskById(2).getTitle());

  }

  @Test
  public void doIt_UndoDeleteAction() throws Exception {
    AddAction add1 = new AddAction(goku);
    add1.title = "hi abc";
    AddAction add2 = new AddAction(goku);
    add2.title = "hi abcdef";
    AddAction add3 = new AddAction(goku);
    add3.title = "hi def";
    DeleteAction delete = new DeleteAction(goku);
    delete.id = 3;
    UndoAction undo = new UndoAction(goku);

    Result result = undo.doIt();
    assertEquals("Failed to undo.", result.getErrorMsg());

    add1.doIt();
    assertEquals(1, undoList.size());
    add2.doIt();
    assertEquals(2, undoList.size());
    add3.doIt();
    assertEquals(3, undoList.size());
    delete.doIt();
    assertEquals(4, undoList.size());
    assertEquals(2, goku.getTaskList().size());
    undo.doIt();
    assertEquals(3, goku.getTaskList().size());

    assertEquals("hi abc", goku.getTaskList().getTaskById(1).getTitle());
    assertEquals("hi abcdef", goku.getTaskList().getTaskById(2).getTitle());
    assertEquals("hi def", goku.getTaskList().getTaskById(3).getTitle());
  }

  @Test
  public void doIt_UndoEditAction() throws Exception {
    AddAction add1 = new AddAction(goku);
    add1.title = "hi abc";
    DateTime prevDeadline = DateUtil.parse("monday");
    add1.dline = prevDeadline;
    EditAction editTitle = new EditAction(goku);
    editTitle.id = 1;
    editTitle.title = "bye abc";
    UndoAction undo = new UndoAction(goku);

    Result result = undo.doIt();
    assertEquals("Failed to undo.", result.getErrorMsg());

    add1.doIt();
    assertEquals(1, undoList.size());
    assertEquals(1, list.size());
    editTitle.doIt();
    assertEquals(2, undoList.size());
    Task task1 = goku.getTaskList().getTaskById(1);
    assertEquals("bye abc", task1.getTitle());
    undo.doIt();
    assertEquals(1, undoList.size());
    Task task2 = goku.getTaskList().getTaskById(1);
    assertEquals("hi abc", task2.getTitle());

    EditAction editDate = new EditAction(goku);
    editDate.id = 1;
    DateTime newDeadline = DateUtil.parse("tuesday");
    editDate.dline = newDeadline;
    assertNotNull(editDate.dline);
    editDate.doIt();
    task1 = goku.getTaskList().getTaskById(1);
    assertEquals(newDeadline, task1.getDeadline());
    undo.doIt();
    task2 = goku.getTaskList().getTaskById(1);
    assertEquals(prevDeadline, task2.getDeadline());

  }

  @Before
  public void setup() {
    goku = new GOKU();
    list = goku.getTaskList();
    undoList = goku.getUndoList();
  }
}
