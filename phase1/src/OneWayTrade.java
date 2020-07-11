import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a one way trade, where one user gives an item to another user
 */
public class OneWayTrade extends Trade {
    private String itemId;
    private String giverUsername;
    private String receiverUsername;


    /**
     * @param type the type (temporary or permanent) of this Trade
     * @param giver the user who is giving the item in this Trade
     * @param receiver the user who is receiving the item in this Trade
     * @param itemId the ID of the item being traded
     */
    public OneWayTrade(String type, String giver, String receiver, String itemId) {
        super(type);
        this.giverUsername = giver;
        this.receiverUsername = receiver;
        this.itemId = itemId;
    }

    /**
     * Gets the item ID of the item being traded
     * @return item ID of item being traded
     */
    public String getItemId() {
        return this.itemId;
    }

    /**
     * Gets the username of the user giving the item
     * @return username of the giver
     */
    public String getGiverUsername(){
        return this.giverUsername;
    }

    /**
     * Gets the username of the user receiving the item
     * @return username of the receiver
     */
    public String getReceiverUsername(){
        return this.receiverUsername;
    }

    /**
     * Gets users involved in the one way trade
     * @return list of users in trade
     */
    @Override
    public List<String> getUsers() {
        List<String> users = new ArrayList<>();
        users.add(giverUsername);
        users.add(receiverUsername);
        return users;
    }

    @Override
    public  List<String> getItems(){
        List<String> items = new ArrayList<>();
        items.add(this.itemId);
        return items;
    }

    @Override
    public Map<String, String> userToItem(){
        HashMap<String, String> userToItem = new HashMap<>();
        userToItem.put(giverUsername, itemId);
        userToItem.put(receiverUsername, null);
        return userToItem;
    }

}

// do we need setters for the item, giver, and receiver?
