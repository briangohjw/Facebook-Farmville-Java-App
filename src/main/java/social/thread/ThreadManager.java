package social.thread;

import java.util.List;

import daos.TaggedDAO;
import daos.ThreadDAO;
import entities.Thread;
import entities.User;

/**
 * The ThreadManager class deletes threads from a wall
 *  
 * @version 1.0 07 Apr 2020
 * @author Brian Goh
 */
public class ThreadManager {

    /** TDM provides access to the Thread table in the database */
    private ThreadDAO TDM;

    /** TaggedDM provides access to the Tagged table in the database */
    private TaggedDAO TaggedDM;

    /** Creates a new ThreadManager with a default Thread and Tagged DM */
    public ThreadManager() {
        TDM = new ThreadDAO();
        TaggedDM = new TaggedDAO();
    }

    /**
     * Check if the thread is kill-able by user, which is when the user is either
     * 1. owner of thread, 2. receiver of thread or 3. tagged in the thread
     * 
     * @param thread    the Thread object to check
     * @param user      the User object to check if valid to kill thread
     * @return          true if user is valid to kill thread, false otherwise
     */
    public boolean checkCanKill(Thread thread, User user) {
        return ( (isTagged(thread, user)) || (isOwnerOfPost(thread, user)) 
                || (isReceiverOfPost(thread, user)) );
    }

    /**
     * Checks if the user is one of those tagged in the thread
     * 
     * @param thread    the Thread object to check
     * @param user      the User object to check if tagged
     * @return          true if tagged in thread, false otherwise
     */
    public boolean isTagged(Thread thread, User user) {
        List<String> taggedUsernames = TaggedDM.getTaggedUsernames(thread);
        String username = user.getUsername();

        if (taggedUsernames.contains(username)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the user is a receiver of the thread
     * 
     * @param thread    the Thread object to check
     * @param user      the User object to check if receiver
     * @return          true if receiver of the thread, false otherwise
     */
    public boolean isReceiverOfPost(Thread thread, User user) {
        String receiverUsername = thread.getReceiverUsername();
        String username = user.getUsername();

        return receiverUsername.equals(username);
    }

    /**
     * Checks if the user is the owner of the thread
     * 
     * @param thread    the Thread object to check
     * @param user      the User object to check if owner
     * @return          true if owner of the thread, false otherwise
     */
    public boolean isOwnerOfPost(Thread thread, User user) {
        String senderUsername = thread.getSenderUsername();
        String username = user.getUsername();

        return senderUsername.equals(username);
    }

    /**
     * Deletes the specified thread completely from the database
     * 
     * @param thread    the Thread object
     * @return          true if successfully deleted, false otherwise.
     */
    public boolean deleteThreadCompletely(Thread thread) {
        return TDM.deleteThreadCompletely(thread);
    }

    /**
     * Deletes the specified thread from the user's wall if he is tagged in the thread
     * or if it is posted on his wall by his friend or both
     * 
     * @param thread    the Thread object
     * @param user      the User object of user
     * @return          true if successfully removed thread from user's wall, false otherwise.
     */
    public boolean deleteThreadFromWall(Thread thread, User user) {
        boolean removedFromTagged = false;
        boolean removedFromReceived = false;
        
        // remove if user is tagged in the thread
        if (isTagged(thread, user)) {
            removedFromTagged = TaggedDM.deleteTaggedFromThread(user, thread);
        }

        // remove if user received this post on his wall
        if (isReceiverOfPost(thread, user)) {
            removedFromReceived = TDM.deleteReceiverFromThread(thread);
        }

        return removedFromReceived || removedFromTagged;
    }
}