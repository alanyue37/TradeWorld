import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A controller class that sends Admin input to Use Cases.
 */
public class AdminController {
    private final TradeModel tradeModel;
    private final AdminPresenter adminPresenter;
    private final BufferedReader br;
    private final String username;

    public AdminController(TradeModel tradeModel, AdminPresenter adminPresenter, String username) {
        this.tradeModel = tradeModel;
        this.adminPresenter = adminPresenter;
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.username = username;
    }

    /**
     * Asks whether the AdminUser wants to continue to see the other menu options or not.
     * If the AdminUser types in "exit", the screen presents a message and then the AdminUser is brought back to the
     * startMenu() and presents the same options again until the AdminUser types "continue."
     *
     * @throws IOException if something goes wrong.
     */
    public void startMenu() throws IOException {
        adminPresenter.giveMenuOptions();
        String input = br.readLine();
        switch (input) {
            case "1":
                askAdminForCreateAccountInfo();
            case "2":
                askAdminToAddNewAdmin();
                break;
            case "3":
                askAdminToFreezeUsers();
                break;
            case "4":
                askAdminToUnfreezeUsers();
                break;
            case "5":
                askAdminToChangeStatusToAvailable();
                break;
            case "6":
                askAdminToSetLendingThreshold();
                break;
            case "exit":
                adminPresenter.end();
                adminPresenter.startMenu();
                break;
            default:
                adminPresenter.menuTryAgain();
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
        tradeModel.getUserManager().createAdminUser(nameInput, emailInput, passwordInput);
    }

    /**
     * Asks the admin to add subsequent admins to the system.
     * @throws IOException if something goes wrong.
     */
    public void askAdminToAddNewAdmin() throws IOException {
        for (String adminUsersToBeAdded : anm.getAdminUsersRequests().keySet()) {
            adminPresenter.addNewAdminUser(adminUsersToBeAdded);
            String addInput = br.readLine();
            if (addInput.equals("yes")) {
                tradeModel.getUserManager().addNewAdminUsers(adminUsersToBeAdded, anm.getAdminUsersRequests().get(adminUsersToBeAdded));
            }
        }
    }

    /**
     * Asks the admin to freeze the accounts of the user.
     * @throws IOException if something goes wrong.
     */
    public void askAdminToFreezeUsers() throws IOException {
        for (User freeze : tradeModel.getUserManager().getUsersForFreezing()) {
            adminPresenter.freezeAccounts(freeze.getUsername());
            String confirmationInput = br.readLine();
            if (confirmationInput.equals("1")) {
                tradeModel.getUserManager().freeze(freeze.getUsername(), true);
            }
        }
    }

    /**
     * Asks the admin to unfreeze the accounts of the user.
     * @throws IOException if something goes wrong.
     */
    public void askAdminToUnfreezeUsers() throws IOException {
        for (String unfreeze : tradeModel.getUserManager().getUnfreezeRequests()) {
            adminPresenter.unfreezeAccounts(unfreeze);
            String confirmationInput = br.readLine();
            if (confirmationInput.equals("1")) {
                tradeModel.getUserManager().freeze(unfreeze, false);
            }
        }
    }

    /**
     * Asks the Admin to confirm whether this item should be added to the system or not.
     * @throws IOException if something goes wrong.
     */
    public void askAdminToChangeStatusToAvailable() throws IOException {
        for (Item pendingForApproval : tradeModel.getItemManager().getPendingItems()) {
            adminPresenter.addItemToInventory(pendingForApproval.getId());
            String addInput = br.readLine();
            if (addInput.equals("yes")) {
                tradeModel.getItemManager().confirmItem(pendingForApproval.getId());
            }
        }
    }

    /**
     * When users request an AdminUser to add an item to their available list of items, the AdminUser must review
     * the item and puts the item in the inventory of the user.
     * @throws IOException if something goes wrong.
     */
    public void askAdminToAddItemToInventory(String emailInput) throws IOException {
        int i = 0;
        for (Item toBeAdded : tradeModel.getItemManager().getPendingItems()) {
            adminPresenter.addItemToInventory(toBeAdded.getId());
            String addInput = br.readLine();
            if (addInput.equals("yes")) {
                tradeModel.getUserManager().addToSet(emailInput, toBeAdded.getId(), itemSets.INVENTORY);
                ++i;
            } else if (addInput.equals("no")) {
                ++i;
            }
        }
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