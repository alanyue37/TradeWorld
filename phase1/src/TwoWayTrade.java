import java.util.ArrayList;

public class TwoWayTrade extends Trade {
    private String user1;
    private String user2;
    private String item1;
    private String item2;

    /**
     * @param type the type (temporary or permanent) of this Trade
     * @param user1 the username of the first user of this Trade
     * @param user2 the username of the second user of this Trade
     * @param item1 the ID of the item that user1 will be trading
     * @param item2 the ID of the item that user2 will be trading
     */
    public TwoWayTrade(String type, String user1, String user2, String item1, String item2) {
        super(type);
        this.user1 = user1;
        this.user2 = user2;
        this.item1 = item1;
        this.item2 = item2;
    }

    public String getUser1() {
        return this.user1;
    }

    public String getUser2() {
        return this.user2;
    }

    public String getItem1() {
        return item1;
    }

    public String getItem2() {
        return item2;
    }

    /**
     * Getter for the item ID based on the user trading it
     * @param user the user who is trading an item
     * @return the item ID that the given user is trading
     */
    public String getItemOfUser(String user) {
        if (user.equals(this.user1)) {
            return this.item1;
        } else {
            return this.item2;
        }
    }

    /**
     * Getter for the user based on the item being trading
     * @param item the ID of the item being traded
     * @return user that is trading the given item
     */
    public String getUserofItem(String item) {
        if (item.equals(this.item1)) {
            return this.item1;
        } else {
            return this.item2;
        }
    }

    @Override
    public ArrayList<String> getUsers() {
        ArrayList<String> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        return users;
    }
}
