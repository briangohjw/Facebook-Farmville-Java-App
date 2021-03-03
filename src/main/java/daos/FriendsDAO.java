package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.User;

/**
 * The FriendsDAO class provides access to the friends table in the database.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class FriendsDAO extends DAO {

	/**
	 * Retrieves a specified user's friends
	 * 
	 * @param user	user to retrieve friends of
	 * @return		list of specified user's friends
	 */
    public List<User> getFriends(User user) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		List<User> friends = new ArrayList<>();

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT f.username1 friend_users FROM FRIENDS f WHERE f.username2=? UNION SELECT t.username2 FROM FRIENDS t WHERE t.username1=? ORDER BY friend_users";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String username = user.getUsername();

			// Set prepared statement placeholder values
            preparedStmt.setString(1, username);
            preparedStmt.setString(2, username);

			// Execute the prepared statement
            rs = preparedStmt.executeQuery();

			// Get results from result set
            while(rs.next()) {
				String friendUsername = rs.getString("friend_users");
				UserDAO userDAO = new UserDAO();
				
				User friend = userDAO.getUser(friendUsername);
                friends.add(friend);
            }
		} catch (Exception e) {
			System.err.println("Error when getting friends:");
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
        return friends;
	}
	
	/**
	 * Checks if two users are friends
	 * 
	 * @param user1	first specified user
	 * @param user2	second specified user
	 * @return		true if specified users are friends, false otherwise
	 */
	public boolean isFriend(User user1, User user2) {
		// Get list of friends of user1
		List<User> user1Friends = getFriends(user1);
		for (User user: user1Friends){
			if (user.getUsername().equals(user2.getUsername())){
				return true;
			}
		}

		return false;
	}

	/**
	 * Deletes friendship between two users
	 * 
	 * @param user1	first specified user
	 * @param user2	second specified user
	 * @return		true upon successful deletion, false otherwise
	 */
    public boolean deleteFriend(User user1, User user2) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int deleteSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL delete statement
			String query = "DELETE FROM FRIENDS WHERE (username1=? AND username2=?) OR (username2=? AND username1=?)";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String username1 = user1.getUsername();
			String username2 = user2.getUsername();
			
			// Set prepared statement placeholder values
            preparedStmt.setString(1, username1);
            preparedStmt.setString(2, username2);
            preparedStmt.setString(3, username1);
            preparedStmt.setString(4, username2);

			// Execute the prepared statement
			deleteSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when deleting friends:");
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
				System.err.println("Exception when closing!");
				System.err.println(e.getMessage());
			}
		}
		return (deleteSuccess != 0);
	}
}