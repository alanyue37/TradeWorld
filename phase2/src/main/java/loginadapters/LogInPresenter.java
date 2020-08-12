package loginadapters;

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

    public void usernameTaken(String username) {
        prompts.add("Username " + username + " is already taken.");
    }

    public void invalidAccount() {
        prompts.add("Incorrect username or password.");
    }

    public void initialScreen() {
        prompts.add("Trading System - Login");
        prompts.add("Welcome");
    }

    public void tryAgain() {
        prompts.add("Please try again");
    }

    /**
     * Return a list with the menu options
     */
    public void startMenu() {
        prompts.add("Log in as a trading user");
        prompts.add("Log in as an admin");
        prompts.add("Create a new account");
        prompts.add("Program demo");
    }

    public void logIn(boolean isAdmin) {
        if (isAdmin) {
            prompts.add("Admin login");
        } else {
            prompts.add("Trader login");
        }
        prompts.add("Username:");
        prompts.add("Password:");
        prompts.add("Log In");
    }

    public void createAccount() {
        prompts.add("Create a new account");
        prompts.add("Name:");
        prompts.add("Username:");
        prompts.add("Password:");
        prompts.add("City");
        prompts.add("Register");
    }

    public void backButton() {
        prompts.add("Back");
    }

}
