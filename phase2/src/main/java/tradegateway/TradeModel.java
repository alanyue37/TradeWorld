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
 * Holder class for the managers.
 */
public class TradeModel implements Serializable, ObservableDataModel {
//TODO: add javadocs
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
     * Creates a new tradegateway.TradeModel with existing managers.
     *
     * @param um the UserManager.
     * @param im the ItemManager.
     * @param tm the TradeManager.
     */
    public TradeModel(UserManager um, ItemManager im, TradeManager tm, MeetingManager mm, ReviewManager rm, UndoManager undomanager) {
        userManager = um;
        itemManager = im;
        tradeManager = tm;
        meetingManager = mm;
        reviewManager = rm;
        this.undoManager = undomanager;
        this.observers = new ArrayList<>();
        this.currentUser = null;
    }

    /**
     * Creates a new tradegateway.TradeModel with no existing managers.
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
     * Set the UserManager for the tradegateway.TradeModel.
     * @param userManager the updated userManager
     */
    public void setUserManager(UserManager userManager) { this.userManager = userManager; }

    /**
     * Return the UserManager for the tradegateway.TradeModel.
     * @return userManager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Set the ItemManager for the tradegateway.TradeModel.
     * @param itemManager the updated itemManager
     */
    public void setItemManager(ItemManager itemManager) { this.itemManager = itemManager; }

    /**
     * Return the ItemManager for the tradegateway.TradeModel.
     * @return itemManager
     */
    public ItemManager getItemManager() {
        return itemManager;
    }

    /**
     * Set the TradeManager for the tradegateway.TradeModel.
     * @param tradeManager the updated tradeManager
     */
    public void setTradeManager(TradeManager tradeManager) { this.tradeManager = tradeManager; }

    /**
     * Return the TradeManager for the tradegateway.TradeModel.
     * @return tradeManager
     */
    public TradeManager getTradeManager() { return tradeManager; }

    /**
     * Set the MeetingManager for the tradegateway.TradeModel.
     * @param meetingManager the updated meetingManager
     */
    public void setMeetingManager(MeetingManager meetingManager) { this.meetingManager = meetingManager; }

    /**
     * Return the MeetingManager for the tradegateway.TradeModel.
     * @return meetingManager
     */
    public MeetingManager getMeetingManager() { return meetingManager; }

    /**
     * Set the ReviewManager for the tradegateway.TradeModel.
     * @param reviewManager the updated reviewManager
     */
    public void setReviewManager(ReviewManager reviewManager) { this.reviewManager = reviewManager; }

    /**
     * Return the ReviewManager for the tradegateway.TradeModel.
     * @return reviewManager
     */
    public ReviewManager getReviewManager() { return reviewManager; }

    /**
     * Set the UndoManager for the tradegateway.TradeModel.
     * @param undoManager the new undoManager
     */
    public void setUndoManager(UndoManager undoManager) { this.undoManager = undoManager; }

    /**
     * Return the UndoManager for the tradegateway.TradeModel.
     * @return undoManager
     */
    public UndoManager getUndoManager() { return undoManager; }

    /**
     * Set the username of the user currently logged in.
     * @param currentUser the username of logged in user
     */
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Return the username of the user currently logged in.
     * @return undoManager
     */
    public String getCurrentUser() {
        return currentUser;
    }

    public boolean hasChanged() {
        return changed;
    }

    public void setChanged() {
        this.changed = true;
        notifyObservers();
    }

    private void notifyObservers() {
        if (hasChanged() && observers != null) {
            for (GUIObserver o : observers) {
                o.update();
            }
            changed = false;
        }
    }

    public void addObserver(GUIObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void clearObservers() {
        observers.clear();
    }

    public void clearState() {
        clearObservers();
        this.currentUser = null;
        this.changed = false;
    }
}
