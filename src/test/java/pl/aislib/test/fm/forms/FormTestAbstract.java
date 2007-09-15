package pl.aislib.test.fm.forms;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.SimpleLog;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import pl.aislib.fm.FmEntityResolver;
import pl.aislib.fm.Form;
import pl.aislib.fm.FormsHandler;
import pl.aislib.fm.MessagesHandler;
import pl.aislib.util.xml.XMLUtils;

/**
 * Abstract class for testing forms.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.8 $
 */
public abstract class FormTestAbstract extends TestCase implements IConstants {

  /**
   * Forms handler object.
   */
  protected FormsHandler forms;

  /**
   * Form being used.
   */
  protected Form form;

  /**
   * Values obtained from validation of the form.
   */
  protected Map values;

  /**
   * Messages handler object.
   */
  protected MessagesHandler messages;

  /**
   * Logging object.
   */
  protected Log log;

  /**
   * True if logging should be enabled.
   */
  protected boolean verbose = false;


  // Constructors

  /**
   * @see junit.framework.TestCase#TestCase(java.lang.String)
   */
  public FormTestAbstract(String name) throws Exception {
    super(name);

    log = new SimpleLog("form_validation");
    ((SimpleLog) log).setLevel(SimpleLog.LOG_LEVEL_DEBUG);

    forms = getForms(getFormsConfigurationFileName());
    messages = getMessages(getMessagesConfigurationFileName());
  }


  // Abstract methods

  /**
   * @return name of forms configuration file.
   */
  protected abstract String getFormsConfigurationFileName();

  /**
   * @return name of messages configuration file.
   */
  protected abstract String getMessagesConfigurationFileName();


  // Protected methods
  /**
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();

    values = new HashMap();
  }

  /**
   * @param formName name of a form.
   * @return <code>Form</code> object.
   */
  protected Form getForm(String formName) {
    return getForm(formName, "en-us");
  }

  /**
   * @param formName name of a form.
   * @param language language of the messages.
   * @return <code>Form</code> object.
   */
  protected Form getForm(String formName, String language) {
    return forms.getForm(formName, messages.cloneMessages(), messages.cloneMessageGroups(), language);
  }

  /**
   * @param values map of string values.
   * @return true if the form has been validated successfully.
   */
  protected boolean validateForm(Map values) {
    return form.validate(values);
  }

  /**
   * Prints values from validation of the form.
   */
  protected void printValues() {
    if (verbose) {
      MapUtils.verbosePrint(System.out, "Original values", form.getOriginalValues());
      MapUtils.verbosePrint(System.out, "Values", form.getValues());
    }
  }


  // Private methods

  /**
   * Reads forms from configuration file.
   *
   * @param xmlFileName name of forms configuration file.
   * @return <code>FormsHandler</code> object.
   * @throws IOException if an error occurs.
   * @throws SAXException if an error occurs.
   */
  private FormsHandler getForms(String xmlFileName) throws IOException, SAXException {
    InputStream stream = this.getClass().getResourceAsStream(xmlFileName);
    assertNotNull("config file: " + xmlFileName + " is null", stream);

    InputSource source = new InputSource(stream);
    source.setSystemId(this.getClass().getResource(xmlFileName).toString());

    XMLReader xmlReader = XMLUtils.newXMLReader(true, true);
    forms = new FormsHandler(log);
    xmlReader.setContentHandler(forms);
    xmlReader.setEntityResolver(FmEntityResolver.getResolverInstance());
    try {
    xmlReader.parse(source);
    } catch (SAXException saxe) {
      saxe.printStackTrace(System.out);
      throw saxe;
    }
    return forms;
  }

  /**
   * Reads messages from configuration file.
   *
   * @param xmlFileName name of messages configuration file.
   * @return <code>MessagesHandler</code> object.
   * @throws IOException if an error occurs.
   * @throws SAXException if an error occurs.
   */
  private MessagesHandler getMessages(String xmlFileName) throws IOException, SAXException {
    InputStream stream = this.getClass().getResourceAsStream(xmlFileName);
    assertNotNull("config file: " + xmlFileName + " is null", stream);

    InputSource source = new InputSource(stream);
    source.setSystemId(this.getClass().getResource(xmlFileName).toString());

    XMLReader xmlReader = XMLUtils.newXMLReader(true, true);
    messages = new MessagesHandler(log);
    xmlReader.setContentHandler(messages);
    xmlReader.setEntityResolver(FmEntityResolver.getResolverInstance());
    xmlReader.parse(source);
    return messages;
  }

} // FormTestAbstract class
