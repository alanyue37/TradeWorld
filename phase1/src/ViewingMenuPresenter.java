import java.util.Collection;
import java.util.List;

public class ViewingMenuPresenter {

    public ViewingMenuPresenter() {
    }

    public void startMenu() {
        System.out.println("Welcome to the viewing menu!\n" +
                "Enter 1 to continue");
    }

    public void invalidInput() {
        System.out.println("Please enter a valid input.");
    }

    public void end() {
        System.out.println("Exiting...");
    }

    public void showViewingOptions() {
        System.out.println("View Options\n" +
                " Enter 1 to view inventory\n" +
                " Enter 2 to view your wishlist\n" +
                " Enter 3 to view your inventory\n" +
                " Enter 4 to view last transaction\n" +
                " Enter 5 to view top 3 most frequent trading partners\n" +
                "\n Type \"exit\" at any time to exit.");
    }


    /**
     * Print the top trading partners of a user
     */
    public void printViewTopTradingPartner(List<String> partners){
        System.out.println("Your top 3 trading partners are: " + partners);
    }

    /**
     * Print the users inventory
     */
    public void printUserInventory(TradeModel tradeModel, String username){
        System.out.println("Your Inventory: ");
        System.out.println(tradeModel.getUserManager().getSetByUsername(username, ItemSets.INVENTORY));
        printInfoForItemIds(tradeModel, tradeModel.getItemManager().getAvailableItems());
        printInfoForItemIds(tradeModel, tradeModel.getUserManager().getSetByUsername(username, ItemSets.INVENTORY));
    }

    /**
     * Print the users wishlist
     */
    public void printUserWishlist(TradeModel tradeModel, String username){
        System.out.println("Wishlist: " + tradeModel.getUserManager().getSetByUsername(username, ItemSets.WISHLIST));
    }

    public void viewLastThreeTrades(TradeModel tradeModel, String username){
        System.out.println("Your last three trades were: " + tradeModel.getTradeManager().getRecentItemsTraded(3, username));
    }

    private void printInfoForItemIds(TradeModel tradeModel, Collection<String> itemIds){
        for (String itemId : itemIds) {
            String itemInfo = tradeModel.getItemManager().getItemInfo(itemId);
            System.out.println(itemInfo);
            System.out.println(itemInfo);
        }
    }

    public void printInventory(TradeModel tradeModel, String username){
        System.out.println("Inventory: ");
        List<String> availableItems = tradeModel.getItemManager().getAvailableItems();
        for (String item: tradeModel.getUserManager().getSetByUsername(username, ItemSets.INVENTORY)){
            availableItems.remove(item);
        }
        printInfoForItemIds(tradeModel, availableItems);
    }

    public void printSystemInventory(TradeModel tradeModel){
        System.out.println("System Inventory: ");
        List<String> availableItems = tradeModel.getItemManager().getAvailableItems();
        printInfoForItemIds(tradeModel, availableItems);
    }



}
