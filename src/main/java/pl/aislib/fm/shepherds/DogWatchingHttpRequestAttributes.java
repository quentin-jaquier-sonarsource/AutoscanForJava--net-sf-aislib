package pl.aislib.fm.shepherds;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper class used for testing HttpServletRequest attributes.
 * @author Michal Jastak
 * @since AISLIB 0.4
 * @see Shepherd
 * @see ShepherdsDog
 */
public class DogWatchingHttpRequestAttributes extends AbstractShepherdsDog {

  /**
   * @return String identyfying this Dog ({@link ShepherdsDog#REQUEST_ATTR})
   */
  public String getBreed() {
    return ShepherdsDog.REQUEST_ATTR;
  }

  /**
   * Returns watched attribute for given HttpServletRequest.
   * <p>Before you will call this method, you should set name of attribute which should be watched using
   * {@link #setWatchedProperty} method, if you forget to do this, you will generate beautiful
   * <code>IllegalStateException</code>.</p>
   * <p>Using this method with second argument other than HttpServletRequest will also cause an error,
   * this time <code>IllegalArgumentException</code>.</p>
   *
   * @param pasture Universe in which everything happens
   * @param sheep HttpServletRequest object which is observed by this one
   * @return value of watched attribute for <code>sheep</code> object
   */
  public Object mindTheSheep(Pasture pasture, Object sheep) {
    if (propertyName == null) {
      throw new IllegalStateException("Watched Property not initialized");
    }
    if (sheep instanceof HttpServletRequest) {
      return ((HttpServletRequest) sheep).getAttribute(propertyName);
    }
    throw new IllegalArgumentException("Invalid argument '" + sheep + "'");
  }

  /**
   * Describe Shepherd Dog
   *
   * @return String contains description of Dog
   */
  public String toString() {
    return "DogWatchingHttpRequestAttributes(" + propertyName + ")";
  }
} // class
