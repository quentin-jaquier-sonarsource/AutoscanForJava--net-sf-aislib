package pl.aislib.util.validators;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.Locale;

import java.math.BigDecimal;

/**
 * <code>BigDecimal</code> number validation class.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.3 $
 */
public class BigDecimalValidator extends NumberValidator {

  /**
   * Format needed for parsing values correctly.
   */
  protected static DecimalFormat bdFormat = getBdFormat();


  // Constructors

  /**
   * Constructor for BigDecimalValidator.
   */
  public BigDecimalValidator() {
    super();
  }


  // Protected check methods

  /**
   * @see pl.aislib.util.validators.RangeValidator#checkRange
   */
  protected boolean checkRange(Object value) {
    BigDecimal bdValue = (BigDecimal) value;
    BigDecimal bdStartRange = (BigDecimal) startRange.getConvertedValue();
    BigDecimal bdEndRange = (BigDecimal) endRange.getConvertedValue();

    if (bdStartRange != null && bdValue.compareTo(bdStartRange) < 0) {
      return false;
    }
    if (bdEndRange != null && bdValue.compareTo(bdEndRange) > 0) {
      return false;
    }
    return true;
  }


  // Protected methods

  /**
   * @see pl.aislib.util.validators.StringValidator#convertObject
   */
  protected Object convertObject(String value) {
    BigDecimal result = null;

    try {
      result = new BigDecimal(bdFormat.format(parseNumber(value).doubleValue()));
    } catch (Exception e) {
      ;
    }

    return result;
  }

  /**
   * @see pl.aislib.util.validators.StringValidator#formatString
   */
  protected Object formatString(String value) throws Exception {
    double d = parseNumber(value).doubleValue();
    String f = bdFormat.format(d);
    BigDecimal result = new BigDecimal(bdFormat.format(parseNumber(value).doubleValue()));
    return result;
  }


  // Protected static methods

  /**
   * @return format needed for parsing values correctly.
   */
  protected static DecimalFormat getBdFormat() {
    NumberFormat result = NumberFormat.getInstance(Locale.US);
    if (result instanceof DecimalFormat) {
      DecimalFormat df = (DecimalFormat) result;
      df.applyPattern("0.##########");
      return df;
    } else {
      return null;
    }
  }

} // BigDecimalValidator class
