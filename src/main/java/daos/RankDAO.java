package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import entities.User;

/**
 * The RankDAO class provides access to the rank table in the database.
 *
 * @version 1.1 04 Apr 2020
 * @author Brian Goh
 */   
public class RankDAO extends DAO {

	/**
	 * Checks if specified user ranks up, and adds plots to his farmland if he does
	 * 
	 * @param user		user to check
	 * @param updatedXp	new xp of user
	 * @return			rank of user after increase in xp
	 */
    public String updateRankAndPlots(User user, int updatedXp) {
        String currentRank = user.getRank();
        String newRank = getRankForXp(updatedXp);

		// Checks whether user ranked up with increase in xp
        if(currentRank.equals(newRank)) {
            return currentRank;
        } else {
            int plotsToAdd = getNumberPlotsForRank(newRank) - getNumberPlotsForRank(currentRank);
			PlotDAO plotDAO = new PlotDAO();
			
			// If user ranked up, add empty plots to his farmland
            for(int i = 0; i < plotsToAdd; i++) {
                plotDAO.addEmptyPlot(user);
            }
            return newRank;
        }
	}

	/**
	 * Get number of plots given to a user of a specified rank
	 * 
	 * @param rank	rank to get number of given plots for
	 * @return		number of plots given to a user of a specified rank
	 */
    public int getNumberPlotsForRank(String rank) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		int numPlots = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM RANK WHERE rank_name=?";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Set prepared statement placeholder values
            preparedStmt.setString(1, rank);

			// Execute the prepared statement
            rs = preparedStmt.executeQuery();

			// Get results from result set
            if(rs.next()) {
				numPlots = rs.getInt("num_plots");
            }
		} catch (Exception e) {
			System.err.println("Error when getting number of plots for rank:");
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
        return numPlots;
    }

	/**
	 * Get rank for a specified xp
	 * 
	 * @param xp	xp to check rank for
	 * @return		rank for a specified xp
	 */
    public String getRankForXp(int xp) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		String rank = null;

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM RANK WHERE ? >= xp ORDER BY xp DESC LIMIT 1";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Set prepared statement placeholder values
            preparedStmt.setInt(1, xp);

			// Execute the prepared statement
            rs = preparedStmt.executeQuery();

			// Get results from result set
            if(rs.next()) {
				rank = rs.getString("rank_name");
            }
		} catch (Exception e) {
			System.err.println("Error when getting rank for xp:");
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
        return rank;
    }
}