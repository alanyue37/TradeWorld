import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewingTradesPresenter extends TextPresenter{
    public ViewingTradesPresenter(){
    }

    public void showViewingOptions() {
        List<String> options = new ArrayList<>();
        options.add("View your ongoing and completed trades ids");
        options.add("View your trade information by typing trade id");
        options.add("View recent transaction items");
        options.add("View most frequent trading partners");
        printList(options, true, false);
        System.out.println("\nPlease enter the # of your choice or \"exit\" to exit: ");
    }

    /**
     * Print the trade with id as tradeId
     */
    public void showTrade(List<String> ongoingIds, List<String> completedIds){
        StringBuilder ongoing;
        StringBuilder completed;
        if (ongoingIds.size() == 0){
            ongoing = new StringBuilder("No ongoing trades.");
        } else{
            ongoing = new StringBuilder("Ongoing trade Ids: ");
            for (String ongoingId :ongoingIds){
                ongoing.append(ongoingId).append("  ");
            }
        }
        if (completedIds.size() == 0){
            completed = new StringBuilder("No completed trades");
        } else{
            completed = new StringBuilder("Completed trade Ids: ");
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
        System.out.println("Enter trade id: ");
    }

    public void showInfo(String tradeInfo){
        System.out.println(tradeInfo);
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

}
