package tradeadapters;

import tradegateway.TradeModel;
import trademisc.RunnableController;
import usercomponent.ItemSets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Manages input from the user to confirm trades happened in real life.
 */
public class ConfirmTradesController implements RunnableController {
    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final ConfirmTradesPresenter presenter;
    private final String username;

    /**
     * Initiates ConfirmTradesController
     * @param tradeModel tradeModel
     * @param username *username* of the user
     */
    public ConfirmTradesController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ConfirmTradesPresenter();
    }

    /**
     * Overrides run() method in the trademisc.RunnableController interface.
     */
    @Override
    public void run() {
        try {
            confirmTrades();
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }

    private boolean confirmTrades() throws IOException {
        Map<String, String> trades = getToBeConfirmedTrades(username);
        for (String tradeId : trades.keySet()) {
            presenter.showTrade(getTradeAllInfo(tradeId));
            String input = br.readLine();
            switch(input) {
                case "1":
                    confirmTradeHappened(tradeId, trades.get(tradeId));
                    break;
                case "2":
                    break;
                case "exit":
                    presenter.end();
                    return false;
                default:
                    presenter.tryAgain();
            }
        } presenter.endTrades();
        return true;
    }

    private Map<String, String> getToBeConfirmedTrades(String username){
        List<String> userOngoing = tradeModel.getTradeManager().getTradesOfUser(username, "ongoing");
        List<String> userToBeConfirmed = tradeModel.getMeetingManager().getToCheckTrades(userOngoing, "needConfirm");
        return tradeModel.getTradeManager().getType(userToBeConfirmed);
    }

    private String getTradeAllInfo(String tradeId){
        return tradeModel.getTradeManager().getTradeInfo(tradeId) + "\n" +
                tradeModel.getMeetingManager().getMeetingsInfo(tradeId);
    }

    /**
     * Allows the user to confirm that the real life meeting happened.
     * @param tradeId id of the trade
     */
    private void confirmTradeHappened(String tradeId, String type) throws IOException {
        if (tradeModel.getMeetingManager().canChangeMeeting(tradeId, username)) {
            changeToConfirmed(tradeId, username);
            presenter.confirmedTrade();

            if (!(tradeModel.getTradeManager().needToAddMeeting(tradeId))) {
                List<String> reviewInfo = getReviewInfo();
                if (reviewInfo.size() > 0) {
                    String receiver = "";
                    for (List<String> users : tradeModel.getTradeManager().itemToUsers(tradeId).values()) {
                        users.remove(username);
                        receiver = users.get(0);
                    }
                    tradeModel.getReviewManager().addReview(Integer.parseInt(reviewInfo.get(0)), reviewInfo.get(1), tradeId, username, receiver);
                }
            }

            if (tradeModel.getTradeManager().getTradesOfUser(username, "completed").contains(tradeId)) {
                completedTradeChanges(tradeId, type);
            } else {
                if (tradeModel.getMeetingManager().tradeMeetingsCompleted(tradeId)){
                    if (tradeModel.getTradeManager().needToAddMeeting(tradeId)){
                        createMandatoryReturnMeeting(tradeId);
                    }
                }
            }
        } else { presenter.declineConfirm(); }
    }

    private List<String> getReviewInfo() throws IOException {
        List<String> reviewInfo = new ArrayList<>();
        presenter.askRating();
        String rating = br.readLine();
        List<String> validRatings = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
        if (rating.equals("")) {
            return reviewInfo;
        } else {
            do {
                presenter.invalidRating();
                rating = br.readLine();
            } while (!(validRatings.contains(rating)));
        }
        presenter.askComment();
        String comment = br.readLine();
        if (!comment.equals("exit")) {
            reviewInfo.add(rating);
            reviewInfo.add(comment);
        }
        return reviewInfo;
    }


        private void changeToConfirmed(String tradeId, String username){
        String meetingId = tradeModel.getTradeManager().getMeetingOfTrade(tradeId).get(tradeModel.getTradeManager().getMeetingOfTrade(tradeId).size() - 1);
        tradeModel.getMeetingManager().meetingHappened(meetingId, username);
        if ((!tradeModel.getTradeManager().needToAddMeeting(tradeId)) && (tradeModel.getMeetingManager().tradeMeetingsCompleted(tradeId))){
            tradeModel.getTradeManager().closeTrade(tradeId);
        }
    }

    /**
     * Creates the mandatory return meeting, 30 days after the first meeting time at the same location
     * @param tradeId id of the trade
     */
    private void createMandatoryReturnMeeting(String tradeId){
        Calendar cal = Calendar.getInstance();
        cal.setTime(tradeModel.getMeetingManager().getLastMeetingTime(tradeId));
        cal.add(Calendar.DATE, 30);
        Date newDate = cal.getTime();
        String newMeetingId = tradeModel.getMeetingManager().createMeeting(tradeModel.getMeetingManager().getLastMeetingLocation(tradeId), newDate, username, tradeId);
        tradeModel.getTradeManager().addMeetingToTrade(tradeId, newMeetingId);
        tradeModel.getMeetingManager().confirmAgreement(newMeetingId);
        presenter.displayNewDate(newDate.toString());
    }

    /**
     * Makes the necessary changes of the item and user status once a trade is completed.
     * @param tradeId id of the trade
     */
    private void completedTradeChanges(String tradeId, String type){
        Map<String, List<String>> itemToUsers = tradeModel.getTradeManager().itemToUsers(tradeId);
        for (String item: itemToUsers.keySet()) {
            if (type.equals("permanent")) {
                tradeModel.getUserManager().removeFromSet(itemToUsers.get(item).get(1), item, ItemSets.WISHLIST);
                tradeModel.getUserManager().removeFromSet(itemToUsers.get(item).get(0), item, ItemSets.INVENTORY);
                tradeModel.getUserManager().addToSet(itemToUsers.get(item).get(1), item, ItemSets.INVENTORY);
                tradeModel.getItemManager().setOwner(item, itemToUsers.get(item).get(1));
            }

            tradeModel.getItemManager().setConfirmedItemAvailable(item, true);
            tradeModel.getUserManager().updateCreditByUsername(itemToUsers.get(item).get(0), true);
            tradeModel.getUserManager().updateCreditByUsername(itemToUsers.get(item).get(1), false);
        }
        tradeModel.getReviewManager().verifyReview(tradeId);
    }
}