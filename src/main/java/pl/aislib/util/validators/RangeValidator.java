package pl.aislib.util.validators;

import pl.aislib.fm.forms.ValidateException;

/**
 * Range validation abstract class.
 * 
 * <p>
 * Implement additional properties:
 * <ul>
 *   <li><code>startRange</code></li>
 *   <li><code>endRange</code></li>
 * </ul>
 * 
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.3 $
 */
public abstract class RangeValidator extends StringValidator {

  /**
   * Start range property.
   */
  protected ConvertedProperty startRange;

  /**
   * End range property.
   */
  protected ConvertedProperty endRange;


  // Constructors
  
  /**
   * Base constructor.
   * 
   * @see StringValidator#StringValidator()
   */
  public RangeValidator() {
    super();

    startRange = new ConvertedProperty("startRange", 4, Property.DEFAULT, null, null);
    endRange   = new ConvertedProperty("endRange", 4, Property.DEFAULT, null, null);
  }


  // Public validation methods
  
  /**
   * @see pl.aislib.util.validators.Validator#validateObject(Object)
   */
  public void validateObject(Object value) throws ValidateException {
    super.validateObject(value);

    if ((checkProperty(startRange) || checkProperty(endRange)) && !checkRange(value)) {
      throw new ValidateException("Value not in range.");
    }
  }


  // Public property methods
    
  /**
   * Should be overridden in subclasses.
   * 
   * @param value a start range.
   */
  public void setStartRange(String value) {
    startRange.set(value, convertObject(value));
  }

  /**
   * Should be overridden in subclasses.
   * 
   * @param value an end range.
   */
  public void setEndRange(String value) {
    endRange.set(value, convertObject(value));
  }


  // Protected check methods

  /**
   * @param value an object.
   * @return true if the object is in valid range.
   */
  protected boolean checkRange(Object value) {
    return checkRange(value, true, true);
  }

  /**
   * Should be overridden in subclasses.
   * 
   * @param value an object.
   * @param minInclusive flag for checking lower bound of the range.
   * @param maxInclusive flag for checking upper bound of the range.
   * @return true if the object is in valid range.
   */
  protected boolean checkRange(Object value, boolean minInclusive, boolean maxInclusive) {
    return true;
  }

} // RangeValidator class.
