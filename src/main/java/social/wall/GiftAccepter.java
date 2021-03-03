package social.wall;

import java.util.Map;

import daos.GiftDAO;
import entities.Crop;
import entities.User;

/**
 * The GiftAccepter class accepts a user's gifts
 *  
 * @version 1.0 07 Apr 2020
 * @author Brian Goh
 */
public class GiftAccepter {

    /** GDM provides access to the Gift table in the database */
    private GiftDAO GDM;

    /** Creates a new GiftAccepter object with a default Gift DM */
    public GiftAccepter() {
        GDM = new GiftDAO();
    }

    /**
     * Accepts all the gifts that the user has not accepted yet
     * 
     * @param user  the User object of the user
     * @return      the Map object, key = Crop, value = no. of times the crop was accepted
     */
    public Map<Crop, Integer> acceptGifts(User user) {
        return GDM.acceptGifts(user); 
    }
}