package social.thread;

import java.util.List;

import daos.CommentDAO;
import daos.ReactionDAO;
import entities.Comment;
import entities.GiftThread;
import entities.PostThread;
import entities.Reaction;
import entities.Thread;

/**
 * The ThreadUtility class contains the utility functions for dealing with threads
 *  
 * @version 1.4 06 Apr 2020
 * @author Wa Thone
 */
public class ThreadUtility {
    
    /**
     * Displays the list of top threads specified on wall / newsfeed
     * 
     * @param threads   the List of Thread objects to display on page
     */
    public static void displayTopThreads(List<Thread> threads){

        if (threads == null || threads.size() == 0) {
            System.out.println("\nNo threads to display.");
            return;
        }

        for (int i = 1; i <= threads.size(); i++) {
            Thread thread = threads.get(i - 1);
            displaySimplifiedThread(thread, i);
            System.out.println();
        }
    }

    /**
     * Displays simplified thread as a listing with index on e.g. My Wall, News Feed, etc
     * 
     * @param thread    the Thread object
     * @param index     the index of the thread
     */
    public static void displaySimplifiedThread(Thread thread, int index) {
        displayThreadText(thread, index);

        ReactionDAO RDM = new ReactionDAO();

        List<Reaction> likes = RDM.getLikes(thread);
        List<Reaction> dislikes = RDM.getDislikes(thread);

        displayLikesDislikesCount(likes, dislikes);
        displayComments(thread, index);
    }

    /**
     * Gets the thread specified by the user's input from the list of threads
     * 
     * @param threads   the list of Thread objects to select from
     * @param input     the user's input, of format <X><index>
     * @return          the Thread object from the list, corresponding to the index of the user's input
     * @throws InvalidThreadException
     */
    public static Thread getThreadByInput(List<Thread> threads, String input) throws InvalidThreadException{
        String threadString = input.substring(1);
        int threadNumber;

        if (threads.size() == 0) {
            throw new InvalidThreadException("There is no thread to be viewed!");
        }
        
        try {
            threadNumber = Integer.parseInt(threadString);
        } catch (NumberFormatException e) {
            throw new InvalidThreadException(
                    "T needs to be followed by an integer of a valid thread number.");
        }

        if (threadNumber < 1) {
            throw new InvalidThreadException(
                    "Invalid thread number! Min number allowed is 1.");
        }

        if (threadNumber > threads.size()) {
            throw new InvalidThreadException(
                    "Invalid thread number! Max number allowed is " + threads.size());
        }

        Thread thread = threads.get(threadNumber - 1);

        return thread;
    }

    /**
     * Displays the main text of the specified thread by index
     * 
     * @param thread    the Thread object
     * @param index     the index to start the text with
     */
    public static void displayThreadText(Thread thread, int index) {

        if (thread instanceof GiftThread) {
            GiftThread giftThread = (GiftThread) thread;
            System.out.printf("%d. %s: Here is a bag of %s seeds for you. - City Farmers\n", 
                    index, thread.getSenderUsername(), giftThread.getCropName());
            
        } else if (thread instanceof PostThread) {
            PostThread postThread = (PostThread) thread;
            System.out.printf("%d. %s: %s\n", index, 
                    thread.getSenderUsername(), postThread.getText());
        }
    }

    /**
     * Displays the number of likes and dislikes formatted as [ <no.> likes, <no.> dislikes ]
     * 
     * @param likes     List of Reaction of type like
     * @param dislikes  List of Reaction of type dislike
     */
    public static void displayLikesDislikesCount(List<Reaction> likes, List<Reaction> dislikes) {
        System.out.printf("[ %d likes, %d dislikes ]\n", likes.size(), dislikes.size());
    }

    /**
     * Displays the comments of the specified thread, starting with index
     * 
     * @param thread    the Thread object
     * @param index     the index to start the comment with
     */
    public static void displayComments(Thread thread, int index) {
        CommentDAO CDM = new CommentDAO();
        List<Comment> comments = CDM.getComments(thread);
        
        if (comments == null) {
            return;
        }

        for (int i = 1; i <= comments.size(); i++ ) {
            Comment comment = comments.get(i - 1);
            String username = comment.getCommenterUsername();
            String commentText = comment.getText();

            System.out.printf("%5d.%d %s: %s\n", index, i, username, commentText);
        }
    }
}