package useradapters;

import trademisc.TextPresenter;

import java.util.ArrayList;
import java.util.List;

public class UserPresenter extends TextPresenter {

    /**
     * Print main menu to run the UserController
     *
     * @param username The username of the currently logged in TradingUser
     */
    public void startMenu(String username) {
        System.out.println("\n*** " + username + " User Menu***");
        List<String> options = new ArrayList<>();
        options.add("Add items to inventory");
        options.add("Add items to wishlist");
        options.add("View inventory or wishlist");
        options.add("View trading history");
        options.add("Initiate trades");
        options.add("Manage proposed trades");
        options.add("Confirm trades");
        printList(options, true, false);

        System.out.println("\nPlease enter the # of your choice or \"exit\" to exit: ");
    }

    /**
     * Print statement to input item name
     */
    public void printInputItemName(){
        System.out.println("Enter item's name: ");
    }

    /**
     * Print statement to input item description
     */
    public void printInputItemDescription(){
        System.out.println("Enter item's description: ");
    }

    /**
     * Print all confirmed items that may be added to inventory
     *
     * @param items The list of item IDs to print
     */
    public void printItemsToAddToWishlist(List<String> items){
        System.out.println("The following items may be added to your wishlist: ");
        printList(items, false, true);
        System.out.println("Please enter the ID of the item you would like to add or \"back\" to go back: ");
    }

    /**
     * Print a message that tells DemoUser they do not have permission to perform a task.
     */
    public void printDemoMessage() {
        System.out.println("This is just a demo. You cannot actually do this.");
    }

}
