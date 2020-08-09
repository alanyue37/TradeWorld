package undocomponent;

import usercomponent.UserManager;

public class UndoAddWishlistItem implements UndoableOperation {
    private final UserManager userManager;
    private final String username;
    private final String itemId;

    /**
     * Instantiates UndoAddWishlistItem
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
     * Deletes item with itemId from wishlist of user with username
     * Does not throw NoLongerUndoableException if item is no longer present in wishlist.
     */
    @Override
    public void undo() throws NoLongerUndoableException {
        userManager.removeFromWishlist(username, itemId);
    }

    @Override
    public String toString() {
        return "Item with id " + itemId + " was added to wishlist by " + username;
    }
}
