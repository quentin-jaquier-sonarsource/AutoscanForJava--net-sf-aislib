package pl.aislib.util.validators;

import java.text.Format;
import java.text.ParsePosition;

import java.math.BigDecimal;

/**
 * <code>BigDecimal</code> number validation class.
 *
 * Implemented additional properties:
 * <ul>
 *   <li><code>useICU4J</code> - default to <code>false</code>;</li>
 * </ul>
 *
 * Additionally, <code>useICU4J</code> (with <code>true</code> value)
 * forces validator to use <a href="http://oss.software.ibm.com/icu4j/">IBM ICU4J</a>
 * library, which provides replacement for J2SE number format classes.
 *
 * @author Tomasz Pik, AIS.PL
 * @author Wojciech Swiatek, AIS.PL
 */
public class BigDecimalValidator extends NumberValidator {

  // Fields

  /** Controls <em>ICU4J</em> library usage. */
  private boolean useICU4J = false;

  // Constructors

  /**
   * Constructor for BigDecimalValidator.
   */
  public BigDecimalValidator() {
    super();
  }


  // Properties

  /**
   * Controls <em>ICU4J</em> library usage.
   *
   * @param useICEU4J <code>true</code> if <em>ICU4J</em> library should be used.
   */
  public void setUseICU4J(boolean useICEU4J) {
    this.useICU4J = useICEU4J;
  }

  /**
   * Gets status of <em>ICU4J</em> library usage.
   *
   * @return <code>true</code> if <em>ICU4J</em> library is used.
   */
  public boolean getUseICU4J() {
    return useICU4J;
  }


  // Protected check methods

  /**
   * @see RangeValidator#checkRange(Object)
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
   * @see StringValidator#convertObject(String)
   */
  protected Object convertObject(String value) {
    try {
      Number val = parseNumber(value);
      return NumberFormatHelper.convert(useICU4J, val);
    } catch (Exception ex) {
      ;
    }

    return null;
  }

  /**
   * @see StringValidator#formatString(String)
   */
  protected Object formatString(String value) throws Exception {
    Number val = parseNumber(value);
    return NumberFormatHelper.convert(useICU4J, val);
  }

  /**
   * @param value a string to be parsed.
   * @return number taken from the string.
   * @throws Exception if the string could not be parsed properly.
   */
  protected Number parseNumber(String value) throws Exception {
    ParsePosition pos = new ParsePosition(0);
    Number result = NumberFormatHelper.parse(getDecimalFormat(), value, pos);
    if (pos.getIndex() != value.length()) {
      throw new NumberFormatException("Invalid format.");
    }
    return result;
  }

  /**
   * @see NumberValidator#getDecimalFormat()
   */
  protected Format getDecimalFormat() {
    Format result = NumberFormatHelper.createInstance(useICU4J, (String) format.getValue(), locale);
    NumberFormatHelper.setMaximumFractionDigits(result, 10);
    format.setValue(NumberFormatHelper.toLocalizedPattern(result));
    return result;
  }
} // pl.aislib.util.validators.BigDecimalValidator class
