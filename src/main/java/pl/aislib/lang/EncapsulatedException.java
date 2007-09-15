package pl.aislib.lang;

/**
 * Generic exception class encapsulating exception describing real problem cause.
 *
 * @deprecated use standard <code>java.lang.Exception</code>.
 * @author Michal Jastak
 * @version $Revision: 1.3 $
 * @since AISLIB 0.1
 */
public class EncapsulatedException extends Exception {

  /**
   * Constructs a new exception.
   */
  public EncapsulatedException () {
    super ();
  }

 /**
  * Constructs a new exception with the specified message.
  *
  * @param message Error message
  */
  public EncapsulatedException(String message) {
    super(message);
  }

  /**
   * Wrap an existing exception in EncapsulatedException.
   *
   * @param rootCause The exception to be wrapped
   */
  public EncapsulatedException(Throwable rootCause) {
    super(rootCause);
  }

  /**
   * Constructs a new exception with specified message, wrapping an existing exception in EncapsulatedException.
   *
   * @param message desciption of problem
   * @param rootCause The exception to be wrapped
   */
  public EncapsulatedException(String message, Throwable rootCause) {
    super(message, rootCause);
  }

  /**
   * Returns exception wrapped inside of this class.
   *
   * @return exception wrapped inside of this class
   */
  public Throwable getRootCause() {
    return super.getCause();
  }

} // class
