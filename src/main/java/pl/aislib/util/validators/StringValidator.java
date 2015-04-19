package pl.aislib.util.validators;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.aislib.fm.forms.ValidateException;

/**
 * Generic string validation class.
 *
 * <p>
 * Implemented additional properties:
 * <ul>
 *   <li><code>minimumLength</code></li>
 *   <li><code>maximumLength</code></li>
 *   <li><code>allowedChars</code></li>
 *   <li><code>disallowedChars</code></li>
 *   <li><code>allowedValues</code></li>
 *   <li><code>allowedValuesDelimiter</code></li>
 *   <li><code>disallowedValues</code></li>
 *   <li><code>disallowedValuesDelimiter</code></li>
 *   <li><code>ignoreCase</code></li>
 *   <li><code>format</code></li>
 *   <li><code>pattern</code></li>
 * </ul>
 *
 * <p>
 * Deprecated properties:
 * <ul>
 *   <li><code>regExp</code></li>
 * </ul>
 *
 * @author Wojciech Swiatek, AIS.PL
 * @since AISLIB 0.1
 */
public class StringValidator extends Validator {

  /**
   * Constant describing the case of using no pattern.
   */
  public static final String PATTERN_NOT_USED = "";

  /**
   * Constant describing the case of using default pattern for this type of validators.
   */
  public static final String PATTERN_DEFAULT = "default";

  /**
   * Constant describing the case of using pattern requiring some value.
   */
  public static final String PATTERN_REQUIRED = ".+";

  /**
   * Minimal length of a string property.
   */
  protected IntegerProperty minimumLength;

  /**
   * Maximal length of a string property.
   *
   * If it is set to negative number, values are not checked against this property.
   */
  protected IntegerProperty maximumLength;

  /**
   * Allowed characters in a string property.
   */
  protected Property allowedChars;

  /**
   * Disallowed characters in a string property.
   */
  protected Property disallowedChars;


  /**
   * Allowed values of a string property.
   */
  protected TokenizedProperty allowedValues;

  /**
   * Delimiters property for the string of allowed values.
   */
  protected Property allowedValuesDelimiter;


  /**
   * Disallowed values of a string property.
   */
  protected TokenizedProperty disallowedValues;

  /**
   * Delimiters property for the string of forbidden values.
   */
  protected Property disallowedValuesDelimiter;


  /**
   * Ignore case distinction property.
   */
  protected BooleanProperty ignoreCase;


  /**
   * String format property.
   */
  protected Property format;


  /**
   * Allow pattern property.
   *
   * Currently, standard regex library is used for patterns.
   */
  protected Property pattern;


  /**
   * Pattern object.
   */
  protected Pattern patternObject;


  // Constructors

  /**
   * Base constructor.
   */
  public StringValidator() {
    super();

    minimumLength             = new IntegerProperty("minimumLength", 1, Property.DEFAULT, 0);
    maximumLength             = new IntegerProperty("maximumLength", 1, Property.DEFAULT,  -1);
    allowedChars              = new Property("allowedChars", 1, Property.DEFAULT, null);
    disallowedChars           = new Property("disallowedChars", 1, Property.DEFAULT, null);
    allowedValues             = new TokenizedProperty("allowedValues", 3, Property.DEFAULT, null);
    allowedValuesDelimiter    = new Property("allowedValuesDelimiter", 3, Property.DEFAULT,  ", ");
    disallowedValues          = new TokenizedProperty("disallowedValues", 3, Property.DEFAULT, null);
    disallowedValuesDelimiter = new Property("disallowedValuesDelimiter", 3, Property.DEFAULT,  ", ");
    ignoreCase                = new BooleanProperty("ignoreCase", 2, Property.DEFAULT,  false);
    format                    = new Property("format", 2, Property.DEFAULT,  "");

    // Pattern initialization
    pattern                   = new Property("pattern", 2, Property.DEFAULT, PATTERN_NOT_USED);
    patternObject             = null;
  }


  // Public validation methods

