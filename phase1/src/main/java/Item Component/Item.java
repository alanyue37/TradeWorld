import java.io.Serializable;


class Item implements Serializable {
    private final String id;
    private String name;
    private String owner;
    private String description;
    private boolean available;

    /**
     * Initiates a new item
     * @param id    The id of the item
     * @param name  The name of the item
     * @param owner The owner of the item
     * @param description   The description of the item
     */
    protected Item(String id, String name, String owner, String description) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.available = false; // initially set to false pending approval by admin
    }

    /**
     * Returns the id of the item
     * @return  The id of the item
     */
    protected String getId() {
        return id;
    }

    /**
     * Returns the name of the item
     * @return  The name of the item
     */
    protected String getName() {
        return name;
    }

    /**
     * Returns the owner of the item
     * @return  The owner of the item
     */
    protected String getOwner() {
        return owner;
    }

    /**
     * Returns the description of the item
     * @return  The description of the item
     */
    protected String getDescription() {
        return description;
    }

    /**
     * Returns true if the item is available or false if the item is not available
     * @return  Whether the item is available or not
     */
    protected boolean isAvailable() {
        return available;
    }

    /**
     * Sets the name of the item
     * @param name  The new name of the item
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the owner for the item
     * @param owner The new owner of the item
     */
    protected void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Sets a description of the item
     * @param description   The new description of the item
     */
    protected void setDescription(String description) {
        this.description = description;
    }

    /**
     * Changes the availability of the item, setting it as either true or false
     * @param available The new availability status of the item
     */
    protected void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Returns a string which includes the itemId, name, description, and its availability
     * @return  The string in identifying the itemId, name, description, and its availability
     */
    public String toString() {
        return "Item ID: " + id + "\nName: " + name + "\nOwner: " + owner + "\nDescription: " + description + "\nAvailable: " + available;
    }
}
