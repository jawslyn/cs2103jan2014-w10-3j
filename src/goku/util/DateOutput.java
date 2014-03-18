package goku.util;

import hirondelle.date4j.DateTime;

import java.util.Date;

public class DateOutput {

  /*
   * @see format
   */
  public static String format(Date date) {
    return format(DateUtil.date4j(date));
  }

  /*
   * Formats a date into a human-friendly string.
   * 1 - date is the same day as current day: "X hours later"
   * 2 - date is less than 7 days away: "X days later"
   * 3 - all other dates "dd/MM"
   */
  public static String format(DateTime date) {
    DateTime now = DateUtil.getNow();
    int daysDifference = now.numDaysFrom(date);
    if (daysDifference == 0) {
      int hoursDifference = getDifferenceInHours(now, date);
      return hoursDifference + " hours later";
    } else if (daysDifference == 1) {
      return "tomorrow";
    } else if (daysDifference < 7) {
      return daysDifference + " days later";
    } else {
      return date.getDay() + "/" + date.getMonth();
    }
  }

  private static int getDifferenceInHours(DateTime now, DateTime date) {
    return date.getHour() - now.getHour();
  }
}
