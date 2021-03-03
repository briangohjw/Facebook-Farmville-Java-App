package social.wall;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import entities.Crop;
import entities.Thread;
import entities.User;

/**
 * The WallCtrl class facilitates the interaction between the database and the WallMenu or FriendWallMenu
 *  
 * @version 1.3 05 Apr 2020
 * @author Wa Thone
 */
public class WallCtrl {

    /** WallGetter retrieves threads from a wall */
    private WallGetter wallGetter;

    /** GiftAccepter accepts a user's gifts */
    private GiftAccepter giftAccepter;

    /** WallUtility contains the utility functions for dealing with walls */
    private WallUtility wallUtility;

    /** WallPoster posts threads to a wall */
    private WallPoster wallPoster;

    /** Creates a WallCtrl object with a default Gift, User, Thread, Friend, and Tagged DM */
    public WallCtrl() {
        wallGetter = new WallGetter();
        giftAccepter = new GiftAccepter();
        wallUtility = new WallUtility();
        wallPoster = new WallPoster();
    }

    /**
     * Gets a List of up to top 5 Threads on the user's wall
     * 
     * @param user  the User object of the user
     * @return      List of Thread objects of up to top 5 threads on the user's wall
     */
    public List<Thread> getWallThreads(User user) {
        return wallGetter.getWallThreads(user);
    }

    /**
     * Accepts all the gifts that the user has not accepted yet
     * 
     * @param user  the User object of the user
     * @return      the Map object, key = Crop, value = no. of times the crop was accepted
     */
    public Map<Crop, Integer> acceptGifts(User user) {
        return giftAccepter.acceptGifts(user); 
    }

    /**
     * Prompts user continuously until a non empty input is given
     * 
     * @param sc    the scanner that takes in user's input
     * @return      the non empty message
     */
    public String getNonNullMessage(Scanner sc) {
        return wallUtility.getNonNullMessage(sc);
    }

    /**
     * Posts the specified message on user's own wall
     * 
     * @param message   the message posted
     * @param user      the User object of the user
     * @return          true if posting is successful, false otherwise.
     */
    public boolean postOnWall(String message, User user) {
        return wallPoster.postOnWall(message, user, user);
    }

    /**
     * Posts the specified message on receiver's wall by sender
     * 
     * @param message   the message posted
     * @param sender    the User object of the sender
     * @param receiver  the User object of the receiver
     * @return          true if posting is successful, false otherwise.
     */
    public boolean postOnWall(String message, User sender, User receiver) {
        return wallPoster.postOnWall(message, sender, receiver);
    }

    /**
     * Displays the information of the user specified
     * 
     * @param user  the User object of the user
     */
    public void displayUserInfo(User user) {
        wallUtility.displayUserInfo(user);
    }

    /**
     * Gets a List of User objects of friends of the user
     * 
     * @param user  the User object of the user
     * @return      List of User objects of friends of the user
     */
    public List<User> getFriends(User user) {
        return wallUtility.getFriends(user);
    }

    /**
     * Checks if user1 is friends with user2
     * 
     * @param user1 the User object of the user1
     * @param user2 the User object of the user2
     * @return      true of user1 and user2 are friends, false otherwise.
     */
    public boolean isFriend(User user1, User user2) {
        return wallUtility.isFriend(user1, user2);
    }
}