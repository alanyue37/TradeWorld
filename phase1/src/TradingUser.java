import java.util.HashSet;

public class TradingUser extends User {

    private final HashSet<Integer> inventory;
    private final HashSet<Integer> wishlist;
    private String city;
    private boolean frozen;
    private int numItemsBorrowed;
    private int numItemsLent;

    public TradingUser(String name, String email, String password) {
        super(name, email, password);
        inventory = new HashSet<>();
        wishlist = new HashSet<>();
        frozen = false;
        numItemsBorrowed = 0;
        numItemsLent = 0;
    }

    public HashSet<Integer> getInventory(){
        return inventory;
    }

    public void addToInventory(int itemID){
        inventory.add(itemID);
    }

    public void removeFromInventory(int itemID){
        inventory.remove(itemID);
    }

    public HashSet<Integer> getWishlist(){
        return wishlist;
    }

    public void addToWishlist(int itemID){
        wishlist.add(itemID);
    }

    public void removeFromWishlist(int itemID){
        wishlist.remove(itemID);
    }

    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public boolean isFrozen(){
        return frozen;
    }

    public void setFrozen(boolean frozen){
        this.frozen = frozen;
    }

    public int getNumItemsBorrowed(){
        return numItemsBorrowed;
    }

    public void incrementNumItemsBorrowed(){
        numItemsBorrowed++;
    }

    public int getNumItemsLent(){
        return numItemsLent;
    }

    public void incrementNumItemsLent(){
        numItemsLent++;
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
        return super.toString() + "\n" + "From " + city + "\n" + status + "\n" + "Have Borrowed " +
                getNumItemsBorrowed() + " Items \n" + "Have Lent " + getNumItemsLent() + "Items";
    }

}
