package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Utility class contains utility methods for use in the app
 *
 * @version 1.1 07 Apr 2020
 * @author Wa Thone
 */
public class Utility {

    /**
     * Prompts user continuously until a non empty input is given
     * 
     * @param prompt        the message that is used as prompt when asking for input
     * @param sc            the scanner that takes in user's input
     * @param printFirst    true if prompt needs to be printed first, otherwise false
     * @return              the user's input that is not empty
     */
    public static String getNonEmptyInput(String prompt, Scanner sc, boolean printFirst) {

        if (printFirst) {
            System.out.print(prompt);
        }
        String result = sc.nextLine();

        while (result.trim().equals("")) {
            System.out.println("Input cannot be empty. Please try again.");

            System.out.print(prompt);
            result = sc.nextLine();
        }

        return result;
    }

    /**
     * Prompts user continuously until a non empty input is given with prompt printed first
     * 
     * @param prompt        the message that is used as prompt when asking for input
     * @param sc            the scanner that takes in user's input
     * @return              the user's input that is not empty
     */
    public static String getNonEmptyInput(String prompt, Scanner sc) {
        return getNonEmptyInput(prompt, sc, true);
    }

    /**
     * Gets the front character of the specified menu only if they have one character in it
     * unless it is a specified excused characters e.g. T will be followed by ID for thread selection
     * If invalid, 'I' for Invalid is returned
     * 
     * @param input         the input string inputted by the user
     * @param excusedChar   the character excused to have more letters behind e.g. T<ID>
     * @return              the front character of a valid input, else 'I' for Invalid.
     */
    public static char getCharFromInput(String input, List<Character> excusedChars) {
        char frontChar = input.charAt(0);

        if ((excusedChars.contains(frontChar)) || (input.substring(1).equals(""))) {
            return frontChar;
        }
        
        return 'I';
    }

    /**
     * Gets the front character of the specified menu only if they have one character in it
     * If invalid, 'I' for Invalid is returned
     * 
     * @param input         the input string inputted by the user
     * @return              the front character of a valid input, else 'I' for Invalid.
     */
    public static char getCharFromInput(String input) {
        return getCharFromInput(input, new ArrayList<Character>());
    }

    /**
     * Prompts user continuously until a number has been inputted.
     * 
     * @param sc        the scanner that takes in user's input
     * @param prompt    the prompt displayed to user
     * @return          the number inputted
     */
    public static int promptForNum(Scanner sc, String prompt){
        while (true){
            try {
                String input = getNonEmptyInput(prompt, sc, false);
                int num1 = Integer.parseInt(input);
                return num1;
            }
            catch (NumberFormatException e){
                System.out.println("Please enter a number.");
                System.out.print(prompt);
            }
        }
    }

    /**
     * Takes in string and checks if it is Numeric
     * 
     * @param str   string to check
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
}