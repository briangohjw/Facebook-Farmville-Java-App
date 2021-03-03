package entities;

import java.util.Date;

/**
 * The PostThread class keeps track of the senderUsername, receiverUsername, timePosted
 * and text of a post thread.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class PostThread extends Thread {

    /** Text content of thread */
    private String text;

    /**
     * Creates a PostThread with the specified senderUsername, receiverUsername,
     * timePosted and text.
     * Used when retrieving a post thread from database
     * 
     * @param senderUsername    username of sender
     * @param receiverUsername  username of receiver
     * @param timePosted        time thread was posted
     * @param text              text content of thread
     */
    public PostThread(String senderUsername, String receiverUsername, Date timePosted, String text) {
        super(senderUsername, receiverUsername, timePosted);
        this.text = text;
    }

    /**
     * Create a PostThread with the specified senderUsername, receiverUsername and text.
     * timePosted is automatically initialiased.
     * Used when a user posts a thread
     * 
     * @param senderUsername    username of sender
     * @param receiverUsername  username of receiver
     * @param text              text content of thread
     */
    public PostThread(String senderUsername, String receiverUsername, String text) {
        super(senderUsername, receiverUsername);
        this.text = text;
    }

    /**
     * Returns text content of thread
     * 
     * @return text content of thread
     */
    public String getText() {
        return this.text;
    }
}