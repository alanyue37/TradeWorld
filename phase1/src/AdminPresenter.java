import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * AdminPresenter prompts users for inputs
 */
public class AdminPresenter implements Iterator<String> {
    private List<String> prompts = new ArrayList<>();
    private int curr = 0;

    public AdminPresenter() {
        prompts.add("Type 'create an account' to create an AdminAccount or 'Login' to login to an AdminAccount: ");
        prompts.add("Enter Name: ");
        prompts.add("Enter Email: ");
        prompts.add("Enter Password: ");
        prompts.add("Add New Admin User(s): ");
        prompts.add("Freeze Accounts: ");
        prompts.add("Requests for Unfreezing Accounts: ");
        prompts.add("Add this item to this User's Inventory?: ");
        prompts.add("Add this item to this User's Wishlist?: ");
        prompts.add("Add item for advertisement?: ");
        prompts.add("Set Threshold for lending: ");
    }

    /**
     * A getter for the input.
     */
    public String getterInput() {
        return prompts.get(curr);
    }

    /**
     * Puts following prompt strings in the prompts list
     * Citation of code: based on the class studentPropertiesIterator.java (week 6)
     */
    @Override
    public boolean hasNext() {
        return curr < prompts.size();
    }

    /**
     * @return the next prompt to print
     * Citation of code: based on the class studentPropertiesIterator.java (week 6)
     */
    @Override
    public String next() {
        String input;
        try {
            input = prompts.get(curr);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        } curr += 1;
        return input;
    }

    /**
     * Removes the prompt from the screen
     * Citation of code: based on the class studentPropertiesIterator.java (week 6)
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not Supported.");
    }
}

