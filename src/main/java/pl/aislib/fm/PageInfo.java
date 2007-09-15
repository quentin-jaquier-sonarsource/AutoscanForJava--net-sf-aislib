package pl.aislib.fm;

import java.lang.reflect.Constructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import pl.aislib.fm.shepherds.Pasture;
import pl.aislib.fm.shepherds.Shepherd;
import pl.aislib.fm.shepherds.ShepherdsSchool;

/**
 * This class describes servlet page attributes.
 * 
 * Those attributes are:
 * key assigned to page identyfying particular page,
 * Java class implementing page behaviour,
 * HTML template name used by this page,
 * HTML template container (frame of this HTML page), 
 * HTML template slot inside of container,
 * content type and content disposition for browser,
 * triggers defined inside of this page.
 *
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Michal Jastak, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.8 $
 * @since AISLIB 0.1
 */
public class PageInfo {

  /**
   * Action key for the page.
   */  
  private String actionKey;
  
  /**
   * Name of main template for the page.
   */
  private String templateName;
  
  /**
   * Type of the page.
   */
  private Class  clazz;

  /**
   * Session expiration behaviour.
   */
  private boolean sessionExpiration = true;
  
  /**
   * Name of the template container of the page.
   */
  private String containerTemplateName;
  
  /**
   * Name of the slot in template container to put template into.
   */
  private String containerSlot;

  /**
   * Map of template name and keys.
   */
  private Map templateIncludes;
  
  /**
   * Map of message codes and keys.
   */
  private Map messageIncludes;
  
  /**
   * Map of properties of the page.
   */
  private Map properties;
  
  /**
   * List of shepherds for the page.
   */
  private List shepherds;
  
  /**
   * Set of flows for the page.
   */
  private Set flows;


  // Constructors
  
  /**
   * Initializes PageInfo with actionKey.
   *
   * @param actionKey action key that identifies this page.
   */
  public PageInfo(String actionKey) {
    this.actionKey   = actionKey;

    messageIncludes  = new HashMap();
    properties       = new HashMap();
    shepherds        = new LinkedList();
    templateIncludes = new HashMap();
    flows            = new HashSet();
  }


  // Public methods
  
  /**
   * Adds template to include in output of the page.
   * 
   * @param name key in master template.
   * @param templateName name of template to include.
   */
  public void addTemplateInclude(String name, String templateName) { 
    templateIncludes.put(name, templateName); 
  }

  /**
   * Adds message to include in output of the page.
   * 
   * @param name key in master template.
   * @param messageCode identification code for a message.
   */
  public void addMessageInclude(String name, int messageCode) {
    messageIncludes.put(name, new Integer(messageCode));
  }

  /**
   * Gets <code>Map</code> of template names and keys.
   * 
   * Returns <code>Map</code> of template names and keys for inclusion
   * into response from the page.
   *
   * @return <code>Map</code> of template names and keys.
   */
  public Map getTemplateIncludes() {
    return templateIncludes;
  }

  /**
   * Gets <code>Map</code> of message codes and keys.
   * 
   * Returns <code>Map</code> of message codes and keys for inclusion
   * into response from the page.
   *
   * @return <code>Map</code> of message codes and keys.
   */
  public Map getMessageIncludes() {
    return messageIncludes;
  }

  /**
   * Gets name of template defined for page represented by this <code>PageInfo</code>.
   *
   * @return name of template.
   */
  public String getTemplateName() {
    return templateName; 
  }

  /**
   * Gets name of the template container for the page.
   *
   * @return name of the container.
   */
  public String getContainerTemplateName() {
    return containerTemplateName; 
  }

  /**
   * Gets name of the slot in the template container for the page.
   *
   * Returns name of slot (key) in template if this <code>PageInfo</code>
   * represents page defined as a part of template container.
   *
   * @return name of the slot.
   */
  public String getContainerSlot() {
    return containerSlot; 
  }

  /**
   * Sets template container for the page.
   * 
   * @param containerTemplateName name of the container.
   * @param containerSlot slot in template defined in the container.
   */
  public void setContainer(String containerTemplateName, String containerSlot) {
    this.containerTemplateName = containerTemplateName;
    this.containerSlot = containerSlot;
  }

