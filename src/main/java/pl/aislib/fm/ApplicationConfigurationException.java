package pl.aislib.fm;

import pl.aislib.lang.EncapsulatedException;

/**
 * Generic exception class throwed by {@link Application} initialization methods.
 * This exception encapsulates real problem cause description.
 *
 * @author Michal Jastak, AIS.PL
 * @since AISLIB 0.1
 */
public class ApplicationConfigurationException extends EncapsulatedException {

  // Constructors

  /**
   * Constructs a new exception.
   */
  public ApplicationConfigurationException () {
    super ();
  }

  /**
   * Constructs a new exception with the specified message.
   *
   * @param message Error message
   */
  public ApplicationConfigurationException (String message) {
    super (message);
  }

  /**
   * Wrap an existing exception.
   *
   * @param rootCause The exception to be wrapped
   */
  public ApplicationConfigurationException (Throwable rootCause) {
    super (rootCause);
  }

  /**
   * Constructs a new exception with specified message, wrapping an existing exception.
   *
   * @param message Error message
   * @param rootCause The exception to be wrapped
   */
  public ApplicationConfigurationException (String message, Throwable rootCause) {
    super (message, rootCause);
  }

} // ApplicationConfigurationException class
