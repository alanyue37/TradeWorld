package viewingadapters;

import org.json.JSONObject;
import trademisc.TextPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A presenter class for trades and printing text to the screen.
 */
public class ViewingTradesPresenter extends TextPresenter {

    public void showViewingOptions() {
        List<String> options = new ArrayList<>();
        options.add("View IDs of your ongoing and completed trades");
        options.add("View your trade information by ID");
        options.add("View recent transaction items");
        options.add("View most frequent trading partners");
        options.add("View most recent reviews from trade");
        printList(options, true, false);
        System.out.println("\nPlease enter the # of your choice or \"back\" to go back: ");
    }

    /**
     * Print the trade with id as tradeId
     *
     * @param ongoingIds The list of ongoing Trade IDs
     * @param completedIds The list of completed Trade IDs
     */
    public void showTrade(List<String> ongoingIds, List<String> completedIds){
        StringBuilder ongoing;
        StringBuilder completed;
        if (ongoingIds.size() == 0){
            ongoing = new StringBuilder("No ongoing trades.");
        } else{
            ongoing = new StringBuilder("Ongoing trade IDs: ");
            for (String ongoingId :ongoingIds){
                ongoing.append(ongoingId).append("  ");
            }
        }
        if (completedIds.size() == 0){
            completed = new StringBuilder("No completed trades.");
        } else{
            completed = new StringBuilder("Completed trade IDs: ");
            for (String completedId: completedIds){
                completed.append(completedId).append("  ");
            }
        }
        System.out.println(ongoing + "\n" + completed);
    }

    /**
     * Prompts the user to enter tradeID
     */
    public void printEnterTradeId(){
        System.out.println("Enter trade ID: ");
    }

    public void showInfo(List<JSONObject> tradeInfo) {
        for (JSONObject details : tradeInfo) {
            System.out.println(details);
        }
    }

    public void noTrades(){
        System.out.println("You do not have any trading history.");
    }

    public void printRecentItems(int num, Map<String, List<String>> itemMap){
        System.out.println("Your last (up to) " + num + " items were:");
        for (String itemsTraded: itemMap.keySet()){
            System.out.println(itemsTraded);
            printList(itemMap.get(itemsTraded), false, true);
        }
    }

    public void printEnterNumTrades(){
        System.out.println("Enter <number> to view the most recent <number> of items that you have traded: ");
    }

    /**
     * Print the top trading partners of a user (if any)
     *
     * @param num The number of partners to be displayed
     * @param partners The list of trading partners
     */
    public void printViewTopTradingPartners(int num, List<String> partners){
        if (partners.size() > 0) {
            System.out.println("Your top trading partners are (up to " + num + "): ");
            printList(partners, true, false);
        } else {
            System.out.println("You do not have any trading history.");
        }
    }

    public void printEnterFrequent(){
        System.out.println("Enter <number> to view your <number> top trading partners: ");
    }

    public void printSearchingInvalid(){
        System.out.println("You do not have a trade with this ID.");
    }

    public void printEnterNumReviews(){
        System.out.println("Enter <number> to view the most recent <number> of reviews from your trades: ");
    }

    public void printViewLastReviews(int num, List<String> reviews){
        if (reviews.size() > 0){
            System.out.println("Your most recent reviews are (up to " + num + "): ");
            printList(reviews, true, false);
        } else{
            System.out.println("You do not have any reviews yet.");
        }
    }
}
