package cityfarmers.sendgift;

import java.util.List;

import daos.CropDAO;
import daos.FriendsDAO;
import daos.GiftDAO;
import daos.UserDAO;
import entities.Crop;
import entities.User;

/**
 * The SendGiftCtrl class facilitates the interaction between the database and the SendGiftMenu
 * 
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class GiftManager {
    /** CDM provides access to the Crop table in the database */
    private CropDAO CDM;

    /** GDM provides access to the Gift table in the database */
    private GiftDAO GDM;

    /** FDM provides access to the Friends table in the database */
    private FriendsDAO FDM;

    /** FDM provides access to the Friends table in the database */
    private UserDAO UDM;
    
    /** Creates a SendGiftCtrl object with a default CDM, GDM and FDM*/
    public GiftManager() {
        CDM = new CropDAO();
        GDM = new GiftDAO();
        FDM = new FriendsDAO();
        UDM = new UserDAO();
    }

    /**
     * Returns all of the user's friends
     * 
     * @param username      friend's name
     * @param loggedInUser  the user
     * @return              list of user's friends
     */
    public boolean isFriend(String username, User loggedInUser) {
        User friend = UDM.getUser(username);
        return FDM.isFriend(friend, loggedInUser);
    }

    /**
     * Checks if the username entered is valid.
     * 
     * @param username  the user
     * @return          true is user is valid, false otherwise
     */
    public boolean isValidUser(String username) {
        if (UDM.getUser(username) != null){
            return true;
        }
        return false;
    }

    /**
     * Checks if the user has already sent 5 gifts today.
     * 
     * @param sender  the user
     * @return        true if user has sent five gifts today, false otherwise
     */
    public boolean haveSentFiveGiftsToday(User sender) {

        return GDM.haveSentFiveGiftsToday(sender);
    }

    /**
     * Checks if the user has sent to his friend today.
     * 
     * @param sender    the user
     * @param receiver  the friend
     * @return          true if user has sent to the friend today, false otherwise
     */
    public boolean haveSentGiftToThisUserToday(User sender, String receiver) {
        User friendObject = UDM.getUser(receiver);

        return GDM.haveSentGiftToThisUserToday(sender,friendObject);
    }

    /**
     * Checks if the user has sent to his friend today.
     * 
     * @param sender    the user
     * @param receiver  the friend
     * @param crop      the crop to send
     * @return          true if gifting to friend is successful, false otherwise
     */
    public boolean sendGift(User sender, String receiver, Crop crop) {
        User friendObject = UDM.getUser(receiver);
        return GDM.sendGift(sender,friendObject,crop);
    }


    /**
     * Returns all of user's crops
     * 
     * @return  list of user's crops
     */
    public List<Crop> getAllCrops() {
        return CDM.getAllCrops();
    }

}