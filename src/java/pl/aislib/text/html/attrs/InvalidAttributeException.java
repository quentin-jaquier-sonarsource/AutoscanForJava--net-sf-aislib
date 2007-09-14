package pl.aislib.text.html.attrs;

/**
 *  
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
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
