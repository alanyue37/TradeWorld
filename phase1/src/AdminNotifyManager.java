import java.util.HashMap;

/**
 * Manages notifications for the AdminUser. Uses the User and Item entities only.
 */
public class AdminNotifyManager {
    private final HashMap<String, User> adminUsersRequests;
    private final HashMap<String, Item> requestItemToBeAdded;

    /**
     * A constructor for class AdminNotifyManager.
     */
    public AdminNotifyManager() {
        adminUsersRequests = new HashMap<>();
        requestItemToBeAdded = new HashMap<>();
    }

    /**
     * @return a string which includes the email's of users that request to become an administrative user.
     */
    public String toStringAdminUsers() {
        String str = "";
        for (String adminUsersKey : this.adminUsersRequests.keySet()) {
            str = str.concat(adminUsersKey);
        } return str;
    }

    /**
     * Allows users to make request to become an AdminUser.
     * @param email is the email address of the user.
     * @param user is the user itself.
     */
    public void makeRequest(String email, User user) {
        adminUsersRequests.put(email, user);
    }

    /**
     * A getter method for the users that request to become an administrative user.
     *  @return adminUserRequests.
     */
    public HashMap<String, User> getAdminUsersRequests() {
        return this.adminUsersRequests;
    }

    /**
     * Adds the email of the user and the item to be added by the AdminUser if the item can/ should
     * be added to the list. This is a way to prevent the users from putting in items such as cars and intangible items
     * in their inventories.
     * @param email is the email address of the user that wants to request to add the item.
     * @param item is the item that the user wants to put in its available lists (i.e., inventory).
     */
    public void requestToAddItem(String email, Item item) {
        requestItemToBeAdded.put(email, item);
    }

    /**
     *
     * @return a hashmap with a key representing the user's email and value representing the item to be added.
     */
    public HashMap<String, Item> getRequestItemToBeAdded() {
        return this.requestItemToBeAdded;
    }

    /**
     * @param item to be put on advertisement by the AdminUser
     * change the isAvailable variable from false to true.
     */
    public void changeAvailability(Item item) {
        if (!item.isAvailable()) {
            item.setAvailable(true);
        }
    }
}
