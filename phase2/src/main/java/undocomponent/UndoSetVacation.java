package undocomponent;

import usercomponent.UserManager;
import java.io.Serializable;

/**
 * An undoable operation representing a user setting their vacation status.
 */
public class UndoSetVacation implements UndoableOperation, Serializable{

    private final String username;
    private final boolean status;
    private final UserManager userManager;

    /**
     * Instantiates a new UndoSetVacation operation.
     * @param username username of the user
     * @param userManager the UserManager containing the user
     */
    public UndoSetVacation(String username, UserManager userManager) {
        this.username = username;
        this.userManager = userManager;
        status = userManager.getOnVacation().contains(username);
    }

    /**
     * Undoes the vacation status of the TradingUser. Does nothing if the user has already changed their status back to
     * what it was before this operation was instantiated.
     */
    @Override
    public void undo(){
        userManager.setOnVacation(username, !status);
    }

    @Override
    public String toString() {
        return username + " has set their vacation status to " + status;
    }

}