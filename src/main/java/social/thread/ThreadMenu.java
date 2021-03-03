package social.thread;

import java.util.List;
import java.util.Scanner;

import entities.Reaction;
import entities.Thread;
import entities.User;
import util.Utility;

/**
 * The ThreadMenu class interacts with the user on the View a Thread Page 
 *
 * @version 1.4 07 Apr 2020
 * @author Wa Thone
 */
public class ThreadMenu {
    
    /** threadCtrl facilitates the interaction between the database and the ThreadMenu */
    private ThreadCtrl threadCtrl;

    /** thread is the Thread Object being viewed on the View a Thread Page */
    private Thread thread;
    
    /** loggedInUser is the User object of the logged in user */
    private User loggedInUser;

    /** likes is a List of Reaction objects corresponding to the likes of the thread */
    private List<Reaction> likes;

    /** dislikes is a List of Reaction objects corresponding to the dislikes of the thread */
    private List<Reaction> dislikes;

    /**
     * Creates a ThreadMenu object with the specified thread and loggedInUser
     * 
     * @param thread        the Thread object that is being viewed
     * @param loggedInUser  the User object of the logged in user
     */
    public ThreadMenu(Thread thread, User loggedInUser) {
        this.threadCtrl = new ThreadCtrl();
        this.thread = thread;
        this.loggedInUser = loggedInUser;
        this.likes = threadCtrl.getReactionByType(thread, true);
        this.dislikes = threadCtrl.getReactionByType(thread, false);
    }

    /**
     * Displays the View a Thread Page, input choices, and prompt
     */
    public void display() {
        System.out.println();
        System.out.println(" == Social Magnet :: View a Thread ==");
        displayThread();
        System.out.print("[M]ain | [K]ill | [R]eply | [L]ike | [D]islike > ");
    }

    /**
     * Takes in user's input choice and invoke corresponding methods
     * 
     * @return  'M' when the user chooses to go back to Main Menu
     */
    public char readOption() {
        Scanner sc = new Scanner(System.in);

        String input;
        char choice;

        do {
            display();

            input = Utility.getNonEmptyInput("[M]ain | [K]ill | [R]eply | [L]ike | [D]islike > ",
                     sc, false);

            choice = Utility.getCharFromInput(input);

            switch(choice) {
                case 'M':
                    // Back to menu
                    break;

                case 'K':
                    choice = killThread();
                    break;

                case 'R':
                    replyThread(sc);
                    break;

                case 'L':
                    likeThread();
                    break;

                case 'D':
                    dislikeThread();
                    break;

                default:
                    System.out.println("Please enter a choice from | M | K | R | L | D |");
            }
        } while(choice != 'M');

        return choice;
    }

    /**
     * Displays the thread text, comments, who liked and disliked the thread on the page
     */
    public void displayThread() {
        ThreadUtility.displayThreadText(thread, 1);
        ThreadUtility.displayComments(thread, 1);
        System.out.println();
        
        System.out.println("Who likes this post:");
        displayReactions(likes);

        System.out.println("Who dislikes this post:");
        displayReactions(dislikes);
        
    }

    /**
     * Displays the specified List of Reactions (likes or dislikes)
     * in the format: <index>. Full name (username)
     * 
     * @param likesOrDislikes   the List of Reaction objects of likes or dislikes
     */
    public void displayReactions(List<Reaction> likesOrDislikes) {

        for (int i = 1; i <= likesOrDislikes.size(); i++) {
            Reaction likeOrDislike = likesOrDislikes.get(i - 1);
            // get user - fullname and username and print
            String reactorFullname = threadCtrl.getReactorFullname(likeOrDislike);

            System.out.printf("%3d. %s (%s)\n", i, reactorFullname, likeOrDislike.getReactorUsername());
        }
        System.out.println();
    }

    /**
     * Deletes the thread completely from database if it was posted by the user.
     * Otherwise, the thread is removed from the user's wall only.
     * 
     * @return  'M' if the post was deleted successfully. Otherwise, return 'K' to stay on the page.
     */
    public char killThread() {
        if (!threadCtrl.checkCanKill(thread, loggedInUser)) {
            System.out.println("You have no rights to kill this thread.");
            return 'K';
        }

        // delete completely if owner
        if (threadCtrl.isOwnerOfPost(thread, loggedInUser)) {
            // error handling for owner deleting post
            boolean status = threadCtrl.deleteThreadCompletely(thread);

            if (status) {
                System.out.println("Post deleted successfully.");
                return 'M';
            } else {
                System.out.println("There was an error deleting this post. Please try again.");
                return 'K';
            }
        }

        // remove from the user's wall if not owner
        boolean status = threadCtrl.deleteThreadFromWall(thread, loggedInUser);

        if (status) {
            System.out.println("Post removed from your wall successfully.");
            return 'M';
        } else {
            System.out.println("There was an error removing this post. Please try again.");
            return 'K';
        }

    }

    /**
     * Takes in user's input as reply to the thread, and add the reply to the thread.
     * 
     * @param sc    the scanner that takes in user's input
     */
    public void replyThread(Scanner sc) {
        String reply = Utility.getNonEmptyInput("Enter your reply > ", sc, true);

        boolean status = threadCtrl.replyThread(thread, loggedInUser, reply);

        if (status) {
            System.out.println("Reply added successfully");
        } else {
            System.out.println("Reply was not added successfully. Please try again.");
        }
    }

    /**
     * If the user has not liked the thread before, adds the user's like to the thread.
     * Otherwise. removes the user's like from the thread.
     */
    public void likeThread() {
        Reaction previousReaction = threadCtrl.getPreviousReaction(likes, loggedInUser);

        if (previousReaction != null) {
            boolean result = threadCtrl.deleteReaction(thread, previousReaction);

            if (result) {
                // update the likes shown on page
                this.likes = threadCtrl.getReactionByType(thread, true);
            } else {
                System.out.println("Removal of your like was unsuccessful. Please try again.");
            }

        } else {
            boolean result = threadCtrl.reactToThread(thread, dislikes, loggedInUser, true);
            
            if (result) {
                // update the likes and dislikes shown on page, in case the opp rxn is deleted
                this.likes = threadCtrl.getReactionByType(thread, true);
                this.dislikes = threadCtrl.getReactionByType(thread, false);
            } else {
                System.out.println("Liking of this thread was unsuccessful. Please try again.");
            }

        }
    }

    /**
     * If the user has not disliked the thread before, adds the user's dislike to the thread.
     * Otherwise. removes the user's dislike from the thread.
     */
    public void dislikeThread() {
        Reaction previousReaction = threadCtrl.getPreviousReaction(dislikes, loggedInUser);

        if (previousReaction != null) {
            boolean result = threadCtrl.deleteReaction(thread, previousReaction);

            if (result) {
                // update the dislikes shown on page
                this.dislikes = threadCtrl.getReactionByType(thread, false);
            } else {
                System.out.println("Removal of your dislike was unsuccessful. Please try again.");
            }

        } else {
            boolean result = threadCtrl.reactToThread(thread, likes, loggedInUser, false);
            
            if (result) {
                // update the likes and dislikes shown on page, in case the opp rxn is deleted
                this.dislikes = threadCtrl.getReactionByType(thread, false);
                this.likes = threadCtrl.getReactionByType(thread, true);
            } else {
                System.out.println("Disliking of this thread was unsuccessful. Please try again.");
            }
        }
    }

}