package useradapters;

import com.google.gson.Gson;
import tradegateway.TradeModel;
import trademisc.RunnableController;
import undocomponent.UndoAddReview;
import undocomponent.UndoableOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ProfileController implements RunnableController {
    // TODO: Clean up unnecessary methods
    // TODO: Combine some getters for profileGUI into one or more JSON replies time permitting
    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final ProfilePresenter presenter;
    private final String username;

    public ProfileController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
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
        tradeModel.getUserManager().setOnVacation(username, confirmationInput.equals("1"));
    }

    private void accountPrivacy() throws IOException {
        presenter.accountPrivacySelection();
        String confirmationInput = br.readLine();
        while (!confirmationInput.equals("0") && !confirmationInput.equals("1")) {
            presenter.invalidInput();
            confirmationInput = br.readLine();
        }
        tradeModel.getUserManager().setPrivate(username, confirmationInput.equals("1"));
    }

    private void reviewFriendRequests() throws IOException {
        Set<String> requests = tradeModel.getUserManager().getFriendRequests(username);
        for (String user : requests) {
            presenter.manageRequest(user);
            String confirmationInput = br.readLine();
            while (!confirmationInput.equals("0") && !confirmationInput.equals("1") && !confirmationInput.equals("2")) {
                presenter.invalidInput();
                confirmationInput = br.readLine();
            }
            if (!confirmationInput.equals("0")){
                tradeModel.getUserManager().setFriendRequest(username, user, confirmationInput.equals("1"));
            }
        }
    }

    private void addFriend() throws IOException {
        presenter.printEnterFriendUsername();
        String friend = br.readLine();
        if (!tradeModel.getUserManager().containsTradingUser(friend)){
            presenter.printSearchingInvalid();
        } else if((tradeModel.getUserManager().getFriendList(username).contains(friend))| (friend.equals(username))){
            presenter.alreadyFriend();
        } else if(tradeModel.getUserManager().getFriendRequests(friend).contains(username)){
            presenter.alreadySentRequest();
        }
        else {
            tradeModel.getUserManager().sendFriendRequest(friend, username);
            presenter.addedFriend();
        }
    }

    private void viewAccountSetting(){
        String privacy;
        String vacation;
        if (tradeModel.getUserManager().getPrivateUser().contains(username)){
            privacy = "private";
        } else{ privacy = "public";}
        if (tradeModel.getUserManager().getOnVacation().contains(username)){
            vacation = "on";
        } else{ vacation = "off";}
        String city = tradeModel.getUserManager().getCityByUsername(username);
        presenter.printViewAccountSettings(privacy, vacation, city);
    }

    private void viewFriends(){
        List<String> friends = new ArrayList<>(tradeModel.getUserManager().getFriendList(username));
        presenter.printViewFriends(friends);
    }

//    protected void addReview(String tradeId) throws IOException{
//        List<String> userTrades = tradeModel.getTradeManager().getTradesOfUser(username, "completed");
//        presenter.printEnterTradeIdForReview();
//        String tradeId = br.readLine();
//        if (!userTrades.contains(tradeId)) {
//            presenter.printInvalidTradeId();
//        } else{
//            String receiver = "";
//            for (List<String> users: tradeModel.getTradeManager().itemToUsers(tradeId).values()){
//                users.remove(username);
//                receiver = users.get(0);
//            }
//            if (tradeModel.getReviewManager().alreadyWroteReview(username, receiver, tradeId)){
//                presenter.alreadyWroteReview(receiver, tradeId);
//            }
//            List<String> reviewInfo = getReviewInfo();
//            String reviewId = tradeModel.getReviewManager().addReview(Integer.parseInt(reviewInfo.get(0)), reviewInfo.get(1), tradeId, username, receiver);
//            UndoableOperation undoableOperation = new UndoAddReview(this.tradeModel.getReviewManager(), reviewId);
//            tradeModel.getUndoManager().add(undoableOperation);
//        }

    public void addReview(String tradeId, int rating, String comment){
        String receiver = "";
        for (List<String> users: tradeModel.getTradeManager().itemToUsers(tradeId).values()){
            users.remove(username);
            receiver = users.get(0);
        }
        String reviewId = tradeModel.getReviewManager().addReview(rating, comment, tradeId, username, receiver);
        UndoableOperation undoableOperation = new UndoAddReview(this.tradeModel.getReviewManager(), reviewId);
        tradeModel.getUndoManager().add(undoableOperation);
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
    public String getReviews(String username) {
            return tradeModel.getReviewManager().getReviews(username);
        }

    /**
     * Get friends of user with username
     * Precondition: valid username
     *
     * @param username user's friends to get
     * @return JSON representation of friends list
     */
    public String getFriends(String username) {
        List<String> friendsList = new ArrayList<>(tradeModel.getUserManager().getFriendList(username));
        Gson gson = new Gson();
        return gson.toJson(friendsList);
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

