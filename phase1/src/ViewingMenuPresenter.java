import java.util.Collection;
import java.util.List;

public class ViewingMenuPresenter extends TextPresenter {

    public ViewingMenuPresenter() {
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
     * Print the top trading partners of a user (if any)
     */
    public void printViewTopTradingPartners(int num, List<String> partners){
        if (partners.size() > 0) {
            System.out.println("Your top trading partners are (up to " + num + "): ");
            printList(partners, true, false);
        } else {
            System.out.println("You do not have any trading history.");
        }
    }

    /**
     * Print the users inventory
     */
    public void printUserInventory(List<String> items){
        if (items.size() > 0) {
            System.out.println("Your Inventory: ");
            printList(items, false, true);
        } else {
            System.out.println("Your inventory is empty.");
        }
    }

    /**
     * Print the users wishlist
     */
    public void printUserWishlist(TradeModel tradeModel, String username){
        System.out.println("Wishlist: " + tradeModel.getUserManager().getSetByUsername(username, ItemSets.WISHLIST));
    }

    public void printRecentTrades(int num, List<String> trades){
        if (trades.size() > 0) {
            System.out.println("Your last (up to) " + num + "trades were:");
            printList(trades, true, false);
        } else {
            System.out.println("You do not have any trading history.");
        }
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
