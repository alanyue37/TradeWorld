import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewingMenuPresenter extends TextPresenter {

    public ViewingMenuPresenter() {  // Java creates a default constructor. Unnecessary?
    }

    public void showViewingOptions() {
        List<String> options = new ArrayList<>();
        options.add("View system inventory");
        options.add("View your inventory");
        options.add("View your wishlist");
        printList(options, true, false);

        System.out.println("\nPlease enter the # of your choice or \"back\" to go back: ");

    }

    /**
     * Print the system inventory (all confirmed items)
     * @param items A list of strings that represent the itemID
     */
    public void printSystemInventory(List<String> items){
        if (items.size() > 0) {
            System.out.println("System Inventory: \n");
            printList(items, false, true);
        } else {
            System.out.println("System inventory is empty.");
        }
    }

    /**
     * Print the users inventory
     * @param items A list of strings that represent the items in the inventory of the User
     */
    public void printUserInventory(List<String> items){
        if (items.size() > 0) {
            System.out.println("Your Inventory: \n");
            printList(items, false, true);
        } else {
            System.out.println("Your inventory is empty.");
        }
    }

    /**
     * Print the users wishlist
     * @param items A list of strings that represent the items in the wishlist of the User
     */
    public void printUserWishlist(List<String> items){
        if (items.size() > 0) {
            System.out.println("Your Wishlist: \n");
            printList(items, false, true);
        } else {
            System.out.println("Your wishlist is empty.");
        }
    }

}
