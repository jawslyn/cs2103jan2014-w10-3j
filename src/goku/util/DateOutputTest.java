package goku.util;

import static org.junit.Assert.assertEquals;
import hirondelle.date4j.DateTime;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Jonathan
 * @author ZhiAn
 * 
 */
public class DateOutputTest {
  public static final String TOMORROW = "tomorrow";
  public static final String HOURS_LATER = "h later";
  public static final String DAYS_LATER = "d later";
  public static final String NEXT_WEEK = "next week";

  DateTime base;

  @Before
  public void setup() {
    base = DateUtil.getNow();
  }

  @Test
  public void formatTimeOnly12h_returnsTimeOnly() throws Exception {
    DateTime dateTime = DateTime.forTimeOnly(13, 15, 0, 0);
    String output = DateOutput.formatTimeOnly12h(dateTime);
    assertEquals("1.15pm", output);
    dateTime = DateTime.forTimeOnly(0, 15, 0, 0);
    output = DateOutput.formatTimeOnly12h(dateTime);
    assertEquals("12.15am", output);
  }

  @Test
  public void formatTimeOnly24h_returnsTimeOnly() throws Exception {
    DateTime dateTime = DateTime.forTimeOnly(13, 15, 0, 0);
    String output = DateOutput.formatTimeOnly24h(dateTime);
    assertEquals("13:15h", output);
    dateTime = DateTime.forTimeOnly(1, 15, 0, 0);
    output = DateOutput.formatTimeOnly24h(dateTime);
    assertEquals("01:15h", output);
  }

  @Test
  public void formatTimeOnlyHoursLater_returnsHoursLater() throws Exception {
    DateTime dt = base.plus(0, 0, 0, 2, 0, 0, 0, DateTime.DayOverflow.FirstDay);
    String actual = DateOutput.formatTimeOnlyHoursLater(dt);
    if (dt.getDay() == base.getDay()) {
      assertEquals("2" + HOURS_LATER, actual);
    }
  }

  @Test
  public void formatDateOnlyDayMonth_returnsDateWithDayMonth() throws Exception {
    DateTime dateTime = DateTime.forDateOnly(2014, 4, 1);
    String output = DateOutput.formatDateOnlyDayMonth(dateTime);
    assertEquals("1/4", output);
  }

  @Test
  public void formatDateOnlyDaysLater_returnsDaysLater() throws Exception {
    DateTime dt = base.plusDays(2);
    String actual = DateOutput.formatDateOnlyDaysLater(dt);
    assertEquals("2" + DAYS_LATER, actual);

    actual = DateOutput.formatDateOnlyDaysLater(base);
    assertEquals("today", actual);

    dt = base.plusDays(7);
    actual = DateOutput.formatDateOnlyDaysLater(dt);
    assertEquals("7" + DAYS_LATER, actual);
  }
}
