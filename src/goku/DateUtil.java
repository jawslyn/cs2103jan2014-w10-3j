package goku;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

  public static boolean dateIsEarlierThan(Date aDate, Date otherDate) {
    return compareDayMonthYear(aDate, otherDate) < 0;
  }

  public static boolean dateIsLaterThan(Date aDate, Date otherDate) {
    return compareDayMonthYear(aDate, otherDate) > 0;
  }

  public static boolean dateIsSameAs(Date aDate, Date otherDate) {
    return compareDayMonthYear(aDate, otherDate) == 0;
  }

  public static boolean dateEarlierThanOrSameAs(Date aDate, Date otherDate) {
    return compareDayMonthYear(aDate, otherDate) <= 0;
  }

  public static boolean dateTimeIsEarlierThan(Date aDate, Date otherDate) {
    return aDate.before(otherDate);
  }

  public static boolean dateTimeIsLaterThan(Date aDate, Date otherDate) {
    return aDate.after(otherDate);
  }

  public static boolean dateTimeIsSameAs(Date aDate, Date otherDate) {
    return aDate.equals(otherDate);
  }

  /*
   * Compares two dates by its day, month and year. returns -1 if aDate is
   * before otherDate, 1 if after, 0 if same.
   */
  public static int compareDayMonthYear(Date aDate, Date otherDate) {
    int[] aDmy = getDayMonthYear(aDate);
    int[] otherDmy = getDayMonthYear(otherDate);
    if (aDmy[2] < otherDmy[2]) {
      return -1;
    } else if (aDmy[2] > otherDmy[2]) {
      return 1;
    } else {
      if (aDmy[1] < otherDmy[1]) {
        return -1;
      } else if (aDmy[1] > otherDmy[1]) {
        return 1;
      } else {
        if (aDmy[0] < otherDmy[0]) {
          return -1;
        } else if (aDmy[0] > otherDmy[0]) {
          return 1;
        } else {
          return 0;
        }
      }
    }
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
