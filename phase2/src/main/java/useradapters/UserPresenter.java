package useradapters;

import trademisc.TextPresenter;

import java.util.ArrayList;
import java.util.List;

public class UserPresenter extends TextPresenter {

    /**
     * Print main menu to run the UserController
     *
     * // @param username The username of the currently logged in TradingUser
     */

    // SHOULD WE DISPLAY THE USERNAME ?
    public List<String> startMenu() {
        // System.out.println("\n*** " + username + " User Menu***");

        List<String> options = new ArrayList<>();
        options.add("Add items to inventory");
        options.add("Add items to wishlist");
        options.add("View inventory, wishlist or an user's profile");
        options.add("View trading history");
        options.add("Initiate trades");
        options.add("Manage proposed trades");
        options.add("Confirm trades");
        options.add("Manage/view your account settings");
        return options;
    }

    /**
     * Print statement to input item name
     */
    public String printInputItemName(){
        return "Enter item's name: ";
    }

    /**
     * Print statement to input item description
     */
    public String printInputItemDescription(){
        return "Enter item's description: ";
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
     * Print a message that tells DemoUser they do not have permission to perform a task.
     */
    public void printDemoMessage() {
        System.out.println("This is just a demo. You cannot actually do this.");
    }

}
