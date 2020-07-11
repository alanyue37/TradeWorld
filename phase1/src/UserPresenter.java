import java.util.ArrayList;
import java.util.List;

public class UserPresenter {
    TradeModel tradeModel;

    /**
     * Constructor for UserController.
     * @param tradeModel Model of the system.
     */
    public UserPresenter(TradeModel tradeModel){
        this.tradeModel = tradeModel;
    }

    /**
     * Print main menu to run the UserController
     * There are two options that are available (viewing and trading)
     */
    public void startMenu() {
        System.out.println("User Options\n" +
                " Enter 1 to view \n" +
                " Enter 2 to trade \n " +
                "\n Type \"exit\" at any time to exit.");
    }

    /**
     * Print menu to run the view options for UserController
     */
    public void printViewOptions() {
        System.out.println("View Options\n" +
                " Enter 1 to view inventory\n" +
                " Enter 2 to view wishlist\n " +
                " Enter 3 to view last transaction \n " +
                " Enter 4 to view top 3 most frequent trading partners: \n" +
                "\n Type \"exit\" at any time to exit.");
    }

    /**
     * Print menu to run the trade options for UserController
     */
    public void printViewTradeOptions() {
        System.out.println("Trade Options\n" +
                " Enter 1 to lend items\n" +
                " Enter 2 to temporarily borrow items\n " +
                " Enter 3 to permanently borrow items\n " +
                " Enter 4 to trade items \n " +
                "\n Type \"exit\" at any time to exit.");
    }

    /**
     * Print statement to input item name
     */
    public void printInputItemName(){
        System.out.println("Enter Item's name: ");
    }

    /**
     * Print statement to input item description
     */
    public void printInputItemDescription(){
        System.out.println("Enter Item's description: ");
    }

    /**
     * Print statement to input item id
     */
    public void printInputItemID(){
        System.out.println("Enter Item's id: ");
    }

    /**
     * Print the users inventory
     */
    public void printUserInventory(String username){
        System.out.println("Inventory: " + tradeModel.getUserManager().getSetByUsername(username, itemSets.INVENTORY));
    }

    /**
     * Print the users wishlist
     */
    public void printUserWishlist(String username){
        System.out.println("Wishlist: " + tradeModel.getUserManager().getSetByUsername(username, itemSets.WISHLIST));
    }

    /**
     * Print the systems inventory
     */
    public void printSystemInventory(){
        System.out.println("System Inventory: " + tradeModel.getItemManager().getAvailableItems());
    }

    /**
     * Print the options that the users has after viewing the inventory
     */
    public void printUserInventoryOptions(){
        System.out.println("Enter 1 to add something to your wishlist\n or enter \"exit\" \n ");
    }

    /**
     * Print the top trading partners of a user
     */
    public void printViewTopTradingPartner(List<String> partners){
        System.out.println("Your top 3 trading partners are: " + partners);
    }

    /**
     * Print the options that a frozen users has
     */
    public void printAccountIsFrozenOption(){
        System.out.println("Sorry your account is frozen\n" +
                " Enter 1 to request your account to be unfrozen \n" +
                " Enter \"exit\" to exit \n");
    }

}
