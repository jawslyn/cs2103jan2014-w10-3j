package goku.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hirondelle.date4j.DateTime;

import org.junit.Test;

public class DateUtilTest {
  @Test
  public void looksLikeOffset() throws Exception {
    assertTrue(DateUtil.isOffsetWord("next"));
  }

  /*
   * Combination heuristics. All inputs below are well-formed
   * and should pass.
   */
  @Test
  public void looksLikeTime_success() throws Exception {
    assertTrue(DateUtil.looksLikeTime("1:45"));
    assertTrue(DateUtil.looksLikeTime("1.45"));
    assertTrue(DateUtil.looksLikeTime("13:45"));
    assertTrue(DateUtil.looksLikeTime("1pm"));
    assertTrue(DateUtil.looksLikeTime("1:45am"));
    assertTrue(DateUtil.looksLikeTime("1:45pm"));
    assertTrue(DateUtil.looksLikeTime("1.45am"));
  }

  /*
   * Combination heuristics. All inputs below are well-formed
   * and should pass.
   */
  @Test
  public void looksLikeDate_success() throws Exception {
    assertTrue(DateUtil.looksLikeDay("sUn"));
    assertTrue(DateUtil.looksLikeDay("Mon"));
    assertTrue(DateUtil.looksLikeDay("tuE"));
    assertTrue(DateUtil.looksLikeDay("wED"));
    assertTrue(DateUtil.looksLikeDay("THur"));
    assertTrue(DateUtil.looksLikeDay("fRi"));
    assertTrue(DateUtil.looksLikeDay("Sat"));
    assertTrue(DateUtil.looksLikeDay("SuNday"));
    assertTrue(DateUtil.looksLikeDay("monday"));
    assertTrue(DateUtil.looksLikeDay("tuesday"));
    assertTrue(DateUtil.looksLikeDay("WedNesDay"));
    assertTrue(DateUtil.looksLikeDay("Thursday"));
    assertTrue(DateUtil.looksLikeDay("Friday"));
    assertTrue(DateUtil.looksLikeDay("saturDAY"));
    assertTrue(DateUtil.looksLikeDay("tOdAy"));
    assertTrue(DateUtil.looksLikeDay("toMORRow"));
    assertTrue(DateUtil.looksLikeDay("tMr"));
    assertTrue(DateUtil.looksLikeDay("tmL"));
    assertTrue(DateUtil.looksLikeDate("4/3"));
    assertTrue(DateUtil.looksLikeDate("4/3/12"));
    assertTrue(DateUtil.looksLikeDate("4-3"));
    assertTrue(DateUtil.looksLikeDate("4-3-12"));
  }

  /*
   * Combination heuristics. All inputs below are well-formed
   * and should pass.
   */
  @Test
  public void parseDay_success() throws Exception {
    DateTime now = DateUtil.getNow();
    DateTime result, expected;

    expected = now.plusDays(1);
    result = DateUtil.parseDay("toMOrrow");
    assertTrue(expected.isSameDayAs(result));
    result = DateUtil.parseDay("tMl");
    assertTrue(expected.isSameDayAs(result));
    result = DateUtil.parseDay("tMr");
    assertTrue(expected.isSameDayAs(result));
    result = DateUtil.parseDay("todaY");
    assertTrue(now.isSameDayAs(result));
  }

  @Test
  public void parseWithWeekOffset_success() throws Exception {
    // DateTime base = new DateTime(2014, 3, 27, 0, 0, 0, 0); // this is a
    // thursday
    // DateTime result, expected;
    // expected = base.plusDays(8);
    //
    // result = DateUtil.parse("next friday".split(" "));
    // assertEquals(expected.getDay(), result.getDay());
    // assertTrue(expected.isSameDayAs(result));
    //
    // // TODO is this expected result?
    // expected = base.plusDays(14);
    // result = DateUtil.parse("next thursday".split(" "));
    // assertTrue(expected.isSameDayAs(result));
    //
    // expected = base.plusDays(11);
    // result = DateUtil.parse("Next monday".split(" "));
    // assertTrue(expected.isSameDayAs(result));
    // assertTrue(expected.isSameDayAs(result));
  }

  /*
   * Combination heuristics. All inputs below are well-formed
   * and should pass.
   */
  @Test
  public void parseDate_success() throws Exception {
    DateTime now = DateUtil.getNow(), actual, expected;

    actual = DateUtil.parseDate("1/1");
    expected = DateTime.forDateOnly(now.getYear(), 1, 1);
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("01/1");
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("01/01");
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("1/01");
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("10/01");
    expected = DateTime.forDateOnly(now.getYear(), 1, 10);
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("19/4");
    expected = DateTime.forDateOnly(now.getYear(), 4, 19);
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("21/4");
    expected = DateTime.forDateOnly(now.getYear(), 4, 21);
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("31/8");
    expected = DateTime.forDateOnly(now.getYear(), 8, 31);
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("4/3");
    expected = DateTime.forDateOnly(now.getYear(), 3, 4);
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("4/3/12");
    expected = DateTime.forDateOnly(2012, 3, 4);
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("04/03/12");
    expected = DateTime.forDateOnly(2012, 3, 4);
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("4-3");
    expected = DateTime.forDateOnly(now.getYear(), 3, 4);
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("4-3-12");
    expected = DateTime.forDateOnly(2012, 3, 4);
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("04-03-12");
    expected = DateTime.forDateOnly(2012, 3, 4);
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("31-8");
    expected = DateTime.forDateOnly(now.getYear(), 8, 31);
    assertTrue(expected.isSameDayAs(actual));

    actual = DateUtil.parseDate("31-8-123");
    expected = DateTime.forDateOnly(2123, 8, 31);
    assertTrue(expected.isSameDayAs(actual));
  }

