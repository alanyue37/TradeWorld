import java.util.HashMap;

/**
 * Manages notifications for the AdminUser.
 */
public class AdminNotifyManager {
    private final UserManager um;
    private final HashMap<String, User> adminUsersRequests;
    private HashMap<String, Item> requestItemToBeAdded;

    /**
     * A constructor for class AdminNotifyManager.
     * @param um which is UserManager.
     */
    public AdminNotifyManager(UserManager um) {
        this.um = um;
        adminUsersRequests = new HashMap<>();
    }

    /**
     * @param newAdminUserEmail is the user's email that is to become an admin.
     * @param newAdminUser is the user object.
     */
    public void addAdminMembers(String newAdminUserEmail, User newAdminUser) {
        HashMap<String, User> existingAdminUsers = um.getAdminUsers();
        existingAdminUsers.put(newAdminUserEmail, newAdminUser);
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
     * A getter method for the users that request to become an administrative user.
     *  @return adminUserRequests.
     */
    public HashMap<String, User> getAdminUsersRequests() {
        return this.adminUsersRequests;
    }

    /**
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
