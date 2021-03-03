package social.friends;

import java.util.List;

import daos.FriendsDAO;
import entities.User;

/**
 * The FriendshipManager class retrieves and updates friendship status
 *  
 * @version 1.0 07 Apr 2020
 * @author Brian Goh
 */
public class FriendshipManager {

    /** FDM provides access to the Friends table in the database */
    private FriendsDAO FDM;

    /** Creates a FriendshipManager object with a default Friends DM */
    public FriendshipManager() {
        FDM = new FriendsDAO();
    }

    /**
     * Gets a List of User objects of friends of the user specified
     * 
     * @param user  the User object
     * @return      List of User objects of friends of the user
     */
    public List<User> getFriends(User user) {
        return FDM.getFriends(user);
    }

    /**
     * Unfriends the specified user1 from user2 and updates the Friends table in database
     * 
     * @param user1     the User object of the friend being unfriended
     * @param user2     the User object of the one doing the unfriending
     * @return          true upon successful unfriending, false otherwise
     */
    public boolean unfriendFriend(User user1, User user2) {
        return FDM.deleteFriend(user1, user2);
    }
}