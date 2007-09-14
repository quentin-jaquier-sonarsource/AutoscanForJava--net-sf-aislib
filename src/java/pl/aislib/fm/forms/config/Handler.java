package pl.aislib.fm.forms.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;
import org.apache.commons.logging.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Main XML Handler class.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.3 $
 */
public abstract class Handler extends DefaultHandler implements IXMLHandler {

  /**
   * Map of partial handlers.
   */
  protected Map partialHandlers = null;

  /**
   * Current partial handler.
   */
  protected PartialHandler currentPartialHandler = null;

  /**
   * Object being used.
   */
  protected Object currentObject = null;

  /**
   * Buffer being used.
   */
  protected StringBuffer currentBuffer = null;

  /**
   * Logging object.
   */
  protected Log log;


  // Constructors

  /**
   * @param log logging object.
   */
  public Handler(Log log) {
    this.log = log;

    partialHandlers = new HashMap();
    createPartialHandlers();
  }


  // Public methods

  /**
   * @see org.xml.sax.ContentHandler#characters(char[], int, int)
   */
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (currentPartialHandler == null) {
      if (currentBuffer != null) {
        currentBuffer.append(new String(ch, start, length));
      }
    } else {
      currentPartialHandler.characters(ch, start, length);
    }
  }

  /**
   * @see org.xml.sax.ContentHandler#endElement(String, String, String)
   */
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (currentPartialHandler == null) {
      processEndElement(namespaceURI, localName, qName);
    } else {
      PartialHandler handler = (PartialHandler) partialHandlers.get(localName);
      currentPartialHandler.endElement(namespaceURI, localName, qName);
      if (handler != null) {
        currentPartialHandler = null;
      }
    }
    currentObject = null;
  }

  /**
   * @see org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)
   */
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if (currentPartialHandler == null) {
      currentPartialHandler = (PartialHandler) partialHandlers.get(localName);
    }
    if (currentPartialHandler != null) {
      currentPartialHandler.startElement(namespaceURI, localName, qName, atts);
      currentObject = currentPartialHandler.currentObject;
    } else {
      currentObject = processStartElement(namespaceURI, localName, qName, atts);
    }
  }


  // Protected methods

  /**
   * @param key name of partial handler.
   * @param handler <code>PartialHandler</code> object.
   */
  protected void addPartialHandler(String key, PartialHandler handler) {
    partialHandlers.put(key, handler);
  }

  /**
   * @param atts <code>Attributes</code> object.
   * @return map of attributes.
   */
  protected Map attributesToMap(Attributes atts) {
    Map map = new SequencedHashMap();
    for (int i = 0, n = atts.getLength(); i < n; i++) {
      map.put(atts.getLocalName(i), atts.getValue(i));
    }
    return map;
  }

  /**
   * Creates partial handlers, if any.
   */
  protected void createPartialHandlers() {
  }

} // Handler class