  /**
   * @see pl.aislib.util.validators.Validator#validateString(String)
   */
  public void validateString(String value) throws ValidateException {
    if (patternObject != null) {
      validatePattern(value);
      return;
    }

    if (checkProperty(allowedChars) && !checkAllowedChars(value)) {
      throw new ValidateException("Value '" + value + "' contains disallowed characters.");
    }

    if (checkProperty(disallowedChars) && checkDisallowedChars(value)) {
      throw new ValidateException("Value '" + value + "' contains disallowed characters.");
    }

    if ((checkProperty(minimumLength) || checkProperty(maximumLength)) && !checkLength(value)) {
      throw new ValidateException("Length of value '" + value + "'is invalid.");
    }
  }

  /**
   * @see pl.aislib.util.validators.Validator#toObject(String)
   */
  public Object toObject(String value) throws ValidateException {
    String strValue = (checkProperty(ignoreCase) && ignoreCase.isTrue()) ? value.toUpperCase() : value;

    Object oValue = null;

    try {
      oValue = formatString(strValue);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (oValue != null || checkProperty(format)) {
        return oValue;
      }
    }

    return null;
  }

  /**
   * @see pl.aislib.util.validators.Validator#validateObject(Object)
   */
  public void validateObject(Object value) throws ValidateException {
    if (checkProperty(allowedValues) && !checkAllowedValues(value)) {
      throw new ValidateException("Value is not allowed.");
    }

    if (checkProperty(disallowedValues) && checkDisallowedValues(value)) {
      throw new ValidateException("Value is not allowed.");
    }
  }


  // Public property methods

  /**
   * @param value minimal length.
   */
  public void setMinimumLength(int value) {
    minimumLength.set(value);
  }

  /**
   * @param value maximal length.
   */
  public void setMaximumLength(int value) {
    maximumLength.set(value);
  }

  /**
   * @param value allowed characters.
   */
  public void setAllowedChars(String value) {
    allowedChars.set(value);
  }

  /**
   * @param value disallowed characters.
   */
  public void setDisallowedChars(String value) {
    disallowedChars.set(value);
  }

  /**
   * @param value allowed values.
   */
  public void setAllowedValues(String value) {
    setAllowedValuesOnly(value);

    allowedValues.set(value);
  }

  /**
   * @param value a delimiter for a string of allowed values.
   */
  public void setAllowedValuesDelimiter(String value) {
    if (allowedValues.getConvertedTokens() != null) {
      String[] saAllowedValues = tokenize(allowedValues.stringValue(), value);
      Object[] oaAllowedValues = convertValues(saAllowedValues);

      allowedValues.setTokens(saAllowedValues, oaAllowedValues);
    }

    allowedValuesDelimiter.set(value);
  }

  /**
   * @param value disallowed values.
   */
  public void setDisallowedValues(String value) {
    setDisallowedValuesOnly(value);

    disallowedValues.set(value);
  }

  /**
   * @param value a delimiter for a string of disallowed values.
   */
  public void setDisallowedValuesDelimiter(String value) {
    if (disallowedValues.getConvertedTokens() != null) {
      String[] saDisallowedValues = tokenize(disallowedValues.stringValue(), value);
      Object[] oaDisallowedValues = convertValues(saDisallowedValues);

      disallowedValues.setTokens(saDisallowedValues, oaDisallowedValues);
    }

    disallowedValuesDelimiter.set(value);
  }

  /**
   * @param value flag for setting case sensitiveness.
   */
  public void setIgnoreCase(boolean value) {
    String[] saAllowedValues = allowedValues.getTokens();
    String[] saDisallowedValues = disallowedValues.getTokens();

    allowedValues.setTokens(saAllowedValues, convertValues(saAllowedValues));
    disallowedValues.setTokens(saDisallowedValues, convertValues(saDisallowedValues));

    ignoreCase.set(value);
  }

  /**
   * @param value a format string.
   */
  public void setFormat(String value) {
    format.set(value);
  }


  // Public methods

  /**
   * @param values a string of values.
   * @param delimiter a delimiter in the string.
   * @return an array of delimited strings.
   */
  public static String[] tokenize(String values, String delimiter) {
    StringTokenizer st     = new StringTokenizer(values, delimiter);
    String[]        result = new String[st.countTokens()];

    for (int i = 0; st.hasMoreTokens(); i++) {
      result[i] = st.nextToken();
    }

    return result;
  }


