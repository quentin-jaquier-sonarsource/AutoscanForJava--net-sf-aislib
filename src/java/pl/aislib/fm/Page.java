package pl.aislib.fm;

import java.io.IOException;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;

/**
 * Abstract class representing one page of application.
 *
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Michal Jastak, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.6 $
 * @since AISLIB 0.1
 */
public abstract class Page {

  /**
   * HTTP Session object describing session corresponding to current request.
   */
  protected HttpSession session;

  /**
   * Contains HTTP request description.
   */
  protected HttpServletRequest request;

  /**
   * Contains HTTP response description.
   */
  protected HttpServletResponse response;

  /**
   * Contains Servlet description, part of which is this Page.
   */
  protected HttpServlet servlet;

  /**
   * Contains additional information about this Page.
   */
  protected PageInfo pageInfo;

  /**
   * Contains Application description to which this Page belongs.
   */
  protected Application application;

  /**
   * Contains Logging Component.
   */
  protected Log log;

  /**
   * Map of properties of the page.
   */
  protected Map properties;

  /**
   * <code>AttributeNameWrapper</code> object.
   */
  private AttributeNameWrapper nameWrapper;


  // Abstract methods

  /**
   * Method which every subclass of <code>Page</code> must implement.
   *
   * @return PageResponse or null if the given Page handles request internally.
   * @throws ServletException in case of invalid execution.
   * @throws IOException in case of I/O errors.
   */
  public abstract PageResponse getPageResponse() throws IOException, ServletException;


  // Public methods

  /**
   * Gets property with given name.
   *
   * @param propertyName name of a property.
   * @return the property or <em>null</em> if it does not exist.
   */
  public Object getProperty(String propertyName) {
    return properties.get(propertyName);
  }

  /**
   * Gets property with given name.
   *
   * @param propertyName name of a property.
   * @param defaultValue default value for the property.
   * @return the property.
   */
  public Object getProperty(String propertyName, Object defaultValue) {
    if (properties.containsKey(propertyName)) {
      return properties.get(propertyName);
    }
    return defaultValue;
  }

  /**
   * Check if a given property exists.
   *
   * @param propertyName name of the property.
   * @return <code>true</code> if the property exists, <code>false</code> otherwise.
   */
  public boolean hasProperty(String propertyName) {
    return properties.containsKey(propertyName);
  }

  /**
   * Gets names of properties of the page.
   *
   * @return <code>Iterator</code> object.
   */
  public Iterator propertyNames() {
    return properties.keySet().iterator();
  }


  // Protected methods

  /**
   * Gets request parameter with a given name.
   *
   * @param paramName name of request parameter.
   * @return parameter value if the parameter exists, <code>null</code> otherwise.
   * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
   */
  protected String getParameter(String paramName) {
    return request.getParameter(paramName);
  }

  /**
   * Gets request parameters with a given name.
   *
   * @param paramName name of request parameter.
   * @return array of string values of the parameter.
   * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
   */
  protected String[] getParameterValues(String paramName) {
    return request.getParameterValues(paramName);
  }

  /**
   * Checks if there is a request parameter with given name.
   *
   * @param paramName name for requested parameter.
   * @return <code>true</code> if the request contains the parameter, <code>false</code> otherwise.
   */
  protected boolean hasParameter(String paramName) {
    return (null != request.getParameter(paramName));
  }

  /**
   * Checks if there is a request parameter with given name.
   *
   * @param paramName name for requested parameter.
   * @return <code>true</code> if the request contains parameter
   *         with the given name or the given name with <code>.x</code>
   *         or <code>.y</code> suffix, <code>false</code> otherwise.
   */
  protected boolean hasImageParameter(String paramName) {
    return ((null != request.getParameter (paramName)) ||
        (null != request.getParameter(paramName + ".x")) ||
        (null != request.getParameter(paramName + ".y")));
  }

  /**
   * <em>Trigger</em> method is called afer the initialization of the internal
   * strucures for page instance (request, response, application). Subclasses
   * may owerride this method for their own use.
   */
  protected void onLoad() {
    ;
  }

  /**
   * Gets session attribute for application.
   *
   * @param attrName name for an attribute.
   * @return session attribute or <code>null</code>.
   */
  protected Object getSessionAttribute(String attrName) {
    return session.getAttribute(nameWrapper.wrap(attrName));
  }

  /**
   * Checks if there is a session attribute for application.
   *
   * @param attrName name of the attribute.
   * @return <code>true</code> if there is an attribute with the given key,
   *         <code>false</code> otherwise.
   */
  protected boolean hasSessionAttribute(String attrName) {
    return (null != session.getAttribute(nameWrapper.wrap(attrName)));
  }

  /**
   * Removes session attribute for the application.
   *
   * @param attrName attribute name to remove.
   */
  protected void removeSessionAttribute(String attrName) {
    session.removeAttribute(nameWrapper.wrap(attrName));
  }

