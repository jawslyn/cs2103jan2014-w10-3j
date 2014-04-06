package goku;

import goku.util.DateOutput;
import goku.util.DateUtil;
import goku.util.InvalidDateRangeException;
import hirondelle.date4j.DateTime;

public class DateRange {
  DateTime startDate;
  DateTime endDate;
  
  private static final String ERR_INVALID_PERIOD = "Invalid period detected!";
  
  public DateRange(DateTime startDate, DateTime endDate) throws InvalidDateRangeException{
    this.startDate = startDate;
    this.endDate = endDate;
    
    if(DateUtil.isEarlierOrOn(endDate, startDate)) {
      throw new InvalidDateRangeException(ERR_INVALID_PERIOD);
    }
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
  
  @Override
  public String toString() {
    return DateOutput.formatDateTimeDayMonthHourMin(startDate)+" to "+
        DateOutput.formatDateTimeDayMonthHourMin(endDate);
  }
}
