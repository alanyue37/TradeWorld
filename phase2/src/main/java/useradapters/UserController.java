package useradapters;

import tradegateway.TradeModel;
import undocomponent.UndoAddWishlistItem;
import undocomponent.UndoableOperation;

// TODO: javadoc
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

    public void addItemToWishlist(String itemID){
        boolean added = tradeModel.getUserManager().addToWishlist(tradeModel.getCurrentUser(), itemID);
        if (added) {
            UndoableOperation undoableOperation = new UndoAddWishlistItem(tradeModel.getUserManager(), tradeModel.getCurrentUser(), itemID);
            tradeModel.getUndoManager().add(undoableOperation);
        }
    }

    public void removeItemFromWishlist(String itemID) {
        tradeModel.getUserManager().removeFromWishlist(tradeModel.getCurrentUser(), itemID);
    }

}