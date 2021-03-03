
import navigation.WelcomeMenu;

/**
 * The App class launches the Social Magnet app and starts Welcome Page
 *
 * @version 1.0 04 Apr 2020
 * @author Wa Thone
 */
public class App {

    public static void main(String[] args) {
        WelcomeMenu welcomeMenu = new WelcomeMenu();
        welcomeMenu.readOption();
    }
}
