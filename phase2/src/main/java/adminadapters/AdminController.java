package adminadapters;

import tradegateway.TradeModel;
import trademisc.RunnableController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A controller class that sends admin input to Use Cases and calls methods in the Use Case classes.
 */
public class AdminController implements RunnableController {
    private final TradeModel tradeModel;
    private final AdminPresenter presenter;
    private final BufferedReader br;
    private final String username;

    public AdminController(TradeModel tradeModel, String username) {
        this.tradeModel = tradeModel;
        this.presenter = new AdminPresenter();
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.username = username;
    }

    /**
     * This method overrides run() method in the trademisc.RunnableController interface. It shows the menu options
     * to the admin user and might catch an IOException if something goes wrong when showing the menu options.
     */
    @Override
    public void run() {
        try {
            boolean active = selectMenu();
            while (active) {
                active = selectMenu();
            }
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
     * @return whether the admin User has selected a menu option
     * @throws IOException If something goes wrong.
     */
    public boolean selectMenu() throws IOException {
        presenter.startMenu(username);
        boolean validInput = false;
        do {
            String input = br.readLine();
            switch (input) {
                case "1":
                    askAdminToAddNewAdmin();
                    validInput = true;
                    break;
                case "2":
                    askAdminToFreezeUsers();
                    validInput = true;
                    break;
                case "3":
                    askAdminToUnfreezeUsers();
                    validInput = true;
                    break;
                case "4":
                    askAdminToReviewItems();
                    validInput = true;
                    break;
                case "5":
                    askAdminToSetLendingThreshold();
                    validInput = true;
                    break;
                case "6":
                    askAdminToSetLimitOfTransactions();
                    validInput = true;
                    break;
                case "7":
                    askAdminToSetLimitOfIncompleteTrades();
                    validInput = true;
                    break;
                case "8":
                    askAdminToSetLimitOfEdits();
                    validInput = true;
                    break;
                case "exit":
                    presenter.end();
                    return false;
                default:
                    presenter.tryAgain();
            }
        } while (!validInput);
        return true;
    }

    /**
     * This method allows an admin user to add another, subsequent admin user.
     * It asks the admin user to type in their name, email, and password to create an account for the subsequent
     * admin user. After adding the subsequent admin user to admin users in UserManager, the admin is presented with
     * the same menu options again.
     *
     * @throws IOException If something goes wrong.
     */
    private void askAdminToAddNewAdmin() throws IOException {
        presenter.accountEnterName();
        String nameInput = br.readLine();
        presenter.accountEnterUsername();
        String usernameInput = br.readLine();
        presenter.accountEnterPassword();
        String passwordInput = br.readLine();
        boolean success = tradeModel.getUserManager().createAdminUser(nameInput, usernameInput, passwordInput);
        if (success) {
            presenter.newAccountCreated(usernameInput);
        } else {
            presenter.usernameTaken(usernameInput);
        }
    }

    /**
     * This method allows an admin user to freeze accounts of trading users who have reached the limits and thresholds.
     * After freezing the account(s), the admin is presented with the same menu options again.
     *
     * @throws IOException If something goes wrong.
     */
    public void askAdminToFreezeUsers() throws IOException {
        Set<String> flaggedAccounts = new HashSet<>();
        List<String> incompleteUsers = tradeModel.getMeetingManager().getTradesIncompleteMeetings(tradeModel.getTradeManager().getAllTypeTrades("ongoing"));
        flaggedAccounts.addAll(tradeModel.getTradeManager().getExceedIncompleteLimitUser(incompleteUsers));
        flaggedAccounts.addAll(getUsersExceedWeekly());
        flaggedAccounts.addAll(tradeModel.getUserManager().getUsersForFreezing());
        boolean empty = flaggedAccounts.isEmpty();
        presenter.freezeAccountsHeading(empty);

        for (String user : flaggedAccounts) {
            presenter.freezeAccounts(user);
            String confirmationInput = br.readLine();
            while (!confirmationInput.equals("0") && !confirmationInput.equals("1")) {
                presenter.invalidInput();
                confirmationInput = br.readLine();
            }
            if (confirmationInput.equals("1")) {
                tradeModel.getUserManager().setFrozen(user, true);
            }
        }
    }

    /**
     * Get the list of usernames who've surpassed the weekly limit of trades in the past week
     *
     * @return list of usernames who've surpassed the weekly limit of trades in the past week
     */
    public Set<String> getUsersExceedWeekly() {
        int limit = tradeModel.getTradeManager().getLimitTransactionPerWeek();
        HashSet<String> weeklyExceedUsers = new HashSet<>();
        List<String> tradesPastWeek = tradeModel.getMeetingManager().getMeetingsPastDays(tradeModel.getTradeManager()
                .getAllTypeTrades("completed"));
        Map<String, Integer> usersPastWeek = tradeModel.getTradeManager().userToNumTradesInvolved(tradesPastWeek);
        for (String username: usersPastWeek.keySet()) {
            if (usersPastWeek.get(username) > limit+2) {
                weeklyExceedUsers.add(username);
            }
            else if (tradeModel.getUserManager().getRankByUsername(username).equals("bronze") &&
                    usersPastWeek.get(username) > limit) {
                weeklyExceedUsers.add(username);
            }
        }
        return weeklyExceedUsers;
    }

    /**
     * This method allows an admin user to unfreeze accounts of trading users who have requested to unfreeze.
     * After unfreezing the account(s), the admin is presented with the same menu options again.
     *
     * @throws IOException If something goes wrong.
     */
    public void askAdminToUnfreezeUsers() throws IOException {
        Set<String> accounts = tradeModel.getUserManager().getUnfreezeRequests();
        boolean empty = accounts.isEmpty();
        presenter.unfreezeAccountsHeading(empty);

        for (String user : accounts) {
            presenter.unfreezeAccounts(user);
            String confirmationInput = br.readLine();
            while (!confirmationInput.equals("0") && !confirmationInput.equals("1")) {
                presenter.invalidInput();
                confirmationInput = br.readLine();
            }
            if (confirmationInput.equals("1")) {
                tradeModel.getUserManager().setFrozen(user, false);
            }
        }
    }

    /**
     * This method allows an admin user to look at the item and check whether this item should be added to the system
     * or not. This is a way to prevent the user from selling items that are possible to sell such as intangible items.
     *
     * @throws IOException If something goes wrong.
     */
    public void askAdminToReviewItems() throws IOException {
        Set<String> items = tradeModel.getItemManager().getItemsByStage("pending");
        boolean empty = items.isEmpty();
        presenter.reviewItemsHeading(empty);

        for (String itemId : items) {
            String itemInfo = tradeModel.getItemManager().getItemInfo(itemId);
            presenter.reviewItem(itemInfo);
            String input = br.readLine();
            while (!input.equals("0") && !input.equals("1")) {
                presenter.invalidInput();
                input = br.readLine();
            }
            if (input.equals("1")) {
                tradeModel.getItemManager().confirmItem(itemId);
            } else {
                tradeModel.getItemManager().deleteItem(itemId);
            }
        }
    }

    /**
     * This method allows an admin user to change the lending threshold, that is, how much the user lends than borrow
     * in order to make a non-lending transaction. It prompts the Admin user to enter a number that is an integer
     * greater than or equal to zero.
     *
     * @throws IOException If something goes wrong.
     */
    public void askAdminToSetLendingThreshold() throws IOException {
        presenter.lendingThreshold(tradeModel.getUserManager().getThreshold("trading"));
        String thresholdInput = br.readLine();
        while (notAnIntegerOrZero(thresholdInput)) {
            presenter.notAnIntegerOrMin();
            presenter.lendingThreshold(tradeModel.getUserManager().getThreshold("trading"));
            thresholdInput = br.readLine();
        }
        int lendingThreshold = Integer.parseInt(thresholdInput);
        tradeModel.getUserManager().setThreshold("trading", lendingThreshold);
        selectMenu();
    }

    /**
     * This method allows an admin user to set a limit for the number of transactions that a user can conduct
     * in one week. It prompts the Admin user to enter a number that is an integer greater than
     * or equal to one.
     *
     * @throws IOException If something goes wrong.
     */
    public void askAdminToSetLimitOfTransactions() throws IOException {
        presenter.limitOfTransactions(tradeModel.getTradeManager().getLimitTransactionPerWeek());
        String thresholdInput = br.readLine();
        while (notAnIntegerOrOne(thresholdInput)) {
            presenter.notAnIntegerOrMin();
            presenter.limitOfTransactions(tradeModel.getTradeManager().getLimitTransactionPerWeek());
            thresholdInput = br.readLine();
        }
        int thresholdTransactions = Integer.parseInt(thresholdInput);
        tradeModel.getTradeManager().changeLimitTransactionPerWeek(thresholdTransactions);
        selectMenu();
    }

    /**
     * This method allows an admin user to set a limit on how many transactions remain incomplete before the account
     * is frozen. It prompts the Admin user to enter a number that is an integer greater than or equal to one.
     *
     * @throws IOException If something goes wrong.
     */
    public void askAdminToSetLimitOfIncompleteTrades() throws IOException {
        presenter.limitOfIncompleteTransactions(tradeModel.getTradeManager().getLimitIncomplete());
        String thresholdInput = br.readLine();
        while (notAnIntegerOrOne(thresholdInput)) {
            presenter.notAnIntegerOrMin();
            presenter.limitOfIncompleteTransactions(tradeModel.getTradeManager().getLimitIncomplete());
            thresholdInput = br.readLine();
        }
        int thresholdIncomplete = Integer.parseInt(thresholdInput);
        tradeModel.getTradeManager().changeLimitIncomplete(thresholdIncomplete);
        selectMenu();
    }

    /**
     * This method allows an admin user to set a limit on edits that the user can make to change the
     * meeting place and time. It prompts the Admin user to enter a number that is an integer greater than
     * or equal to zero.
     *
     * @throws IOException If something goes wrong.
     */
    public void askAdminToSetLimitOfEdits() throws IOException {
        presenter.limitOfEdits(tradeModel.getMeetingManager().getLimitEdits());
        String thresholdInput = br.readLine();
        while (notAnIntegerOrZero(thresholdInput)) {
            presenter.notAnIntegerOrMin();
            presenter.limitOfEdits(tradeModel.getMeetingManager().getLimitEdits());
            thresholdInput = br.readLine();
        }
        int thresholdEdits = Integer.parseInt(thresholdInput);
        tradeModel.getMeetingManager().changeLimitEdits(thresholdEdits);
        selectMenu();
    }

    /**
     * A helper method that returns true iff the input is not an integer or the input is a negative number
     * to prompt the Admin user to re-enter a valid input. Returns false when the input is valid.
     * @param adminInput    The input from the Admin user.
     * @return  True iff the input is not an integer or the input is less than 0.
     */
    private boolean notAnIntegerOrZero(String adminInput) {
        try {
            int isInt = Integer.parseInt(adminInput);
            return isInt < 0;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /**
     * A helper method that returns true iff the input is not an integer or the input is less than 1 to prompt the Admin
     * user to re-enter a valid input. Returns false when the input is valid.
     * @param adminInput    The input from the Admin user.
     * @return  True iff the input is not an integer or the input is less than 1.
     */
    private boolean notAnIntegerOrOne(String adminInput) {
        try {
            int isInt = Integer.parseInt(adminInput);
            return isInt < 1;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
