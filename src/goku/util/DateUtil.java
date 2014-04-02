package goku.util;

import hirondelle.date4j.DateTime;

import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
  private static String[] weekdays = { null, "sunday", "monday", "tuesday",
      "wednesday", "thursday", "friday", "saturday" };
  private static String[] dayKeywords = { "tomorrow", "tml", "tmr", "today",
      "monday", "tuesday", "wednesday", "thursday", "friday", "saturday",
      "sunday" };
  private static String[] dateDelimiters = { "-", "/" };
  private static String[] timeDelimiters = { "pm", "am", ":", "." };
  private static String[] weekOffsets = { "coming", "next" };

  /*
   * Converts a {@link java.util.Date} into {@ DateTime}
   */
  public static DateTime date4j(Date date) {
    return DateTime.forInstant(date.getTime(), TimeZone.getDefault());
  }

  /*
   * Given a starting date, finds the nearest weekday from this date that
   * matches the target weekday
   */
  public static DateTime getNearestDateToWeekday(DateTime baseDate,
      Integer targetWeekday) {
    Integer baseWeekday = baseDate.getWeekDay();
    Integer days = (targetWeekday - baseWeekday);
    if (days <= 0) {
      days += 7;
    }

    baseDate = baseDate.plusDays(days);
    baseDate = baseDate.getEndOfDay().truncate(DateTime.Unit.SECOND);

    return baseDate;
  }

  /*
   * Gets the nearest weekday which matches the target weekday from current date
   * 
   * @see #getNearestDateToWeekday(DateTime baseDate, Integer targetWeekday)
   */
  public static DateTime getNearestDateToWeekday(Integer targetWeekday) {
    return getNearestDateToWeekday(getNow(), targetWeekday);
  }

  private static DateTime getNearestDateToWeekday(String weekday) {
    return getNearestDateToWeekday(weekdayToInteger(weekday));
  }

  /*
   * Returns the current date and time
   */
  public static DateTime getNow() {
    return DateTime.now(TimeZone.getDefault());
  }

  /*
   * Returns the current date (day) only
   */
  public static DateTime getNowDate() {
    return DateTime.today(TimeZone.getDefault());
  }

  public static boolean isEarlierOrOn(DateTime aDate, DateTime otherDate) {
    if (aDate == null || otherDate == null) {
      return false;
    }

    // no time indicated => compare only by date
    if (aDate.getHour() == null || otherDate.getHour() == null) {
      return isEarlierThanOrOnByDate(aDate, otherDate);
    }
    return aDate.lteq(otherDate);
  }

  public static boolean isEarlierThan(DateTime aDate, DateTime otherDate) {
    if (aDate == null || otherDate == null) {
      return false;
    }

    // no time indicated => compare only by date
    if (aDate.getHour() == null || otherDate.getHour() == null) {
      return isEarlierThanByDate(aDate, otherDate);
    }
    return aDate.lt(otherDate);
  }

  public static boolean isEarlierThanByDate(DateTime aDate, DateTime otherDate) {
    assert otherDate != null;
    if (aDate == null) {
      return false;
    }

    DateTime aDateOnly = DateTime.forDateOnly(aDate.getYear(),
        aDate.getMonth(), aDate.getDay());
    DateTime otherDateOnly = DateTime.forDateOnly(otherDate.getYear(),
        otherDate.getMonth(), otherDate.getDay());

    return aDateOnly.lt(otherDateOnly);
  }

  public static boolean isEarlierThanOrOnByDate(DateTime aDate,
      DateTime otherDate) {
    assert otherDate != null;
    if (aDate == null) {
      return false;
    }

    DateTime aDateOnly = DateTime.forDateOnly(aDate.getYear(),
        aDate.getMonth(), aDate.getDay());
    DateTime otherDateOnly = DateTime.forDateOnly(otherDate.getYear(),
        otherDate.getMonth(), otherDate.getDay());

    return aDateOnly.lteq(otherDateOnly);
  }

  public static boolean isLaterOrOn(DateTime aDate, DateTime otherDate) {
    assert otherDate != null;
    if (aDate == null) {
      return false;
    }
    return aDate.gteq(otherDate);
  }

  public static boolean isLaterThan(DateTime aDate, DateTime otherDate) {
    assert otherDate != null;
    if (aDate == null) {
      return false;
    }
    return aDate.gt(otherDate);
  }

  public static boolean isLaterThanByDate(DateTime aDate, DateTime otherDate) {
    assert otherDate != null;
    if (aDate == null) {
      return false;
    }

    DateTime aDateOnly = DateTime.forDateOnly(aDate.getYear(),
        aDate.getMonth(), aDate.getDay());
    DateTime otherDateOnly = DateTime.forDateOnly(otherDate.getYear(),
        otherDate.getMonth(), otherDate.getDay());

    return aDateOnly.gt(otherDateOnly);
  }

  public static boolean isLaterThanOrOnByDate(DateTime aDate, DateTime otherDate) {
    assert otherDate != null;
    if (aDate == null) {
      return false;
    }

    DateTime aDateOnly = DateTime.forDateOnly(aDate.getYear(),
        aDate.getMonth(), aDate.getDay());
    DateTime otherDateOnly = DateTime.forDateOnly(otherDate.getYear(),
        otherDate.getMonth(), otherDate.getDay());

    return aDateOnly.gteq(otherDateOnly);
  }

  static boolean isOffsetWord(String candidate) {
    candidate = candidate.trim().toLowerCase();
    for (String offsetWord : weekOffsets) {
      if (offsetWord.contains(candidate)) {
        return true;
      }
    }
    return false;
  }

  /*
   * A string looks like a date if it has a "-" or "/". Examples are "3/4",
   * "3/4/12", "3-4", "3-4-12"
   */
  public static boolean looksLikeDate(String candidate) {
    for (String delimiter : dateDelimiters) {
      if (candidate.contains(delimiter)) {
        return true;
      }
    }
    return false;
  }

  /*
   * A candidate looks like a day when it matches: 1) today, tomorrow, tml, tmr
   * 2) names of weekdays, spelled in full for short
   */
  public static boolean looksLikeDay(String candidate) {
    candidate = candidate.trim().toLowerCase();
    for (String dayKeyword : dayKeywords) {
      if (dayKeyword.contains(candidate)) {
        return true;
      }
    }
    return false;
  }

  /*
   * A candidate looks like a time when it has ".", ":", "am" or "pm". Examples
   * are: "1.45", "1.45pm", "1:45", "1:45am"
   */
  public static boolean looksLikeTime(String candidate) {
    // TODO use regex?
    for (String delimiter : timeDelimiters) {
      if (candidate.contains(delimiter)) {
        return true;
      }
    }
    return false;
  }

  /*
   * Merge the date component of date with the time component of time.
   * 
   * 
   * @param date a DateTime where only the date matters
   * 
   * @param date a DateTime where only the time matters
   * 
   * @return null when both params are null, or when only one is null, it tries
   * to merge intelligently, using the current date or midnight time.
   */
  public static DateTime mergeDateAndTime(DateTime date, DateTime time,
      Integer offsetDays) {
    if (date == null && time == null) {
      return null;
    }
    DateTime result = null;

    if (date == null) {
      date = getNow();
      result = new DateTime(date.getYear(), date.getMonth(), date.getDay(),
          time.getHour(), time.getMinute(), time.getSecond(), null);
    } else if (time == null) {
      result = date;
    } else {
      result = new DateTime(date.getYear(), date.getMonth(), date.getDay(),
          time.getHour(), time.getMinute(), time.getSecond(), null);
    }

    return result.plusDays(offsetDays);
  }

  public static DateTime parse(String string) {
    String[] s = string.split(" ");
    return parse(s);
  }

  /*
   * Parses and array of String into a DateTime. It looks at each element and
   * decides if it looks like a date or a time, It parses according to the rules
   * described above, returning the appropriate date or time component, in a
   * DateTime. All the information is then merged into a final DateTime.
   * 
   * pre: inputs contain Strings which are non-empty and trimmed
   * 
   * @return null when parsing fails, i.e. no date or time can be interpreted
   * from the inputs.
   * 
   * Caution: For each component, day, date, time, once found, the rest of the input
   * will NOT be tested for possibility of that component. Test priority in order of
   * offset, day > date, then time.
   */
  public static DateTime parse(final String[] inputs) {
    DateTime date = null, time = null;
    boolean offsetFound = false, dateFound = false, timeFound = false;

    int daysOffsets = 0;
    try {
      for (String input : inputs) {
        if (isOffsetWord(input) && offsetFound == false) {
          daysOffsets = parseOffset(input);
          offsetFound = true;
        } else if (looksLikeDay(input) && dateFound == false) {
          date = parseDay(input);
          dateFound = true;
        } else if (looksLikeDate(input) && !dateFound) {
          date = parseDate(input);
          dateFound = date == null ? false : true;
        } else if (!timeFound) {
          time = parseTime(input);
          timeFound = time == null ? false : true;
        }

        // stop searching if all components found
        if (offsetFound == true && dateFound == true && timeFound == true) {
          break;
        }
      }
    } catch (Exception e) {
      // this catches an exception thrown by DateTime's parse methods.
      // when there is a parsing error, e.g. time given is 123.45pm,
      // an exception is thrown and we treat it as no time was given
      // TODO return null;
    }
    return mergeDateAndTime(date, time, daysOffsets);
  }

  /*
   * Parses a string into a DateTime where only the date matters. We can handled
   * cases: 1) Day and Month specified, 2) Day and Month and Year specified
   */
  // dd/mm or dd/mm/yy

  public static DateTime parseDate(String string) {
    if (string.matches("(([02]?[0-9])|(3[01]))[/-]([1-9]?|1[0-2])[/-]?\\d*")) {
      String date[] = string.split("[-/]");
      Integer day, month, year;
      day = Integer.parseInt(date[0]);
      month = Integer.parseInt(date[1]);
      year = date.length > 2 ? Integer.parseInt(date[2]) + 2000 : getNow()
          .getYear();

      return DateTime.forDateOnly(year, month, day);
    } else {
      return null;
    }
  }

  /*
   * Parses a string into a DateTime where only the date matters It handles
   * keywords like today, tomorrow, and names of weekdays. It will convert the
   * weekdays to the nearest weekday that has not passed. I.e. if today is
   * Tuesday, and string is Wednesday, the DateTime will be 1 day after today.
   * if today is Tuesday, and string is Wednesday, the DateTime will be 7 days
   * after today.
   */
  public static DateTime parseDay(String string) {
    string = string.toLowerCase();
    DateTime today = getNowDate();
    switch (string) {
      case "today" :
        return today;
      case "tomorrow" :
      case "tml" :
      case "tmr" :
        return DateTime.today(TimeZone.getDefault()).plusDays(1);
      default :
        return getNearestDateToWeekday(string);
    }
  }

  private static int parseOffset(String string) {
    string = string.toLowerCase();
    switch (string) {
      case "coming" :
        return 0;
      case "next" :
        return 7;
      default :
        return 0;
    }

  }

  /*
   * Parses a string into a time. A time can optionally have am or pm appended
   * to the actual time. The actual time can be either 1) hour 2) hour delimiter
   * minute The delimiter is either a ":" or "."
   */
  public static DateTime parseTime(String string) {
    Integer hour = null;
    Integer min = null;
    if (string.matches("(([01]?[0-9]|2[0-3])[:.][0-5][0-9])")) {
      String[] results = string.split("[:.]");
      hour = Integer.parseInt(results[0]);
      min = Integer.parseInt(results[1]);
      return DateTime.forTimeOnly(hour, min, 0, 0);
    } else if (string.matches("(([1-9]|1[0-2])([:.]?[0-5][0-9])?[ap]m)")) {
      String[] results = string.split("[:.\\D]");
      if (string.contains("am")) {
        hour = Integer.parseInt(results[0]);
      } else {
        hour = Integer.parseInt(results[0]);
        if (hour != 12) {
          hour += 12;
        }
      }
      if (results.length > 1) {
        min = Integer.parseInt(results[1]);
      }
      if (results.length > 1) {
        min = Integer.parseInt(results[1]);
      } else {
        min = 0;
        min = 0;
      }
      return DateTime.forTimeOnly(hour, min, 0, 0);
    } else {
      return null;
    }
  }

  public static Date toDate(DateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return new Date(dateTime.getMilliseconds(TimeZone.getDefault()));
  }

  /*
   * converts a weekday into an integer which date4j uses
   */
  public static Integer weekdayToInteger(String weekday) {
    for (int i = 0; i < weekdays.length; i++) {
      if (weekdays[i] != null && weekdays[i].contains(weekday)) {
        return i;
      }
    }
    return -1;
  }

  public static String toString(DateTime date) {
    return DateOutput.formatDateTimeDayMonthHourMin(date);
  }
}