  @Test
  public void parseDate_invalidDates_returnsNullDateTime() throws Exception {
    DateTime actual;

    actual = DateUtil.parseDate("34-3-12");
    assertNull(actual);

    actual = DateUtil.parseDate("004-3-12");
    assertNull(actual);

    actual = DateUtil.parseDate("4-23-12");
    assertNull(actual);

    actual = DateUtil.parseDate("4-023-12");
    assertNull(actual);
  }

  /*
   * Combination heuristics. All inputs below are well-formed
   * and should pass.
   */
  @Test
  public void parseTime_success() throws Exception {
    DateTime date, expected;

    date = DateUtil.parseTime("1:45");
    expected = DateTime.forTimeOnly(1, 45, 0, 0);
    assertEquals(expected.getHour(), date.getHour());
    assertEquals(expected.getMinute(), date.getMinute());

    date = DateUtil.parseTime("1.45");
    expected = DateTime.forTimeOnly(1, 45, 0, 0);
    assertEquals(expected.getHour(), date.getHour());
    assertEquals(expected.getMinute(), date.getMinute());

    date = DateUtil.parseTime("13:45");
    expected = DateTime.forTimeOnly(13, 45, 0, 0);
    assertEquals(expected.getHour(), date.getHour());
    assertEquals(expected.getMinute(), date.getMinute());

    date = DateUtil.parseTime("1pm");
    expected = DateTime.forTimeOnly(13, 0, 0, 0);
    assertEquals(expected.getHour(), date.getHour());
    assertEquals(expected.getMinute(), date.getMinute());

    date = DateUtil.parseTime("3am");
    expected = DateTime.forTimeOnly(3, 0, 0, 0);
    assertEquals(expected.getHour(), date.getHour());
    assertEquals(expected.getMinute(), date.getMinute());

    date = DateUtil.parseTime("1:45am");
    expected = DateTime.forTimeOnly(1, 45, 0, 0);
    assertEquals(expected.getHour(), date.getHour());
    assertEquals(expected.getMinute(), date.getMinute());

    date = DateUtil.parseTime("1:45pm");
    expected = DateTime.forTimeOnly(13, 45, 0, 0);
    assertEquals(expected.getHour(), date.getHour());
    assertEquals(expected.getMinute(), date.getMinute());

    date = DateUtil.parseTime("1.45am");
    expected = DateTime.forTimeOnly(1, 45, 0, 0);
    assertEquals(expected.getHour(), date.getHour());
    assertEquals(expected.getMinute(), date.getMinute());

    date = DateUtil.parseTime("91.45am");
    assertNull(date);
  }

  @Test
  public void parseTime_invalidTime_returnsNull() throws Exception {
    DateTime actual;
    actual = DateUtil.parseTime("90pm");
    assertNull(actual);
    actual = DateUtil.parseTime("21.45am");
    assertNull(actual);
    actual = DateUtil.parseTime("21:45pm");
    assertNull(actual);
  }

  /*
   * Combination heuristics. All inputs below are well-formed
   * and should pass.
   */
  @Test
  public void getNearestDateToWeekday_success() {
    DateTime start, nearest, result;
    start = DateTime.forDateOnly(2014, 3, 4);
    result = DateUtil.getNearestDateToWeekday(start, 2);
    nearest = DateTime.forDateOnly(2014, 3, 10);
    assertTrue(nearest.isSameDayAs(result));

    start = DateTime.forDateOnly(2014, 3, 4);
    result = DateUtil.getNearestDateToWeekday(start, start.getWeekDay());
    nearest = DateTime.forDateOnly(2014, 3, 11);
    assertTrue(nearest.isSameDayAs(result));

    start = DateTime.forDateOnly(2014, 3, 31);
    result = DateUtil.getNearestDateToWeekday(start, start.getWeekDay() + 1);
    nearest = DateTime.forDateOnly(2014, 4, 1);
    assertTrue(nearest.isSameDayAs(result));
  }

  /*
   * Combination heuristics. All inputs below are well-formed
   * and should pass.
   */
  @Test
  public void mergeDateAndTime_success() throws Exception {
    DateTime date = null, time = null, result, expected;
    date = DateTime.forDateOnly(2014, 3, 4);
    expected = DateTime.forDateOnly(2014, 3, 4);
    result = DateUtil.mergeDateAndTime(date, null, 0);
    assertTrue(expected.isSameDayAs(result));

    time = DateTime.forTimeOnly(12, 13, 14, 0);
    expected = DateUtil.getNow();
    result = DateUtil.mergeDateAndTime(null, time, 0);
    assertTrue(expected.isSameDayAs(result));
    assertTrue(result.getHour() == 12);
    assertTrue(result.getMinute() == 13);
    assertTrue(result.getSecond() == 14);

    date = DateTime.forDateOnly(2014, 3, 4);
    time = DateTime.forTimeOnly(12, 13, 14, 0);
    expected = new DateTime(2014, 3, 4, 12, 13, 14, 0)
        .truncate(DateTime.Unit.SECOND);
    result = DateUtil.mergeDateAndTime(date, time, 0);
    assertEquals(expected, result);
  }
}
