package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import entities.Plot;
import entities.User;

/**
 * The PlotDAO class provides access to the plot table in the database.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class PlotDAO extends DAO { 

	/**
	 * Adds empty plot to a user's farmland
	 * @param user	user whose farmland to add plot to
	 * @return		plot that was created
	 */
    public Plot addEmptyPlot(User user) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		Plot plot = null;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL insert statement
			String query = "INSERT INTO PLOT (username) VALUES (?)";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String username = user.getUsername();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, username);

			// Execute the prepared statement
			preparedStmt.executeUpdate();

			// Close prepared statement before running next query
			if(preparedStmt != null) {
				preparedStmt.close();
			}

			// Create the SQL select statement
			query = "SELECT * FROM PLOT WHERE username=? ORDER BY plot_id DESC LIMIT 1";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Set prepared statement placeholder values
			preparedStmt.setString(1, username);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			if(rs.next()) {
				int plotId = rs.getInt("plot_id");
				plot = new Plot(plotId, username);
			}
		} catch (Exception e) {
			System.err.println("Error when creating empty plot:");
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
		return plot;
	}

	/**
	 * Returns a plot with specified plot id
	 * 
	 * @param plotId	plot id of plot
	 * @return			retrieved plot
	 */
	public Plot getPlot(int plotId) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		Plot plot = null;

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM PLOT WHERE plot_id=?";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Set prepared statement placeholder values
			preparedStmt.setInt(1, plotId);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			if(rs.next()) {
				String username = rs.getString("username");
				String cropName = rs.getString("crop_name"); 

				// Check whether there is a crop present on plot
				if(!rs.wasNull()) {
					Timestamp timeCropPlanted = rs.getTimestamp("time_crop_planted");
					int remainingPercentage = rs.getInt("remaining_percentage");
					int originalYield = rs.getInt("original_yield");

					plot = new Plot(plotId, username, timeCropPlanted, remainingPercentage, cropName, originalYield);
				} else {
					plot = new Plot(plotId, username);
				}
			}
		} catch (Exception e) {
			System.err.println("Error when getting plot:");
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
		return plot;
	}

	/**
	 * Returns plots owned by a specified user
	 * 
	 * @param username	username of user whose plots to retrieve
	 * @return			list of plots owned by specified user
	 */
	public List<Plot> getPlotsOwnedByUser(String username) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		List <Plot> plots = new ArrayList<>();

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM PLOT WHERE username=? ORDER BY plot_id ASC";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Set prepared statement placeholder values
			preparedStmt.setString(1, username);
			
			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			while(rs.next()) {
				Plot plot;
				int plotId = rs.getInt("plot_id");
				String cropName = rs.getString("crop_name"); 

				// Check whether there is a crop present on plot
				if(!rs.wasNull()) {
					Timestamp timeCropPlanted = rs.getTimestamp("time_crop_planted");
					int remainingPercentage = rs.getInt("remaining_percentage");
					int originalYield = rs.getInt("original_yield");

					plot = new Plot(plotId, username, timeCropPlanted, remainingPercentage, cropName, originalYield);
				} else {
					plot = new Plot(plotId, username);
				}
				plots.add(plot);
			}
		} catch (Exception e) {
			System.err.println("Error when getting Plots owned by User:");
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
		return plots;
	}

	/**
	 * Updates a specified plot in plot table
	 * 
	 * @param plot	plot to update
	 * @return		true upon successful update, false otherwise
	 */
    public boolean updatePlot(Plot plot) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int updateSuccess = 0;

    	try {
			// Create the SQL database connection
			conn = getConnection();
		
			// Create the SQL update statement 
			String query = "UPDATE PLOT SET time_crop_planted=?, remaining_percentage=?, crop_name=?, original_yield=? WHERE plot_id=?";
			
			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			Timestamp timeCropPlanted = (Timestamp)plot.getTimeCropPlanted();
			int remainingPercentage = plot.getRemainingPercentage();
			String cropName = plot.getCropName();
			int originalYield = plot.getOriginalYield();

			// Set prepared statement placeholder values
			preparedStmt.setTimestamp(1, timeCropPlanted);
			preparedStmt.setInt(2, remainingPercentage);
			preparedStmt.setString(3, cropName);
			preparedStmt.setInt(4, originalYield);
			preparedStmt.setInt(5, plot.getPlotId());

			// Execute the prepared statement
			updateSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when updating Plot database:");
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
		return (updateSuccess != 0);
	}
	
    /**
	 * Clears a crop from a plot and remove its thieves record
	 * 
	 * @param plot	plot to clear
	 * @return		true upon successful clearance, false otherwise
	 */
	public boolean clearPlot(Plot plot) {
		
		int plotId = plot.getPlotId();
		String ownerUsername = plot.getOwnerUsername();

		// Create empty plot object with the same plot id and updates database
		plot = new Plot(plotId, ownerUsername);
		boolean updateSuccess = updatePlot(plot);

		// Removes currently stored list of thieves for this plot
		StealDAO stealDAO = new StealDAO();
		boolean resetThievesSuccess = stealDAO.resetThieves(plot);

		return updateSuccess && resetThievesSuccess;
	}
}