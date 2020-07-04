import java.util.HashSet;

/**
 * Manages all notifications and send to the AdminUser
 */
public class AdminNotifyManager {
    private UserManager um = new UserManager();
    private HashSet<User> adminUsersRequests;
    private HashSet<User> adminUsersConfirmed;

    /**
     * Contructor for AdminNotifyManager
     * @param um which is UserManager
     */
    public AdminNotifyManager(UserManager um) {
        this.um = um;
    }

    public void addOtherAdminMembers() {
        adminUsersConfirmed.addAll(adminUsersRequests);
    }

    public String adminUserToString() {
        String str = "";
        for (User admins : adminUsersRequests) {
            str = str + admins.toString();
        } return str;
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




    /// should I use methods from UserManager or these?
    /**
     * @return a hashset of regularUsers
     */
    public HashSet<String> notifyUnfreezeRequests() {
        return um.getUnfreezeRequests();
    }

    /**
     * @return a hashset of regularAccounts that have to get frozen
     */
    public HashSet<String> notifyToFreezeAccounts() {
        return um.getFreezeAccounts();
    }
}
