package goku;

import java.util.Date;

public class DateRange {
  Date startDate;
  Date endDate;

  public DateRange(Date startDate, Date endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

}
