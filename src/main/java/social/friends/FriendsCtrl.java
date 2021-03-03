package social.friends;

import java.util.List;

import entities.User;

/**
 * The FriendsCtrl class facilitates the interaction between the database and the FriendsMenu
 *  
 * @version 1.1 05 Apr 2020
 * @author Wa Thone
 */
public class FriendsCtrl {

    /** FriendshipManager retrieves and updates friendship status */
    private FriendshipManager friendshipManager;

    /** RequestAccepter retrieves, accepts and rejects friend requests */
    private RequestAccepter requestAccepter;

    /** RequestSender validates and sends friend requests */
    private RequestSender requestSender;

    /** 
     * Creates a FriendsCtrl object with a default 
     * FriendshipManager, RequestAccepter, RequestSender 
     */
    public FriendsCtrl() {
        friendshipManager = new FriendshipManager();
        requestAccepter = new RequestAccepter();
        requestSender = new RequestSender();
    }

    /**
     * Gets a List of User objects of friends of the user specified
     * 
     * @param user  the User object
     * @return      List of User objects of friends of the user
     */
    public List<User> getFriends(User user) {
        return friendshipManager.getFriends(user);
    }

    /**
     * Gets a List of User objects of friend requests received by the user specified
     * 
     * @param user  the User object
     * @return      List of User objects of friend requests of the user
     */
    public List<User> getFriendRequests(User user) {
        return requestAccepter.getFriendRequests(user);
    }

    /**
     * Unfriends the specified user1 from user2 and updates the Friends table in database
     * 
     * @param user1     the User object of the friend being unfriended
     * @param user2     the User object of the one doing the unfriending
     * @return          true upon successful unfriending, false otherwise
     */
    public boolean unfriendFriend(User user1, User user2) {
        return friendshipManager.unfriendFriend(user1, user2);
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
        return requestSender.getFriendRequestValidityType(receiverUsername, requestor);
    }

    /**
     * Sends friend request to the specified receiver username from requestor
     * 
     * @param receiverUsername  the username of the user to send request to
     * @param requestor         the User object of the user sending request
     * @return                  true upon successful sending, false otherwise
     */
    public boolean sendFriendRequest(String receiverUsername, User requestor) {
        return requestSender.sendFriendRequest(receiverUsername, requestor);
    }

    /**
     * Accepts friend request from the specified requestor user by receiver
     * @param requestor     the User object of the requestor
     * @param receiver      the User object of the receiver
     * @return              true upon successful accepting, false otherwise
     */
    public boolean acceptFriendRequest(User requestor, User receiver) {
        return requestAccepter.acceptFriendRequest(requestor, receiver);        
    }

    /**
     * rejects friend request from the specified requestor user by the receiver
     * @param requestor     the User object of the requestor
     * @param receiver      the User object of the receiver
     * @return              true upon successful rejecting, false otherwise
     */
    public boolean rejectFriendRequest(User requestor, User receiver) {
        return requestAccepter.rejectFriendRequest(requestor, receiver);
    }
}