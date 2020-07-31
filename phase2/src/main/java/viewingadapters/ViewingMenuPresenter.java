package viewingadapters;

import trademisc.TextPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * A presenter class for the menu options and printing text to the screen.
 */
public class ViewingMenuPresenter extends TextPresenter {

    public void showViewingOptions() {
        List<String> options = new ArrayList<>();
        options.add("View system inventory");
        options.add("View your inventory");
        options.add("View your wishlist");
        options.add("View a user's profile");
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

    public void printEnterUsername(){
        System.out.println("Enter username of user: ");
    }

    public void printInvalidUsername(){
        System.out.println("There is no existing user with this username.");
    }

    public void printEnterNumReviews(){
        System.out.println("Enter <number> to view the most recent <number> of reviews from your trades: ");
    }

    public void printViewLastReviews(int num, List<String> reviews){
        if (reviews.size() > 0){
            System.out.println("This user's most recent reviews are (up to " + num + "): ");
            printList(reviews, true, false);
        } else{
            System.out.println("This user do not have any reviews yet.");
        }
    }

}
