package tradegateway;

import itemcomponent.ItemManager;
import tradecomponent.TradeManager;
import usercomponent.UserManager;

import java.io.*;

/**
 * Holder class for the managers.
 */
public class TradeModel implements Serializable {

    private UserManager userManager;
    private ItemManager itemManager;
    private TradeManager tradeManager;

    /**
     * Creates a new tradegateway.TradeModel with existing managers.
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

    /**
     * Creates a new tradegateway.TradeModel with no existing managers.
     */
    public TradeModel() {
        userManager = new UserManager();
        itemManager = new ItemManager();
        tradeManager = new TradeManager();
    }

    /**
     * Set the UserManager for the tradegateway.TradeModel.
     * @param userManager the updated userManager
     */
    public void setUserManager(UserManager userManager) { this.userManager = userManager; }

    /**
     * Return the UserManager for the tradegateway.TradeModel.
     * @return userManager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Set the ItemManager for the tradegateway.TradeModel.
     * @param itemManager the updated itemManager
     */
    public void setItemManager(ItemManager itemManager) { this.itemManager = itemManager; }

    /**
     * Return the ItemManager for the tradegateway.TradeModel.
     * @return itemManager
     */
    public ItemManager getItemManager() {
        return itemManager;
    }

    /**
     * Set the TradeManager for the tradegateway.TradeModel.
     * @param tradeManager the updated userManager
     */
    public void setTradeManager(TradeManager tradeManager) { this.tradeManager = tradeManager; }

    /**
     * Return the TradeManager for the tradegateway.TradeModel.
     * @return tradeManager
     */
    public TradeManager getTradeManager() { return tradeManager; }

}
