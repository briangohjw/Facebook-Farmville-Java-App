package social.friends;

import java.util.List;

import daos.FriendRequestDAO;
import entities.User;

/**
 * The RequestAccepter class retrieves, accepts and rejects friend requests
 *  
 * @version 1.0 07 Apr 2020
 * @author Brian Goh
 */
public class RequestAccepter {

    /** FRDM provides access to the Friend_requests table in the database */
    private FriendRequestDAO FRDM;

    /** Creates a RequestAccepter object with a default Request DM */
    public RequestAccepter() {
        FRDM = new FriendRequestDAO();
    }

    /**
     * Gets a List of User objects of friend requests received by the user specified
     * 
     * @param user  the User object
     * @return      List of User objects of friend requests of the user
     */
    public List<User> getFriendRequests(User user) {
        return FRDM.getFriendRequests(user);
    }

    /**
     * Accepts friend request from the specified requestor user by receiver
     * @param requestor     the User object of the requestor
     * @param receiver      the User object of the receiver
     * @return              true upon successful accepting, false otherwise
     */
    public boolean acceptFriendRequest(User requestor, User receiver) {
        return FRDM.acceptFriendRequest(requestor, receiver);        
    }

    /**
     * rejects friend request from the specified requestor user by the receiver
     * @param requestor     the User object of the requestor
     * @param receiver      the User object of the receiver
     * @return              true upon successful rejecting, false otherwise
     */
    public boolean rejectFriendRequest(User requestor, User receiver) {
        return FRDM.rejectFriendRequest(requestor, receiver);
    }
}