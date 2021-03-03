package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.Plot;
import entities.User;

/**
 * The StealDAO class provides access to the steal_plot table in the database.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class StealDAO extends DAO {

	/**
	 * Add a specified thief to a specified plot
	 * 
	 * @param plot	plot to add thief too
	 * @param thief	thief to add
	 * @return		true upon successful addition, false otherwise
	 */
    public boolean addThief(Plot plot, User thief) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		int addSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL insert statement
			String query = "INSERT INTO STEAL_PLOT (plot_id, thief_username) VALUES (?, ?)";

			// Create the prepared statement from query
            preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			int plotId = plot.getPlotId();
			String thiefUsername = thief.getUsername();

			// Set prepared statement placeholder values
			preparedStmt.setInt(1, plotId);
			preparedStmt.setString(2, thiefUsername);

			// Execute the prepared statement
			addSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when adding thief:");
			System.err.println(e.getMessage());
		} finally {
			// Close connection, prepared statement and/or result set
			try {
				if(conn != null) {
					conn.close();
				}
				if(preparedStmt != null) {
					preparedStmt.close();
				}
			} catch(Exception e) {
				System.err.println("Error when closing:");
				System.err.println(e.getMessage());
			}
		}
		return (addSuccess != 0);
	}

	/**
	 * Return thieves of a specified plot
	 * 
	 * @param plot	plot to retrieve thieves from
	 * @return		list of thieves of specified plot
	 */
    public List<User> getThieves(Plot plot) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		UserDAO userDAO = new UserDAO();
		List<User> thieves = new ArrayList<>();

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM STEAL_PLOT WHERE plot_id=?";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			int plotId = plot.getPlotId();

			// Set prepared statement placeholder values
			preparedStmt.setInt(1, plotId);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			while(rs.next()) {
                String thiefUsername = rs.getString("thief_username");
                thieves.add(userDAO.getUser(thiefUsername));
			}
		} catch (Exception e) {
			System.err.println("Error when getting thieves:");
			System.err.println(e.getMessage());
		} finally {
			// Close connection, prepared statement and/or result set
			try {
				if(conn != null) {
					conn.close();
				}
				if(preparedStmt != null) {
					preparedStmt.close();
				}
				if(rs != null) {
					rs.close();
				}
			} catch(Exception e) {
				System.err.println("Error when closing:");
				System.err.println(e.getMessage());
			}
		}
		return thieves;
	}

	/**
	 * Remove all thieves from a specified plot
	 * 
	 * @param plot	plot to remove thieves from
	 * @return		true upon successful reset, false otherwise
	 */
	public boolean resetThieves(Plot plot) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int deleteSuccess = 0;

    	try {
			// Create the SQL database connection
			conn = getConnection();
		
			// Create the SQL delete statement 
			String query = "DELETE FROM STEAL_PLOT WHERE plot_id=?";
			
			// Create the prepared statement from query
            preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			int plotId = plot.getPlotId();

			// Set prepared statement placeholder values
			preparedStmt.setInt(1, plotId);
	
			// Execute the prepared statement
			deleteSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when resetting thieves:");
			System.err.println(e.getMessage());
		} finally {
			// Close connection, prepared statement and/or result set
			try {
				if(conn != null) {
					conn.close();
				}
				if(preparedStmt != null) {
					preparedStmt.close();
				}
			} catch(Exception e) {
				System.err.println("Error when closing:");
				System.err.println(e.getMessage());
			}
		}
		return (deleteSuccess != 0);
	}

	/**
	 * Checks if specified user has stolen from a specified plot
	 * 
	 * @param suspect	user to check stolen status
	 * @param plot		plot to check if stolen from
	 * @return			true if user has stolen from plot before, false otherwise
	 */
	public boolean hasStolen(User suspect, Plot plot) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		boolean hasStolen = false;

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM STEAL_PLOT WHERE plot_id=? AND thief_username=?";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			int plotId = plot.getPlotId();
			String thiefUsername = suspect.getUsername();

			// Set prepared statement placeholder values
			preparedStmt.setInt(1, plotId);
			preparedStmt.setString(2, thiefUsername);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// If there are row(s) returned
			if (rs.isBeforeFirst() ) {     
				rs.next();
				hasStolen = true;
			}
		} catch (Exception e) {
			System.err.println("Error when checking if user has stolen from a plot:");
			System.err.println(e.getMessage());
		} finally {
			// Close connection, prepared statement and/or result set
			try {
				if(conn != null) {
					conn.close();
				}
				if(preparedStmt != null) {
					preparedStmt.close();
				}
				if(rs != null) {
					rs.close();
				}
			} catch(Exception e) {
				System.err.println("Error when closing:");
				System.err.println(e.getMessage());
			}
		}
		return hasStolen;
	}
}