package usercomponent;

import java.util.Set;
import java.util.HashSet;

class TradingUser extends User {

    private final Set<String> inventory;
    private final Set<String> wishlist;
    private boolean frozen;
    private int credit;

    /**
     * Instantiates a new TradingUser
     *
     * @param name The given name
     * @param username The given username
     * @param password The given password
     */
    protected TradingUser(String name, String username, String password) {
        super(name, username, password, false);
        inventory = new HashSet<>();
        wishlist = new HashSet<>();
        frozen = false;
        credit = 0;
    }

    /**
     * Gets the inventory of this TradingUser
     *
     * @return The inventory of this TradingUser
     */
    protected Set<String> getInventory(){
        return inventory;
    }

    /**
     * Adds an item to the inventory of this TradingUser
     *
     * @param itemID The id of the item to be added
     */
    protected void addToInventory(String itemID){
        inventory.add(itemID);
    }

    /**
     * Removes an item from the inventory of this TradingUser
     *
     * @param itemID The id of the item to be removed
     */
    protected void removeFromInventory(String itemID){
        inventory.remove(itemID);
    }

    /**
     * Gets the wishlist of this TradingUser
     *
     * @return The wishlist of this TradingUser
     */
    protected Set<String> getWishlist(){
        return wishlist;
    }

    /**
     * Adds an item to the wishlist of this TradingUser
     *
     * @param itemID The id of the item to be added
     */
    protected void addToWishlist(String itemID){
        wishlist.add(itemID);
    }

    /**
     * Removes an item from the wishlist of this TradingUser
     *
     * @param itemID The id of the item to be removed
     */
    protected void removeFromWishlist(String itemID){
        wishlist.remove(itemID);
    }

    /**
     * Returns whether this TradingUser is currently frozen and prevented from making trades
     *
     * @return Whether this TradingUser is currently frozen
     */
    protected boolean isFrozen(){
        return frozen;
    }

    /**
     * Sets the frozen status of this TradingUser
     *
     * @param frozen Whether or not this TradingUser should be frozen
     */
    protected void setFrozen(boolean frozen){
        this.frozen = frozen;
    }

    /**
     * Gets the current credit standing of this TradingUser
     *
     * @return The current credit standing of this TradingUser
     */
    protected int getCredit(){
        return credit;
    }

    /**
     * Increments the current credit standing of this TradingUser by 1
     */
    protected void incrementCredit(){
        credit++;
    }

    /**
     * Decrements the current credit standing of this TradingUser by 1
     */
    protected void decrementCredit(){
        credit--;
    }

    public String toString() {
        String status;
        if (isFrozen()) {
            status = "Currently Frozen";
        }
        else {
            status = "In Good Standing";
        }
        return super.toString() + "\n" + status + "\n" + "Credit: " + getCredit();
    }

}