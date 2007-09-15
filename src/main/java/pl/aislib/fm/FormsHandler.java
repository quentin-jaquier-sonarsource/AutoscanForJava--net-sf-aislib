package pl.aislib.fm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import pl.aislib.fm.forms.config.Handler;

/**
 * XML handler for handling framework's forms.
 * 
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.2 $
 */
public class FormsHandler extends Handler {

  /**
   * Map of forms.
   */
  protected Map forms = new HashMap();

  /**
   * Form being used.
   */
  protected Form currentForm;


  // Constructors
  
  /**
   * @see pl.aislib.fm.forms.config.Handler#Handler(Log)
   */
  public FormsHandler(Log log) {
    super(log);
  }


  // Public methods

  /**
   * @param formName name of a form.
   * @param messages map of messages.
   * @param messageGroups map of message groups.
   * @param lang language of the form.
   * @return the form.
   */
  public Form getForm(String formName, Map messages, Map messageGroups, String lang) {
    Form form = (Form) forms.get(formName);
    try {
      Form result = (Form) form.clone();
      result.messages = messages;
      result.messageGroups = messageGroups;
      result.lang = lang;
      return result;
    } catch (CloneNotSupportedException cnse) {
      return null;
    }
  }

  /**
   * @see pl.aislib.fm.forms.config.IXMLHandler#processEndElement(String, String, String)
   */
  public void processEndElement(String namespaceURI, String localName, String qName) throws SAXException {
    if ("form".equals(localName)) {
      currentForm.setConditionalFields();
    }
  }

  /**
   * @see pl.aislib.fm.forms.config.IXMLHandler#processStartElement(String, String, String, Attributes)
   */
  public Object processStartElement(String namespaceURI, String localName, String qName, Attributes atts)
    throws SAXException {
    if ("form".equals(localName)) {
      String formName = atts.getValue("name");
      forms.put(formName, currentForm = new Form(formName));
      currentForm.setLog(log);
      return currentForm;
    }
    return localName;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();
    for (Iterator i = forms.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      String formName = (String) me.getKey();
      Form form = (Form) me.getValue();
      sb.append("Form: " + formName);
      sb.append("\n");
      sb.append("----------\n");
      sb.append(form);
      sb.append("\n");
    }
    return sb.toString();
  }


  // Protected methods

  /**
   * @see pl.aislib.fm.forms.config.Handler#createPartialHandlers()
   */
  protected void createPartialHandlers() {
    addPartialHandler("field", new FieldHandler(this));
    addPartialHandler("rule", new RuleHandler(this));
  }

} // FormsHandler class
