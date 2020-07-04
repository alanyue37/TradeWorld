import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * A controller class that sends Admin input to Use Cases
 */
public class AdminController {
    private final TradeModel tradeModel;
    private AdminPresenter adminPresenter;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Checks whether the AdminUser wants to continue to see the other menu options or not.
     */
    public void startMenu() {
        adminPresenter.startMenu();
        try {
            String input = br.readLine();
            if (input.equals("continue")) {
                askAdminForCreateAccountInfo();
            }
        } catch (IOException e) {
            System.out.println("Oops, Try Again!");
        }
    }

    public void askAdminForCreateAccountInfo() {
        adminPresenter.createAccount();
        try {
            String input = br.readLine();
            if (input.equals("create an account")) {
                createAdmin(input, input, input);
            } else if (input.equals("login")) {
                adminLogin(input, input);
            }
        } catch (IOException e) {
            System.out.println("Oops, Try Again!");
        }
    }


















    /**
     * @param name for createAccount
     * @param email for createAccount
     * @param password for createAccount
     */
    private void createAdmin(String name, String email, String password) {
        tradeModel.getUserManager().createAdminUser(name, email, password);
    }

    /**
     * @param email for adminLogin
     * @param password for adminLogin
     */
    public void adminLogin(String email, String password) {
        tradeModel.getUserManager().admin_login(email, password);
    }


    /**
     * @param tradeModel for AdminController
     */

    public AdminController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
    }

    /**
     * @param email for addItem
     * @param item for addItem
     */
    public void addItemToInventory(String email, Item item) {
        tradeModel.getUserManager().addToInventory(email, item);
    }

    public void addItemToWishlist() {
        tradeModel.getUserManager().
    }

    /**
     * @param email for confirmToFreeze
     */
    public void confirmToFreeze(String email) {
        if (tradeModel.getTradeManager().isfrozen == false) {
            tradeModel.getUserManager().freeze(email);
        }
    }

    /**
     * @param unfreezeRequests for confirmToUnfreeze
     */
    public void confirmToUnfreeze(HashSet<String> unfreezeRequests) {
        for (String user:  tradeModel.getUserManager().getUnfreezeRequests(unfreezeRequests)) {
            tradeModel.getUserManager().unfreeze(user);
        }
    }

    /**
     * @param threshold for changeThresholds
     */
    public void changeThresholds(int threshold) {
        tradeModel.getUserManager().setThreshold(threshold);
    }

    /**
     *
     */
    public void updateView() {

    }
}




