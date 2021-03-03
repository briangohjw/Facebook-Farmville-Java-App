package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import entities.Comment;
import entities.Thread;

/**
 * The CommentDAO class provides access to the comment table in the database.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class CommentDAO extends DAO {

	/**
	 * Adds a comment posted to a thread by a user
	 * 
	 * @param thread	thread that the comment is made on
	 * @param comment	comment that user has posted
	 * @return			true upon successful insertion, false otherwise
	 */
    public boolean addComment(Thread thread, Comment comment) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		int addSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL insert statement
			String query = "INSERT INTO COMMENT (threader_username, thread_time_stamp, commenter_username, comment_time_stamp, text) VALUES (?, ?, ?, ?, ?)";
			
			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
            String threaderUsername = thread.getSenderUsername();
            Timestamp threadTimeStamp = (Timestamp)thread.getTimePosted();
            String commenterUsername = comment.getCommenterUsername();
            Timestamp commentTimeStamp = (Timestamp)comment.getTimeCommented();
            String text = comment.getText();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, threaderUsername);
			preparedStmt.setTimestamp(2, threadTimeStamp);
			preparedStmt.setString(3, commenterUsername);
			preparedStmt.setTimestamp(4, commentTimeStamp);
			preparedStmt.setString(5, text);

			// Execute the prepared statement
			addSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when adding comment:");
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
	 * Returns comments of a thread
	 * 
	 * @param thread	thread to retrieve comments from
	 * @return			list of comments of a thread
	 */
    public List<Comment> getComments(Thread thread) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		List<Comment> comments = new ArrayList<>();

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM COMMENT WHERE threader_username=? AND thread_time_stamp=?";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String threaderUsername = thread.getSenderUsername();
			Timestamp threadTimeStamp = (Timestamp)thread.getTimePosted();

			// Set prepared statement placeholder values
            preparedStmt.setString(1, threaderUsername);
            preparedStmt.setTimestamp(2, threadTimeStamp);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			while(rs.next()) {
                String commenterUsername = rs.getString("commenter_username");
                Timestamp commentTimeStamp = rs.getTimestamp("comment_time_stamp");
				String text = rs.getString("text");
				
				Comment comment = new Comment(commenterUsername, commentTimeStamp, text);
				comments.add(comment);
			}
		} catch (Exception e) {
			System.err.println("Error when getting comments from thread:");
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
		return comments;
	}
}