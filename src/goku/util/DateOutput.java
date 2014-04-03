package goku.util;

import hirondelle.date4j.DateTime;

import java.util.Locale;

/*
 * Formats dates into a more readable string
 * Mainly wrappers around DateTime.format.
 */
public class DateOutput {

  /*
   * converts DateTime -> "1.15pm"
   */
  public static String formatTimeOnly12h(DateTime dateTime) {
    String m = "";
    if (dateTime.getHour() == null) {
      return dateTime.format("h12.mm");
    } else {
      return dateTime.format("h12.mma", Locale.getDefault()).toLowerCase();
    }
  }

  /*
   * converts DateTime -> "1.15pm"
   */
  public static String formatTimeOnly12hIgnoreZeroMinutes(DateTime dateTime) {
    String time = formatTimeOnly12h(dateTime);
    if (time.substring(time.indexOf(".")).contains("00")) {
      return time.substring(0, time.indexOf("."))
          + time.substring(time.indexOf(".") + 3);
    }
    return time;
  }

  /*
   * converts DateTime -> "08:50h"
   */
  public static String formatTimeOnly24h(DateTime dateTime) {
    return dateTime.format("hh:mm|h|").toLowerCase();
  }

  /*
   * converts DateTime -> "3h later"
   */
  public static String formatTimeOnlyHoursLater(DateTime date) {
    long hoursDifference = getDifferenceInHours(DateUtil.getNow(), date);
    return hoursDifference + "h later";
  }

  /*
   * converts DateTime -> "4/10"
   */
  public static String formatDateOnlyDayMonth(DateTime dateTime) {
    return dateTime.format("D/M").toLowerCase();
  }

  /*
   * converts DateTime -> "today" or "4d later"
   */
  public static String formatDateOnlyDaysLater(DateTime date) {
    DateTime now = DateUtil.getNow();
    int daysDifference = now.numDaysFrom(date);
    // if negative?
    if (daysDifference == 0) {
      return "today";
    } else {
      return daysDifference + "d later";
    }
  }

  /*
   * converts DateTime -> "4/10 1.15pm"
   */
  public static String formatDateTimeDayMonthHourMin(DateTime date) {
    return formatDateOnlyDayMonth(date) + " " + formatTimeOnly12h(date);
  }

  /*
   * converts DateTime -> "4/10 3pm"
   */
  public static String formatDateTimeDayMonthHourMinIgnoreZeroMinutes(
      DateTime date) {
    return formatDateOnlyDayMonth(date) + " "
        + formatTimeOnly12hIgnoreZeroMinutes(date);
  }

  private static long getDifferenceInHours(DateTime now, DateTime date) {
    return (now.numSecondsFrom(date) / 60 / 60);
  }

}
