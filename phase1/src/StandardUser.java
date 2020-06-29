import java.lang.reflect.Array;
import java.util.ArrayList;

public class StandardUser extends User{
    private ArrayList<Integer> inventory;
    private ArrayList<Integer> wishlist;
    private String city;
    private boolean frozen;
    private int numItemsBorrowed;
    private int numItemsLent;

    public StandardUser(String name, String email, String password) {
        super(name, email, password);
        inventory = new ArrayList<>();
        wishlist = new ArrayList<>();
        frozen = false;
        numItemsBorrowed = 0;
        numItemsLent = 0;
    }

    public ArrayList<Integer> getInventory(){
        return inventory;
    }

    public void addToInventory(int itemID){
        inventory.add(itemID);
    }

    public void removeFromInventory(int itemID){
        inventory.remove(Integer.valueOf(itemID));
    }

    public ArrayList<Integer> getWishlist(){
        return wishlist;
    }

    public void addToWishlist(int itemID){
        wishlist.add(itemID);
    }

    public void removeFromWishlist(int itemID){
        wishlist.remove(Integer.valueOf(itemID));
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
}
