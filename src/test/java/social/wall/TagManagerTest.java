package social.wall;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagManagerTest {

    @Test
    public void testDeleteTaggedFriendsFromMessageWithTags(){
        String message = "@hi @wathone and i would also like to tag@2 and @d and @";
        List<String> usernames = new ArrayList<>(Arrays.asList("wathone", "br2", "2", "d"));
        
        String result = TagManager.deleteTaggedFriendsFromMessage(message, usernames);

        assertEquals("@hi wathone and i would also like to tag@2 and d and @", result);
    }

    @Test
    public void testDeleteTaggedFriendsFromMessageButNoFriends(){
        String message = "@hi @wathone @d but i have no friends so";
        List<String> usernames = new ArrayList<>();
        
        String result = TagManager.deleteTaggedFriendsFromMessage(message, usernames);

        assertEquals("@hi @wathone @d but i have no friends so", result);
    }

    @Test
    public void testDeleteTaggedFriendsFromMessageWithNoTags(){
        String message = "hi this is a normal message with no tags";
        List<String> usernames = new ArrayList<>(Arrays.asList("wathone", "br2", "2", "d"));
        
        String result = TagManager.deleteTaggedFriendsFromMessage(message, usernames);

        assertEquals("hi this is a normal message with no tags", result);
    }

    @Test
    public void testDeleteTaggedFriendsFromMessageWithUntaggedFriend(){
        String message = "hi";
        List<String> usernames = new ArrayList<>(Arrays.asList("hi", "br2", "2", "d"));
        
        String result = TagManager.deleteTaggedFriendsFromMessage(message, usernames);

        assertEquals("hi", result);
    }
    @Test
    public void testDeleteTaggedFriendsFromMessageWithRepeatAtCorners(){
        String message = "@repeat tagging u again @repeat hello @repeat";
        List<String> usernames = new ArrayList<>(Arrays.asList("repeat"));
        
        String result = TagManager.deleteTaggedFriendsFromMessage(message, usernames);

        assertEquals("repeat tagging u again repeat hello repeat", result);
    }
}