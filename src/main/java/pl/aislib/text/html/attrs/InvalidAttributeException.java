package pl.aislib.text.html.attrs;

/**
 *
 * @author Michal Jastak
 * @since AISLIB 0.1
 */
public class InvalidAttributeException extends AttributeException {

 /**
  * Constructs a new exception.
  */
  public InvalidAttributeException () {
    super ();
  }

 /**
  * Constructs a new exception.
  */
  public InvalidAttributeException (String message) {
    super(message);
  }

} // class
