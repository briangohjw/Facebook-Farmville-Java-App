package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import entities.Thread;
import entities.User;

/**
 * The TaggedDAO class provides access to the thread_tagged table in the database.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class TaggedDAO extends DAO {

	/**
	 * Add list of tagged users to a thread
	 * 
	 * @param thread			thread that contains tags
	 * @param taggedUsernames	list of usernames to tag in thread
	 * @return					true upon successful tagging, false otherwise
	 */
    public boolean addTaggedUsers(Thread thread, List<String> taggedUsernames) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int addSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Get required fields for query
            String threaderUsername = thread.getSenderUsername();
            Timestamp threadTimeStamp = (Timestamp)thread.getTimePosted();

            // Loop through tagged usernames and insert into database
            for(String taggedUsername : taggedUsernames) {
				// Create the SQL select statement 
				String query = "INSERT INTO THREAD_TAGGED (sender_username, time_stamp, tagged_username) VALUES (?, ?, ?)";
				
				// Create the prepared statement from query
                preparedStmt = conn.prepareStatement(query);
				
				// Set prepared statement placeholder values
                preparedStmt.setString(1, threaderUsername);
                preparedStmt.setTimestamp(2, threadTimeStamp);
                preparedStmt.setString(3, taggedUsername);

                // Execute the prepared statement
                addSuccess = preparedStmt.executeUpdate();
            }
		} catch (Exception e) {
			System.err.println("Error when adding tagged user:");
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
	 * Get username of users who are tagged in specified thread
	 * 
	 * @param thread	thread to get tagged usernames from
	 * @return			list of usernames of users tagged in specified thread
	 */
    public List<String> getTaggedUsernames(Thread thread) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		List<String> taggedUsernames = new ArrayList<>();

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM THREAD_TAGGED WHERE sender_username=? AND time_stamp=?";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String senderUsername = thread.getSenderUsername();
			Timestamp timePosted = (Timestamp)thread.getTimePosted();

			// Set prepared statement placeholder values
            preparedStmt.setString(1, senderUsername);
            preparedStmt.setTimestamp(2, timePosted);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			while(rs.next()) {
                String taggedUsername = rs.getString("tagged_username");
                taggedUsernames.add(taggedUsername);
			}
		} catch (Exception e) {
			System.err.println("Error when getting tagged usernames:");
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
		return taggedUsernames;
	}

	/**
	 * Delete tagged user from specified thread
	 * 
	 * @param taggedUser	user to untag from thread
	 * @param thread		thread to untag user from
	 * @return				true upon successful untagging, false otherwise
	 */
	public boolean deleteTaggedFromThread(User taggedUser, Thread thread) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int deleteSuccess = 0;

    	try {
			// Create the SQL database connection
			conn = getConnection();
		
			// Create the SQL delete statement 
			String query = "DELETE FROM THREAD_TAGGED WHERE sender_username=? AND tagged_username=? AND time_stamp=?";
			
			// Create the prepared statement from query
            preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String senderUsername = thread.getSenderUsername();
			String taggedUsername = taggedUser.getUsername();
			Timestamp timePosted = (Timestamp)thread.getTimePosted();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, senderUsername);
			preparedStmt.setString(2, taggedUsername);
			preparedStmt.setTimestamp(3, timePosted);
	
			// Execute the prepared statement
			deleteSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when untagging user from thread:");
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