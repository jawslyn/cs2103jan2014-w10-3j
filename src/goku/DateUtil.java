package goku;

import hirondelle.date4j.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
  private static String[] weekdays = { null, "sunday", "monday", "tuesday",
      "wednesday", "thursday", "friday", "saturday" };
  private static String[] dayKeywords = { "tomorrow", "tml", "tmr", "today",
      "monday", "tuesday", "wednesday", "thursday", "friday", "saturday",
      "sunday" };
  private static String[] dayDelimiters = { "-", "/" };
  private static String[] timeDelimiters = { "pm", "am", ":", "." };

  public static DateTime date4j(Date date) {
    return DateTime.forInstant(date.getTime(), TimeZone.getDefault());
  }

  public static Date toDate(DateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return new Date(dateTime.getMilliseconds(TimeZone.getDefault()));
  }

  public static DateTime mergeDateAndTime(DateTime date, DateTime time) {
    if (date == null && time == null) {
      return null;
    }
    if (date == null) {
      date = getNow();
    }
    if (time == null) {
      time = DateTime.forTimeOnly(0, 0, 0, 0);
    }
    return new DateTime(date.getYear(), date.getMonth(), date.getDay(),
        time.getHour(), time.getMinute(), time.getSecond(),
        time.getNanoseconds());
  }

  public static DateTime getNow() {
    return DateTime.now(TimeZone.getDefault());
  }

  public static DateTime parse(final String[] inputs) {
    DateTime date = null, time = null;
    for (String input : inputs) {
      input.trim().toLowerCase();
      if (looksLikeDay(input) || looksLikeDate(input)) {
        date = parse(input);
      } else if (looksLikeTime(input)) {
        time = parse(input);
      }
    }
    return mergeDateAndTime(date, time);
  }

  public static DateTime parse(String string) {
    if (looksLikeDay(string)) {
      return parseDay(string);
    } else if (looksLikeDate(string)) {
      return parseDate(string);
    } else if (looksLikeTime(string)) {
      return parseTime(string);
    }
    return null;
  }

  public static boolean looksLikeDate(String candidate) {
    candidate = candidate.trim().toLowerCase();
    for (String delimiter : dayDelimiters) {
      if (candidate.contains(delimiter)) {
        return true;
      }
    }
    return false;
  }

  public static DateTime parseDate(String string) {
    String date[] = new String[0];
    if (string.contains("-")) {
      date = string.split("-");
    } else if (string.contains("/")) {
      date = string.split("/");
    }
    if (date.length == 2) {
      Integer yearNow = getNow().getYear();
      return DateTime.forDateOnly(yearNow, Integer.parseInt(date[1]),
          Integer.parseInt(date[0]));
    } else if (date.length == 3) {
      String possibleYear = date[2];
      if (possibleYear.length() == 2) {
        possibleYear = "20" + possibleYear;
      }

      return DateTime.forDateOnly(Integer.parseInt(possibleYear),
          Integer.parseInt(date[1]), Integer.parseInt(date[0]));
    }
    return null;
  }

  public static DateTime parseTime(String string) {
    if (string.contains("am")) {
      String time = string.substring(0, string.indexOf("am"));
      if (time.contains(".")) {
        String[] hourMinute = time.split("\\.");
        return DateTime.forTimeOnly(Integer.parseInt(hourMinute[0]),
            Integer.parseInt(hourMinute[1]), 0, 0);
      } else if (string.contains(":")) {
        String[] hourMinute = time.split(":");
        return DateTime.forTimeOnly(Integer.parseInt(hourMinute[0]),
            Integer.parseInt(hourMinute[1]), 0, 0);
      } else {
        return DateTime.forTimeOnly(Integer.parseInt(time), 0, 0, 0);
      }
    } else if (string.contains("pm")) {
      String time = string.substring(0, string.indexOf("pm"));
      if (time.contains(".")) {
        String[] hourMinute = time.split("\\.");
        return DateTime.forTimeOnly(Integer.parseInt(hourMinute[0]) + 12,
            Integer.parseInt(hourMinute[1]), 0, 0);
      } else if (time.contains(":")) {
        String[] hourMinute = time.split(":");
        return DateTime.forTimeOnly(Integer.parseInt(hourMinute[0]) + 12,
            Integer.parseInt(hourMinute[1]), 0, 0);
      } else {
        return DateTime.forTimeOnly(Integer.parseInt(time) + 12, 0, 0, 0);
      }
    } else {
      if (string.contains(".")) {
        String[] hourMinute = string.split("\\.");
        return DateTime.forTimeOnly(Integer.parseInt(hourMinute[0]),
            Integer.parseInt(hourMinute[1]), 0, 0);
      } else if (string.contains(":")) {
        String[] hourMinute = string.split(":");
        return DateTime.forTimeOnly(Integer.parseInt(hourMinute[0]),
            Integer.parseInt(hourMinute[1]), 0, 0);
      }
    }
    return null;
  }

  public static DateTime parseDay(String string) {
    DateTime now = getNow();
    switch (string) {
      case "today" :
        return now;
      case "tomorrow" :
        return now.plusDays(1);
      default :
        return getNearestDateToWeekday(string);
    }
  }

  public static Integer weekdayToInteger(String weekday) {
    for (int i = 0; i < weekdays.length; i++) {
      if (weekdays[i] != null && weekdays[i].contains(weekday)) {
        return i;
      }
    }
    return -1;
  }

  public static DateTime getNearestDateToWeekday(Integer targetWeekday) {
    return getNearestDateToWeekday(getNow(), targetWeekday);
  }

  public static DateTime getNearestDateToWeekday(DateTime baseDate,
      Integer targetWeekday) {
    Integer baseWeekday = baseDate.getWeekDay();
    Integer days = (targetWeekday - baseWeekday);
    if (days <= 0) {
      days += 7;
    }
    return baseDate.plusDays(days);
  }

  private static DateTime getNearestDateToWeekday(String weekday) {
    return getNearestDateToWeekday(weekdayToInteger(weekday));
  }

  public static boolean looksLikeTime(String candidate) {
    // TODO use regex?
    for (String delimiter : timeDelimiters) {
      if (candidate.contains(delimiter)) {
        return true;
      }
    }
    return false;
  }

  public static boolean looksLikeDay(String candidate) {
    candidate = candidate.trim().toLowerCase();
    for (String dayKeyword : dayKeywords) {
      if (dayKeyword.contains(candidate)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isEarlierThan(Date aDate, Date otherDate) {
    DateTime dt1 = date4j(aDate);
    DateTime dt2 = date4j(otherDate);
    return dt1.lt(dt2);
  }

  public static boolean isLaterThan(Date aDate, Date otherDate) {
    DateTime dt1 = date4j(aDate);
    DateTime dt2 = date4j(otherDate);
    return dt1.gt(dt2);
  }

  private static Calendar dateToCal(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal;
  }

  public static int[] getDayMonthYear(Date date) {
    Calendar now = dateToCal(date);
    int[] dmy = new int[3];
    dmy[0] = now.get(Calendar.DAY_OF_MONTH);
    dmy[1] = now.get(Calendar.MONTH);
    dmy[2] = now.get(Calendar.YEAR);
    return dmy;
  }

  /*
   * Calendar API wraps dates around. i.e. when day=40 month=8 it wraps around
   * to day=10 month=9. and month begins at 0. Might want to use a library
   * around this.
   */
  public static Date makeDate(int day, int month) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, day);
    cal.set(Calendar.MONTH, month);
    return cal.getTime();
  }

  public static Date makeDate(int day, int month, int year) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, day);
    cal.set(Calendar.MONTH, month);
    cal.set(Calendar.YEAR, year);
    return cal.getTime();
  }
}
