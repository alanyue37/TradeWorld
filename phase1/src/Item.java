import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Item implements Serializable {
    private final String id;
    private String name;
    private String owner;
    private String description;
    private boolean available;

    // Static Variables -- // TODO: THIS IS NOT SERIALIZED.
    private static AtomicInteger counter = new AtomicInteger(); // starts at 0

    public Item(String name, String owner, String description) {
        this.id = String.valueOf(counter.getAndIncrement());
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.available = false; // initially set to false pending approval by admin
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String toString() {
        return "Item ID: " + id + "\nName: " + name + "\nOwner: " + owner + "\nDescription: " + description + "\nAvailable: " + available;
    }
}
