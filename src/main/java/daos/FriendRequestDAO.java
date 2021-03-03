package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.User;

/**
 * The FriendRequestDAO class provides access to the friend_request table in the database.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class FriendRequestDAO extends DAO {

	/**
	 * Returns friend requests received by specified user
	 * 
	 * @param user	user to retrieve friend requests of
	 * @return		list of users who sent friend request to specified user
	 */
    public List<User> getFriendRequests(User user) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		List<User> friendRequests = new ArrayList<>();

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM FRIEND_REQUESTS WHERE receiver_username=?";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String username = user.getUsername();

			// Set prepared statement placeholder values
            preparedStmt.setString(1, username);

			// Execute the prepared statement
            rs = preparedStmt.executeQuery();

			// Get results from result set
            while(rs.next()) {
				String requestorUsername = rs.getString("requestor_username");
				UserDAO userDAO = new UserDAO();
				
				User requestor = userDAO.getUser(requestorUsername);
                friendRequests.add(requestor);
            }
		} catch (Exception e) {
			System.err.println("Error when getting friend requests:");
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
        return friendRequests;
	}

	/**
	 * Adds a friend request from requestor user to receiver user
	 * 
	 * @param requestor	user who sends the friend request
	 * @param receiver	user who receives the friend request
	 * @return			true upon successful insertion, false otherwise
	 */
    public boolean addFriendRequest(User requestor, User receiver) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int addSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL insert statement
            String query = "INSERT INTO FRIEND_REQUESTS (requestor_username, receiver_username) VALUES (?, ?)";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			preparedStmt.setString(1, requestor.getUsername());
			preparedStmt.setString(2, receiver.getUsername());

			// Execute the prepared statement
			addSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when adding friend request:");
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
	 * Accepts a friend request that was sent from requestor to receiver
	 * 
	 * @param requestor	user who sent the friend request
	 * @param receiver	user who received the friend request
	 * @return			true upon successful acceptance, false otherwise
	 */
    public boolean acceptFriendRequest(User requestor, User receiver) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int insertSuccess = 0;
		int deleteSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL insert statement
			String query = "INSERT INTO FRIENDS (username1, username2) VALUES (?, ?)";
			
			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String requestorUsername = requestor.getUsername();
			String receiverUsername = receiver.getUsername();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, requestorUsername);
			preparedStmt.setString(2, receiverUsername);

			// Execute the prepared statement
			insertSuccess = preparedStmt.executeUpdate();

			// Close prepared statement before running next query
			if(preparedStmt != null) {
				preparedStmt.close();
			}

			// Create the SQL delete statement
            query = "DELETE FROM FRIEND_REQUESTS WHERE (requestor_username=? AND receiver_username=?) OR (receiver_username=? AND requestor_username=?)";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Set prepared statement placeholder values
			preparedStmt.setString(1, requestorUsername);
			preparedStmt.setString(2, receiverUsername);
			preparedStmt.setString(3, requestorUsername);
			preparedStmt.setString(4, receiverUsername);

			// Execute the prepared statement
			deleteSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when accepting friend request:");
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
		return (insertSuccess + deleteSuccess != 0);
    }

	/**
	 * Rejects a friend request that was sent from requestor to receiver
	 * 
	 * @param requestor	user who sent the friend request
	 * @param receiver	user who received the friend request
	 * @return			true upon successful rejection, false otherwise
	 */
    public boolean rejectFriendRequest(User requestor, User receiver) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int rejectSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL delete statement
            String query = "DELETE FROM FRIEND_REQUESTS WHERE (requestor_username=? AND receiver_username=?) OR (receiver_username=? AND requestor_username=?)";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String requestorUsername = requestor.getUsername();
			String receiverUsername = receiver.getUsername();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, requestorUsername);
			preparedStmt.setString(2, receiverUsername);
			preparedStmt.setString(3, requestorUsername);
			preparedStmt.setString(4, receiverUsername);

			// Execute the prepared statement
			rejectSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when rejecting friend request:");
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
		return (rejectSuccess != 0);
	}

	/**
	 * Checks if requestor has already sent receiver a friend request
	 * or if receiver has already sent requestor a friend requesr
	 * 
	 * @param requestor	user who is sending the friend request
	 * @param receiver	user who is receiving the friend request
	 * @return			true if a request already exists, false otherwise
	 */
	public boolean requestAlreadyExists(User requestor, User receiver) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		boolean requestAlreadyExists = false;

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM FRIEND_REQUESTS WHERE requestor_username=? AND receiver_username=?";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			String requestorUsername = requestor.getUsername();
			String receiverUsername = receiver.getUsername();

			// Get required fields for query
			preparedStmt.setString(1, requestorUsername);
			preparedStmt.setString(2, receiverUsername);

			// Execute the prepared statement
            rs = preparedStmt.executeQuery();

			// If there are row(s) returned
			if (rs.isBeforeFirst()) {     
				rs.next();
				requestAlreadyExists = true;
			}
		} catch (Exception e) {
			System.err.println("Error when checking if friend request already exists:");
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
        return requestAlreadyExists;
    }
}