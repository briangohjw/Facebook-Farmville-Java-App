package navigation;

import daos.UserDAO;
import entities.User;

/**
 * The WelcomeCtrl class facilitates the interaction between the database and the WelcomeMenu
 *  
 * @version 1.2 05 Apr 2020
 * @author Wa Thone
 */
public class WelcomeCtrl {

    /** UDM provides access to the User table in the database */
    private UserDAO UDM;

    /** Creates a WelcomeCtrl object with a default UDM */
    public WelcomeCtrl() {
        UDM = new UserDAO();
    }

    /**
     * Checks if username exists in the User table in the database
     * 
     * @param username  the username being checked
     * @return          true if username exists in the User table, false otherwise
     */
    public boolean usernameExists(String username) {
        User user = UDM.getUser(username);
        if (user == null) {
            return false;
        } 
        return true;
    }

    /**
     * Registers a user with the specified username, fullName, and password by adding
     * to the User table in the database
     * 
     * @param username  the username
     * @param fullName  the full name of the user
     * @param password  the password
     * @return          true if successfully added to the database, false otherse
     */
    public boolean register(String username, String fullName, String password) {
        User createdUser = new User(username, fullName);
        
        return UDM.addUser(createdUser, password);
    }

    /**
     * Logs in with the specified username and password, and retrieves the User
     * 
     * @param username  the username
     * @param password  the password
     * @return          the User object correspnding to the username and password. If invalid, return null.
     */
    public User logIn(String username, String password) {
        return UDM.authenticate(username, password);
    }
}