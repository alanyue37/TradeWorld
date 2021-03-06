package itemcomponent;

import java.io.Serializable;
import java.util.Date;

class Item implements Serializable {
    private final String id;
    private final String name;
    private final String description;
    private String owner;
    private String stage;
    private boolean available;
    private final Date dateCreated;

    /**
     * Creates a new item with a given id, name, owner, and description.
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
        this.stage = "pending";
        this.available = false; // initially set to false pending approval by admin
        this.dateCreated = new Date();
    }

    /**
     * Returns the id of the item
     * @return the id of the item
     */
    protected String getId() {
        return id;
    }

    /**
     * Returns the name of the item
     * @return the name of the item
     */
    protected String getName() {
        return name;
    }

    /**
     * Returns the owner of the item
     * @return the owner of the item
     */
    protected String getOwner() {
        return owner;
    }

    /**
     * Returns the description of the item
     * @return the description of the item
     */
    protected String getDescription() {
        return description;
    }

    /**
     * Gets the current stage of this item
     * @return the current stage of this item
     */
    protected String getStage() {
        return stage;
    }

    /**
     * Returns true if the item is available or false if the item is not available
     * @return whether the item is available or not
     */
    protected boolean isAvailable() {
        return available;
    }

    /**
     * Gets when this item was created
     * @return when this item was created
     */
    protected Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets the owner for the item
     * @param owner The new owner of the item
     */
    protected void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Sets the current stage of this item
     * Precondition: A valid stage for an item must be given (pending, early, common).
     *
     * @param stage The new stage of the item
     */
    protected void setStage(String stage) {
        this.stage = stage;
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
     * @return  the string in identifying the itemId, name, description, and its availability
     */
    public String toString() {
        return "Item ID: " + id + "\nName: " + name + "\nOwner: " + owner + "\nDescription: " + description + "\nAvailable: " + available;
    }

}
