package cityfarmers.sendgift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import entities.Crop;
import entities.User;
import util.Utility;

/**
 * The SendGiftMenu class interacts with the user on the SendGift Page
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */ 
public class SendGiftMenu {

    SendGiftCtrl sendGiftCtrl;
    User loggedInUser;
    List<Crop> crops;

    /** 
     * Creates a SendGiftMenu object with a default sendGiftCtrl
     * 
     * @param loggedInUser  the user
     */
    public SendGiftMenu(User loggedInUser) {
        this.sendGiftCtrl = new SendGiftCtrl();
        this.loggedInUser = loggedInUser;
        this.crops = sendGiftCtrl.getAllCrops();
    }

    /**
     * Takes in user's input choice and checks if it is Numeric
     * @param str   string to check if its a number
     * @return      true if numeric, false otherwise
     */
    public static boolean isNumeric(String str) { 
        try {
          Double.parseDouble(str);
          return true;
        } catch(NumberFormatException e){
          return false;
        }
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
            display();
            input = Utility.getNonEmptyInput("[R]eturn to main | Select choice > ", 
                    sc, false);

            choice = input.charAt(0);

            if ((choice == 'R') && (input.length() == 1)) {
                return 'M';
            }

            if ((Character.isDigit(choice)) && (Utility.isNumeric(input))) {
                processInput(sc, input, loggedInUser);
            } else {
                System.out.println("Please enter a choice from | R | Valid Crop Number |");
            }
        } while (true);
    }

    /**
     * Displays the SendGift Page, input choices, and prompt
     */
    public void display(){
        List<Crop> crops = sendGiftCtrl.getAllCrops();
        // City Farmer header information 
        System.out.println();
        System.out.println("== Social Magnet :: City Farmers :: Send a Gift ==");
        System.out.println("Welcome, " + loggedInUser.getFullName() + "!");
        System.out.println("Title: " + loggedInUser.getRank() + "\t" + "Gold: " + loggedInUser.getGold());
        System.out.println();
        System.out.println("Gifts Available:");
        for (int i = 0; i < crops.size(); i++){
            System.out.println(i+1 + ". 1 Bag of " + crops.get(i).getCropName() + " Seeds");
        }
        System.out.print("[R]eturn to main | Select choice > ");
    }

    /**
     * Takes in user's input and processes it
     * @param sc            Scanner
     * @param input         user's input 
     * @param loggedInUser  the logged in user
     */
    public void processInput(Scanner sc, String input, User loggedInUser) {
        List<Crop> crops = sendGiftCtrl.getAllCrops();
        int cropNumber = Integer.parseInt(input);

        if (cropNumber > crops.size() || cropNumber < 1) {
            System.out.println("Invalid crop number! Number range allowed is from 1 to " 
                    + crops.size());
        } else {
            attemptGifting(sc, cropNumber, loggedInUser, crops);
        }
    }

    /**
     * Takes in user's input crop Number and invoke corresponding methods.
     * @param sc            Scanner
     * @param cropNumber    user's input crop number
     * @param loggedInUser  the logged in user
     * @param crops         user crops
     */
    public void attemptGifting(Scanner sc, int cropNumber, User loggedInUser, List<Crop> crops) {
        if (sendGiftCtrl.haveSentFiveGiftsToday(loggedInUser)) {
            System.out.println("You have already sent 5 gifts today.");
            
            return;
        }

        // Getting a a non empty list of candidate usernames to send gift to
        String input = Utility.getNonEmptyInput("Send to > ", sc);
                
        input += ',';
        List<String> candidateUsernames = Arrays.asList(input.trim().split("\\s*,\\s*"));

        while (candidateUsernames.size() == 0) {
            System.out.println("Input cannot be empty. Please try again.");
            input = Utility.getNonEmptyInput("Send to > ", sc);
                    
            input += ',';
            candidateUsernames = Arrays.asList(input.trim().split("\\s*,\\s*"));
        }

        // Check if every candidate is valid, if so gift is sent.
        boolean canSend = checkValidCandidates(candidateUsernames, loggedInUser);

        if (canSend){
            Crop chosenCrop = crops.get(cropNumber-1);
            for (String friend: candidateUsernames){
                sendGiftCtrl.sendGift(loggedInUser, friend, chosenCrop);
            }
            System.out.println("Gift posted to your friends' wall.");
        }
    }

    /**
     * Checks if all the usernames in the specified candidate list are valid to send gift to
     * 
     * @param candidateUsernames    the list of usernames to check
     * @param loggedInUser          the user
     * @return                      true if everyone in the list is valid to send gift to, false otherwise
     */
    public boolean checkValidCandidates(List<String> candidateUsernames, User loggedInUser) {
        if (candidateUsernames == null || candidateUsernames.size() == 0) {
            return false;
        }

        boolean canSend = true;
        List<String> friendsToSend = new ArrayList<>();
        
        for (String friend: candidateUsernames){
            if (friend.equals(loggedInUser.getUsername())) {
                System.out.println("You cannot send a gift to yourself.");
                canSend = false;
                
            } else if (!sendGiftCtrl.isValidUser(friend)) {
                System.out.println(friend + " does not exist.");
                canSend = false;

            } else if (!sendGiftCtrl.isFriend(friend, loggedInUser)) {
                System.out.println(friend + " is not your friend.");
                canSend = false;

            } else if (sendGiftCtrl.haveSentGiftToThisUserToday(loggedInUser, friend)) {
                System.out.println("You have already sent " + friend + " a gift today.");
                canSend = false;

            } else if (friendsToSend.contains(friend)) {
                System.out.println("Duplicate " + friend + " entered.");
                canSend = false;

            } else {
                friendsToSend.add(friend);
            }
        }
        return canSend;
    }

}