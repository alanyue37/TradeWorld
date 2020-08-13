package adminadapters;

import tradegateway.TradeModel;
import undocomponent.NoLongerUndoableException;
import undocomponent.UndoAddInventoryItem;
import undocomponent.UndoableOperation;

import java.io.IOException;
import java.util.*;

/**
 * A controller class that sends admin input to Use Cases and calls methods in the Use Case classes.
 */
public class AdminController {
    private final TradeModel tradeModel;
    // private final String username;

    public AdminController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
        // this.username = username;
    }

    /**
     * This method overrides run() method in the trademisc.RunnableController interface. It shows the menu options
     * to the admin user and might catch an IOException if something goes wrong when showing the menu options.
     */
    /*@Override
    public void run() {
        try {
            boolean active = selectMenu();
            while (active) {
                active = selectMenu();
            }
        } catch (IOException e) {
            System.out.println("Oops, something bad happened!");
        }
    }*/

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
    /*public boolean selectMenu() throws IOException {
        presenter.startMenu(username);
        boolean validInput = false;
        do {*/
           /* String input = br.readLine();
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
    }*/

    /**
     * This method allows an admin user to add another, subsequent admin user.
     * It asks the admin user to type in their name, email, and password to create an account for the subsequent
     * admin user. After adding the subsequent admin user to admin users in UserManager, the admin is presented with
     * the same menu options again.
     */
    protected boolean askAdminToAddNewAdmin(String name, String username, String password) {
        return tradeModel.getUserManager().createAdminUser(name, username, password);
    }

    /**
     * This method allows an admin user to freeze accounts of trading users who have reached the limits and thresholds.
     * After freezing the account(s), the admin is presented with the same menu options again.
     */
    protected void askAdminToFreezeUsers(List<String> accountsSelected) {
        // Set<String> flaggedAccounts = new HashSet<>();
        // List<String> incompleteUsers = tradeModel.getMeetingManager().getTradesIncompleteMeetings(tradeModel.getTradeManager().getAllTypeTrades("ongoing"));
        // flaggedAccounts.addAll(tradeModel.getTradeManager().getExceedIncompleteLimitUser(incompleteUsers));
        // flaggedAccounts.addAll(getUsersExceedWeekly());
        // flaggedAccounts.addAll(tradeModel.getUserManager().getUsersForFreezing());
        // boolean empty = flaggedAccounts.isEmpty();
        // presenter.freezeAccountsHeading(empty);

        for (String user : accountsSelected) {
            //while (!confirmationInput.equals("0") && !confirmationInput.equals("1")) {
            // presenter.invalidInput();
            // confirmationInput = br.readLine();
            //}
            tradeModel.getUserManager().setFrozen(user, true);
        }
    }

    /**
     * Get the list of usernames who've surpassed the weekly limit of trades in the past week
     *
     * @return list of usernames who've surpassed the weekly limit of trades in the past week
     */
    protected Set<String> getUsersExceedWeekly() {
        int limit = tradeModel.getTradeManager().getLimitTransactionPerWeek();
        HashSet<String> weeklyExceedUsers = new HashSet<>();
        List<String> tradesPastWeek = tradeModel.getMeetingManager().getMeetingsPastDays(tradeModel.getTradeManager()
                .getAllTypeTrades("completed"));

        Map<String, Integer> usersPastWeek = tradeModel.getTradeManager().userToNumTradesInvolved(tradesPastWeek);
        for (String username : usersPastWeek.keySet()) {
            if (usersPastWeek.get(username) > limit + 2) {
                weeklyExceedUsers.add(username);
            } else if (tradeModel.getUserManager().getRankByUsername(username).equals("bronze") &&
                    usersPastWeek.get(username) > limit) {
                weeklyExceedUsers.add(username);
            }
        }
        return weeklyExceedUsers;
    }

    /**
     * This method allows an admin user to unfreeze accounts of trading users who have requested to unfreeze.
     * After unfreezing the account(s), the admin is presented with the same menu options again.
     */
    protected void askAdminToUnfreezeUsers(List<String> accountsSelected) {
        // Set<String> accounts = tradeModel.getUserManager().getUnfreezeRequests();
        // boolean empty = accounts.isEmpty();
        // presenter.unfreezeAccountsHeading(empty);

        for (String user : accountsSelected) {
            // presenter.unfreezeAccounts(user);
            // String confirmationInput = br.readLine();
            // while (!confirmationInput.equals("0") && !confirmationInput.equals("1")) {
            //  presenter.invalidInput();
            //  confirmationInput = br.readLine();
            tradeModel.getUserManager().setFrozen(user, false);
        }
    }

    /**
     * This method allows an admin user to look at the item and check whether this item should be added to the system
     * or not. This is a way to prevent the user from selling items that are possible to sell such as intangible items.
     */
    protected void askAdminToReviewItems(List<String> itemsSelected, List<String> notItemsSelected) throws NoLongerUndoableException {
        // Set<String> items = tradeModel.getItemManager().getItemsByStage("pending");
        // boolean empty = items.isEmpty();
        // presenter.reviewItemsHeading(empty);

        if (!itemsSelected.isEmpty()) {
            for (String itemId : itemsSelected) {
                //String itemInfo = tradeModel.getItemManager().getItemInfo(itemId);
                //presenter.reviewItem(itemInfo);
                // String input = br.readLine();
                // while (!input.equals("0") && !input.equals("1")) {
                // presenter.invalidInput();
                // input = br.readLine();
                tradeModel.getItemManager().confirmItem(itemId);
                UndoableOperation undoableOperation = new UndoAddInventoryItem(tradeModel.getItemManager(), tradeModel.getTradeManager(), itemId);
                tradeModel.getUndoManager().add(undoableOperation);
            }
        }
        if (!notItemsSelected.isEmpty()) {
            for (String itemId : notItemsSelected) {
                tradeModel.getItemManager().deleteItem(itemId);
            }
        }
    }

    /**
     * This method allows an admin user to change the lending threshold, that is, how much the user lends than borrow
     * in order to make a non-lending transaction. It prompts the Admin user to enter a number that is an integer
     * greater than or equal to zero.
     */
    protected void askAdminToSetLendingThreshold(String lendingLimit) {
        // presenter.lendingThreshold(tradeModel.getUserManager().getThreshold("trading"));
        // String thresholdInput = br.readLine();
        // while (notAnIntegerOrZero(thresholdInput)) {
        // presenter.notAnIntegerOrMin();
        // presenter.lendingThreshold(tradeModel.getUserManager().getThreshold("trading"));
        // thresholdInput = br.readLine();
        // }
        int lendingThreshold = Integer.parseInt(lendingLimit);
        tradeModel.getUserManager().setThreshold("trading", lendingThreshold);
    }

    /**
     * This method allows an admin user to set a limit for the number of transactions that a user can conduct
     * in one week. It prompts the Admin user to enter a number that is an integer greater than
     * or equal to one.
     */
    protected void askAdminToSetLimitOfTransactions(String transactionsLimit) {
        // presenter.limitOfTransactions(tradeModel.getTradeManager().getLimitTransactionPerWeek());
        // String thresholdInput = br.readLine();
        // while (notAnIntegerOrOne(thresholdInput)) {
        //  presenter.notAnIntegerOrMin();
        // presenter.limitOfTransactions(tradeModel.getTradeManager().getLimitTransactionPerWeek());
        //thresholdInput = br.readLine();
        // }
        int transactionsThreshold = Integer.parseInt(transactionsLimit);
        tradeModel.getTradeManager().changeLimitTransactionPerWeek(transactionsThreshold);
    }

    /**
     * This method allows an admin user to set a limit on how many transactions remain incomplete before the account
     * is frozen. It prompts the Admin user to enter a number that is an integer greater than or equal to one.
     */
    protected void askAdminToSetLimitOfIncompleteTrades(String tradeLimit) {
        // presenter.limitOfIncompleteTransactions(tradeModel.getTradeManager().getLimitIncomplete());
        // String thresholdInput = br.readLine();
        // while (notAnIntegerOrOne(thresholdInput)) {
        // presenter.notAnIntegerOrMin();
        // presenter.limitOfIncompleteTransactions(tradeModel.getTradeManager().getLimitIncomplete());
        // thresholdInput = br.readLine();
        int tradeThreshold = Integer.parseInt(tradeLimit);
        tradeModel.getTradeManager().changeLimitIncomplete(tradeThreshold);
    }

    /**
     * This method allows an admin user to set a limit on edits that the user can make to change the
     * meeting place and time. It prompts the Admin user to enter a number that is an integer greater than
     * or equal to zero.
     */
    public void askAdminToSetLimitOfEdits(String editLimit) {
        // presenter.limitOfEdits(tradeModel.getMeetingManager().getLimitEdits());
        // String thresholdInput = br.readLine();
        // while (notAnIntegerOrZero(thresholdInput)) {
        //  presenter.notAnIntegerOrMin();
        // presenter.limitOfEdits(tradeModel.getMeetingManager().getLimitEdits());
        // thresholdInput = br.readLine();
        int editThreshold = Integer.parseInt(editLimit);
        tradeModel.getMeetingManager().changeLimitEdits(editThreshold);
    }

    /**
     * A helper method that returns true iff the input is an integer or and the input is at least 0
     * to prompt the Admin user to re-enter a valid input. Returns false when the input is not valid.
     *
     * @param adminInput The input from the Admin user.
     * @return True iff the input is an integer and the input is at least 0.
     */
    protected boolean IsAnIntegerOrZero(String adminInput) {
        try {
            int isInt = Integer.parseInt(adminInput);
            return isInt >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * A helper method that returns true iff the input is an integer or and the input is at least 1 to prompt the Admin
     * user to re-enter a valid input. Returns false when the input is not valid.
     *
     * @param adminInput The input from the Admin user.
     * @return True iff the input is an integer and the input is at least 1.
     */
    protected boolean IsAnIntegerOrOne(String adminInput) {
        try {
            int isInt = Integer.parseInt(adminInput);
            return isInt >= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * This method allows an admin user to set a value for the gold limit.
     * @param goldLimit The goldLimit input from the Admin user.
     */
    protected void setGoldThreshold(String goldLimit) {
        int goldThreshold = Integer.parseInt(goldLimit);
        tradeModel.getUserManager().setThreshold("gold", goldThreshold);
    }

    /**
     * This method allows an admin user to set a value for the silver limit.
     * @param silverLimit The silver limit input from the Admin user.
     */
    protected void setSilverThreshold(String silverLimit) {
        int silverThreshold = Integer.parseInt(silverLimit);
        tradeModel.getUserManager().setThreshold("silver", silverThreshold);
    }

    /**
     * This method provides an admin user all the undo operations (i.e., undo add inventory item, undo add proposed
     * trade, undo add review, and undo add wishlist item).
     * @throws NoLongerUndoableException If the undo no longer exists.
     */
    protected void undoOperations(List<String> undoActions) throws NoLongerUndoableException {
        for (String undoID : undoActions) {
            tradeModel.getUndoManager().execute(undoID);
        }
    }

    /**
     * This method returns a list of strings representing the undo operation and what needs to be undone.
     * @return A list of strings representing the type of undo operations (i.e., undo add inventory item, undo add proposed
     * trade, undo add review, and undo add wishlist item) and what needs to be reversed.
     */
    protected List<String> undoOperationsString() {
        Collection<UndoableOperation> undoOperations = tradeModel.getUndoManager().getUndoableOperations().values();
        ArrayList<String> undoOperationsString = new ArrayList<>();

        for (UndoableOperation undoableOperation : undoOperations) {
            undoOperationsString.add(undoableOperation.toString());
        } return undoOperationsString;
    }
}
