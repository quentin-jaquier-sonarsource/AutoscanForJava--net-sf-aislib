package pl.aislib.util.jdbc;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import pl.aislib.fm.jdbc.Manager;

/**
 * The simplest implementation of JDBC <code>Manager</code>.
 *
 * @author Tomasz Pik
 */
public class SimpleManager extends Manager implements Serializable {

  private String jdbcUrl;
  private String jdbcUser;
  private String jdbcPassword;
  private String jdbcDriver;
  private transient boolean driverLoaded;

  /**
   * Load the given JDBC Driver and copy the parameters for use in <code>getConnection</code>.
   *
   * @param _jdbcDriver to load
   * @param _jdbcUrl to connect
   * @param _jdbcUser to connect
   * @param _jdbcPassword to connect
   * @throws ClassNotFoundException if problem with loading the driver occurs
   */
  public SimpleManager(String _jdbcDriver, String _jdbcUrl, String _jdbcUser, String _jdbcPassword)
  throws ClassNotFoundException {
    this(_jdbcUrl, _jdbcUser, _jdbcPassword);
    Class.forName(_jdbcDriver);
    jdbcDriver = _jdbcDriver;
    driverLoaded = true;
  }

  /**
   * Copy the parameters for use in <code>getConnection</code>.
   *
   * @param _jdbcUrl to connect
   * @param _jdbcUser to connect
   * @param _jdbcPassword to connect
   */
  public SimpleManager(String _jdbcUrl, String _jdbcUser, String _jdbcPassword) {
    jdbcUrl      = _jdbcUrl;
    jdbcUser     = _jdbcUser;
    jdbcPassword = _jdbcPassword;
  }

  /**
   * Close the given Connection.
   *
   * @param con Connection to be closed
   * @throws SQLException thrown during <code>close</code> operation
   */
  public void releaseConnection(Connection con) throws SQLException {
    con.close();
  }

  /**
   * Call {@link DriverManager#getConnection(String,String,String)} to create new Connection.
   *
   * @return Connection opened by <code>DriverManager</code>
   * @throws SQLException if driver cannot open connection or there's a problem with
   *         loading driver after deserialization
   */
  public Connection getConnection() throws SQLException {
    if ((!driverLoaded) && (jdbcDriver != null)) {
      try {
        Class.forName(jdbcDriver);
      } catch (ClassNotFoundException cnfe) {
        throw new SQLException("problem with driver loading: " + cnfe.getMessage());
      }
      driverLoaded = true;
    }
    return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
  }

  /**
   * @return database URL and user.
   */
  public String toString() {
    return "SimpleManager: " + jdbcUrl + ", " + jdbcUser;
  }
}
