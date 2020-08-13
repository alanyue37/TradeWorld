package adminadapters;

import trademisc.TextPresenter;
import java.util.ArrayList;
import java.util.List;

/**
 * A Presenter class that prompts an admin user for inputs.
 */
public class AdminPresenter extends TextPresenter {

    /**
     * This method returns a string asking the admin user to enter the name of another admin user.
     * @return Returns a string to enter the new admin name.
     */
    public String accountEnterName() {
        return "Enter Name: ";
    }

    /**
     * This method returns a string asking the admin user to enter the username of another admin user.
     * @return Returns a string to enter the new admin username.
     */
    public String accountEnterUsername() {
        return "Enter Username: ";
    }

    /**
     * This method returns a string asking the admin user to enter the password of another admin user.
     * @return Returns a string to enter the new admin password.
     */
    public String accountEnterPassword() {
        return "Enter Password: ";
    }

    /**
     * This method returns a message that the admin new account has been successfully created.
     * @param username  The username that the Admin user inputs.
     * @return Returns a string to inform that a new Admin has been created.
     */
    public String newAccountCreated(String username) {
        return "New admin account created: " + username;
    }

    /**
     * This method returns a message that the inputted username for the new account has been taken.
     * @param username  The username of the Admin User inputs.
     * @return Returns a string to inform that the username has been taken.
     */
    public String usernameTaken(String username) {
        return "Username " + username + " is taken.";
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
        }
    }
    /**
     * This method prompts the admin user to enter 1 to freeze this particular account or 0 to skip.
     */
    public String freezeAccounts() {
        return "These accounts have reached the limits.\n";
    }

    /**
     * Prints relevant heading for unfreeze account view.
     * Prints no accounts to be unfrozen if empty is true. Otherwise prints instructions.
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
     * @param selected True iff no items to be reviewed.
     */
    public String reviewItemsHeading(boolean selected) {
        if (selected) {
            return "Selected items have been added to the system.";
        }
        else {
            return "No items are added to the system.";
        }
    }

    /**
     * This method prompts the admin user to review the item and decided whether this item should be added
     * to the system or not.
     * @param item  The item could be added to the system.
     */
    public String reviewItem(String item) {
        return "These items are pending to be added. Unselected items will be deleted.\n Hold down shift to add multiple items to the system.";
    }

    /**
     * This method prompts the admin user to set a threshold for how much a user has to lend than borrow to make
     * a non-lending transaction (i.e., just borrow and not lend).
     */
    public String lendingThreshold() {
        return "How much does the user have to (at least) lend than \nthey borrow in order to make a non-lending transaction? \nEnter a whole " +
                "number (minimum 0) for the new limit: ";
    }

    /**
     * This method prompts the admin user to set a threshold for the number of transaction that a use can make in one
     * week (i.e., 7 days).
     */
    public String limitOfTransactions() {
        return "What is the maximum number of transactions a user \ncan conduct in a week? \nEnter a whole number (minimum 1) for the new limit: ";
    }

    /**
     * This method prompts the admin user to set a threshold for the number of incomplete transactions a user can have
     * before the account gets frozen.
     */
    public String limitOfIncompleteTransactions() {
        return "After how many incomplete transactions should a \nuser be flagged for freezing? \nEnter a whole number (minimum 1) for the new limit: ";
    }

    /**
     * This method prompts the admin user to set a threshold for the number of edits allowed by the user to change the
     * meeting place and time. The limit of edits should remain 3.
     */
    public String limitOfEdits() {
        return "What is the maximum number of times the proposed \nmeeting time for a trade can be edited? \nEnter a whole number (minimum 0) for the new limit: ";
    }

    public String goldThreshold() {
        return "What is the credit limit that ranks users gold? \nEnter a whole number (minimum 0) for the new limit: ";
    }

    public String silverThreshold() {
        return "What is the credit limit that ranks users silver? \nEnter a whole number (minimum 0) for the new limit: ";
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
    public String loggedOut() {
        return "See you soon!";
    }
}


 /* *
    /**
     * This method prints the menu options to the screen and asks the admin user to enter a corresponding number to
     * select a menu option. The number determines which method to call in the admin controller.
     *//*
    public List<String>  startMenu() {
        List<String> options = new ArrayList<>();
        options.add("Add new admins");
        options.add("Freeze user accounts");
        options.add("Unfreeze user accounts");
        options.add("Review newly added items");
        options.add("Set the lending threshold");
        options.add("Set the limit of number of weekly transactions");
        options.add("Set the limit of number of incomplete transactions");
        options.add("Set the limit of number of edits to a meeting");
        return options;
    }*/
