package pl.aislib.fm.shepherds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Helper class used for testing HttpSession attributes.
 * @author Michal Jastak
 * @since AISLIB 0.4
 * @see Shepherd
 * @see ShepherdsDog
 */
public class DogWatchingHttpSessionAttributes extends AbstractShepherdsDog {

  /**
   * @return String identyfying this Dog ({@link ShepherdsDog#SESSION_ATTR})
   */
  public String getBreed() {
    return SESSION_ATTR;
  }

  /**
   * Returns watched attribute for given HttpSession.
   * <p>Before you will call this method, you should set name of attribute which should be watched using
   * {@link #setWatchedProperty} method, if you forget to do this, you will generate beautiful
   * <code>IllegalStateException</code>.</p>
   * <p>Using this method with second argument other than {@link HttpServletRequest} or {@link HttpSession}
   * will also cause an error, this time <code>IllegalArgumentException</code>.</p>
   *
   * @param pasture Universe in which everything happens
   * @param sheep HttpServletRequest or HttpSession object which is observed by this one
   * @return value of watched attribute for <code>sheep</code> object
   */
  public Object mindTheSheep(Pasture pasture, Object sheep) {
    if (propertyName == null) {
      throw new IllegalStateException("Watched Property not initialized");
    }
    String expectedName = pasture.getName() + "." + propertyName;
    if (sheep instanceof HttpServletRequest) {
      HttpSession session = ((HttpServletRequest) sheep).getSession();
      if (session != null) {
        return session.getAttribute(expectedName);
      } else {
        return null;
      }
    }
    if (sheep instanceof HttpSession) {
      return ((HttpSession) sheep).getAttribute(expectedName);
    }
    throw new IllegalArgumentException("Invalid argument '" + sheep + "'");
  }

  /**
   * Describe Shepherd Dog
   *
   * @return String contains description of Dog
   */
  public String toString() {
    return "DogWatchingHttpSessionAttributes(" + propertyName + ")";
  }
} // class
