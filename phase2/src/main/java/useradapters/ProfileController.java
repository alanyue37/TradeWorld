package useradapters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tradegateway.TradeModel;
import trademisc.RunnableController;
import undocomponent.UndoAddReview;
import undocomponent.UndoableOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

public class ProfileController implements RunnableController {
    // TODO: Clean up unnecessary methods
    // TODO: Combine some getters for profileGUI into one or more JSON replies time permitting
    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final ProfilePresenter presenter;

    public ProfileController(TradeModel tradeModel) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        presenter = new ProfilePresenter();
    }

    @Override
    public void run() {
        try {
            browseProfile();
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }

    private boolean browseProfile() throws IOException {
        presenter.showOptions();
        String input = br.readLine();
        switch (input) {
            case "1": // set/unset vacation
                editVacation();
                break;
            case "2": // private/public account
                accountPrivacy();
                break;
            case "3": // manage friend requests
                reviewFriendRequests();
                break;
            case "4": // adding friend (send friend request)
                addFriend();
                break;
            case "5": // view friend list
                viewFriends();
                break;
            case "6": // view account setting: privacy, vacation, city
                viewAccountSetting();
                break;
//            case "7": // add review after trade is complete
//                addReview();
//                break;
            case "exit":
                return false;
            default:
                presenter.tryAgain();
        }
        return true;
    }


    private void editVacation() throws IOException {
        presenter.accountModeSelection();
        String confirmationInput = br.readLine();
        while (!confirmationInput.equals("0") && !confirmationInput.equals("1")) {
            presenter.invalidInput();
            confirmationInput = br.readLine();
        }
        tradeModel.getUserManager().setOnVacation(tradeModel.getCurrentUser(), confirmationInput.equals("1"));
    }

    private void accountPrivacy() throws IOException {
        presenter.accountPrivacySelection();
        String confirmationInput = br.readLine();
        while (!confirmationInput.equals("0") && !confirmationInput.equals("1")) {
            presenter.invalidInput();
            confirmationInput = br.readLine();
        }
        tradeModel.getUserManager().setPrivate(tradeModel.getCurrentUser(), confirmationInput.equals("1"));
    }

    private void reviewFriendRequests() throws IOException {
        Set<String> requests = tradeModel.getUserManager().getFriendRequests(tradeModel.getCurrentUser());
        for (String user : requests) {
            presenter.manageRequest(user);
            String confirmationInput = br.readLine();
            while (!confirmationInput.equals("0") && !confirmationInput.equals("1") && !confirmationInput.equals("2")) {
                presenter.invalidInput();
                confirmationInput = br.readLine();
            }
            if (!confirmationInput.equals("0")){
                tradeModel.getUserManager().setFriendRequest(tradeModel.getCurrentUser(), user, confirmationInput.equals("1"));
            }
        }
    }

    private void addFriend() throws IOException {
        presenter.printEnterFriendUsername();
        String friend = br.readLine();
        if (!tradeModel.getUserManager().containsTradingUser(friend)){
            presenter.printSearchingInvalid();
        } else if((tradeModel.getUserManager().getFriendList(tradeModel.getCurrentUser()).contains(friend))| (friend.equals(tradeModel.getCurrentUser()))){
            presenter.alreadyFriend();
        } else if(tradeModel.getUserManager().getFriendRequests(friend).contains(tradeModel.getCurrentUser())){
            presenter.alreadySentRequest();
        }
        else {
            tradeModel.getUserManager().sendFriendRequest(friend, tradeModel.getCurrentUser());
            presenter.addedFriend();
        }
    }

    private void viewAccountSetting(){
        String privacy;
        String vacation;
        if (tradeModel.getUserManager().getPrivateUser().contains(tradeModel.getCurrentUser())){
            privacy = "private";
        } else{ privacy = "public";}
        if (tradeModel.getUserManager().getOnVacation().contains(tradeModel.getCurrentUser())){
            vacation = "on";
        } else{ vacation = "off";}
        String city = tradeModel.getUserManager().getCityByUsername(tradeModel.getCurrentUser());
        presenter.printViewAccountSettings(privacy, vacation, city);
    }

    private void viewFriends(){
        List<String> friends = new ArrayList<>(tradeModel.getUserManager().getFriendList(tradeModel.getCurrentUser()));
        presenter.printViewFriends(friends);
    }

    public void addReview(String tradeId, int rating, String comment){
        String receiver = "";
        for (List<String> users: tradeModel.getTradeManager().itemToUsers(tradeId).values()){
            users.remove(tradeModel.getCurrentUser());
            receiver = users.get(0);
        }
        String reviewId = tradeModel.getReviewManager().addReview(rating, comment, tradeId, tradeModel.getCurrentUser(), receiver);
        UndoableOperation undoableOperation = new UndoAddReview(this.tradeModel.getReviewManager(), reviewId);
        tradeModel.getUndoManager().add(undoableOperation);
    }

    public boolean canAddReview(String tradeId, String username) {
        List<String> userTrades = tradeModel.getTradeManager().getTradesOfUser(username, "completed");
        if (!userTrades.contains(tradeId)) {
            return false;
        } else {
            String receiver = "";
            for (List<String> users : tradeModel.getTradeManager().itemToUsers(tradeId).values()) {
                users.remove(username);
                receiver = users.get(0);
            }
            return !tradeModel.getReviewManager().alreadyWroteReview(username, receiver, tradeId);
        }
    }

    private List<String> getReviewInfo() throws IOException {
        List<String> reviewInfo = new ArrayList<>();
        presenter.askRating();
        String rating = br.readLine();
        List<String> validRatings = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
        while (!(validRatings.contains(rating))) {
            presenter.invalidRating();
            rating = br.readLine();
            }
        presenter.askComment();
        String comment = br.readLine();
        if (!comment.equals("exit")) {
            reviewInfo.add(rating);
            reviewInfo.add(comment);
        }
        return reviewInfo;
    }

    /**
     * Get reviews written for user with username
     * Precondition: valid username
     *
     * @param username user's reviews to get
     * @return JSON representation of reviews
     */
    public List<String> getReviews(String username) {
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

    public String getAverageRating(String username) {
        String json = tradeModel.getReviewManager().getReviewsByUser(username);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
        List<Map<String, String>> reviewMaps = gson.fromJson(json, type);
        List<String> reviewStrings = new ArrayList<>();
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
    public List<String> getFriends(String username) {
        List<String> friendsList = new ArrayList<>(tradeModel.getUserManager().getFriendList(username));
        Collections.sort(friendsList, String.CASE_INSENSITIVE_ORDER);
        return friendsList;
    }

    /**
     * Get rank of user with username
     * Precondition: valid username
     *
     * @param username user's rank to get
     * @return user's rank
     */
    public String getRank(String username) {
        return tradeModel.getUserManager().getRankByUsername(username);
    }

    public String getCity(String username) {
        return tradeModel.getUserManager().getCityByUsername(username);
    }

    public boolean getFrozenStatus(String username) {
        return tradeModel.getUserManager().isFrozen(username);
    }

    public boolean getVacationMode(String username) {
        return tradeModel.getUserManager().getOnVacation().contains(username);
    }

    public boolean getPrivacyMode(String username) {
        return tradeModel.getUserManager().getPrivateUser().contains(username);
    }

    public void setVacationMode(boolean vacation) {
        tradeModel.getUserManager().setOnVacation(tradeModel.getCurrentUser(), vacation);
    }

    public void setPrivacyMode(boolean privacy) {
        tradeModel.getUserManager().setPrivate(tradeModel.getCurrentUser(), privacy);
    }

    public void requestUnfreeze() {
        tradeModel.getUserManager().markUserForUnfreezing(tradeModel.getCurrentUser());
    }

    public boolean isOwnProfile(String username) {
        return tradeModel.getCurrentUser().equals(username);
    }

    public String getFriendRequests() {
        List<String> friendsRequestsList = new ArrayList<>(tradeModel.getUserManager().getFriendRequests(tradeModel.getCurrentUser()));
        Gson gson = new Gson();
        return gson.toJson(friendsRequestsList);
    }

    public String getFriendshipStatus(String otherUsername) {
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

    public void sendFriendRequest(String otherUsername) {
        tradeModel.getUserManager().sendFriendRequest(otherUsername, tradeModel.getCurrentUser());
    }

    public void acceptOrIgnoreFriendsRequests(List<String> usernames, boolean accept) {
        for (String u : usernames) {
            tradeModel.getUserManager().setFriendRequest(tradeModel.getCurrentUser(), u, accept);
        }
    }

    public List<String> getOtherUsersWithProfiles() {
        Set<String> userIds = tradeModel.getUserManager().getAllTradingUsers();
        List<String> otherUserIds = new ArrayList<>();
        for (String id: userIds) {
            if (tradeModel.getCurrentUser() == null || !id.equals(tradeModel.getCurrentUser())) {
                otherUserIds.add(id);
            }
        }
        Collections.sort(otherUserIds, String.CASE_INSENSITIVE_ORDER);
        return otherUserIds;
    }

}

