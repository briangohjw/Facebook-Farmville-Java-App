package social.wall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import entities.Crop;
import entities.Thread;
import entities.User;
import social.thread.InvalidThreadException;
import social.thread.ThreadMenu;
import social.thread.ThreadUtility;
import util.Utility;

/**
 * The WallMenu class interacts with the user on the My Wall Page 
 *
 * @version 1.4 07 Apr 2020
 * @author Wa Thone
 */
public class WallMenu {

    /** The wallCtrl facilitates the interaction between the database and the WallMenu */
    private WallCtrl wallCtrl;
    
    /** loggedInUser is the User object of the logged in user */
    private User loggedInUser;

    /** threads is a List of up to top 5 Thread objects on My Wall Page of the user */
    private List<Thread> threads;

    /**
     * Creates a WallMenu object with the specified loggedInUser
     * 
     * @param loggedInUser  the User object of the logged in user
     */
    public WallMenu(User loggedInUser) {
        this.wallCtrl = new WallCtrl();
        this.loggedInUser = loggedInUser;
        this.threads = wallCtrl.getWallThreads(loggedInUser);
    }

    /**
     * Displays the My Wall Page, input choices, and prompt
     */
    public void display() {
        System.out.println();
        System.out.println(" == Social Magnet :: My Wall ==");
        wallCtrl.displayUserInfo(loggedInUser);
        
        ThreadUtility.displayTopThreads(threads);

        System.out.print("[M]ain | [T]hread | [A]ccept Gift | [P]ost > ");
    }

    /**
     * Takes in user's input choice and invoke corresponding methods
     */
    public void readOption() {
        Scanner sc = new Scanner(System.in);

        String input;
        char choice;

        do {
            display();

            input = Utility.getNonEmptyInput("[M]ain | [T]hread | [A]ccept Gift | [P]ost > ",
                     sc, false);

            
            ArrayList<Character> excusedChars = new ArrayList<>(Arrays.asList('T'));
            choice = Utility.getCharFromInput(input, excusedChars);

            switch(choice) {
                case 'M':
                    // Back to menu
                    break;

                case 'T':
                    choice = displaySpecificThread(input);
                    break;
                    
                case 'A':
                    acceptGifts();
                    break;

                case 'P':
                    postOnWall(sc);
                    break;

                default:
                    System.out.println("Please enter a choice from | M | T | A | P |");
            }
        } while(choice != 'M');
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
     * Accepts all the gifts that the user has received but has not accepted yet
     */
    public void acceptGifts() {
        Map<Crop, Integer> acceptedGiftsCountMap = wallCtrl.acceptGifts(loggedInUser);

        if (acceptedGiftsCountMap.isEmpty()) {
            System.out.println("No new gifts to accept.");
            return;
        }

        String acceptedGiftsMessage = "You have accepted:\n";

        for (Crop crop: acceptedGiftsCountMap.keySet()) {
            acceptedGiftsMessage += " - " + acceptedGiftsCountMap.get(crop) + " x Bag(s) of " 
                                    + crop.getCropName() + " Seeds\n";
        }

        System.out.println(acceptedGiftsMessage);
    }

    /**
     * Takes in user's input as text for a new post, and creates a post on the user's wall
     * 
     * @param sc    the scanner that takes in user's input
     */
    public void postOnWall(Scanner sc) {
        String message = wallCtrl.getNonNullMessage(sc);

        boolean result = wallCtrl.postOnWall(message, loggedInUser);

        if (result) {
            System.out.println("Message successfully posted.");
            // update threads shown on wall after user post on the wall
            this.threads = wallCtrl.getWallThreads(loggedInUser);
        } else {
            System.out.println("Message unsuccessfully posted. Please try again.");
        }
    }
}