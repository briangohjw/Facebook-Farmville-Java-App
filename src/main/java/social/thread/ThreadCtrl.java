package social.thread;

import java.util.List;

import entities.Reaction;
import entities.Thread;
import entities.User;

/**
 * The ThreadCtrl class facilitates the interaction between the database and the ThreadMenu
 *  
 * @version 1.1 05 Apr 2020
 * @author Wa Thone
 */
public class ThreadCtrl {

    /** ReactionManager posts reactions to a thread */
    private ReactionManager reactionManager;

    /** ThreadManager deletes threads from a wall */
    private ThreadManager threadManager;

    /** CommentManager posts comments to a thread */
    private CommentManager commentManager;

    /** 
     * Creates a ThreadCtrl object with a default 
     * ReactionManager, ThreadManager, CommentManager 
     */
    public ThreadCtrl() {
        reactionManager = new ReactionManager();
        threadManager = new ThreadManager();
        commentManager = new CommentManager();
    }

    /**
     * Gets List of Reaction objects of likes or dislikes from the specified thread
     * 
     * @param thread    the Thread object to get reaction from
     * @param isLike    true indicates like reaction type, dislike if otherwise
     * @return          List of Reaction objects of likes or dislikes depending on type
     */
    public List<Reaction> getReactionByType(Thread thread, boolean isLike) {
        return reactionManager.getReactionByType(thread, isLike);
    }
    
    /**
     * Gets full name of the reactor of the specified Reaction object
     * 
     * @param reaction  the Reaction object
     * @return          the full name of the reactor
     */
    public String getReactorFullname(Reaction reaction) {
        return reactionManager.getReactorFullname(reaction);
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
        return threadManager.checkCanKill(thread, user);
    }

    /**
     * Checks if the user is the owner of the thread
     * 
     * @param thread    the Thread object to check
     * @param user      the User object to check if owner
     * @return          true if owner of the thread, false otherwise
     */
    public boolean isOwnerOfPost(Thread thread, User user) {
        return threadManager.isOwnerOfPost(thread, user);
    }

    /**
     * Deletes the specified thread completely from the database
     * 
     * @param thread    the Thread object
     * @return          true if successfully deleted, false otherwise.
     */
    public boolean deleteThreadCompletely(Thread thread) {
        return threadManager.deleteThreadCompletely(thread);
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
        return threadManager.deleteThreadFromWall(thread, user);
    }

    /**
     * Adds specified reply to the thread by the user
     * 
     * @param thread    the Thread object
     * @param user      the User object of the user
     * @param reply     the reply message to the thread
     * @return          true if the reply is added to the thread, false otherwise.
     */
    public boolean replyThread(Thread thread, User user, String reply) {
        return commentManager.replyThread(thread, user, reply);
    }

    /**
     * Adds the specified like or dislike to the thread by user
     * If the user has reacted oppositely before, the previous reaction is removed
     * 
     * @param thread            the Thread object to add reaction to
     * @param oppositeReactions the List of Reaction from the thread, of opposite nature from is LIke
     * @param user              the User object of the user
     * @param isLike            true indicates like reaction type, dislike if otherwise
     * @return                  true if reaction is added successfully, false otherwise.
     */
    public boolean reactToThread(Thread thread, List<Reaction> oppositeReactions, User user, boolean isLike) {
        return reactionManager.reactToThread(thread, oppositeReactions, user, isLike);
    }

    /**
     * Deletes the specified reaction from the thread
     * 
     * @param thread    the Thread object
     * @param reaction  the Reaction object to be deleted
     * @return          true if deletion is successfuly, false otherwise.
     */
    public boolean deleteReaction(Thread thread, Reaction reaction) {
         return reactionManager.deleteReaction(thread, reaction);
    }

    /**
     * Gets the Reaction object by the user from the specified reactions, if any 
     * 
     * @param reactions List of Reaction objects to check from
     * @param user      the User object
     * @return          the Reaction object user if any, else return null.
     */
    public Reaction getPreviousReaction(List<Reaction> reactions, User user) {
        return reactionManager.getPreviousReaction(reactions, user);
    }
}