package profileadapters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tradegateway.TradeModel;
import undocomponent.UndoAddReview;
import undocomponent.UndoableOperation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProfileController  {
    // TODO: Clean up unnecessary methods
    // TODO: Combine some getters for profileGUI into one or more JSON replies time permitting
    private final TradeModel tradeModel;

    public ProfileController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
    }

    /**
     * Adds a review with given rating (1-5) and comment for trade with tradeId written by currently logged in user.
     * Precondition: this review must be legal (i.e. canAddReview returns true)
     * @param tradeId trade for review
     * @param rating rating to include in review
     * @param comment comment to include in review
     */
    public void addReview(String tradeId, int rating, String comment){
        String receiver = "";
        for (String item : tradeModel.getTradeManager().itemToUsers(tradeId).keySet()) {
            List<String> users = tradeModel.getTradeManager().itemToUsers(tradeId).get(item);
            if (users.get(0).equals(tradeModel.getCurrentUser())) {
                receiver = users.get(1);
            } else {
                receiver = users.get(0);
            }
        }
        String reviewId = tradeModel.getReviewManager().addReview(rating, comment, tradeId, tradeModel.getCurrentUser(), receiver);
        UndoableOperation undoableOperation = new UndoAddReview(this.tradeModel.getReviewManager(), reviewId);
        tradeModel.getUndoManager().add(undoableOperation);
    }

    /**
     * Checks if given user can write a review for given trade.
     * Trade must be completed, and user must be one of the traders involved who have not yet written a review for it.
     * @param tradeId id of trade for potential review
     * @param username id of author for potential review
     * @return true iff user is allowed to write a review for trade with tradeid
     */
    public boolean canAddReview(String tradeId, String username) {
        List<String> userTrades = tradeModel.getTradeManager().getTradesOfUser(username, "completed");
        if (!userTrades.contains(tradeId)) {
            // check trade has completed and username is one of the users involved
            return false;
        } else {
            // check user hasn't already written a review
            String receiver = "";
            for (String item : tradeModel.getTradeManager().itemToUsers(tradeId).keySet()) {
                List<String> users = tradeModel.getTradeManager().itemToUsers(tradeId).get(item);
                if (users.get(0).equals(username)) {
                    receiver = users.get(1);
                } else {
                    receiver = users.get(0);
                }
            }
            return !tradeModel.getReviewManager().alreadyWroteReview(username, receiver, tradeId);
        }
    }

    /**
     * Get reviews written for user with username
     * Precondition: valid username
     *
     * @param username user's reviews to get
     * @return JSON representation of reviews
     */
    protected List<String> getReviews(String username) {
        String json = tradeModel.getReviewManager().getReviewsByUser(username);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
        List<Map<String, String>> reviewMaps = gson.fromJson(json, type);
        List<String> reviewStrings = new ArrayList<>();

        for (int i=1; i < reviewMaps.size(); i++) {
            // Skip first map because that's average rating
            Map<String, String> reviewMap = reviewMaps.get(i);
            String r = "Author: " + reviewMap.get("author") + "\nRating: " + reviewMap.get("rating") + "\n" + reviewMap.get("comment");
            reviewStrings.add(r);
        }
        return reviewStrings;
    }

    protected String getAverageRating(String username) {
        String json = tradeModel.getReviewManager().getReviewsByUser(username);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
        List<Map<String, String>> reviewMaps = gson.fromJson(json, type);
        String averageRating = reviewMaps.get(0).get("average");
        return averageRating;
    }

    /**
     * Get friends of user with username
     * Precondition: valid username
     *
     * @param username user's friends to get
     * @return JSON representation of friends list
     */
    protected List<String> getFriends(String username) {
        List<String> friendsList = new ArrayList<>(tradeModel.getUserManager().getFriendList(username));
        friendsList.sort(String.CASE_INSENSITIVE_ORDER);
        return friendsList;
    }

    /**
     * Get rank of user with username
     * Precondition: valid username
     *
     * @param username user's rank to get
     * @return user's rank
     */
    protected String getRank(String username) {
        return tradeModel.getUserManager().getRankByUsername(username);
    }

    protected String getCity(String username) {
        return tradeModel.getUserManager().getCityByUsername(username);
    }

    protected boolean getFrozenStatus(String username) {
        return tradeModel.getUserManager().isFrozen(username);
    }

    protected boolean getVacationMode(String username) {
        return tradeModel.getUserManager().getOnVacation().contains(username);
    }

    protected boolean getPrivacyMode(String username) {
        return tradeModel.getUserManager().getPrivateUser().contains(username);
    }

    protected void setVacationMode(boolean vacation) {
        tradeModel.getUserManager().setOnVacation(tradeModel.getCurrentUser(), vacation);
    }

    protected void setPrivacyMode(boolean privacy) {
        tradeModel.getUserManager().setPrivate(tradeModel.getCurrentUser(), privacy);
    }

    protected void requestUnfreeze() {
        tradeModel.getUserManager().markUserForUnfreezing(tradeModel.getCurrentUser());
    }

    protected boolean isOwnProfile(String username) {
        return tradeModel.getCurrentUser().equals(username);
    }

    protected String getFriendRequests() {
        List<String> friendsRequestsList = new ArrayList<>(tradeModel.getUserManager().getFriendRequests(tradeModel.getCurrentUser()));
        Gson gson = new Gson();
        return gson.toJson(friendsRequestsList);
    }

    protected String getFriendshipStatus(String otherUsername) {
        if (tradeModel.getUserManager().getFriendList(tradeModel.getCurrentUser()).contains(otherUsername)) {
            return "friends";
        }
        else if(tradeModel.getUserManager().getFriendRequests(otherUsername).contains(tradeModel.getCurrentUser())) {
            return "sent";
        }
        else if(tradeModel.getUserManager().getFriendRequests(tradeModel.getCurrentUser()).contains(otherUsername)) {
            return "received";
        }
        else {
            return "none";
        }
    }

    protected void sendFriendRequest(String otherUsername) {
        tradeModel.getUserManager().sendFriendRequest(otherUsername, tradeModel.getCurrentUser());
    }

    protected void acceptOrIgnoreFriendsRequests(List<String> usernames, boolean accept) {
        for (String u : usernames) {
            tradeModel.getUserManager().setFriendRequest(tradeModel.getCurrentUser(), u, accept);
        }
    }

    protected List<String> getOtherUsersWithProfiles() {
        Set<String> userIds = tradeModel.getUserManager().getAllTradingUsers();
        List<String> otherUserIds = new ArrayList<>();
        for (String id: userIds) {
            if (tradeModel.getCurrentUser() == null || !id.equals(tradeModel.getCurrentUser())) {
                otherUserIds.add(id);
            }
        }
        otherUserIds.sort(String.CASE_INSENSITIVE_ORDER);
        return otherUserIds;
    }

}
