package pl.aislib.text.html;

import pl.aislib.text.html.attrs.AbstractHTMLAttribute;
import pl.aislib.text.html.attrs.AttributeNotAllowedException;
import pl.aislib.text.html.attrs.AttributesSet;
import pl.aislib.text.html.attrs.InvalidAttributeException;
import pl.aislib.text.html.attrs.InvalidAttributeValueException;
import pl.aislib.util.xml.XHtmlOutputter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Core class for all HTML objects.
 * @author Michal Jastak
 * @since AISLIB 0.1
 */
public class AbstractHTMLObject extends Object {

  /**
   * Set of attributes allowed for this element.
   */
  protected AttributesSet attrs;

  /**
   * Descendants of this element.
   */
  protected List content;

  /**
   * HTML Element name. Should be overwritten by extending class.
   */
  protected String htmlName;

  /**
   * HTML formatting directive. Defaults to <code>false</code>.
   * @since AISLIB 0.2
   */
  protected boolean keepWithPrevious;

  /**
   * HTML formatting directive. Defaults to <code>false</code>.
   * @since AISLIB 0.2
   */
  protected boolean keepWithNext;

  /**
   *
   */
  protected AbstractHTMLObject() {
    super();
    attrs   = new AttributesSet();
    content = new LinkedList();
    keepWithPrevious = false;
    keepWithNext     = false;
  }

  /**
   *
   */
  public AbstractHTMLObject(String htmlName) {
    this();
    this.htmlName = htmlName;
  }

  /**
   *
   */
  protected void addAttributesSet(AttributesSet newAttrs) {
    attrs.addAll(newAttrs);
  }

  /**
   * Adds attribute to set of allowed attributes.
   * @since AISLIB 0.2
   */
  protected void addAttribute(AbstractHTMLAttribute newAttr) {
    attrs.add(newAttr);
  }

  /**
   * Gets attribute description.
   * <dl>
   *   <dt><b>Note:</b></dt>
   *   <dd>if you have any code using AISLIB 0.1 method with same name as this one, please rewrite code,
   *   it should use {@link #getAttributeValue} method right now, sorry for any inconvenience</dd>
   * </dl>
   * @since AISLIB 0.2
   */
  protected AbstractHTMLAttribute getAttribute(String attrName) {
    return attrs.get(attrName);
  }

  /**
   * @since AISLIB 0.2
   */
  protected void removeAttribute(String attrName) {
    attrs.remove(attrName);
  }

  /**
   * Sets attribute value.
   * <dl>
   *   <dt><b>Note:</b></dt>
   *   <dd>Deprecated since AISLIB 0.2, please use {@link #setAttributeValue} instead.</dd>
   * </dl>
   * @param name Attribute name
   * @param value New Attribute value
   * @deprecated
   */
  public void setAttribute(String name, Object value)
         throws AttributeNotAllowedException, InvalidAttributeValueException {
    setAttributeValue(name, value);
  }

  /**
   * Sets attribute value.
   * @since AISLIB 0.2
   */
  public void setAttributeValue(String name, Object value)
         throws AttributeNotAllowedException, InvalidAttributeValueException {
    if (!attrs.contains(name)) {
      throw new AttributeNotAllowedException(htmlName, name, attrs);
    }
    attrs.setAttribute(name, value);
  }

  /**
   * Checks if attribute given as argument has been set for this object.
   */
  public boolean hasBeenSet(String name) throws InvalidAttributeException {
    return attrs.hasBeenSet(name);
  }

  /**
   * Gets attribute value.
   * <dl>
   *   <dt><b>Note:</b></dt>
   *   <dd>This method has been renamed in AISLIB 0.2 from previous <code>getAttribute</code></dd>
   * </dl>
   * @since AISLIB 0.2
   */
  public Object getAttributeValue(String name) throws AttributeNotAllowedException {
    if (!attrs.contains(name)) {
      throw new AttributeNotAllowedException(htmlName, name, attrs);
    }
    return attrs.getAttribute(name);
  }

  /**
   * Checks if this object has given attribute.
   * @since AISLIB 0.2
   * @param name Attribute name
   * @return <code>true</code> if attribute is allowed for this object, <code>false</code> otherwise
   */
  public boolean hasAttribute(String name) {
    return attrs.contains(name);
  }

  /**
   * @since AISLIB 0.2
   */
  public boolean getKeepWithPrevious() {
    return keepWithPrevious;
  }

  /**
   * @since AISLIB 0.2
   */
  public boolean getKeepWithNext() {
    return keepWithNext;
  }

  /**
   * @since AISLIB 0.2
   */
  public void setKeepWithPrevious(boolean value) {
    keepWithPrevious = value;
  }

  /**
   * @since AISLIB 0.2
   */
  public void setKeepWithNext(boolean value) {
    keepWithNext = value;
  }

  /**
   *
   */
  public Iterator attributeNamesIterator() {
    return attrs.attributeNamesIterator();
  }

  /**
   *
   */
  public void addContent(AbstractHTMLObject child) {
    content.add(child);
  }

  /**
   *
   */
  public Iterator contentIterator() {
    return content.iterator();
  }

  /**
   * Used internally to reconstruct XML tree.
   */
  protected Element toXML(Element parent) {
    Element result = new Element(htmlName);
    List attrsList = new LinkedList();

    for (Iterator it = attributeNamesIterator(); it.hasNext();) {
      String attrName  = (String) it.next();
      AbstractHTMLAttribute attr = (AbstractHTMLAttribute) attrs.get(attrName);
      Object value = attr.getValue();
      if (value != null) {
        attrsList.add(new Attribute(attrName, value.toString()));
      }
    }

    if (keepWithPrevious) {
      attrsList.add(new Attribute("keep-with-previous", "true",
                                  Namespace.getNamespace("ais-xhtml-fo", "ais-xhtml-fo")));
    }

    if (keepWithNext) {
      attrsList.add(new Attribute("keep-with-next", "true",
                                  Namespace.getNamespace("ais-xhtml-fo", "ais-xhtml-fo")));
    }

    result.setAttributes(attrsList);

    for (Iterator it = contentIterator(); it.hasNext();) {
      AbstractHTMLObject child = (AbstractHTMLObject) it.next();
      Element content = child.toXML(result);
      if (content != null) {
        result.addContent(content);
      }
    }

    return result;
  }

  /**
   * Reconstructs XML tree using this element as root.
   */
  public Element toXML() {
    return toXML(null);
  }

  /**
   * Returns String representation of this object.
   * <p>Such a representation is created using two steps:
   *   <ul>
   *     <li>reconstructing XML tree using {@link #toXML()} method</li>
   *     <li>writing this tree to String using XMLOutputter</li>
   *   </ul>
   * </p>
   * <p><i>Note:</i><br />Since AISLIB 0.2: Lines are separated using value returned by
   * <code>System.getProperty(String)</code> method, called with "line.separator" as argument.</p>
   */
  public String toString() {
    XHtmlOutputter xHtmlOutputter = new XHtmlOutputter();
    String result = null;
    try {
      result = xHtmlOutputter.outputString(toXML());
    } catch (Exception ex) { ; }
    return result;
  }

} // class
