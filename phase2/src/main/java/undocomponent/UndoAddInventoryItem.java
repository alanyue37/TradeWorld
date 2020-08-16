package undocomponent;

import itemcomponent.ItemManager;
import tradecomponent.TradeManager;

import java.io.Serializable;

/**
 * An undoable operation representing a user adding an item to their inventory.
 */
public class UndoAddInventoryItem implements UndoableOperation, Serializable {
    private final ItemManager itemManager;
    private final TradeManager tradeManager;
    private final String itemId;

    /**
     * Instantiates a new UndoAddInventoryItem operation.
     * @param itemManager reference to ItemManager instance
     * @param tradeManager reference to TradeManager instance
     * @param itemId id of item added
     */
    public UndoAddInventoryItem(ItemManager itemManager, TradeManager tradeManager, String itemId) {
        this.itemManager = itemManager;
        this.tradeManager = tradeManager;
        this.itemId = itemId;
    }

    /**
     * Deletes item with itemId already confirmed by admin and all proposed trades which involve itemId.
     * @throws NoLongerUndoableException if the item is currently part of an active trade
     */
    @Override
    public void undo() throws NoLongerUndoableException {
        itemManager.deleteItem(itemId); // Delete item from inventory
        tradeManager.deleteCommonItemTrades(itemId); // Delete proposed trades (not yet confirmed) involving itemId
    }

    @Override
    public String toString() {
        return "Item with id " + itemId + " was confirmed by admin";
    }
}
