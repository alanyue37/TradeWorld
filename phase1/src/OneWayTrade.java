import java.util.ArrayList;

public class OneWayTrade extends Trade {
    private String itemId;
    private String giverUsername;
    private String receiverUsername;


    /**
     * @param type the type (temporary or permanent) of this Trade
     * @param giver the user who is giving the item in this Trade
     * @param receiver the user who is receiving the item in this Trade
     * @param itemId the ID of the item being traded
     */
    public OneWayTrade(String type, String giver, String receiver, String itemId) {
        super(type);
        this.giverUsername = giver;
        this.receiverUsername = receiver;
        this.itemId = itemId;
    }

    public String getItemId() {
        return this.itemId;
    }

    public String getGiverUsername(){
        return this.giverUsername;
    }

    public String getReceiverUsername(){
        return this.receiverUsername;
    }

    @Override
    public ArrayList<String> getUsers() {
        ArrayList<String> users = new ArrayList<>();
        users.add(giverUsername);
        users.add(receiverUsername);
        return users;
    }
}

// do we need setters for the item, giver, and receiver?
