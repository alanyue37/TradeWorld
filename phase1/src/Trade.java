import java.util.HashMap;

public abstract class Trade {
    private String type;  //temporary or permanent
    private int numOfEdits;
    private int idOfTrade;
    private boolean IsCompleted; // transaction is done (permanent: once, temporary: twice)
    private int numConfirmations; // once they set up the meeting, if they confirmed
    private static int totalTrade;
    private HashMap<String, String> meeting; //think of something else better

    /**
     * @param type
     */
    public Trade(String type) {
        this.numOfEdits = 0;
        this.type = type;
        this.idOfTrade = totalTrade + 1;
        this.IsCompleted = false;
        this.numConfirmations = 0;
        totalTrade += 1;
        this.meeting = new HashMap<String, String>(); //come back to this
    }

    public String getTradeType() {
        return this.type;
    }
    // ask if we need setter for type of trade

    public HashMap<String, String> getMeetingInfo() {
        return this.meeting;
    }

    public void setMeetingInfo(HashMap<String, String> newMeetingInfo) {
        this.meeting = newMeetingInfo;
    }

    public int getNumOfEdits() {
        return this.numOfEdits;
    }

    public void incrementNumOfEdits() {
        this.numOfEdits += 1;
    }

    public int getIdOfTrade() {
        return this.idOfTrade;
    }

    public boolean getIsCompleted() {
        return this.IsCompleted;
    }

    public void setIsCompleted(boolean newIsCompleted) {
        this.IsCompleted = newIsCompleted;
    }

    public int getNumConfirmations() {
        return this.numConfirmations;
    }

    public void incrementNumConfirmations() {
        this.numConfirmations++;
    }

// confirmation of meeting time/place for temporary vs. permanent
}