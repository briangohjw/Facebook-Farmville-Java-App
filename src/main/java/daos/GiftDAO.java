package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import entities.Crop;
import entities.User;

/**
 * The GiftDAO class provides access to the gift table in the database.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */  
public class GiftDAO extends DAO {

	/**
	 * Sends a gift to another user by updating the thread and gift tables
	 * 
	 * @param sender	user who sends the gift
	 * @param receiver	user who receives the gift
	 * @param crop		crop that the gift contains
	 * @return			true upon successful sending, false otherwise
	 */
     public boolean sendGift(User sender, User receiver, Crop crop) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int threadSuccess = 0;
		int giftSuccess = 0;
		
		// Get current timestamp
        java.util.Date date= new java.util.Date();
        long time = date.getTime();
        java.sql.Timestamp timeSent = new java.sql.Timestamp(time);

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL insert statement
			String threadQuery = "INSERT INTO THREAD (sender_username, receiver_username, time_stamp) VALUES (?, ?, ?)";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(threadQuery);
			
			// Get required fields for query
			String senderUsername = sender.getUsername();
			String receiverUsername = receiver.getUsername();
			
			// Set prepared statement placeholder values
        	preparedStmt.setString(1, senderUsername);
			preparedStmt.setString(2, receiverUsername);
			preparedStmt.setTimestamp(3, timeSent);

			// Execute the prepared statement
			threadSuccess = preparedStmt.executeUpdate();
			
			// Close prepared statement before running next query
			if(preparedStmt != null) {
				preparedStmt.close();
			}
            
			// Create the SQL insert statement
			String giftQuery = "INSERT INTO GIFT (sender_username, receiver_username, time_stamp, crop_name) VALUES (?, ?, ?, ?)";
			
			// Create the prepared statement from query
            preparedStmt = conn.prepareStatement(giftQuery);

			// Set prepared statement placeholder values
        	preparedStmt.setString(1, senderUsername);
			preparedStmt.setString(2, receiverUsername);
            preparedStmt.setTimestamp(3, timeSent);
            preparedStmt.setString(4, crop.getCropName());

			// Execute the prepared statement
			giftSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when sending gift:");
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
		return (threadSuccess + giftSuccess != 0);
	}

	/**
	 * Returns crop name of crop stored in a gift
	 * 
	 * @param senderUsername	username of user who sent the gift
	 * @param timeStamp			timestamp that gift is sent
	 * @return					crop name of crop, null if gift does not exist
	 */
    public String getGiftCropName(String senderUsername, java.util.Date timeStamp) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		String cropName = null;

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM GIFT WHERE sender_username=? AND time_stamp=?";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Set prepared statement placeholder values
            preparedStmt.setString(1, senderUsername);
            preparedStmt.setTimestamp(2, (Timestamp)timeStamp);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			if(rs.next()) {
                cropName = rs.getString("crop_name");
			}
		} catch (Exception e) {
			System.err.println("Error when getting gift's crop name:");
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
		return cropName;
	}

	/**
	 * Accepts all gifts received by a user
	 * 
	 * @param user	user that is accepting gifts
	 * @return		map storing types and number of crops received
	 */
    public Map<Crop, Integer> acceptGifts(User user) {
        Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet giftRs = null;
        Map<Crop, Integer> acceptedGifts = new HashMap<>();

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement
			String giftQuery = "SELECT * FROM GIFT WHERE receiver_username=? AND accepted = 0";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(giftQuery);

			// Set prepared statement placeholder values
            preparedStmt.setString(1, user.getUsername());

			// Execute the prepared statement
			giftRs = preparedStmt.executeQuery();

			// Get results from result set
			while(giftRs.next()) {
                String cropName = giftRs.getString("crop_name");
                CropDAO cropDAO = new CropDAO();
				Crop crop = cropDAO.getCrop(cropName);

				// Add crop to hashmap of accepted gifts
                if(acceptedGifts.containsKey(crop)) {
                    acceptedGifts.put(crop, acceptedGifts.get(crop) + 1);
                } else {
                    acceptedGifts.put(crop, 1);
                }

                // Update user's inventory with this received crop
                UserCropDAO userCropDAO = new UserCropDAO();
				userCropDAO.updateUserCrops(user, crop, 1);
			}
			
			// Close prepared statement before running next query
			if(preparedStmt != null) {
				preparedStmt.close();
			}

			// Create the SQL update statement
            String updateAcceptedQuery = "UPDATE GIFT SET accepted=1 WHERE receiver_username=?";
			
			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(updateAcceptedQuery);

			// Set prepared statement placeholder values
			preparedStmt.setString(1, user.getUsername());

			// Execute the prepared statement
			preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when accepting gifts:");
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
				if(giftRs != null) {
					giftRs.close();
				}
			} catch(Exception e) {
				System.err.println("Error when closing:");
				System.err.println(e.getMessage());
			}
		}
		return acceptedGifts;
	}

	/**
	 * Checks if user has already sent five gifts today
	 * 
	 * @param sender	user to check
	 * @return			true if user has sent five gifts today, false otherwise
	 */
	public boolean haveSentFiveGiftsToday(User sender) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		boolean haveSentFiveGiftsToday = false;

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT COUNT(*) num_gifts FROM GIFT WHERE sender_username=? AND DATE(time_stamp)=DATE(?)";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String senderUsername = sender.getUsername();
			java.util.Date date= new java.util.Date();
			long time = date.getTime();
			Timestamp currentTime = new Timestamp(time);

			// Set prepared statement placeholder values
			preparedStmt.setString(1, senderUsername);
			preparedStmt.setTimestamp(2, currentTime);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			if(rs.next()) {
				int countGiftsToday = rs.getInt("num_gifts");
				if(countGiftsToday >= 5) {
					haveSentFiveGiftsToday = true;
				}
			}
		} catch (Exception e) {
			System.err.println("Error when checking if user has sent 5 gifts today:");
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
		return haveSentFiveGiftsToday;
	}

	/**
	 * Check if sender user has already sent a gift to receiver user today
	 * 
	 * @param sender	gift sender user
	 * @param receiver	gift receiver user
	 * @return			true if sender already sent gift to receiver today, false otherwise
	 */
	public boolean haveSentGiftToThisUserToday(User sender, User receiver) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		boolean haveSentGiftToThisUserToday = false;

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM GIFT WHERE sender_username=? AND receiver_username=? AND DATE(time_stamp)=DATE(?)";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String senderUsername = sender.getUsername();
			String receiverUsername = receiver.getUsername();
			java.util.Date date= new java.util.Date();
			long time = date.getTime();
			Timestamp currentTime = new Timestamp(time);

			// Set prepared statement placeholder values
			preparedStmt.setString(1, senderUsername);
			preparedStmt.setString(2, receiverUsername);
			preparedStmt.setTimestamp(3, currentTime);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();
 
			// If there are row(s) returned
			if (rs.isBeforeFirst()) {     
				rs.next();
				haveSentGiftToThisUserToday = true;
			}
		} catch (Exception e) {
			System.err.println("Error when checking if User has sent gift to receiver today:");
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
		return haveSentGiftToThisUserToday;
	}
}