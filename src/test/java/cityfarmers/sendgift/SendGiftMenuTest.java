package cityfarmers.sendgift;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daos.CropDAO;
import daos.FriendRequestDAO;
import daos.GiftDAO;
import daos.UserDAO;
import entities.Crop;
import entities.User;

public class SendGiftMenuTest {
    private SendGiftMenu sendGiftMenu;
    private UserDAO UDM;
    private User user1;
    private User user2;
    private User user3;
    private User user4;

    @BeforeEach
    public void setUpMenuAndUser() {
        UDM = new UserDAO();
        FriendRequestDAO FRDM = new FriendRequestDAO();

        user1 = new User("user1", "test name1");
        UDM.addUser(user1, "password");

        user2 = new User("user2", "test name2");
        UDM.addUser(user2, "password");

        user3 = new User("user3", "test name3");
        UDM.addUser(user3, "password");

        user4 = new User("user4", "test name4");
        UDM.addUser(user4, "password");

        // user1 is friends with user2 and user4. user2 is friends with user3
        FRDM.acceptFriendRequest(user1, user2);
        FRDM.acceptFriendRequest(user1, user4);
        FRDM.acceptFriendRequest(user2, user3);

        this.sendGiftMenu = new SendGiftMenu(user1);
    }

    @AfterEach
    public void tearDownMenuAndUser() {
        UDM.delete(user4);
        UDM.delete(user3);
        UDM.delete(user2);
        UDM.delete(user1);
    }


    @Test
    public void checkGiftToSelftest(){
        List<String> usernames = new ArrayList<>();
        usernames.add("user1");

        System.out.println("== Test: Check Gift Send to Self ==");
        assertFalse(sendGiftMenu.checkValidCandidates(usernames, user1));
    }

    @Test
    public void checkGiftToNobodyTest(){
        List<String> usernames = new ArrayList<>();
        
        System.out.println("== Test: Check Gift Send to Nobody ==");
        assertFalse(sendGiftMenu.checkValidCandidates(usernames, user1));
    }

    @Test
    public void checkGiftToInvalidUser(){
        List<String> usernames = new ArrayList<>();
        usernames.add("asf;ljk234jl");

        System.out.println("== Test: Check Gift Send to Invalid Username ==");
        assertFalse(sendGiftMenu.checkValidCandidates(usernames, user1));
    }

    @Test
    public void checkGiftToNonFriend(){
        List<String> usernames = new ArrayList<>();
        usernames.add("user3");

        System.out.println("== Test: Check Gift Send to Non-Friend Username ==");
        assertFalse(sendGiftMenu.checkValidCandidates(usernames, user1));
    }

    @Test
    public void checkGiftToFriend(){
        List<String> usernames = new ArrayList<>();
        usernames.add("user2");

        System.out.println("== Test: Check Gift Send to Friend ==");
        assertTrue(sendGiftMenu.checkValidCandidates(usernames, user1));
    }

    @Test
    public void checkGiftToDuplicateFriends(){
        List<String> usernames = new ArrayList<>();
        usernames.add("user2");
        usernames.add("user2");

        System.out.println("== Test: Check Gift Send to Same Friend Multiple Times ==");
        assertFalse(sendGiftMenu.checkValidCandidates(usernames, user1));
    }

    @Test
    public void checkGiftToFriendTwiceToday(){
        GiftDAO GFM = new GiftDAO();
        CropDAO CFM = new CropDAO();

        Crop crop1 = CFM.getCrop("Papaya");
        GFM.sendGift(user1, user2, crop1);

        List<String> usernames = new ArrayList<>();
        usernames.add("user2");

        System.out.println("== Test: Check Gift Send to Friend who have already received today ==");
        assertFalse(sendGiftMenu.checkValidCandidates(usernames, user1));
    }

    @Test
    public void checkGiftToMultipleWithInvalid(){
        List<String> usernames = new ArrayList<>();
        usernames.add("user2");
        usernames.add("user3");
        usernames.add("user4");

        System.out.println("== Test: Check Gift Send to Some Friends and Nonfriends ==");
        assertFalse(sendGiftMenu.checkValidCandidates(usernames, user1));
    }

    @Test
    public void checkGiftToMultipleWithValid(){
        List<String> usernames = new ArrayList<>();
        usernames.add("user2");
        usernames.add("user4");

        System.out.println("== Test: Check Gift Send to Some Friends ==");
        assertTrue(sendGiftMenu.checkValidCandidates(usernames, user1));
    }
}