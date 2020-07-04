import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A controller class that sends Admin input to Use Cases
 */
public class AdminController {
    private final TradeModel tradeModel;
    private final AdminPresenter adminPresenter;
    private final BufferedReader br;
    private AdminNotifyManager anm;

    public AdminController(TradeModel tradeModel, AdminPresenter adminPresenter, AdminNotifyManager anm) {
        this.tradeModel = tradeModel;
        this.adminPresenter = adminPresenter;
        this.anm = anm;
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Asks whether the AdminUser wants to continue to see the other menu options or not.
     * If the AdminUser types in "exit", the AdminUser is brought back to the startMenu() and presents the same
     * options again until the AdminUser types "continue."
     * @throws IOException if something goes wrong.
     */
    public void startMenu() throws IOException {
        adminPresenter.startMenu();
        String input = br.readLine();
        if (input.equals("continue")) {
            askAdminForCreateAccountInfo();
        } else if (input.equals("exit")) {
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
     * @param name for the AdminUser name.
     * @param email for the AdminUser email.
     * @param password for the AdminUser password.
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
     * @param email for the AdminUser email.
     * @param password for the AdminUser password.
     */
    private void adminLogin(String email, String password) {
        tradeModel.getUserManager().admin_login(email, password);
    }

    /**
     *
     * @throws IOException if something goes wrong.
     */
    public void askAdminToAddNewAdmin() throws IOException {
        adminPresenter.addNewAdminUser();
        String addInput = br.readLine();
        if (addInput.equals("yes")) {
            addNewAdmins();
        }
    }

    public void addNewAdmins(){
        adminNotifyManager.addOtherAdminMembers();
    }

     public void askAdminToFreezeUsers() throws IOException {
         for (String freeze : adminNotifyManager.notifyToFreezeAccounts()) {
             adminPresenter.freezeAccounts();
             String confirmationInput = br.readLine();
             if (confirmationInput.equals("confirm")) {
                 confirmToFreeze(freeze);
             }
         }
     }

    public void confirmToFreeze(String email) {
        tradeModel.getUserManager().freeze(email);
    }

     public void askAdminToUnfreezeUsers() throws IOException {
         for (String unfreeze : adminNotifyManager.notifyUnfreezeRequests()) {
             adminPresenter.unfreezeAccounts();
             String confirmationInput = br.readLine();
             if (confirmationInput.equals("confirm")) {
                 confirmToUnfreeze(unfreeze);
             }
         }
     }

    public void confirmToUnfreeze(String unfreezeRequests) {
        tradeModel.getUserManager().unfreeze(unfreezeRequests);
    }

     public void askAdminToChangeStatusToAvailable() throws IOException {
       for (Item pendingForApproval : itemManager.getPendingItems())
            adminPresenter.addItemToInventory(); // change this
            String addInput = br.readLine();
            if (addInput.equals("yes")) {
                changeAvailability(pendingForApproval);
        }
     }

     public void changeAvailability(Item item) {
         adminNotifyManager.changeAvailability(item);
     }

     public void askAdminToAddItemsToInventory() throws IOException {

     }

     public void adminAddsItemToInventory() throws IOException {
         for (Item pendingForApproval : itemManager.getPendingItems())
             adminPresenter.addItemToInventory(); // change this
             String addInput = br.readLine();
             if (addInput.equals("yes")) {
                 addItemToInventory(email, item);
        }
     }

    public void addItemToInventory(String email, Item item) {
        tradeModel.getUserManager().addToInventory(email, item);
    }

    public void askAdminToSetLendingThreshold() throws IOException {
        adminPresenter.setThreshold();
        String thresholdInput = br.readLine();
        int threshold = Integer.parseInt(thresholdInput);
        um.setThreshold(threshold);
    }

}





