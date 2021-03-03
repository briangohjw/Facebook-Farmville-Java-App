package social.friends;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import entities.User;
import social.wall.FriendWallMenu;
import util.Utility;

/**
 * The FriendsMenu class interacts with the user on the My Friends Page 
 *
 * @version 1.3 07 Apr 2020
 * @author Wa Thone
 */
public class FriendsMenu {
    
    /** The friendsCtrl facilitates the interaction between the database and the FriendsMenu */
    private FriendsCtrl friendsCtrl;

    /** loggedInUser is the User object of the logged in user */
    private User loggedInUser;

    /** friends is the List of User objects of friends of the user */
    private List<User> friends;

    /** requests is the List of User objects of those who have sent friend requests to the user */
    private List<User> requests;

    /** noOfFriends is the number of friends the user has */
    private int noOfFriends;

    /** noOfRequests is the number of friend requests the user has */
    private int noOfRequests;

    /**
     * Creates a FriendsMenu object with the specified loggedInUser
     * 
     * @param loggedInUser  the User object of the logged in user
     */
    public FriendsMenu(User loggedInUser) {
        this.friendsCtrl = new FriendsCtrl();
        this.loggedInUser = loggedInUser;
        this.friends = friendsCtrl.getFriends(loggedInUser);
        this.requests = friendsCtrl.getFriendRequests(loggedInUser);
        this.noOfFriends = getListSize(friends);
        this.noOfRequests = getListSize(requests);
    }

