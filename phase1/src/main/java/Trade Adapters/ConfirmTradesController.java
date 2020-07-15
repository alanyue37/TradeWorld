import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ConfirmTradesController implements RunnableController {
    private final BufferedReader br;
    TradeModel tradeModel;
    ConfirmTradesPresenter presenter;
    private String username;

    /**
     * Initiates ConfirmTradesController
     */
    public ConfirmTradesController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ConfirmTradesPresenter();
    }

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
            presenter.showTrade(tradeId, tradeModel.getTradeManager().getTradeAllInfo(tradeId));
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

    private void completedTradeChanges(String tradeId){
        Map<String, List<String>> itemToUsers = tradeModel.getTradeManager().itemToUsers(tradeId);
        for (String item: itemToUsers.keySet()){
            tradeModel.getItemManager().setConfirmedItemAvailable(item, true);
            tradeModel.getUserManager().updateCreditByUsername(itemToUsers.get(item).get(0), true);
            tradeModel.getUserManager().updateCreditByUsername(itemToUsers.get(item).get(1), false);
        }
    }
}