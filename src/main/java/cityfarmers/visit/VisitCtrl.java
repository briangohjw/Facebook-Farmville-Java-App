package cityfarmers.visit;

import java.util.List;

import cityfarmers.farmland.FarmlandViewer;
import entities.Plot;
import entities.User;

/**
 * The VisitCtrl class facilitates the interaction between the database and the VisitMenu
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class VisitCtrl {
    /** stealManager provides access to StealManager methods */
    private StealManager stealManager;

    /** farmlandViewer provides access to FarmlandViewer methods */
    private FarmlandViewer farmlandViewer;

    /** Creates a VisitCtrl object with a default UDM, PDM, CDM, FDM, SDM and RDM */
    public VisitCtrl() {
        stealManager = new StealManager();
        farmlandViewer = new FarmlandViewer();
    }

    /**
     * Returns all of the user's friend's user object.
     * 
     * @param username  the friend's username
     * @return          all of the user's friends' user object
     */
    public List<User> getFriends(User username){
        return stealManager.getFriends(username);
    }

    /**
     * The checkAnySteal method takes a plot and checks if any of the friend's plots are ready for stealing
     * 
     * @param loggedInUser  the user
     * @param chosenFriend  the friend
     * @return      list of plots ready for stealing, null otherwise
     */
    public List<Plot> checkAnySteal(User loggedInUser, User chosenFriend) {
        return stealManager.checkAnySteal(loggedInUser, chosenFriend);
    }

    /**
     * The steal method takes a list of plot and steals, updates the user and plots stolen.
     * 
     * @param loggedInUser      the user
     * @param plotsToBeStolen   plots to be stolen
     */
    public void steal(User loggedInUser, List<Plot> plotsToBeStolen) {
        stealManager.steal(loggedInUser, plotsToBeStolen);
    }

    /**
     * Displays friend's farmland and plots with an index.
     * 
     * @param chosenFriend the chosen friend
     */
    public void displayFriend(User chosenFriend) {
        farmlandViewer.displayFriend(chosenFriend);
    }
    
}
