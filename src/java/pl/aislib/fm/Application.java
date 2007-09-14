package pl.aislib.fm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.aislib.fm.shepherds.Pasture;
import pl.aislib.util.xml.JarEntityResolver;
import pl.aislib.util.xml.LogErrorHandler;
import pl.aislib.util.xml.XMLUtils;

/**
 * Describes an application.
 *
 * Uses following components:
 * <ul>
 *   <li>{@link FormsHandler} -- describing all forms inside of application</li>
 *   <li>{@link MessagesHandler} -- describing all applications messages</li>
 *   <li>{@link Workflow} -- describing flow betweeen application pages</li>
 * </ul>
 *
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Michal Jastak, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.10 $
 * @since AISLIB 0.1
 */
public class Application implements Pasture {

  /**
   * Path to all framework DTD files inside of Jar archive.
   * Don't use leading '/'. Use '/' instead of '.' in package name.
   * Thanks for your cooperation :)
   */
  private static final String DTD_JAR_PREFIX = "pl/aislib/fm/dtds";

  /**
   * Name of servlet context parameter whose value should identify application version.
   */
  private static final String APP_VERSION_CONTEXT_PARAM = "app.version";

  /**
   * Default content type for responses returned in PageResponse.
   */
  private String defaultContentType = "text/html";

  /**
   * Name of the application.
   */
  private String applicationName;

  /**
   * Servlet that uses the application.
   */
  private HttpServlet servlet;

  /**
   * {@link Workflow} component.
   */
  private Workflow workflow;

  /**
   * {@link FormsHandler} component.
   */
  private FormsHandler forms;

  /**
   * {@link MessagesHandler} component.
   */
  private MessagesHandler messages;

  /**
   * {@link TemplateEngine} object for the application.
   */
  private TemplateEngine templateEngine;

  /**
   * {@link ConfigAdapter} object for the application.
   */
  private ConfigAdapter configAdapter;

  /**
   * {@link AttributeNameWrapper} object for the application.
   */
  private AttributeNameWrapper attributeNameWrapper;

  /**
   * {@link Database} defined for the application.
   */
  private Database database;

  /**
   * {@link Controller} object for the application.
   */
  private Controller controller;

  /**
   * Map of registered identification objects.
   */
  private Map registeredIds;

  /**
   * Main logging object for the application.
   */
  private Log mainLog;

  /**
   * Logging object for workflow.
   */
  private Log workflowLog;

  /**
   * Logging object for forms.
   */
  private Log formsLog;

  /**
   * Logging object for messages.
   */
  private Log messagesLog;

  /**
   * Logging object for templates.
   */
  private Log templateLog;

  // Constructors

  /**
   * Main constructor.
   *
   * @param _applicationName name of the application.
   * @param _servlet that requests this application handle.
   * Throws NullPointerException if the <code>_applicationName</code> or <code>_servlet</code> is null
   */
  public Application(String _applicationName, HttpServlet _servlet) {
    if (_applicationName == null) {
      throw new NullPointerException("Application name cannot be null");
    }
    if (_servlet == null) {
      throw new NullPointerException("servlet cannot be null");
    }
    applicationName = _applicationName;
    servlet  = _servlet;

    configAdapter        = new DefaultConfigAdapter(servlet);
    attributeNameWrapper = new AttributeNameWrapper(applicationName);

    formsLog    = getLog("forms");
    mainLog     = getLog();
    messagesLog = getLog("messages");
    templateLog = getLog("template");
    workflowLog = getLog("workflow");

    registeredIds  = new HashMap(4);
    registeredIds.put("-//AIS.PL//DTD Forms Description 0.2//EN",    "forms_0_2.dtd");
    registeredIds.put("-//AIS.PL//DTD Forms Description 0.4//EN",    "forms_0_4.dtd");
    registeredIds.put("-//AIS.PL//DTD Messages Description 0.2//EN", "messages_0_2.dtd");
    registeredIds.put("-//AIS.PL//DTD Workflow Description 0.2//EN", "workflow_0_2.dtd");

    workflow       = new Workflow(workflowLog);
    forms          = new FormsHandler(formsLog);
    messages       = new MessagesHandler(messagesLog);

    if (mainLog.isInfoEnabled()) {
      String version = servlet.getServletContext().getInitParameter(APP_VERSION_CONTEXT_PARAM);
      mainLog.info("Version: " + version);
    }
  }


