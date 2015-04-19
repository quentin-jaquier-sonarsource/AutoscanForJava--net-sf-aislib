package pl.aislib.text.html.attrs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class holding set of attributes.
 * @author Michal Jastak
 * @since AISLIB 0.1
 * @see AbstractHTMLAttribute
 */
public class AttributesSet extends Object {

  /**
   * Holds attributes within this AttributesSet.
   */
  protected Map attrs;

  /**
   * Constructor.
   */
  public AttributesSet() {
    attrs = new HashMap();
  }

  /**
   * Adds given attribute to this set.
   * @param attribute Attribute which should be added to this set
   */
  public void add(AbstractHTMLAttribute attribute) {
    attrs.put(attribute.getName(), attribute);
  }

  /**
   * Removes given attribute from this set.
   * @since AISLIB 0.2
   * @param name Attribute which should be removed from this set
   */
  public void remove(String name) {
    attrs.remove(name);
  }

  /**
   * Returns Attribute description.
   * @param name Name of Attribute, which description we want to get
   * @return Description of Attribute
   */
  public AbstractHTMLAttribute get(String name) {
    return (AbstractHTMLAttribute) attrs.get(name);
  }

  /**
   * Checks if this set contains Attribute with name given as parameter.
   * @param name Attribute name
   * @return <code>true</code> if this set contains attribute with given name, <code>false</code> otherwise
   */
  public boolean contains(String name) {
    return attrs.containsKey(name);
  }

  /**
   *
   */
  public Iterator attributeNamesIterator() {
    return attrs.keySet().iterator();
  }

  /**
   *
   */
  public void addAll(AttributesSet newAttrs) {
    for (Iterator it = newAttrs.attributeNamesIterator(); it.hasNext();) {
      String attrName = (String) it.next();
      attrs.put(attrName, newAttrs.get(attrName));
    }
  }

  /**
   * Sets attribute value.
   */
  public void setAttribute(String name, Object value) throws InvalidAttributeValueException {
    AbstractHTMLAttribute attribute = get(name);
    attribute.setValue(value);
  }

  /**
   * Gets attribute value.
   */
  public Object getAttribute(String name) {
    AbstractHTMLAttribute attribute = get(name);
    return attribute.getValue();
  }

  /**
   *
   */
  public boolean hasBeenSet(String name) throws InvalidAttributeException {
    if (!contains(name)) {
      throw new InvalidAttributeException();
    }
    AbstractHTMLAttribute attr = get(name);
    return attr.hasBeenSet();
  }

  /**
   *
   */
  public String toString() {
    StringBuffer result = new StringBuffer();
    for (Iterator it = attributeNamesIterator(); it.hasNext();) {
      String attributeName = (String) it.next();
      AbstractHTMLAttribute attribute = (AbstractHTMLAttribute) attrs.get(attributeName);
      if (result.length() > 0) {
        result.append(", ");
      }
      result.append(attribute.toString());
    }
    return new String(result);
  }

} // class
