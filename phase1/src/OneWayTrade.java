public class OneWayTrade extends Trade {
    private int itemId;
    private String giverUsername;
    private String receiverUsername;


    /**
     * @param type
     */
    public OneWayTrade(String type, String giver, String receiver, int itemId) {
        super(type);
        this.giverUsername = giver;
        this.receiverUsername = receiver;
        this.itemId = itemId;
    }

    public int getItemId() {
        return this.itemId;
    }

    public String getGiverUsername(){
        return this.giverUsername;
    }

    public String getReceiverUsername(){
        return this.receiverUsername;
    }
}

// do we need setters for the item, giver, and receiver?
