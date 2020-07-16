package adminadapters;

import trademisc.TextPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * A Presenter class that prompts an admin user for inputs.
 */
public class AdminPresenter extends TextPresenter {

    /**
     * This method prints the menu options to the screen and asks the admin user to enter a corresponding number to
     * select a menu option. The number determines which method to call in the admin controller.
     */
    public void startMenu(String username) {
        System.out.println("\n*** " + username + " Admin Menu***\n");
        List<String> options = new ArrayList<>();
        options.add("Add new admins");
        options.add("Freeze user accounts");
        options.add("Unfreeze user accounts");
        options.add("Review newly added items");
        options.add("Set the lending threshold");
        options.add("Set the limit of number of weekly transactions");
        options.add("Set the limit of number of incomplete transactions");
        options.add("Set the limit of number of edits to a meeting");
        printList(options, true, false);

        System.out.println("\nPlease enter the # of your choice or \"exit\" to exit: ");
    }

    /**
     * This method prompts the admin user to enter the name of another admin user.
     */
    public void accountEnterName() {
        System.out.println("Enter name: ");
    }

    /**
     * This method prompts the admin user to enter the username of another admin user.
     */
    public void accountEnterUsername() {
        System.out.println("Enter Username: ");
    }

    /**
     * This method prompts the admin user to enter the password of another admin user.
     */
    public void accountEnterPassword() {
        System.out.println("Enter password: ");
    }

    /**
     * Prints message that the admin new account has been successfully created.
     * @param username  The username of the Admin User inputs.
     */
    public void newAccountCreated(String username) {
        System.out.println("New admin account " + username + " created.\n");
    }

    /**
     * Prints message that the inputted username for new account has been taken.
     * @param username  The username of the Admin User inputs.
     */
    public void usernameTaken(String username) {
        System.out.println("Username " + username + " is taken. Please try again.\n");
    }

    /**
     * Prints relevant heading for freeze account view.
     * Prints no accounts to be frozen if empty is true. Otherwise prints instructions.
     * @param empty True iff no accounts have been flagged for freezing.
     */
    public void freezeAccountsHeading(boolean empty) {
        if (empty) {
            System.out.println("No accounts have been flagged.\n");
        }
        else {
            System.out.println("The following accounts have exceeded one or more of the system thresholds.\n" +
                            "For each account enter 1 to freeze or 0 to skip.\n");
        }
    }
    /**
     * This method prompts the admin user to enter 1 to freeze this particular account or 0 to skip.
     * @param user  The account that the admin can freeze.
     */
    public void freezeAccounts(String user) {
        System.out.println("This account has reached the limits. Enter 1 to freeze this account or 0 to skip. " +
                "\n Freeze Account for:" + user);
    }

    /**
     * Prints relevant heading for unfreeze account view.
     * Prints no accounts to be unfrozen if empty is true. Otherwise prints instructions.
     * @param empty True iff no accounts have requested to be unfrozen.
     */
    public void unfreezeAccountsHeading(boolean empty) {
        if (empty) {
            System.out.println("No users have requested to have their accounts unfrozen.\n");
        }
        else {
            System.out.println("The following accounts have requested to be unfrozen.\n" +
                    "For each account enter 1 to unfreeze or 0 to skip.\n");
        }
    }

    /**
     * This method prompts the admin user to enter 1 to unfreeze this particular account or 0 to skip.
     * @param user  The account that the admin can unfreeze.
     */
    public void unfreezeAccounts(String user) {
        System.out.println("This user has their account frozen and is requesting to unfreeze. " +
                "Enter 1 to unfreeze this account or 0 to skip.\n Unfreeze Account for: " + user);
    }

    /**
     * Prints relevant heading for reviewing added items view.
     * Prints no items to be reviewed if empty is true. Otherwise prints instructions.
     * @param empty True iff no items to be reviewed.
     */
    public void reviewItemsHeading(boolean empty) {
        if (empty) {
            System.out.println("No items to be reviewed.\n");
        }
        else {
            System.out.println("The following items have been recently added and need to be reviewed.\n" +
                    "For each item enter 1 to add the item or 0 to not add the item.\n");
        }
    }

    /**
     * This method prompts the admin user to review the item and decided whether this item should be added
     * to the system or not.
     * @param item  The item could be added to the system.
     */
    public void reviewItem(String item) {
        System.out.println("Enter 1 to add the following item or 0 to not add the following item: \n" + item);
    }

    /**
     * This method prompts the admin user to set a threshold for how much a user has to lend than borrow to make
     * a non-lending transaction (i.e., just borrow and not lend).
     * @param current   The current threshold set for how much the user has to lend than borrow to make a non-lending
     *                  transaction.
     */
    public void lendingThreshold(int current) {
        System.out.println("How many more times (at least) does the user have to lend than they borrow in order to make " +
                "a non-lending transaction?");
        System.out.println("The current Lending Threshold is: " + current);
        System.out.println("Enter a whole number (minimum 0) for the Lending Threshold: ");
    }

    /**
     * This method prompts the admin user to set a threshold for the number of transaction that a use can make in one
     * week (i.e., 7 days).
     * @param current   The current threshold set for the number of transactions a user can conduct in a week.
     */
    public void limitOfTransactions(int current) {
        System.out.println("What is the maximum number of transactions a user can conduct in a week?");
        System.out.println("The current limit is: " + current);
        System.out.println("Enter a whole number (minimum 1) for the new limit: ");
    }

    /**
     * This method prompts the admin user to set a threshold for the number of incomplete transactions a user can have
     * before the account gets frozen.
     * @param current   The current threshold set for incomplete transactions.
     */
    public void limitOfIncompleteTransactions(int current) {
        System.out.println("After how many incomplete transactions should a user be flagged for freezing?");
        System.out.println("The current threshold is: " + current);
        System.out.println("Enter a whole number (minimum 1) for the new threshold: ");
    }

    /**
     * This method prompts the admin user to set a threshold for the number of edits allowed by the user to change the
     * meeting place and time. The limit of edits should remain 3.
     * @param current   The current threshold set for the number of edits allowed by the user
     */
    public void limitOfEdits(int current) {
        System.out.println("What is the maximum number of times the proposed meeting time for a trade can be edited?");
        System.out.println("The current limit is: " + current);
        System.out.println("Enter a whole number (minimum 0) for the new limit: ");
    }

    /**
     * This method informs the admin user that the input is not accepted by the program.
     */
    public void invalidInput() {
        System.out.println("This is an invalid input.\n Please try again!");
    }

    /**
     * This method informs the admin user that the input is not an integer or the number entered is not
     * accepted by the program.
     */
    public void notAnInteger() {
        System.out.println("This is not an integer or it is a number less than the minimum number.\n " +
                "Please try again!");
    }

    /**
     * This method prints to the screen and signals to the user that the system has successfully ended.
     */
    public void end() {
        System.out.println("See you soon!");
    }

}
