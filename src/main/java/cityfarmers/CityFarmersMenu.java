package cityfarmers;

import java.util.Scanner;

import cityfarmers.farmland.FarmlandMenu;
import cityfarmers.inventory.InventoryMenu;
import cityfarmers.sendgift.SendGiftMenu;
import cityfarmers.store.StoreMenu;
import cityfarmers.visit.VisitMenu;
import entities.User;
import util.Utility;

/**
 * The CityFarmersMenu class interacts with the user on the CityFarmers Page
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */ 
public class CityFarmersMenu {
    private User loggedInUser;

    /** 
     * Creates a CityFarmersMenu object.
     * 
     * @param loggedInUser  the user
     */
    public CityFarmersMenu(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    /**
     * Takes in user's input choice and invoke corresponding methods.
     * 
     * @return  user's selected choice
     */
    public char readOption() {
        Scanner sc = new Scanner(System.in);
        String input;
        char choice;

        do {
            display();
            input = Utility.getNonEmptyInput("[M]ain | Enter your choice > ", 
            sc, false);

            choice = Utility.getCharFromInput(input);

            switch(choice) {
                case '1':
                // My farmland
                    choice = displayFarmlandMenu();
                    break;

                case '2':
                // My Store
                    choice = displayMyStoreMenu();
                    break;

                case '3':
                // My inventory 
                    choice = displayInventoryMenu();
                    break;
                    
                case '4':
                // Visit Friend
                    choice = displayVisitMenu();
                    break;

                case '5':
                // Send Gift 
                    choice = displaySendGiftMenu();
                    break;

                case 'M':
                // Main Menu
                    break;
                    
                default:
                    System.out.println("Please enter a choice from M | 1 | 2 | 3 | 4 | 5 |");
            }
        } while(choice != 'M');
        return choice;
    }

    /**
     * Displays the CityFarmer Page, input choices, and prompt
     */
    public void display() {
        System.out.println();
        System.out.println("== Social Magnet :: City Farmers ==");
        System.out.println("Welcome, " + loggedInUser.getFullName());
        System.out.println("Title: " + loggedInUser.getRank() + "\t" + "Gold: " + loggedInUser.getGold());
        System.out.println();
        System.out.println("1. My Farmland");
        System.out.println("2. My Store"); 
        System.out.println("3. My Inventory");
        System.out.println("4. Visit Friend");
        System.out.println("5. Send Gift");
        System.out.print("[M]ain | Enter your choice > ");
    }

    /**
     * Directs user to the My Farmland
     * 
     * @return  M to be back to main
     */
    public char displayFarmlandMenu() {
        FarmlandMenu farmlandMenu = new FarmlandMenu(loggedInUser);
        return farmlandMenu.readOption();
    }

    /**
     * Directs user to the My Store
     * 
     * @return  M to be back to main
     */
    public char displayMyStoreMenu() {
        StoreMenu myStoreMenu = new StoreMenu(loggedInUser);
        return myStoreMenu.readOption();
    }

    /**
     * Directs user to the My Inventory
     * 
     * @return  M to be back to main
     */
    public char displayInventoryMenu() {
        InventoryMenu myInventory = new InventoryMenu(loggedInUser);
        return myInventory.readOption();
    }

    /**
     * Directs user to the Visit Menu
     * 
     * @return  M to be back to main
     */
    public char displayVisitMenu() {
        VisitMenu visitFriend = new VisitMenu(loggedInUser);
        return visitFriend.readOption();
    }

    /**
     * Directs user to the SendGift Menu
     * 
     * @return  M to be back to main
     */
    public char displaySendGiftMenu() {
        SendGiftMenu sendGift = new SendGiftMenu(loggedInUser);
        return sendGift.readOption();
    }
    

    
}