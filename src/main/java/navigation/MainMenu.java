package navigation;

import java.util.Scanner;

import cityfarmers.CityFarmersMenu;
import entities.User;
import social.feed.NewsFeedMenu;
import social.friends.FriendsMenu;
import social.wall.WallMenu;
import util.Utility;

/**
 * The MainMenu class interacts with the user on the Main Menu Page 
 *
 * @version 1.2 06 Apr 2020
 * @author Wa Thone
 */ 
public class MainMenu {

    /** loggedInUser is the User object of the logged in user */
    private User loggedInUser;

    /**
     * Creates a MainMenu object with the specified loggedInUser
     * 
     * @param loggedInUser  the User object of the logged in user
     */
    public MainMenu(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    /**
     * Displays the Main Menu Page, input choices, and prompt
     */
    public void display() {
        System.out.println();
        System.out.println(" == Social Magnet :: Main Menu ==");
        System.out.println("Welcome, " + loggedInUser.getFullName() + "!");
        System.out.println("1. News Feed");
        System.out.println("2. My Wall");
        System.out.println("3. My Friends");
        System.out.println("4. City Farmers");
        System.out.println("5. Logout");
        System.out.print("Enter your choice > ");
    }

    /**
     * Takes in user's input choice and invoke corresponding methods
     */
    public void readOption() {
        Scanner sc = new Scanner(System.in);

        int choice;

        do {
            display();
            choice = Utility.promptForNum(sc, "Enter your choice > ");

            switch(choice) {
                case 1:
                    displayNewsFeed();
                    break;

                case 2:
                    displayWall();
                    break;

                case 3:
                    displayFriends();
                    break;

                case 4:
                    enterCityFarmers();
                    break;

                case 5:
                    System.out.println("Logged out successfully.");
                    break;

                default:
                    System.out.println("Please enter a choice between 1 & 5!");
            }
        } while(choice != 5);
    }

    /**
     * Directs user to the News Feed Page
     */
    public void displayNewsFeed(){
        NewsFeedMenu newsFeedMenu = new NewsFeedMenu(loggedInUser);
        newsFeedMenu.readOption();
    }

    /**
     * Directs user to the My Wall Page
     */
    public void displayWall(){
        WallMenu wallMenu = new WallMenu(loggedInUser);
        wallMenu.readOption();
    }

    /**
     * Directs user to the My Friends Page
     */
    public void displayFriends(){
        FriendsMenu friendsMenu = new FriendsMenu(loggedInUser);
        friendsMenu.readOption();
    }

    /**
     * Directs user to the City Farmers Page
     */
    public void enterCityFarmers(){
        CityFarmersMenu cityFarmersMenu = new CityFarmersMenu(loggedInUser);
        cityFarmersMenu.readOption();
    }
}