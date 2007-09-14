package pl.aislib.fm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;

import pl.aislib.fm.shepherds.Pasture;
import pl.aislib.fm.shepherds.Shepherd;
import pl.aislib.fm.shepherds.ShepherdsSchool;
import pl.aislib.lang.Loader;
import pl.aislib.util.predicates.FalsePredicate;
import pl.aislib.util.predicates.servlet.IsNewHttpSessionPredicate;

/**
 * Describes Flow between Application pages.
 *
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Michal Jastak, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.12 $
 * @since AISLIB 0.1
 *
 * @see Application
 * @see Page
 * @see PageInfo
 * @see <a href='http://jakarta.apache.org/commons/logging.html'>Jakarta Commons Logging Component</a>
 */
public class Workflow extends DefaultHandler {

  /**
   * Constant: session-expiration.
   */
  private static final String SESSION_EXPIRATION = "session-expiration";

  /**
   * Constant: flow.
   */
  private static final String FLOW = "flow";


  /**
   * Holds application pages descriptions ({@link PageInfo} objects).
   */
  protected Map pages;

  /**
   * List of shepherds.
   */
  protected List workflowShepherds;

  /**
   * Map of properties of template container.
   */
  protected Map containerProperties;

  /**
   * Map of properties of page.
   */
  protected Map pageProperties;

  /**
   * List of properties.
   */
  protected PropertyList propertyList;

  /**
   * Logging object.
   */
  protected Log log;

  /**
   * Name of template container.
   */
  private String   tContainerName;

  /**
   * Name of slot.
   */
  private String   tContainerSlot;

  /**
   * Page information object.
   */
  private PageInfo tPage;

  /**
   * Name of start page of the application.
   */
  private String startPage = "index";

  /**
   * Session predicate object.
   */
  private Predicate sessionPredicate;


  // Constructors

  /**
   * Main constructor.
   *
   * Initializes internal structures: logging object and description of application pages.
   *
   * @param log Logging Component to use
   */
  public Workflow(Log log) {
    pages    = new HashMap();
    workflowShepherds = new ArrayList();
    this.log = log;
  }


  // Public methods

  /**
   * Returns Application Page description for specified page.
   *
   * @param actionKey key identifying page
   * @return page description encapsulated in {@link PageInfo} class
   *          or null, if the requested page does not exist.
   * @see PageInfo
   * @see Page
   */
  public PageInfo getPageInfo(String actionKey) {
    return (PageInfo) pages.get(actionKey);
  }

  /**
   * @param pasture <code>Pasture</code> object.
   * @param sheep <code>Object</code> implemented as sheep.
   * @return <code>String</code> object.
   */
  String resolveGlobalTriggers(Pasture pasture, Object sheep) {
    return ShepherdsSchool.examine(workflowShepherds, pasture, sheep, null);
  }

  /**
   * Returns Application Welome Page (defined as <code>index-page</code>
   * attribute in <code>workflow</code> element.
   *
   * @return PageInfo describing Page, which is declared as <code>index-page</code>.
   */
  public PageInfo getStartPageInfo() {
    return getPageInfo(startPage);
  }

  /**
   * Returns session status (expiration).
   *
   * @param session to be checked.
   * @return <code>true</code> if session expired, <code>false</code> otherwise.
   */
  public boolean isNewSession(HttpSession session) {
    return sessionPredicate.evaluate(session);
  }

  /**
   * Returns list of useable DTD for {@link Workflow} configuration files.
   *
   * Those DTD's are described by their public ids and sorted descending
   * starting from the newest one.
   *
   * @return List containing one DTD public id (<code>-//AIS.PL//DTD Workflow Description 0.2//EN</code>.
   */
  public List getPublicIds() {
    LinkedList result = new LinkedList();
    result.add("-//AIS.PL//DTD Workflow Description 0.2//EN");
    return result;
  }

