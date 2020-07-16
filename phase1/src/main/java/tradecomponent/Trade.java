package tradecomponent;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a trade to exchange item(s)
 */
abstract class Trade implements Serializable {
    private String type;
    private String idOfTrade;
    private boolean IsOpened;
    private List<Meeting> meeting;
    private Date creationDate;

    /** Constructs trade given the type (temporary or permanent)
     * @param type temporary or permanent trade
     * @param id the ID of the trade
     */
    protected Trade(String type, String id) {
        this.type = type;
        this.idOfTrade = id;
        this.IsOpened = true;
        this.meeting = new ArrayList<>();
        this.creationDate = new Date();
    }

    /**
     * Gets the type of the trade (permanent or temporary)
     * @return type of the trade: permanent or temporary
     */
    protected String getTradeType() {
        return this.type;
    }

    /**
     * Gets the ID of the trade
     * @return the ID of the trade
     */
    protected String getIdOfTrade() {
        return this.idOfTrade;
    }

    /**
     * Gets status of the trade: whether the trade is opened or closed
     * @return for permanent trade, false if the meeting has occurred and been confirmed; for temporary trade, false if
     * both meetings have occurred and been confirmed
     */
    protected boolean getIsOpened() {
        return this.IsOpened;
    }

    /**
     * Changes IsOpened to false (the trade is closed)
     */
    protected void changeIsOpened() {
        this.IsOpened = false;
    }

    /**
     * Gets a list of meeting(s)
     * @return list of meeting(s)
     */
    protected List<Meeting> getMeetingList(){
        return this.meeting;
    }

    /**
     * Adds another meeting to the list of meetings (if temporary trade)
     * @param newMeeting second meeting to return items (for temporary trade)
     */
    protected void incrementMeetingList(Meeting newMeeting){
        this.meeting.add(newMeeting);
    }

    /**
     * Gets the date of creation of the trade
     * @return creationDate (date the trade was created)
     */
    protected Date getCreationDate(){
        return this.creationDate;
    }

    /**
     * Gets users involved in the trade
     * @return list of users in trade
     */
    protected abstract List<String> getUsers();

    /**
     * Gets items involved in the trade
     * @return list of items in trade
     */
    protected abstract List<String> getItems();

    /**
     * Returns a map of item ID mapped to a list of usernames of users who gave the item and who received the item
     * @return map of item ID mapped to a list of the giver and receiver
     */
    protected abstract Map<String, List<String>> itemToTrader();

    /**
     * Returns a String representation of the trade which includes: the type of the trade, the status of the trade
     * (i.e., ongoing or completed), the number of meetings that have happened and/or been planned, the creation date,
     * and the users and items involved
     * @return String representation of the trade
     */
    public String toString(){
        String status;
        if (this.IsOpened){
            status = "ongoing";
        } else{
            status = "completed";
        }
        return "Trade ID: " + this.idOfTrade +"\nType: " + this.type + "\nStatus: " + status + "\nNumber of meetings: " + this.getMeetingList().size() +
                "\nCreation Date: " + this.creationDate.toString() + "\nUsers involved: " + getUsers() + "\nItems involved: " + getItems();
    }

    /**
     * Returns if the given item is part of the trade
     * @return true if the item with the ID "itemId" is part of the trade, false if not
     */
    protected boolean containItem(String itemId){
        return getItems().contains(itemId);
    }
}