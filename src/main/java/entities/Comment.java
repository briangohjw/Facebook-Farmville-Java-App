package entities;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The Comment class keeps track of the commenterUsername, timeCommented and text.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */     
public class Comment {

    /** Username of commenter */
    private String commenterUsername;

    /** Time of comment */
    private Date timeCommented;

    /** Text content of comment */
    private String text;

    /**
     * Creates a Comment with the specified commenterUsername, timePosted and text.
     * Used when retrieving a comment from database
     * 
     * @param commenterUsername username of commenter
     * @param timeCommented     time of comment
     * @param text              text content of comment
     */
    public Comment(String commenterUsername, Date timeCommented, String text) {
        this.commenterUsername = commenterUsername;
        this.timeCommented = timeCommented;
        this.text = text;
    }

    /**
     * Create a Comment with the specified commenterUsername and text.
     * timeCommented is automatically initialiased.
     * Used when a user posts a comment
     * 
     * @param commenterUsername
     * @param text
     */
    public Comment(String commenterUsername, String text) {
        this.commenterUsername = commenterUsername;
        this.text = text;     
        
        Date date= new java.util.Date();
        long time = date.getTime();
        Timestamp timeCommented = new Timestamp(time);
        this.timeCommented = timeCommented;
    }

    /**
     * Returns username of commenter
     * 
     * @return username of commenter
     */
    public String getCommenterUsername() {
        return this.commenterUsername;
    }

    /**
     * Returns time of comment
     * 
     * @return time of comment
     */
    public Date getTimeCommented() {
        return this.timeCommented;
    }

    /**
     * Returns text content of comment
     * 
     * @return text content of comment
     */
    public String getText() {
        return this.text;
    }
}