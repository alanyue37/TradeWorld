import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;


public class UserController implements RunnableController {
    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final UserPresenter presenter;
    private final String username;

    /**
     * Constructor for UserController.
     * @param tradeModel Model of the system.
     * @param username Username associated with the user interacting with the system.
     */
    public UserController(TradeModel tradeModel, String username){
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new UserPresenter();
    }

    /**
     * Main method to run the UserController
     */
    public void run() {
        try {
            selectMenu();
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }

    /**
     * Main menu to run the UserController
     */
    private boolean selectMenu() throws IOException {
        List<String> vaildOptions = Arrays.asList(new String[] {"1", "2", "3", "4", "5", "6", "exit"});
        presenter.startMenu();
        String input = br.readLine();
        while (!vaildOptions.contains(input)) {
            System.out.println("Invalid input. Please try again:");
            input = br.readLine();
        }
        switch(input) {
            case "1": // add items to inventory
                createItem();
                break;
            case "2": // add items to wishlist
                viewInventory();
                break;
            case "3": // view inventory, wishlist, and trading history
                RunnableController vmc = new ViewingMenuController(tradeModel, username);
                vmc.run();
                break;
            case "4": // initiate a trade
                RunnableController controller = new InitiateTradeController(tradeModel, username);
                controller.run();
                break;
            case "5": // manage proposed trades
                RunnableController ptc = new ProposedTradesController(tradeModel, username);
                ptc.run();
                break;
            case "6": // confirm a trade
                RunnableController ctc = new ConfirmTradesController(tradeModel, username);
                ctc.run();
                break;
            case "exit":
                return false;
            default:
                System.out.println("Please enter a valid input.");
        }
        return true;
    }

    /**
     * Allows user to create an item
     */
    private void createItem()throws IOException{
        presenter.printInputItemName();             // enter name of the item
        String itemName = br.readLine();
        presenter.printInputItemDescription();      // enter description of the item
        String itemDescription = br.readLine();
        tradeModel.getItemManager().addItem(itemName, username, itemDescription);
    }

    /**
     * View the Inventory. Gives the user the option of adding items to their wishlist
     */
    public void viewInventory() throws IOException{
        presenter.printInventory(tradeModel, username);
        presenter.printUserInventoryOptions();
        String choice = br.readLine();
        if (choice.equals("1")){
            presenter.printInputItemID();
            String itemId = br.readLine();
            tradeModel.getUserManager().addToSet(username, itemId, ItemSets.WISHLIST);
        }
    }
}