package undocomponent;

import usercomponent.UserManager;

import java.io.Serializable;

/**
 * An undoable operation representing a user adding an item to their wishlist.
 */
public class UndoAddWishlistItem implements UndoableOperation, Serializable {
    private final UserManager userManager;
    private final String username;
    private final String itemId;

    /**
     * Instantiates a new UndoAddWishlistItem operation.
     * @param userManager reference to UserManager reference
     * @param username username of user who added item
     * @param itemId id of item added
     */
    public UndoAddWishlistItem(UserManager userManager, String username, String itemId) {
        this.userManager = userManager;
        this.username = username;
        this.itemId = itemId;
    }

    /**
     * Deletes item with itemId from wishlist of user with username.
     * Does not throw NoLongerUndoableException if item is no longer present in wishlist.
     */
    @Override
    public void undo() {
        userManager.removeFromWishlist(username, itemId);
    }

    @Override
    public String toString() {
        return "Item with id " + itemId + " was added to wishlist by " + username;
    }
}
