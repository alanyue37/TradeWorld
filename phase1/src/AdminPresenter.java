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
    public void startMenu() {
        System.out.println("Welcome! Admin Options\n " +
                "Enter 1 to add New Admins\n" +
                "Enter 2 to Freeze User Accounts\n" +
                "Enter 3 to Unfreeze User Accounts\n" +
                "Enter 4 to change Availability of items\n" +
                "Enter 5 to add items to Inventory\n" +
                "Enter 6 to set a Lending Threshold\n" +
                "Enter 7 to set a Limit for the Number of Transactions that could be conducted in one week\n" +
                "Enter 8 to set a Limit for Incomplete Transactions\n" +
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
        System.out.println("Type 1 to add this item or 0 to not add this item. Should " + item + "to the system?: ");
    }

    /**
     * This method prompts the admin user to set a threshold for how much a user has to lend than borrow to make
     * a non-lending transaction (i.e., just borrow and not lend).
     */
    public void lendingThreshold() {
        System.out.println("How much does the user have to (at least) lend than they have borrowed in order to make " +
                "a non-lending transaction? Enter a whole number for the Lending Threshold: ");
    }

    /**
     * This method prompts the admin user to set a threshold for the number of transaction that a use can make in one
     * week (i.e., 7 days).
     */
    public void limitOfTransactions() {
        System.out.println("Enter a whole number to set a limit on the number of transactions that the user can " +
                "conduct in one week: ");
    }

    /**
     * This method prompts the admin user to set a threshold for the number of incomplete transactions a user can have
     * before the account gets frozen.
     */
    public void limitOfIncompleteTransactions() {
        System.out.println("Enter a whole number to set a limit for the number of incomplete transactions before " +
                "the user account is frozen");
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
