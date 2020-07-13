import java.util.ArrayList;
import java.util.List;

public class UserInitiateTradePresenter {

    /**
     * This method prints the items available to trade for or borrow.
     */
    public void availableItemsMenu(List<String> items) {
        System.out.println("The following items are available for trade:");
        printList(items, false);
        System.out.println("Please enter the ID of the item you would like to choose: ");
    }

    public void itemsToOffer(List<String> items, boolean wishlist) {
        if (wishlist) {
            System.out.println("The following items from your inventory are in the other user's wishlist:");
        }
        else {
            System.out.println("You do not have any items from the other user's wishlist. Choose an item from your inventory to suggest:");
        }
        printList(items, false);
    }

    public void noItemsToOffer() {
        System.out.println("Trade not possible: You do not have any available items in your inventory to offer in return.");
    }

    public void askMeetingLocation() {
        System.out.println("Enter the location of the meeting: ");
    }

    public void askMeetingDate() {
        System.out.println("Enter the date and time of the meeting (dd/mm/yyyy hh:mm): ");
    }

    public void tryAgain() {
        System.out.println("Invalid input. Please enter again:");
    }

    public void frozenAccount() {
        System.out.println("Your account is frozen. You cannot initiate a trade until it's unfrozen by an admin.");
    }

    public void successful(boolean success){
        if (success) {
            System.out.println("The trade has been initiated successfully.");
        }
        else {
            System.out.println("The trade was not initiated. Please try again.");
        }
    }

    public void tradeTypesMenu() {
        List<String> tradeTypes = new ArrayList<>();
        tradeTypes.add("One way temporary");
        tradeTypes.add("One way permanent");
        tradeTypes.add("Two way temporary");
        tradeTypes.add("Two way permanent");
        printList(tradeTypes, true);
        System.out.println("Please enter the # of your choice of trade: ");
    }

    private void printList(List<String> options, boolean numbered) {
        if (numbered) {
            int i = 1;
            for (String s : options) {
                System.out.println(i + ". " + s + "\n");
                i++;
            }
        } else {
            for (String s : options) {
                System.out.println(s + "\n");
            }
        }
    }

}
