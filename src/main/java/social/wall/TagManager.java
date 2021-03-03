package social.wall;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import daos.FriendsDAO;
import entities.User;

/**
 * The TagManager class retrieves and deletes tags from a thread
 *  
 * @version 1.0 07 Apr 2020
 * @author Brian Goh
 */
public class TagManager {

    /** FDM provides access to the Friends table in the database */
    private FriendsDAO FDM;

    /** Creates a new TagManager object with a default Friends DM */
    public TagManager() {
        FDM = new FriendsDAO();
    }
    
    /**
     * Gets the usernames of user's friends tagged in the specified message
     * 
     * @param message   the message to check for tagged friends
     * @param user      the User object of the user
     * @return          List of usernames of all the tagged user's friends in message
     */
    public List<String> getTaggedFriendsUsernames(String message, User user) {

        List<String> allTags = new ArrayList<>();

        Matcher m = Pattern.compile("@\\w+").matcher(message);
        while (m.find()) {
            allTags.add(m.group().substring(1));
        }

        Set<String> allUniqueTags = new HashSet<>(allTags);

        // Check if matches are user's friends
        List<User> friends = FDM.getFriends(user);

        List<String> friendsUsernames = new ArrayList<>();
        for (User friend: friends) {
            friendsUsernames.add(friend.getUsername());
        }

        List<String> result = new ArrayList<>();

        for (String tag : allUniqueTags) {
            if (friendsUsernames.contains(tag)) {
                result.add(tag);
            }
        }

        return result;
    }

    /**
     * Deletes the '@' sign from in front of the specified friends' usernames in the message
     * @param message                   the message to modify
     * @param taggedFriendsUsernames    the usernames of the friends to remove '@' from
     * @return                          the modified message with '@' removed from in front of tagged usernames
     */
    public static String deleteTaggedFriendsFromMessage(String message, List<String> taggedFriendsUsernames) {

        String result = "";
        for (int i = 0; i < message.length(); i++) {
            
            if ((message.charAt(i) == '@') 
                    && ( (i == 0) 
                    || ( (i != 0) && (message.charAt(i-1) == ' ') ) )) {
                /*
                 * If @ is encountered after a space, 
                 * of if @ is encountered as the first character
                 * check if the phrase after it is a username
                 */
                String tagCandidate = "";

                i++;
                if (i == message.length()) {
                    // It has reached end of line - message ends with '@'
                    result += "@";
                    return result;
                }

                char currentChar = message.charAt(i);

                while (Character.isDigit(currentChar) || Character.isLetter(currentChar)) {
                    // Lengthens the tag candidate ch by ch till next ch is not alphanumeric
                    tagCandidate += currentChar;
                    i++;

                    if (i == message.length()) {
                        // It has reached end of line - skip to checking of candidate so far
                        break;
                    }
                    currentChar = message.charAt(i);
                }

                // if the candidate in front of @ is not a valid friend username, "@" is kept
                if (!(taggedFriendsUsernames.contains(tagCandidate)) ) {
                    result += "@";
                }

                result += tagCandidate;
                if (i < message.length()) {
                    result += currentChar;
                }

            } else {
                result += message.charAt(i);
            }
        }

        return result;
    }
}