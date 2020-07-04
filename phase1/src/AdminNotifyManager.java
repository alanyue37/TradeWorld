import java.util.HashSet;

/**
 * Manages all notifications and send to the AdminUser
 */
public class AdminNotifyManager {
    private UserManager um = new UserManager();

    /**
     * Contructor for AdminNotifyManager
     * @param um which is UserManager
     */
    public AdminNotifyManager(UserManager um) {
        this.um = um;
    }

    /**
     * @param item to be put on advertisement by the AdminUser
     * @return a string in the format "Name: Description"
     */
    public String putOnAD(Item item) {
        return item.getName() + ": " + item.getDescription();
    }

    /**
     *
     * @param unfreezeRequests of all regularUsers that want to be unfrozen
     * @return a hashset of regularUsers
     */
    public HashSet<String> notifyUnfreezeRequests(HashSet<String> unfreezeRequests) {
        return um.getUnfreezeRequests(unfreezeRequests);
    }

    /**
     * @param freezeAccounts for accounts that have to be frozen by the AdminUser
     * @return a hashset of regularAccounts that have to get frozen
     */
    public HashSet<String> notifyToFreezeAccounts(HashSet<String> freezeAccounts) {
        return um.getFreezeAccounts(freezeAccounts);
    }
}
