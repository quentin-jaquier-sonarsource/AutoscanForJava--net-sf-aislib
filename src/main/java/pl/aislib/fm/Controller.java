package pl.aislib.fm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;

/**
 * Main controller of application.
 *
 * @author Tomasz Pik, AIS.PL
 */
public class Controller {

  /**
   * {@link Workflow} component.
   */
  private Workflow workflow;

  /**
   * {@link Application} component that should be controlled.
   */
  private Application application;

  /**
   * Logging object for workflow.
   */
  private Log workflowLog;


  // Constructors

  /**
   * Constructor for Controller.
   *
   * @param application {@link Application} component that should be controlled.
   * @param workflow {@link Workflow} component that defines flow between parts of the application.
   */
  public Controller(Application application, Workflow workflow) {
    this.workflow = workflow;
    this.application = application;
    workflowLog = workflow.log;
  }


  // Public methods

  /**
   * @param request <code>HttpServletRequest</code> object.
   * @param response <code>HttpServletResponse</code> object.
   * @param selector <code>PageSelector</code> instance.
   * @return page information.
   */
  public PageInfo getPageInfo(HttpServletRequest request, HttpServletResponse response, PageSelector selector) {
    String pageName  = workflow.resolveGlobalTriggers(application, request);
    if (pageName != null) {
      return workflow.getPageInfo(pageName);
    }

    pageName = selector.getRequestedPageKey(request);
    HttpSession session = request.getSession();
    PageInfo    pageInfo;
    if (pageName == null) {
      pageInfo = workflow.getStartPageInfo();
    } else {
      pageInfo = workflow.getPageInfo(pageName);
    }

    if (pageInfo != null) {
      if (workflowLog.isDebugEnabled()) {
        workflowLog.debug("page from request: " + pageInfo.getActionKey());
      }
      if (pageInfo.getSessionExpiration() && workflow.isNewSession(session)) {
        pageInfo = workflow.getStartPageInfo();
        request.setAttribute(Constants.PL_AISLIB_FM_SESSION_EXPIRATION, Boolean.TRUE);
      }
      pageName = pageInfo.resolveTriggers(application, request);
      pageInfo = workflow.getPageInfo(pageName);
    }

    return pageInfo;
  }

} // Controller class
