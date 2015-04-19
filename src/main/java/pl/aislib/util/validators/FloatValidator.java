package pl.aislib.util.validators;

/**
 * Float number validation class.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class FloatValidator extends NumberValidator {

  // Constructors

  /**
   * Base constructor.
   */
  public FloatValidator() {
    super();
  }


  // Protected check methods

  /**
   * @see pl.aislib.util.validators.RangeValidator#checkRange
   */
  protected boolean checkRange(Object value) {
    Float lValue    = (Float) value;
    Float lStartRange = (Float) startRange.getConvertedValue();
    Float lEndRange   = (Float) endRange.getConvertedValue();

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
    Float result = null;

    try {
      result = new Float(parseNumber(value).floatValue());
    } catch (Exception e) {
      ;
    }

    return result;
  }

  /**
   * @see pl.aislib.util.validators.StringValidator#formatString
   */
  protected Object formatString(String value) throws Exception {
    Float result = new Float(parseNumber(value).floatValue());
    return result;
  }

} // FloatValidator class
