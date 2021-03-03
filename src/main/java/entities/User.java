package entities;

/**
 * The User class keeps track of the username, fullName, rank, xp and gold of a user.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */   
public class User {

    /** Username of the user */
    private String username;

    /** Full name of the user */
    private String fullName;

    /** Rank of the user */
    private String rank;

    /** Xp of the user */
    private int xp;

    /** Gold of the user */
    private int gold;
    
    /**
     * Creates a User with the specified username, fullName, rank, xp and gold.
     * Used when retrieving a user from database
     * 
     * @param username  username of user
     * @param fullName  full name of user
     * @param rank      rank of user
     * @param xp        xp of user
     * @param gold      gold of user
     */
    public User(String username, String fullName, String rank, int xp, int gold) {
        this.username = username;
        this.fullName = fullName;
        this.rank = rank;
        this.xp = xp;
        this.gold = gold;
    }

    /**
     * Creates a User with the specified username, fullName.
     * Default rank, xp and gold are automatically initialised
     * Used when a user registers for a new account
     * 
     * @param username  username of user
     * @param fullName  full name of user
     */
    public User(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
        this.rank = "Novice";
        this.xp = 0;
        this.gold = 0;
    }

    /**
     * Returns username of user
     * 
     * @return username of user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns full name of user
     * 
     * @return full name of user
     */
    public String getFullName() {
        return this.fullName;
    }

    /**
     * Returns rank of user
     * 
     * @return rank of user
     */
    public String getRank() {
        return this.rank;
    }

    /**
     * Sets rank of user
     * 
     * @param rank  rank of user
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * Returns xp of user
     * 
     * @return xp of user
     */
    public int getXp() {
        return this.xp;
    }

    /**
     * Sets xp of user
     * 
     * @param xp    xp of user
     */
    public void setXp(int xp) {
        this.xp = xp;
    }

    /**
     * Returns gold of user
     * 
     * @return gold of user
     */
    public int getGold() {
        return this.gold;
    }

    /**
     * Sets gold of user
     * 
     * @param gold  gold of user
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Checks if two user objects are equal
     * 
     * @param user2 another user
     * @return      true if user objects are equal, false otherwise
     */
    public boolean equals(User user2) {
        return this.username.equals(user2.username);
    }
}