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
     * Instantiates an ItemManager
     */
    public ItemManager() {
        confirmedItems = new HashMap<>();
        pendingItems = new HashMap<>();
    }

    /**
     * Returns true if the item already exists in the system (confirmed or pending), false otherwise
     * @param itemId    The id of the item
     * @return  Whether this item exists already
     */
    public boolean containsItem(String itemId) {
        return confirmedItems.containsKey(itemId) || pendingItems.containsKey(itemId);
    }

    /**
     * Returns the information of the item including its itemID, name, description, and its availability.
     * Returns null if the item doesn't exist.
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
     * Returns a list of strings consisting of all the ids of items that have been confirmed by admin.
     * @return A list of string consisting of all of itemIds that have been confirmed by admin
     */
    public List<String> getConfirmedItems() {
        List<String> items = new ArrayList<>();
        for (Item item : confirmedItems.values()) {
            items.add(item.getId());
        }
        return items;
    }

    /**
     * Returns a list of strings consisting of all the ids of items that are pending confirmation by admin.
     * @return A list of string consisting of all of itemIds that are pending confirmation by admin
     */
    public List<String> getPendingItems() {
        List<String> items = new ArrayList<>();
        for (Item item : pendingItems.values()) {
            items.add(item.getId());
        }
        return items;
    }

    /**
     * Returns a list of strings of itemIds of confirmed items that are available. Note that all pending items are
     * unavailable by default.
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
     * Returns the owner of the item if the item exists and has been confirmed, null otherwise.
     * @param itemId    The id of the item
     * @return  The owner of the item if the item exists and has been confirmed
     */
    public String getOwner(String itemId) {
        Item item = confirmedItems.get(itemId);
        if (item == null) {
            return null;
        }
        return item.getOwner();
    }

    /**
     * Sets the owner for the item if the item exists and has been confirmed.
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
     * Adds the item to the list of items pending confirmation by an admin
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
     * Marks an item as confirmed if it exists and is pending confirmation by admin and returns true.
     * Otherwise returns false.
     * @param itemId    The id of the item
     * @return  True iff the item is deleted from the list of items pending confirmation by admin and was moved to the
     * list of confirmed items
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
     * Deletes the item from the system if it exists and returns true. Otherwise returns false.
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
     * Sets the availability of an item that exists and has been confirmed and returns true. It cannot change the
     * availability of items pending confirmation by admin.
     * Returns false for pending items and items which do not exist.
     * @param itemID    The id of the item
     * @param available     The availability of the item
     * @return  True iff the confirmed item was found and its availability was set to available.
     */
    public boolean setConfirmedItemAvailable(String itemID, boolean available) {
        if (!confirmedItems.containsKey(itemID)) {
            return false;
        }
        confirmedItems.get(itemID).setAvailable(available);
        return true;
    }

}
