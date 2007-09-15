package pl.aislib.util.web.filters;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.aislib.util.jdbc.JNDIManager;

/**
 * Simple filter which checks if all specified database connections can be open.
 *
 * @author Andrzej Luszczyk, AIS.PL
 * @author Tomasz Pik, AIS.PL
 * @author Bartosz Belcik, AIS.PL
 * @version $Revision: 1.4 $
 */
public class ConnectionCheckerFilter implements Filter {

  public static final String LOG_CATEGORY = "log.category";
  public static final String JNDI_DATASOURCE_NAME = "jndi.datasource.name";
  public static final String PING_QUERY = "ping.query";
  public static final String CHECKING_TIMEOUT = "checking.timeout";
  public static final String ERROR_ACTION = "error.action";
  public static final String ERROR_ACTION_EXCEPTION = "exception";
  public static final String ERROR_ACTION_EXCEPTION_MESSAGE = "exception.message";
  public static final String ERROR_ACTION_REDIRECT = "redirect";
  public static final String ERROR_ACTION_REDIRECT_URL = "redirect.url";
  public static final String ERROR_ACTION_DISPATCHER = "dispatch";
  public static final String ERROR_ACTION_DISPATCHER_URL = "dispatch.url";

  public static final String CONNECTION_STATUS_OK = "OK";
  public static final String CONNECTION_STATUS_FAILED = "FAILED";

  // Default values
  public static final String DEFAULT_LOG_CATEGORY = "pl.aislib.util.web.filters.connection-checker";
  public static final String DEFAULT_EXCEPTION_MESSAGE =
    "Could not connect to the database server. Please try again later.";

  private ConnectionChecker connectionChecker;
  private Log log;
  private JNDIManager manager;
  private int checkingTimeout = 30;
  private String errorAction;
  private String exceptionMessage = DEFAULT_EXCEPTION_MESSAGE;
  private String redirectUrl;
  private String dispatcherUrl;

  private String connectionStatus;
  private long lastChecked = 0;

