//@author A0101232H
package goku;

import goku.storage.Storeable;
import goku.util.DateOutput;
import goku.util.DateUtil;
import goku.util.DiffMatchPath;
import hirondelle.date4j.DateTime;

import com.google.gson.Gson;

/**
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 * 
 */
public class Task implements Storeable, Comparable<Task> {

  private Integer id;
  private String title;
  private DateTime deadline;
  private DateRange period;
  private String[] tags;
  private String notes;
  private Boolean complete;
  private Boolean impt;

  public Task() {
  }

  /*
   * This constructor is used to clone a task
   */
  public Task(Task task) {
    id = task.id;
    title = task.title;
    deadline = task.deadline;
    period = task.period;
    tags = task.tags;
    notes = task.notes;
    complete = task.complete;
    impt = task.impt;
  }

  public boolean titleMatches(String title) {
    if (getTitle() == null || title == null) {
      return false;
    }
    String aTitle = getTitle().toLowerCase();
    String otherTitle = title.toLowerCase();
    int match = (new DiffMatchPath()).match_main(aTitle, otherTitle, 0);
    return match != -1;
  }

  public boolean titleMatchesExactly(String title) {
    if (getTitle() == null || title == null) {
      return false;
    }
    String aTitle = getTitle().toLowerCase();
    String otherTitle = title.toLowerCase();
    return aTitle.contains(otherTitle);
  }

  public void updateWith(Task other) {
    title = other.title == null ? title : other.title;
    deadline = other.deadline == null ? deadline : other.deadline;
    period = other.period == null ? period : other.period;
    complete = other.complete == null ? complete : other.complete;
    tags = other.tags == null ? tags : other.tags;
    notes = other.notes == null ? notes : other.notes;
    impt = other.impt == null ? impt : other.impt;
  }

  @Override
  public String toStorageFormat() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append(impt == null ? "" : (impt ? "(!) " : ""));
    sb.append("[" + id + "] ");

    if (title != null) {
      sb.append(title);
    }

    if (deadline != null) {
      sb.append(" | by ");
      sb.append(DateOutput.formatDateTimeDayMonthHourMin(deadline));
    }

    if (period != null) {
      sb.append(" | from ");
      sb.append(DateOutput.formatDateTimeDayMonthHourMin(period.getStartDate()));
      sb.append(" to ");
      sb.append(DateOutput.formatDateTimeDayMonthHourMin(period.getEndDate()));
    }

    return sb.toString();

  }

  @Override
  public boolean equals(Object anObject) {
    if (anObject == null) {
      return false;
    }
    if (anObject instanceof Task) {
      Task aTask = (Task) anObject;
      if (id == null || aTask.id == null) {
        return false;
      }
      return id.equals(aTask.id);
    }
    return false;
  }

  public Integer getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public DateTime getDeadline() {
    return deadline;
  }

  public DateRange getDateRange() {
    return period;
  }

  public DateTime getStartDate() {
    return period.getStartDate();
  }

  public DateTime getEndDate() {
    return period.getEndDate();
  }

  public Boolean getImpt() {
    return impt;
  }

  public Boolean getStatus() {
    return complete;
  }

  // @author A0099585Y
  public Boolean isDone() {
    return complete;
  }

  public boolean isDueOn(DateTime date) {
    return deadline != null && DateUtil.isEarlierOrOn(deadline, date);
  }

  public boolean isOn(DateTime date) {
    return deadline != null && DateUtil.isSameDay(deadline, date);
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setStatus(Boolean status) {
    if (status == null) {
      return;
    }
    complete = status;
  }

  public void setDeadline(DateTime deadline) {
    this.deadline = deadline;
  }

  public void setPeriod(DateRange period) {
    this.period = period;
  }

  public void setImpt(Boolean impt) {
    if (impt == null) {
      return;
    }
    this.impt = impt;
  }

  // @author A0096444X
  /*
   * Case 1: If impt, task is higher on the list
   * Case 2: Else by earliest start date or deadline
   */
  @Override
  public int compareTo(Task thatTask) {
    // Case 1:
    if (this.impt == true && thatTask.impt == false) {
      return -1;
    } else if (this.impt == false && thatTask.impt == true) {
      return 1;
    }

    // Case 2: compare by deadline or start date
    if (this.deadline == null && this.period == null) {
      if (thatTask.deadline != null || thatTask.period != null) {
        return 1;
      } else {
        return 0;
      }
    } else if (this.deadline != null || this.period != null) {
      if (thatTask.deadline == null && thatTask.period == null) {
        return -1;
      }
    }

    DateTime thisDate = null;
    DateTime thatDate = null;

    // get comparative date for this
    if (this.deadline != null) {
      assert this.period == null;
      thisDate = this.deadline;
    } else {
      assert this.period != null;
      thisDate = this.period.getStartDate();
    }

    // get comparative date for that
    if (thatTask.deadline != null) {
      assert thatTask.period == null;
      thatDate = thatTask.deadline;
    } else {
      assert thatTask.period != null;
      thatDate = thatTask.period.getStartDate();
    }

    // compare dates
    if (thisDate == null && thatDate == null) {
      return 0;
    } else if (thisDate != null && thatDate != null) {
      return thisDate.compareTo(thatDate);
    } else if (thisDate != null) {
      return -1;
    } else {
      return 1;
    }
  }
}