  /**
   * Sets properties of the page.
   * 
   * @param properties properties of the page.
   */
  protected void setProperties(Map properties) {
    this.properties = properties;
  }

  /**
   * Gets action key for the page.
   * 
   * @return action key for the page. 
   */
  public String getActionKey() {
    return actionKey; 
  }

  /**
   * Returns <code>Class</code> for the page.
   * 
   * @return <code>Class</code> for the page.
   */
  public Class getClazz() {
    return clazz; 
  }

  /**
   * Checks 'equality' of two PageInfo objects.
   * 
   * This 'equality' is defined as equality of their <code>actionKeys</code>.</p>
   * <p>You may use this method with following argument types:
   * <ul>
   *   <li><code>PageInfo</code> object to compare with this one</li>
   *   <li>String containing <code>actionKey</code> for <code>PageInfo</code> object to compare</li>
   * </ul>
   * </p>
   * 
   * @param object PageInfo object or <code>actionKey</code> String to check.
   * @return true if PageInfo objects are equal in above meaning.
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object object) {
    if (object instanceof PageInfo) {
      PageInfo pageInfo = (PageInfo) object;
      return pageInfo.getActionKey().equals(actionKey);
    }
    if (object instanceof String) {
      String temp = (String) object;
      return actionKey.equals(temp);
    }
    return false;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return actionKey.hashCode();
  }


  // Protected methods
  
  /**
   * Method called by Workflow class when reading XML configuration.
   *
   * @param templateName name of main template for the page.
   */
  protected void setTemplateName(String templateName) { 
    this.templateName = templateName; 
  }
  
  /**
   * Sets <em>session expiration<code> beahviour.
   * 
   * Method called by Workflow class when reading XML configuration.
   *
   * @param _sessionExpiration session expiration behaviour.
   */
  protected void setSessionExpiration(boolean _sessionExpiration) {
    sessionExpiration = _sessionExpiration;
  }

  /**
   * Returns <em>session expiration</em> behaviour.
   *
   * Returns <code>true</code> if page should be checked
   * agains expiration of session, <code>false</code> otherwise.
   *
   * @return specification of <em>session expiration</code> behaviour.
   */
  protected boolean getSessionExpiration() {
    return sessionExpiration;
  }


  // Package methods
  
  /**
   * Method called by Workflow class when reading XML configuration.
   *
   * @param clazz type of the page
   */
  void setPageClass(Class clazz) {
    this.clazz = clazz;
  }

  /**
   * Creates {@link Page} object.
   * 
   * @return {@link Page} subclass.
   * @throws ServletException if an error occurs during page creation.
   */
  Page newPageInstance() throws ServletException {
    Page page = null;
    try {
      Class[] argClasses = new Class[0];
      Constructor constructor = clazz.getDeclaredConstructor(argClasses);
      if (constructor != null) {
        Object[] args = new Object[0];
        page = (Page) constructor.newInstance(args);
      }
    } catch (Exception ex) {
      throw new ServletException("Errors during page '" + actionKey + "' creation: ", ex);
    }
    page.pageInfo = this;
    page.properties = Collections.unmodifiableMap(properties);
    return page;
  }

  /**
   * Method called by Workflow class when reading XML configuration.
   *
   * @param shepherd to be added.
   */
  void addShepherd(Shepherd shepherd) {
    shepherds.add(shepherd); 
  }

  /**
   * Register flow from descibed page to page with given name.
   * 
   * @param pageName name of page to which flow is possible.
   */
  void addFlow(String pageName) {
    flows.add(pageName);
  }

  /**
   * Checks if flow from described page to page with given name exists.
   *
   * @param pageName name of page to which flow is checked.
   * @return <code>true</code> if flow is registered, <code>false</code> otherwise.
   */
  boolean checkFlow(String pageName) {
    return flows.contains(pageName);
  }

  /**
   * Checks the set of trigger keys for this instance of PageInfo.
   *
   * @param pasture pasture where page is executed.
   * @param sheep {@link javax.servlet.http.HttpServletRequest} object
   * @return action key for the page accesible through one of the triggers
   *         or this page's action key, if there are no triggers.
   */
   String resolveTriggers(Pasture pasture, Object sheep) {
     return ShepherdsSchool.examine(shepherds, pasture, sheep, actionKey);
   }

} // PageInfo class
