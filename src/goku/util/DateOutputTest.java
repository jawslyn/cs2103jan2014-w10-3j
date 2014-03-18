package goku.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import hirondelle.date4j.DateTime;

import org.junit.Before;
import org.junit.Test;

public class DateOutputTest {
  public static final String TOMORROW = "tomorrow";
  public static final String HOURS_LATER = " hours later";
  public static final String DAYS_LATER = " days later";
  public static final String NEXT_WEEK = "next week";

  DateTime base; // Has the same year, month, day as the date now, but set is
                 // set to 6pm

  @Before
  public void setup() {
    base = DateUtil.getNow();
  }

  @Test
  public void format_HoursLater_returnsHOURS_LATER() throws Exception {
    DateTime dt = base.plus(0, 0, 0, 2, 0, 0, 0, DateTime.DayOverflow.FirstDay);
    String actual = DateOutput.format(dt);

    if (dt.getDay() == base.getDay()) {
      assertEquals("2" + HOURS_LATER, actual);
    } else {
      assertEquals(TOMORROW, actual);
    }

    dt = base.plus(0, 0, 0, 5, 0, 0, 0, DateTime.DayOverflow.FirstDay);
    actual = DateOutput.format(dt);

    if (dt.getDay() == base.getDay()) {
      assertEquals("5" + HOURS_LATER, actual);
    } else {
      assertEquals(TOMORROW, actual);
    }

    dt = base.plus(0, 0, 0, 24, 0, 0, 0, DateTime.DayOverflow.FirstDay);
    actual = DateOutput.format(dt);
    assertNotEquals("24" + HOURS_LATER, actual);
  }

  @Test
  public void format_1DayLater_returnsTOMORROW() {
    DateTime dt = base.plusDays(1);
    String actual = DateOutput.format(dt);
    assertEquals(TOMORROW, actual);
  }

  @Test
  public void format_2To6DaysLater_returnsDAYS_LATER() throws Exception {
    DateTime dt = base.plusDays(2);
    String actual = DateOutput.format(dt);
    assertEquals("2" + DAYS_LATER, actual);

    dt = base.plusDays(3);
    actual = DateOutput.format(dt);
    assertEquals("3" + DAYS_LATER, actual);

    dt = base.plusDays(6);
    actual = DateOutput.format(dt);
    assertEquals("6" + DAYS_LATER, actual);

    dt = base.plusDays(7);
    actual = DateOutput.format(dt);
    assertNotEquals("7" + DAYS_LATER, actual);
  }

  @Test
  public void format_MoreThan7DaysLater_returnsWEEK_LATER() throws Exception {
    DateTime dt = base.plusDays(7);
    String actual = DateOutput.format(dt);
    assertEquals(dt.getDay() + "/" + dt.getMonth(), actual);
  }
}
