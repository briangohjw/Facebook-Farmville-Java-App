package social.feed;

import java.util.List;

import daos.ThreadDAO;
import entities.Thread;
import entities.User;

/**
 * The NewsFeedGetter class retrieves threads for a feed
 *  
 * @version 1.0 07 Apr 2020
 * @author Brian Goh
 */
public class NewsFeedGetter {

    /** TDM provides access to the Thread table in the database */
    private ThreadDAO TDM;

    /** Creates a NewsFeedGetter object with a default Thread DM */
    public NewsFeedGetter() {
        TDM = new ThreadDAO();
    }

    /**
     * Gets a List of up to top 5 Threads of the user's feed
     * 
     * @param user  the User object of the user
     * @return      List of Thread objects of up to top 5 threads of the user
     */
    public List<Thread> getFeedThreads(User user) {
        return TDM.getFeedThreads(user);
    }
}