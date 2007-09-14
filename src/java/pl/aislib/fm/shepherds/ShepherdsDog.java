package pl.aislib.fm.shepherds;

import org.apache.commons.logging.Log;

/**
 * This interface should be implemented by helper classes used by <code>Shepherds</code>
 * for watching some properties of <code>sheeps</code>.
 * <p>Typical use of such a class:
 * <ul>
 *   <li>class implementing <code>ShepherdsDog</code> interface is instantiated by some
 *   {@link ShepherdsSchool} object, during training new {@link Shepherd} object, and
 *   passed to this new <code>Shepherd</code>, which remembers it for future use.
 *   <code>ShepherdsSchool</code> object also calls {@link #setWatchedProperty} method
 *   for newly instantiated <code>ShepherdsDog</code> instance to prepare it for use.</li>
 *   <li>when <code>Shepherd</code> is asked to mind some sheep (typical
 *   {@link javax.servlet.http.HttpServletRequest} object) he calls {@link #mindTheSheep} method
 *   of this interface, which should return value of watched property</li>
 *   <li>property value returned by {@link #mindTheSheep} method is the used by <code>Shepherd</code>
 *   to decide what should be done with <code>sheep</code></li>
 *
 * @author Michal Jastak
 * @version $Revision: 1.3 $
 * @since AISLIB 0.4
 * @see Shepherd
 * @see ShepherdsSchool
 */
public interface ShepherdsDog {

  /**
   * Constant identyfying Dogs watching <code>HttpSession</code> attributes.
   */
  public static final String SESSION_ATTR  = "session-attr";

  /**
   * Constant identyfying Dogs watching <code>HttpServletRequest</code> parameters.
   */
  public static final String REQUEST_PARAM = "request-param";

  /**
   * Constant identyfying Dogs watching <code>HttpServletRequest</code> attributes.
   */
  public static final String REQUEST_ATTR  = "request-attr";

  /**
   * Constant identyfying Dogs watching whole <code>HttpServletRequest</code>.
   */
  public static final String REQUEST = "request";


  /**
   * Returns unique code describing type of object implementing this interface.
   * <p>Value returned by this method may be one of the {@link #SESSION_ATTR}, {@link #REQUEST_PARAM} or
   * {@link #REQUEST_ATTR}, but is not limited to those three values. One and only limitation for value returned
   * by this method is that it should be unique.</p>
   * <p>This method is used by {@link ShepherdsSchool} when searching for appropriate
   * helper class while instatiating new {@link Shepherd}.</p>
   * @return unique code describing type of object implementing this interface
   */
  public String getBreed();

  /**
   * Returns value of property watched by this object for given <code>sheep</code> object.
   * <p>Before you will call this method, you should set name of parameter which should be watched
   * using {@link #setWatchedProperty} method.</p>
   * <dl>
   * <dt><b>Note:</b></dt>
   * <dd>This method should throw {@link IllegalStateException} if user has forgot to set watched property name
   *   before calling this method and {@link IllegalArgumentException} if this method implementation gets as
   *   a <code>sheep</code> argument object which cannot be used.</dd>
   * </dl>
   * @param pasture Universe in which everything happens
   * @param sheep Object which should be watched
   * @return value of watched property for given <code>sheep</code> object
   */
  public Object mindTheSheep(Pasture pasture, Object sheep);

  /**
   * Sets name of property which should be watched by this object.
   * @param propertyName name of property which should be watched by this object
   */
  public void setWatchedProperty(String propertyName);

  /**
   * Returns name of property which this object is watching.
   * @return name of property watched by this object
   */
  public String getWatchedProperty();

  /**
   * Sets log for dog.
   *
   * @param log to set.
   */
  public void setLog(Log log);

} // interface
