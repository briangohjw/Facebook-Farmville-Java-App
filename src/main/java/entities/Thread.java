package entities;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The Thread class keeps track of the senderUsername, receiverUsername
 * and timePosted of a thread
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class Thread {

    /** Username of sender */
    private String senderUsername;

    /** Username of receiver */
    private String receiverUsername;

    /** Time thread was posted */
    private Date timePosted;

    /**
     * Creates a Thread with the specified senderUsername, receiverUsername and timePosted.
     * Used by subclasses when retrieving a post thread or gift thread from database
     * 
     * @param senderUsername    username of sender
     * @param receiverUsername  username of receiver
     * @param timePosted        time thread was posted
     */
    public Thread(String senderUsername, String receiverUsername, Date timePosted) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.timePosted = timePosted;
    }

    /**
     * Creates a Thread with the specified senderUsername and receiverUsername.
     * timePosted is automatically initialiased.
     * Used by subclasses when a user posts a thread or sends a gift
     * 
     * @param senderUsername    username of sender
     * @param receiverUsername  username of receiver
     */
    public Thread(String senderUsername, String receiverUsername) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        Date date= new Date();
        long time = date.getTime();
        Timestamp timePosted = new Timestamp(time);
        this.timePosted = timePosted;
    }

    /**
     * Returns username of sender
     * 
     * @return username of sender
     */
    public String getSenderUsername() {
        return this.senderUsername;
    }

    /**
     * Returns username of receiver
     * 
     * @return username of receiver
     */
    public String getReceiverUsername() {
        return this.receiverUsername;
    }

    /**
     * Returns time thread was posted
     * 
     * @return time thread was posted
     */
    public Date getTimePosted() {
        return this.timePosted;
    }
}