package viewingadapters;

import org.json.JSONException;
import org.json.JSONObject;
import tradegateway.TradeModel;
import trademisc.RunnableController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * A controller class using the RunnableController interface for viewing the trades and related activities.
 */
public class ViewingTradesController implements RunnableController {
    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final ViewingTradesPresenter presenter;
    private final String username;

    public ViewingTradesController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ViewingTradesPresenter();
    }

    @Override
    public void run() {
        try {
            browseViewOptions();
        } catch (IOException | JSONException e) {
            System.out.println("Something bad happened.");
        }
    }

    private void browseViewOptions() throws IOException, JSONException {
        presenter.showViewingOptions();
        boolean validInput = false;
        do {
            String input = br.readLine();
            switch (input) {
                case "1": // get all ongoing trades and completed trades ids (will be separated)
                    viewTradeIds();
                    validInput = true;
                    break;
                case "2": // view trade information by inputting trade id
                    viewTradeInfo();
                    validInput = true;
                    break;
                case "3": // view recent transaction items
                    viewRecentItems();
                    validInput = true;
                    break;
                case "4": // view most frequent trading partners
                    viewTradingPartners();
                    validInput = true;
                    break;
                case "5": // view most recent reviews
                    viewReviews();
                    validInput = true;
                    break;
                case "back":
                    return;
                default:
                    presenter.tryAgain();
            }
        } while (!validInput);
    }

    private void viewTradeIds(){
        List<String> ongoingTradeIds = tradeModel.getTradeManager().getTradesOfUser(username, "ongoing");
        List<String> completedTradeIds = tradeModel.getTradeManager().getTradesOfUser(username, "completed");
        presenter.showTrade(ongoingTradeIds, completedTradeIds);
    }

    private void viewTradeInfo() throws IOException, JSONException {
        List<String> userTrades = tradeModel.getTradeManager().getTradesOfUser(username, "ongoing");
        userTrades.addAll(tradeModel.getTradeManager().getTradesOfUser(username, "completed"));
        presenter.printEnterTradeId();
        String tradeId = br.readLine();
        if (!userTrades.contains(tradeId)) {
            presenter.printSearchingInvalid();
        } else {
            List<JSONObject> allTradeInfo = new ArrayList<>();
            allTradeInfo.add(tradeModel.getTradeManager().getTradeInfo(tradeId));
            allTradeInfo.addAll(tradeModel.getMeetingManager().getMeetingsInfo(tradeId));
            presenter.showInfo(allTradeInfo);
        }
    }


    /**
     * View user's last numLastTrades items
     */
    private void viewRecentItems() throws IOException{
        presenter.printEnterNumTrades();
        String numItems = br.readLine();
        int numLastTrades = Integer.parseInt(numItems);

        List<String> userCompletedTrades = tradeModel.getTradeManager().getTradesOfUser(username, "completed");
        List<String> sortedCompletedTrades = tradeModel.getMeetingManager().sortedMeeting(userCompletedTrades);
        List<String> tradeIds = tradeModel.getTradeManager().getRecentTrades(numLastTrades, sortedCompletedTrades);
        if (tradeIds.size() == 0){
            presenter.noTrades();
        } else {
            Map<String, List<String>> mapOfItem = new HashMap<>();
            for (String tradeId : tradeIds) {
                StringBuilder itemTrading = new StringBuilder("Trade with ID " + tradeId + ": ");
                for (String itemId : tradeModel.getTradeManager().itemToUsers(tradeId).keySet()) {
                    String giver = tradeModel.getTradeManager().itemToUsers(tradeId).get(itemId).get(0);
                    String receiver = tradeModel.getTradeManager().itemToUsers(tradeId).get(itemId).get(1);
                    itemTrading.append("item with ID ").append(itemId).append(" was traded to ").append(receiver).append(" by ").append(giver).append(". ");
                }
                mapOfItem.put(itemTrading.toString(), getItemsInfo(tradeModel.getTradeManager().itemToUsers(tradeId).keySet()));
            }
            presenter.printRecentItems(numLastTrades, mapOfItem);
        }
    }


    /**
     * View top numTradingPartners most frequent trading partners
     */
    private void viewTradingPartners() throws IOException {
        presenter.printEnterFrequent();
        String numItems = br.readLine();
        int numLastTrades = Integer.parseInt(numItems);
        List<String> tradingPartners = tradeModel.getTradeManager().getFrequentPartners(numLastTrades, username);
        presenter.printViewTopTradingPartners(numLastTrades, tradingPartners);
    }

    private List<String> getItemsInfo(Collection<String> itemIds) {
        List <String> itemsInfo = new ArrayList<>();
        for (String itemId : itemIds) {
            itemsInfo.add(tradeModel.getItemManager().getItemInfo(itemId));
        }
        return itemsInfo;
    }

    private void viewReviews() throws IOException {
        presenter.printEnterNumReviews();
        String numReviews = br.readLine();
        int numLastReviews = Integer.parseInt(numReviews);
        List<String> reviews = tradeModel.getReviewManager().viewProfile(username, numLastReviews);
        presenter.printViewLastReviews(numLastReviews, reviews);
    }


}
