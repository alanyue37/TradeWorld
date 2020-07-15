import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewingTradesController implements RunnableController {
    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final ViewingTradesPresenter presenter;
    private final String username;
    private final int numTradingPartners;
    private final int numLastTrades;

    public ViewingTradesController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ViewingTradesPresenter();
        numTradingPartners = 3;
        numLastTrades = 3;
    }

    @Override
    public void run() {
        try {
            browseViewOptions();
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }

    private void browseViewOptions() throws IOException {
        presenter.showViewingOptions();
        boolean validInput = false;
        do {
            String input = br.readLine();
            switch (input) {
                case "1": // view all ongoing trades
                    viewOngoingTrades();
                    validInput = true;
                    break;
                case "2": // view trade status
                    viewTradeStatus();
                    validInput = true;
                    break;
                case "3": // view last transaction
                    viewRecentTrades();
                    validInput = true;
                    break;
                case "4": // view top 3 most frequent trading partners
                    viewTradingPartners();
                    validInput = true;
                    break;
                case "exit":
                    return;
                default:
                    presenter.tryAgain();
            }
        } while (!validInput);
    }


    private void viewOngoingTrades(){
        List<String> trades = tradeModel.getTradeManager().getTradesOfUser(username, "ongoing");
        for (String tradeId : trades) {
            presenter.showTrade(tradeId, tradeModel.getTradeManager().getTradeAllInfo(tradeId));
        }
    }

    private void viewTradeStatus() throws IOException{
        presenter.printEnterTradeId();
        String tradeId = br.readLine();
        if (tradeModel.getTradeManager().tradeCompleted(tradeId)){
            presenter.printTradeCompleted();
        }
        else if(tradeModel.getTradeManager().isIncompleteTrade(tradeId)){
            presenter.printTradeIncomplete();
        }
    }


    /**
     * View user's last numLastTrades trades
     */
    private void viewRecentTrades(){
        List<String> lastTrades = tradeModel.getTradeManager().getRecentItemsTraded(numLastTrades, username);
        presenter.printRecentTrades(numLastTrades, lastTrades);
    }

    /**
     * View top numTradingPartners most frequent trading partners
     */
    private void viewTradingPartners(){
        List<String> tradingPartners = tradeModel.getTradeManager().getFrequentPartners(numTradingPartners, username);
        presenter.printViewTopTradingPartners(numTradingPartners, tradingPartners);
    }

    private List<String> getItemsInfo(Collection<String> itemIds) {
        List <String> itemsInfo = new ArrayList<>();
        for (String itemId : itemIds) {
            itemsInfo.add(tradeModel.getItemManager().getItemInfo(itemId));
        }
        return itemsInfo;
    }


}