  /**
   * Receives notification of the beginning of an element.
   *
   * @see org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)
   */
  public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs)
    throws SAXException {

    if ("workflow".equals(localName)) {
      if (attrs.getValue("start-page") != null) {
        startPage = attrs.getValue("start-page");
      }
      sessionPredicate = createSessionPredicate(attrs.getValue("session-expiration-check"));
      return;
    }

    if ("template-container".equals (localName)) {
      tContainerName = attrs.getValue ("templateName");
      tContainerSlot = attrs.getValue ("slot");
      containerProperties = new HashMap();
      pageProperties      = null;
      return;
    }

    if ("property".equals (localName)) {
      String propertyName  = attrs.getValue("name");
      Object propertyValue = attrs.getValue("value");
      String propertyClass = attrs.getValue("class");

      if (propertyClass != null) {
        Class clazz = null;
        try {
          clazz = Loader.findClass(propertyClass);
        } catch (ClassNotFoundException cnfe) {
          String message = "Property class '" + propertyClass + "' cannot be loaded";
          log.fatal(message, cnfe);
          throw new SAXException(message, cnfe);
        }
        try {
          propertyValue = ConvertUtils.convert((String) propertyValue, clazz);
        } catch (ConversionException ce) {
          String message = "Cannot convert property with name: '" + propertyName + "'";
          log.fatal(message, ce);
          throw new SAXException(message, ce);
        }
      }

      if (pageProperties != null) {
        pageProperties.put(propertyName, propertyValue);
      } else {
        containerProperties.put(propertyName, propertyValue);
      }
    }

    if ("property-list".equals (localName)) {
      String propertyName  = attrs.getValue("name");
      String propertyClass = attrs.getValue("class");
      if (propertyClass == null) {
        propertyClass = "java.lang.String";
      }
      Class clazz = null;
      try {
        clazz = Loader.findClass(propertyClass);
      } catch (ClassNotFoundException cnfe) {
        String message = "Property class '" + propertyClass + "' cannot be loaded";
        log.fatal(message, cnfe);
        throw new SAXException(message, cnfe);
      }
      propertyList = new PropertyList(propertyName, clazz);
    }

    if ("list-item".equals (localName)) {
      String itemValue  = attrs.getValue("value");
      if ((propertyList != null) && (itemValue != null)) {
        try {
          propertyList.addValue(itemValue);
        } catch (ConversionException ce) {
          String message = "Cannot convert list item value : '" + itemValue + "'";
          if (ce.getCause() != null) {
            log.fatal(message, ce.getCause());
          } else {
            log.fatal(message, ce);
          }
          throw new SAXException(message, ce);
        }
      }
    }

    if ("page".equals (localName)) {
      tPage = new PageInfo(attrs.getValue("name"));
      if (pages.containsKey(tPage.getActionKey())) {
        String message = "'" + tPage.getActionKey() + "' is already registered";
        throw new SAXException(message);
      }
      tPage.setContainer(tContainerName, tContainerSlot);
      tPage.setTemplateName(attrs.getValue("template"));
      pageProperties = new HashMap();
      if (containerProperties != null) {
        pageProperties.putAll(containerProperties);
      }
      if ((attrs.getValue(SESSION_EXPIRATION) == null) || attrs.getValue(SESSION_EXPIRATION).equals("true")) {
        tPage.setSessionExpiration(true);
      } else {
        tPage.setSessionExpiration(false);
      }
      String pageClass = attrs.getValue("class");
      try {
        tPage.setPageClass(Loader.findClass(pageClass));
      } catch (ClassNotFoundException cnfe) {
        String message = "Page class '" + pageClass + "' cannot be loaded";
        log.fatal(message, cnfe);
        throw new SAXException(message, cnfe);
      }
      return;
    }

    if ("trigger".equals (localName)) {
      String pageRef       = attrs.getValue("page-ref");
      String triggerName   = attrs.getValue("name");
      String predicateName = attrs.getValue("predicate");
      String triggerType   = attrs.getValue("type");
      try {
        Shepherd shepherd = ShepherdsSchool.newShepherd(triggerName, pageRef, predicateName, triggerType, log);
        if (tPage != null) {
          tPage.addShepherd(shepherd);
        } else {
          workflowShepherds.add(shepherd);
        }
      } catch (Exception e) {
        log.fatal("cannot create trigger with predicate: " + predicateName, e);
      }
      return;
    }

    if (FLOW.equals(localName) && (tPage != null)) {
      String pageRef = attrs.getValue("page-ref");
      tPage.addFlow(pageRef);
      return;
    }

    if ("include".equals (localName)) {
      String includeName  = attrs.getValue("name");
      String templateName = attrs.getValue("template");
      String messageCode  = attrs.getValue("message-code");
      if ((templateName != null) && (messageCode != null)) {
        throw new SAXException("only one of 'template' or 'message-code' attributes may be used in <include>");
      }

      if (templateName != null) {
        tPage.addTemplateInclude(includeName, templateName);
        return;
      } else if (messageCode != null) {
        try {
          tPage.addMessageInclude(includeName, Integer.parseInt(messageCode));
        } catch (NumberFormatException nfe) {
          throw new SAXException("message-code must be a integer");
        }
        return;
      }
      throw new SAXException("'template' or 'message-code' must be defined in <include>");
    }
  }

  /**
   * Receives notification of the end of an element.
   *
   * @see org.xml.sax.ContentHandler#endElement(String, String, String)
   */
  public void endElement(String uri, String localName, String rawName) {
    if ("template-container".equals(localName)) {
      tContainerSlot = null;
      tContainerName = null;
      tPage          = null;
    }

    if ("page".equals(localName)) {
      tPage.setProperties(pageProperties);
      pages.put (tPage.getActionKey(), tPage);
      tPage = null;
    }

    if ("property-list".equals(localName)) {
      if (pageProperties != null) {
        pageProperties.put(propertyList.getName(), propertyList.getValues());
      } else {
        containerProperties.put(propertyList.getName(), propertyList.getValues());
      }
      propertyList = null;
    }
  }


  // Private methods

  /**
   * @param key name of session predicate to create.
   * @return the predicate.
   */
  private Predicate createSessionPredicate(String key) {
    if ((key != null) && (key.equals("isNew"))) {
      return new IsNewHttpSessionPredicate();
    } else {
      return new FalsePredicate();
    }
  }


  // Private classes

  /**
   * Class of a list of properties.
   *
   * @author Tomasz Pik, AIS.PL
   * @author Michal Jastak, AIS.PL
   */
  private class PropertyList {

    /**
     * List of values.
     */
    private List values;

    /**
     * Name.
     */
    private String name;

    /**
     * Type.
     */
    private Class  clazz;


    // Constructors

    /**
     * @param name <code>String</code> object.
     * @param clazz <code>Class</code> object.
     */
    public PropertyList(String name, Class clazz) {
      this.name  = name;
      this.clazz = clazz;
      values = new ArrayList();
    }


    // Public methods

    /**
     * @param value <code>String</code> object.
     * @throws ConversionException if an error occurs.
     */
    public void addValue(String value) throws ConversionException {
      values.add(ConvertUtils.convert(value, clazz));
    }

    /**
     * @return name.
     */
    public String getName() {
      return name;
    }

    /**
     * @return list of values.
     */
    public List getValues() {
      return values;
    }

  } // PropertyList class

} // Workflow class
