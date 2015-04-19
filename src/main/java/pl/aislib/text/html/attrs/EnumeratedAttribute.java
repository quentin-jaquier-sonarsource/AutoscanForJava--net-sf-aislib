package pl.aislib.text.html.attrs;

import java.util.HashSet;
import java.util.Set;

/**
 * Class describing Attribute with enumerated allowed values.
 * @author Michal Jastak
 * @since AISLIB 0.1
 */
public class EnumeratedAttribute extends AbstractHTMLAttribute {

  /**
   * Set of values allowed for this attribute.
   */
  protected Set allowedValues;

  /**
   *
   */
  public EnumeratedAttribute(String name) throws ClassNotFoundException {
    super(name, "java.lang.Object");
    allowedValues = new HashSet();
  }

  /**
   * Adds new value to those allowed for this attribute.
   */
  public void addAllowedValue(Object value) {
    allowedValues.add(value);
  }

  /**
   * Checks if given value is allowed for this attribute.
   * @param value value which should be checked
   * @return true if it is allowed to set such a value, false otherwise
   */
  public boolean isAllowed(Object value) {
    return allowedValues.contains(value);
  }

  /**
   * Sets value for this attribute, checking if it is allowed first.
   * @param value new attribute value
   * @throws AttributeValueNotAllowedException if this value is not allowed
   */
  public void setValue(Object value) throws InvalidAttributeValueException {
    if (!isAllowed(value)) {
      throw new AttributeValueNotAllowedException("Attribute '" + attributeName + "' doesn't allow '" + value
                                                 + "' as value");
    }
    super.setValue(value);
  }

} // class
