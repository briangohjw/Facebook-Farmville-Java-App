package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import entities.GiftThread;
import entities.PostThread;
import entities.Thread;
import entities.User;

/**
 * The ThreadDAO class provides access to the thread table in the database.
 *
 * @version 1.2 06 Apr 2020
 * @author Brian Goh
 */   
public class ThreadDAO extends DAO {

	/**
	 * Insert a thread into database
	 * 
	 * @param thread	thread to add
	 * @return			true upon successful addition, false otherwise
	 */
    public boolean add(Thread thread) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int addSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL insert statement
			String query = "INSERT INTO THREAD (sender_username, receiver_username, time_stamp, text) VALUES (?, ?, ?, ?)";

			// Create the prepared statement from query
            preparedStmt = conn.prepareStatement(query);
			
			// Get required fields for query
            String senderUsername = thread.getSenderUsername();
            String receiverUsername = thread.getReceiverUsername();
            String text = null;
			Timestamp timeStamp = (Timestamp)thread.getTimePosted();

			// Get text if thread is a post thread (no gift)
            if(thread instanceof PostThread) {
                text = ((PostThread)thread).getText();
            }

			// Set prepared statement placeholder values
			preparedStmt.setString(1, senderUsername);
            preparedStmt.setString(2, receiverUsername);
            preparedStmt.setTimestamp(3, timeStamp);
			preparedStmt.setString(4, text);

			// Execute the prepared statement
			addSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when adding thread:");
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
	 * Retrieve five most recent threads to show on a user's news feed
	 * 
	 * @param user	user to retrieve threads for
	 * @return		list of threads to display on a user's news feed
	 */
    public List<Thread> getFeedThreads(User user) {
		// Get a list of user's friends
		FriendsDAO friendsDAO = new FriendsDAO();
		List<User> receivers = friendsDAO.getFriends(user);

		// Get a list of usernames of users who could appear on user's news feed
		receivers.add(user);
		List<String> receiverUsernames = new ArrayList<>();
		for(User receiver: receivers) {
			receiverUsernames.add(receiver.getUsername());
		}

		// Create appropriate number of placeholders (?)
		int receiverUsernameSize = receiverUsernames.size();
		String placeholders = "";
		for(int i = 0; i < receiverUsernameSize; i++) {
			placeholders += "?, ";
		}
		placeholders = placeholders.substring(0, placeholders.length() - 2);

		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		List<Thread> feedThreads = new ArrayList<>();

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL select statement (5 most recent non-gift threads)
			String threadQuery = String.format("SELECT t1.sender_username, t2.time_stamp, receiver_username, text FROM (SELECT DISTINCT * FROM (SELECT sender_username, time_stamp FROM thread WHERE receiver_username IN (%s) AND text IS NOT NULL UNION SELECT sender_username, time_stamp FROM thread_tagged WHERE tagged_username IN (%s)) t ORDER BY time_stamp DESC LIMIT 5) t1 INNER JOIN thread t2 ON t1.sender_username = t2.sender_username AND t1.time_stamp = t2.time_stamp ORDER BY time_stamp DESC", placeholders, placeholders);

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(threadQuery);

			// Set prepared statement placeholder values
			for(int i = 0; i < receiverUsernameSize * 2; i++) {
				preparedStmt.setString(i+1, receiverUsernames.get(i % receiverUsernameSize));
			}

			// Execute the prepared statement
            rs = preparedStmt.executeQuery();

			// Loop through retrieved threads
            while(rs.next()) {
				String senderUsername = rs.getString("sender_username");
				String receiverUsername = rs.getString("receiver_username");
				java.util.Date timeStamp = rs.getTimestamp("time_stamp");
				String text = rs.getString("text");

				Thread thisThread = new PostThread(senderUsername, receiverUsername, timeStamp, text);
				feedThreads.add(thisThread);
            }
		} catch (Exception e) {
			System.err.println("Error when getting threads:");
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
        return feedThreads;
	}
	
	/**
	 * Retrieve five most recent threads to show on a user's wall
	 * 
	 * @param user	user to retrieve threads for
	 * @return		list of threads to display on a user's news feed
	 */
	public List<Thread> getWallThreads(User user) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet threadRs = null;
		List<Thread> wallThreads = new ArrayList<>();

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL select statement 
			String threadQuery = "SELECT t1.sender_username, t2.time_stamp, receiver_username, text FROM (SELECT DISTINCT sender_username, time_stamp FROM thread WHERE receiver_username = ? UNION SELECT sender_username, time_stamp FROM thread_tagged WHERE tagged_username = ? ORDER BY time_stamp DESC LIMIT 5) t1 INNER JOIN thread t2 ON t1.sender_username = t2.sender_username AND t1.time_stamp = t2.time_stamp ORDER BY time_stamp DESC";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(threadQuery);

			// Get required fields for query
			String receiverUsername = user.getUsername();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, receiverUsername);
			preparedStmt.setString(2, receiverUsername);

			// Execute the prepared statement
			threadRs = preparedStmt.executeQuery();

			// Loop through retrieved threads
            while(threadRs.next()) {
				String senderUsername = threadRs.getString("sender_username");
				java.util.Date timeStamp = threadRs.getTimestamp("time_stamp");
				String text = threadRs.getString("text");

				// Get crop name of crop in gift (if present, else null)
				GiftDAO giftDAO = new GiftDAO();
				String giftCropName = giftDAO.getGiftCropName(senderUsername, timeStamp);

				// Create PostThread or GiftThread depending on whether gift exists
				Thread thisThread;
				if(giftCropName == null) {
					thisThread = new PostThread(senderUsername, receiverUsername, timeStamp, text);
				} else {
					thisThread = new GiftThread(senderUsername, receiverUsername, timeStamp, giftCropName);
				}
				wallThreads.add(thisThread);
            }
		} catch (Exception e) {
			System.err.println("Error when getting threads:");
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
				if(threadRs != null) {
					threadRs.close();
				}
			} catch(Exception e) {
				System.err.println("Error when closing:");
				System.err.println(e.getMessage());
			}
		}
        return wallThreads;
	}

	/**
	 * Delete a thread from all walls it was posted on
	 * 
	 * @param thread	thread to delete
	 * @return			true upon successful deletion, false otherwise
	 */
    public boolean deleteThreadCompletely(Thread thread) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int deleteSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL delete statement
			String query = "DELETE FROM THREAD WHERE sender_username=? AND time_stamp=?";
            preparedStmt = conn.prepareStatement(query);
			
			// Get required fields for query
			String senderUsername = thread.getSenderUsername();
			Timestamp timePosted = (Timestamp)thread.getTimePosted();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, senderUsername);
			preparedStmt.setTimestamp(2, timePosted);

			// Execute the prepared statement
			deleteSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when deleting thread completely:");
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
	 * Remove a thread from specified receiver's (whose wall was posted on) wall
	 * 
	 * @param thread	thread to delete from receiver's wall
	 * @return			true upon successful deletion, false otherwise
	 */
	public boolean deleteReceiverFromThread(Thread thread) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int deleteSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL update statement
			String query = "UPDATE THREAD SET receiver_username=null WHERE sender_username=? AND time_stamp=?";

			// Create the prepared statement from query
            preparedStmt = conn.prepareStatement(query);
		   
			// Get required fields for query
            String senderUsername = thread.getSenderUsername();
			Timestamp timePosted = (Timestamp)thread.getTimePosted();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, senderUsername);
			preparedStmt.setTimestamp(2, timePosted);

			// Execute the prepared statement
			deleteSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when deleting receiver from thread:");
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