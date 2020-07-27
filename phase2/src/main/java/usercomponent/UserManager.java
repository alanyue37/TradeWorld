package usercomponent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserManager implements Serializable {

    private final Map<String, TradingUser> tradingUsers;
    private final Map<String, User> adminUsers;
    private final Set<String> unfreezeRequests;
    private int threshold;

    /**
     * Instantiates a UserManager
     */
    public UserManager() {
        tradingUsers = new HashMap<>();
        adminUsers = new HashMap<>();
        unfreezeRequests = new HashSet<>();
        User initialAdmin = new User("Initial Admin", "ia", "initialize");
        adminUsers.put("ia", initialAdmin);
    }

    /**
     * Returns the allowed threshold for a TradingUser to continue trading
     *
     * @return An int that represents the number of items a TradingUser must have lent more than they have borrowed
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Sets the allowed threshold for a TradingUser to continue trading
     *
     * @param threshold An int that represents the number of items a TradingUser must have lent more than they have
     *                  borrowed
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Checks a User's username and password on login.
     *
     * @param username The submitted username.
     * @param password The submitted password.
     * @param type The type of user
     * @return Whether or not the username/password combination is valid.
     */
    public boolean login(String username, String password, UserTypes type) {
        User account = null;
        switch (type) {
            case TRADING:
                account = tradingUsers.get(username);
                break;
            case ADMIN:
                account = adminUsers.get(username);
                break;
        }
        if (account == null){
            return false; // account username not found
        }
        return password.equals(account.getPassword());
    }

    /**
     * Creates a TradingUser and adds it to the set of TradingUsers if the username is unique
     *
     * @param name     The name of the TradingUser
     * @param username The username of the TradingUser
     * @param password The password of the TradingUser
     * @param city     The city of thr TradingUser
     * @return Whether or not the TradingUser was successfully added
     */
    public boolean createTradingUser(String name, String username, String password, String city) {
        if (tradingUsers.containsKey(username) | adminUsers.containsKey(username)) {
            return false;
        }
        tradingUsers.put(username, new TradingUser(name, username, password, city));
        return true;
    }

    /**
     * Creates an administrative user and adds it to the set of administrative users if the username is unique
     *
     * @param name     The name of the administrative user
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
     * Sets the password of a particular User
     *
     * @param username The username of the chosen User. Must be a valid username for an existing User.
     * @param password The intended password
     * @param type     The type of User
     */
    public void setPasswordByUsername(String username, String password, UserTypes type) {
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
     *
     * @param username The username of the chosen TradingUser. Must be a valid username for an existing TradingUser.
     * @param increment Whether to increment or decrement
     */
    public void updateCreditByUsername(String username, boolean increment) {
        TradingUser account = tradingUsers.get(username);
        if (increment) {
            account.incrementCredit();
        } else {
            account.decrementCredit();
        }
    }

    /**
     * Gets a chosen TradingUser's credit attribute.
     * @param username The username of the chosen TradingUser. Must be a valid username for an existing TradingUser.
     * @return the TradingUser's credit value.
     */
    public int getCreditByUsername(String username) {
        TradingUser account = tradingUsers.get(username);
        return account.getCredit();
    }

    /**
     * Gets a particular set of item ids stored in a TradingUser
     *
     * @param username The username of the chosen TradingUser
     * @param set      The name of the requested set
     * @return The requested set
     */
    public Set<String> getSetByUsername(String username, ItemSets set) {
        TradingUser account = tradingUsers.get(username);
        Set<String> requestedSet = null;
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
     * Adds the id of a particular item to one of the sets stored in a TradingUser
     *
     * @param username The username of the chosen TradingUser. Must be a valid username for an existing TradingUser.
     * @param id       The id of the given item
     * @param set      The name of the requested set
     */
    public void addToSet(String username, String id, ItemSets set) {
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
     * Removes the id of a particular item from one of the sets stored in a TradingUser
     *
     * @param username The username of the chosen TradingUser. Must be a valid username for an existing TradingUser.
     * @param id       The id of the given item
     * @param set      The name of the requested set
     */
    public void removeFromSet(String username, String id, ItemSets set) {
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
     * Freezes or unfreezes the account of a particular TradingUser and removes them from the set of TradingUsers who've
     * requested to be unfrozen if it's the latter
     *
     * @param username The username of the TradingUser involved. Must be a valid username for an existing TradingUser.
     * @param frozen   Whether or not the intended account should be frozen or unfrozen
     */
    public void setFrozen(String username, boolean frozen) {
        TradingUser account = tradingUsers.get(username);
        account.setFrozen(frozen);
        if (!frozen) {
            unfreezeRequests.remove(username);
        }
    }

    /**
     * Gets a set of the usernames of TradingUsers who are below the borrowing ratio and are not currently frozen
     *
     * @return A set of the usernames of TradingUsers who are below the borrowing ratio and are not currently frozen
     */
    public Set<String> getUsersForFreezing() {
        Set<String> result = new HashSet<>();
        for (TradingUser trader : tradingUsers.values()) {
            if (trader.getCredit() < getThreshold() && !trader.isFrozen()) {
                result.add(trader.getUsername());
            }
        }
        return result;
    }

    /**
     * Gets the set of all TradingUsers who have requested to be unfrozen
     *
     * @return The set of all TradingUsers who have requested to be unfrozen
     */
    public Set<String> getUnfreezeRequests() {
        return unfreezeRequests;
    }

    /**
     * Adds a TradingUser to the unfreeze request queue.
     *
     * @param username The username of the TradingUser requesting an unfreeze. Must be a valid username for an existing
     *                 TradingUser.
     */
    public void markUserForUnfreezing(String username) {
        unfreezeRequests.add(username);
    }

    /**
     * Returns whether a particular TradingUser is currently frozen
     *
     * @param username The username of the TradingUser checking their status. Must be a valid username for an existing
     *                 TradingUser.
     * @return Whether the TradingUser is currently frozen
     */
    public boolean isFrozen(String username) {
        TradingUser account = tradingUsers.get(username);
        return account.isFrozen();
    }

    /**
     * Returns the city of a given TradingUser
     * @param username The username of the given TradingUser.
     */
    public String getCityByUsername(String username) {
        TradingUser account = tradingUsers.get(username);
        return account.getCity();

    }

    /**
     * Sets or unset the account on vacation mode.
     * @param username The username of the given TradingUser.
     * @param vacation Whether or not the intended account is set to vacation mode.
     */
    public void setOnVacation(String username, boolean vacation) {
        TradingUser account = tradingUsers.get(username);
        account.setVacation(vacation);
    }

    /**
     * Gets a set of the usernames of TradingUsers who are on vacation mode.
     * @return A set of the usernames of TradingUsers who are on vacation mode.
     */
    public Set<String> getOnVacation(){
        Set<String> result = new HashSet<>();
        for (TradingUser trader: tradingUsers.values()){
            if (trader.isOnVacation()){
                result.add(trader.getUsername());
            }
        }
        return result;
    }

}