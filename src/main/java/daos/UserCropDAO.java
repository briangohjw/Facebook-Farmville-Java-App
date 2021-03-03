package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import entities.Crop;
import entities.User;

/**
 * The UserCropDAO class provides access to the user_crop table in the database.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class UserCropDAO extends DAO {

	/**
	 * Returns all crops and their quantity owned by specified user
	 * 
	 * @param user	user to retrieve crops from
	 * @return		map of specified user's crops and their quantity
	 */
	public Map<Crop, Integer> getUserCrops(User user) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		CropDAO cropDAO = new CropDAO();
		Map<Crop, Integer> crops = new HashMap<>();

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// our SQL SELECT query
			String query = "SELECT * FROM USER_CROP WHERE username=? AND num_crops>0";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, user.getUsername());

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			while(rs.next()) {
				String cropName = rs.getString("crop_name");
				int numCrops = rs.getInt("num_crops");

				crops.put(cropDAO.getCrop(cropName), numCrops);
			}
		} catch (Exception e) {
			System.err.println("Error when getting user crops:");
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
		return crops;
    }
	
	/**
	 * Update the quantity of specified crop owned by specified user
	 * 
	 * @param user		user to update inventory of
	 * @param crop		crop to update
	 * @param change	change in crop quantity (can be negative)
	 * @return			true upon successful update, false otherwise
	 */
    public boolean updateUserCrops(User user, Crop crop, int change) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		int deleteSuccess = 0;

		try {
			// Create the SQL database connection
			conn = getConnection();

			// Create the SQL update statement
			String query = "INSERT INTO USER_CROP (username, crop_name, num_crops) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE username = ?, crop_name = ?, num_crops = num_crops + (?)";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Get required fields for query
			String username = user.getUsername();
			String cropName = crop.getCropName();

			// Set prepared statement placeholder values
			preparedStmt.setString(1, username);
            preparedStmt.setString(2, cropName);
            preparedStmt.setInt(3, change);
            preparedStmt.setString(4, username);
            preparedStmt.setString(5, cropName);
            preparedStmt.setInt(6, change);

			// Execute the prepared statement
			deleteSuccess = preparedStmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("Error when updating user's crops:");
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