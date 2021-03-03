package social.thread;

import daos.CommentDAO;
import entities.Comment;
import entities.Thread;
import entities.User;

/**
 * The CommentManager class posts comments to a thread
 *  
 * @version 1.0 07 Apr 2020
 * @author Brian Goh
 */
public class CommentManager {
    
    /** CDM provides access to the Comment table in the database */
    private CommentDAO CDM;

    /** Creates a new CommentManager with a default Comment DM */
    public CommentManager() {
        CDM = new CommentDAO();
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
        Comment newComment = new Comment(user.getUsername(), reply);

        return CDM.addComment(thread, newComment);
    }
}