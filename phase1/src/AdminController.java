import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A controller class that sends admin input to Use Cases and calls methods in the Use Case classes.
 */
public class AdminController implements RunnableController {
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
     * This method overrides run() method in the RunnableController interface. It shows the menu options
     * to the admin user and might catch an IOException if something goes wrong when showing the menu options.
     */
    @Override
    public void run() {
        try {
            selectMenu();
        } catch (IOException e) {
            System.out.println("Oops, something bad happened!");
        }
    }

    /**
     * This method prints the menu options and asks the admin user to enter a corresponding number to
     * select a menu option. The number determines which method to call in the admin controller. If the admin user
     * types "exit" at the beginning or any time during the program, the screen prints a message and then the
     * admin user is brought back to selectMenu() and prints the same options again until the admin user enters
     * a corresponding number.
     *
     * @throws IOException if something goes wrong.
     */
    public void selectMenu() throws IOException {
        adminPresenter.startMenu();
        String input = br.readLine();
        switch (input) {
            case "1":
                askAdminToAddNewAdmin();
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
                askAdminToReviewItems();
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
     * This method allows an admin user to add another, subsequent admin user.
     * It asks the admin user to type in their name, email, and password to create an account for the subsequent
     * admin user. After adding the subsequent admin user to admin users in UserManager, the admin is presented with
     * the same menu options again.
     *
     * @throws IOException if something goes wrong.
     */
    private void askAdminToAddNewAdmin() throws IOException {
        adminPresenter.accountEnterName();
        String nameInput = br.readLine();
        adminPresenter.accountEnterUsername();
        String usernameInput = br.readLine();
        adminPresenter.accountEnterPassword();
        String passwordInput = br.readLine();
        tradeModel.getUserManager().createAdminUser(nameInput, usernameInput, passwordInput);
        selectMenu();
    }

    /**
     * This method allows an admin user to freeze accounts of trading users who have reached the limits and thresholds.
     * After freezing the account(s), the admin is presented with the same menu options again.
     *
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
        selectMenu();
    }

    /**
     * This method allows an admin user to unfreeze accounts of trading users who have requested to unfreeze.
     * After unfreezing the account(s), the admin is presented with the same menu options again.
     *
     * @throws IOException if something goes wrong.
     */
    public void askAdminToUnfreezeUsers() throws IOException {
        for (String unfreeze : tradeModel.getUserManager().getUnfreezeRequests()) {
            adminPresenter.unfreezeAccounts(unfreeze);
            String confirmationInput = br.readLine();
            if (confirmationInput.equals("1")) {
                tradeModel.getUserManager().freeze(unfreeze, false);
                selectMenu();
            }
            selectMenu();
        }
    }

    /**
     * This method allows an admin user to look at the item and check whether this item should be added to the system
     * or not. This is a way to prevent the user from selling items that are possible to sell such as intangible items.
     *
     * @throws IOException if something goes wrong.
     */
    public void askAdminToReviewItems() throws IOException {
        for (String pendingForApproval : tradeModel.getItemManager().getPendingItems()) {
            adminPresenter.reviewItem(pendingForApproval);
            String addInput = br.readLine();
            if (addInput.equals("1")) {
                tradeModel.getItemManager().confirmItem(pendingForApproval);
            } else if (addInput.equals("0")) {
                tradeModel.getItemManager().deleteItem(pendingForApproval);
            }
        }
        selectMenu();
    }

    /**
     * This method allows an admin user to change the lending threshold, that is, how much the user lends than borrow
     * in order to make a non-lending transaction.
     *
     * @throws IOException if something goes wrong.
     */
    public void askAdminToSetLendingThreshold() throws IOException {
        adminPresenter.setLendingThreshold();
        String thresholdInput = br.readLine();
        int threshold = Integer.parseInt(thresholdInput);
        tradeModel.getUserManager().setThreshold(threshold);
        selectMenu();
    }

    /**
     * This method allows an admin user to set a limit for the number of transactions that a user can conduct
     * in one week.
     */
    public void askAdminToSetLimitOfTransactions() throws IOException {

    }

    /**
     * This method allows an admin user to set a limit on how many transactions remain incomplete before the account
     * is frozen.
     */
    public void askAdminToSetLimitOfIncompleteTrades() throws IOException {

    }
}
