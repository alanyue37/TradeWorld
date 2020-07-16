package tradeadapters;

import trademisc.TextPresenter;

import java.util.ArrayList;
import java.util.List;

public class InitiateTradePresenter extends TextPresenter {

    /**
     * Prints the items available to trade or borrow.
     *
     * @param items The list of item IDs to be printed
     */
    public void availableItemsMenu(List<String> items) {
        System.out.println("The following items are available for trade:");
        printList(items, false, true);
        System.out.println("Please enter the ID of the item you would like to choose or \"back\" to go back: ");
    }

    /**
     * Prints the items that can be offered in return in a two-way trade.
     * @param items The list of item IDs to be printed
     * @param wishlist true iff there are items in other user's wishlist that overlap with current user's available
     * items
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
     * Prints that user has no available items to offer in return in a two-way trade.
     */
    public void noItemsToOffer() {
        System.out.println("Trade not possible: You do not have any available items in your inventory to offer in return.");
    }

    /**
     * Prints that there are no available items in the system to trade or borrow.
     */
    public void noItemsToTrade() {
        System.out.println("Trade not possible: There are no available items.");
    }

    /**
     * Prints prompt for meeting location.
     */
    public void askMeetingLocation() {
        System.out.println("Enter the location of the meeting: ");
    }

    /**
     * Prints prompt for meeting date.
     */
    public void askMeetingDate() {
        System.out.println("Enter the date and time of the meeting (dd/mm/yyyy hh:mm): ");
    }

    /**
     * Prints that user has not lent enough items to borrow.
     */
    public void notEnoughCredits(int deficit) {
        System.out.println("You have insufficient credit to borrow. You may not initiate a one-way trade until you" +
                " have loaned at least " + deficit + " item(s) in one-way trades.");
    }

    /**
     * Prints that user's account is frozen and asks if they would like to make an unfreeze request.
     */
    public void frozenAccount() {
        System.out.println("Your account is frozen. You cannot initiate a trade until it's unfrozen by an admin.");
        System.out.println("Enter 1 to request to have your account unfrozen by an admin. Otherwise enter 0.");
    }

    /**
     * Prints whether trade has been initiated successfully or not.
     * @param success true iff trade was initiated successfully
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
     * Prints list of types of trade and asks user to choice.
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
