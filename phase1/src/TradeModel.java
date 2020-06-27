import java.io.*;

/**
 * Holder class for the managers.
 */
public class TradeModel implements Serializable {

    UserManager userManager;
    AdminManager adminManager;
    ItemManager itemManager;
    TradeManager tradeManager;

    /**
     * Creates a new TradeModel.
     *
     * @param um the UserManager.
     * @param am the AdminManager.
     * @param im the ItemManager.
     * @param tm the TradeManager.
     */
    public TradeModel(UserManager um, AdminManager am, ItemManager im, TradeManager tm) {
        userManager = um;
        adminManager = am;
        itemManager = im;
        tradeManager = tm;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setAdminManager(AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    public AdminManager getAdminManager() {
        return adminManager;
    }

    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public void setTradeManager(TradeManager tradeManager) {
        this.tradeManager = tradeManager;
    }

    public TradeManager getTradeManager() {
        return tradeManager;
    }
}
