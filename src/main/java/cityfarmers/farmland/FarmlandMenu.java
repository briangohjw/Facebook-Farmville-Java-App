package cityfarmers.farmland;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import entities.Plot;
import entities.User;
import util.Utility;

/**
 * The FarmlandMenu class interacts with the user on the Farmland Page
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */ 
public class FarmlandMenu {

    /** farmlandCtrl facilitates the interaction between the database and the FarmlandMenu*/
    private FarmlandCtrl farmlandCtrl;

    /** the User */
    private User loggedInUser;

    /** 
     * Creates a FarmlandMenu object with a default farmlandCtrl and loggedIn user 
     * 
     * @param loggedInUser  the user
     * */
    public FarmlandMenu(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        farmlandCtrl = new FarmlandCtrl();
    }

    /**
     * Takes in user's input choice and invoke corresponding methods.
     * @return  corresponding character output M || F
     */
    public char readOption() {
        Scanner sc = new Scanner(System.in);
        String input;
        char choice;
        do {
            display();
            input = Utility.getNonEmptyInput("[M]ain | City [F]armers | [P]lant | [C]lear | [H]arvest > ", 
            sc, false);
                        
            ArrayList<Character> excusedChars = new ArrayList<>(Arrays.asList('P', 'C'));
            choice = Utility.getCharFromInput(input, excusedChars);

            switch(choice) {
                case 'H':
                    harvest();
                    break;
                
                case 'P':
                    choice = plant(input);
                    break;

                case 'C':
                    clear();
                    break;

                case 'F':
                    break;

                case 'M':
                    break;
                    
                default:
                    System.out.println("Please enter a choice from | M | F | P + Valid Plot Number | C | H |");
            }
        } while (choice != 'F' && choice != 'M');
        return choice;
    }

    /**
     * Displays the Farmland Page, input choices, and prompt
     */
    public void display() {
        List<Plot> plots = farmlandCtrl.getPlotsOwnedByUser(loggedInUser.getUsername());
        // City Farmer header information 
        System.out.println();
        System.out.println("== Social Magnet :: City Farmers :: My Farmland ==");
        System.out.println("Welcome, " + loggedInUser.getFullName() + "!");
        System.out.println("Title: " + loggedInUser.getRank() + "\t" + "Gold: " + loggedInUser.getGold());
        System.out.println();

        // List number of plots 
        System.out.println("You have " + plots.size() + " plots of land.");
        farmlandCtrl.displayPlots(plots);
        System.out.print("[M]ain | City [F]armers | [P]lant | [C]lear | [H]arvest > ");
    }

    /**
     * Plants the selected plot if valid. 
     * 
     * @param input     user input
     * @return character to return to menu
     */
    public char plant(String input) {
        return farmlandCtrl.plant(input, loggedInUser);
    }

    /**
     * Harvests the user's plots if valid. 
     */
    public void harvest() {
        farmlandCtrl.harvest(loggedInUser);
    }

    
    /**
     * Clears the user's plots if valid. 
     */
    public void clear() {
        farmlandCtrl.clear(loggedInUser);
    }
}