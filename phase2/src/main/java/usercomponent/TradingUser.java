package usercomponent;

import java.util.HashSet;
import java.util.Set;

/**
 * The entity class that represents a trading user in the trading system.
 */
class TradingUser extends User {

    private final Set<String> wishlist;
    private boolean frozen;
    private int credit;
    private final String city;
    private boolean vacation;
    private boolean privacy;
    private final Set<String> friends;
    private final Set<String> pendingFriends;

    /**
     * Instantiates a new TradingUser.
     *
     * @param name The given name
     * @param username The given username
     * @param password The given password
     * @param city     The given city
     */
    protected TradingUser(String name, String username, String password, String city) {
        super(name, username, password);
        wishlist = new HashSet<>();
        frozen = false;
        credit = 0;
        this.city = city;
        this.vacation = false;
        this.privacy = false;
        this.friends = new HashSet<>();
        this.pendingFriends = new HashSet<>();
    }

    /**
     * Gets the wishlist of this TradingUser.
     *
     * @return the wishlist of this TradingUser
     */
    protected Set<String> getWishlist(){
        return wishlist;
    }

    /**
     * Adds an item to the wishlist of this TradingUser.
     *
     * @param itemID the id of the item to be added
     */
    protected void addToWishlist(String itemID){
        wishlist.add(itemID);
    }

    /**
     * Removes an item from the wishlist of this TradingUser.
     *
     * @param itemID the id of the item to be removed
     */
    protected void removeFromWishlist(String itemID){
        wishlist.remove(itemID);
    }

    /**
     * Returns whether this TradingUser is currently frozen and prevented from making trades.
     *
     * @return whether this TradingUser is currently frozen
     */
    protected boolean isFrozen(){
        return frozen;
    }

    /**
     * Sets the frozen status of this TradingUser.
     *
     * @param frozen whether or not this TradingUser should be frozen
     */
    protected void setFrozen(boolean frozen){
        this.frozen = frozen;
    }

    /**
     * Gets the current credit standing of this TradingUser.
     *
     * @return the current credit standing of this TradingUser
     */
    protected int getCredit(){
        return credit;
    }

    /**
     * Increments the current credit standing of this TradingUser by one.
     */
    protected void incrementCredit(){
        credit++;
    }

    /**
     * Decrements the current credit standing of this TradingUser by one.
     */
    protected void decrementCredit(){
        credit--;
    }

    /**
     * Returns the city of this TradingUser.
     * @return the current city of this TradingUser
     */
    protected String getCity() {
        return city;
    }

    /**
     * Returns whether this TradingUser is an admin.
     * @return whether this TradingUser is an admin
     */
    @Override
    protected boolean isAdmin() {
        return false;
    }

    /**
     * Returns the string representation of this TradingUser with their current status and credit.
     * @return the string representation of this TradingUser
     */
    @Override
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

    /**
     * Returns whether this TradingUser is on vacation mode.
     * @return true iff this user is on vacation, otherwise false
     */
    protected boolean isOnVacation(){
        return this.vacation;
    }

    /**
     * Sets the vacation mode of this TradingUser.
     * @param vacation whether or not this TradingUser is on vacation
     */
    protected void setVacation(boolean vacation){
        this.vacation = vacation;
    }

    /**
     * Returns whether this TradingUser is a private account.
     * @return true iff this TradingUser's account is private, otherwise false
     */
    protected boolean isPrivate(){
        return this.privacy;
    }

    /**
     * Sets the privacy of this TradingUser.
     * @param privacy whether or not this TradingUser's account is private
     */
    protected void setPrivacy(boolean privacy){
        this.privacy = privacy;
    }

    /**
     * Returns the set of friends of this TradingUser.
     * @return the set of friends of this TradingUser
     */
    protected Set<String> getFriends(){
        return this.friends;
    }

    /**
     * Returns the set of friend requests of this TradingUser.
     * @return the set of friend requests of this TradingUser
     */
    protected Set<String> getPendingFriends(){
        return this.pendingFriends;
    }

    /**
     * Adds a user to a given TradingUser's list of friends.
     * @param username username of TradingUser to add to the friend list
     */
    protected void addToFriends(String username){
        this.friends.add(username);
    }

    /**
     * Remove a user to given TradingUser.
     * @param username username of TradingUser to remove from the friend list
     */
    protected void removeFromFriends(String username){
        this.friends.remove(username);
    }

    /**
     * Adds a user to a given TradingUser's pending friend list.
     * @param username username of TradingUser to add to pending friend list
     */
    protected void addToPendingFriends(String username){
        this.pendingFriends.add(username);
    }

    /**
     * Removes a user to a given TradingUser's pending friend list.
     * @param username username of TradingUser to remove from pending friend list
     */
    protected void removeFromPendingFriends(String username){
        this.pendingFriends.remove(username);
    }
}