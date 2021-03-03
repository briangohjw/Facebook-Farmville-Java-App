package social.wall;

import java.util.List;

import daos.ThreadDAO;
import entities.User;
import entities.Thread;

/**
 * The WallGetter class retrieves threads from a wall
 *  
 * @version 1.0 07 Apr 2020
 * @author Brian Goh
 */
public class WallGetter {

    /** TDM provides access to the Thread table in the database */
    private ThreadDAO TDM;
    
    /** Creates a new WallGetter object with a default Thread DM */
    public WallGetter() {
        TDM = new ThreadDAO();
    }

    /**
     * Gets a List of up to top 5 Threads on the user's wall
     * 
     * @param user  the User object of the user
     * @return      List of Thread objects of up to top 5 threads on the user's wall
     */
    public List<Thread> getWallThreads(User user) {
        return TDM.getWallThreads(user);
    }
}