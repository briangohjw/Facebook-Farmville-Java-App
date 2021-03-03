package cityfarmers.sendgift;

import java.util.List;

import entities.Crop;
import entities.User;

/**
 * The SendGiftCtrl class facilitates the interaction between the database and the SendGiftMenu
 * 
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class SendGiftCtrl {
    /** giftManager provides access to the giftManager methods */
    private GiftManager giftManager;

    /** Creates a SendGiftCtrl object with a default CDM, GDM and FDM*/
    public SendGiftCtrl() {
        giftManager = new GiftManager();
    }
    

    /**
     * Returns all of the user's friends
     * 
     * @param username      friend's name
     * @param loggedInUser  the user
     * @return              list of user's friends
     */
    public boolean isFriend(String username, User loggedInUser) {

        return giftManager.isFriend(username, loggedInUser);
    }

    /**
     * Checks if the username entered is valid.
     * 
     * @param username  the user
     * @return          true is user is valid, false otherwise
     */
    public boolean isValidUser(String username) {
        return giftManager.isValidUser(username);
    }

    /**
     * Checks if the user has sent to his friend today.
     * 
     * @param sender    the user
     * @param receiver  the friend
     * @return          true if user has sent to the friend today, false otherwise
     */
    public boolean haveSentGiftToThisUserToday(User sender, String receiver) {
        return giftManager.haveSentGiftToThisUserToday(sender,receiver);
    }

    /**
     * Checks if the user has already sent 5 gifts today.
     * 
     * @param sender  the user
     * @return        true if user has sent five gifts today, false otherwise
     */
    public boolean haveSentFiveGiftsToday(User sender) {

        return giftManager.haveSentFiveGiftsToday(sender);
    }

    /**
     * Returns all of user's crops
     * @return list of all crops
     */
    public List<Crop> getAllCrops() {

        return giftManager.getAllCrops();
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
        return giftManager.sendGift(sender,receiver,crop);
    }
}