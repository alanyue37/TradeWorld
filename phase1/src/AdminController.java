import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 */
public class AdminController {

    private TradeModel tradeModel = new TradeModel();

    /**
     * Prompts the AdminUser to enter input
     */


    /**
     * @param tradeModel for AdminController
     */

    public AdminController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
    }

    /**
     * @param name for createAccount
     * @param email for createAccount
     * @param password for createAccount
     */
    public void createAdmin(String name, String email, String password) {
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




