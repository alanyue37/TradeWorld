package useradapters;

import tradegateway.TradeModel;
import trademisc.TextPresenter;

import java.io.IOException;
import java.util.*;

public class UserPresenter2 implements Iterator<String> {

    protected final TradeModel tradeModel;
    protected final String username;
    private final List<String> prompts = new ArrayList<>();
    private int current = 0;

    public UserPresenter2(TradeModel tm, String un) {
        tradeModel = tm;
        username = un;
    }

    /**
     * Checks for subsequent prompts.
     * @return true if there is prompt that has not yet been returned.
     */
    @Override
    public boolean hasNext() {
        return current < prompts.size();
    }

    /**
     * Returns the next prompt to be printed.
     * @return the next prompt.
     */
    @Override
    public String next() {
        String res;
        // List.get(i) throws an IndexOutBoundsException if
        // we call it with i >= properties.size().
        // But Iterator's next() needs to throw a
        // NoSuchElementException if there are no more elements.
        try {
            res = prompts.get(current);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
        current += 1;
        return res;
    }

    public void menuScreen() {
        prompts.add("Trading User - Main Menu");
        prompts.add("Welcome " + tradeModel.getUserManager().getName(username));
    }

    /**
     * Return a list with the menu options
     */
    public void startMenu() {
        prompts.add("Add items to inventory");
        prompts.add("Add items to wishlist");
        prompts.add("Manage trades");
        prompts.add("Manage your account settings");
    }

    public void frozenAlert() {
        prompts.add("Warning Dialog");
        prompts.add("Your account is frozen.");
        prompts.add("You are currently unable to make trades.");
    }

    public void addItemScreen() {
        prompts.add("Current Inventory");
        prompts.add("Add item to inventory");
        prompts.add("Item name");
        prompts.add("Item description");
        prompts.add("Create Item");
//        System.out.println(prompts.size());
    }

    public void itemAdded() {
        prompts.add("Item added");
    }

    public void addWishlistScreen() {
        prompts.add("All available items");
        prompts.add("Your wishlist");
        prompts.add("Add item");
        prompts.add("Remove item");
    }

    public void placeholderText(String type) {
        switch (type) {
            case "all items":
                prompts.add("No items to display");
                break;
            case "inventory":
                prompts.add("No items in inventory");
                break;
            case "wishlist":
                prompts.add("No items in wishlist");
                break;
        }
    }

    public void backButton() {
        prompts.add("Back");
    }

    /**
     * Return user's own inventory.
     */
    public void viewInventory() {
        Set<String> userInventory = tradeModel.getItemManager().getInventory(username);
        for (String itemID : userInventory) {
            prompts.add(itemID + " " +
                    tradeModel.getItemManager().getName(itemID) + " " +
                    tradeModel.getItemManager().getDescription(itemID));
        }
    }

    /**
     * Return system inventory of users in same city (except items in current user's inventory).
     */
    public void viewAllItems() {
        Set<String> items = tradeModel.getItemManager().getItemsByStage("common");
        if (tradeModel.getUserManager().getRankByUsername(username).equals("gold")) {
            items.addAll(tradeModel.getItemManager().getItemsByStage("early"));
        }
        Set<String> userInventory =  tradeModel.getItemManager().getInventory(username);

        for (String itemID : items) {
            String otherUsername = tradeModel.getItemManager().getOwner(itemID);
            String thisUserCity = tradeModel.getUserManager().getCityByUsername(username);
            String otherUserCity = tradeModel.getUserManager().getCityByUsername(otherUsername);
            if (!userInventory.contains(itemID) && thisUserCity.equals(otherUserCity)) {
                prompts.add(itemID + " " +
                        tradeModel.getItemManager().getName(itemID) + " " +
                        tradeModel.getItemManager().getOwner(itemID) + " " +
                        tradeModel.getItemManager().getDescription(itemID));
            }
        }
    }

    /**
     * Return user's wishlist.
     */
    public void viewWishlist() {
        Set<String> userWishlist =  tradeModel.getUserManager().getWishlistByUsername(username);
        for (String itemID : userWishlist) {
            prompts.add(itemID + " " +
                    tradeModel.getItemManager().getName(itemID) + " " +
                    tradeModel.getItemManager().getOwner(itemID) + " " +
                    tradeModel.getItemManager().getDescription(itemID));
        }
    }

//    /**
//     * Print all confirmed items that may be added to inventory
//     *
//     * @param items The list of item IDs to print
//     */
//    public List<String> printItemsToAddToWishlist(List<String> items){
//        System.out.println("The following items may be added to your wishlist: ");
//        List<String> returnList = new ArrayList<>();
//        for (String s : items) {
//            returnList.add(s + "\n");
//        }
//        return returnList;
//    }


    /**
     * Print a message that tells DemoUser they do not have permission to perform a task.
     */
    public void printDemoMessage() {
        System.out.println("This is just a demo. You cannot actually do this.");
    }

}
