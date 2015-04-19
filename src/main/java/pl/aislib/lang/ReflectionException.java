package pl.aislib.lang;

import org.apache.commons.lang.exception.NestableException;

/**
 * Encapsulation of <em>reflection</em> exceptions.
 *
 * @author Tomasz Pik, AIS.PL
 * @since aislib 0.5.2
 */
public class ReflectionException extends NestableException {

  /**
   * Create new exception marking that class cannot be found.
   *
   * @param className name of class that cannot be found.
   * @param cnfe original exception.
   */
  public ReflectionException(String className, ClassNotFoundException cnfe) {
    super("Cannot find class with name: " + className, cnfe);
  }

  /**
   * Create new exception marking that class cannot be instantiated
   * due to insufficient access.
   *
   * @param className name of class that cannot be found.
   * @param iae original exception.
   */
  public ReflectionException(String className, IllegalAccessException iae) {
    super("Cannot create instance of class: " + className + " due to insufficient access", iae);
  }

  /**
   * Create new exception marking that class cannot be instantiated
   * because is abstract or an interface.
   *
   * @param className name of class that cannot be found.
   * @param ie original exception.
   */
  public ReflectionException(String className, InstantiationException ie) {
    super("Cannot create instance of class: " + className + " probably it is interface of class is abstract", ie);
  }

  /**
   * Create new exception marking that class with given name cannot be used
   * as specified class.
   *
   * @param className name of class that cannot be found.
   * @param cce original exception.
   */
  public ReflectionException(String className, ClassCastException cce) {
    super("Cannot cast instance of class: " + className, cce);
  }

}
