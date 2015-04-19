package pl.aislib.fm.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class is responsible for managing connections to the JDBC database.
 *
 * @author Tomasz Pik, AIS.PL
 * @since AISLIB 0.1
 */
public abstract class Manager {

  /**
   * @return ready to use <code>Connection</code> object.
   * @throws SQLException if database error occurs.
   */
  public abstract Connection getConnection() throws SQLException;

  /**
   * Release the given connection.
   *
   * @param con <code>Connection</code> object to release.
   * @throws SQLException if database error occurs.
   */
  public abstract void releaseConnection(Connection con) throws SQLException;

} // Manager class
