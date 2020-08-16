package undocomponent;

import usercomponent.UserManager;
import java.io.Serializable;

public class UndoSetPrivacy implements UndoableOperation, Serializable{

    private final String username;
    private final boolean status;
    private final UserManager userManager;

    /**
     * Instantiates a new UndoSetPrivacy operation
     * @param username username of the user
     * @param userManager the UserManager containing the user
     */
    public UndoSetPrivacy(String username, UserManager userManager) {
        this.username = username;
        this.userManager = userManager;
        status = userManager.getPrivateUser().contains(username);
    }

    /**
     * Undoes the privacy status of the TradingUser. Does nothing if the user has already changed their status back to
     * what it was before this operation was instantiated.
     */
    @Override
    public void undo(){
        userManager.setPrivate(username, !status);
    }

    @Override
    public String toString() {
        String privacy;
        if (status) {
            privacy = "private";
        }
        else {
            privacy = "public";
        }
        return username + " has set their profile to " + privacy;
    }

}