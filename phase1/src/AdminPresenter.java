

/**
 * AdminPresenter prompts an AdminUser for inputs
 */
public class AdminPresenter {
    private AdminNotifyManager anm;
    private final TradeModel tradeModel;

    public AdminPresenter(UserManager um, ItemManager im, TradeManager tm, TradeModel tradeModel, AdminNotifyManager anm) {
        this.tradeModel = tradeModel;
    }

    public void startMenu() {
        System.out.println("Welcome! Enter 'continue' to continue or 'exit' to exit");
    }

    public void chooseCreateAccountOrLogin() {
        System.out.println("Type 'create an account' to create an AdminAccount or 'Login' to login to an AdminAccount: ");
    }

    public void accountEnterName() {
        System.out.println("Enter Name: ");
    }

    public void accountEnterEmail() {
        System.out.println("Enter Email: ");
    }

    public void accountEnterPassword() {
        System.out.println("Enter Password: ");
    }

    public void addNewAdminUser() {
        System.out.println("Add New Admin User: " + anm.adminUserToString());
    }


    public void freezeAccounts() {
        System.out.println("Freeze Accounts: ");
    }

    public void unfreezeAccounts() {
        System.out.println("Requests for Unfreezing Accounts: ");
    }

    public void addItemToInventory() {
        System.out.println("Add this item to this User's Inventory?: ");
    }


    public void addItemForAD() {
        System.out.println("Add item for advertisement?: " + um.toString());
    }

    public void setThreshold() {
        System.out.println("Set Threshold for lending: ");
    }











    /**
     * Checks whether there is another prompt after current prompt
     * Citation of code: based on the class studentPropertiesIterator.java (week 6)
     */
    @Override
    public boolean hasNext() {
        return curr < prompts.size();
    }

    /**
     * @return the next prompt to print
     * Citation of code: based on the class studentPropertiesIterator.java (week 6)
     */
    @Override
    public String next() {
        String input;
        try {
            input = prompts.get(curr);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        } curr += 1;
        return input;
    }

    /**
     * Removes the prompt from the screen
     * Citation of code: based on the class studentPropertiesIterator.java (week 6)
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not Supported.");
    }


}

