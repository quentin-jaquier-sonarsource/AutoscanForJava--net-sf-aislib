package pl.aislib.util.builders;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import pl.aislib.fm.forms.FieldBuilder;

/**
 * Field builder for <code>Date</code> dates.
 *
 * <p>
 * This class is used to build date from day, month and year fields.
 * <br>
 * Expected parameters:
 * <ul>
 *   <li><code>year</code></li>
 *   <li><code>month</code></li>
 *   <li><code>day</code></li>
 * </ul>
 * <br>
 * Properties:
 * <ul>
 *   <li>
 *     <code>format</code> - a format string used to build date.
 *       <br>
 *       Please note that only the following entites are used:
 *       <br>
 *       <code>yyyy</code>, <code>yy</code>, <code>MM</code>, <code>M</code>, <code>dd</code>, <code>d</code>.
 *   </li>
 * </ul>
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.3 $
 */
public class DateBuilder extends FieldBuilder {

  /**
   * Format property.
   */
  protected String format;


  // Constructors

  /**
   * Constructor for DateBuilder.
   */
  public DateBuilder() {
    super();

    format = "yyyy/MM/dd";
  }


  // Public methods

  /**
   * @see pl.aislib.fm.forms.FieldBuilder#join(Map)
   */
  public String join(Map values) {
    Object year = values.get("year");
    Object month = values.get("month");
    Object day = values.get("day");

    String strYear = year != null ? year.toString() : "";
    if (strYear.length() == 1) {
      strYear = "0" + strYear;
    }
    String strMonth = month != null ? month.toString() : "";
    String strDay = day != null ? day.toString() : "";

    int lYear = strYear.length();
    int lMonth = strMonth.length();
    int lDay = strDay.length();

    boolean bYear = (lYear != 0 && (format.indexOf("yyyy") != -1 || format.indexOf("yy") != -1));
    boolean bMonth = (lMonth != 0 && (format.indexOf("MM") != -1 || format.indexOf("M") != -1));
    boolean bDay = (lDay != 0 && (format.indexOf("dd") != -1 || format.indexOf("d") != -1));

    if (!bYear && !bMonth && !bDay) {
      return null;
    }

    StringBuffer result = new StringBuffer(format);

    if (!replace(result, "yyyy", lYear == 2 ? "20" + strYear : (lYear != 0 ? strYear : "yyyy"))) {
      replace(result, "yy", lYear != 0 ? strYear.substring(lYear - 2) : "yy");
    }

    if (!replace(result, "MM", lMonth != 0 ? (lMonth == 1 ? "0" + strMonth : strMonth) : "MM")) {
      replace(result, "M", lMonth != 0 ? strMonth : "M");
    }

    if (!replace(result, "dd", lDay != 0 ? (lDay == 1 ? "0" + strDay : strDay) : "dd")) {
      replace(result, "d", lDay != 0 ? strDay : "d");
    }

    return result.toString();
  }

  /**
   * @see pl.aislib.fm.forms.FieldBuilder#split(Object)
   */
  public Map split(Object value) {
    if (value == null) {
      return null;
    }

    if (value instanceof Date) {
      return splitDate((Date) value);
    }

    return null;
  }


  // Public property methods

  /**
   * @param format a string.
   */
  public void setFormat(String format) {
    this.format = format;
  }


  // Protected methods

  /**
   * @param value date given as @link{java.util.Calendar} object.
   * @return map of date elements as integers.
   */
  protected Map splitCalendar(Calendar value) {
    Map map = new HashMap();

    map.put("year", new Integer(value.get(Calendar.YEAR)));
    map.put("month", new Integer(value.get(Calendar.MONTH) + 1));
    map.put("day", new Integer(value.get(Calendar.DAY_OF_MONTH)));

    return map;
  }

  /**
   * @param value date given as @link{java.util.Date} object.
   * @return map of date elements as integers.
   */
  protected Map splitDate(Date value) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(value);
    return splitCalendar(cal);
  }


  // Private methods

  /**
   * Replaces string with another string in a buffer.
   * @param buf buffer.
   * @param what string to be replaced.
   * @param with a new string to be inserted.
   * @return true if the string has been replaced.
   */
  private boolean replace(StringBuffer buf, String what, String with) {
    int index = format.indexOf(what);
    if (index != -1) {
      buf.replace(index, index + what.length(), with);
    }
    return index != -1;
  }

} // DateBuilder class
