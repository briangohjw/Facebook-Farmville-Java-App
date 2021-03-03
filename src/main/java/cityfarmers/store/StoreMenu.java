package cityfarmers.store;

import java.util.List;
import java.util.Scanner;

import entities.Crop;
import entities.User;
import util.Utility;

/**
 * The StoreMenu class interacts with the user on the Store Page
 * @version 1.0 07 Apr 2020
 * @author Wan Ding Yang
 */ 
public class StoreMenu {

    private StoreCtrl storeCtrl;
    private User loggedInUser;

    /** 
     * Creates a StoreMenu object with a default storeCtrl
     * 
     * @param loggedInUser  the user
     */
    public StoreMenu( User loggedInUser) {

        /** storeCtrl facilitates the interaction between the database and the StoreMenu*/
        this.storeCtrl = new StoreCtrl();

        /** the User */
        this.loggedInUser = loggedInUser;

    }

    /**
     * Takes in user's input choice and invoke corresponding methods.
     * @return  'M' or 'F' when user decides to go back to Main Menu or City Farmers
     */
    public char readOption() {
        Scanner sc = new Scanner(System.in);
        String input;
        char choice;

        do {
            display();
            input = Utility.getNonEmptyInput("[M]ain | City [F]armers | Select Choice > ", 
                    sc, false);

            choice = input.charAt(0);

            if ((choice == 'M' || choice == 'F') && input.length() == 1) {
                return choice;
            }
            if ((Character.isDigit(choice)) && (Utility.isNumeric(input))) {
                processInput(sc, input, loggedInUser);
            } else {
                System.out.println("Please enter a choice from | M | F | Valid Crop Number |");
            }
        } while (true);
    }

    /**
     * Displays the Farmland Page, input choices, and prompt
     */
    public void display() {
        System.out.println();
        System.out.println("== Social Magnet :: City Farmers :: My Store ==");
        System.out.println("Welcome, " + loggedInUser.getFullName() + "!");
        System.out.println("Title: " + loggedInUser.getRank() + "\t" + "Gold: " + loggedInUser.getGold());
        System.out.println();
        System.out.println("Seeds Available:");
        // readCrops and display the Crops 
        int count = 0;
        List<Crop> crops = storeCtrl.getAllCrops();
        for (Crop crop: crops){
            count += 1;
            System.out.printf("%d. %-11s- %d gold\n",count, crop.getCropName(), crop.getCost());
            int harvestTime = crop.getTimeToHarvest();
            if (harvestTime%60 == 0){
                System.out.println("   Harvest in: " + harvestTime/60 + " hours");
            } else {
                System.out.println("   Harvest in: " + harvestTime + " minutes");  
            }
            System.out.println("   XP Gained: " + crop.getXp());
        }
        System.out.print("[M]ain | City [F]armers | Select Choice > ");

    }

    /**
     * Takes in user's input and processes it
     * @param sc            Scanner
     * @param input         user's input 
     * @param loggedInUser  the logged in user
     */
    public void processInput(Scanner sc, String input, User loggedInUser){
        List<Crop> crops = storeCtrl.getAllCrops();
        int cropNumber = Integer.parseInt(input);
        
        if (cropNumber > crops.size() || cropNumber < 1) {
            System.out.println("Invalid crop number! Number range allowed is from 1 to " 
                    + crops.size());
        } else {
            attemptPurchase(sc, cropNumber, loggedInUser, crops);
        }

    }

    /**
     * Asks user for quantity of crops they want to purchase. Sends the purchase request to data base
     * If unsuccessful e.g. not enough gold, user is sent back to My Store Page
     * 
     * @param sc            Scanner
     * @param cropNumber    user's input crop number
     * @param loggedInUser  the logged in user
     * @param crops         user crops
     */
    public void attemptPurchase(Scanner sc, int cropNumber, User loggedInUser, List<Crop> crops) {
        Crop chosenCrop = crops.get(cropNumber - 1);
        int amount = 0;

        System.out.print("Enter quantity > ");
        amount = Utility.promptForNum(sc, "Enter quantity > ");

        if (amount > 0) {
            storeCtrl.purchase(chosenCrop, amount, loggedInUser);
        } else {
            System.out.println("You cannot purchase " + amount + " crops.");
        }
    }
}
