package cityfarmers.store;

import java.util.List;

import entities.Crop;
import entities.User;

/**
 * The StoreCtrl class facilitates the interaction between the database and the StoreMenu
 *  
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class StoreCtrl {
    /** CDM provides access to the Crop table in the database */
    private StoreManager storeManager;

    /** Creates a StoreCtrl object with a default UDM, UCDM and UDM*/
    public StoreCtrl() {
        storeManager = new StoreManager();
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
        return storeManager.purchase(cropChoice, amount, loggedInUser);
    }

    /**
     * Returns all of user's crops
     * @return list of all crops
     */
    public List<Crop> getAllCrops() {
        return storeManager.getAllCrops();
    }

}

