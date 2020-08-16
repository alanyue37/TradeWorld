package tradecomponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Represents a trade to exchange item(s)
 */
abstract class Trade implements Serializable {
    private final String type;
    private final String idOfTrade;
    private boolean IsOpened;
    private List<String> meeting;
    private final Date creationDate;

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
     * Changes IsOpened to false (the trade is closed)
     */
    protected void changeIsOpened() {
        this.IsOpened = false;
    }

    /**
     * Gets a list of meeting(s)
     * @return list of meeting(s)
     */
    protected List<String> getMeetingList(){
        return this.meeting;
    }

    /**
     * Adds another meeting to the list of meetings (if temporary trade)
     * @param newMeeting second meeting to return items (for temporary trade)
     */
    protected void incrementMeetingList(String newMeeting){
        this.meeting.add(newMeeting);
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
     * Returns a JSON object of the trade which includes: the type of the trade, the status of the trade
     * (i.e., ongoing or completed), the number of meetings that have happened and/or been planned, the creation date,
     * and the users and items involved
     * @return JSON object of the trade
     */
    public JSONObject getTradeInfo() throws JSONException {
        JSONObject info = new JSONObject();
        info.put("Trade ID", idOfTrade);
        info.put("Type", type);
        if (this.IsOpened) {
            info.put("Status", "ongoing");
        } else {
            info.put("Status", "completed");
        }
        info.put("Number of meetings", String.valueOf(getMeetingList().size()));
        info.put("Creation Date", creationDate.toString());
        info.put ("Users involved", getUsers());
        info.put("Items involved", getItems());
        return info;
    }

    /**
     * Returns if the given item is part of the trade
     * @param itemId the ID of the given item
     * @return true if the item with the ID "itemId" is part of the trade, false if not
     */
    protected boolean containItem(String itemId){
        return getItems().contains(itemId);
    }
}