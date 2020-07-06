import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents a trade to exchange item(s)
 */
public abstract class Trade {
    private String type;
    private int idOfTrade;
    private boolean IsOpened;
    private static int totalTrade;
    private ArrayList<Meeting> meeting;
    private Date creationDate;


    /** Constructs trade given the type (temporary or permanent)
     * @param type temporary or permanent trade
     */
    public Trade(String type) {
        this.type = type;
        this.idOfTrade = totalTrade + 1;
        this.IsOpened = true;
        totalTrade += 1;
        this.meeting = new ArrayList<>();
        this.creationDate = new Date();
    }

    /**
     * Gets the type of the trade (permanent or temporary)
     * @return type of the trade: temporary or permanent
     */
    public String getTradeType() {
        return this.type;
    }

    /**
     * Gets the ID of the trade
     * @return the ID of the trade
     */
    public int getIdOfTrade() {
        return this.idOfTrade;
    }

    /**
     * Gets status of the trade: whether the trade is opened or closed
     * @return for permanent trade, false if the meeting has occurred (and is confirmed); for temporary trade, false if
     * both meetings have occurred (and is confirmed)
     */
    public boolean getIsOpened() {
        return this.IsOpened;
    }

    /**
     * Changes IsOpened to false (close the trade)
     */
    public void changeIsOpened() {
        this.IsOpened = false;
    }

    /**
     * Gets the total number of trades that has been created
     * @return the number of total trades created
     */
    public int getTotalTrade(){
        return totalTrade;
    }

    /**
     * Gets the array list of meeting(s)
     * @return list of meeting(s)
     */
    public ArrayList<Meeting> getMeetingList(){
        return this.meeting;
    }

    /**
     * Adds another meeting to the list of meeting (if temporary trade)
     * @param newMeeting second meeting to return items (for temporary trade)
     */
    public void incrementMeetingList(Meeting newMeeting){
        this.meeting.add(newMeeting);
    }

    /**
     * Gets the date of creation of the trade
     * @return creationDate (date the trade was created)
     */
    public Date getCreationDate(){
        return this.creationDate;
    }

    /**
     * Gets users involved in the trade
     * @return list of users in trade
     */
    public abstract ArrayList<String> getUsers();

    public abstract ArrayList<String> getItems();

    public abstract HashMap<String, String> userToItem();
}