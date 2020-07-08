import java.util.HashSet;

public class TradingUser extends User {

    private final HashSet<String> inventory;
    private final HashSet<String> wishlist;
    private boolean frozen;
    private int credit;

    public TradingUser(String name, String username, String password) {
        super(name, username, password);
        inventory = new HashSet<>();
        wishlist = new HashSet<>();
        frozen = false;
        credit = 0;
    }

    public HashSet<String> getInventory(){
        return inventory;
    }

    public void addToInventory(String itemID){
        inventory.add(itemID);
    }

    public void removeFromInventory(String itemID){
        inventory.remove(itemID);
    }

    public HashSet<String> getWishlist(){
        return wishlist;
    }

    public void addToWishlist(String itemID){
        wishlist.add(itemID);
    }

    public void removeFromWishlist(String itemID){
        wishlist.remove(itemID);
    }

    public boolean isFrozen(){
        return frozen;
    }

    public void setFrozen(boolean frozen){
        this.frozen = frozen;
    }

    public int getCredit(){
        return credit;
    }

    public void incrementCredit(){
        credit++;
    }

    public void decrementCredit(){
        credit--;
    }

    @Override
    public boolean getIsAdmin() {
        return false;
    }

    public String toString() {
        String status;
        if (isFrozen()) {
            status = "Currently Frozen";
        }
        else {
            status = "In Good Standing";
        }
        return super.toString() + "\n" + status + "\n" + "Credit: " + getCredit();
    }

}
