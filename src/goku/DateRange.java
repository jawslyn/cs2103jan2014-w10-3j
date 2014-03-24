package goku;

import goku.util.DateUtil;
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
  
  public boolean containsDate(DateTime date) {
    if (date == null) return false;
    return DateUtil.isEarlierOrOn(date, getEndDate())
        && DateUtil.isLaterOrOn(date, getStartDate());
  }
  
  public boolean intersectsWith(DateRange range) {
    if (range == null) return false;
    return containsDate(range.getStartDate()) || containsDate(range.getEndDate());
  }
}
