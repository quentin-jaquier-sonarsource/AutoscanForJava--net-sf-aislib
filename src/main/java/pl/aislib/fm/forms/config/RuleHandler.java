package pl.aislib.fm.forms.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import pl.aislib.lang.Loader;
import pl.aislib.lang.ReflectionException;

/**
 * Base rule handler class.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public abstract class RuleHandler extends PartialHandler {

  /**
   * Rule being used.
   */
  protected IRule currentRule;

  /**
   * Properties being used.
   */
  protected Map currentProperties;

  /**
   * Mapping being used.
   */
  protected Map currentMapping;


  // Constructors

  /**
   * @see pl.aislib.fm.forms.config.PartialHandler#PartialHandler(Handler)
   */
  public RuleHandler(Handler parentHandler) {
    super(parentHandler);
  }


  // Public methods

  /**
   * @see org.xml.sax.ContentHandler#endElement(String, String, String)
   */
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if ("rule".equals(localName)) {
      addRule(currentRule, currentAttributes);
      return;
    }
  }

  /**
   * @see org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)
   */
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if ("rule".equals(localName)) {
      currentObject = currentRule = processRule(atts);
      return;
    }
    if ("property".equals(localName)) {
      currentProperties.put(atts.getValue("name"), atts.getValue("value"));
      currentObject = "property";
    }
    if ("mapping".equals(localName)) {
      currentMapping.put(atts.getValue("rule-param"), atts.getValue("field-name"));
    }
    currentObject = localName;
  }


  // Protected methods

  /**
   * @param atts <code>Attributes</code> object.
   * @return <code>IRule</code> object.
   * @throws SAXException if an error occurs while parsing.
   */
  protected IRule processRule(Attributes atts) throws SAXException {
    try {
      currentRule = (IRule) Loader.instantiate(atts.getValue("class"), IRule.class);
    } catch (ReflectionException re) {
      throw new SAXException(re.getMessage(), re);
    }
    currentAttributes = attributesToMap(atts);
    currentProperties = new LinkedHashMap();
    currentMapping = new LinkedHashMap();

    return currentRule;
  }


  // Abstract methods

  /**
   * @param rule <code>IRule</code> object.
   * @param atts map of attributes.
   * @throws SAXException if an error occurs.
   */

  protected abstract void addRule(IRule rule, Map atts) throws SAXException;

} // RuleHandler class
