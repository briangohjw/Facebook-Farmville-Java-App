package social.feed;

import java.util.List;

import entities.Thread;
import entities.User;

/**
 * The NewsFeedCtrl class facilitates the interaction between the database and the NewsFeedMenu
 *  
 * @version 1.2 05 Apr 2020
 * @author Wa Thone
 */
public class NewsFeedCtrl {

    /** NewsFeedGetter retrieves threads for a feed */
    private NewsFeedGetter newsFeedGetter;

    /** 
     * Creates a NewsFeedCtrl object with a default NewsFeedGetter
     */
    public NewsFeedCtrl() {
        newsFeedGetter = new NewsFeedGetter();
    }

    /**
     * Gets a List of up to top 5 Threads of the user's feed
     * 
     * @param user  the User object of the user
     * @return      List of Thread objects of up to top 5 threads of the user
     */
    public List<Thread> getFeedThreads(User user) {
        return newsFeedGetter.getFeedThreads(user);
    }
}