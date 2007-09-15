package pl.aislib.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;

/**
 * Generic XML utils class.
 *
 * @author Tomasz Pik
 * @author Michal Jastak
 * @version $Revision: 1.4 $
 * @since AISLIB 0.1
 */
public class XMLUtils {

  /**
   * Creates {@link XMLReader}.
   *
   * @param validating specify is created readed should be validating.
   * @param namespaceAware specify if created reader should be namespace awared.
   * @return new XMLReader with specified validating/namespace related behaviour.
   * @exception SAXException exception if underlying JAXP implementation cannot provide specified reader.
   */
  public static XMLReader newXMLReader(boolean validating, boolean namespaceAware) throws SAXException {
    SAXParserFactory factory = null;
    SAXParser         parser = null;
    XMLReader         reader = null;
    try {
      factory = SAXParserFactory.newInstance();
      factory.setValidating(validating);
      factory.setNamespaceAware(namespaceAware);
      factory.setFeature("http://xml.org/sax/features/validation", validating);
      factory.setFeature("http://xml.org/sax/features/namespaces", namespaceAware);
      parser  = factory.newSAXParser();
    } catch (ParserConfigurationException pce) {
      throw new SAXException(pce.getMessage(), pce);
    }
    reader = parser.getXMLReader();
    return reader;
  }

  /**
   * Creates {@link InputSource}.
   *
   * Open stream from given <code>url</code> and create <code>InputSource</code>.
   * Sets <code>systemId</code> using path specified by <code>url</code>.
   * 
   * @param url represents resource that should be converted to <code>InputSource</code>.
   * @return InputSource with <code>systemId</code>.
   * @throws IOException if stream from <code>url</code> cannot be opened.
   * @throws NullPointerException if <code>url</code> is <em>null</em>.
   */
  public static InputSource createInputSource(URL url) throws IOException, NullPointerException {
    if (url == null) {
      throw new NullPointerException("cannot create InputSource using null");
    }
    InputStream stream = url.openStream();
    InputSource result = new InputSource(stream);
    result.setSystemId(url.toString());
    return result;
  }
 
} // class
