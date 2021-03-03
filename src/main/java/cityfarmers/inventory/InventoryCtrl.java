package cityfarmers.inventory;

import java.util.Map;

import entities.Crop;
import entities.User;

/**
 * The InventoryCtrl class facilitates the interaction between the database and the InventoryMenu
 *  
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class InventoryCtrl {
    /** UCDM provides access to the UserCrop table in the database */
    private InventoryManager inventoryManager;

    /** Creates a InventoryCtrl object with a default InventoryManager */
    public InventoryCtrl() {
        inventoryManager = new InventoryManager();
    }

    /**
     *Returns all of the user's crops.
     *
     * @param loggedInUser  the user
     * @return              a map of the corresponding amount of crops a user has
     */
    public Map<Crop, Integer> getUserCrops(User loggedInUser) {
        return inventoryManager.getUserCrops(loggedInUser);
    }

}