  // Public methods

  /**
   * Initialize {@link Workflow} component.
   *
   * @param workflowStream contains XML definition of application workflow
   * @exception ApplicationConfigurationException if something goes wrong
   */
  public void initWorkflow(InputStream workflowStream) throws ApplicationConfigurationException {
    if (workflowStream == null) {
      throw new NullPointerException ("Invalid (null) configuration for Workflow");
    }
    initializeWorkflow(new InputSource(workflowStream));
  }

  /**
   * Initialize {@link Workflow} component.
   *
   * @param workflowSource contains XML definition of application workflow
   * @exception ApplicationConfigurationException if something goes wrong
   */
  public void initWorkflow(InputSource workflowSource) throws ApplicationConfigurationException {
    if (workflowSource == null) {
      throw new NullPointerException ("Invalid (null) configuration for Workflow");
    }
    initializeWorkflow(workflowSource);
  }

  private void initializeWorkflow(InputSource workflowSource) throws ApplicationConfigurationException {
    if (mainLog.isInfoEnabled()) {
      mainLog.info("initialize workflow");
    }
    XMLReader   workflowReader = null;
    try {
      workflowReader = XMLUtils.newXMLReader(true, true);
    } catch (SAXException saxe) {
      throw new ApplicationConfigurationException(saxe);
    }
    workflowReader.setContentHandler(workflow);
    LogErrorHandler errorLog = new LogErrorHandler(workflowLog);
    errorLog.setAllErrorsAreFatal(true);
    workflowReader.setErrorHandler(errorLog);
    JarEntityResolver jarEntityResolver = new JarEntityResolver(Application.DTD_JAR_PREFIX, registeredIds, workflowLog);
    workflowReader.setEntityResolver(jarEntityResolver);
    try {
      workflowReader.parse(workflowSource);
    } catch (IOException ioe) {
      throw new ApplicationConfigurationException(ioe);
    } catch (SAXException saxe) {
      throw new ApplicationConfigurationException(saxe);
    }
    controller = new Controller(this, workflow);
  }

  /**
   * Initialize {@link MessagesHandler} component.
   *
   * @param messagesStream contains XML definition of application messages
   * @exception ApplicationConfigurationException if something goes wrong
   */
  public void initMessages(InputStream messagesStream) throws ApplicationConfigurationException {
    if (messagesStream == null) {
      throw new NullPointerException ("Invalid (null) configuration for Messages");
    }
    initializeMessages(new InputSource(messagesStream));
  }

  /**
   * Initialize {@link MessagesHandler} component.
   *
   * @param messagesSource contains XML definition of application messages
   * @exception ApplicationConfigurationException if something goes wrong
   */
  public void initMessages(InputSource messagesSource) throws ApplicationConfigurationException {
    if (messagesSource == null) {
      throw new NullPointerException ("Invalid (null) configuration for Messages");
    }
    initializeMessages(messagesSource);
  }

  private void initializeMessages(InputSource messagesSource) throws ApplicationConfigurationException {
    if (mainLog.isInfoEnabled()) {
      mainLog.info("initialize messages");
    }
    XMLReader   messagesReader = null;
    try {
      messagesReader = XMLUtils.newXMLReader(true, true);
    } catch (SAXException saxe) {
      throw new ApplicationConfigurationException(saxe);
    }
    messagesReader.setContentHandler(messages);
    LogErrorHandler errorLog = new LogErrorHandler (messagesLog);
    errorLog.setAllErrorsAreFatal(true);
    messagesReader.setErrorHandler(errorLog);
    JarEntityResolver jarEntityResolver = new JarEntityResolver(Application.DTD_JAR_PREFIX, registeredIds, messagesLog);
    messagesReader.setEntityResolver(jarEntityResolver);
    try {
      messagesReader.parse(messagesSource);
    } catch (IOException ioe) {
      throw new ApplicationConfigurationException(ioe);
    } catch (SAXException saxe) {
      throw new ApplicationConfigurationException(saxe);
    }
  }

  /**
   * Initialize {@link FormsHandler} component.
   *
   * @param formsStream contains XML definition of application forms
   * @exception ApplicationConfigurationException if something goes wrong
   */
  public void initForms(InputStream formsStream) throws ApplicationConfigurationException {
    if (formsStream == null) {
      throw new NullPointerException ("Invalid (null) configuration for Forms");
    }
    initializeForms(new InputSource(formsStream));
  }

