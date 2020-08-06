package usercomponent;

import java.util.HashSet;
import java.util.Set;

class TradingUser extends User {

    private final Set<String> wishlist;
    private boolean frozen;
    private int credit;
    private String city;
    private boolean vacation;
    private boolean privacy;
    private Set<String> friends;
    private Set<String> pendingFriends;

    /**
     * Instantiates a new TradingUser
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

    protected String getCity() {
        return city;
    }

    @Override
    protected boolean isAdmin() {
        return false;
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

    protected boolean isOnVacation(){
        return this.vacation;
    }

    protected void setVacation(boolean vacation){
        this.vacation = vacation;
    }

    protected boolean isPrivate(){
        return this.privacy;
    }

    protected void setPrivacy(boolean privacy){
        this.privacy = privacy;
    }

    protected Set<String> getFriends(){
        return this.friends;
    }

    protected Set<String> getPendingFriends(){
        return this.pendingFriends;
    }

    protected void addToFriends(String username){
        this.friends.add(username);
    }

    protected void removeFromFriends(String username){
        this.friends.remove(username);
    }

    protected void addToPendingFriends(String username){
        this.pendingFriends.add(username);
    }

    protected void removeFromPendingFriends(String username){
        this.pendingFriends.remove(username);
    }



}