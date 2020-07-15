import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPresenter extends TextPresenter {

    /**
     * Constructor for UserController.
     */
    public UserPresenter(){
    }

    /**
     * Print main menu to run the UserController
     */
    public void startMenu(String username) {
        System.out.println("\n*** " + username + " User Menu***");
        List<String> options = new ArrayList<>();
        options.add("Add items to inventory");
        options.add("Add items to wishlist");
        options.add("View inventory, wishlist, or trading history");
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
     */
    public void printItemsToAddToWishlist(List<String> items){
        System.out.println("The following items may be added to your wishlist: ");
        printList(items, false, true);
        System.out.println("Please enter the ID of the item you would like to add or \"back\" to go back: ");
    }

}
