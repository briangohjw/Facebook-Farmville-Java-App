package daos;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The DAO class contains the getConnection method from ConnectionFactory 
 * for other DAOs to inherit.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public abstract class DAO {

    /**
     * Gets connection to MySQL database
     * 
     * @return  connection to MySQL database
     * @throws  SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection conn;
		conn = ConnectionFactory.getConnection();
		return conn;
    }
}