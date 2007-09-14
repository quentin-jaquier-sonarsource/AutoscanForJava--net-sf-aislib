package pl.aislib.util.validators;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
 * @version $Revision: 1.3 $
 */
public class NumberValidator extends RangeValidator {

  /**
   * Format needed for parsing values.
   */
  protected DecimalFormat decimalFormat;


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
    decimalFormat.applyLocalizedPattern(value);
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
    DecimalFormatSymbols dfs = decimalFormat.getDecimalFormatSymbols();
    char gsOld = dfs.getGroupingSeparator();
    dfs.setGroupingSeparator(value.charAt(0));
    decimalFormat.setDecimalFormatSymbols(dfs);
    format.setValue(format.getValue().toString().replace(gsOld, value.charAt(0)));
    decimalFormat.applyLocalizedPattern(format.getValue().toString());
  }


  // Protected check methods

  /**
   * @see pl.aislib.util.validators.RangeValidator#checkRange
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
   * @see pl.aislib.util.validators.StringValidator#convertObject
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
   * @see pl.aislib.util.validators.StringValidator#formatString
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
    Number result = decimalFormat.parse(value, pos);
    if (pos.getIndex() != value.length()) {
      throw new NumberFormatException("Invalid format.");
    }
    return result;
  }

  /**
   * @return new instance of decimal format.
   */
  protected DecimalFormat getDecimalFormat() {
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
