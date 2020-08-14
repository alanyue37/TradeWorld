package useradapters;

import tradeadapters.ConfirmTradesController;
import tradeadapters.InitiateTradeController;
import tradeadapters.ProposedTradesController;
import tradegateway.TradeModel;
import trademisc.RunnableController;
import undocomponent.UndoAddInventoryItem;
import undocomponent.UndoAddWishlistItem;
import undocomponent.UndoableOperation;
import viewingadapters.ViewingMenuController;
import viewingadapters.ViewingTradesController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class UserController {

    protected final TradeModel tradeModel;

    /**
     * Constructor for UserController.
     * @param tradeModel Model of the system.
     */
    public UserController(TradeModel tradeModel){
        this.tradeModel = tradeModel;
    }

    /**
     * Allows user to create an item
     */
    protected void createItem(String username, String itemName, String itemDescription){
        tradeModel.getItemManager().addItem(itemName, username, itemDescription);
    }

    public boolean addItemToWishlist(String itemID){
        boolean added = tradeModel.getUserManager().addToWishlist(tradeModel.getCurrentUser(), itemID);
        if (added) {
            UndoableOperation undoableOperation = new UndoAddWishlistItem(tradeModel.getUserManager(), tradeModel.getCurrentUser(), itemID);
            tradeModel.getUndoManager().add(undoableOperation);
        }
        return added;
    }

    public void removeItemFromWishlist(String itemID) {
        tradeModel.getUserManager().removeFromWishlist(tradeModel.getCurrentUser(), itemID);
    }

}