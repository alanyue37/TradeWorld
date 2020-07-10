/**
 * AdminPresenter prompts an AdminUser for inputs
 */
public class AdminPresenter extends LogInPresenter {

    public AdminPresenter(UserManager um) {
        super(um);
    }

    public void startMenu() {
        System.out.println("Welcome! Type 0 to exit and 1 to see more options");
    }

    public void chooseCreateAccountOrLogin() {
        System.out.println("Type 'create an account' to create an AdminAccount or 'Login' to login to an AdminAccount: ");
    }

    public void accountEnterName() {
        System.out.println("Enter your name: ");
    }

    public void accountEnterEmail() {
        System.out.println("Enter your email: ");
    }

    public void accountEnterPassword() {
        System.out.println("Enter your password: ");
    }

    public void addNewAdminUser(String adminUsersToBeAdded) {
        System.out.println("Type '1' to make this user an admin user." + "Add New Admin User: " + adminUsersToBeAdded);
    }

    public void freezeAccounts(String freeze) {
        System.out.println("This account has to be frozen. Type 1 to freeze this account. " +
                "Freeze Account for: " + freeze);
    }

    public void unfreezeAccounts(String unfreeze) {
        System.out.println("This user has their account frozen and is requesting to unfreeze. " +
                "Type 'confirm' to unfreeze this account. Unfreeze Account for: " + unfreeze);
    }

    public void addItemToInventory(String itemToBeAdded) {
        System.out.println("The user wants to add an item to their inventory. " +
                "Type 'yes' to add this item or 'no' to not add this item. Add this item to this User's Inventory?: "
                + itemToBeAdded);
    }


    public void setThreshold() {
        System.out.println("How much does the user have to (at least) lend than they have borrowed in order to make " +
                "a non-lending transaction? Enter a whole number for the Lending Threshold: ");
    }

    public void end() {
        System.out.println("See you soon!");
    }
}

