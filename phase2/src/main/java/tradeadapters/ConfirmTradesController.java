package tradeadapters;

import tradegateway.TradeModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Manages input from the user to confirm trades happened in real life.
 */
public class ConfirmTradesController {
    private final TradeModel tradeModel;
    private final String username;

    /**
     * Initiates ConfirmTradesController
     * @param tradeModel tradeModel
     */
    public ConfirmTradesController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
        this.username = tradeModel.getCurrentUser();
    }

    /**
     * Gets a map that maps the trades of a user that need to be confirmed to the trade type (permanent or temporary)
     * @param username the username of the user
     */
    protected Map<String, String> getToBeConfirmedTrades(String username){
        List<String> userOngoing = tradeModel.getTradeManager().getTradesOfUser(username, "ongoing");
        List<String> userToBeConfirmed = tradeModel.getMeetingManager().getToCheckTrades(userOngoing, "needConfirm");
        return tradeModel.getTradeManager().getType(userToBeConfirmed);
    }

    /**
     * Allows the user to confirm that the real life meeting happened.
     * @param tradeId ID of the trade
     * @param type the type of trade (temporary or permanent)
     */
    protected void confirmTradeHappened(String tradeId, String type) {
        if (tradeModel.getMeetingManager().canChangeMeeting(tradeId, username)) {
            changeToConfirmed(tradeId, username);

            if (tradeModel.getTradeManager().getTradesOfUser(username, "completed").contains(tradeId)) {
                completedTradeChanges(tradeId, type);
            } else {
                if (tradeModel.getMeetingManager().tradeMeetingsCompleted(tradeId)){
                    if (tradeModel.getTradeManager().needToAddMeeting(tradeId, 1, 2)){
                        createMandatoryReturnMeeting(tradeId);
                    }
                }
            }
        }
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
     * @param tradeId ID of the trade
     */
    private void createMandatoryReturnMeeting(String tradeId){
        Calendar cal = Calendar.getInstance();
        cal.setTime(tradeModel.getMeetingManager().getLastMeetingTime(tradeId));
        cal.add(Calendar.DATE, 30);
        Date newDate = cal.getTime();
        String newMeetingId = tradeModel.getMeetingManager().createMeeting(tradeModel.getMeetingManager().getLastMeetingLocation(tradeId), newDate, username, tradeId);
        tradeModel.getTradeManager().addMeetingToTrade(tradeId, newMeetingId);
        tradeModel.getMeetingManager().confirmAgreement(newMeetingId);
    }

    /**
     * Makes the necessary changes of the item and user status once a trade is completed.
     * @param tradeId ID of the trade
     * @param type the type of trade (temporary or permanent)
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