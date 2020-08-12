package useradapters;

import tradeadapters.ConfirmTradesController;
import tradeadapters.InitiateTradeController;
import tradeadapters.ProposedTradesController;
import tradegateway.TradeModel;
import trademisc.RunnableController;
import undocomponent.UndoAddWishlistItem;
import undocomponent.UndoableOperation;
import viewingadapters.ViewingMenuController;
import viewingadapters.ViewingTradesController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class UserController2 {

    protected final TradeModel tradeModel;
    protected final String username;
    private List <String> itemsToShow;

    /**
     * Constructor for UserController.
     * @param tradeModel Model of the system.
     * @param username Username associated with the user interacting with the system.
     */
    public UserController2(TradeModel tradeModel, String username){
        this.tradeModel = tradeModel;
        this.username = username;
        itemsToShow = new ArrayList<>();
    }

//    /**
//     * The main selection menu when a user logs in.
//     */
//    protected boolean selectMenu() throws IOException {
//        boolean validInput = false;
//        do {
//            String input = br.readLine();
//            switch (input) {
//                case "1": // add items to inventory
////                    createItem();
//                    validInput = true;
//                    break;
//                case "2": // add items to wishlist
//                    viewItemsToAddToWishlist();
//                    validInput = true;
//                    break;
//                case "3": // view inventory, wishlist, search user's profile
//                    RunnableController vmc = new ViewingMenuController(tradeModel, username);
//                    vmc.run();
//                    validInput = true;
//                    break;
//                case "4": // view frequent trades, trading history, trade status, trade reviews
//                    RunnableController vtc = new ViewingTradesController(tradeModel, username);
//                    vtc.run();
//                    validInput = true;
//                    break;
//                case "5": // initiate a trade
//                    RunnableController controller = new InitiateTradeController(tradeModel, username);
//                    controller.run();
//                    validInput = true;
//                    break;
//                case "6": // manage proposed trades
//                    RunnableController ptc = new ProposedTradesController(tradeModel, username);
//                    ptc.run();
//                    validInput = true;
//                    break;
//                case "7": // confirm a trade
//                    RunnableController ctc = new ConfirmTradesController(tradeModel, username);
//                    ctc.run();
//                    validInput = true;
//                    break;
//                case "8":
//                    RunnableController profileC = new ProfileController(tradeModel, username);
//                    profileC.run();
//                    validInput = true;
//                    break;
//                case "exit":
//                    presenter.end();
//                    return false;
//                default:
//                    presenter.tryAgain();
//            }
//        } while (!validInput);
//        return true;
//    }

    /**
     * Allows user to create an item
     */
    protected void createItem(String username, String itemName, String itemDescription){
        tradeModel.getItemManager().addItem(itemName, username, itemDescription);
    }

    public boolean isFrozen() {
        return tradeModel.getUserManager().isFrozen(username);
    }

    public boolean addItemToWishlist(String itemID){
        return tradeModel.getUserManager().addToWishlist(username, itemID);
    }

    public void removeItemFromWishlist(String itemID) {
        tradeModel.getUserManager().removeFromWishlist(username, itemID);
    }

//    public boolean addItemsToWishlist(String choice){
//        while (!itemsToShow.contains(choice) && !choice.equals("back")) {
//            presenter.tryAgain();
//        }
//        if (itemsToShow.contains(choice)) {
//            tradeModel.getUserManager().addToWishlist(username, choice);
//            UndoableOperation undoableOperation = new UndoAddWishlistItem(tradeModel.getUserManager(), username, choice);
//            tradeModel.getUndoManager().add(undoableOperation);
//            return true;
//        }
//        return false;
//    }

//    protected List<String> getItemsInfo(Collection<String> itemIds) {
//        List <String> itemsInfo = new ArrayList<>();
//        for (String itemId : itemIds) {
//            itemsInfo.add(tradeModel.getItemManager().getItemInfo(itemId));
//        }
//        return itemsInfo;
//    }
}