package cityfarmers.inventory;

import java.util.Map;
import java.util.Scanner;

import entities.Crop;
import entities.User;
import util.Utility;

/**
 * The InventoryMenu class interacts with the user on the Inventory Page
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */ 
public class InventoryMenu {
    /** inventoryCtrl facilitates the interaction between the database and the InventoryMenu*/
    private InventoryCtrl inventoryCtrl;

    /** the User */
    private User loggedInUser;
    
    /** 
     * Creates a InventoryMenu object with a default inventoryCtrl and loggedIn user 
     * 
     * @param loggedInUser  the user
     * */
    public InventoryMenu(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.inventoryCtrl  = new InventoryCtrl();
    }

    /**
     * Takes in user's input choice and invoke corresponding methods.
     * @return  corresponding return character
     */
    public char readOption() {
        Scanner sc = new Scanner(System.in);
        String input;
        char choice;

        do {
            display(loggedInUser);
            input = Utility.getNonEmptyInput("[M]ain | City [F]armers | Select Choice > ", 
            sc, false);
            
            choice = Utility.getCharFromInput(input);

            switch(choice) {
                case 'M':
                    break;

                case 'F':
                    break;

                default:
                    System.out.println("Please enter a choice from | M | F |");
            }
        } while (choice != 'F' && choice != 'M');
        return choice;
        }
        
    /**
     * Displays the Inventory Page, input choices, and prompt
     * 
     * @param loggedInUser  the user
     */
    public void display(User loggedInUser) {
        Map<Crop, Integer> crops = inventoryCtrl.getUserCrops(loggedInUser);
        System.out.println();
        System.out.println("== Social Magnet :: City Farmers :: My Inventory ==");
        System.out.println("Welcome, " + loggedInUser.getFullName() + "!");
        System.out.println("Title: " + loggedInUser.getRank() + "\t" + "Gold: " + loggedInUser.getGold());
        System.out.println(); 
        System.out.println("My Seeds:");
        int count = 0;

        if (crops.size() < 1){
            System.out.println();
            System.out.println("You have no seeds.");
            System.out.println();
        } else {
            for (Map.Entry<Crop, Integer> entry : crops.entrySet()) {
                count += 1;
                System.out.println(count + ". " + entry.getValue() + " Bags of " + entry.getKey().getCropName());
            }
        }
        System.out.print("[M]ain | City [F]armers | Select Choice > ");
        }

}