  /**
   * Initialize {@link FormsHandler} component.
   *
   * @param formsSource contains XML definition of application forms
   * @exception ApplicationConfigurationException if something goes wrong
   */
  public void initForms(InputSource formsSource) throws ApplicationConfigurationException {
    if (formsSource == null) {
      throw new NullPointerException ("Invalid (null) configuration for Forms");
    }
    initializeForms(formsSource);
  }

  private void initializeForms(InputSource formsSource) throws ApplicationConfigurationException {
    if (mainLog.isInfoEnabled()) {
      mainLog.info("initialize forms");
    }
    XMLReader   formsReader = null;
    try {
      formsReader = XMLUtils.newXMLReader(true, true);
    } catch (SAXException saxe) {
      throw new ApplicationConfigurationException(saxe);
    }
    formsReader.setContentHandler(forms);
    LogErrorHandler errorLog = new LogErrorHandler (formsLog);
    errorLog.setAllErrorsAreFatal (true);
    formsReader.setErrorHandler(errorLog);
    JarEntityResolver jarEntityResolver = new JarEntityResolver (Application.DTD_JAR_PREFIX, registeredIds, formsLog);
    formsReader.setEntityResolver(jarEntityResolver);
    try {
      formsReader.parse(formsSource);
    } catch (IOException ioe) {
      throw new ApplicationConfigurationException(ioe);
    } catch (SAXException saxe) {
      throw new ApplicationConfigurationException(saxe);
    }
  }

  /**
   * Set {@link TemplateEngine} for this Application.
   *
   * @param _templateEngine which should be used to load templates.
   */
  public void setTemplateEngine(TemplateEngine _templateEngine) {
    if (_templateEngine != null) {
      if (mainLog.isInfoEnabled()) {
        mainLog.info("setting template engine: " + _templateEngine.getClass().getName());
      }
      templateEngine = _templateEngine;
    } else {
      mainLog.error("try to set null as a template engine");
      throw new NullPointerException("template engine cannot be null");
    }
  }

  /**
   * Set {@link Database} for this Application
   *
   * During setting as a component inside Application, <code>jdbc</code>
   * logger is pushed into given Database.
   *
   * @param _database for using within given Application.
   * Throws NullPointerException if given argument is <code>null</code>
   */
  public void setDatabase(Database _database) {
    if (_database != null) {
      if (mainLog.isInfoEnabled()) {
        mainLog.info("setting database: " + _database.getClass().getName());
      }
      database = _database;
      database.setLog(getLog("jdbc"));
    } else {
      if (mainLog.isErrorEnabled()) {
        mainLog.error("try to set null as a database");
      }
      throw new NullPointerException("database cannot be null");
    }
  }

  /**
   * @return Database defined for Application.
   */
  public Database getDatabase() {
    return database;
  }

  /**
   * Set {@link ConfigAdapter} for this Application.
   *
   * @param _configAdapter which should be used to access application parameters.
   */
  public void setConfigAdapter(ConfigAdapter _configAdapter) {
    if (_configAdapter != null) {
      if (mainLog.isInfoEnabled()) {
        mainLog.info("setting config adapter: " + _configAdapter.getClass().getName());
      }
      configAdapter = _configAdapter;
    } else {
      if (mainLog.isErrorEnabled()) {
        mainLog.error("try to set null as a config adapter");
      }
      throw new NullPointerException("config adapter cannot be null");
    }
  }

  /**
   * Sets default <code>ContentType</code> for the application.
   *
   * <code>text/html</code> is hard-coded as default one.
   *
   * @param _contentType default <code>ContentType</code>
   */
  public void setDefaultContentType(String _contentType) {
    defaultContentType = _contentType;
  }

  /**
   * @return default <code>ContentType</code> for the application.
   */
  public String getDefaultContentType() {
    return defaultContentType;
  }

  /**
   * @return the configuration adapter for the application.
   */
  public ConfigAdapter getConfigAdapter() {
    return configAdapter;
  }

  /**
   *
   * @return <code>AttributeNameWrapper</code> for the application
   */
  public AttributeNameWrapper getAttributeNameWrapper() {
    return attributeNameWrapper;
  }

  /**
   * @return name of the application.
   */
  public String getName() {
    return applicationName;
  }

  /**
   * @return servlet that uses the application.
   */
  public HttpServlet getServlet() {
    return servlet;
  }

