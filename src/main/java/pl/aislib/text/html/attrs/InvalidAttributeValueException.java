package pl.aislib.text.html.attrs;

/**
 * @author Michal Jastak
 * @since AISLIB 0.1
 */
public class InvalidAttributeValueException extends AttributeException {

 /**
  * Constructs a new exception.
  */
  public InvalidAttributeValueException () {
    super ();
  }

 /**
  * Constructs a new exception.
  */
  public InvalidAttributeValueException (String message) {
    super(message);
  }

} // class
