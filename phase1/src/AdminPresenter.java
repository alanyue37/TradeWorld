import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * AdminPresenter prompts an AdminUser for inputs
 */
public class AdminPresenter implements Iterable<String>{
    private final List<String> prompts = new ArrayList<>();
    private int curr = 0;
    private UserManager um = new UserManager();
    private ItemManager im = new ItemManager();
    private TradeManager tm = new TradeManager();
    private AdminNotifyManager anm = new AdminNotifyManager(um);
    private TradeModel tradeModel = new TradeModel(um, im, tm);

    public AdminPresenter(UserManager um, ItemManager im, TradeManager tm, TradeModel tradeModel) {
        this.um = um;
        this.im = im;
        this.tm = tm;
        this.tradeModel = tradeModel;
    }

    public void startMenu() {
        System.out.println("Welcome! Enter 'continue' to continue or 'exit' to exit");
    }

    public void chooseCreateAccountOrLogin() {
        System.out.println("Type 'create an account' to create an AdminAccount or 'Login' to login to an AdminAccount: ");
    }

    public void createAccount() {
        String[] createAccount = {"Enter Name: ", "Enter Email: ", "Enter Password: "};
        for (int curr = 0; curr < createAccount.length; ++curr)
            System.out.println(createAccount[curr]);
    }

    public void addNewAdminUsers() {
        System.out.println("Add New Admin User(s): ");
    }

    public void freezeAccounts() {
        System.out.println("Freeze Accounts: ");
    }

    public void unfreezeAccounts() {
        System.out.println("Requests for Unfreezing Accounts: ");
    }

    public void addItemToInventory() {
        System.out.println("Add this item to this User's Inventory?: ");
    }

    public void addItemToWishlist() {
        System.out.println("Add this item to this User's Wishlist?: ");
    }

    public void addItemForAD() {
        System.out.println("Add item for advertisement?: ");
    }

    public void setThreshold() {
        System.out.println("Set Threshold for lending: ");
    }








    /**
     * Checks whether there is another prompt after current prompt
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

