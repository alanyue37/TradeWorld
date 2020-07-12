import java.util.ArrayList;
import java.util.Collection;
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
                " Enter 2 to view your wishlist\n " +
                " Enter 3 to view your inventory\n " +
                " Enter 4 to view last transaction \n " +
                " Enter 5 to view top 3 most frequent trading partners: \n" +
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
                " Enter 4 to temporarily trade items\n " +
                " Enter 5 to permanently trade items\n " +
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
        System.out.println("Your Inventory: ");
        printInfoForItemIds(tradeModel.getUserManager().getSetByUsername(username, ItemSets.INVENTORY));
    }

    /**
     * Print the users wishlist
     */
    public void printUserWishlist(String username){
        System.out.println("Wishlist: " + tradeModel.getUserManager().getSetByUsername(username, ItemSets.WISHLIST));
    }

    /**
     * Print the systems inventory
     */
    public void printSystemInventory(){
        System.out.println("System Inventory: ");
        printInfoForItemIds(tradeModel.getItemManager().getAvailableItems());
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

    public void viewLastThreeTrades(String username){
        System.out.println("Your last three trades were: " + tradeModel.getTradeManager().getRecentItemsTraded(3, username));
    }

    public void printEnterTradeLocation(){
        System.out.println("Enter the location of the meeting: ");
    }

    public void printEnterTradeDate(){
        System.out.println("Enter the date and time of the meeting (dd/mm/yyyy hh:mm): ");
    }

    private void printInfoForItemIds(Collection<String> itemIds){
        for (String itemId : itemIds) {
            String itemInfo = tradeModel.getItemManager().getItemInfo(itemId);
            System.out.println(itemInfo);
        }
    }

}
