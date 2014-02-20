package goku;

import java.util.Date;

/*
 * Task is the core of GOKU. GOKU is designed to keep track of tasks, which are
 * analogous to real life tasks which the user wishes to note down.
 */
public class Task {
  enum Importance {
    LOW, MEDIUM, HIGH
  }

  enum Status {
    INCOMPLETE, COMPLETED
  }

  private static Integer count = 0;
  private Integer id;
  private String title;
  private Date deadline;
  private Date startDate;
  private Date endDate;
  private String[] tags;
  private String notes;
  private Importance importance;
  private Status status;

  public Task() {
    this.id = ++count;
  }

  public Task(Task task) {
    id = task.id;
    title = task.title;
    deadline = task.deadline;
    startDate = task.startDate;
    endDate = task.endDate;
    tags = task.tags;
    notes = task.notes;
    importance = task.importance;
    status = task.status;
  }

  @Override
  public String toString() {
    return String.valueOf(id);
  }

  public Integer getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getDeadline() {
    return deadline;
  }

  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String[] getTags() {
    return tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Importance getImportance() {
    return importance;
  }

  public void setImportance(Importance importance) {
    this.importance = importance;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
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

}
