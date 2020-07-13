import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class ViewingMenuController implements RunnableController {
    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final ViewingMenuPresenter presenter;
    private final String username;
    private final int numTradingPartners;
    private final int numLastTrades;

    public ViewingMenuController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ViewingMenuPresenter();
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
        List<String> vaildOptions = Arrays.asList(new String[] {"1", "2", "3", "4", "5", "exit"});
        String input = br.readLine();
        while (!vaildOptions.contains(input)) {
            System.out.println("Invalid input. Please try again:");
            input = br.readLine();
        }
        switch (input) {
            case "1": // view inventory
                viewInventory();
                break;
            case "2": // view your wishlist
                viewWishlist();
                break;
            case "3": // view your inventory
                viewUserInventory();
                break;
            case "4": // view last transaction
                viewRecentTrades();
                break;
            case "5": // view top 3 most frequent trading partners
                viewTradingPartners();
                break;
            case "exit":
                return;
            default:
                presenter.tryAgain();
        }
    }

    /**
     * View the Inventory (all items in the system except the ones owned by the user)
     */
    public void viewInventory(){
        presenter.printSystemInventory(tradeModel);
    }

    /**
     * View user wishlist
     */
    public void viewWishlist(){
        presenter.printUserWishlist(tradeModel, username);
    }

    /**
     * View user inventory
     */
    public void viewUserInventory() {
        List<String> items = getItemsInfo(tradeModel.getUserManager().getSetByUsername(username, ItemSets.INVENTORY));
        presenter.printUserInventory(items);
    }

    /**
     * View top numTradingPartners most frequent trading partners
     */
    public void viewTradingPartners(){
        List<String> tradingPartners = tradeModel.getTradeManager().getFrequentPartners(numTradingPartners, username);
        presenter.printViewTopTradingPartners(numTradingPartners, tradingPartners);
    }

    /**
     * View user's last numLastTrades trades
     */
    public void viewRecentTrades(){
        List<String> lastTrades = tradeModel.getTradeManager().getRecentItemsTraded(numLastTrades, username);
        presenter.printRecentTrades(numLastTrades, lastTrades);
    }

    private List<String> getItemsInfo(Collection<String> itemIds) {
        List <String> itemsInfo = new ArrayList<>();
        for (String itemId : itemIds) {
            itemsInfo.add(tradeModel.getItemManager().getItemInfo(itemId));
        }
        return itemsInfo;
    }


}
