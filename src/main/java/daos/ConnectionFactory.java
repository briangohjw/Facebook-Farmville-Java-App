package daos;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * The ConnectionFactory connects to the MySQL database.
 *
 * @version 1.1 05 Apr 2020
 * @author Brian Goh
 */   
public class ConnectionFactory {

    /**
     * Gets connection from the MySQL database
     * 
     * @return  a connection to the MySQL database
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/oop?useSSL=false&user=root&serverTimezone=UTC");  
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return null;
    }
}