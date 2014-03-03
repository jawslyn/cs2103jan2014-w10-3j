package goku;

import java.util.Date;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */

public class Task {

  enum Importance {
    HIGH, MEDIUM, LOW
  }

  private static Integer count = 0;
  private Integer id;
  private String title;
  private Date deadline;
  private DateRange period;
  private String[] tags;
  private String notes;
  private Importance importance;
  private boolean isComplete;

  public Task() {
    this.id = ++count;
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
    importance = task.importance;
    isComplete = false;
  }

  /*
   * A Task MUST have a title, else it will not be stored.
   */
  public boolean isValid() {
    return (this.title != null);
  }

  public boolean titleMatches(Task otherTask) {
    if (getTitle() == null || otherTask.getTitle() == null) {
      return false;
    }
    String aTitle = getTitle().toLowerCase();
    String otherTitle = otherTask.getTitle().toLowerCase();
    return aTitle.contains(otherTitle);
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

  public boolean isDueBefore(Date date) {
    return DateUtil.dateTimeIsEarlierThanOrSameAs(deadline, date);
  }

  public boolean isDueBefore(Task task) {
    return DateUtil.dateTimeIsEarlierThanOrSameAs(deadline, task.getDeadline());
  }

  public boolean inPeriod(Date date) {
    return DateUtil.dateTimeIsEarlierThan(date, getEndDate())
        && DateUtil.dateTimeIsLaterThan(date, getStartDate());
  }

  public void updateWith(Task otherTask) {
    title = otherTask.title == null ? title : otherTask.title;
    deadline = otherTask.deadline == null ? deadline : otherTask.deadline;
    period = otherTask.period == null ? period : otherTask.period;
    tags = otherTask.tags == null ? tags : otherTask.tags;
    notes = otherTask.notes == null ? notes : otherTask.notes;
    importance = otherTask.importance == null ? importance
        : otherTask.importance;
  }

  @Override
  public boolean equals(Object anObject) {
    if (anObject == null) {
      return false;
    }
    if (anObject instanceof Task) {
      Task aTask = (Task) anObject;
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

  public Importance getImportance() {
    return importance;
  }

  public String getNotes() {
    return notes;
  }

  public Date getStartDate() {
    return period.getStartDate();
  }

  public boolean getStatus() {
    return isComplete;
  }

  public String[] getTags() {
    return tags;
  }

  public String getTitle() {
    return title;
  }

  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

  public void setImportance(Importance importance) {
    this.importance = importance;
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

  @Override
  public String toString() {
    return String.valueOf(id) + title;
  }

  public String toStorageFormat() {
    return toString();
  }
}
