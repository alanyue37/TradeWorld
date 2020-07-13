/**
 * A Presenter class that prompts an admin user for inputs.
 */
public class AdminPresenter {
    private UserManager userManager;

    public AdminPresenter(TradeModel tm) {
        this.userManager = tm.getUserManager();
    }

    /**
     * This method prints the menu options to the screen and asks the admin user to enter a corresponding number to
     * select a menu option. The number determines which method to call in the admin controller.
     */
    public void startMenu(String username) {
        System.out.println("*** " + username + " Admin Menu***\n" +
                "- Enter 1 to add new admins\n" +
                "- Enter 2 to freeze user Accounts\n" +
                "- Enter 3 to unfreeze user Accounts\n" +
                "- Enter 4 to review items\n" +
                "- Enter 5 to set the lending threshold\n" +
                "- Enter 6 to set the limit for the number of weekly transactions\n" +
                "- Enter 7 to set the limit for incomplete transactions\n" +
                "- Enter 8 to set the limit for edits\n" +
                "\n Type \"exit\" at any time to exit.");
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
     * This method prompts the admin user to enter 1 to freeze this particular account.
     * @param freeze  The account that the admin has to freeze.
     */
    public void freezeAccounts(String freeze) {
        System.out.println("This account has reached its limits and has to be frozen. " +
                "Type 1 to freeze this account.\n Freeze Account for: " + freeze);
    }

    /**
     * This method prompts the admin user to enter 1 to unfreeze this particular account.
     * @param unfreeze  The account that the admin has to unfreeze.
     */
    public void unfreezeAccounts(String unfreeze) {
        System.out.println("This user has their account frozen and is requesting to unfreeze." +
                "Type 1 to unfreeze this account.\n Unfreeze Account for: " + unfreeze);
    }

    /**
     * This method prompts the admin user to review the item and decided whether this item should be added
     * to the system or not.
     */
    public void reviewItem(String item) {
        System.out.println("Type 1 to add this item or 0 to not add the following item: \n" + item);
    }

    /**
     * This method prompts the admin user to set a threshold for how much a user has to lend than borrow to make
     * a non-lending transaction (i.e., just borrow and not lend).
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
     */
    public void limitOfTransactions(int current) {
        System.out.println("What is the maximum number of transactions a user can conduct in a week?");
        System.out.println("The current limit is: " + current);
        System.out.println("Enter a whole number (minimum 1) for the new limit: ");
    }

    /**
     * This method prompts the admin user to set a threshold for the number of incomplete transactions a user can have
     * before the account gets frozen.
     */
    public void limitOfIncompleteTransactions(int current) {
        System.out.println("After how many incomplete transactions should a user be flagged for freezing?");
        System.out.println("The current threshold is: " + current);
        System.out.println("Enter a whole number (minimum 1) for the new threshold: ");
    }

    /**
     * This method prompts the admin user to set a threshold for the number of edits allowed by the user to change the
     * meeting place and time. The limit of edits should remain 3.
     */
    public void limitOfEdits(int current) {
        System.out.println("What is the maximum number of times the proposed meeting time for a trade can be edited?");
        System.out.println("The current limit is: " + current);
        System.out.println("Enter a whole number (minimum 0) for the new limit: ");
    }

    /**
     * This method prints to the screen and signals the admin user that the system has successfully ended.
     */
    public void end() {
        System.out.println("See you soon!");
    }

    /**
     * This method informs the admin user that the input is not accepted by the program.
     */
    public void invalidInput() {
        System.out.println("This is an invalid input.\n Please try again!");
    }
}