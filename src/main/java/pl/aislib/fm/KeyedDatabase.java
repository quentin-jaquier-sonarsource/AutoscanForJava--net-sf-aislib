package pl.aislib.fm;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import java.sql.Connection;

import org.apache.commons.logging.Log;

import pl.aislib.fm.jdbc.Manager;

/**
 * Container for set of <code>Database</code> instances.
 *
 * @author Tomasz Pik, AIS.PL
 * @since AISLIB 0.2
 */
public class KeyedDatabase extends Database {

  /**
   * Database instances.
   */
  protected Map databases;


  // Constructors

  /**
   *
   */
  public KeyedDatabase() {
    super(new NopManager());
    databases = new HashMap();
  }


  // Public methods

  /**
   * Puts database to map.
   *
   * @param databaseName name of database.
   * @param database <code>Database</code> implementation.
   */
  public void addDatabase(String databaseName, Database database) {
    databases.put(databaseName, database);
  }

  /**
   * @param databaseName name of database.
   * @return <code>Database</code> implementation from map.
   */
  public Database getDatabase(String databaseName) {
    return (Database) databases.get(databaseName);
  }


  // Package methods

  /**
   * @see pl.aislib.fm.Database#setLog(org.apache.commons.logging.Log)
   */
  void setLog(Log _log) {
    super.setLog(_log);
    Iterator iterator = databases.values().iterator();
    while (iterator.hasNext()) {
      Database database = (Database) iterator.next();
      database.setLog(_log);
    }
  }


  // Private classes

  /**
   * Not supported manager implementation.
   *
   * @author Tomasz Pik, AIS.PL
   */
  private static class NopManager extends Manager {

    // Public methods

    /**
     * @see pl.aislib.fm.jdbc.Manager#getConnection()
     */
    public Connection getConnection() {
      throw new UnsupportedOperationException("This manager doesn't operate on database");
    }

    /**
     * @see pl.aislib.fm.jdbc.Manager#releaseConnection(java.sql.Connection)
     */
    public void releaseConnection(Connection con) {
      throw new UnsupportedOperationException("This manager doesn't operate on database");
    }

  } // NopManager class

} // KeyedDatabase class
