package pl.aislib.text.html.attrs;

/**
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.2
 */
public class AttributeValueReadOnlyException extends AttributeException {

 /**
  * Constructs a new exception.
  */
  public AttributeValueReadOnlyException() {
    super();
  }

 /**
  * Constructs a new exception.
  */
  public AttributeValueReadOnlyException(String message) {
    super(message);
  }

} // class
