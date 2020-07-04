import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * A controller class that sends Admin input to Use Cases
 */
public class AdminController {
    private final TradeModel tradeModel;
    private final AdminPresenter adminPresenter;
    private final BufferedReader br;
    private final  AdminNotifyManager anm;

    public AdminController(TradeModel tradeModel, AdminPresenter adminPresenter, AdminNotifyManager anm) {
        this.tradeModel = tradeModel;
        this.adminPresenter = adminPresenter;
        this.anm = anm;
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Asks whether the AdminUser wants to continue to see the other menu options or not.
     * If the AdminUser types in "exit", the screen presents a message and then the AdminUser is brought back to the
     * startMenu() and presents the same options again until the AdminUser types "continue."
     * @throws IOException if something goes wrong.
     */
    public void startMenu() throws IOException {
        adminPresenter.startMenu();
        String input = br.readLine();
        if (input.equals("continue")) {
            askAdminForCreateAccountInfo();
        } else if (input.equals("exit")) {
            adminPresenter.end();
            adminPresenter.startMenu();
        }
    }

    /**
     * Asks the AdminUser to type in "continue" or "login."
     * @throws IOException if something goes wrong.
     */
    public void chooseLoginOptions() throws IOException {
        adminPresenter.chooseCreateAccountOrLogin();
        String choiceInput = br.readLine();
        if (choiceInput.equals("create an account")) {
            askAdminForCreateAccountInfo();
        } else if (choiceInput.equals("log in")) {
            askAdminLoginInfo();
        }
    }

    /**
     * A helper function for chooseLoginOptions().
     * Asks the AdminUser to type in their name, email, and password to create an account.
     * @throws IOException if something goes wrong.
     */
    private void askAdminForCreateAccountInfo() throws IOException {
        adminPresenter.accountEnterName();
        String nameInput = br.readLine();
        adminPresenter.accountEnterEmail();
        String emailInput = br.readLine();
        adminPresenter.accountEnterPassword();
        String passwordInput = br.readLine();
        createAdmin(nameInput, emailInput, passwordInput);
    }

    /**
     * A helper function for askAdminForCreateAccountInfo().
     * @param name is the AdminUser name.
     * @param email is the AdminUser email.
     * @param password is the AdminUser password.
     */
    private void createAdmin(String name, String email, String password) {
        tradeModel.getUserManager().createAdminUser(name, email, password);
    }

    /**
     * A helper function for chooseLoginOptions().
     * Asks the AdminUser to type in their email and password if they already have an existing account.
     * @throws IOException if something goes wrong.
     */
    public void askAdminLoginInfo() throws IOException {
        adminPresenter.accountEnterEmail();
        String emailInput = br.readLine();
        adminPresenter.accountEnterPassword();
        String passwordInput = br.readLine();
        adminLogin(emailInput, passwordInput);
    }

    /**
     * A helper function for askAdminLoginInfo().
     * @param email is the AdminUser email.
     * @param password is the AdminUser password.
     */
    private void adminLogin(String email, String password) {
        tradeModel.getUserManager().admin_login(email, password);
    }

    /**
     * Asks the admin to add subsequent admins to the system.
     * @throws IOException if something goes wrong.
     */
    public void askAdminToAddNewAdmin() throws IOException {
        for (String adminUsersToBeAdded : anm.getAdminUsersRequests().keySet()){
            adminPresenter.addNewAdminUser(adminUsersToBeAdded);
            String addInput = br.readLine();
            if (addInput.equals("yes")) {
                addNewAdmins(adminUsersToBeAdded, anm.getAdminUsersRequests().get(adminUsersToBeAdded));
            }
        }
    }

    /**
     * A helper function for askAdminToAddNewAdmin().
     * @param newAdminUserEmail is the email of the new admin.
     * @param newAdminUser is the AdminUser itself.
     */
    private void addNewAdmins(String newAdminUserEmail, User newAdminUser){
        tradeModel.getUserManager().addNewAdminUsers(newAdminUserEmail, newAdminUser);
    }

    /**
     * Asks the admin to freeze the accounts of the user.
     * @throws IOException if something goes wrong.
     */
     public void askAdminToFreezeUsers() throws IOException {
         for (String freeze : tradeModel.getUserManager().getFreezeAccounts()) {
             adminPresenter.freezeAccounts(freeze);
             String confirmationInput = br.readLine();
             if (confirmationInput.equals("confirm")) {
                 confirmToFreeze(freeze);
             }
         }
     }

    /**
     * A helper function for askAdminToFreezeUsers().
     * @param email is the email of the account that is to be frozen by the AdminUser.
     */
    private void confirmToFreeze(String email) {
        tradeModel.getUserManager().freeze(email);
    }

    /**
     * Asks the admin to unfreeze the accounts of the user.
     * @throws IOException if something goes wrong.
     */
     public void askAdminToUnfreezeUsers() throws IOException {
         for (String unfreeze : tradeModel.getUserManager().getUnfreezeRequests()) {
             adminPresenter.unfreezeAccounts(unfreeze);
             String confirmationInput = br.readLine();
             if (confirmationInput.equals("confirm")) {
                 confirmToUnfreeze(unfreeze);
             }
         }
     }

    /**
     * A helper function for askAdminToFreezeUsers().
     * @param  unfreezeRequests the email of the account that is to be unfrozen by the AdminUser.
     */
    public void confirmToUnfreeze(String unfreezeRequests) {
        tradeModel.getUserManager().unfreeze(unfreezeRequests);
    }

    /**
     * Asks the Admin to confirm whether this item should be added to the system or not.
     * @throws IOException if something goes wrong.
     */
    public void askAdminToChangeStatusToAvailable() throws IOException {
        for (Item pendingForApproval : tradeModel.getItemManager().getPendingItems()) {
            adminPresenter.addItemToInventory(pendingForApproval.toString());
            String addInput = br.readLine();
            if (addInput.equals("yes")) {
                changeAvailability(pendingForApproval);
            }
        }
    }

    /**
     * A helper function for askAdminToChangeStatusToAvailable().
     * @param item is the item that has its availability status changed from false to true.
     */
     public void changeAvailability(Item item) {
         anm.changeAvailability(item);
     }

    /**
     * When users request an AdminUser to add an item to their available list of items, the AdminUser must review
     * the item and puts the item in the inventory of the user.
     * @throws IOException if something goes wrong.
     */
    public void askAdminToAddItemToInventory() throws IOException {
        ArrayList<String> str = new ArrayList<>(anm.getRequestItemToBeAdded().keySet());
        int i = 0;
        for (Item toBeAdded : anm.getRequestItemToBeAdded().values()) {
            adminPresenter.addItemToInventory(toBeAdded.toString());
            String addInput = br.readLine();
            if (addInput.equals("yes")) {
                addItemToInventory(str.get(i), toBeAdded);
                ++i;
            } else if (addInput.equals("no")) {
                ++i;
            }
        }
    }

    /**
     * A helper function for askAdminToAddItemToInventory().
     * @param email is the email of the TradingUser.
     * @param item is the item that the TradingUser requests to be added to their inventory.
     */
    public void addItemToInventory(String email, Item item) {
        tradeModel.getUserManager().addToInventory(email, item);
    }

    /**
     * The admin can change the lending threshold that is, how much does the user have to lend than they have borrowed
     * in order to make a non-lending transaction.
     * @throws IOException if something goes wrong.
     */
    public void askAdminToSetLendingThreshold() throws IOException {
        adminPresenter.setThreshold();
        String thresholdInput = br.readLine();
        int threshold = Integer.parseInt(thresholdInput);
        tradeModel.getUserManager().setThreshold(threshold);
    }
}





