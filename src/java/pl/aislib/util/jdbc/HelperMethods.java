package pl.aislib.util.jdbc;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.Types;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Static methods dealing with <code>ResultSet</code>,
 * <code>PreparedStatement</code> and transactions.
 * @author Tomasz Pik
 * @author Milosz Tylenda
 * @since 0.2
 */
public class HelperMethods {

  /**
   * Read <code>Integer</code> from <code>rs</code>.
   *
   * Corresponding <em>read</em> method: {@link ResultSet#getInt(int)}
   *
   * @param rs <code>ResultSet</code> to read.
   * @param position index of column to read.
   * @return an object if there wasn't a <code>null</code> in the <code>rs</code>, <code>null</code>
   *         otherwise.
   */
  public static Integer readInteger(ResultSet rs, int position) throws SQLException {
    int tmpValue = rs.getInt(position);
    if (rs.wasNull()) {
      return null;
    } else {
      return new Integer(tmpValue);
    }
  }

  /**
   * Setting the given object as an value in the specific position or setting <code>null</code>
   * if the object is <code>null</code>.
   *
   * Corresponding JDBC Type is {@link Types#INTEGER}
   *
   * @param pstmt <code>PreparedStatement</code> on which we operate.
   * @param position index of the column to write.
   * @param object value to set.
   */
  public static void setInteger(PreparedStatement pstmt, int position, Integer object) throws SQLException {
    if (object == null) {
      pstmt.setNull(position, Types.INTEGER);
    } else {
      pstmt.setInt(position, object.intValue());
    }
  }

  /**
   * Read <code>Long</code> from <code>rs</code>.
   *
   * Corresponding <em>read</em> method: {@link ResultSet#getLong(int)}
   *
   * @param rs <code>ResultSet</code> to read.
   * @param position index of column to read.
   * @return an object if there wasn't a <code>null</code> in the <code>rs</code>, <code>null</code>
   *         otherwise.
   */
  public static Long readLong(ResultSet rs, int position) throws SQLException {
    long tmpValue = rs.getLong(position);
    if (rs.wasNull()) {
      return null;
    } else {
      return new Long(tmpValue);
    }
  }

  /**
   * Setting the given object as an value in the specific position or setting <code>null</code>
   * if the object is <code>null</code>.
   *
   * Corresponding JDBC Type is {@link Types#BIGINT}
   *
   * @param pstmt <code>PreparedStatement</code> on which we operate.
   * @param position index of the column to write.
   * @param object value to set.
   */
  public static void setLong(PreparedStatement pstmt, int position, Long object) throws SQLException {
    if (object == null) {
      pstmt.setNull(position, Types.BIGINT);
    } else {
      pstmt.setLong(position, object.longValue());
    }
  }

  /**
   * Read <code>Double</code> from <code>rs</code>.
   *
   * Corresponding <em>read</em> method: {@link ResultSet#getDouble(int)}
   *
   * @param rs <code>ResultSet</code> to read.
   * @param position index of column to read.
   * @return an object if there wasn't a <code>null</code> in the <code>rs</code>, <code>null</code>
   *         otherwise.
   */
  public static Double readDouble(ResultSet rs, int position) throws SQLException {
    double tmpValue = rs.getDouble(position);
    if (rs.wasNull()) {
      return null;
    } else {
      return new Double(tmpValue);
    }
  }

  /**
   * Setting the given object as an value in the specific position or setting <code>null</code>
   * if the object is <code>null</code>.
   *
   * Corresponding JDBC Type is {@link Types#DOUBLE}
   *
   * @param pstmt <code>PreparedStatement</code> on which we operate.
   * @param position index of the column to write.
   * @param object value to set.
   */
  public static void setDouble(PreparedStatement pstmt, int position, Double object) throws SQLException {
    if (object == null) {
      pstmt.setNull(position, Types.DOUBLE);
    } else {
      pstmt.setDouble(position, object.doubleValue());
    }
  }

  /**
   * Read <code>Float</code> from <code>rs</code>.
   *
   * Corresponding <em>read</em> method: {@link ResultSet#getFloat(int)}
   *
   * @param rs <code>ResultSet</code> to read.
   * @param position index of column to read.
   * @return an object if there wasn't a <code>null</code> in the <code>rs</code>, <code>null</code>
   *         otherwise.
   */
  public static Float readFloat(ResultSet rs, int position) throws SQLException {
    float tmpValue = rs.getFloat(position);
    if (rs.wasNull()) {
      return null;
    } else {
      return new Float(tmpValue);
    }
  }

  /**
   * Setting the given object as an value in the specific position or setting <code>null</code>
   * if the object is <code>null</code>.
   *
   * Corresponding JDBC Type is {@link Types#FLOAT}
   *
   * @param pstmt <code>PreparedStatement</code> on which we operate.
   * @param position index of the column to write.
   * @param object value to set.
   */
  public static void setFloat(PreparedStatement pstmt, int position, Float object) throws SQLException {
    if (object == null) {
      pstmt.setNull(position, Types.FLOAT);
    } else {
      pstmt.setFloat(position, object.floatValue());
    }
  }

