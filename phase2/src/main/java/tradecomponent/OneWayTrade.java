package tradecomponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a one way trade, where one user gives an item to another user
 */
class OneWayTrade extends Trade {
    private final String itemId;
    private final String giverUsername;
    private final String receiverUsername;

    /**
     * @param type the type (temporary or permanent) of this Trade
     * @param tradeId the ID of this Trade
     * @param giver the user who is giving the item in this Trade
     * @param receiver the user who is receiving the item in this Trade
     * @param itemId the ID of the item being traded
     */
    protected OneWayTrade(String type, String tradeId, String giver, String receiver, String itemId) {
        super(type, tradeId);
        this.giverUsername = giver;
        this.receiverUsername = receiver;
        this.itemId = itemId;
    }

    /**
     * Gets users involved in the one way trade
     * @return list of users in trade
     */
    @Override
    protected List<String> getUsers() {
        List<String> users = new ArrayList<>();
        users.add(giverUsername);
        users.add(receiverUsername);
        return users;
    }

    /**
     * Gets items involved in the trade
     * @return list of items in trade
     */
    @Override
    protected List<String> getItems(){
        List<String> items = new ArrayList<>();
        items.add(this.itemId);
        return items;
    }

    /**
     * Returns a map of item ID mapped to a list of usernames of users who gave the item and who received the item
     * @return map of item ID mapped to a list of the giver and receiver
     */
    @Override
    protected Map<String, List<String>> itemToTrader(){
        Map<String, List<String>> userToItem = new HashMap<>();
        List<String> users = new ArrayList<>();
        users.add(giverUsername);
        users.add(receiverUsername);
        userToItem.put(this.itemId, users);
        return userToItem;
    }
}