package pl.aislib.text.html.attrs;

import pl.aislib.lang.Loader;

/**
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.1
 */
public class AbstractHTMLAttribute extends Object {

  /**
   *
   */
  protected String attributeName;

  /**
   *
   */
  protected Object attributeValue;

  /**
   *
   */
  protected Object defaultValue;

  /**
   *
   */
  protected Class implementation;

  /**
   *
   * @since AISLIB 0.2
   */
  protected boolean valueLocked;

  /**
   *
   */
  protected AbstractHTMLAttribute(String name, String implementingClass) throws ClassNotFoundException {
    attributeName = name;
    try {
      implementation = Loader.findClass(implementingClass);
    } catch (Exception e) {
      throw new ClassNotFoundException(implementingClass + " not found");
    }
    valueLocked = false;
  }

  /**
   *
   */
  protected void setDefaultValue(Object value) {
    defaultValue = value;
  }

  /**
   *
   */
  public void setValue(Object value) throws AttributeException {
    if (value == null) {
      throw new NullPointerException("Attribute value cannot be null");
    }
    if (valueLocked) {
      throw new AttributeValueReadOnlyException("Cannot change read only value for '" + attributeName + "'");
    }
    if (!implementation.isAssignableFrom(value.getClass())) {
      throw new ClassCastException("Wrong type of attribute value");
    }
    attributeValue = value;
  }

  /**
   *
   */
  public Object getValue() {
    return (attributeValue != null) ? attributeValue : defaultValue;
  }

  /**
   *
   */
  public String getName() {
    return attributeName;
  }

  /**
   *
   */
  public boolean hasBeenSet() {
    return (attributeValue != null);
  }

  /**
   *
   * @since AISLIB 0.2
   */
  public boolean isLocked() {
    return valueLocked;
  }

  /**
   *
   * @since AISLIB 0.2
   */
  public void lockValue() {
    valueLocked = true;
  }

  /**
   *
   */
  public String toString() {
    return new String(attributeName + "[" + implementation + "]: " + getValue());
  }

} // class
