package undocomponent;

import usercomponent.UserManager;
import java.io.Serializable;

/**
 * An undoable operation representing a user adding a friend.
 */
public class UndoAddFriend implements UndoableOperation, Serializable{

    private final String userOne;
    private final String userTwo;
    private final UserManager userManager;

    /**
     * Instantiates a new UndoAddFriend operation.
     * @param userOne username of one friend
     * @param userTwo username of other friend
     * @param userManager the UserManager containing the users
     */
    public UndoAddFriend(String userOne, String userTwo, UserManager userManager) {
        this.userOne = userOne;
        this.userTwo = userTwo;
        this.userManager = userManager;
    }

    /**
     * Removes a friend of a TradingUser.
     */
    @Override
    public void undo(){
        userManager.removeFriend(userOne, userTwo);
    }

    @Override
    public String toString() {
        return "Users " + userOne + " and " + userTwo + " have become friends.";
    }

}
