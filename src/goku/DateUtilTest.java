package goku;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class DateUtilTest {
  @Test
  public void makeDates() throws Exception {
    Date aDate = DateUtil.makeDate(1, 8);
    int[] dmy = DateUtil.getDayMonthYear(aDate);
    assertEquals(1, dmy[0]);
    assertEquals(8, dmy[1]);
    assertEquals(2014, dmy[2]);

    Date otherDate = DateUtil.makeDate(1, 8, 2011);
    int[] otherDmy = DateUtil.getDayMonthYear(otherDate);
    assertEquals(1, otherDmy[0]);
    assertEquals(8, otherDmy[1]);
    assertEquals(2011, otherDmy[2]);
  }

  @Test
  public void compareDayMonthYear() {
    Date aDate;
    Date otherDate;

    /* All return 0 */
    aDate = DateUtil.makeDate(1, 8, 2011);
    otherDate = DateUtil.makeDate(1, 8, 2011);
    assertEquals(0, DateUtil.compareDayMonthYear(aDate, otherDate));

    aDate = DateUtil.makeDate(31, 8, 2016);
    otherDate = DateUtil.makeDate(31, 8, 2016);
    assertEquals(0, DateUtil.compareDayMonthYear(aDate, otherDate));

    /* All return -1 */
    aDate = DateUtil.makeDate(1, 8, 2010);
    otherDate = DateUtil.makeDate(1, 8, 2011);
    assertEquals(-1, DateUtil.compareDayMonthYear(aDate, otherDate));

    aDate = DateUtil.makeDate(1, 7, 2011);
    otherDate = DateUtil.makeDate(1, 8, 2011);
    assertEquals(-1, DateUtil.compareDayMonthYear(aDate, otherDate));

    aDate = DateUtil.makeDate(1, 8, 2011);
    otherDate = DateUtil.makeDate(2, 8, 2011);
    assertEquals(-1, DateUtil.compareDayMonthYear(aDate, otherDate));

    /* All return 1 */
    aDate = DateUtil.makeDate(1, 8, 2012);
    otherDate = DateUtil.makeDate(1, 8, 2011);
    assertEquals(1, DateUtil.compareDayMonthYear(aDate, otherDate));

    aDate = DateUtil.makeDate(1, 9, 2011);
    otherDate = DateUtil.makeDate(1, 8, 2011);
    assertEquals(1, DateUtil.compareDayMonthYear(aDate, otherDate));

    aDate = DateUtil.makeDate(2, 8, 2011);
    otherDate = DateUtil.makeDate(1, 8, 2011);
    assertEquals(1, DateUtil.compareDayMonthYear(aDate, otherDate));
  }

}
