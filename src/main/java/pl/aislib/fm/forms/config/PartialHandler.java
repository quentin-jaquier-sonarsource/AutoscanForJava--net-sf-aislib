package pl.aislib.fm.forms.config;

import java.lang.reflect.InvocationTargetException;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pl.aislib.lang.Loader;

/**
 * Partial handler class.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.4 $
 */
public abstract class PartialHandler extends DefaultHandler {

  /**
   * Parent handler object of this handle.
   */
  protected Handler parentHandler;

  /**
   * Object being used.
   */
  protected Object currentObject;

  /**
   * Attributes being used.
   */
  protected Map currentAttributes;

  /**
   * Buffer being used.
   */
  protected StringBuffer currentBuffer;

  // Constructors

  /**
   * @param parentHandler parent XML handler.
   */
  public PartialHandler(Handler parentHandler) {
    this.parentHandler = parentHandler;
  }

  // Public methods

  /**
   * @see org.xml.sax.ContentHandler#characters(char[], int, int)
   */
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (currentBuffer != null) {
      currentBuffer.append(new String(ch, start, length));
    }
  }

  // Protected methods

  /**
   * @deprecated
   * @see Loader#findClass(String)
   */
  protected Object create(String className) throws ClassNotFoundException, InstantiationException,
    IllegalAccessException {
    return Loader.findClass(className).newInstance();
  }

  /**
   * @deprecated
   * @see Class#forName(String)
   */
  protected Object createObject(String className) throws SAXException {
    try {
      return create(className);
    } catch (ClassNotFoundException e) {
      throw new SAXException(e.getMessage());
    } catch (InstantiationException e) {
      throw new SAXException(e.getMessage());
    } catch (IllegalAccessException e) {
      throw new SAXException(e.getMessage());
    }
  }

  /**
   * @param atts <code>Attributes</code> object.
   * @return map of attributes.
   */
  protected Map attributesToMap(Attributes atts) {
    return parentHandler.attributesToMap(atts);
  }

  /**
   * @param obj bean object to populate properties with.
   * @param properties <code>Map</code> object.
   * @throws SAXException if an error occurs.
   */
  protected void populate(Object obj, Map properties) throws SAXException {
    try {
      BeanUtils.populate(obj, properties);
    } catch (IllegalAccessException iae) {
      throw new SAXException("Cannot populate " + obj + " with " + properties, iae);
    } catch (InvocationTargetException ite) {
      throw new SAXException("Cannot populate " + obj + " with " + properties, ite);
    }
  }

} // PartialHandler class
