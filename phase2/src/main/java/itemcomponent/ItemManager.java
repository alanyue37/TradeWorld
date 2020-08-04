package itemcomponent;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemManager implements Serializable {

    private final Map<String, Item> items;
    private final AtomicInteger counter = new AtomicInteger();

    /**
     * Instantiates an ItemManager
     */
    public ItemManager() {
        items = new HashMap<>();
    }

    /**
     * Returns the information of the item including its itemID, name, description, and its availability.
     * Returns null if the item doesn't exist.
     * @param itemId    The id of the item
     * @return  The information of the item in a string format
     */
    public String getItemInfo(String itemId) {
        if (!items.containsKey(itemId)) {
            return null;
        }
        return String.valueOf(items.get(itemId));
    }

    /**
     * Gets a set of item IDs that correspond to items in a particular stage
     * Precondition: This must be a valid stage for an item.
     *
     * @param stage The given stage to filter by
     * @return A set of item IDs that correspond to items in a particular stage
     */
    public Set<String> getItemsByStage(String stage) {
        HashSet<String> filteredItems = new HashSet<>();
        for (String id : items.keySet()) {
            if (items.get(id).getStage().equals(stage)) {
                filteredItems.add(id);
            }
        }
        return filteredItems;
    }

    /**
     * Gets a set of item IDs that correspond to items that are currently available to a user with a particular rank.
     * Note that all pending items are unavailable by default.
     *
     * @param rank The rank of some user
     * @return A set of item IDs that correspond to items that are currently available to a user with a particular rank
     */
    public Set<String> getAvailableItems(String rank) {
        HashSet<String> availableItems = new HashSet<>();
        if (rank.equals("gold")) {
            for (String id : items.keySet()) {
                if (items.get(id).isAvailable()) {
                    availableItems.add(id);
                }
            }
        }
        else {
            for (String id : items.keySet()) {
                if (items.get(id).isAvailable() && items.get(id).getStage().equals("common")) {
                    availableItems.add(id);
                }
            }
        }
        return availableItems;
    }

    /**
     * Returns the owner of the item
     * Precondition: An item with the given ID must exist.
     *
     * @param itemId    The id of the item
     * @return  The owner of the item
     */
    public String getOwner(String itemId) {
        Item item = items.get(itemId);
        return item.getOwner();
    }

    /**
     * Sets the owner for the item
     * Precondition: An item with the given ID must exist.
     *
     * @param itemId    The id of the item
     * @param username  The name of the owner
     */
    public void setOwner(String itemId, String username) {
        Item item = items.get(itemId);
        item.setOwner(username);
    }

    /**
     * Creates a new item and adds it to the current set of items
     * @param name  The name of the item
     * @param owner The owner of the item
     * @param description   The description of the item
     * @return  The id of the item
     */
    public String addItem(String name, String owner, String description) {
        String id = String.valueOf(counter.getAndIncrement());
        Item item = new Item(id, name, owner, description);
        items.put(item.getId(), item);
        return item.getId();
    }

    /**
     * Changes the stage of an item with the associated ID to "early" access and set it to available
     * Precondition: An item with the given ID must exist.
     *
     * @param itemId    The id of the item
     */
    public void confirmItem(String itemId) {
        Item item = items.get(itemId);
        item.setStage("early");
        item.setAvailable(true);
    }

    /**
     * Deletes the item from the system
     * Precondition: An item with the given ID must exist.
     *
     * @param itemId    The id of the item
     */
    public void deleteItem(String itemId) {
        items.remove(itemId);
    }

    /**
     * Sets the availability of an item
     * Precondition: An item with the given ID must exist.
     *
     * @param itemID    The id of the item
     * @param available     The availability of the item
     */
    public void setItemAvailable(String itemID, boolean available) {
        items.get(itemID).setAvailable(available);
    }

    /**
     * Updates all items in the "early" access stage so they are changed to "common" if it has been more than two days
     * since an item was added to the system
     */
    public void updateEarlyItems() {
        long twoDayMilliseconds = 172800000;
        Date today = new Date();
        long twoDaysAgo = today.getTime() - twoDayMilliseconds;
        for (Item item : items.values()) {
            if (item.getStage().equals("early") && item.getDateCreated().getTime() < twoDaysAgo) {
                item.setStage("common");
            }
        }
    }

}
