import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A controller class that sends Admin input to Use Cases
 */
public class AdminController {
    private final TradeModel tradeModel;
    private AdminPresenter adminPresenter;
    private BufferedReader br;
    private AdminNotifyManager adminNotifyManager;
    private ItemManager itemManager;
    private UserManager um;

    public AdminController(TradeModel tradeModel, AdminPresenter adminPresenter, AdminNotifyManager adminNotifyManager, ItemManager im, UserManager um) {
        this.tradeModel = tradeModel;
        this.adminPresenter = adminPresenter;
        this.adminNotifyManager = adminNotifyManager;
        this.itemManager = im;
        this.um = um;
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }
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
            System.out.println("Something went wrong!");
        }
    }

    public void chooseLoginOptions() throws IOException {
        adminPresenter.chooseCreateAccountOrLogin();
        String choiceInput = br.readLine();
        if (choiceInput.equals("create an account")) {
            askAdminForCreateAccountInfo();
        } askAdminLoginInfo();
    }

    public void askAdminForCreateAccountInfo() throws IOException {
        adminPresenter.accountEnterName();
        String nameInput = br.readLine();
        adminPresenter.accountEnterEmail();
        String emailInput = br.readLine();
        adminPresenter.accountEnterPassword();
        String passwordInput = br.readLine();
        createAdmin(nameInput, emailInput, passwordInput);
    }

    private void createAdmin(String name, String email, String password) {
        tradeModel.getUserManager().createAdminUser(name, email, password);
    }

    public void askAdminLoginInfo() throws IOException {
        adminPresenter.accountEnterEmail();
        String emailInput = br.readLine();
        adminPresenter.accountEnterPassword();
        String passwordInput = br.readLine();
        adminLogin(emailInput, passwordInput);
    }

    public void adminLogin(String email, String password) {
        tradeModel.getUserManager().admin_login(email, password);
    }

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
        if (tradeModel.getTradeManager().isfrozen == false) {
            tradeModel.getUserManager().freeze(email);
        }
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
        for (String user:  tradeModel.getUserManager().getUnfreezeRequests()) {
            tradeModel.getUserManager().unfreeze(user);
        }
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





