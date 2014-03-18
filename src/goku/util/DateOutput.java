package goku.util;

import hirondelle.date4j.DateTime;

import java.util.Date;

public class DateOutput {

  public static String format(Date date) {
    return format(DateUtil.date4j(date));
  }

  public static String format(DateTime date) {
    DateTime now = DateUtil.getNow();
    int daysDifference = now.numDaysFrom(date);
    if (daysDifference == 0) {
      int hoursDifference = getDifferenceInHours(now, date);
      return hoursDifference + " hours later";
    } else if (daysDifference == 1) {
      return date.getHour() + ":" + date.getMinute();
    } else if (daysDifference < 7) {
      if (daysDifference < 0) {
        return "expired";
      } else {
        return daysDifference + " days later";
      }
    } else {
      return date.getDay() + "/" + date.getMonth();
    }
  }

  private static int getDifferenceInHours(DateTime now, DateTime date) {
    return date.getHour() - now.getHour();

  }
}
