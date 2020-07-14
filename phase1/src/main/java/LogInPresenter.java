import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Presenter for logging in.
 * Structure of some methods copied from ReadWriteEx.
 */
public class LogInPresenter implements Iterator<String> {

    private final List<String> prompts = new ArrayList<>();
    private int current = 0;

    /**
     * Creates a LogInPresenter.
     */
    public LogInPresenter() {}

    /**
     * Checks for subsequent prompts.
     * @return true if there is prompt that has not yet been returned.
     */
    @Override
    public boolean hasNext() {
        return current < prompts.size();
    }

    /**
     * Returns the next prompt to be printed.
     * @return the next prompt.
     */
    @Override
    public String next() {
        String res;

        // List.get(i) throws an IndexOutBoundsException if
        // we call it with i >= properties.size().
        // But Iterator's next() needs to throw a
        // NoSuchElementException if there are no more elements.
        try {
            res = prompts.get(current);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
        current += 1;
        return res;
    }

    /**
     * Prints the next prompt to the screen.
     */
    public void nextLine() {
        System.out.println(next());
    }

    /**
     * Adds the prompt for the start menu.
     */
    public void startMenu() {
        prompts.add("Welcome to the Trading System!\n" +
                "- Enter 1 to log in as a trading user\n" + "" +
                "- Enter 2 to log in as an admin\n" +
                "- Enter 3 to create a new account\n" +
                "- Enter \"exit\" to exit");
    }

    /**
     * Adds the prompt for the trying again in the start menu.
     */
    public void menuTryAgain() {
        prompts.add("Invalid input\n" + "" +
                "Please try again\n");
    }

    /**
     * Adds the prompts for logging in.
     */
    public void logIn() {
        prompts.add("Enter your username");
        prompts.add("Enter your password");
    }

    /**
     * Adds the prompt for an invalid account.
     */
    public void invalidAccount() {
        prompts.add("Invalid username or password\n" +
                "Please try again\n");
    }

    /**
     * Adds the prompts for creating a new account.
     */
    public void newAccount() {
        prompts.add("Enter your name");
        prompts.add("Enter your username");
        prompts.add("Enter your password");
    }

    /**
     * Adds the prompt for a taken username.
     */
    public void usernameTaken(String username) {
        prompts.add("Username " + username + " is taken\n" +
                "Please try again\n");
    }

    /**
     * Adds the welcome prompt after successful login
     */
    public void welcome(String username) {
        prompts.add("Welcome " + username + ".\n");
    }

}
