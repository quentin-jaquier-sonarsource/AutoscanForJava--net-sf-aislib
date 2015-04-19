package pl.aislib.text.html.attrs;

/**
 *
 * @author Michal Jastak
 * @since AISLIB 0.1
 */
public class AttributeValueNotAllowedException extends InvalidAttributeValueException {

 /**
  * Constructs a new exception.
  */
  public AttributeValueNotAllowedException () {
    super ();
  }

 /**
  * Constructs a new exception.
  */
  public AttributeValueNotAllowedException (String message) {
    super(message);
  }

} // class
