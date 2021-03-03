package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import entities.User;

/**
 * The UserDAO class provides access to the user table in the database.
 *
 * @version 1.5 05 Apr 2020
 * @author Brian Goh
 */   
public class UserDAO extends DAO {

	/**
	 * Adds a user to the database and create default 5 plots
	 * 
	 * @param user		user to add
	 * @param password	password of user
	 * @return			true upon successful addition, false otherwise
	 */
    public boolean addUser(User user, String password) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int success = 0;

    	try {
			// Create the SQL database connection
			conn = getConnection();
		
			// Create the SQL insert statement 
			String query = "INSERT INTO USER (username, fullname, password, rank, xp, gold) VALUES (?, ?, ?, 'Novice', 0, 50)";
			
			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String username = user.getUsername();
			String fullName = user.getFullName();

			// Format full name as title case
			String[] fullNameSplit = fullName.split("\\s+");
			String fullNameFormatted = "";
			for(String word: fullNameSplit) {
				String firstChar = word.substring(0, 1).toUpperCase();
				String otherChars = "";
				if(word.length() > 1) {
					otherChars += word.substring(1).toLowerCase();
				}
				fullNameFormatted += firstChar + otherChars + " ";
			}
			if(fullNameFormatted.charAt(fullNameFormatted.length() - 1) == ' ') {
				fullNameFormatted = fullNameFormatted.substring(0, fullNameFormatted.length() - 1);
			}

			// Set prepared statement placeholder values
			preparedStmt.setString(1, username);
			preparedStmt.setString(2, fullNameFormatted);
			preparedStmt.setString(3, password);

			// Execute the prepared statement
			success = preparedStmt.executeUpdate();

			// Add default of 5 empty plots
			PlotDAO plotDAO = new PlotDAO();
			for(int i = 0; i < 5; i++) {
				plotDAO.addEmptyPlot(user);
			}
		} catch (Exception e) {
			System.err.println("Error when adding user to database:");
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
		return (success != 0);
	}

	/**
	 * Checks if a user's input username and password matches database
	 * 
	 * @param username			username of user attempting to log in
	 * @param inputtedPassword	inputted password of user attempting to log in
	 * @return					authenticated user if successful, null otherwise
	 */
	public User authenticate(String username, String inputtedPassword) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		User authenticatedUser = null;

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM USER WHERE username=? AND password=?";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, username);
			preparedStmt.setString(2, inputtedPassword);
			
			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// If there are row(s) returned
			if (rs.isBeforeFirst()) {  
				rs.next();   
				String fullName = rs.getString("fullname");
				String rank = rs.getString("rank");
				int xp = rs.getInt("xp");
				int gold = rs.getInt("gold");

				authenticatedUser = new User(username, fullName, rank, xp, gold);
			}
		} catch (Exception e) {
			System.err.println("Error when authenticating user:");
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
		return authenticatedUser;
	}

	/**
	 * Returns a user with the specified username
	 * 
	 * @param username	username of user to retrieve
	 * @return			user of specified username, null if not found
	 */
	public User getUser(String username) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		User returnUser = null;

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM USER WHERE username=?";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, username);
			
			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			if(rs.next()) {
				String fullName = rs.getString("fullname");
				String rank = rs.getString("rank");
				int xp = rs.getInt("xp");
				int gold = rs.getInt("gold");
					
				returnUser = new User(username, fullName, rank, xp, gold);
			}
		} catch (Exception e) {
			System.err.println("Error when retrieving user:");
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
		return returnUser;
	}

	/**
	 * Returns wealth ranking of a specified user among his friends
	 * 
	 * @param user	user to retrieve ranking of
	 * @return		wealth ranking among user's friends
	 */
	public int getRichRank(User user) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		int richRank = -1;

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT COUNT(*)+1 this_rank FROM (SELECT * FROM (SELECT * FROM user WHERE user.username IN (SELECT f.username1 friend_users FROM FRIENDS f WHERE f.username2=? UNION SELECT t.username2 FROM FRIENDS t WHERE t.username1=?)) friends_info WHERE friends_info.gold > (SELECT gold FROM USER WHERE username = ?)) b";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String username = user.getUsername();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, username);
			preparedStmt.setString(2, username);
			preparedStmt.setString(3, username);
			
			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			if(rs.next()) {
				richRank = rs.getInt("this_rank");
			}
		} catch (Exception e) {
			System.err.println("Error when retrieving user's rich rank:");
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
		return richRank;
	}
	
	/**
	 * Update specified user's information in database
	 * 
	 * @param user	user to update
	 * @return		true upon successful update, false otherwise
	 */
    public boolean update(User user) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int updateSuccess = 0;

    	try {
			// Create the SQL database connection
			conn = getConnection();
		
			// Create the SQL update statement 
			String query = "UPDATE USER SET fullname=?, rank=?, xp=?, gold=? WHERE username=?";
			
			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Set prepared statement placeholder values
			preparedStmt.setString(1, user.getFullName());
			preparedStmt.setString(2, user.getRank());
			preparedStmt.setInt(3, user.getXp());
			preparedStmt.setInt(4, user.getGold());
			preparedStmt.setString(5, user.getUsername());
	
			// Execute the prepared statement
			updateSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when updating user database:");
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
		return (updateSuccess != 0);
    }

	/**
	 * Delete a user
	 * 
	 * @param user	user to delete
	 * @return		true upon successful deletion, false otherwise
	 */
    public boolean delete(User user) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int deleteSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL delete statement
			String query = "DELETE FROM USER WHERE username=?";
            preparedStmt = conn.prepareStatement(query);
			
			// Get required fields for query
			String username = user.getUsername();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, username);

			// Execute the prepared statement
			deleteSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when deleting user:");
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