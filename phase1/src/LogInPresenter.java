import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Presenter for logging in.
 * Structure of some methods copied from ReadWriteEx
 */
public class LogInPresenter implements Iterator<String> {

    private List<String> prompts = new ArrayList<>();
    private UserManager userManager;
    private int current = 0;

    public LogInPresenter(UserManager um) {
        userManager = um;
    }

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

    public void nextLine() {
        System.out.println(next());
    }

    public void startMenu() {
        prompts.add("Welcome to the Trading System!\n Enter 1 to log in as a trading user\n Enter 2 to log in as an admin\n Enter 3 to create a new account\n Enter \"exit\" to exit");
    }

    public void menuTryAgain() {
        prompts.add("Invalid input\n Please try again");
    }

    public void logIn() {
        prompts.add("Enter your email");
        prompts.add("Enter your password");
    }

    public void invalidAccount() {
        prompts.add("Invalid email or password\n Please try again");
    }

    public void newAccount() {
        prompts.add("Enter your name");
        prompts.add("Enter your email");
        prompts.add("Enter your password");
    }

    public void emailTaken(String email) {
        prompts.add("Email " + email + " is taken\n Please try again");
    }

}
