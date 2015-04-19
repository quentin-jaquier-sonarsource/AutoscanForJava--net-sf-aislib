package pl.aislib.util.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import pl.aislib.fm.forms.ValidateException;

/**
 * Date validation class.
 *
 * <p>
 * Implemented additional properties:
 * <ul>
 *   <li><code>allowFuture</code></li>
 *   <li><code>allowPast</code></li>
 * </ul>
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class DateValidator extends RangeValidator {

  /**
   * Property of checking validated past date against current date.
   */
  protected BooleanProperty allowPast;

  /**
   * Property of checking validated future date against current date.
   */
  protected BooleanProperty allowFuture;


  // Constructors

  /**
   * Base constructor.
   */
  public DateValidator() {
    super();

    format.setValue("yyyy/MM/dd");

    allowPast = new BooleanProperty("allowPast", 5, Property.DEFAULT, true);
    allowFuture = new BooleanProperty("allowFuture", 5, Property.DEFAULT, true);
  }


  // Public validation methods

  /**
   * @see pl.aislib.util.validators.Validator#validateObject(Object)
   */
  public void validateObject(Object value) throws ValidateException {
    super.validateObject(value);

    if (checkProperty(allowPast) && !checkPast(value)) {
      throw new ValidateException("Value is before current date.");
    }

    if (checkProperty(allowFuture) && !checkFuture(value)) {
      throw new ValidateException("Value is after current date.");
    }
  }


  // Public property methods

  /**
   * @param value true if date should be checked against current date.
   */
  public void setAllowPast(boolean value) {
    allowPast.set(value);
  }

  /**
   * @param value true if date should be checked against current date.
   */
  public void setAllowFuture(boolean value) {
    allowFuture.set(value);
  }


  // Protected check methods

  /**
   * @param value value to be checked against current date.
   * @return true if value is after current date.
   */
  protected boolean checkPast(Object value) {
    if (allowPast.isTrue()) {
      return true;
    }
    Date dValue = (Date) value;

    Date dCurrent = null;
    Calendar cal = Calendar.getInstance();

    try {
      SimpleDateFormat sdf = new SimpleDateFormat((String) format.getValue(), locale);
      sdf.setLenient(false);
      String strCurrent = sdf.format(cal.getTime());
      dCurrent = sdf.parse(strCurrent);
    } catch (ParseException e) {
      dCurrent = cal.getTime();
    }

    return !(dValue.before(dCurrent));
  }

  /**
   * @param value value to be checked against current date.
   * @return true if value is after current date.
   */
  protected boolean checkFuture(Object value) {
    if (allowFuture.isTrue()) {
      return true;
    }
    Date dValue = (Date) value;

    Date dCurrent = null;
    Calendar cal = Calendar.getInstance();

    try {
      SimpleDateFormat sdf = new SimpleDateFormat((String) format.getValue(), locale);
      sdf.setLenient(false);
      String strCurrent = sdf.format(cal.getTime());
      dCurrent = sdf.parse(strCurrent);
    } catch (ParseException e) {
      dCurrent = cal.getTime();
    }

    return !(dValue.after(dCurrent));
  }

  /**
   * @see RangeValidator#checkRange
   */
  protected boolean checkRange(Object value) {
    Date dValue      = (Date) value;
    Date dStartRange = (Date) startRange.getConvertedValue();
    Date dEndRange   = (Date) endRange.getConvertedValue();

    if (dStartRange != null && dValue.before(dStartRange)) {
      return false;
    }
    if (dEndRange != null && dValue.after(dEndRange)) {
      return false;
    }

    return true;
  }


  // Protected methods

  /**
   * @see pl.aislib.util.validators.StringValidator#convertObject
   */
  protected Object convertObject(String value) {
    Date dRange = null;

    try {
      SimpleDateFormat sdf = new SimpleDateFormat((String) format.getValue(), locale);
      dRange = sdf.parse(value);
    } catch (Exception e) {
      ;
    }

    return dRange;
  }

  /**
   * @see pl.aislib.util.validators.StringValidator#formatString
   */
  protected Object formatString(String value) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat((String) format.getValue(), locale);
    sdf.setLenient(false);
    Date dValue = sdf.parse(value);
    return dValue;
  }

} // DateValidator class
