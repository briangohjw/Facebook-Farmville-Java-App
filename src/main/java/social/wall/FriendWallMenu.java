package social.wall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import entities.Thread;
import entities.User;
import social.thread.InvalidThreadException;
import social.thread.ThreadMenu;
import social.thread.ThreadUtility;
import util.Utility;

/**
 * The FriendWallMenu class interacts with the user on the Friends' Wall Page 
 *
 * @version 1.4 07 Apr 2020
 * @author Wa Thone
 */
public class FriendWallMenu {
    
    /** The wallCtrl facilitates the interaction between the database and the FriendWallMenu */
    private WallCtrl wallCtrl;
    
    /**  is the User object of the friend whose wall is being viewed */
    private User friendUser;
    
    /** loggedInUser is the User object of the logged in user */
    private User loggedInUser;

    /** threads is a List of up to top 5 Thread objects on the Wall Page of the friend */
    private List<Thread> threads;

    /**
     * Creates a FriendWallMenu object with the specified friendUser and loggedInUser
     * 
     * @param friendUser    the User object of the friend
     * @param loggedInUser  the User object of the logged in user
     */
    public FriendWallMenu(User friendUser, User loggedInUser) {
        this.wallCtrl = new WallCtrl();
        this.friendUser = friendUser;
        this.loggedInUser = loggedInUser;
        this.threads = wallCtrl.getWallThreads(friendUser);
    }

    /**
     * Displays the Friend's Wall Page, input choices, and prompt
     */
    public void display() {
        String friendUsername = friendUser.getUsername();
        String capitalFriendUsername = friendUsername.substring(0, 1).toUpperCase() + friendUsername.substring(1);

        System.out.println();
        System.out.println(" == Social Magnet :: " + capitalFriendUsername + "'s Wall ==");
        System.out.println("Welcome, " + loggedInUser.getFullName() + "!\n");

        wallCtrl.displayUserInfo(friendUser);
        
        ThreadUtility.displayTopThreads(threads);

        System.out.println("\n" + capitalFriendUsername + "'s Friend");
        displayUserFriends();

        System.out.print("[M]ain | [T]hread | [P]ost > ");
    }

    /**
     * Takes in user's input choice and invoke corresponding methods
     * 
     * @return choice   the user's selected choice
     */
    public char readOption() {
        Scanner sc = new Scanner(System.in);

        String input;
        char choice;

        do {
            display();

            input = Utility.getNonEmptyInput("[M]ain | [T]hread | [P]ost > ", sc, false);
                        
            ArrayList<Character> excusedChars = new ArrayList<>(Arrays.asList('T'));
            choice = Utility.getCharFromInput(input, excusedChars);

            switch(choice) {
                case 'M':
                    // Back to menu
                    break;

                case 'T':
                    choice = displaySpecificThread(input);
                    break;

                case 'P':
                    postOnWall(sc);
                    break;

                default:
                    System.out.println("Please enter a choice from | M | T | P |");
            }
        } while(choice != 'M');

        return choice;
    }

    /**
     * Directs user to the View a Thread Page of the thread the user selects
     * 
     * @param input     the input of user that specifies the thread selected, T<id>
     * @return          'M' when the user chooses to go back to Main Menu from View a Thread Page, 'T' if error
     */
    public char displaySpecificThread(String input) {
        try {
            Thread threadByInput = ThreadUtility.getThreadByInput(threads, input);
            ThreadMenu threadMenu = new ThreadMenu(threadByInput, loggedInUser);

            return threadMenu.readOption();

        } catch (InvalidThreadException e) {
            System.out.println(e.getMessage());
            return 'T';
        }
    }

    /**
     * Displays a list of the friend's friends, indicating when he/she is also friend 
     * with the current user, in the format:
     * <index>. Full name (Common)  [if friends with current user]
     * <index>. Full name           [if not friends with current user]
     */
    public void displayUserFriends() {
        List<User> friendsOfFriend = wallCtrl.getFriends(friendUser);

        for (int i = 1; i <= friendsOfFriend.size(); i++) {
            User friendOfFriend = friendsOfFriend.get(i - 1);

            String result = "" + i + ". " + friendOfFriend.getFullName();

            if (wallCtrl.isFriend(friendOfFriend, loggedInUser)) {
                result += " (Common Friend)";
            }
            
            System.out.println(result);
        }
        System.out.println();
    }

    /**
     * Takes in user's input as text for a new post, and creates a post on the friend's wall
     * 
     * @param sc    the scanner that takes in user's input
     */
    public void postOnWall(Scanner sc) {
        String message = wallCtrl.getNonNullMessage(sc);

        boolean result = wallCtrl.postOnWall(message, loggedInUser, friendUser);

        if (result) {
            System.out.println("Message successfully posted.");
            // update threads shown on wall after user post on the wall
            this.threads = wallCtrl.getWallThreads(friendUser);
        } else {
            System.out.println("Message unsuccessfully posted. Please try again.");
        }

    }
}