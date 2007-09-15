package pl.aislib.text.html.attrs;

/**
 *  
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.1
 */
public class AttributeNotAllowedException extends AttributeException {

  private AttributesSet allowedAttributes;

  private String elementName;
  private String attrName;

 /**
  * Constructs a new exception.
  */
  public AttributeNotAllowedException (String elementName, String attrName) {
    super ();
    this.elementName = elementName;
    this.attrName    = attrName;
  }

 /**
  * Constructs a new exception.
  */
  public AttributeNotAllowedException (String elementName, String attrName, AttributesSet allowedAttributes) {
    this(elementName, attrName);
    this.allowedAttributes = allowedAttributes;
  }

 /**
  * Returns a detail message for this exception.
  * If there is an embedded exception, and if the EncapsulatedException has no detail message of its own, 
  * this method will return the detail message from the embedded exception.
  * @return error message
  */
  public String getMessage () {
    StringBuffer result = new StringBuffer();
    result.append("Element '" + elementName + "' doesn't have '" + attrName + "' attribute.");
    if (allowedAttributes != null) {
      result.append("\nAttributes allowed for this element follows: ");
      result.append(allowedAttributes.toString());
    }
    return new String(result);
  }

} // class