  /**
   * @return main logging object for the application.
   */
  public Log getLog() {
    return LogFactory.getLog(applicationName);
  }

  /**
   * @param loggerName name of a logging object.
   * @return a "sublogger" of the application's main logging object.
   */
  public Log getLog(String loggerName) {
    return LogFactory.getLog(applicationName + "." + loggerName);
  }

  /**
   *
   * @deprecated As of AISLIB 0.5 replaced by {@link Page#getMessage(int)} with default language.
   * @see #getMessage(int, String).
   */
  public Message getMessage(int messageCode) {
    Object obj = messages.getMessage(messageCode);
    return obj != null ? (Message) obj : null;
  }

  /**
   * Method getMessage returns the message for given code and language.
   *
   * @param messageCode code of message.
   * @param lang language of message content.
   * @return Message for given <code>messageCode</code> and <code>lang</code>
   *                  (if <code>lang</code> is null it returns default content).
   */
  public Message getMessage(int messageCode, String lang) {
    Message message;
    if (lang != null && lang.trim().length() > 0) {
      message = messages.getMessage(messageCode, lang);
    } else {
      message = messages.getMessage(messageCode);
    }
    return message;
  }

  /**
   * @param groupCode code of message group.
   * @return List of {@link Message} objects defined as group with given code.
   */
  public List getMessageGroup(int groupCode) {
    return messages.getMessageGroup(groupCode);
  }

  /**
   * Gets form.
   *
   * @param formName name of a form.
   * @return <code>Form</code> object.
   * @deprecated As of AISLIB 0.5 replaced by {@link Page#getForm(String)} which supports multi language.
   */
  public Form getForm(String formName) {
    return forms.getForm(formName, messages.messages, messages.messageGroups, null);
  }

  /**
   * Gets form.
   *
   * @param formName name of a form.
   * @param language language for the form.
   * @return <code>Form</code> object.
   */
  public Form getForm(String formName, String language) {
    return forms.getForm(formName, messages.messages, messages.messageGroups, language);
  }

