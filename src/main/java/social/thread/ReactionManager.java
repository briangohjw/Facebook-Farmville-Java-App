package social.thread;

import java.util.List;

import daos.ReactionDAO;
import daos.UserDAO;
import entities.Reaction;
import entities.Thread;
import entities.User;

/**
 * The ReactionManager class posts reactions to a thread
 *  
 * @version 1.0 07 Apr 2020
 * @author Brian Goh
 */
public class ReactionManager {

    /** RDM provides access to the Reaction table in the database */
    private ReactionDAO RDM;

    /** UDM provides access to the User table in the database */
    private UserDAO UDM;

    /** Creates a new ReactionManager with a default Reaction and User DM */
    public ReactionManager() {
        RDM = new ReactionDAO();
        UDM = new UserDAO();
    }

    /**
     * Gets List of Reaction objects of likes or dislikes from the specified thread
     * 
     * @param thread    the Thread object to get reaction from
     * @param isLike    true indicates like reaction type, dislike if otherwise
     * @return          List of Reaction objects of likes or dislikes depending on type
     */
    public List<Reaction> getReactionByType(Thread thread, boolean isLike) {
        if (isLike) {
            return RDM.getLikes(thread);
        } else {
            return RDM.getDislikes(thread);
        }
    }

    /**
     * Gets full name of the reactor of the specified Reaction object
     * 
     * @param reaction  the Reaction object
     * @return          the full name of the reactor
     */
    public String getReactorFullname(Reaction reaction) {
        String username = reaction.getReactorUsername();
        User reactorUser = UDM.getUser(username);
        String reactorFullName = reactorUser.getFullName();
        
        return reactorFullName;
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

        String reactorUsername = user.getUsername();
        Reaction previousOppositeReaction = getPreviousReaction(oppositeReactions, user);

        if (previousOppositeReaction != null) {
            RDM.deleteReaction(thread, previousOppositeReaction);
        }

        // adding new reaction
        Reaction newReaction;
        if (isLike) {
            newReaction = new Reaction(reactorUsername, 1);
        } else {
            newReaction = new Reaction(reactorUsername, -1);
        }

        return RDM.addReaction(thread, newReaction);
    }

    /**
     * Deletes the specified reaction from the thread
     * 
     * @param thread    the Thread object
     * @param reaction  the Reaction object to be deleted
     * @return          true if deletion is successfuly, false otherwise.
     */
    public boolean deleteReaction(Thread thread, Reaction reaction) {
        return RDM.deleteReaction(thread, reaction);        
   }

    /**
     * Gets the Reaction object by the user from the specified reactions, if any 
     * 
     * @param reactions List of Reaction objects to check from
     * @param user      the User object
     * @return          the Reaction object user if any, else return null.
     */
    public Reaction getPreviousReaction(List<Reaction> reactions, User user) {
        Reaction result = null;

        String username = user.getUsername();
        for (Reaction rxn: reactions) {
            if (rxn.getReactorUsername().equals(username)) {
                result = rxn;
            }
        }

        return result;
    }
}