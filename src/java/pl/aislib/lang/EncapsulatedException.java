package pl.aislib.lang;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Generic exception class encapsulating exception describing real problem cause.
 *
 * @author Michal Jastak
 * @version $Revision: 1.2 $
 * @since AISLIB 0.1
 */
public class EncapsulatedException extends Exception {

  private Throwable rootCause;

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
    super();
    this.rootCause = rootCause;
  }

  /**
   * Constructs a new exception with specified message, wrapping an existing exception in EncapsulatedException.
   *
   * @param message desciption of problem
   * @param rootCause The exception to be wrapped
   */
  public EncapsulatedException(String message, Throwable rootCause) {
    super(message);
    this.rootCause = rootCause;
  }

  /**
   * Returns a detail message for this exception.
   * If there is an embedded exception, and if the EncapsulatedException has no detail message of its own,
   * this method will return the detail message from the embedded exception.
   * @return error message
   */
  public String getMessage () {
    String result = super.getMessage();
    if (result != null) {
      return result;
    }
    if (rootCause != null) {
      return rootCause.getMessage();
    }
    return null;
  }

  /**
   * Returns exception wrapped inside of this class.
   *
   * @return exception wrapped inside of this class
   */
  public Throwable getRootCause() {
    return rootCause;
  }

  /**
   * @see Throwable#printStackTrace()
   */
  public void printStackTrace() {
    super.printStackTrace();
    if (rootCause != null) {
      System.err.println("Caused by: " + rootCause);
      rootCause.printStackTrace();
    }
  }

  /**
   * @see Throwable#printStackTrace(PrintStream)
   */
  public void printStackTrace(PrintStream stream) {
    super.printStackTrace(stream);
    if (rootCause != null) {
      stream.println("Caused by: " + rootCause);
      rootCause.printStackTrace(stream);
    }
  }

  /**
   * @see Throwable#printStackTrace(PrintWriter)
   */
  public void printStackTrace(PrintWriter writer) {
    super.printStackTrace(writer);
    if (rootCause != null) {
      writer.println("Caused by: " + rootCause);
      rootCause.printStackTrace();
    }
  }
} // class