    /**
     * Displays the My Friends Page, input choices, and prompt
     */
    public void display() {
        System.out.println();
        System.out.println(" == Social Magnet :: My Friends ==");
        System.out.println("Welcome, " + loggedInUser.getFullName() + "!\n");
        
        displayFriendsOrRequests(friends, 0, "Friends");
        System.out.println();
        displayFriendsOrRequests(requests, noOfFriends, "Requests");

        System.out.print("[M]ain | [U]nfriend | re[Q]uest | [A]ccept | [R]eject | [V]iew > ");
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

            input = Utility.getNonEmptyInput(
                    "[M]ain | [U]nfriend | re[Q]uest | [A]ccept | [R]eject | [V]iew > ", 
                    sc, false);

            
            ArrayList<Character> excusedChars = new ArrayList<>(Arrays.asList('U','A','R','V'));
            choice = Utility.getCharFromInput(input, excusedChars);

            switch(choice) {
                case 'M':
                    // Back to menu
                    break;

                case 'U':
                    unfriendFriend(input);
                    break;

                case 'Q':
                    sendFriendRequest(sc);
                    break;

                case 'A':
                    acceptFriendRequest(input);
                    break;

                case 'R':
                    rejectFriendRequest(input);
                    break;

                case 'V':
                    choice = viewFriend(input);
                    break;

                default:
                    System.out.println("Please enter a choice from | M | U | Q | A | R | V |");
            }
        } while(choice != 'M');
    }

    /**
     * Displays the specified List of User objects of either friends or friend requests,
     * specified by the list type, with the first index of the list starting from 
     * specified start_index
     * 
     * @param friendsOrRequests the List of User objects of friends or friend requests
     * @param start_index       the starting index of the list
     * @param listType          the list type, either 'Friends' or 'Requests'
     */
    public void displayFriendsOrRequests(List<User> friendsOrRequests, int start_index, String listType) {
        System.out.println("My " + listType + ":");

        if (friendsOrRequests.size() == 0) {
            System.out.println("No one on your " + listType + " List.");

        } else {

            for (int i = 1; i <= friendsOrRequests.size(); i++) {
                User friendOrRequest = friendsOrRequests.get(i - 1);
                System.out.printf("%d. %s\n", (start_index + i), friendOrRequest.getUsername());
            }
        }
    }

    /**
     * Checks if the specified input for user's choice of friend is valid.
     * If valid, unfriends the friend chosen.
     * 
     * @param input     the input of user's choice specifying the friend, format U<index>
     */
    public void unfriendFriend(String input) {
        if (noOfFriends == 0) {
            System.out.println("You have no Friends to unfriend.");
            return;
        }

        if (!checkValidFriendRange(input)) {
            System.out.println("Invalid choice for Friends. Please enter a valid integer choice.");
            return;
        }

        User friendUser = getUserByInput(input);
        String friendUsername = friendUser.getUsername();

        boolean result = friendsCtrl.unfriendFriend(friendUser, loggedInUser);
        if (result) {
            System.out.println(friendUsername + " is removed from your Friend List successfully.");
            // update the friend list shown after unfriending
            this.friends = friendsCtrl.getFriends(loggedInUser);
            this.noOfFriends = getListSize(friends);
        } else {
            System.out.println("There was a problem removing " + friendUsername + " From your Friend List.");
        }
    }

    /**
     * Takes in user's input as the username of a user to send friend request to.
     * If valid username, friend request is sent to that user.
     * 
     * @param sc    the scanner that takes in user's input
     */
    public void sendFriendRequest(Scanner sc) {

        System.out.print("Enter the username > ");
        String requestUsername = sc.nextLine();

        while (requestUsername.equals("")) {
            System.out.println("Please enter a non empty username.");

            System.out.println("Enter the username > ");
            requestUsername = sc.nextLine();
        }

        // check validity and print the type of invalid errors
        String validityType = friendsCtrl.getFriendRequestValidityType(requestUsername, loggedInUser);

        if (validityType.equals("valid")) {
            boolean result = friendsCtrl.sendFriendRequest(requestUsername, loggedInUser);
            if (result) {
                System.out.println("A friend request is sent to " + requestUsername);   
            } else {
                System.out.println("Friend request to " + requestUsername + " was not successful.");
            }
        } else {
            System.out.println(validityType);
        }
    }

    /**
     * Checks if the specified input for user's choice of friend request is valid.
     * If valid, the friend request is accepted
     * 
     * @param input     the input of user's choice specifying the friend request, format A<index>
     */
    public void acceptFriendRequest(String input) {
        if (noOfRequests == 0) {
            System.out.println("You have no Friend Requests to accept.");
            return;
        }
        
        if (!checkValidFriendRequestRange(input) ) {
            System.out.println("Invalid choice for Friend Requests. Please enter a valid integer choice.");
            return;
        }
        
        User friendUser = getUserByInput(input);
        String friendUsername = friendUser.getUsername();

        boolean result = friendsCtrl.acceptFriendRequest(friendUser, loggedInUser);

        if (result) {
            System.out.println(friendUsername + " is now your friend.");
            // update the friend list and friend request list shown after accepting friend request
            this.friends = friendsCtrl.getFriends(loggedInUser);
            this.noOfFriends = getListSize(friends);
            
            this.requests = friendsCtrl.getFriendRequests(loggedInUser);
            this.noOfRequests = getListSize(requests);
        } else {
            System.out.println(friendUsername + " is not successfully added. Try again.");
        }
    }

    /**
     * Checks if the specified input for user's choice of friend request is valid.
     * If valid, the friend request is rejected
     * 
     * @param input     the input of user's choice specifying the friend request, format R<index>
     */
    public void rejectFriendRequest(String input) {
        if (noOfRequests == 0) {
            System.out.println("You have no Friend Requests to reject.");
            return;
        }
                
        if (!checkValidFriendRequestRange(input) ) {
            System.out.println("Invalid choice for Friend Requests. Please enter a valid integer choice.");
            return;
        }
        
        User friendUser = getUserByInput(input);
        String friendUsername = friendUser.getUsername();

        boolean result = friendsCtrl.rejectFriendRequest(friendUser, loggedInUser);

        if (result) {
            System.out.println(friendUsername + " is successfully rejected.");
            // update the friend request list shown after rejecting friend request
            this.requests = friendsCtrl.getFriendRequests(loggedInUser);
            this.noOfRequests = getListSize(requests);
        } else {
            System.out.println(friendUsername + " is not successfully rejected. Try again.");
        }
    }

    /**
     * Checks if the specified input for user's choice of friend friend is valid.
     * If valid, directs user to that Friend's Wall Page.
     * 
     * @param input     the input of user's choice specifying the friend, format V<index>
     * @return          'M' when the user chooses to go back to Main Menu from Friend's Wall Page, 'V' if error
     */
    public char viewFriend(String input) {
        if (noOfFriends == 0) {
            System.out.println("You have no Friends to view.");
            return 'V';
        }
        
        if (!checkValidFriendRange(input)) {
            System.out.println("Invalid choice for Friends. Please enter a valid integer choice.");
            return 'V';
        }
        
        User friendUser = getUserByInput(input);

        FriendWallMenu friendWallMenu = new FriendWallMenu(friendUser, loggedInUser);
        return friendWallMenu.readOption();
    }

    /**
     * Checks that the input's index is within the range of numbers shown for friend list
     * 
     * @param input     the input of user's choice specifying the friend, format <X><index>
     * @return          true if input's index is within valid range for friends, false otherwise.
     */
    public boolean checkValidFriendRange(String input) {
        return checkValidRange(input, 1, noOfFriends);
    }

    /**
     * Checks that the input's index is within the range of numbers shown for friend request list
     * 
     * @param input     the input of user's choice specifying the friend request, format <X><index>
     * @return          true if input's index is within valid range for requests, false otherwise.
     */
    public boolean checkValidFriendRequestRange(String input) {
        return checkValidRange(input, noOfFriends + 1, noOfFriends + noOfRequests);
    }

    /**
     * Checks that the input's index is within the range of specified start index and end index
     * 
     * @param input         the input of user's choice, format <X><index>
     * @param start_index   the minimum value that the index can take to be valid
     * @param end_index     the maximum value that the index can take to be valid
     * @return              true if index is within the range specified, false otherwise.
     */
    public boolean checkValidRange(String input, int start_index, int end_index) {

        try {
            String inputNumber = input.substring(1);
            int i = Integer.parseInt(inputNumber);
            if (i >= start_index && i <= end_index) {
                return true;
            }
            return false;

        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Gets the size of the specified list
     * 
     * @param arrayList     the List of String to find size of
     * @return              0 if list is null, otherwise, the size of the list
     */
    public int getListSize(List<User> arrayList) {
        if (arrayList == null){
            return 0;
        } else {
            return arrayList.size();
        }
    }
    
    /**
     * Gets the selected User by the specified user's input
     * 
     * @param input     the input of user's choice, format <X><index>
     * @return          the User object corresponding to the specified input's index
     */
    public User getUserByInput(String input) {
        int friendOrRequestIndex = Integer.parseInt(input.substring(1));

        if (friendOrRequestIndex > noOfFriends) {
            // it's a request
            return requests.get(friendOrRequestIndex - noOfFriends - 1);
        } else {
            // it's a friend
            return friends.get(friendOrRequestIndex - 1);
        }
    }
}