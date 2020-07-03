import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Prompts input from the AdminUser
 */
public class AdminPresenter implements Iterator<String> {
    private List<String> prompts = new ArrayList<>();
    private int curr = 0;


    public AdminPresenter() {
        prompts.add("Email: ");
        prompts.add("Password: ");
        prompts.add("Add Admin User(s): ");
        prompts.add("Freeze Accounts: ");
        prompts.add("Unfreeze Accounts: ");
        prompts.add("Add items to Inventory: ");
        prompts.add("Add items to Wishlist: ");
    }
}
