package cityfarmers.farmland;

/**
 * The InvalidPlotException class is an exception for invalid plot
 *  
 * @version 1.0 07 Apr 2020
 * @author Wan Ding Yang
 */
public class InvalidPlotException extends Exception {
    /**
     * Creates InvalidPlotException object with the specified error exception
     * @param message   the exception message
     */
    public InvalidPlotException(String message) {
        super(message);
    }
}