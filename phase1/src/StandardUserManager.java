import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class StandardUserManager implements Serializable {
    private HashMap<String, StandardUser> users = new HashMap<>();
    private ArrayList<String> unfreezeRequests = new ArrayList<>();

    /**
     * Checks a user's email and password on login.
     * @param email The submitted email.
     * @param password The submitted password.
     * @return Whether or not the username/password combination is valid.
     */
    boolean login(String email, String password) {
        StandardUser account = users.get(email);
        return password.equals(account.getPassword());
    }

    /**
     * Checks a StandardUser and adds it to users if the email is unique
     * @param name The name of the StandardUser
     * @param email The email of the StandardUser
     * @param password The password of the StandardUser
     * @return Whether or not the StandardUser was successfully added
     */
    boolean createStandardUser(String name, String email, String password) {
        if (users.containsKey(email)) {
            return false;
        }
        users.put(email, new StandardUser(name, email, password));
        return true;
    }

    /**
     * Gets a user's information given their email, or null if no matching user is found.
     * @param email The email of the user to look up.
     * @return A User object with the user's information, or null if no matching user is found.
     */
    public User getUserByEmail(String email){
        if (!users.containsKey(email)) {
            return null;
        }
        return users.get(email);
    }

    /**
     * Gets a list of all users who are below the borrowing ratio.
     * @return A list of all users who are below the borrowing ratio.
     */
    public ArrayList<User> getUsersBelowThreshold(){
        ArrayList<User> result = new ArrayList<>();
        for (StandardUser user : users.values()){
            if (user.getNumItemsBorrowed() - user.getNumItemsLent() >= 1) { //TODO allow changing this ratio requirement
                result.add(user);
            }
        }
        return result;
    }

    /**
     * Adds a user to the unfreeze request queue.
     * @param email The email of the user requesting an unfreeze.
     */
    public void markUserForUnfreezing(String email){
        unfreezeRequests.add(email);
    }
}
