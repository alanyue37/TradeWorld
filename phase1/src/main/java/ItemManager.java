import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemManager implements Serializable {
    private final Map<String, Item> confirmedItems;
    private final Map<String, Item> pendingItems;
    private final AtomicInteger counter = new AtomicInteger();

    /**
     * Instantiates a ItemManager
     */
    public ItemManager() {
        confirmedItems = new HashMap<>();
        pendingItems = new HashMap<>();
    }

    /**
     * Returns true if the item already exists in confirmedItems or pendingItem, false otherwise
     * @param itemId    The id of the item
     * @return  Whether this item exists already
     */
    public boolean containsItem(String itemId) {
        return confirmedItems.containsKey(itemId) || pendingItems.containsKey(itemId);
    }

    /**
     * Returns the information of the item including its itemID, name, description, and its availability
     * @param itemId    The id of the item
     * @return  The information of the item in a string format
     */
    public String getItemInfo(String itemId) {
        if (!containsItem(itemId)) {
            return null;
        }
        else if (confirmedItems.containsKey(itemId)) {
            return String.valueOf(confirmedItems.get(itemId));
        }
        else {
            return String.valueOf(pendingItems.get(itemId));
        }
    }

    /**
     * Returns the item which exists in the confirmedItems
     * @param itemId    The id of the item
     * @return  The item in a string format
     */
    public String getConfirmedItem(String itemId) {
        return String.valueOf(confirmedItems.get(itemId));
    }

    /**
     * Returns a list of strings consisting of all the ids of items that are in confirmedItems
     * @return A list of string consisting of all of itemIds in the confirmedItems
     */
    public List<String> getConfirmedItems() {
        List<String> items = new ArrayList<>();
        for (Item item : confirmedItems.values()) {
            items.add(item.getId());
        }
        return items;
    }

    /**
     * Returns the information of the item including its itemID, name, description, and its availability in the
     * pendingItems
     * @param itemId    The id of the item
     * @return  The information of the item in a string format
     */
    public String getPendingItem(String itemId) {
        return String.valueOf(pendingItems.get(itemId));
    }

    /**
     * Returns a list of strings consisting of all the ids of items that are in pendingItems
     * @return A list of string consisting of all of itemIds in the pendingItems
     */
    public List<String> getPendingItems() {
        List<String> items = new ArrayList<>();
        for (Item item : pendingItems.values()) {
            items.add(item.getId());
        }
        return items;
    }

    /**
     * Returns a list of strings of itemIds from the confirmItems that are available
     * @return  A list of strings of itemIds that are available
     */
    public List<String> getAvailableItems() {
        List<String> items = new ArrayList<>();
        for (Item item : confirmedItems.values()) {
            if (item.isAvailable()) {
                items.add(item.getId());
            }
        }
        return items;
    }

    /**
     * Returns the owner of the item if the item is in confirmedItems, null otherwise
     * @param itemId    The id of the item
     * @return  The owner of the item if the item is in confirmedItems
     */
    public String getOwner(String itemId) {
        Item item = confirmedItems.get(itemId);
        if (item == null) {
            return null;
        }
        return item.getOwner();
    }

    /**
     * Sets the owner for the item if the item exists in confirmedItems
     * @param itemId    The id of the item
     * @param username  The name of the owner
     */
    public void setOwner(String itemId, String username) {
        Item item = confirmedItems.get(itemId);
        if (item != null) {
            item.setOwner(username);
        }

    }

    /**
     * Adds the item to the list of pendingItems to get the item approved by a Admin user
     * @param name  The name of the item
     * @param owner The owner of the item
     * @param description   The description of the item
     * @return  The id of the item
     */
    public String addItem(String name, String owner, String description) {
        String id = String.valueOf(counter.getAndIncrement());
        Item item = new Item(id, name, owner, description);
        pendingItems.put(item.getId(), item);
        return item.getId();
    }

    /**
     * Returns true iff the item is deleted from the pendingItems and was placed in the confirmedItems
     * Returns false when the item is not found in the pendingItems
     * @param itemId    The id of the item
     * @return  True iff the item is deleted from the pendingItems and was placed in the confirmedItems
     */
    public boolean confirmItem(String itemId) {
        Item item = pendingItems.get(itemId);
        if (item == null) {
            return false;
        }
        pendingItems.remove(itemId);
        item.setAvailable(true);
        confirmedItems.put(itemId, item);
        return true;
    }

    /**
     * Returns false if the item is not in pendingItems or in confirmedItems
     * Returns true when the item is deleted from confirmedItems and pendingItems
     * @param itemId    The id of the item
     * @return  True if the item exists and is deleted, false otherwise
     */
    public boolean deleteItem(String itemId) {
        if (!containsItem(itemId)) {
            return false;
        }
        confirmedItems.remove(itemId);
        pendingItems.remove(itemId);
        return true;
    }

    /**
     * Returns true iff the item exists in confirmedItems and the availability is set to available
     * Returns false if the item is not in confirmedItems
     * @param itemID    The id of the item
     * @param available     The availability of the item
     * @return  True iff the item already exists in confirmedItems, false otherwise
     */
    public boolean setConfirmedItemAvailable(String itemID, boolean available) {
        if (!confirmedItems.containsKey(itemID)) {
            return false;
        }
        confirmedItems.get(itemID).setAvailable(available);
        return true;
    }

}
