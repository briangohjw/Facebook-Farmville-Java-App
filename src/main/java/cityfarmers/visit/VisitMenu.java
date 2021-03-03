package cityfarmers.visit;

import java.util.List;
import java.util.Scanner;

import entities.Plot;
import entities.User;
import util.Utility;

/**
 * The VisitMenu class interacts with the user on the Visit Page
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */ 
public class VisitMenu {
    /** visitCtrl facilitates the interaction between the database and the VisitCtrl*/
    private VisitCtrl visitCtrl;

    /** the User */
    private User loggedInUser;

    /** plots will store the loggedInUser's list of friends */
    private List<User> friends;

    /**
     * Creates a VisitMenu object with a default visitCtrl 
     * 
     * @param loggedInUser  the user
     * */
    public VisitMenu(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.visitCtrl = new VisitCtrl();
        this.friends = visitCtrl.getFriends(loggedInUser);
    }

    /**
     * Takes in user's input choice and invoke corresponding methods.
     * @return  'M' or 'F' when user decides to go back to Main Menu or City Farmers
     */
    public char readOption() {
        Scanner sc = new Scanner(System.in);
        String input = "";
        char choice;
        int friendNumber = 0;

        do {
            display();
            input = Utility.getNonEmptyInput("[M]ain | City [F]armers | Select Choice > ", 
                    sc, false);

            choice = input.charAt(0);

            if ((choice == 'M' || choice == 'F') && input.length() == 1) {
                return choice;
            }
            
            if ((Character.isDigit(choice)) && (Utility.isNumeric(input))) {
                friendNumber = Integer.parseInt(input);

                if (friendNumber > friends.size() || friendNumber < 1){
                    System.out.println("Invalid friend number! Number range allowed is from 1 to " 
                            + friends.size());
                } else {
                    choice = visitFriend(sc, friendNumber);
                    return choice;
                }
            } else {
                System.out.println("Please enter a choice from | M | F | Valid Friend Number |");
            }
        } while (true);
    }

    /**
     * User visits friend and can steal their crops
     * 
     * @param sc            the scanner user uses to input
     * @param friendNumber  the friend index of the friend user wants to visit
     * @return              'M' or 'F' when user wants to go back to Main Menu or City Farmer
     */
    public char visitFriend(Scanner sc, int friendNumber) {        
        char choice;
        String input;
        User chosenFriend = friends.get(friendNumber - 1);
        
        do {
            visitCtrl.displayFriend(chosenFriend);

            input = Utility.getNonEmptyInput("[M]ain | City [F]armers | [S]teal >", 
                    sc, false);
            choice = input.charAt(0);

            if (choice == 'S'){
                List<Plot> plotsToBeStolen = visitCtrl.checkAnySteal(loggedInUser, chosenFriend);

                if (!plotsToBeStolen.isEmpty()){
                    visitCtrl.steal(loggedInUser, plotsToBeStolen);
                } else {
                    System.out.println("There are no plots available for stealing.");
                }

            } else if (choice == 'F' || choice == 'M') {
                return choice;

            } else {
                System.out.println("Please enter a choice from | M | F | S |");
            }
        } while (true);
    }

    /**
     * Displays the Visit Page, input choices, and prompt
     */
    public void display() {
        System.out.println();
        System.out.println("== Social Magnet :: City Farmers :: Visit Friend ==");
        System.out.println("Welcome, " + loggedInUser.getFullName() + "!");
        System.out.println("Title: " + loggedInUser.getRank() + "\t" + "Gold: " + loggedInUser.getGold());
        System.out.println();
        System.out.println("My Friends:");
        if (friends.size() < 1){
            System.out.println();
            System.out.println("You have no friends.");
        }
        for (int i=0; i < friends.size(); i++){
            User friend = friends.get(i);
            System.out.println((i + 1) +". " + friend.getFullName() + " (" + friend.getUsername() +")");
        }
        System.out.println();
        System.out.print("[M]ain | City [F]armers | Select Choice > ");
    }

}

