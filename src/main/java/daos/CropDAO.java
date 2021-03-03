package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entities.Crop;

/**
 * The CropDAO class provides access to the crop table in the database.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class CropDAO extends DAO {  

	/**
	 * Returns a specified crop from the crop table
	 * 
	 * @param cropName	name of crop to retrieve
	 * @return			the retrieved crop
	 */
	public Crop getCrop(String cropName) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		Crop crop = null;

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM CROP WHERE crop_name=?";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Set prepared statement placeholder values
			preparedStmt.setString(1, cropName);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			if(rs.next()) {
				int cost = rs.getInt("cost");
				int matureTime = rs.getInt("mature_time");
				int xp = rs.getInt("xp");
				int minYield = rs.getInt("min_yield");
				int maxYield = rs.getInt("max_yield");
				int sellingPrice = rs.getInt("selling_price");
				crop = new Crop(cropName, minYield, maxYield, sellingPrice, cost, matureTime, xp);
			}
		} catch (Exception e) {
			System.err.println("Error when getting crop:");
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
		return crop;
	}

	/**
	 * Returns all crops in the crop table
	 * 
	 * @return	list of retrieved crops
	 */
	public List<Crop> getAllCrops() {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		List<Crop> crops = new ArrayList<>();

		try {
			// Create the SQL database connection
			conn = getConnection();
			
			// Create the SQL select statement 
			String query = "SELECT * FROM CROP";

			// Create the prepared statement from query
			preparedStmt = conn.prepareStatement(query);

			// Execute the prepared statement
			rs = preparedStmt.executeQuery();

			// Get results from result set
			while(rs.next()) {
				String cropName = rs.getString("crop_name");
				int cost = rs.getInt("cost");
				int matureTime = rs.getInt("mature_time");
				int xp = rs.getInt("xp");
				int minYield = rs.getInt("min_yield");
				int maxYield = rs.getInt("max_yield");
				int sellingPrice = rs.getInt("selling_price");

				Crop crop = new Crop(cropName, minYield, maxYield, sellingPrice, cost, matureTime, xp);
				crops.add(crop);
			}
		} catch (Exception e) {
			System.err.println("Error when getting all crops:");
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
}