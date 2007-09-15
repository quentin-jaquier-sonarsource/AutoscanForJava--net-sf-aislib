package pl.aislib.fm.forms.config;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * XML handler interface.
 * 
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.2 $
 */
public interface IXMLHandler {

  /**
   * @see org.xml.sax.ContentHandler#endElement(String, String, String)
   */
  void processEndElement(String namespaceURI, String localName, String qName) throws SAXException;

  /**
   * @see org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)
   */
  Object processStartElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException;

} // IXMLHandler interface
