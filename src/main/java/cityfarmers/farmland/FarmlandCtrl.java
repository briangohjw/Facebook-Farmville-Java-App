package cityfarmers.farmland;

import java.util.List;

import entities.Plot;
import entities.User;

/**
 * The FarmlandCtrl class facilitates the interaction between the FarmlandPlanter, FarmlandHarvester, 
 * FarmlandClearer and the FarmlandMenu
 * 
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class FarmlandCtrl {
    /** farmlandViewer provides access to FarmlandViewer methods */
    FarmlandViewer farmlandViewer;

    /** farmlandPlanter provides access to FarmlandPlanter methods */
    FarmlandPlanter farmlandPlanter;

    /** farmlandHarvester provides access to FarmlandHarvester methods */
    FarmlandHarvester farmlandHarvester;

    /** farmlandClearer provides access to FarmlandClearer methods */
    FarmlandClearer farmlandClearer;

    /** Creates a FarmlandCtrl object with a default farmlandViewer, farmlandPlanter, farmland Harvester, farmlandClearer*/
    public FarmlandCtrl() {
        farmlandViewer = new FarmlandViewer();
        farmlandPlanter = new FarmlandPlanter();
        farmlandHarvester = new FarmlandHarvester();
        farmlandClearer = new FarmlandClearer();
    }

    /**
     * Displays all a user's plots with an index and harvest status. 
     * For example, 1. Banana [###-------] 37%
     * 
     * @param plots a list of the user's plots
     */
    public void displayPlots(List<Plot> plots) {
        farmlandViewer.displayPlots(plots);
    }

    /**
     * Returns all of the user's plots.
     * 
     * @param   username        the username
     * @return  list of plots owned by user
     */
    public List<Plot> getPlotsOwnedByUser(String username) {
        return farmlandViewer.getPlotsOwnedByUser(username);
    }

    /**
     * Plant the user's plots if valid. 
     * 
     * @param   input           user input
     * @param   loggedInUser    the user
     * @return  character of user input, default is '-'
     */
    public char plant(String input, User loggedInUser) {
        return farmlandPlanter.plant(input, loggedInUser);
    }

    /**
     * Harvests the user's plots if valid. 
     * @param   loggedInUser    the user
     */
    public void harvest(User loggedInUser) {
        farmlandHarvester.harvest(loggedInUser);
    }

    /**
     * Clears the user's plots if valid.
     * @param   loggedInUser    the user 
     */
    public void clear(User loggedInUser) {
        farmlandClearer.clear(loggedInUser);
    }
}