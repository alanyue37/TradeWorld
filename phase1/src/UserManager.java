import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
     * Check a TradingUser's email and password on login.
     * @param email The submitted email.
     * @param password The submitted password.
     * @return Whether or not the username/password combination is valid.
     */
    public boolean trader_login(String email, String password) {
        User account = tradingUsers.get(email);
        return password.equals(account.getPassword());
    }

    /**
     * Check an administrative user's email and password on login.
     * @param email The submitted email.
     * @param password The submitted password.
     * @return Whether or not the username/password combination is valid.
     */
    public boolean admin_login(String email, String password) {
        User account = adminUsers.get(email);
        return password.equals(account.getPassword());
    }

    /**
     * Create a TradingUser and add it to the list of TradingUsers if the email is unique
     * @param name The name of the TradingUser
     * @param email The email of the TradingUser
     * @param password The password of the TradingUser
     * @return Whether or not the TradingUser was successfully added
     */
    public boolean createTradingUser(String name, String email, String password) {
        if (tradingUsers.containsKey(email)) {
            return false;
        }
        tradingUsers.put(email, new TradingUser(name, email, password));
        return true;
    }

    /**
     * Create an administrative user and add it to the list of administrative users if the email is unique
     * @param name The name of the administrative user
     * @param email The email of the administrative user
     * @param password The password of the administrative user
     * @return Whether or not the administrative user was successfully added
     */
    public boolean createAdminUser(String name, String email, String password) {
        if (adminUsers.containsKey(email)) {
            return false;
        }
        adminUsers.put(email, new User(name, email, password));
        return true;
    }

    /**
     * Gets a user's information given their email, or null if no matching user is found.
     * @param email The email of the user to look up.
     * @return A User object with the user's information, or null if no matching user is found.
     */
    public TradingUser getUserByEmail(String email){
        if (!tradingUsers.containsKey(email)) {
            return null;
        }
        return tradingUsers.get(email);
    }

    /**
     * Add the id of a particular item to a user's inventory
     * @param email The given email
     * @param item The given item
     */
    public void addToInventory(String email, Item item) {
        TradingUser account = tradingUsers.get(email);
        account.addToInventory(item.getId());
    }

    /**
     * Remove the id of a particular item in a user's inventory
     * @param email The given email
     * @param item The given item
     */
    public void removeFromInventory(String email, Item item) {
        TradingUser account = tradingUsers.get(email);
        account.removeFromInventory(item.getId());
    }

    /**
     * Freeze the account of a particular TradingUser
     * @param email The given email
     */
    public void freeze(String email) {
        TradingUser account = tradingUsers.get(email);
        account.setFrozen(true);
    }

    /**
     * Unfreeze the account of a particular TradingUser
     * @param email The given email
     */
    public void unfreeze(String email) {
        TradingUser account = tradingUsers.get(email);
        account.setFrozen(false);
    }

    /**
     * Gets a list of all users who are below the borrowing ratio.
     * @return A list of all users who are below the borrowing ratio.
     */
    public ArrayList<User> getUsersBelowThreshold(){
        ArrayList<User> result = new ArrayList<>();
        for (TradingUser trader : tradingUsers.values()){
            if (trader.getNumItemsBorrowed() - trader.getNumItemsLent() >= getThreshold()) {
                result.add(trader);
            }
        }
        return result;
    }

    /**
     * Adds a user to the unfreeze request queue.
     * @param email The email of the user requesting an unfreeze.
     */
    public void markUserForUnfreezing(String email){
        unfreezeRequests.add(email);
    }

    public void markUserForFreezing(String email) {
        freezeAccounts.add(email);
    }

    public HashSet<String> getUnfreezeRequests(HashSet<String> unfreezeRequests) {
        return unfreezeRequests;
    }

    public HashSet<String> getFreezeAccounts(HashSet<String> freezeAccounts) {
        return freezeAccounts;
    }

}
