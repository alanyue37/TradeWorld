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

    public ItemManager() {
        confirmedItems = new HashMap<>();
        pendingItems = new HashMap<>();
    }

    public boolean containsItem(String itemId) {
        return confirmedItems.containsKey(itemId) || pendingItems.containsKey(itemId);
    }

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
            items.add(item.getId());
        }
        return items;
    }

    public List<String> getAvailableItems() {
        List<String> items = new ArrayList<>();
        for (Item item : confirmedItems.values()) {
            if (item.isAvailable()) {
                items.add(item.getId());
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

    public void setOwner(String itemId, String username) {
        Item item = confirmedItems.get(itemId);
        if (item != null) {
            item.setOwner(username);
        }

    }

    public String addItem(String name, String owner, String description) {
        String id = String.valueOf(counter.getAndIncrement());
        Item item = new Item(id, name, owner, description);
        pendingItems.put(item.getId(), item);
        return item.getId();
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

    public boolean setConfirmedItemAvailable(String itemID, boolean available) {
        if (!confirmedItems.containsKey(itemID)) {
            return false;
        }
        confirmedItems.get(itemID).setAvailable(available);
        return true;
    }

}
