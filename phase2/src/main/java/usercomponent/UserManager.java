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
    private int tradingThreshold;
    private int goldThreshold;
    private int silverThreshold;

    /**
     * Instantiates a UserManager
     */
    public UserManager() {
        tradingUsers = new HashMap<>();
        adminUsers = new HashMap<>();
        unfreezeRequests = new HashSet<>();
        tradingThreshold = 0;
        silverThreshold = 3;
        goldThreshold = 5;
        User initialAdmin = new User("Initial Admin", "ia", "initialize", true);
        adminUsers.put("ia", initialAdmin);
    }

    /**
     * Returns the specified threshold value
     * Precondition: The requested threshold must be valid.
     *
     * @param which threshold is being requested
     * @return The requested threshold value
     */
    public int getThreshold(String which) {
        if (which.equals("gold")) {
            return goldThreshold;
        }
        else if (which.equals("silver")) {
            return silverThreshold;
        }
        else {
            return tradingThreshold;
        }
    }

    /**
     * Sets the specified threshold value
     * Precondition: The requested threshold must be valid.
     *
     * @param which threshold is being requested
     * @param value The new value
     */
    public void setThreshold(String which, int value) {
        if (which.equals("gold")) {
            goldThreshold = value;
        }
        else if (which.equals("silver")) {
            silverThreshold = value;
        }
        else {
            tradingThreshold = value;
        }
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
        adminUsers.put(username, new User(name, username, password, true));
        return true;
    }

    private HashMap<String, User> getAllUsers() {
        HashMap<String, User> allUsers = new HashMap<>();
        allUsers.putAll(tradingUsers);
        allUsers.putAll(adminUsers);
        return allUsers;
    }

    /**
     * Returns whether a particular user has admin capabilities
     *
     * @param username The username of the chosen user.
     * @return Whether the User is an admin
     */
    public boolean isAdmin(String username) {
        HashMap<String, User> allUsers = getAllUsers();
        User account = allUsers.get(username);
        if (account == null){
            return false; // account username not found
        }
        return account.isAdmin();
    }

    /**
     * Checks a User's username and password on login.
     *
     * @param username The submitted username.
     * @param password The submitted password.
     * @return Whether or not the username/password combination is valid.
     */
    public boolean login(String username, String password) {
        HashMap<String, User> allUsers = getAllUsers();
        User account = allUsers.get(username);
        if (account == null){
            return false; // account username not found
        }
        return password.equals(account.getPassword());
    }

    /**
     * Sets the password of a particular User
     *
     * @param username The username of the chosen User. Must be a valid username for an existing User.
     * @param password The intended password
     */
    public void setPasswordByUsername(String username, String password) {
        HashMap<String, User> allUsers = getAllUsers();
        User account = allUsers.get(username);
        account.setPassword(password);
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
     * Gets the current rank of a particular TradingUser
     * Precondition: A TradingUser with the specified username must exist.
     *
     * @param username The username of the chosen TradingUser
     * @return The user's rank
     */
    public String getRankByUsername(String username) {
        TradingUser account = tradingUsers.get(username);
        if (account.getCredit() >= goldThreshold) {
            return "gold";
        }
        else if (account.getCredit() >= silverThreshold) {
            return "silver";
        }
        else {
            return "bronze";
        }
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
            if (trader.getCredit() < getThreshold("trading") && !trader.isFrozen()) {
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

    public boolean containsTradingUser(String username){
        return this.tradingUsers.containsKey(username);
    }

}