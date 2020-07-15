import java.io.Serializable;


public class Item implements Serializable {
    private final String id;
    private String name;
    private String owner;
    private String description;
    private boolean available;

    public Item(String id, String name, String owner, String description) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.available = false; // initially set to false pending approval by admin
    }

    /**
     *
     * @return  The id of the item
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return  The name of the item
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return  The owner of the item
     */
    public String getOwner() {
        return owner;
    }

    /**
     *
     * @return  The description of the item
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return  Whether is item is avaiable or not
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     *
     * @param name  The new name of the item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param owner The new owner of the item
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     *
     * @param description   The new description of the item
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @param available Change the availability of the item
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     *
     * @return  The string in identifying the itemId, name, description, and its availability.
     */
    public String toString() {
        return "Item ID: " + id + "\nName: " + name + "\nOwner: " + owner + "\nDescription: " + description + "\nAvailable: " + available;
    }
}
