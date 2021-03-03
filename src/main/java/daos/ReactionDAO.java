package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import entities.Reaction;
import entities.Thread;

/**
 * The ReactionDAO class provides access to the reaction table in the database.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class ReactionDAO extends DAO {

	/**
	 * Adds a reaction by a user to a thread
	 * 
	 * @param thread	thread that user reacted to
	 * @param reaction	reaction that was posted
	 * @return			true upon successful addition, false otherwise
	 */
    public boolean addReaction(Thread thread, Reaction reaction) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		int addSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL insert statement
			String query = "INSERT INTO REACTION (threader_username, thread_time_stamp, reaction_type, reactor_username, reaction_time_stamp) VALUES (?, ?, ?, ?, ?)";

			// Create the prepared statement from query
            preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
            String threaderUsername = thread.getSenderUsername();
            Timestamp threadTimeStamp = (Timestamp)thread.getTimePosted();
			int reactionType = reaction.getReactionType();
			String reactorUsername = reaction.getReactorUsername();
            Timestamp reactionTimeStamp = (Timestamp)reaction.getTimeReacted();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, threaderUsername);
			preparedStmt.setTimestamp(2, threadTimeStamp);
			preparedStmt.setInt(3, reactionType);
			preparedStmt.setString(4, reactorUsername);
			preparedStmt.setTimestamp(5, reactionTimeStamp);

			// Execute the prepared statement
			addSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when adding reaction:");
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
	 * Returns list of likes for a specified thread
	 * 
	 * @param thread	thread to retrieve likes from
	 * @return			list of reaction objects which are likes for a thread
	 */
	public List<Reaction> getLikes(Thread thread) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		List<Reaction> likes = new ArrayList<>();

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM REACTION WHERE threader_username=? AND thread_time_stamp=? AND reaction_type=1";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String threaderUsername = thread.getSenderUsername();
			Timestamp timeStamp = (Timestamp)thread.getTimePosted();

			// Set prepared statement placeholder values
            preparedStmt.setString(1, threaderUsername);
            preparedStmt.setTimestamp(2, timeStamp);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			while(rs.next()) {
				String reactorUsername = rs.getString("reactor_username");
				java.util.Date reactionTimeStamp = rs.getTimestamp("reaction_time_stamp");
				
				Reaction like = new Reaction(reactorUsername, reactionTimeStamp, 1);
				likes.add(like);
			}
		} catch (Exception e) {
			System.err.println("Error when getting likes from thread:");
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
		return likes;
	}

	/**
	 * Returns list of dislikes for a specified thread
	 * 
	 * @param thread	thread to retrieve dislikes from
	 * @return			list of reaction objects which are dislikes for a thread
	 */
	public List<Reaction> getDislikes(Thread thread) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		List<Reaction> dislikes = new ArrayList<>();

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM REACTION WHERE threader_username=? AND thread_time_stamp=? AND reaction_type=-1";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String threaderUsername = thread.getSenderUsername();
			Timestamp timeStamp = (Timestamp)thread.getTimePosted();

			// Set prepared statement placeholder values
            preparedStmt.setString(1, threaderUsername);
            preparedStmt.setTimestamp(2, timeStamp);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			while(rs.next()) {
				String reactorUsername = rs.getString("reactor_username");
				java.util.Date reactionTimeStamp = rs.getTimestamp("reaction_time_stamp");
				
				Reaction dislike = new Reaction(reactorUsername, reactionTimeStamp, -1);
				dislikes.add(dislike);
			}
		} catch (Exception e) {
			System.err.println("Error when getting dislikes from thread:");
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
		return dislikes;
	}

	/**
	 * Update a reaction in a thread, such as from like to dislike or vice-versa
	 * 
	 * @param thread	thread that contains the reaction
	 * @param reaction	updated reaction
	 * @return			true upon successful update, false otherwise
	 */
    public boolean updateReaction(Thread thread, Reaction reaction) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		int updateSuccess = 0;

    	try {
			// Create the SQL database connection
			conn = getConnection();
		
			// Create the SQL update statement 
			String query = "UPDATE REACTION SET reaction_type=?, reaction_time_stamp=? WHERE threader_username=? AND thread_time_stamp=? AND reactor_username=? AND reaction_time_stamp=?";
			
			// Create the prepared statement from query
            preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
            String threaderUsername = thread.getSenderUsername();
            Timestamp threadTimeStamp = (Timestamp)thread.getTimePosted();
            int reactionType = reaction.getReactionType();
			String reactorUsername = reaction.getReactorUsername();
            Timestamp reactionTimeStamp = (Timestamp)reaction.getTimeReacted();

			// Set prepared statement placeholder values
			preparedStmt.setInt(1, reactionType);
			preparedStmt.setTimestamp(2, reactionTimeStamp);
            preparedStmt.setString(3, threaderUsername);
            preparedStmt.setTimestamp(4, threadTimeStamp);
            preparedStmt.setString(5, reactorUsername);
            preparedStmt.setTimestamp(6, reactionTimeStamp);
	
			// Execute the prepared statement
			updateSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when updating reaction:");
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
	 * Delete a specified reaction from thread
	 * 
	 * @param thread	thread to delete reaction from
	 * @param reaction	reaction to delete
	 * @return			true upon successful deletion, false otherwise
	 */
    public boolean deleteReaction(Thread thread, Reaction reaction) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		int deleteSuccess = 0;

    	try {
			// Create the SQL database connection
			conn = getConnection();
		
			// Create the SQL delete statement 
			String query = "DELETE FROM REACTION WHERE threader_username=? AND thread_time_stamp=? AND reactor_username=? AND reaction_time_stamp=?";
			
			// Create the prepared statement from query
            preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
            String threaderUsername = thread.getSenderUsername();
            Timestamp threadTimeStamp = (Timestamp)thread.getTimePosted();
			String reactorUsername = reaction.getReactorUsername();
            Timestamp reactionTimeStamp = (Timestamp)reaction.getTimeReacted();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, threaderUsername);
			preparedStmt.setTimestamp(2, threadTimeStamp);
            preparedStmt.setString(3, reactorUsername);
            preparedStmt.setTimestamp(4, reactionTimeStamp);
	
			// Execute the prepared statement
			deleteSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when deleting reaction:");
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
}