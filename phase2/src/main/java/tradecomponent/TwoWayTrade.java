package tradecomponent;

import java.util.*;

/**
 * Represents a two way trade, where two users give an item and receive the other's item
 */
class TwoWayTrade extends Trade {
    private final String user1;
    private final String user2;
    private final String item1;
    private final String item2;

    /** Constructs a two way trade
     * @param type the type (temporary or permanent) of this Trade
     * @param tradeId the ID of this Trade
     * @param user1 the username of the first user of this Trade
     * @param user2 the username of the second user of this Trade
     * @param item1 the ID of the item that user1 will be trading
     * @param item2 the ID of the item that user2 will be trading
     */
    protected TwoWayTrade(String type, String tradeId, String user1, String user2, String item1, String item2) {
        super(type, tradeId);
        this.user1 = user1;
        this.user2 = user2;
        this.item1 = item1;
        this.item2 = item2;
    }

    /**
     * Gets users involved in the trade
     * @return list of users in trade
     */
    @Override
    protected List<String> getUsers() {
        List<String> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        return users;
    }

    /**
     * Gets items involved in the trade
     * @return list of items in trade
     */
    @Override
    protected List<String> getItems() {
        List<String> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        return items;
    }

    /**
     * Returns a map of item ID mapped to a list of usernames of users who gave the item and who received the item
     * @return map of item ID mapped to a list of the giver and receiver
     */
    @Override
    protected Map<String, List<String>> itemToTrader() {
        Map<String, List<String>> userToItem = new HashMap<>();
        List<String> item1user = Arrays.asList(user1, user2);
        List<String> item2user = Arrays.asList(user2, user1);
        userToItem.put(item1, item1user);
        userToItem.put(item2, item2user);
        return userToItem;
    }
}
