package pl.aislib.util.validators;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class of objects validating array of strings.
 *
 * <p>
 * Implemented additional properties:
 * <ul>
 *   <li><code>minimumLength</code></li>
 *   <li><code>maximumLength</code></li>
 * </ul>
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class ArrayValidator extends AbstractValidator implements pl.aislib.fm.forms.ArrayValidator {

  /**
   * Minimal length property.
   */
  protected IntegerProperty minimumLength;

  /**
   * Maximal length property.
   *
   * If it is set to negative number, values are not checked against this property.
   */
  protected IntegerProperty maximumLength;


  // Constructors

  /**
   * @see AbstractValidator#AbstractValidator()
   */
  public ArrayValidator() {
    super();

    minimumLength = new IntegerProperty("minimumLength", 1, Property.DEFAULT, 0);
    maximumLength = new IntegerProperty("maximumLength", 1, Property.DEFAULT, -1);
  }


  // Public methods

  /**
   * @see pl.aislib.util.validators.AbstractValidator#validate(String[])
   */
  public Object[] validate(String[] values) throws ValidateException {
    values = (String[]) super.validate(values);

    // Validate against everything that concerns the value as an array of strings
    validateStrings(values);

    // Try to convert the value to an array of objects
    Object[] oValues = toObject(values);

    if (oValues == null && required.isFalse()) {
      throw new ValidateException("Invalid conversion.");
    }

    // Validate against possible conditions for the array
    if (oValues != null) {
      validateObjects(oValues);
    }

    return oValues;
  }


  // Protected validation methods

  /**
   * Syntactic validation of an array of strings.
   *
   * The array is checked against valid length.
   *
   * @param values an array of strings to be syntactically validated.
   * @throws ValidateException if the array could not be validated.
   */
  protected void validateStrings(String[] values) throws ValidateException {
    if ((checkProperty(minimumLength) || checkProperty(maximumLength)) && !checkLength(values)) {
      throw new ValidateException("Length of array is invalid.");
    }
  }

  /**
   * Conversion of an array of strings to a desired array of objects.
   *
   * @param values an array of strings to be converted.
   * @return a converted array of objects.
   * @throws ValidateException if the array could not be converted.
   */
  protected Object[] toObject(String[] values) throws ValidateException {
    return values;
  }

  /**
   * Semantic validation of an array of objects.
   *
   * Can be overriden in subclasses.
   *
   * @param values an array of objects to be validated.
   * @throws ValidateException if the array could not be validated.
   */
  protected void validateObjects(Object[] values) throws ValidateException {
  }


  // Public property methods

  /**
   * @param value minimal length of an array.
   */
  public void setMinimumLength(int value) {
    minimumLength.set(value);
  }

  /**
   * @param value maximal length of an array.
   */
  public void setMaximumLength(int value) {
    maximumLength.set(value);
  }


  // Protected check methods

  /**
   * @param values an array of objects.
   * @return true if length of the array is valid.
   */
  protected boolean checkLength(Object[] values) {
    return checkLength(values, true, true);
  }

  /**
   * @param values an array of objects.
   * @param minInclusive flag for checking the minimal length.
   * @param maxInclusive flag for checking the maximal length.
   * @return true if length of the array is valid.
   */
  protected boolean checkLength(Object[] values, boolean minInclusive, boolean maxInclusive) {
    int l = values.length;

    int iMinimumLength = minimumLength.intValue();
    int iMaximumLength = maximumLength.intValue();

    if (!maximumLength.isSet()) {
      iMaximumLength = -1;
    }

    if (iMinimumLength >= 0) {
      if (minInclusive && (l < iMinimumLength)) {
        return false;
      }
      if ((!minInclusive) && (l <= iMinimumLength)) {
        return false;
      }
    }
    if (iMaximumLength >= 0) {
      if (maxInclusive && (l > iMaximumLength)) {
        return false;
      }
      if ((!maxInclusive) && (l >= iMaximumLength)) {
        return false;
      }
    }
    return true;
  }

} // ArrayValidator class
