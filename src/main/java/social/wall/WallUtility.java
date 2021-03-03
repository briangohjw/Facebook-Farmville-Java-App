package social.wall;

import java.util.List;
import java.util.Scanner;

import daos.FriendsDAO;
import daos.UserDAO;
import entities.User;

/**
 * The WallUtility class contains the utility functions for dealing with walls
 *  
 * @version 1.0 07 Apr 2020
 * @author Brian Goh
 */
public class WallUtility {

    /** UDM provides access to the User table in the database */
    private UserDAO UDM;

    /** FDM provides access to the Friends table in the database */
    private FriendsDAO FDM;

    /** Creates a new WallUtility object with a default User and Friends DM */
    public WallUtility() {
        UDM = new UserDAO();
        FDM = new FriendsDAO();
    }

    /**
     * Prompts user continuously until a non empty input is given
     * 
     * @param sc    the scanner that takes in user's input
     * @return      the non empty message
     */
    public String getNonNullMessage(Scanner sc) {
        
        System.out.print("Enter your message > ");
        String result = sc.nextLine();

        while (result.equals("")) {
            System.out.println("Message posting unsuccessfull: Message cannot be empty.");

            System.out.print("Enter your message > ");
            result = sc.nextLine();
        }

        return result;
    }

    /**
     * Displays the information of the user specified
     * 
     * @param user  the User object of the user
     */
    public void displayUserInfo(User user) {
        int richRank = UDM.getRichRank(user);
        String richRankString = richRank + getSuffix(richRank);

        System.out.println("About " + user.getUsername());
        System.out.println("Full Name: " + user.getFullName());
        System.out.println(user.getRank() + " Farmer, " + richRankString +" richest\n");
    }

    /**
     * Gets suffix of the integer specified
     * 
     * @param n     the integer
     * @return      the suffix of the integer
     */
    public static String getSuffix(int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  
                return "st";
            case 2:  
                return "nd";
            case 3:  
                return "rd";
            default: 
                return "th";
        }
    }

    /**
     * Gets a List of User objects of friends of the user
     * 
     * @param user  the User object of the user
     * @return      List of User objects of friends of the user
     */
    public List<User> getFriends(User user) {
        return FDM.getFriends(user);
    }

    /**
     * Checks if user1 is friends with user2
     * 
     * @param user1 the User object of the user1
     * @param user2 the User object of the user2
     * @return      true of user1 and user2 are friends, false otherwise.
     */
    public boolean isFriend(User user1, User user2) {
        return FDM.isFriend(user1, user2);
    }
}