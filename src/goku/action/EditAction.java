package goku.action;

import goku.DateRange;
import goku.GOKU;
import goku.Result;
import goku.Task;
import goku.TaskList;
import goku.util.DateOutput;
import hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
public class EditAction extends Action {
  public static final String ERR_INSUFFICIENT_ARGS = "Can't edit, need ID! Try \"edit 1 new title!\"";
  public static final String ERR_INSUFFICIENT_ARGS_FOR_COMPLETION = "Can't complete, need ID! Try \"do 1\"";
  private static final String MSG_HAS_OVERDUE = "[!] You have overdue tasks, \"view overdue\" to see them.";

  public static final String ERR_NO_ID_GIVEN = ERR_INSUFFICIENT_ARGS;

  private final String MSG_SUCCESS = "Edited task %d\n";

  public int id;

  public String title;
  public DateTime dline;
  public DateRange period;
  public Boolean isComplete;
  public boolean removeDeadline;
  public boolean removePeriod;
  public boolean toggleImportant;

  public EditAction(GOKU goku) {
    super(goku);
    // TODO Auto-generated constructor stub
  }

  public void addToUndoList() {
    TaskList currList = new TaskList();
    currList = list.clone();

    TaskList newCurrList = new TaskList();
    for (Task t : currList) {
      Task newT = new Task(t);
      newCurrList.addTaskWithoutSettingId(newT);
    }

    List<Integer> newIdList = new ArrayList<Integer>();
    for (Integer id : list.getUnusedId()) {
      newIdList.add(id);
    }

    Collections.sort(newIdList);
    newCurrList.setRunningId(list.getRunningId());
    newCurrList.setUnusedId(newIdList);

    goku.getUndoList().offer(newCurrList);
  }

  @Override
  public Result doIt() {
    addToUndoList();
    if (removeDeadline) {
      doRemoveDeadline();
    } else if (toggleImportant) {
      doToggleImportant();
    } else if (removePeriod) {
      doRemovePeriod();
    }
    return updateTask();
  }

  private void doRemoveDeadline() {
    Task t = list.getTaskById(id);
    t.setDeadline(null);
  }

  private void doToggleImportant() {
    Task t = list.getTaskById(id);
    t.setImpt(!t.getImpt());
  }

  private void doRemovePeriod() {
    Task t = list.getTaskById(id);
    t.setPeriod(null);
  }

  public String editMsgIfHaveOverdue(String msg) {
    if (list.getOverdue().size() != 0) {
      msg += System.lineSeparator() + MSG_HAS_OVERDUE;
    }
    return msg;
  }

  public Result updateTask() {
    Task taskWithEdits = new Task();
    taskWithEdits.setTitle(title);
    taskWithEdits.setDeadline(dline);
    taskWithEdits.setPeriod(period);
    taskWithEdits.setStatus(isComplete);
    Task t = list.getTaskById(id);

    String oldTitle = t.getTitle();

    t.updateWith(taskWithEdits);

    if (isComplete != null && isComplete) {
      list.editCompletedTaskById(id);
      t.setId(0);
    }

    // issue is is that when we are setting a new period
    // deadline is null, so the task has a deadline AND a period
    // and when displaying the deadline is checked first, so the task seems to
    // be not updated
    // but actually it just didn't remove the deadline
    if (dline != null) {
      t.setDeadline(dline);
      t.setPeriod(null);
    } else if (period != null) {
      t.setPeriod(period);
      t.setDeadline(null);
    }

    String successMsg = String.format(MSG_SUCCESS, id);
    String titleMsg = "Changed task's name \"%s\" to \"%s\"";
    String dlineMsg = "Changed task's deadline to \"by %s\"";
    String periodMsg = "Changed task's period to \"from %s to %s\"";
    String incompleteMsg = "Task \"%s\" is marked as incomplete";
    String completeMsg = "Task \"%s\" is marked as completed";
    int msgCount = 0;

    if (title != null) {
      successMsg += String.format(titleMsg, oldTitle, title);
      msgCount++;
    }
    if (dline != null) {
      if (msgCount != 0) {
        successMsg += "\n";
      }
      successMsg += String.format(dlineMsg,
          DateOutput.formatTimeOnly12hIgnoreZeroMinutes(dline));
      msgCount++;
    }
    if (period != null) {
      if (msgCount != 0) {
        successMsg += "\n";
      }
      successMsg += String.format(periodMsg,
          DateOutput.formatDateTimeDayMonthHourMinIgnoreZeroMinutes(period
              .getStartDate()), DateOutput
              .formatDateTimeDayMonthHourMinIgnoreZeroMinutes(period
                  .getEndDate()));
    }

    if (isComplete != null) {
      if (isComplete) {
        successMsg += String.format(completeMsg, t.getTitle());
      } else {
        successMsg += String.format(incompleteMsg, t.getTitle());
      }
    }

    return new Result(true, editMsgIfHaveOverdue(successMsg), null,
        list.getAllIncomplete());
  }

}
