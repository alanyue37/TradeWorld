import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.io.Serializable;

enum userTypes {
    TRADING,
    ADMIN
}

enum itemSets {
    INVENTORY,
    WISHLIST
}

public class UserManager implements Serializable {

    private final HashMap<String, TradingUser> tradingUsers;
    private final HashMap<String, User> adminUsers;
    private final HashSet<String> unfreezeRequests;
    private final HashSet<String> freezeAccounts;
    private int threshold;

    /**
     * Instantiate a UserManager
     */
    public UserManager() {
        tradingUsers = new HashMap<>();
        adminUsers = new HashMap<>();
        unfreezeRequests = new HashSet<>();
        freezeAccounts = new HashSet<>();
    }

    /**
     * Return the allowed threshold for a TradingUser to continue trading
     * @return An int that represents the number of items a TradingUser must have lent more than they have borrowed
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Set the allowed threshold for a TradingUser to continue trading
     * @param threshold An int that represents the number of items a TradingUser must have lent more than they have
     *                  borrowed
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Check a User's username and password on login.
     * @param username The submitted username.
     * @param password The submitted password.
     * @return Whether or not the username/password combination is valid.
     */
    public boolean login(String username, String password, userTypes type) {
        User account = null;
        switch (type) {
            case TRADING:
                account = tradingUsers.get(username);
                break;
            case ADMIN:
                account = adminUsers.get(username);
                break;
        }
        return password.equals(account.getPassword());
    }

    /**
     * Create a TradingUser and add it to the set of TradingUsers if the username is unique
     * @param name The name of the TradingUser
     * @param username The username of the TradingUser
     * @param password The password of the TradingUser
     * @return Whether or not the TradingUser was successfully added
     */
    public boolean createTradingUser(String name, String username, String password) {
        if (tradingUsers.containsKey(username) | adminUsers.containsKey(username)) {
            return false;
        }
        tradingUsers.put(username, new TradingUser(name, username, password));
        return true;
    }

    /**
     * Create an administrative user and add it to the set of administrative users if the username is unique
     * @param name The name of the administrative user
     * @param username The username of the administrative user
     * @param password The password of the administrative user
     * @return Whether or not the administrative user was successfully added
     */
    public boolean createAdminUser(String name, String username, String password) {
        if (tradingUsers.containsKey(username) | adminUsers.containsKey(username)) {
            return false;
        }
        adminUsers.put(username, new User(name, username, password));
        return true;
    }

    /**
     * Set the password of a particular User
     * @param username The username of the chosen User
     * @param password The intended password
     * @param type The type of User
     */
    public void setPasswordByUsername(String username, String password, userTypes type) {
        User account = null;
        switch (type) {
            case TRADING:
                account = tradingUsers.get(username);
                break;
            case ADMIN:
                account = adminUsers.get(username);
                break;
        }
        account.setPassword(password);
    }

    /**
     * Updates a chosen TradingUser's credit attribute
     * @param increment Whether to increment or decrement
     */
    public void updateCreditByUsername(String username, boolean increment) {
        TradingUser account = tradingUsers.get(username);
        if(increment) {
            account.incrementCredit();
        }
        else {
            account.decrementCredit();
        }
    }

    /**
     * Get a particular set of item ids stored in a TradingUser
     * @param username The username of the chosen TradingUser
     * @param set The name of the requested set
     * @return The requested set
     */
    public HashSet<String> getSetByUsername(String username, itemSets set) {
        TradingUser account = tradingUsers.get(username);
        HashSet<String> requestedSet = null;
        switch (set) {
            case INVENTORY:
                requestedSet = account.getInventory();
                break;
            case WISHLIST:
                requestedSet = account.getWishlist();
                break;
        }
        return requestedSet;
    }

    /**
     * Add the id of a particular item to one of the sets stored in a TradingUser
     * @param username The username of the chosen TradingUser
     * @param id The id of the given item
     * @param set The name of the requested set
     */
    public void addToSet(String username, String id, itemSets set) {
        TradingUser account = tradingUsers.get(username);
        switch (set) {
            case INVENTORY:
                account.addToInventory(id);
                break;
            case WISHLIST:
                account.addToWishlist(id);
                break;
        }
    }

    /**
     * Remove the id of a particular item from one of the sets stored in a TradingUser
     * @param username The username of the chosen TradingUser
     * @param id The id of the given item
     * @param set The name of the requested set
     */
    public void removeFromSet(String username, String id, itemSets set) {
        TradingUser account = tradingUsers.get(username);
        switch (set) {
            case INVENTORY:
                account.removeFromInventory(id);
                break;
            case WISHLIST:
                account.removeFromWishlist(id);
                break;
        }
    }

    /**
     * Freeze or unfreeze the account of a particular TradingUser
     * @param username The given username
     * @param frozen Whether or not the intended account should be frozen or unfrozen
     */
    public void freeze(String username, boolean frozen) {
        TradingUser account = tradingUsers.get(username);
        account.setFrozen(frozen);
    }

    /**
     * Gets a list of all TradingUsers who are below the borrowing ratio and are not currently frozen
     * @return A list of all TradingUsers who are below the borrowing ratio and are not currently frozen
     */
    public ArrayList<User> getUsersForFreezing(){
        ArrayList<User> result = new ArrayList<>();
        for (TradingUser trader : tradingUsers.values()){
            if (trader.getCredit() >= getThreshold() && !trader.isFrozen()) {
                result.add(trader);
            }
        }
        return result;
    }

    /**
     * Adds a TradingUser to the unfreeze request queue.
     * @param username The username of the TradingUser requesting an unfreeze.
     */
    public void markUserForUnfreezing(String username){
        unfreezeRequests.add(username);
    }

    /**
     * Adds a user to freeze their account.
     * @param email is the email of the user.
     */
    public void markUserForFreezing(String email) {
        freezeAccounts.add(email);
    }

    /**
     * @return a hashset of strings that has all unfreezeRequests.
     */
    public HashSet<String> getUnfreezeRequests() {
        return this.unfreezeRequests;
    }

    /**
     * @return a hashset of strings that has all accounts that should be frozen.
     */
    public HashSet<String> getFreezeAccounts() {
        return this.freezeAccounts;
    }

    /**
     * Adds new Administrative Users to AdminUsers.
     * @param email is the email of the user.
     * @param user is the user itself.
     */
    public void addNewAdminUsers(String email, User user) {
        adminUsers.put(email, user);
    }

    /**
     * @return all the adminUsers.
     */
    public HashMap<String, User> getAdminUsers() {
        return this.adminUsers;
    }

    /**
     * @param email The users given email
     * @return a string version of the users inventory
     */
    public String getInventory(String email) {
        TradingUser account = tradingUsers.get(email);
        return String.valueOf(account.getInventory());
        // TODO: can we get the names of the items from the account
    }

    /**
     * @param email The users given email
     * @return a string version of the users wishlist
     */
    public String getWishlist(String email) {
        TradingUser account = tradingUsers.get(email);
        return String.valueOf(account.getWishlist());
        // TODO: can we get the names of the items from the account
    }
}
