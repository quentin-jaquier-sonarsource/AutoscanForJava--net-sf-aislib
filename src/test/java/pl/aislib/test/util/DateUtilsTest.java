package pl.aislib.test.util;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import pl.aislib.util.DateUtils;

import junit.framework.TestCase;

/**
 * Testing funcionality of <code>pl.aislib.util.DateUtils</code> class.
 *
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.1.1.1 $
 */
public class DateUtilsTest extends TestCase {

  private Date todayDate1;
  private Date todayDate2;
  private Calendar todayCalendar1;
  private Calendar todayCalendar2;

  /**
   * Create today's dates and calendars.
   */
  public void setUp() {
    todayDate1 = new Date();
    todayDate2 = (Date) todayDate1.clone();
    todayCalendar1 = new GregorianCalendar();
    todayCalendar1.setTime(todayDate1);
    todayCalendar2 = (GregorianCalendar) todayCalendar1.clone();
  }

  /**
   * Does nothing.
   */
  public DateUtilsTest(String message) {
    super(message);
  }

  /**
   * Testing two equal dates.
   */
  public void testEqualDates() {
    assertEquals("two equal dates", DateUtils.lenientCompare(todayDate1, todayDate2), 0);
  }

  /**
   * Testing two equals calendars.
   */
  public void testEqualCalendars() {
    assertEquals("two equal calendars", DateUtils.lenientCompare(todayCalendar1, todayCalendar2), 0);
  }

  /**
   * Tesing equal date and calendar.
   */
  public void testEqualDateAndCalendar() {
    assertEquals("equal date and calendar", DateUtils.lenientCompare(todayCalendar1, todayDate1), 0);
    assertEquals("equal calendar and date", DateUtils.lenientCompare(todayDate1, todayCalendar1), 0);
  }
}