  /**
   * Process request and response.
   *
   * This method should always be used in <code>servlet</code> object.
   *
   * @see javax.servlet.Servlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   */
  public void dispatch(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    if (controller == null) {
      throw new ServletException("controller not initialized");
    }

    String      pageName  = request.getParameter("page");
    HttpSession session   = request.getSession();

    PageInfo pageInfo = controller.getPageInfo(request, response);

    if (pageInfo == null) {
      if (workflowLog.isDebugEnabled()) {
        workflowLog.debug("unknown page for request, page attribute: " + pageName);
      }
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    // create a new Page instance and initialize it through 'init' method
    Page page = pageInfo.newPageInstance();
    page.init(this, request, response, session);

    if (workflowLog.isDebugEnabled()) {
      workflowLog.debug("getting response from page: " + pageName);
    }

    PageResponse pageResponse = page.getPageResponse();

    if (pageResponse != null) {
      pageInfo = pageResponse.getPage().pageInfo;
      if (pageResponse.getContentType() != null) {
        response.setContentType(pageResponse.getContentType());
      } else if (defaultContentType != null) {
        response.setContentType(defaultContentType);
      }

      if (pageResponse.getContentByte() != null) {
        OutputStream stream = response.getOutputStream();
        stream.write(pageResponse.getContentByte());
        return;
      }

      Object masterTemplate = null;
      Map    params         = pageResponse.getContentMap();
      if (params == null) {
        params = new HashMap();
      }

      Map    includes = loadAndFill(page, pageInfo, params, request, response);
      params.putAll(includes);

      if (pageInfo.getContainerTemplateName() == null) {
        masterTemplate = loadTemplate(request, response, pageInfo.getTemplateName());
      } else {
        masterTemplate = loadTemplate(request, response, pageInfo.getContainerTemplateName());

        Object pageTemplate = loadTemplate(request, response, pageInfo.getTemplateName());

        try {
          params.put(pageInfo.getContainerSlot(), templateEngine.evaluate(pageTemplate, params));
        } catch (TemplateEngineException teex) {
          throw new ServletException(teex.getMessage (), teex.getRootCause());
        }
      }

      String responseString = null;
      try {
        responseString = templateEngine.evaluate(masterTemplate, params);
      } catch (TemplateEngineException teex) {
        throw new ServletException(teex.getMessage(), teex.getRootCause());
      }
      Writer writer = response.getWriter();
      writer.write((String) responseString);
      return;
    }
  }


  // Package methods

  /**
   * @see TemplateEngine#load(Application, HttpServletRequest, HttpServletResponse, String)
   */
  Object loadTemplate(HttpServletRequest request, HttpServletResponse response, String templateName)
    throws IOException, ServletException {
    if (templateLog.isDebugEnabled()) {
      templateLog.debug("loading template: " + templateName);
    }
    if (templateEngine == null) {
      throw new NullPointerException ("Template Engine not initialized");
    }
    Object template = null;
    try {
      template = templateEngine.load(this, request, response, templateName);
    } catch (TemplateEngineException teex) {
      if (templateLog.isErrorEnabled()) {
        String error = "exception caught during template '" + templateName + "' loading";
        if (teex.getRootCause() != null) {
          templateLog.error(error + ": " + teex.getMessage(), teex.getRootCause());
        } else {
          templateLog.error(error, teex);
        }
      }
      if (teex.getRootCause() != null) {
        if (teex.getRootCause() instanceof IOException) {
          throw (IOException) teex.getRootCause();
        }
        if (teex.getRootCause() instanceof ServletException) {
          throw (ServletException) teex.getRootCause();
        }
      }
      throw new ServletException(teex.getMessage(), teex.getRootCause());
    }
    return template;
  }

  /**
   * @param actionKey name of a page.
   * @return the page.
   * @throws ServletException if an error occurs.
   */
  Page getPage(String actionKey) throws ServletException {
    if (workflowLog.isDebugEnabled()) {
      workflowLog.debug("getting page: " + actionKey);
    }
    PageInfo pageInfo = workflow.getPageInfo(actionKey);
    if (pageInfo != null) {
      Page page = pageInfo.newPageInstance();
      page.pageInfo = pageInfo;
      return page;
    }
    return null;
  }


  // Private methods

  /**
   * Fills a template with values.
   *
   * @param page <code>Page</code> instance.
   * @param pageInfo page information object.
   * @param templateParams evaluation parameters.
   * @param request <code>HttpServletRequest</code> object.
   * @param response <code>HttpServletResponse</code> object.
   * @return map of values.
   * @throws IOException if an error occurs.
   * @throws ServletException if an error occurs.
   */
  private Map loadAndFill(Page page, PageInfo pageInfo, Map templateParams, HttpServletRequest request,
                          HttpServletResponse response) throws IOException, ServletException {
    Map result = new HashMap();
    Map templateIncludes = pageInfo.getTemplateIncludes();
    Iterator keys = templateIncludes.keySet().iterator();
    while (keys.hasNext()) {
      String includeName         = (String) keys.next();
      String includeTemplateName = (String) templateIncludes.get(includeName);
      Object includeTemplate     = loadTemplate(request, response, includeTemplateName);
      try {
        result.put (includeName, templateEngine.evaluate (includeTemplate, templateParams));
      } catch (TemplateEngineException teex) {
        throw new ServletException (teex.getMessage (), teex.getRootCause ());
      }
    }

    Map messageIncludes = pageInfo.getMessageIncludes();
    keys = messageIncludes.keySet().iterator();
    while (keys.hasNext()) {
      String  includeName = (String) keys.next();
      Integer messageCode = (Integer) messageIncludes.get(includeName);
      Message message = page.getMessage(messageCode.intValue());
      result.put(includeName, message.getContent());
    }
    return result;
  }


  // Package classes

  /**
   * Default implementation of {@link ConfigAdapter} class.
   *
   * Call {@link HttpServlet#getInitParameter} and {@link javax.servlet.ServletContext#getInitParameter}
   * methods.
   */
  class DefaultConfigAdapter extends ConfigAdapter {

    /**
     * Servlet for the adapter.
     */
    private GenericServlet servlet;


    // Constructors

    /**
     * @param servlet servlet for the adapter.
     */
    DefaultConfigAdapter(GenericServlet servlet) {
      this.servlet = servlet;
    }


    // Public methods

    /**
     * Return standard parameter for a given servlet.
     *
     * @param key name of a parameter.
     * @return value for the parameter or <code>null</code> if the parameter is not defined.
     */
    public String getConfigParameter(String key) {
      String value = servlet.getInitParameter(key);
      if (value != null) {
        return value;
      } else {
        return servlet.getServletContext().getInitParameter(key);
      }
    }
  } // DefaultConfigAdapter class

} // Application class
