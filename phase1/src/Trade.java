import java.util.ArrayList;
import java.util.Date;

public abstract class Trade {
    private String type;  // temporary or permanent
    private int idOfTrade;
    private boolean IsOpened; // transaction is done (1 meeting for permanent, 2 for temporary)
    private static int totalTrade;
    private ArrayList<Meeting> meeting;
    private Date creationDate;

    /** Construct trade given type (temporary or permanent)
     * @param type temporary or permanent trade
     */
    public Trade(String type) {
        this.type = type;
        this.idOfTrade = totalTrade + 1;
        this.IsOpened = true;
        totalTrade += 1;
        this.meeting = new ArrayList<Meeting>();
        this.creationDate = new Date();
    }

    /**
     * Getter for the type of the trade (permanent or temporary)
     * @return type of the trade: temporary or permanent
     */
    public String getTradeType() {
        return this.type;
    }

    /**
     * Getter for the ID of the trade.
     * @return the ID of the trade.
     */
    public int getIdOfTrade() {
        return this.idOfTrade;
    }

    /**
     * Getter for status of the trade: whether the trade is still opened.
     * @return for permanent trade, true if the meeting has occurred (and is confirmed). for temporary trade, true if
     * both meeting has occurred (and is confirmed)
     */
    public boolean getIsOpened() {
        return this.IsOpened;
    }

    /**
     * Change the IsOpened to false (close the trade)
     */
    public void changeIsOpened() {
        this.IsOpened = false;
    }

    /**
     * Getter for the total number of trades that has been created.
     * @return the number of total trades created
     */
    public int getTotalTrade(){
        return totalTrade;
    }

    /**
     * Getter for the meeting array list
     * @return list of meeting
     */
    public ArrayList<Meeting> getMeetingList(){
        return this.meeting;
    }

    /**
     * Add another meeting to the list of meeting (if temporary trade)
     * @param newMeeting second meeting time to return items (for temporary trade)
     */
    public void incrementMeetingList(Meeting newMeeting){
        this.meeting.add(newMeeting);
    }

    /**
     * Return the date of creation of the trade
     * @return creationDate (date the trade was created)
     */
    public Date getCreationDate(){
        return this.creationDate;
    }

    /**
     * Returns users involved in the trade, regardless of type
     * @return list of users in trade
     */
    public abstract ArrayList<String> getUsers();
}