  // Protected check methods

  /**
   * @param value a string to be checked against length.
   * @return true if the length of the string is valid.
   */
  protected boolean checkLength(String value) {
    return checkLength(value, true, true);
  }

  /**
   * @param value a string to be checked against length.
   * @param minInclusive flag for checking a lower bound of the length.
   * @param maxInclusive flag for checking an upper bound of the length.
   * @return true if the length of the string is valid.
   */
  protected boolean checkLength(String value, boolean minInclusive, boolean maxInclusive) {
    int l = value.length();

    int iMinimumLength = minimumLength.intValue();
    int iMaximumLength = maximumLength.intValue();

    if (!maximumLength.isSet()) {
      iMaximumLength = -1;
    }

    if (iMinimumLength >= 0) {
      if (minInclusive && l < iMinimumLength) {
        return false;
      }
      if (!minInclusive && l <= iMinimumLength) {
        return false;
      }
    }

    if (iMaximumLength >= 0) {
      if (maxInclusive && l > iMaximumLength) {
        return false;
      }
      if (!maxInclusive && l >= iMaximumLength) {
        return false;
      }
    }

    return true;
  }

  /**
   * @param value a string.
   * @return true if the string contains only allowed characters.
   */
  protected boolean checkAllowedChars(String value) {
    String strChars = (String) allowedChars.getValue();

    if (strChars == null) {
      return true;
    }

    if (ignoreCase.isTrue()) {
      value = value.toUpperCase();
      strChars = strChars.toUpperCase();
    }

    for (int i = 0, l = value.length(); i < l; i++) {
      if (strChars.indexOf(value.charAt(i)) == -1) {
        return false;
      }
    }

    return true;
  }

  /**
   * @param value a string.
   * @return true if the string does not contain disallowed characters.
   */
  protected boolean checkDisallowedChars(String value) {
    String strChars = (String) disallowedChars.getValue();

    if (strChars == null) {
      return false;
    }

    if (ignoreCase.isTrue()) {
      value = value.toUpperCase();
      strChars = strChars.toUpperCase();
    }

    for (int i = 0, l = value.length(); i < l; i++) {
      if (strChars.indexOf(value.charAt(i)) != -1) {
        return true;
      }
    }
    return false;
  }

  /**
   * @param value an object.
   * @return true if the object is one of the allowed values.
   */
  protected boolean checkAllowedValues(Object value) {
    Object[] oAllowedValues = allowedValues.getConvertedTokens();

    if (oAllowedValues == null) {
      return true;
    }

    for (int i = 0, l = oAllowedValues.length; i < l; i++) {
      if (value.equals(oAllowedValues[i])) {
        return true;
      }
    }

    return false;
  }

  /**
   * @param value an object.
   * @return true if the object is not any disallowed value.
   */
  protected boolean checkDisallowedValues(Object value) {
    Object[] oDisallowedValues = disallowedValues.getConvertedTokens();

    if (oDisallowedValues == null) {
      return false;
    }

    for (int i = 0, l = oDisallowedValues.length; i < l; i++) {
      if (value.equals(oDisallowedValues[i])) {
        return true;
      }
    }

    return false;
  }


  // Protected methods

  /**
   * @param value a string.
   * @return a converted string.
   */
  protected Object convertObject(String value) {
    return ignoreCase.isTrue() ? value.toUpperCase() : value;
  }

  /**
   * @param values an array of strings.
   * @return an array of converted strings.
   */
  protected Object[] convertValues(String[] values) {
    if (values == null) {
      return null;
    }

    int      l      = values.length;
    Object[] result = new Object[l];

    for (int i = 0; i < l; i++) {
      Object o = convertObject(values[i]);
      if (o == null) {
        return null;
      }
      result[i] = o;
    }

    return result;
  }

  /**
   * @param value a string.
   * @return a formatted object.
   * @throws Exception if there were errors in formatting the string.
   */
  protected Object formatString(String value)
    throws Exception {
    return value;
  }

