import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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
            boolean active = selectMenu();
            while (active) {
                active = selectMenu();
            }
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }
    /**
     * Main menu to run the UserController
     */
    private boolean selectMenu() throws IOException {
        presenter.startMenu(username);
        boolean validInput = false;
        do {
            String input = br.readLine();
            switch (input) {
                case "1": // add items to inventory
                    createItem();
                    validInput = true;
                    break;
                case "2": // add items to wishlist
                    viewItemsToAddToWishlist();
                    validInput = true;
                    break;
                case "3": // view inventory, wishlist
                    RunnableController vmc = new ViewingMenuController(tradeModel, username);
                    vmc.run();
                    validInput = true;
                    break;
                case "4": // view frequent trades, trading history, trade status
                    RunnableController vtc = new ViewingTradesController(tradeModel, username);
                    vtc.run();
                    validInput = true;
                    break;
                case "5": // initiate a trade
                    RunnableController controller = new InitiateTradeController(tradeModel, username);
                    controller.run();
                    validInput = true;
                    break;
                case "6": // manage proposed trades
                    RunnableController ptc = new ProposedTradesController(tradeModel, username);
                    ptc.run();
                    validInput = true;
                    break;
                case "7": // confirm a trade
                    RunnableController ctc = new ConfirmTradesController(tradeModel, username);
                    ctc.run();
                    validInput = true;
                    break;
                case "exit":
                    presenter.end();
                    return false;
                default:
                    presenter.tryAgain();
            }
        } while (!validInput);
        return true;
    }
    /**
     * Allows user to create an item
     */
    private void createItem() throws IOException{
        presenter.printInputItemName();             // enter name of the item
        String itemName = br.readLine();
        presenter.printInputItemDescription();      // enter description of the item
        String itemDescription = br.readLine();
        tradeModel.getItemManager().addItem(itemName, username, itemDescription);
    }
    /**
     * View system inventory (except items in current user's inventory or wishlist). Gives the user the option of adding items to their wishlist.
     */
    public void viewItemsToAddToWishlist() throws IOException{
        List<String> items = tradeModel.getItemManager().getConfirmedItems();
        Set<String> filterItems = tradeModel.getUserManager().getSetByUsername(username, ItemSets.INVENTORY);
        filterItems.addAll(tradeModel.getUserManager().getSetByUsername(username, ItemSets.WISHLIST));
        for (String itemId : filterItems) {
            items.remove(itemId);
        }
        presenter.printItemsToAddToWishlist(getItemsInfo(items));
        String choice = br.readLine();
        while (!items.contains(choice) && !choice.equals("back")) {
            presenter.tryAgain();
            choice = br.readLine();
        }
        if (items.contains(choice)) {
            tradeModel.getUserManager().addToSet(username, choice, ItemSets.WISHLIST);
        }
    }
    private List<String> getItemsInfo(Collection<String> itemIds) {
        List <String> itemsInfo = new ArrayList<>();
        for (String itemId : itemIds) {
            itemsInfo.add(tradeModel.getItemManager().getItemInfo(itemId));
        }
        return itemsInfo;
    }
}