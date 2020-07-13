import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ViewingMenuController implements RunnableController {
    private final BufferedReader br;
    TradeModel tradeModel;
    ViewingMenuPresenter presenter;
    private String username;

    public ViewingMenuController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ViewingMenuPresenter();
    }

    @Override
    public void run() {
        try {
            presenter.startMenu();
            String input = br.readLine();
            while (!input.equals("exit")) {
                browseViewOptions();
            }
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }

    private void browseViewOptions() throws IOException {

        presenter.showViewingOptions();
        String input = br.readLine();
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
                viewLastThreeTrades();
                break;
            case "5": // view top 3 most frequent trading partners
                viewTradingPartners();
                break;
            case "exit":
                presenter.end();
                presenter.startMenu();
            default:
                presenter.invalidInput();
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
        presenter.printUserInventory(tradeModel, username);
    }

    /**
     * View top 3 most frequent trading partners
     */
    public void viewTradingPartners(){
        presenter.printViewTopTradingPartner(tradeModel.getTradeManager().getFrequentPartners(3,username));
    }

    /**
     * View user's last 3 trades
     */
    public void viewLastThreeTrades(){
        presenter.viewLastThreeTrades(tradeModel, username);
    }


}
