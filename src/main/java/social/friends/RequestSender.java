package social.friends;

import daos.FriendRequestDAO;
import daos.FriendsDAO;
import daos.UserDAO;
import entities.User;

/**
 * The RequestSender class validates and sends friend requests
 *  
 * @version 1.0 07 Apr 2020
 * @author Brian Goh
 */
public class RequestSender {

    /** UDM provides access to the User table in the database */
    private UserDAO UDM;

    /** FRDM provides access to the Friend_requests table in the database */
    private FriendRequestDAO FRDM;

    /** FDM provides access to the Friends table in the database */
    private FriendsDAO FDM;

    /** Creates a RequestSender object with a default User, Request, Friends DM */
    public RequestSender() {
        UDM = new UserDAO();
        FRDM = new FriendRequestDAO();
        FDM = new FriendsDAO();
    }

    /**
     * Gets the validity type of the friend request sent from specified requestor
     * to user of specified receiver username
     * 
     * @param receiverUsername  the username of the person to send friend request to
     * @param requestor         the User object of the ser sending the request
     * @return                  specific error messages if invalid. Else, returns "valid"
     */
    public String getFriendRequestValidityType(String receiverUsername, User requestor) {
        // check if user exists
        User receiver = UDM.getUser(receiverUsername);

        if (receiver == null) {
            return "User does not exist by the given username: " + receiverUsername;
        }

        // check that they are not already friends
        if (FDM.isFriend(receiver, requestor)) {
            return "You are already friends with " + receiverUsername;
        }

        // check that the receiver has not already sent a request
        if (FRDM.requestAlreadyExists(receiver, requestor)) {
            return receiverUsername + " has already sent you a friend request, please accept it instead.";
        }

        if (FRDM.requestAlreadyExists(requestor, receiver)) {
            return "You have already sent a friend request to " + receiverUsername 
                    + ". Please wait for his / her response.";
        }

        return "valid";
    }

    /**
     * Sends friend request to the specified receiver username from requestor
     * 
     * @param receiverUsername  the username of the user to send request to
     * @param requestor         the User object of the user sending request
     * @return                  true upon successful sending, false otherwise
     */
    public boolean sendFriendRequest(String receiverUsername, User requestor) {
        User receiver = UDM.getUser(receiverUsername);

        return FRDM.addFriendRequest(requestor, receiver);
    }
}