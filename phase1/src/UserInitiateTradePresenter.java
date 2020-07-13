import java.util.List;

public class UserInitiateTradePresenter {

    /**
     * This method prints the items available to trade for or borrow.
     */
    public void availableItemsMenu(List<String> items) {
        displayMenu(items);
    }

    public void displayMenu(List<String> options) {
        int i = 1;
        for (String s : options) {
            System.out.println(i + ". " + options.get(i));
            i++;
        }
        System.out.println("Please enter your # choice: ");
    }

}
