package pl.aislib.util.jdbc;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import pl.aislib.fm.jdbc.Manager;

/**
 * Implementation based on container-driven <code>DataSource</code>.
 *
 * @author Tomasz Pik
 */
public class JNDIManager extends Manager implements Serializable {

  protected transient DataSource dataSource;
  private String       jndiPath;

  /**
   * @param _jndiPath path to <code>DataSource</code> in JNDI Context.
   *        in most cases, this parameter starts with <code>java:comp/env/jdbc</code>.
   * @throws NamingException while problems with JNDI occurs or there's
   *         no <code>DataSource</code> registered.
   * @throws NullPointerException if given <code>_jndiPath</code> is null.
   */
  public JNDIManager(String _jndiPath) throws NamingException {
    if (_jndiPath == null) {
      throw new NullPointerException("jndiPath cannot be null");
    }
    jndiPath = _jndiPath;

    dataSource = lookup(jndiPath);
    if (dataSource == null) {
      throw new NamingException("there's no DataSource registered as: " + jndiPath);
    }
  }

  /**
   * Get <code>Connection<code> from <code>DataSource</code>
   *
   * @return Connection from <code>DataSource</code>
   * @throws SQLException thrown during <code>DataSource.getConnection()</code> operation
   *         or there's a problem with JNDI after deserialization.
   */
  public Connection getConnection() throws SQLException {
    synchronized (jndiPath) {
      if (dataSource == null) {
        try {
          dataSource = lookup(jndiPath);
        } catch (NamingException ne) {
          throw new SQLException("problem with JNDI lookup: " + ne.getMessage());
        }
        if (dataSource == null) {
          throw new SQLException("there's no DataSource registered as: " + jndiPath);
        }
      }
    }
    return dataSource.getConnection();
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
   * @return path inside JNDI context
   */
  public String toString() {
    return "JNDIManager: " + jndiPath;
  }

  private DataSource lookup(String jndiPath) throws NamingException {
    Context env = new InitialContext();
    Object o = env.lookup(jndiPath);
    try {
      return (DataSource) o;
    } catch (ClassCastException cce) {
      throw new NamingException("ClassCastExeption: " + o.getClass().getName());
    }
  }
}
