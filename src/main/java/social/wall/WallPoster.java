package social.wall;

import java.util.List;

import daos.TaggedDAO;
import daos.ThreadDAO;
import entities.PostThread;
import entities.Thread;
import entities.User;

/**
 * The WallPoster class posts threads to a wall
 *  
 * @version 1.0 07 Apr 2020
 * @author Brian Goh
 */
public class WallPoster {

    /** TDM provides access to the Thread table in the database */
    private ThreadDAO TDM;

    /** TaggedDM provides access to the Thread_tagged table in the database */
    private TaggedDAO TaggedDM;

    /** TagManager retrieves and deletes tags from a thread */
    private TagManager tagManager;

    /** Creates a new WallPoster object with a default Thread DM, Tagged DM, TagManager */
    public WallPoster() {
        TDM = new ThreadDAO();
        TaggedDM = new TaggedDAO();
        tagManager = new TagManager();
    }

    /**
     * Posts the specified message on user's own wall
     * 
     * @param message   the message posted
     * @param user      the User object of the user
     * @return          true if posting is successful, false otherwise.
     */
    public boolean postOnWall(String message, User user) {
        return postOnWall(message, user, user);
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
        // Gets the username of all the tagged friends
        List<String> taggedFriendsUsernames = tagManager.getTaggedFriendsUsernames(message, sender);
        
        // Modifies the posted message such that tagged '@friend' becomes 'friend'
        if (taggedFriendsUsernames.size() > 0) {
            message = TagManager.deleteTaggedFriendsFromMessage(message, taggedFriendsUsernames);
        }

        // Add thread with (maybe modified) message
        Thread newThread = new PostThread(sender.getUsername(), receiver.getUsername(), message);
        boolean addResult = TDM.add(newThread);

        // Add tags to tagged table if any
        boolean tagResult = true;
        if (taggedFriendsUsernames.size() > 0) {
            tagResult = TaggedDM.addTaggedUsers(newThread, taggedFriendsUsernames);
        }
        
        return addResult && tagResult;
    }
}