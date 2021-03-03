package cityfarmers.inventory;

import java.util.Map;

import daos.UserCropDAO;
import entities.Crop;
import entities.User;

/**
 * The InventoryManager class facilitates the interaction between the database and controllers 
 * requiring Inventory methods
 *  
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class InventoryManager {

    /** UCDM provides access to the UserCrop table in the database */
    private UserCropDAO UCDM;

    /** Creates a InventoryCtrl object with a default UCDM */
    public InventoryManager() {
        UCDM = new UserCropDAO();
    }

    /**
     *Returns all of the user's crops.
     *
     * @param loggedInUser  the user
     * @return              a map of the corresponding amount of crops a user has
     */
    public Map<Crop, Integer> getUserCrops(User loggedInUser) {
        return UCDM.getUserCrops(loggedInUser);
    }

        /**
     *Updates user crops and its corresponding quantity.
     *
     * @param loggedInUser  the user
     * @param cropChoice    choice of crop
     * @param amount        amount to reduce by
     */
    public void updateUserCrops(User loggedInUser, Crop cropChoice, int amount) {
        UCDM.updateUserCrops(loggedInUser, cropChoice, amount);
    }


}