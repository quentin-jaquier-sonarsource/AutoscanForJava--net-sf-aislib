package pl.aislib.fm;

import pl.aislib.lang.EncapsulatedException;

/**
 * Generic exception class throwed by {@link TemplateEngine} methods.
 * 
 * This exception encapsulates real problem cause description.
 *  
 * @author Michal Jastak, AIS.PL
 * @version $Revision: 1.2 $
 * @since AISLIB 0.1
 * @see TemplateEngine
 */
public class TemplateEngineException extends EncapsulatedException {

  // Constructors

  /**
   * Constructs a new exception.
   */
  public TemplateEngineException () {
    super ();
  }

  /**
   * Constructs a new exception with the specified message.
   *
   * @param message Error message.
   */
  public TemplateEngineException (String message) {
    super (message);
  }

  /**
   * Wrap an existing exception in a TemplateEngineException.
   *
   * @param rootCause The exception to be wrapped.
   */
  public TemplateEngineException (Throwable rootCause) {
    super (rootCause);
  }

  /**
   * Constructs a new exception with specified message, wrapping an existing exception in a TemplateEngineException.
   * 
   * @param message <code>String</code> object.
   * @param rootCause The exception to be wrapped.
   */
  public TemplateEngineException (String message, Throwable rootCause) {
    super (message, rootCause);
  }

} // TemplateEngineExecution class
