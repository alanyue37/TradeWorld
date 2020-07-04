import java.util.HashMap;

/**
 * Manages notifications for the AdminUser.
 */
public class AdminNotifyManager {
    private UserManager um;
    private HashMap<String, User> adminUsersRequests;

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

}
