package pl.aislib.util.validators;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * Number validation class.
 * 
 * <p>
 * It uses conversion from strings to doubles.
 * <br>
 * Implemented additional properties:
 * <ul>
 *   <li><code>groupingSeparator</code></li>
 * </ul>
 * 
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.6 $
 */
public class NumberValidator extends RangeValidator {

  /**
   * Format needed for parsing values.
   */
  protected Format decimalFormat;


  // Constructors
  
  /**
   * Base constructor.
   */
  public NumberValidator() {
    super();

    decimalFormat = getDecimalFormat();
  }


  // Public property methods

  /**
   * @see pl.aislib.util.validators.StringValidator#setFormat(String)
   */
  public void setFormat(String value) {
    super.setFormat(value);
    NumberFormatHelper.applyLocalizedPattern(decimalFormat, value);
  }
  
  /**
   * @see pl.aislib.util.validators.AbstractValidator#setLocale(java.lang.String)
   */
  public void setLocale(String value) {
    super.setLocale(value);
    decimalFormat = getDecimalFormat();
  }

  /**
   * @param value grouping separator.
   */  
  public void setGroupingSeparator(String value) {
    char gsOld = NumberFormatHelper.setGroupingSeparator(decimalFormat, value.charAt(0));
    format.setValue(format.getValue().toString().replace(gsOld, value.charAt(0)));
    NumberFormatHelper.applyLocalizedPattern(decimalFormat, format.getValue().toString());
  }


  // Protected check methods
  
  /**
   * @see RangeValidator#checkRange(Object)
   */
  protected boolean checkRange(Object value) {
    Double dValue    = (Double) value;
    Double dStartRange = (Double) startRange.getConvertedValue();
    Double dEndRange   = (Double) endRange.getConvertedValue();

    if (dStartRange != null && dValue.compareTo(dStartRange) < 0) {
      return false;
    }
    if (dEndRange != null && dValue.compareTo(dEndRange) > 0) {
      return false;
    }
    
    return true;
  }


  // Protected methods

  /**
   * @see StringValidator#convertObject(String)
   */
  protected Object convertObject(String value) {
    Double result = null;
  
    try {
      result = new Double(parseNumber(value).doubleValue());
    } catch (Exception e) {
      ;
    }
    
    return result;
  }
    
  /**
   * @see StringValidator#formatString(String)
   */
  protected Object formatString(String value) throws Exception {
    Double result = new Double(parseNumber(value).doubleValue());
    return result;
  }

  /**
   * @param value a string to be parsed.
   * @return number taken from the string.
   * @throws Exception if the string could not be parsed properly.
   */
  protected Number parseNumber(String value) throws Exception {
    ParsePosition pos = new ParsePosition(0);
    Number result = NumberFormatHelper.parse(decimalFormat, value, pos);
    if (pos.getIndex() != value.length()) {
      throw new NumberFormatException("Invalid format.");
    }
    return result;
  }

  /**
   * @return new instance of decimal format.
   */  
  protected Format getDecimalFormat() {
    NumberFormat result = NumberFormat.getInstance(locale);
    if (result instanceof DecimalFormat) {
      DecimalFormat df = (DecimalFormat) result;
      df.setMaximumFractionDigits(10);
      format.setValue(df.toLocalizedPattern());
      return df;
    } else {
      return null;
    }
  }

} // NumberValidator class
