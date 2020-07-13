import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPresenter {

    /**
     * Constructor for UserController.
     */
    public UserPresenter(){
    }

    /**
     * Print main menu to run the UserController
     */
    public void startMenu() {
        System.out.println("User Options\n" +
                " Enter 1 to add items to inventory\n" +
                " Enter 2 to add items to wishlist\n" +
                " Enter 3 to view menu\n" +
                " Enter 4 to initiate a trade\n" +
                " Enter 5 to propose a trade\n" +
                " Enter 6 to confirm a trade\n" +
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
    public void printUserInventory(TradeModel tradeModel, String username){
        System.out.println("Your Inventory: ");
        printInfoForItemIds(tradeModel, tradeModel.getUserManager().getSetByUsername(username, ItemSets.INVENTORY));
    }

    /**
     * Print the users wishlist
     */
    public void printUserWishlist(TradeModel tradeModel, String username){
        System.out.println("Wishlist: " + tradeModel.getUserManager().getSetByUsername(username, ItemSets.WISHLIST));
    }

    /**
     * Print the systems inventory
     */
    public void printSystemInventory(TradeModel tradeModel){
        System.out.println("System Inventory: ");
        printInfoForItemIds(tradeModel, tradeModel.getItemManager().getAvailableItems());
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

    public void viewLastThreeTrades(TradeModel tradeModel, String username){
        System.out.println("Your last three trades were: " + tradeModel.getTradeManager().getRecentItemsTraded(3, username));
    }

    public void printEnterTradeLocation(){
        System.out.println("Enter the location of the meeting: ");
    }

    public void printEnterTradeDate(){
        System.out.println("Enter the date and time of the meeting (dd/mm/yyyy hh:mm): ");
    }

    private void printInfoForItemIds(TradeModel tradeModel, Collection<String> itemIds){
        for (String itemId : itemIds) {
            String itemInfo = tradeModel.getItemManager().getItemInfo(itemId);
            System.out.println(itemInfo);
        }
    }

    public void printInventory(TradeModel tradeModel, String username){
        System.out.println("System Inventory: ");
        List<String> availableItems = tradeModel.getItemManager().getAvailableItems();
        for (String item: tradeModel.getUserManager().getSetByUsername(username, ItemSets.INVENTORY)){
            availableItems.remove(item);
        }
        printInfoForItemIds(tradeModel, availableItems);
    }

}
