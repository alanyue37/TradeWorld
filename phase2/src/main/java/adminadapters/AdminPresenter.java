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
     * @param username The username of the currently logged in admin User
     */
    public String startMenu() {
        return "Add new admins, Freeze user accounts, Unfreeze user accounts, Review newly added items, " +
                "Set the lending threshold, Set the limit of number of weekly transactions, " +
                "Set the limit of number of incomplete transactions, Set the limit of number of edits to a meeting";


       // System.out.println("\n*** " + username + " Admin Menu***\n");
        //List<String> options = new ArrayList<>();
        //options.add("Add new admins");
        //options.add("Freeze user accounts");
        //options.add("Unfreeze user accounts");
        //options.add("Review newly added items");
        //options.add("Set the lending threshold");
       // options.add("Set the limit of number of weekly transactions");
        //options.add("Set the limit of number of incomplete transactions");
        //options.add("Set the limit of number of edits to a meeting");
       // printList(options, true, false);
        //System.out.println("\nPlease enter the # of your choice or \"exit\" to exit: ");
    }

    /**
     * This method prompts the admin user to enter the name of another admin user.
     */
    public String accountEnterName() {
        return "Enter name: ";
    }

    /**
     * This method prompts the admin user to enter the username of another admin user.
     */
    public String accountEnterUsername() {
        return "Enter Username: ";
    }

    /**
     * This method prompts the admin user to enter the password of another admin user.
     */
    public String accountEnterPassword() {
        return "Enter password: ";
    }

    /**
     * Prints message that the admin new account has been successfully created.
     * @param username  The username of the Admin User inputs.
     */
    public String newAccountCreated(String username) {
        return "New admin account " + username + " created.\n";
    }

    /**
     * Prints message that the inputted username for new account has been taken.
     * @param username  The username of the Admin User inputs.
     */
    public String usernameTaken(String username) {
        return "Username " + username + " is taken. Please try again.\n";
    }

    /**
     * Prints relevant heading for freeze account view.
     * Prints no accounts to be frozen if empty is true. Otherwise prints instructions.
     * @param selected True iff no accounts have been flagged for freezing.
     */
    public String freezeAccountsHeading(boolean selected) {
        if (selected) {
            return "Selected accounts have been frozen";
                    // "No accounts have been flagged.\n";
        }
        else {
            return "No accounts have been selected to be frozen.";

                    //"The following accounts have exceeded one or more of the system thresholds.\n" +
                           // "For each account enter 1 to freeze or 0 to skip.\n");
        }
    }
    /**
     * This method prompts the admin user to enter 1 to freeze this particular account or 0 to skip.
     * @param user  The account that the admin can freeze.
     */
    public String freezeAccounts(String user) {
        return "This account has reached the limits. Enter 1 to freeze this account or 0 to skip. " +
                "\n Freeze Account for:" + user;  // Delete this method?
    }

    /**
     * Prints relevant heading for unfreeze account view.
     * Prints no accounts to be unfrozen if empty is true. Otherwise prints instructions.
     * @param empty True iff no accounts have requested to be unfrozen.
     */
    public String unfreezeAccountsHeading(boolean selected) {
        if (selected) {
            return "Selected accounts are now unfrozen.";
        }
        else {
            return "No accounts are selected to be unfrozen";
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
    public String reviewItemsHeading(boolean empty) {
        if (empty) {
            return "No items to be reviewed.\n";
        }
        else {
            return "Selected items have been added to the system.";

            // System.out.println("The following items have been recently added and need to be reviewed.\n" +
              //      "For each item enter 1 to add the item or 0 to not add the item.\n");
        }
    }

    /**
     * This method prompts the admin user to review the item and decided whether this item should be added
     * to the system or not.
     * @param item  The item could be added to the system.
     */
    public String reviewItem(String item) {
        return "Selected items have been added to the system and the items not selected are deleted.";

               // "Enter 1 to add the following item or 0 to not add the following item: \n" + item);
    }

    /**
     * This method prompts the admin user to set a threshold for how much a user has to lend than borrow to make
     * a non-lending transaction (i.e., just borrow and not lend).
     */
    public String lendingThreshold() {
        return "How much does the user have to (at least) lend than they borrow in order \nto make a non-lending transaction? \nEnter a whole " +
                "number (minimum 0) for the Lending Threshold: ";
    }

    /**
     * This method prompts the admin user to set a threshold for the number of transaction that a use can make in one
     * week (i.e., 7 days).
     */
    public String limitOfTransactions() {
        return "What is the maximum number of transactions a user can conduct in a week? \nEnter a whole number (minimum 1) for the new limit: ";
    }

    /**
     * This method prompts the admin user to set a threshold for the number of incomplete transactions a user can have
     * before the account gets frozen.
     */
    public String limitOfIncompleteTransactions() {
        return "After how many incomplete transactions should a user be flagged for freezing? \nEnter a whole number (minimum 1) for the new threshold: ";
    }

    /**
     * This method prompts the admin user to set a threshold for the number of edits allowed by the user to change the
     * meeting place and time. The limit of edits should remain 3.
     * @param current   The current threshold set for the number of edits allowed by the user
     */
    public String limitOfEdits() {
        return "What is the maximum number of times the proposed meeting time for a trade can be edited? \nEnter a whole number (minimum 0) for the new limit: ";
    }

    /**
     * This method informs the admin user that the input is not accepted by the program.
     */
    public void invalidInput() {
        System.out.println("This is an invalid input.\n Please try again!");
    }


    public String enterAtLeastZero() {
        return "Not an integer or is a number less than zero. \n Please try again";
    }

    public String enterAtLeastOne() {
        return "Not an integer or is a number less than one. \n Please try again";
    }

    public String confirmationOfThreshold() {
        return "Threshold is set.";
    }

    /**
     * This method prints to the screen and signals to the user that the system has successfully ended.
     */
    public void end() {
        System.out.println("See you soon!");
    }


}
