package navigation;

import java.util.Scanner;

import entities.User;
import util.Utility;

/**
 * The WelcomeMenu class interacts with the user on the Welcome Page 
 *
 * @version 1.3 06 Apr 2020
 * @author Wa Thone
 */     
public class WelcomeMenu {

    /** welcomeCtrl facilitates the interaction between the database and the WelcomeMenu*/
    private WelcomeCtrl welcomeCtrl;

    /** 
     * Creates a WelcomeMenu object with a default welcomeCtrl 
     */
    public WelcomeMenu() {
        this.welcomeCtrl = new WelcomeCtrl();
    }

    /**
     * Displays the Welcome Page, input choices, and prompt
     */
    public void display() {
        System.out.println();
        System.out.println("== Social Magnet :: Welcome ==");
        System.out.println("Good morning, anonymous!");
        System.out.println("1. Register");
        System.out.println("2. Login"); 
        System.out.println("3. Exit");
        System.out.print("Enter your choice > ");
    }

    /**
     * Takes in user's input choice and invoke corresponding methods.
     */
    public void readOption() {
        Scanner sc = new Scanner(System.in);

        int choice;

        do {
            display();
            choice = Utility.promptForNum(sc, "Enter your choice > ");

            switch(choice) {
                case 1:
                    register(sc);
                    break;
                case 2:
                    User loggedInUser = logIn(sc);

                    if (loggedInUser != null) {
                        MainMenu mainMenu = new MainMenu(loggedInUser);
                        mainMenu.readOption();
                    } else {
                        System.out.println("Log in failed.");
                    }
                    break;
                case 3:
                    System.out.println("Bye!");
                    sc.close();
                    break;
                default:
                    System.out.println("Please enter a choice between 1 & 3!");
            }
        } while(choice != 3);
    }

    /**
     * Registers the user with their input from the scanner if valid. The scanner will ask for
     * username, full name, password and confirmed password.
     * 
     * @param sc    the scanner that takes in user's input
     */
    public void register(Scanner sc) {
        System.out.println();
        System.out.println("== Social Magnet :: Registration ==");

        String username = Utility.getNonEmptyInput("Enter your username > ", sc);
        
        while (!isValidUsername(username) || welcomeCtrl.usernameExists(username)){

            if (isValidUsername(username)) {
                System.out.println("Username already exists. Try a different one.");
            } else {
                System.out.println("Invalid username: Can only contain alphanumeric characters with no space.");
            }

            username = Utility.getNonEmptyInput("Enter your username > ", sc);
        }

        String fullName = Utility.getNonEmptyInput("Enter your Full name > ", sc);
        String password1 = Utility.getNonEmptyInput("Enter your password > ", sc);
        String password2 = Utility.getNonEmptyInput("Confirm your password > ", sc);

        while (!password1.equals(password2)) {
            System.out.println("Password does not match. Please try again. \n");
            
            password1 = Utility.getNonEmptyInput("Enter your password > ", sc);
            password2 = Utility.getNonEmptyInput("Confirm your password > ", sc);
        }

        boolean createdSuccess = welcomeCtrl.register(username, fullName, password1);
        if (createdSuccess) {
            System.out.println(username + ", your account is successfully created!");
        } else {
            System.out.println("There was a problem creating your account, please try again.");
        }
    }

    /**
     * Prompts user for username and password, and logs in
     * @param sc    the scanner that takes in user's input
     * @return      the User object corresponding to the username and password. If invalid, return null
     */
    public User logIn(Scanner sc) {
        System.out.println();
        System.out.println("== Social Magnet :: Login ==");

        String username = Utility.getNonEmptyInput("Enter your username > ", sc);
        String password = Utility.getNonEmptyInput("Enter your password > ", sc);
        
        User loggedInUser = welcomeCtrl.logIn(username, password);
        
        return loggedInUser;
    }

    /**
     * Checks if the username is valid: alphanumeric and contains no space
     * 
     * @param username  the username in question
     * @return          true if the username is alphanumeric and contains no space, false otherwise
     */
    public static boolean isValidUsername(String username) {
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);

            if (c == ' ') {
                return false;
            }
            
            if (!Character.isDigit(c) && !Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }
}