  /**
   * @param value allowed values.
   */
  protected void setAllowedValuesOnly(String value) {
    String[] saAllowedValues = tokenize(value, (String) allowedValuesDelimiter.getValue());
    Object[] oaAllowedValues = convertValues(saAllowedValues);

    allowedValues.setTokens(saAllowedValues, oaAllowedValues);
  }

  /**
   * @param value allowed values.
   * @param tokenize flag for forcing delimiting the values.
   */
  protected void setAllowedValuesOnly(String value, boolean tokenize) {
    if (tokenize) {
      setAllowedValuesOnly(value);
    }

    allowedValues.setValue(value);
  }

  /**
   * @param value disallowed values.
   */
  protected void setDisallowedValuesOnly(String value) {
    String[] saDisallowedValues = tokenize(value, (String) disallowedValuesDelimiter.getValue());
    Object[] oaDisallowedValues = convertValues(saDisallowedValues);

    disallowedValues.setTokens(saDisallowedValues, oaDisallowedValues);
  }

  /**
   * @param value disallowed values.
   * @param tokenize flag for forcing delimiting the values.
   */
  protected void setDisallowedValuesOnly(String value, boolean tokenize) {
    if (tokenize) {
      setDisallowedValuesOnly(value);
    }

    disallowedValues.setValue(value);
  }


  // Pattern part

  /**
   * Validates value against pattern set up earlier.
   *
   * @param value value to be validated.
   * @throws ValidateException if value has not been successfully validated.
   */
  public void validatePattern(String value) throws ValidateException {
    if (patternObject != null) {
      Matcher matcher = patternObject.matcher(value);
      if (!matcher.matches()) {
        throw new ValidateException(
          "Value '" + value + "' has not been matched successfully against pattern."
        );
      }
    } else {
      throw new ValidateException(
        "Value '" + value + "' has not been matched because there is no pattern to match against."
      );
    }
  }

  /**
   * Sets pattern.
   *
   * Currently, regular expressions should be used.
   *
   * @param value pattern string.
   */
  public void setPattern(String value) {
    pattern.set(value);

    if (PATTERN_NOT_USED.equals(value)) {
      patternObject = null;
    } else if (PATTERN_DEFAULT.equals(value)) {
      patternObject = getDefaultPatternObject();
    } else {
      patternObject = Pattern.compile(value);
    }
  }

  /**
   * Gets default pattern for this type of validators.
   *
   * @return default <code>Pattern</code> object.
   */
  protected Pattern getDefaultPatternObject() {
    return null;
  }


  // Deprecated code

  /**
   * Constant describing no regular expression type.
   * @deprecated Use {@link StringValidator#PATTERN_NOT_USED} constant.
   */
  public static final String REG_EXP_NONE = "";

  /**
   * Constant describing default regular expression type.
   * @deprecated Use {@link StringValidator#PATTERN_DEFAULT} constant.
   */
  public static final String REG_EXP_DEFAULT = "default";

  /**
   * Constant describing default regular expression type.
   * @deprecated Use {@link StringValidator#PATTERN_REQUIRED} constant.
   */
  public static final String REG_EXP_REQUIRED = ".+";

  /**
   * @param value value to be validated against regular expression string.
   * @throws ValidateException if value has not been successfully validated.
   * @deprecated Use {@link StringValidator#validatePattern(String)} method.
   */
  public void validateRegExp(String value) throws ValidateException {
    validatePattern(value);
  }

  /**
   * @param value regular expression or <code>none</code> or <code>format</code>.
   * @deprecated Use {@link StringValidator#setPattern(String)} method.
   */
  public void setRegExp(String value) {
    setPattern(value);
  }

  /**
   * @return default regular expression string for this class of validators.
   * @deprecated Use {@link StringValidator#getDefaultPatternObject()} method that returns default pattern object.
   */
  protected String getDefaultRegExp() {
    Pattern pattern = getDefaultPatternObject();
    return pattern != null ? pattern.pattern() : PATTERN_NOT_USED;
  }

} // pl.aislib.util.validators.StringValidator class
