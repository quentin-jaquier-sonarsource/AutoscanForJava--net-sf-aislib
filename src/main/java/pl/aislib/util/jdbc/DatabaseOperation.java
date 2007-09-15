package pl.aislib.util.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Indicates ability to perform database operations.
 * @see HelperMethods#executeInTransaction(Connection con, DatabaseOperation operation, int isolationLevel)
 * @author Milosz Tylenda
 * @since 0.5
 * @version $Revision: 1.1.1.1 $
 */
public interface DatabaseOperation {

   public void executeOperation(Connection con) throws SQLException;

}

