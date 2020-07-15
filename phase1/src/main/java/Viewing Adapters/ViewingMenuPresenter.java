import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewingMenuPresenter extends TextPresenter {

    public ViewingMenuPresenter() {
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
