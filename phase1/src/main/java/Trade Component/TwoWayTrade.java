import java.lang.reflect.Array;
import java.util.*;

/**
 * Represents a two way trade, where two users give an item and receive the other's item
 */
class TwoWayTrade extends Trade {
    private String user1;
    private String user2;
    private String item1;
    private String item2;

    /** Constructs a two way trade
     * @param type the type (temporary or permanent) of this Trade
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
     * Gets the username of the first user in the trade
     * @return username of first user
     */
    protected String getUser1() {
        return this.user1;
    }

    /**
     * Gets the username of the second user in the trade
     * @return username of second user
     */
    protected String getUser2() {
        return this.user2;
    }

    /**
     * Gets the item ID of the item being traded by user1
     * @return item ID of item being traded by user1
     */
    protected String getItem1() {
        return item1;
    }

    /**
     * Gets the item ID of the item being traded by user2
     * @return item ID of item being traded by user2
     */
    protected String getItem2() {
        return item2;
    }

    /**
     * Gets the item ID based on the user trading it
     * @param user the user who is trading an item
     * @return the item ID that the given user is trading
     */
    protected String getItemOfUser(String user) {
        if (user.equals(this.user1)) {
            return this.item1;
        } else {
            return this.item2;
        }
    }

    /**
     * Gets the user based on the item being trading
     * @param item the ID of the item being traded
     * @return user that is trading the given item
     */
    protected String getUserOfItem(String item) {
        if (item.equals(this.item1)) {
            return this.item1;
        } else {
            return this.item2;
        }
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
