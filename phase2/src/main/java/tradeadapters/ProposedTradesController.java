package tradeadapters;

import tradegateway.TradeModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Manages input from the user to confirm or edit the meeting time(s) of proposed trades.
 */
public class ProposedTradesController {
    private final TradeModel tradeModel;
    private final String username;

    /**
     * Initiates the ProposedTradesController.
     *
     * @param tradeModel tradeModel
     */
    public ProposedTradesController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
        this.username = tradeModel.getCurrentUser();
    }

    /**
     * Allows user to confirm with the suggested meeting details (time, place)
     *
     * @param tradeId id of the trade
     */
    protected void confirmMeetingTime(String tradeId) {
        if (tradeModel.getMeetingManager().canChangeMeeting(tradeId, username)) {
            String meetingId = tradeModel.getTradeManager().getMeetingOfTrade(tradeId).get(tradeModel.getTradeManager().getMeetingOfTrade(tradeId).size() - 1);
            tradeModel.getMeetingManager().confirmAgreement(meetingId);
            changeItemUnavailable(tradeId);
        } else {
        }
    }

    /**
     * Makes the changes that are necessary for availability of the items of the users when the trade
     * meeting time/place is confirmed.
     *
     * @param tradeId id of the trade
     */
    private void changeItemUnavailable(String tradeId) {
        Map<String, List<String>> itemToUsers = tradeModel.getTradeManager().itemToUsers(tradeId);
        for (String item : itemToUsers.keySet()) {
            tradeModel.getItemManager().setItemAvailable(item, false);
            tradeModel.getTradeManager().deleteCommonItemTrades(item, tradeId);
        }
    }

    protected void editMeetingTime(String tradeId, List<String> details) {
        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date time = format.parse(details.get(1));
            String meetingId = tradeModel.getTradeManager().getMeetingOfTrade(tradeId).get(tradeModel.getTradeManager().getMeetingOfTrade(tradeId).size() - 1);
            tradeModel.getMeetingManager().changeMeeting(meetingId, details.get(0), time, username);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    protected void declineTrade(String tradeId){
        tradeModel.getTradeManager().cancelTrade(tradeId);
        tradeModel.getMeetingManager().cancelMeetingsOfTrade(tradeId);
    }
}
