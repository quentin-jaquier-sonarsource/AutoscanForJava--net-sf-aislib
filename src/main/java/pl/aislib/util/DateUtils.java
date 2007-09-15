package pl.aislib.util;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Lenient date comparision - hour/minute/second stripping.
 * Only methods for {@link java.util.Date} and {@link java.util.Calendar}
 * are implemented - the rest ({@link java.sql.Date}, {@link java.sql.Time}
 * {@link java.util.GregorianCalendar}, {@link java.sql.Timestamp}) are
 * subclasses of these two.
 *
 * @author Daniel Rychcik, AIS.PL
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.3
 */

public class DateUtils {

  /**
   * Return the specified date with time of day set to 0:00AM
   *
   * @param src Argument (with possible H/M/S)
   * @return Stripped version
   */
  public static Calendar stripTime(Calendar src) {

    Calendar res = (Calendar) src.clone();

    res.set(Calendar.AM_PM, Calendar.AM);
    res.set(Calendar.HOUR, 0);
    res.set(Calendar.MINUTE, 0);
    res.set(Calendar.SECOND, 0);
    res.set(Calendar.MILLISECOND, 0);

    return res;
  }

  /**
   * Compare two {@link Calendar} objects ignoring time of day.
   *
   * @return <ul>
   * <li>-1 if <code>a</code> preceeds <code>b</code>
   * <li>0 if <code>a</code> is the same day as <code>b</code>
   * <li>1 if <code>a</code> follows <code>b</code>
   * </ul>
   */
  public static int lenientCompare(Calendar a, Calendar b) {

    Calendar stripped1 = stripTime(a);
    Calendar stripped2 = stripTime(b);

    Date date1 = stripped1.getTime();
    Date date2 = stripped2.getTime();

    return date1.compareTo(date2);
  }

  /**
   * Similar method for {@link Calendar}-to{@link Date} comparision
   */
  public static int lenientCompare(Calendar a, Date b) {

    Calendar cal2 = new GregorianCalendar();
    cal2.setTime(b);

    return lenientCompare(a, cal2);
  }

  /**
   * Similar method for {@link Date}-to{@link Calendar} comparision
   */
  public static int lenientCompare(Date a, Calendar b) {

    Calendar cal1 = new GregorianCalendar();
    cal1.setTime(a);

    return lenientCompare(cal1, b);
  }

  /**
   * Similar method for {@link Date}-to{@link Date} comparision
   */
  public static int lenientCompare(Date a, Date b) {

    Calendar cal1 = new GregorianCalendar();
    cal1.setTime(a);

    Calendar cal2 = new GregorianCalendar();
    cal2.setTime(b);

    return lenientCompare(cal1, cal2);
  }

} // class
