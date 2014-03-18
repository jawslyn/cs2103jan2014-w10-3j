package goku;

import goku.util.DateUtil;

import java.util.Date;

import com.google.gson.Gson;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */

public class Task {

  private Integer id;
  private String title;
  private Date deadline;
  private DateRange period;
  private String[] tags;
  private String notes;
  private Boolean isComplete;
  private Boolean isImpt;

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
    isComplete = false;
    isImpt = task.isImpt;
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

  public DateRange getDateRange() {
    return period;
  }

  public Date getDeadline() {
    return deadline;
  }

  public Date getEndDate() {
    return period.getEndDate();
  }

  public Integer getId() {
    return id;
  }

  public Boolean getImpt() {
    return isImpt;
  }

  public String getNotes() {
    return notes;
  }

  public Date getStartDate() {
    return period.getStartDate();
  }

  public Boolean getStatus() {
    return isComplete;
  }

  public String[] getTags() {
    return tags;
  }

  public String getTitle() {
    return title;
  }

  public boolean inPeriod(Date date) {
    return DateUtil.isEarlierThan(date, getEndDate())
        && DateUtil.isLaterThan(date, getStartDate());
  }

  public Boolean isDone() {
    return isComplete;
  }

  public boolean isDueBefore(Date date) {
    return DateUtil.isEarlierThan(deadline, date);
  }

  public boolean isDueBefore(Task task) {
    return DateUtil.isEarlierThan(deadline, task.getDeadline());
  }

  public boolean isDueOn(Date date) {
    return DateUtil.isEarlierOrOn(deadline, date);
  }

  public boolean isDueOn(Task task) {
    return DateUtil.isEarlierOrOn(deadline, task.getDeadline());
  }

  public void setComplete(Boolean complete) {
    this.isComplete = complete;
  }

  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setImpt(Boolean impt) {
    this.isImpt = impt;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setPeriod(Date startDate, Date endDate) {
    setPeriod(new DateRange(startDate, endDate));
  }

  public void setPeriod(DateRange period) {
    this.period = period;
  }

  public void setStatus(boolean status) {
    isComplete = status;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean tagsMatch(Task otherTask) {
    String[] otherTags = otherTask.getTags();
    for (String otherTag : otherTags) {
      for (String tag : tags) {
        if (otherTag.equalsIgnoreCase(tag)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean titleMatches(Task otherTask) {
    if (getTitle() == null || otherTask.getTitle() == null) {
      return false;
    }
    String aTitle = getTitle().toLowerCase();
    String otherTitle = otherTask.getTitle().toLowerCase();
    return aTitle.contains(otherTitle);
  }

  public String toStorageFormat() {
    Gson gson = new Gson();

    return gson.toJson(this);
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("ID: ");
    sb.append(id);
    sb.append(" | Title: ");
    sb.append(title);

    if (deadline != null) {
      sb.append(" | Deadline: ");
      sb.append(deadline.toString().substring(0, 16));
    }

    if (period != null) {
      sb.append(" | From: ");
      sb.append(period.getStartDate());
      sb.append(" To: ");
      sb.append(period.getEndDate());
    }

    sb.append(" | Status: ");
    if (isComplete != null && isComplete) {
      sb.append("done");
    } else {
      sb.append("not done");
    }

    sb.append(" | Importance: ");
    if (isImpt != null && isImpt) {
      sb.append("HIGH");
    } else {
      sb.append("NORMAL");
    }

    return sb.toString();

  }

  public void updateWith(Task otherTask) {
    title = otherTask.title == null ? title : otherTask.title;
    deadline = otherTask.deadline == null ? deadline : otherTask.deadline;
    period = otherTask.period == null ? period : otherTask.period;
    isComplete = otherTask.isComplete == null ? isComplete
        : otherTask.isComplete;
    tags = otherTask.tags == null ? tags : otherTask.tags;
    notes = otherTask.notes == null ? notes : otherTask.notes;
    isImpt = otherTask.isImpt == null ? isImpt : otherTask.isImpt;
  }
}
