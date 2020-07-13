import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInitiateTradeController implements RunnableController {
    private final TradeModel tradeModel;
    private final UserInitiateTradePresenter presenter;
    private final BufferedReader br;
    private final String username;

    public UserInitiateTradeController(TradeModel tradeModel, String username) {
        this.tradeModel = tradeModel;
        this.presenter = new UserInitiateTradePresenter();
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.username = username;
    }

    @Override
    public void run() {

    }

    public String getItemIdChoice() throws IOException {
        // Show items available not owned by user
        //List<String> itemsAvailable = tradeModel.getItemManager().getAvailableItems();
        List<String> itemsAvailable = Arrays.asList(new String[] {"1", "2"});
        List <String> itemsToShow = new ArrayList<>();
        for (String itemId : itemsAvailable) {
            if (!tradeModel.getItemManager().getOwner(itemId).equals(username)) {
                itemsToShow.add(tradeModel.getItemManager().getItemInfo(itemId));
            }
        }
        presenter.availableItemsMenu(itemsToShow);
        String input = br.readLine();
        return input;
    }

    public static void main(String[] args) {
        UserInitiateTradeController c = new UserInitiateTradeController(new TradeModel(), "u1");
    }
}
