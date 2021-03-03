package entities;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The Reaction class keeps track of a reaction's reactorUsername, 
 * timeReacted and reactionType (like or dislike).
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class Reaction {

    /** Username of reactor */
    private String reactorUsername;

    /** Time of reaction */
    private Date timeReacted;

    /** Type of reaction (1: like, -1: dislike) */
    private int reactionType;

    /**
     * Creates a Reaction with the specified reactorUsername, timeReacted and reactionType.
     * Used when retrieving a reaction from database
     * 
     * @param reactorUsername   username of reactor
     * @param timeReacted       time of reaction
     * @param reactionType      type of reaction
     */
    public Reaction(String reactorUsername, Date timeReacted, int reactionType) {
        this.reactorUsername = reactorUsername;
        this.timeReacted = timeReacted;
        this.reactionType = reactionType;
    }

    /**
     * Create a Reaction with the specified reactorUsername and reactionType.
     * timeReacted is automatically initialiased.
     * Used when a user likes or dislikes a post
     * 
     * @param reactorUsername   username of reactor
     * @param reactionType      type of reaction
     */
    public Reaction(String reactorUsername, int reactionType) {
        this.reactorUsername = reactorUsername;
        this.reactionType = reactionType;     
        
        Date date= new java.util.Date();
        long time = date.getTime();
        Timestamp timeReacted = new Timestamp(time);
        this.timeReacted = timeReacted;
    }

    /**
     * Returns username of this reactor 
     * 
     * @return username of this reactor 
     */
    public String getReactorUsername() {
        return this.reactorUsername;
    }

    /**
     * Returns time of this reaction
     * 
     * @return time of this reaction
     */
    public Date getTimeReacted() {
        return this.timeReacted;
    }

    /**
     * Returns type of this reaction (1: like, -1: dislike)
     * 
     * @return type of this reaction (1: like, -1: dislike)
     */
    public int getReactionType() {
        return this.reactionType;
    }
}