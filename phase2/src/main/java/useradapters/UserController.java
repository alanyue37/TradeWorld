package useradapters;

import tradegateway.TradeModel;
import undocomponent.UndoAddWishlistItem;
import undocomponent.UndoableOperation;

/**
 * A controller class that sends user input to use case classes and calls use case class methods.
 */
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
     * Allows the current user to create an item and add it to their inventory.
     *
     * @param itemName the name of the item
     * @param itemDescription the description of the item
     */
    protected void createItem(String itemName, String itemDescription){
        tradeModel.getItemManager().addItem(itemName, tradeModel.getCurrentUser(), itemDescription);
    }

    /**
     * Allows the current user to add the given item to their wishlist.
     *
     * @param itemID the item ID to add to the wishlist
     */
    protected void addItemToWishlist(String itemID){
        boolean added = tradeModel.getUserManager().addToWishlist(tradeModel.getCurrentUser(), itemID);
        if (added) {
            UndoableOperation undoableOperation = new UndoAddWishlistItem(tradeModel.getUserManager(), tradeModel.getCurrentUser(), itemID);
            tradeModel.getUndoManager().add(undoableOperation);
        }
    }

    /**
     * Allows the current user to remove the given item from their wishlist.
     *
     * @param itemID the item ID to remove from thewishlist
     */
    protected void removeItemFromWishlist(String itemID) {
        tradeModel.getUserManager().removeFromWishlist(tradeModel.getCurrentUser(), itemID);
    }

}