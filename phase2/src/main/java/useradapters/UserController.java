package useradapters;

import tradeadapters.ConfirmTradesController;
import tradeadapters.InitiateTradeController;
import tradeadapters.ProposedTradesController;
import tradegateway.TradeModel;
import trademisc.RunnableController;
import usercomponent.ItemSets;
import viewingadapters.ViewingMenuController;
import viewingadapters.ViewingTradesController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class UserController implements RunnableController {
    protected final BufferedReader br;
    protected final TradeModel tradeModel;
    protected final UserPresenter presenter;
    protected final String username;
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
     * trademisc.Main method to run the UserController
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
     * trademisc.Main menu to run the UserController
     */
    protected boolean selectMenu() throws IOException {
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
    protected void createItem() throws IOException{
        presenter.printInputItemName();             // enter name of the item
        String itemName = br.readLine();
        presenter.printInputItemDescription();      // enter description of the item
        String itemDescription = br.readLine();
        tradeModel.getItemManager().addItem(itemName, username, itemDescription);
    }
    /**
     * View system inventory of users in same city (except items in current user's inventory or wishlist).
     * Gives the user the option of adding items to their wishlist.
     *
     * @throws IOException if a problem occurs with reading in input
     */
    public void viewItemsToAddToWishlist() throws IOException {
        List<String> items = tradeModel.getItemManager().getConfirmedItems();
        List <String> itemsToShow = new ArrayList<>();
        Set<String> userInventory =  tradeModel.getUserManager().getSetByUsername(username, ItemSets.INVENTORY);
        Set<String> userWishlist = tradeModel.getUserManager().getSetByUsername(username, ItemSets.WISHLIST);

        for (String itemId : items) {
            String otherUsername = tradeModel.getItemManager().getOwner(itemId);
            String thisUserCity = tradeModel.getUserManager().getCityByUsername(username);
            String otherUserCity = tradeModel.getUserManager().getCityByUsername(otherUsername);
            if (!userInventory.contains(itemId) && !userWishlist.contains(itemId) && thisUserCity.equals(otherUserCity)) {
                itemsToShow.add(itemId);
            }
        }

        presenter.printItemsToAddToWishlist(getItemsInfo(itemsToShow));
        String choice = br.readLine();
        while (!itemsToShow.contains(choice) && !choice.equals("back")) {
            presenter.tryAgain();
            choice = br.readLine();
        }
        if (itemsToShow.contains(choice)) {
            tradeModel.getUserManager().addToSet(username, choice, ItemSets.WISHLIST);
        }
    }
    protected List<String> getItemsInfo(Collection<String> itemIds) {
        List <String> itemsInfo = new ArrayList<>();
        for (String itemId : itemIds) {
            itemsInfo.add(tradeModel.getItemManager().getItemInfo(itemId));
        }
        return itemsInfo;
    }
}