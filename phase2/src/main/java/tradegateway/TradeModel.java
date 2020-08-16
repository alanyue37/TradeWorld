package tradegateway;

import itemcomponent.ItemManager;
import profilecomponent.ReviewManager;
import tradecomponent.MeetingManager;
import tradecomponent.TradeManager;
import undocomponent.UndoManager;
import usercomponent.UserManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Facade class that stores all the information relating to the Trading system.
 */
public class TradeModel implements Serializable, ObservableDataModel {

    private UserManager userManager;
    private ItemManager itemManager;
    private TradeManager tradeManager;
    private MeetingManager meetingManager;
    private ReviewManager reviewManager;
    private UndoManager undoManager;
    private String currentUser;
    private boolean changed;
    private final List<GUIObserver> observers;

    /**
     * Creates a new TradeModel with existing managers.
     *
     * @param um the UserManager
     * @param im the ItemManager
     * @param tm the TradeManager
     * @param mm the MeetingManager
     * @param rm the ReviewManager
     * @param undoManager the UndoManager
     */
    public TradeModel(UserManager um, ItemManager im, TradeManager tm, MeetingManager mm, ReviewManager rm, UndoManager undoManager) {
        userManager = um;
        itemManager = im;
        tradeManager = tm;
        meetingManager = mm;
        reviewManager = rm;
        this.undoManager = undoManager;
        this.observers = new ArrayList<>();
        this.currentUser = null;
    }

    /**
     * Creates a new TradeModel with no existing managers.
     */
    public TradeModel() {
        userManager = new UserManager(this);
        itemManager = new ItemManager(this);
        tradeManager = new TradeManager(this);
        meetingManager = new MeetingManager(this);
        reviewManager = new ReviewManager(this);
        this.undoManager = new UndoManager(this);
        this.observers = new ArrayList<>();
        this.currentUser = null;
    }

    /**
     * Sets the UserManager for this TradeModel.
     * @param userManager the updated UserManager
     */
    public void setUserManager(UserManager userManager) { this.userManager = userManager; }

    /**
     * Returns the UserManager for this TradeModel.
     * @return the UserManager for this TradeModel
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Sets the ItemManager for this TradeModel.
     * @param itemManager the updated ItemManager
     */
    public void setItemManager(ItemManager itemManager) { this.itemManager = itemManager; }

    /**
     * Returns the ItemManager for this TradeModel.
     * @return the ItemManager for this TradeModel.
     */
    public ItemManager getItemManager() {
        return itemManager;
    }

    /**
     * Sets the TradeManager for the TradeModel.
     * @param tradeManager the updated TradeManager
     */
    public void setTradeManager(TradeManager tradeManager) { this.tradeManager = tradeManager; }

    /**
     * Returns the TradeManager for this TradeModel.
     * @return the TradeManager for this TradeModel
     */
    public TradeManager getTradeManager() { return tradeManager; }

    /**
     * Sets the MeetingManager for this TradeModel.
     * @param meetingManager the updated MeetingManager
     */
    public void setMeetingManager(MeetingManager meetingManager) { this.meetingManager = meetingManager; }

    /**
     * Returns the MeetingManager for this TradeModel.
     * @return the MeetingManager for this TradeModel.
     */
    public MeetingManager getMeetingManager() { return meetingManager; }

    /**
     * Sets the ReviewManager for this TradeModel.
     * @param reviewManager the updated ReviewManager
     */
    public void setReviewManager(ReviewManager reviewManager) { this.reviewManager = reviewManager; }

    /**
     * Returns the ReviewManager for this TradeModel.
     * @return the ReviewManager for this TradeModel
     */
    public ReviewManager getReviewManager() { return reviewManager; }

    /**
     * Set the UndoManager for this TradeModel.
     * @param undoManager the updated UndoManager
     */
    public void setUndoManager(UndoManager undoManager) { this.undoManager = undoManager; }

    /**
     * Returns the UndoManager for this TradeModel.
     * @return the UndoManager for this TradeModel
     */
    public UndoManager getUndoManager() { return undoManager; }

    /**
     * Sets currentUser to the username of the user currently logged in.
     * @param currentUser the username of logged in user
     */
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Gets the username of the user currently logged in.
     * @return the username of the current user
     */
    public String getCurrentUser() {
        return currentUser;
    }

    /**
     * Gets whether the TradeModel has changed.
     * @return whether Trademodel has changed
     */
    public boolean hasChanged() {
        return changed;
    }

    /**
     * Sets changed to true and notifies the observers.
     */
    public void setChanged() {
        this.changed = true;
        notifyObservers();
    }

    /**
     * Notifies the observers that a change may have happened.
     */
    private void notifyObservers() {
        if (hasChanged() && observers != null) {
            for (GUIObserver o : observers) {
                o.update();
            }
            changed = false;
        }
    }

    /**
     * Adds a GUIObserver to the list of observers.
     * @param observer the GUIObserver to add
     */
    public void addObserver(GUIObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes all observers.
     */
    public void clearObservers() {
        observers.clear();
    }

    /**
     * Clears this TradeModel of the current user and observers.
     * Used when a user logs out.
     */
    public void clearState() {
        clearObservers();
        this.currentUser = null;
        this.changed = false;
    }
}
