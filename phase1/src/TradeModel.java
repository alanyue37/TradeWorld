import java.io.*;

/**
 * Holder class for the managers.
 */
public class TradeModel implements Serializable {

    UserManager userManager;
    ItemManager itemManager;
    TradeManager tradeManager;

    /**
     * Creates a new TradeModel.
     *
     * @param um the UserManager.
     * @param im the ItemManager.
     * @param tm the TradeManager.
     */
    public TradeModel(UserManager um, ItemManager im, TradeManager tm) {
        userManager = um;
        itemManager = im;
        tradeManager = tm;
    }

    public void setUserManager(UserManager userManager) { this.userManager = userManager; }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setItemManager(ItemManager itemManager) { this.itemManager = itemManager; }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public void setTradeManager(TradeManager tradeManager) { this.tradeManager = tradeManager; }

    public TradeManager getTradeManager() { return tradeManager; }
}
