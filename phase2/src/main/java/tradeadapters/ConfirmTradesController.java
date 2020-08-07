package tradeadapters;

import org.json.JSONException;
import org.json.JSONObject;
import tradegateway.TradeModel;
import trademisc.RunnableController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
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
        } catch (IOException | JSONException e) {
            System.out.println("Something bad happened.");
        }
    }

    private boolean confirmTrades() throws IOException, JSONException {
        Map<String, String> trades = getToBeConfirmedTrades(username);
        for (String tradeId : trades.keySet()) {
            List<JSONObject> allTrade = new ArrayList<>();
            allTrade.add(tradeModel.getTradeManager().getTradeInfo(tradeId));
            allTrade.addAll(tradeModel.getMeetingManager().getMeetingsInfo(tradeId));
            StringBuilder allTradeInfo = new StringBuilder();
            for (JSONObject details : allTrade) {
                allTradeInfo.append(details.toString(4));
            }
            presenter.showTrade(allTradeInfo.toString());
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

    /**
     * Allows the user to confirm that the real life meeting happened.
     * @param tradeId id of the trade
     */
    private void confirmTradeHappened(String tradeId, String type) throws IOException {
        if (tradeModel.getMeetingManager().canChangeMeeting(tradeId, username)) {
            changeToConfirmed(tradeId, username);
            presenter.confirmedTrade();

            if (tradeModel.getTradeManager().getTradesOfUser(username, "completed").contains(tradeId)) {
                completedTradeChanges(tradeId, type);
            } else {
                if (tradeModel.getMeetingManager().tradeMeetingsCompleted(tradeId)){
                    if (tradeModel.getTradeManager().needToAddMeeting(tradeId, 1, 2)){
                        createMandatoryReturnMeeting(tradeId);
                    }
                }
            }
        } else { presenter.declineConfirm(); }
    }

    private void changeToConfirmed(String tradeId, String username){
        String meetingId = tradeModel.getTradeManager().getMeetingOfTrade(tradeId).get(tradeModel.getTradeManager().getMeetingOfTrade(tradeId).size() - 1);
        tradeModel.getMeetingManager().meetingHappened(meetingId, username);
        if ((!tradeModel.getTradeManager().needToAddMeeting(tradeId, 1, 2)) && (tradeModel.getMeetingManager().tradeMeetingsCompleted(tradeId))){
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
                tradeModel.getUserManager().removeFromWishlist(itemToUsers.get(item).get(1), item);
                tradeModel.getItemManager().setOwner(item, itemToUsers.get(item).get(1));
            }
            tradeModel.getItemManager().setItemAvailable(item, true);
            tradeModel.getUserManager().updateCreditByUsername(itemToUsers.get(item).get(0), true);
            tradeModel.getUserManager().updateCreditByUsername(itemToUsers.get(item).get(1), false);
        }
    }
}