  /**
   * Read <code>String</code> from <code>rs</code>.
   *
   * Corresponding <em>read</em> method: {@link ResultSet#getString(int)}
   *
   * @param rs <code>ResultSet</code> to read.
   * @param position index of column to read.
   * @return an object if there wasn't a <code>null</code> in the <code>rs</code>, <code>null</code>
   *         otherwise.
   */
  public static String readString(ResultSet rs, int position) throws SQLException {
    String tmpValue = rs.getString(position);
    if (rs.wasNull()) {
      return null;
    } else {
      return tmpValue;
    }
  }

  /**
   * Setting the given object as an value in the specific position or setting <code>null</code>
   * if the object is <code>null</code>.
   *
   * Corresponding JDBC Type is {@link Types#VARCHAR}
   *
   * @param pstmt <code>PreparedStatement</code> on which we operate.
   * @param position index of the column to write.
   * @param object value to set.
   */
  public static void setString(PreparedStatement pstmt, int position, String object) throws SQLException {
    if (object == null) {
      pstmt.setNull(position, Types.VARCHAR);
    } else {
      pstmt.setString(position, object);
    }
  }

  /**
   * Read <code>Timestamp</code> from <code>rs</code>.
   *
   * Corresponding <em>read</em> method: {@link ResultSet#getTimestamp(int)}
   *
   * @param rs <code>ResultSet</code> to read.
   * @param position index of column to read.
   * @return an object if there wasn't a <code>null</code> in the <code>rs</code>, <code>null</code>
   *         otherwise.
   */
  public static Timestamp readTimestamp(ResultSet rs, int position) throws SQLException {
    Timestamp tmpValue = rs.getTimestamp(position);
    if (rs.wasNull()) {
      return null;
    } else {
      return tmpValue;
    }
  }

  /**
   * Setting the given object as an value in the specific position or setting <code>null</code>
   * if the object is <code>null</code>.
   *
   * Corresponding JDBC Type is {@link Types#TIMESTAMP}
   *
   * @param pstmt <code>PreparedStatement</code> on which we operate.
   * @param position index of the column to write.
   * @param object value to set.
   */
  public static void setTimestamp(PreparedStatement pstmt, int position, Timestamp object) throws SQLException {
    if (object == null) {
      pstmt.setNull(position, Types.TIMESTAMP);
    } else {
      pstmt.setTimestamp(position, object);
    }
  }

  /**
   * Read <code>BigDecimal</code> from <code>rs</code>.
   *
   * Corresponding <em>read</em> method: {@link ResultSet#getBigDecimal(int)}
   *
   * @param rs <code>ResultSet</code> to read.
   * @param position index of column to read.
   * @return an object if there wasn't a <code>null</code> in the <code>rs</code>, <code>null</code>
   *         otherwise.
   */
  public static BigDecimal readBigDecimal(ResultSet rs, int position) throws SQLException {
    BigDecimal tmpValue = rs.getBigDecimal(position);
    if (rs.wasNull()) {
      return null;
    } else {
      return tmpValue;
    }
  }

  /**
   * Setting the given object as an value in the specific position or setting <code>null</code>
   * if the object is <code>null</code>.
   *
   * Corresponding JDBC Type is {@link Types#NUMERIC}
   *
   * @param pstmt <code>PreparedStatement</code> on which we operate.
   * @param position index of the column to write.
   * @param object value to set.
   */
  public static void setBigDecimal(PreparedStatement pstmt, int position, BigDecimal object) throws SQLException {
    if (object == null) {
      pstmt.setNull(position, Types.NUMERIC);
    } else {
      pstmt.setBigDecimal(position, object);
    }
  }

  /**
   * Executes <code>{@link DatabaseOperation#executeOperation(Connection)}</code>
   * method in a transaction, performing rollback if necessary.
   * @param con the open <code>Connection</code> to database
   * @param operation the object providing implementation of <code>DatabaseOperation</code> interface.
   * @param isolationLevel the transaction isolation level as defined in
   * <code>{@link java.sql.Connection}</code> to the exclusion of
   * <code>TRANSACTION_NONE</code>
   * @since 0.5
   */
  public static void executeInTransaction(Connection con, DatabaseOperation operation, int isolationLevel)
    throws SQLException {

    boolean isAutoCommit = con.getAutoCommit();
    int     oldLevel     = con.getTransactionIsolation();
    con.setAutoCommit(false);
    try {
      con.setTransactionIsolation(isolationLevel);
      operation.executeOperation(con);
      con.commit();
    } catch (SQLException sqle) {
      con.rollback();
      con.setAutoCommit(isAutoCommit);
      con.setTransactionIsolation(oldLevel);
      throw sqle;
    }
    con.setAutoCommit(isAutoCommit);
    con.setTransactionIsolation(oldLevel);
  }
}
