package goku;

import hirondelle.date4j.DateTime;

public class DateRange {
  DateTime startDate;
  DateTime endDate;

  public DateRange(DateTime startDate, DateTime endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public DateTime getStartDate() {
    return startDate;
  }

  public DateTime getEndDate() {
    return endDate;
  }

}
