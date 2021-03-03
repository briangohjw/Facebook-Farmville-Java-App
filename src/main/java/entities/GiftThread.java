package entities;

import java.util.Date;

/**
 * The GiftThread class keeps track of the senderUsername, receiverUsername, timePosted
 * and cropName of a gift thread.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */     
public class GiftThread extends Thread {
    /** Name of crop */
    private String cropName;

    // Getting thread from DB (existing timePosted)

    /**
     * Creates a GiftThread with the specified senderUsername, receiverUsername,
     * timePosted and cropName.
     * Used when retrieving a gift thread from database
     * 
     * @param senderUsername    username of sender
     * @param receiverUsername  username of receiver
     * @param timePosted        time that gift was sent
     * @param cropName          name of crop in gift
     */
    public GiftThread(String senderUsername, String receiverUsername, Date timePosted, String cropName) {
        super(senderUsername, receiverUsername, timePosted);
        this.cropName = cropName;
    }


    /**
     * Create a GiftThread with the specified senderUsername, receiverUsername and cropName.
     * timePosted is automatically initialiased.
     * Used when a user sends a gift
     * 
     * @param senderUsername    username of sender
     * @param receiverUsername  username of receiver
     * @param cropName          name of crop in gift
     */
    public GiftThread(String senderUsername, String receiverUsername, String cropName) {
        super(senderUsername, receiverUsername);
        this.cropName = cropName;
    }

    /**
     * Returns name of crop in gift
     * 
     * @return name of crop in gift
     */
    public String getCropName() {
        return this.cropName;
    }
}