package cityfarmers.store;

import java.util.List;

import cityfarmers.inventory.InventoryManager;
import daos.CropDAO;
import daos.UserDAO;
import entities.Crop;
import entities.User;

/**
 * The StoreCtrl class facilitates the interaction between the database and the StoreMenu
 *  
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class StoreManager {
    /** CDM provides access to the Crop table in the database */
    private CropDAO CDM;

     /** UCDM provides access to the UserCrop table in the database */
    private InventoryManager inventoryManager;

    /** UDM provides access to the User table in the database */
    private UserDAO UDM;

    /** Creates a StoreCtrl object with a default UDM, UCDM and UDM*/
    public StoreManager() {
        CDM = new CropDAO();
        inventoryManager = new InventoryManager();
        UDM = new UserDAO();
    }

    /**
     * Checks if amount of crop can be purchased and purchases them.
     * 
     * @param cropChoice    chosen seed
     * @param amount        amount of crop to buy
     * @param loggedInUser  the user
     * @return  true if an item can be and has been purchased, false otherwise
     */
    public boolean purchase(Crop cropChoice, int amount, User loggedInUser) {
        // Check if user has enough gold
        int totalCost = cropChoice.getCost() * amount;

        if (loggedInUser.getGold() < totalCost) {
            System.out.println("Insufficient gold. Please try again.");
            return false;

        } else {
            // UserCropDAO to add number of Crops user has
            // If successful, print success message, else, throw error.
            inventoryManager.updateUserCrops(loggedInUser, cropChoice, amount);
            loggedInUser.setGold(loggedInUser.getGold() - totalCost);
            UDM.update(loggedInUser);
            
            System.out.println( amount + " bags of seeds purchased for " + totalCost + " gold.");
            return true;
        }

    }

    /**
     * Returns all of user's crops
     * @return list of all crops
     */
    public List<Crop> getAllCrops() {
        
        return CDM.getAllCrops();
    }
}

