package pl.aislib.util.validators;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * Long number validation class.
 * 
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.4 $
 */
public class LongValidator extends NumberValidator {

  // Constructors
  
  /**
   * Base constructor.
   */
  public LongValidator() {
    super();
  }


  // Protected check methods
  
  /**
   * @see pl.aislib.util.validators.RangeValidator#checkRange
   */
  protected boolean checkRange(Object value) {
    Long lValue    = (Long) value;
    Long lStartRange = (Long) startRange.getConvertedValue();
    Long lEndRange   = (Long) endRange.getConvertedValue();

    if (lStartRange != null && lValue.compareTo(lStartRange) < 0) {
      return false;
    }
    if (lEndRange != null && lValue.compareTo(lEndRange) > 0) {
      return false;
    }
    return true;
  }


  // Protected methods

  /**
   * @see pl.aislib.util.validators.StringValidator#convertObject
   */
  protected Object convertObject(String value) {
    Long result = null;
  
    try {
      result = new Long(parseNumber(value).longValue());
    } catch (Exception e) {
      ;
    }
    
    return result;
  }
    
  /**
   * @see pl.aislib.util.validators.StringValidator#formatString
   */
  protected Object formatString(String value) throws Exception {
    Long result = new Long(parseNumber(value).longValue());
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

} // LongValidator class
