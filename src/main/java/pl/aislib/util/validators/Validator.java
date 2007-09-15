package pl.aislib.util.validators;

import pl.aislib.fm.forms.ValidateException;

/**
 * Class of objects validating single strings.
 * 
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.2 $
 */
public abstract class Validator extends AbstractValidator implements pl.aislib.fm.forms.Validator {

  // Public methods
  
  /**
   * @see pl.aislib.util.validators.AbstractValidator#validate(String)
   */
  public Object validate(String value) throws ValidateException {

    value = (String) super.validate(value);
    
    if (required.isFalse() && checkEmpty(value)) {
      return value;
    }
    
    // Validate against everything that concerns the value as a string
    validateString(value);

    // Try to convert the value to an object
    Object oValue = toObject(value);

    if (oValue == null && required.isFalse()) {
      throw new ValidateException("Invalid conversion.");
    }

    // Validate against possible object conditions
    if (oValue != null) {
      validateObject(oValue);
    }
    
    return oValue;
  }


  // Protected validation methods
  
  /**
   * Syntactic validation of a string.
   * 
   * Must be overridden in subclasses.
   * 
   * @param value a string to be syntactically validated.
   * @throws ValidateException if the string could not be validated.
   */
  protected abstract void validateString(String value) throws ValidateException;

  /**
   * Conversion of a string to a desired object.
   * 
   * Must be overridden in subclasses.
   * 
   * @param value a string to be converted.
   * @return a converted object.
   * @throws ValidateException if the string could not be converted.
   */
  protected abstract Object toObject(String value) throws ValidateException;

  /**
   * Semantic validation of a string as an object.
   * 
   * Must be overriden in subclasses.
   * 
   * @param value an object to be validated.
   * @throws ValidateException if the object could not be validated.
   */
  protected abstract void validateObject(Object value) throws ValidateException;


  // Protected methods  

  /**
   * @see pl.aislib.util.validators.AbstractValidator#checkEmpty(Object)
   */
  protected boolean checkEmpty(Object value) {
    if (value == null) {
      return false;
    }
    if (value instanceof String) {
      return ((String) value).trim().equals("");
    }
    return super.checkEmpty(value);
  }

} // Validator class