  /**
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig filterConfig) throws ServletException {
    String logCategory = filterConfig.getInitParameter(LOG_CATEGORY);
    if (logCategory == null || logCategory.trim().length() == 0) {
      logCategory = DEFAULT_LOG_CATEGORY;
    }
    log = LogFactory.getLog(logCategory);
    if (log.isDebugEnabled()) {
      log.debug("Setup connection checker filter");
    }
    String jndiDatasourceName = filterConfig.getInitParameter(JNDI_DATASOURCE_NAME);
    try {
      manager = new JNDIManager(jndiDatasourceName);
    } catch (NamingException ne) {
      log.error("Invalid datasource name: " + jndiDatasourceName, ne);
      throw new ServletException("Invalid datasource name: " + jndiDatasourceName);
    }

    String checkingTimeoutParam = filterConfig.getInitParameter(CHECKING_TIMEOUT);
    if (checkingTimeoutParam != null && checkingTimeoutParam.trim().length() > 0) {
      checkingTimeout = Integer.parseInt(checkingTimeoutParam);
    }
    errorAction = filterConfig.getInitParameter(ERROR_ACTION);
    if (errorAction == null || errorAction.trim().length() == 0) {
      errorAction = ERROR_ACTION_EXCEPTION;
    }
    if (errorAction.equals(ERROR_ACTION_EXCEPTION)) {
      exceptionMessage = filterConfig.getInitParameter(ERROR_ACTION_EXCEPTION_MESSAGE);
      if (exceptionMessage == null || exceptionMessage.trim().length() == 0) {
        exceptionMessage = DEFAULT_EXCEPTION_MESSAGE;
      }
    }
    if (errorAction.equals(ERROR_ACTION_REDIRECT)) {
      redirectUrl = filterConfig.getInitParameter(ERROR_ACTION_REDIRECT_URL);
      if (redirectUrl == null || redirectUrl.trim().length() == 0) {
        throw new ServletException("Redirect URL not specified.");
      }
    }
    if (errorAction.equals(ERROR_ACTION_DISPATCHER)) {
      dispatcherUrl = filterConfig.getInitParameter(ERROR_ACTION_DISPATCHER_URL);
      if (dispatcherUrl == null || dispatcherUrl.trim().length() == 0) {
        throw new ServletException("Dispatcher URL not specified.");
      }
    }
    String pingQuery = filterConfig.getInitParameter(PING_QUERY);
    if (pingQuery == null || pingQuery.trim().length() == 0) {
      connectionChecker = new SimpleConnectionChecker();
    } else {
      connectionChecker = new PingQueryConnectionChecker(pingQuery);
    }
  }

  /**
   * @see javax.servlet.Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
   */
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    long now = Calendar.getInstance().getTime().getTime();
    if (lastChecked != 0 && lastChecked > (now - (checkingTimeout * 1000))) {
      if (connectionStatus != null) {
        if (connectionStatus.equals(CONNECTION_STATUS_OK)) {
          filterChain.doFilter(servletRequest, servletResponse);
          return;
        } else {
          processErrorAction(servletRequest, servletResponse);
          return;
        }
      }
    }
    Connection con = null;
    try {
      con = manager.getConnection();
      if (connectionChecker.checkConnection(con)) {
        lastChecked = now;
        connectionStatus = CONNECTION_STATUS_OK;
      }
    } catch (SQLException sqle) {
      log.error("SQL ERROR: ", sqle);
    } catch (Exception e) {
      log.error("ERROR: ", e);
    } finally {
      try {
        if (con != null) {
          manager.releaseConnection(con);
        }
      } catch (SQLException sqle) {
        // I can't do more...
      }
    }
    if ((connectionStatus != null) && (connectionStatus.equals(CONNECTION_STATUS_OK))) {
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      processErrorAction(servletRequest, servletResponse);
    }
  }

  /**
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
  }

  private void processErrorAction(ServletRequest request, ServletResponse response) throws ServletException,
      IOException {
    connectionStatus = CONNECTION_STATUS_FAILED;
    if (errorAction.equals(ERROR_ACTION_DISPATCHER)) {
      RequestDispatcher dispatcher = request.getRequestDispatcher(dispatcherUrl);
      dispatcher.forward(request, response);
    } else if (errorAction.equals(ERROR_ACTION_REDIRECT)) {
      HttpServletResponse httpServletReponse = (HttpServletResponse) response;
      httpServletReponse.sendRedirect(httpServletReponse.encodeURL(redirectUrl));
    } else {
      throw new ServletException(exceptionMessage);
    }
  }

  interface ConnectionChecker {

    boolean checkConnection(Connection con) throws SQLException;
  }

  /**
   * Implementation of <code>ConnectionChecker</code> interface,
   * which invokes <code>pingQuery</code> on given database connection.
   */
  class PingQueryConnectionChecker implements ConnectionChecker {

    private String pingQuery;

    /**
     * Creates object sets <code>pingQuery</code>.
     *
     * @param pingQuery query to run.
     */
    public PingQueryConnectionChecker(String pingQuery) {
      this.pingQuery = pingQuery;
    }

    /**
     * @see pl.aislib.apps.sample.utils.ConnectionCheckerFilter.ConnectionChecker#checkConnection(java.sql.Connection)
     */
    public boolean checkConnection(Connection con) throws SQLException {
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
        pstmt = con.prepareStatement(pingQuery);
        rs = pstmt.executeQuery();
        rs.next();
      } finally {
        if (rs != null) {
          try {
            rs.close();
          } catch (SQLException sqle) {
            // Cannot properly close result set
          }
        }
        if (pstmt != null) {
          try {
            pstmt.close();
          } catch (SQLException sqle) {
            // Cannot properly close prepared statement
          }
        }
      }
      return true;
    }
  }

  /**
   * Simple implementation of <code>ConnectionChecker</code> interface,
   * which checks only if databse connection is closed.
   */
  class SimpleConnectionChecker implements ConnectionChecker {

    /**
     * @see pl.aislib.apps.sample.utils.ConnectionCheckerFilter.ConnectionChecker#checkConnection(java.sql.Connection)
     */
    public boolean checkConnection(Connection con) throws SQLException {
      return !con.isClosed();
    }
  }

}