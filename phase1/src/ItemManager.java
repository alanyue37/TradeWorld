import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager implements Serializable {
    private final Map<String, Item> confirmedItems;
    private final Map<String, Item> pendingItems;

    public ItemManager() {
        confirmedItems = new HashMap<>();
        pendingItems = new HashMap<>();
    }

    public boolean containsItem(String itemId) {
        return confirmedItems.containsKey(itemId) || pendingItems.containsKey(itemId);
    }

    public Item getConfirmedItem(String itemId) {
        return confirmedItems.get(itemId);
    }

    public List<Item> getConfirmedItems() {
        return (List<Item>) confirmedItems.values();
    }

    public Item getPendingItem(String itemId) {
        return pendingItems.get(itemId);
    }

    public List<Item> getPendingItems() {
        return (List<Item>) pendingItems.values();
    }

    public List<Item> getAvailableItems() {
        List<Item> items = new ArrayList();
        for (Item item : confirmedItems.values()) {
            if (item.isAvailable()) {
                items.add(item);
            }
        }
        return items;
    }

    public String getOwner(String itemId) {
        Item item = getConfirmedItem(itemId);
        if (item == null) {
            return null;
        }
        return item.getOwner();
    }

    public void addItem(String name, String owner, String description) {
        Item item = new Item(name, owner, description);
        pendingItems.put(item.getId(), item);
    }

    public boolean confirmItem(String itemId) {
        Item item = getPendingItem(itemId);
        if (item == null) {
            return false;
        }
        pendingItems.remove(itemId);
        item.setAvailable(true);
        confirmedItems.put(itemId, item);
        return true;
    }

    public boolean deleteItem(String itemId) {
        if (!containsItem(itemId)) {
            return false;
        }
        confirmedItems.remove(itemId);
        pendingItems.remove(itemId);
        return true;
    }

    public String ToString(Item item) {
        return item.getName() + ": " + item.getDescription();
    }
}
