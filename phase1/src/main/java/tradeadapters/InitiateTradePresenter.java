package tradeadapters;

import trademisc.TextPresenter;

import java.util.ArrayList;
import java.util.List;

public class InitiateTradePresenter extends TextPresenter {

    /**
     * This method prints the items available to trade for or borrow.
     *
     * @param items The list of item IDs to be printed
     */
    public void availableItemsMenu(List<String> items) {
        System.out.println("The following items are available for trade:");
        printList(items, false, true);
        System.out.println("Please enter the ID of the item you would like to choose or \"back\" to go back: ");
    }

    /**
     * Informs the User whether they have items to offer and the items is in other User's wishlist.
     * @param items     A list of strings representing the items
     * @param wishlist  It is either true or false
     */
    public void itemsToOffer(List<String> items, boolean wishlist) {
        if (wishlist) {
            System.out.println("The following items from your inventory are in the other user's wishlist:");
        }
        else {
            System.out.println("You do not have any items from the other user's wishlist. Choose an item from your inventory to suggest:");
        }
        printList(items, false, true);
    }

    /**
     * Informs the User that is trade is not possible because they do not have items in their inventory to
     * make an offer.
     */
    public void noItemsToOffer() {
        System.out.println("Trade not possible: You do not have any available items in your inventory to offer in return.");
    }

    /**
     * Prints that the trade is not possible because there are no items available.
     */
    public void noItemsToTrade() {
        System.out.println("Trade not possible: There are no available items.");
    }

    /**
     * Prompts the User to enter the location of the meeting.
     */
    public void askMeetingLocation() {
        System.out.println("Enter the location of the meeting: ");
    }

    /**
     * Prompts the User to enter the date and time in the following format: dd/mm/yyyy hh:mm, where dd is day, mm is
     * the month, yyyy is the year, hh is the hour, and mm is the minute.
     */
    public void askMeetingDate() {
        System.out.println("Enter the date and time of the meeting (dd/mm/yyyy hh:mm): ");
    }

    /**
     * Informs the User that their account is frozen and provides option to make a request to the admin
     * to unfreeze their account.
     */
    public void frozenAccount() {
        System.out.println("Your account is frozen. You cannot initiate a trade until it's unfrozen by an admin.");
        System.out.println("Enter 1 to request to have your account unfrozen by an admin. Otherwise enter 0.");
    }

    /**
     * Informs the User whether the trade has been completed or not.
     * @param success   It is either true or false
     */
    public void successful(boolean success){
        if (success) {
            System.out.println("The trade has been initiated successfully.");
        }
        else {
            System.out.println("The trade was not initiated. Please try again.");
        }
    }

    /**
     * These are the menu options prompting the User to enter a number corresponding to the menu options
     * or type back to go back to the menu.
     */
    public void tradeTypesMenu() {
        List<String> tradeTypes = new ArrayList<>();
        tradeTypes.add("One way temporary");
        tradeTypes.add("One way permanent");
        tradeTypes.add("Two way temporary");
        tradeTypes.add("Two way permanent");
        printList(tradeTypes, true, false);
        System.out.println("Please enter the # of your choice of trade or \"back\" to go back: ");
    }
}
