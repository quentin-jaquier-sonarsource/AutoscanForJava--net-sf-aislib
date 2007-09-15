package pl.aislib.lang;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Takes responsibility of <code>ClassLoader</code> related operations
 * and instantiating loaded classes.
 *
 * <p>Use two-steps strategy for loading classes and streams (URLs) with <code>ClassLoader</code>
 * methods. First it calls <code>ClassLoader</code> that loads this class, then
 * <code>Thread.currentThread().getContextClassLoader()</code>.</p>
 *
 * <p>Use <code>pl.aislib.lang.loader</code> log category with <code>TRACE</code>
 * level to report resource loading operations.</p>
 *
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.5 $
 * @since AISLIB 0.4
 */
public class Loader {

  /**
   * Singleton used in {@link #findResource(String)} method.
   */
  private static Loader singleton = new Loader();

  private static Log log = LogFactory.getLog("pl.aislib.lang.loader");

  /**
   * Returns the <code>Class</code> object associated with the class or interface with the given string name.
   *
   * @param className of <code>Class</code>.
   * @return the <code>Class</code> object for the class with the specified name.
   * @throws ClassNotFoundException if the <code>Class</code> cannot be found.
   * @throws NullPointerException if the <code>className</code> is <em>null</em>.
   */
  public static Class findClass(String className) throws ClassNotFoundException {
    if (className == null) {
      throw new NullPointerException("class name cannot be null");
    }
    Class result = null;
    ClassNotFoundException cnfe = null;
    try {
      try {
        result = Class.forName(className);
      } catch (ClassNotFoundException cnfe1) {
        result = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
      }
    } catch (ClassNotFoundException cnfe2) {
      cnfe = cnfe2;
    }
    if (result != null) {
      return result;
    } else {
      throw cnfe;
    }
  }

  /**
   * Load class with given name and instantiate instance of loaded class.
   *
   * @param className name of class.
   * @return instance of class with given name.
   * @throws ReflectionException if class cannot be loaded or instance cannot be created.
   */
  public static Object instantiate(String className) throws ReflectionException {
    try {
      Class clazz = findClass(className);
      Object result = clazz.newInstance();
      return result;
    } catch (ClassNotFoundException cnfe) {
      throw new ReflectionException(className, cnfe);
    } catch (IllegalAccessException iae) {
      throw new ReflectionException(className, iae);
    } catch (InstantiationException ie) {
      throw new ReflectionException(className, ie);
    }
  }

  /**
   * Load class with given name, instantiate instance of loaded class
   * and check, if result may be cased to <code>Class</code> defined
   * as <code>specification</code>.
   *
   * @param className name of class.
   * @param specification expected superclass or interface.
   * @return instance of class with given name.
   * @throws ReflectionException if class cannot be loaded, instance cannot be created
   *         or do not implement/extend given class.
   */
  public static Object instantiate(String className, Class specification) throws ReflectionException {
    try {
      Class clazz = findClass(className);
      Object result = clazz.newInstance();
      if (specification.isAssignableFrom(clazz)) {
        return result;
      } else {
        throw new ReflectionException(className, new ClassCastException(className + " is not a " + specification.getName()));
      }
    } catch (ClassNotFoundException cnfe) {
      throw new ReflectionException(className, cnfe);
    } catch (IllegalAccessException iae) {
      throw new ReflectionException(className, iae);
    } catch (InstantiationException ie) {
      throw new ReflectionException(className, ie);
    }
  }

  /**
   * Finds the resource with the given name.
   *
   * @param name the resource name.
   * @return a URL for reading the resource, or null  if the resource could not be found.
   */
  public static URL findResource(String name) {
    if (name == null) {
      return null;
    }
    URL result = singleton.getClassLoader().getResource(name);
    if (result == null) {
      result = ClassLoader.getSystemClassLoader().getResource(name);
    }
    if (result == null) {
      result = Thread.currentThread().getContextClassLoader().getResource(name);
    }
    if (log.isTraceEnabled()) {
      if (result == null) {
        log.trace("resource: " + name + " cannot be found");
      } else {
        log.trace("resource: " + name + " found");
      }
    }
    return result;
  }

  /**
   * Singleton.
   */
  private Loader() {
  }

  /**
   * Used in {@link #findResource(String)} method.
   *
   * @return ClassLoader associated with <code>Loader</code> class.
   */
  private ClassLoader getClassLoader() {
    return getClass().getClassLoader();
  }
}
