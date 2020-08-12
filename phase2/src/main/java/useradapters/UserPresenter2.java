package useradapters;

import tradegateway.TradeModel;
import trademisc.TextPresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserPresenter2 {

    protected final TradeModel tradeModel;
    protected final String username;
    private List <String> itemsToShow;

    public UserPresenter2(TradeModel tm, String un) {
        tradeModel = tm;
        username = un;
    }

    /**
     * Return a list with the menu options
     */
    public List<String> startMenu() {
        List<String> options = new ArrayList<>();
        options.add("Add items to inventory");
        options.add("Add items to wishlist");
        options.add("Manage trades");
//        options.add("View trading history");
//        options.add("Initiate trades");
//        options.add("Manage proposed trades");
//        options.add("Confirm trades");
        options.add("Manage your account settings");
        return options;
    }

    public String getName() {
        return tradeModel.getUserManager().getName(username);
    }

    /**
     * Return user's own inventory.
     */
    public List<String[]> viewInventory() {
        Set<String> userInventory =  tradeModel.getItemManager().getInventory(username);
        itemsToShow = new ArrayList<>();
        itemsToShow.addAll(userInventory);
        return returnItemInfo(itemsToShow, false);
    }

    /**
     * Return system inventory of users in same city (except items in current user's inventory).
     */
    public List<String[]> viewAllItems() {
        itemsToShow = new ArrayList<>();
        Set<String> items = tradeModel.getItemManager().getItemsByStage("common");
        if (tradeModel.getUserManager().getRankByUsername(username).equals("gold")) {
            items.addAll(tradeModel.getItemManager().getItemsByStage("early"));
        }
        Set<String> userInventory =  tradeModel.getItemManager().getInventory(username);

        for (String itemId : items) {
            String otherUsername = tradeModel.getItemManager().getOwner(itemId);
            String thisUserCity = tradeModel.getUserManager().getCityByUsername(username);
            String otherUserCity = tradeModel.getUserManager().getCityByUsername(otherUsername);
            if (!userInventory.contains(itemId) && thisUserCity.equals(otherUserCity)) {
                itemsToShow.add(itemId);
            }
        }
        return returnItemInfo(itemsToShow, true);
    }

    /**
     * Return user's wishlist.
     */
    public List<String[]> viewWishlist() {
        Set<String> userWishlist =  tradeModel.getUserManager().getWishlistByUsername(username);
        itemsToShow = new ArrayList<>();
        itemsToShow.addAll(userWishlist);
        return returnItemInfo(itemsToShow, true);
    }

    /**
     * Print all confirmed items that may be added to inventory
     *
     * @param items The list of item IDs to print
     */
    public List<String> printItemsToAddToWishlist(List<String> items){
        System.out.println("The following items may be added to your wishlist: ");
        List<String> returnList = new ArrayList<>();
        for (String s : items) {
            returnList.add(s + "\n");
        }
        return returnList;
    }

    /**
     * Return a list of String arrays containing item ID, name, (owner), and description.
     * Includes owner if showOwner is true.
     * @param items The list of item IDs to print
     * @param showOwner Whether to include owner in list
     */
    private List<String[]> returnItemInfo(List<String> items, boolean showOwner){
        List<String[]> itemInfoList = new ArrayList<>();
        for (String item : items) {
            if (showOwner) {
                itemInfoList.add(new String[]{item,
                        tradeModel.getItemManager().getName(item),
                        tradeModel.getItemManager().getOwner(item),
                        tradeModel.getItemManager().getDescription(item)});
            } else {
                itemInfoList.add(new String[]{item,
                        tradeModel.getItemManager().getName(item),
                        tradeModel.getItemManager().getDescription(item)});
            }
        }
        return itemInfoList;
    }

    /**
     * Print a message that tells DemoUser they do not have permission to perform a task.
     */
    public void printDemoMessage() {
        System.out.println("This is just a demo. You cannot actually do this.");
    }

}