  /**
   * Sets session attribute for the application.
   *
   * @param attrName name of the attribute.
   * @param attrValue value of the attribute.
   */
  protected void setSessionAttribute(String attrName, Object attrValue) {
    session.setAttribute(nameWrapper.wrap(attrName), attrValue);
  }

  /**
   * Gets an <code>Iterator</code> of <code>String</code>
   * objects containing the names of all the session objects of application.
   *
   * @return <code>Iterator</code> object.
   */
  protected Iterator getSessionAttributeNames() {
    return nameWrapper.unwrap(session.getAttributeNames());
  }

  /**
   * Cleans all session objects associated with this application.
   */
  protected void cleanSession() {
    Iterator iter = nameWrapper.getMatched(session.getAttributeNames());
    while (iter.hasNext()) {
      session.removeAttribute((String) iter.next());
    }
  }

  /**
   * Gets the request attribute for application.
   *
   * @param attrName name of the attribute for the application.
   * @return attribute with the name or <code>null</code>.
   */
  protected Object getRequestAttribute(String attrName) {
    return request.getAttribute(nameWrapper.wrap(attrName));
  }

  /**
   * Check if there is a session attribute for the application.
   *
   * @param attrName name of the attribute for the application.
   * @return <code>true</code> if there is an attribute with the given key,
   *         <code>false</code> otherwise.
   */
  protected boolean hasRequestAttribute(String attrName) {
    return (null != request.getAttribute(nameWrapper.wrap(attrName)));
  }

  /**
   * Removes the session attribute for application.
   *
   * @param attrName name of the attribute to remove.
   */
  protected void removeRequestAttribute(String attrName) {
    request.removeAttribute(nameWrapper.wrap(attrName));
  }

  /**
   * Sets the request attribute for application.
   *
   * @param attrName name of the attribute.
   * @param attrValue value of the attribute.
   */
  protected void setRequestAttribute(String attrName, Object attrValue) {
    request.setAttribute(nameWrapper.wrap(attrName), attrValue);
  }

  /**
   * Gets configuration parameter.
   *
   * @param paramName name of the parameter.
   * @return value of the parameter.
   */
  protected String getConfigParameter(String paramName) {
    return application.getConfigAdapter().getConfigParameter(paramName);
  }

  /**
   * Loads application-specific template using {@link TemplateEngine} object.
   *
   * @param templateName TemplateEngine-specific template name.
   * @return template or <code>null</code>.
   * @throws IOException if an error occurs while loading.
   * @throws ServletException if an error occurs while loading.
   */
  protected Object loadTemplate(String templateName) throws IOException, ServletException {
    return application.loadTemplate(request, response, templateName);
  }

  /**
   * Gets new Page object corresponding to given <code>actionKey</code>.
   *
   * @param actionKey <i>action key</i> describing which Page Object should be created.
   * @return initialized {@link Page} object or <code>null</code> if <code>Page</code> cannot be found.
   * @throws ServletException if an error occurs.
   */
  protected Page getPage(String actionKey) throws ServletException {
    Page page = application.getPage(actionKey);
    if (page != null) {
      if (!pageInfo.checkFlow(actionKey)) {
        if (log.isWarnEnabled()) {
          log.warn("using non defined flow from page " + pageInfo.getActionKey() + " to page " + actionKey);
        }
      }
      page.init(application, request, response, session);
    }
    return page;
  }

  /**
   * Gets message for given code.
   *
   * @param messageCode identification code for the message.
   * @return message with specified language content.
   * @see Application#getMessage(int, String)
   */
  protected Message getMessage(int messageCode) {
    return application.getMessage(messageCode, (String) request.getAttribute(Constants.LANG));
  }

  /**
   * Gets form.
   *
   * @param formName name of the form.
   * @return the form.
   */
  protected Form getForm(String formName) {
    return application.getForm(formName, (String) request.getAttribute(Constants.LANG));
  }


  // Package methods

  /**
   * Initializes the given Page object.
   *
   * <p>
   * Initializes fields:
   * {@link #request}, {@link #response} and {@link #application} are initialized
   * using method parameters;
   * {@link #log} and {@link #servlet} are initialized using values contained within
   * {@link #application} object;
   * {@link #session} is initialized using {@link HttpServletRequest#getSession} method;
   * </p>
   *
   * @param _application <code>Application</code> object.
   * @param _request <code>HttpServletRequest</code> object.
   * @param _response <code>HttpServletResponse</code> object.
   * @param _session <code>HttpSession</code> object.
   */
  void init(
    Application _application, HttpServletRequest _request, HttpServletResponse _response, HttpSession _session
  ) {
    request     = _request;
    response    = _response;
    session     = _session;
    servlet     = _application.getServlet();
    application = _application;
    log         = application.getLog();
    nameWrapper = application.getAttributeNameWrapper();
    onLoad();
  }

} // Page class
