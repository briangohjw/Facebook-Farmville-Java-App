package social.feed;

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
 * The NewsFeedMenu class interacts with the user on the News Feed Page 
 *
 * @version 1.4 07 Apr 2020
 * @author Wa Thone
 */  
public class NewsFeedMenu {

    /** The newsFeedCtrl facilitates the interaction between the database and the NewsFeedMenu */
    private NewsFeedCtrl newsFeedCtrl;

    /** loggedInUser is the User object of the logged in user */
    private User loggedInUser;

    /** threads is a List of up to top 5 Thread objects on the News Feed Page of the user */
    private List<Thread> threads;

    /**
     * Creates a NewsFeedMenu object with the specified loggedInUser
     * 
     * @param loggedInUser  the User object of the logged in user
     */
    public NewsFeedMenu(User loggedInUser) {
        this.newsFeedCtrl = new NewsFeedCtrl();
        this.loggedInUser = loggedInUser;
        this.threads = newsFeedCtrl.getFeedThreads(loggedInUser);
    }

    /**
     * Displays the News Feed Page, input choices, and prompt
     */
    public void display() {
        System.out.println();
        System.out.println(" == Social Magnet :: News Feed ==");

        ThreadUtility.displayTopThreads(threads);


        System.out.print("[M]ain | [T]hread > ");
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

            input = Utility.getNonEmptyInput("[M]ain | [T]hread > ", sc, false);

            ArrayList<Character> excusedChars = new ArrayList<>(Arrays.asList('T'));
            choice = Utility.getCharFromInput(input, excusedChars);

            switch(choice) {
                case 'M':
                    // Goes back to menu
                    break;

                case 'T':
                    choice = displaySpecificThread(input);
                    break;
                    
                default:
                    System.out.println("Please enter a choice from | M | T |");
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
}