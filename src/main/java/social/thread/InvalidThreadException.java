package social.thread;

/**
 * The InvalidThreadException class is an exception for invalid thread
 *  
 * @version 1.0 04 Apr 2020
 * @author Wa Thone
 */
public class InvalidThreadException extends Exception {

    /**
     * Creates InvalidThreadException object with the specified error exception
     * @param message   the exception message
     */
    public InvalidThreadException(String message) {
        super(message);
    }
}