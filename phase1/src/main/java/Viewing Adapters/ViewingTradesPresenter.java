import java.util.ArrayList;
import java.util.List;

public class ViewingTradesPresenter extends TextPresenter{
    public ViewingTradesPresenter(){
    }

    public void showViewingOptions() {
        List<String> options = new ArrayList<>();
        options.add("View your ongoing trades");
        options.add("View trade status");
        options.add("View recent transaction items");
        options.add("View most frequent trading partners");
        printList(options, true, false);
        System.out.println("\nPlease enter the # of your choice or \"back\" to go back: ");
    }

    /**
     * Print the trade with id as tradeId
     */
    public void showTrade(String tradeId, String tradeInfo){
        System.out.println(tradeId + ": " + tradeInfo);
    }

    /**
     * Prompts the user to enter tradeID
     */
    public void printEnterTradeId(){
        System.out.println("Enter trade id: ");
    }

    /**
     * Print a trade complete message for the user
     */
    public void printTradeCompleted(){
        System.out.println("Trade Completed");
    }

    /**
     * Print a trade incomplete message for the user
     */
    public void printTradeIncomplete(){
        System.out.println("Trade Incomplete");
    }

    public void printRecentItems(int num, List<String> items){
        printList(items, false, true);
        if (items.size() > 0) {
            System.out.println("Your last (up to) " + num + " items were:");
            printList(items, true, false);
        } else {
            System.out.println("You do not have any trading history.");
        }
    }

    public void printEnterNumTrades(){
        System.out.println("Enter <number> to view the most recent <number> of items that you have traded: ");
    }

    /**
     * Print the top trading partners of a user (if any)
     */
    public void printViewTopTradingPartners(int num, List<String> partners){
        if (partners.size() > 0) {
            System.out.println("Your top trading partners are (up to " + num + "): ");
            printList(partners, true, false);
        } else {
            System.out.println("You do not have any trading history.");
        }
    }

}
