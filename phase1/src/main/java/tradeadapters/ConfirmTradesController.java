package tradeadapters;

import trademisc.RunnableController;
import tradegateway.TradeModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Manages input from the user to confirm trades happened in real life.
 */
public class ConfirmTradesController implements RunnableController {
    private final BufferedReader br;
    TradeModel tradeModel;
    ConfirmTradesPresenter presenter;
    private String username;

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
        List<String> trades = tradeModel.getTradeManager().getToBeConfirmedTrades(username);
        for (String tradeId : trades) {
            presenter.showTrade(tradeModel.getTradeManager().getTradeAllInfo(tradeId));
            String input = br.readLine();
            switch(input) {
                case "1":
                    confirmTradeHappened(tradeId);
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

    /**
     * Allows the user to confirm that the real life meeting happened.
     * @param tradeId id of the trade
     */
    private void confirmTradeHappened(String tradeId) {
        if (tradeModel.getTradeManager().canChangeMeeting(tradeId, username)){
            tradeModel.getTradeManager().confirmMeetingHappened(tradeId, username);
            presenter.confirmedTrade();

            if (tradeModel.getTradeManager().getTradesOfUser(username, "completed").contains(tradeId)) {
                completedTradeChanges(tradeId);
            } else {
                if (!tradeModel.getTradeManager().getIncompleteTrade().contains(tradeId)) {
                    if (tradeModel.getTradeManager().needToAddMeeting(tradeId)){
                        createMandatoryReturnMeeting(tradeId);
                    }
                }
            }
        } else{ presenter.declineConfirm(); }
    }

    /**
     * Creates the mandatory return meeting, 30 days after the first meeting time at the same location
     * @param tradeId id of the trade
     */
    private void createMandatoryReturnMeeting(String tradeId){
        Calendar cal = Calendar.getInstance();
        cal.setTime(tradeModel.getTradeManager().getLastConfirmedMeetingTime(tradeId));
        cal.add(Calendar.DATE, 30);
        Date newDate = cal.getTime();
        tradeModel.getTradeManager().editMeetingOfTrade(tradeId,
                tradeModel.getTradeManager().getLastConfirmedMeetingLocation(tradeId), newDate, username, "add");
        tradeModel.getTradeManager().agreeMeetingDetails(tradeId);
        presenter.displayNewDate(newDate.toString());
    }

    /**
     * Makes the necessary changes of the item and user status once a trade is completed.
     * @param tradeId id of the trade
     */
    private void completedTradeChanges(String tradeId){
        Map<String, List<String>> itemToUsers = tradeModel.getTradeManager().itemToUsers(tradeId);
        for (String item: itemToUsers.keySet()){
            tradeModel.getItemManager().setConfirmedItemAvailable(item, true);
            tradeModel.getUserManager().updateCreditByUsername(itemToUsers.get(item).get(0), true);
            tradeModel.getUserManager().updateCreditByUsername(itemToUsers.get(item).get(1), false);
        }
    }
}