package pl.aislib.util.validators;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * Integer number validation class.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class IntegerValidator extends NumberValidator {

  // Constructors

  /**
   * Base constructor.
   */
  public IntegerValidator() {
    super();
  }


  // Protected check methods

  /**
   * @see pl.aislib.util.validators.RangeValidator#checkRange
   */
  protected boolean checkRange(Object value) {
    Integer iValue     = (Integer) value;
    Integer iStartRange = (Integer) startRange.getConvertedValue();
    Integer iEndRange  = (Integer) endRange.getConvertedValue();

    if (iStartRange != null && iValue.compareTo(iStartRange) < 0) {
      return false;
    }
    if (iEndRange != null && iValue.compareTo(iEndRange) > 0) {
      return false;
    }
    return true;
  }


  // Protected methods

  /**
   * @see pl.aislib.util.validators.StringValidator#convertObject
   */
  protected Object convertObject(String value) {
    Integer result = null;

    try {
      result = new Integer(parseNumber(value).intValue());
    } catch (Exception e) {
      ;
    }

    return result;
  }

  /**
   * @see pl.aislib.util.validators.StringValidator#formatString
   */
  protected Object formatString(String value) throws Exception {
    Integer result = new Integer(parseNumber(value).intValue());
    return result;
  }

  /**
   * @see pl.aislib.util.validators.NumberValidator#getDecimalFormat()
   */
  protected Format getDecimalFormat() {
    Format df = super.getDecimalFormat();
    NumberFormatHelper.setParseIntegerOnly(df, true);
//    df.setParseIntegerOnly(true);
    return df;
  }

} // IntegerValidator class
