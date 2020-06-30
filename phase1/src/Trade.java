import java.util.ArrayList;

public abstract class Trade {
    private String type;  //temporary or permanent
    private int idOfTrade;
    private boolean IsOpened; //transaction is done (1 for permanent, 2 for temporary)
    private static int totalTrade;
    private ArrayList<Meeting> meeting; //think of something else better

    /** Construct trade given type (temporary or permanent)
     * @param type temporary or permanent trade
     */
    public Trade(String type) {
        this.type = type;
        this.idOfTrade = totalTrade + 1;
        this.IsOpened = false;
        totalTrade += 1;
        // implement meeting in constructor
    }

    /**
     * Getter for the type of the trade (permanent or temporary)
     * @return type of the trade: temporary or permanent
     */
    public String getTradeType() {
        return this.type;
    }
    // ask if we need setter for type of trade

    /**
     *
     * @return
     */
    public int getIdOfTrade() {
        return this.idOfTrade;
    }

    public boolean getIsOpened() {
        return this.IsOpened;
    }

    public void changeIsOpened() {
        this.IsOpened = false;
    }

    //do we need a setter? or automatically?

    // meeting getters**


// confirmation of meeting time/place for temporary vs. permanent
// opened/not opened variable or like getter with if(...) then(true/false)
}