package pl.aislib.fm;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.aislib.fm.jdbc.Manager;

/**
 * Core implementation of database object.
 *
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.4 $
 * @since AISLIB 0.1
 */
public class Database {

  /**
   * Logging object.
   */
  protected Log log;

  /**
   * <code>Manager</code> implementation object.
   */
  protected Manager manager;


  // Constructors

  /**
   * @param _manager <code>Manager</code> implementation object.
   */
  public Database(Manager _manager) {
    log = LogFactory.getLog("pl.aislib.fm.database");
    manager = new LoggedManager(_manager, log);
  }


  // Public methods

  /**
   * Gets wrapped <code>Manager</code> instance used to operate with database connections.
   *
   * @return manager used by the database.
   */
  public Manager getManager() {
    return manager;
  }

  /**
   * Gets {@link Connection} from {@link Manager#getConnection()}.
   *
   * @return Connection from {@link Manager#getConnection()}.
   * @throws SQLException throws by called method.
   */
  public Connection getConnection() throws SQLException {
    return manager.getConnection();
  }

  /**
   * Passes {@link Connection} to {@link Manager#releaseConnection(Connection)}.
   *
   * @param con which should be passed to {@link Manager#getConnection()}.
   * @throws SQLException thrown by called method.
   */
  public void releaseConnection(Connection con) throws SQLException {
    manager.releaseConnection(con);
  }


  // Package methods

  /**
   * @param _log logging object.
   */
  void setLog(Log _log) {
    log = _log;
    ((LoggedManager) manager).setLog(_log);
  }


  // Private classes

  /**
   * Wrapper of manager logging information about connections.
   *
   * @author Tomasz Pik, AIS.PL
   */
  private class LoggedManager extends Manager {

    /**
     * Manager to trace.
     */
    private Manager manager;

    /**
     * Logging object.
     */
    private Log log;

    /**
     * Number of current connections.
     */
    private int counter;


    // Constructors

    /**
     * @param _manager manager to trace.
     * @param _log logging object.
     */
    public LoggedManager(Manager _manager, Log _log) {
      manager = _manager;
      log     = _log;
      counter = 0;
    }


    // Public methods

    /**
     * @param _log logging object.
     */
    public void setLog(Log _log) {
      log = _log;
    }

    /**
     * @see pl.aislib.fm.jdbc.Manager#getConnection()
     */
    public Connection getConnection() throws SQLException {
      counter++;
      if (log.isTraceEnabled()) {
        log.trace("getting connection " + counter);
      }
      try {
        return manager.getConnection();
      } catch (SQLException sqle) {
        log.fatal("getting connection", sqle);
        throw sqle;
      }
    }

    /**
     * @see pl.aislib.fm.jdbc.Manager#releaseConnection(java.sql.Connection)
     */
    public void releaseConnection(Connection con) throws SQLException {
      if (log.isTraceEnabled()) {
        log.trace("release connection " + counter);
      }
      counter--;
      try {
        manager.releaseConnection(con);
      } catch (SQLException sqle) {
        log.fatal("release connection", sqle);
        throw sqle;
      }
    }

  } // Logging manager class

} //  Database class
