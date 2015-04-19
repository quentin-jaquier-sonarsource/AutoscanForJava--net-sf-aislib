package pl.aislib.lang;

/**
 * @author Tomasz Sandecki
 * @since AISLIB 0.3
 */
public class TaskProcessingException extends EncapsulatedException  {

 /**
  * Constructs a new exception.
  */
  public TaskProcessingException() {
    super();
  }

 /**
  * Constructs a new exception with the specified message.
  *
  * @param message Error message
  */
  public TaskProcessingException(String message) {
    super(message);
  }

 /**
  * Wrap an existing exception in TaskProcessException.
  *
  * @param rootCause The exception to be wrapped
  */
  public TaskProcessingException(Throwable rootCause) {
    super(rootCause);
  }

} // class
