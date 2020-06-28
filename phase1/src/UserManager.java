import java.io.Serializable;
import java.util.ArrayList;

public class UserManager implements Serializable {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<String> unfreezeRequests = new ArrayList<>();

    /**
     * Checks a user's email and password on login.
     * @param email The submitted email.
     * @param password The submitted password.
     * @return Whether or not the username/password combination is valid.
     */
    public boolean authenticateUser(String email, String password){
        for (User user : users){
            if (user.getEmail().equals(email) && user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a user's information given their email, or null if no matching user is found.
     * @param email The email of the user to look up.
     * @return A User object with the user's information, or null if no matching user is found.
     */
    public User getUserByEmail(String email){
        for (User user : users){
            if (user.getEmail().equals(email)){
                return user;
            }
        }
        return null;
    }

    /**
     * Gets a list of all users who are below the borrowing ratio.
     * @return A list of all users who are below the borrowing ratio.
     */
    public ArrayList<User> getUsersBelowThreshold(){
        ArrayList<User> result = new ArrayList<User>();
        for (User user : users){
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
