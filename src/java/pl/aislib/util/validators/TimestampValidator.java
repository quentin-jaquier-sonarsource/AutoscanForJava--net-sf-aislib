package pl.aislib.util.validators;

import java.sql.Timestamp;

import java.util.Date;

/**
 * Timestamp validation class.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.2 $
 */
public class TimestampValidator extends DateValidator {

  // Constructors

  /**
   * Constructor for TimestampValidator.
   */
  public TimestampValidator() {
    super();
  }


  // Protected methods

  /**
   * @see pl.aislib.util.validators.StringValidator#convertObject(String)
   */
  protected Object convertObject(String value) {
    Object date = super.convertObject(value);
    return date != null ? new Timestamp(((Date) date).getTime()) : null;
  }

  /**
   * @see pl.aislib.util.validators.StringValidator#formatString(String)
   */
  protected Object formatString(String value) throws Exception {
    Object date = super.formatString(value);
    return date != null ? new Timestamp(((Date) date).getTime()) : null;
  }

} // TimestampValidator class
