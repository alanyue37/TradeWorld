import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class ViewingMenuController implements RunnableController {
    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final ViewingMenuPresenter presenter;
    private final String username;

    public ViewingMenuController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ViewingMenuPresenter();
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
                case "1": // view all confirmed items in system
                    viewSystemInventory();
                    validInput = true;
                    break;
                case "2": // view your inventory
                    viewUserInventory();
                    validInput = true;
                    break;
                case "3": // view your wishlist
                    viewWishlist();
                    validInput = true;
                    break;
                case "exit":
                    return;
                default:
                    presenter.tryAgain();
            }
        } while (!validInput);
    }

    /**
     * View the system inventory (all confirmed items -- available and unavailable -- in the system)
     */
    public void viewSystemInventory(){
        List<String> confirmedItems = getItemsInfo(tradeModel.getItemManager().getConfirmedItems());
        presenter.printSystemInventory(confirmedItems);
    }

    /**
     * View user inventory
     */
    public void viewUserInventory() {
        List<String> items = getItemsInfo(tradeModel.getUserManager().getSetByUsername(username, ItemSets.INVENTORY));
        presenter.printUserInventory(items);
    }

    /**
     * View user wishlist
     */
    public void viewWishlist(){
        List<String> items =  getItemsInfo(tradeModel.getUserManager().getSetByUsername(username, ItemSets.WISHLIST));
        presenter.printUserWishlist(items);
    }

    private List<String> getItemsInfo(Collection<String> itemIds) {
        List <String> itemsInfo = new ArrayList<>();
        for (String itemId : itemIds) {
            itemsInfo.add(tradeModel.getItemManager().getItemInfo(itemId));
        }
        return itemsInfo;
    }


}
