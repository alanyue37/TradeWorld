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

    public String getConfirmedItem(String itemId) {
        return String.valueOf(confirmedItems.get(itemId));
    }

    public List<String> getConfirmedItems() {
        List<String> items = new ArrayList<>();
        for (Item item : confirmedItems.values()) {
            items.add(String.valueOf(item));
        }
        return items;
    }

    public String getPendingItem(String itemId) {
        return String.valueOf(pendingItems.get(itemId));
    }

    public List<String> getPendingItems() {
        List<String> items = new ArrayList<>();
        for (Item item : pendingItems.values()) {
            items.add(String.valueOf(item));
        }
        return items;
    }

    public List<String> getAvailableItems() {
        List<String> items = new ArrayList<>();
        for (Item item : confirmedItems.values()) {
            if (item.isAvailable()) {
                items.add(String.valueOf(item));
            }
        }
        return items;
    }

    public String getOwner(String itemId) {
        Item item = confirmedItems.get(itemId);
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
        Item item = pendingItems.get(itemId);
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
