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

    public ConfirmTradesController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ConfirmTradesPresenter(tradeModel);
    }

    @Override
    public void run() {
        try {
            presenter.startMenu();
            String input = br.readLine();
            while (!input.equals("exit")) {
                confirmTrades();
            }
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }

    private void confirmTrades() throws IOException {
        List<String> trades = tradeModel.getTradeManager().getToBeConfirmedTrades(username);
        for (String tradeId : trades) {
            presenter.showTrade(tradeId);
            String input = br.readLine();
            switch (input) {
                case "1": // confirm trade
                    confirmTrade(tradeId);
                    break;
                case "2":
                    break;
                case "exit":
                    presenter.end();
                    presenter.startMenu();
                default:
                    presenter.invalidInput();
            }
        }
    }

    private void confirmTrade(String tradeId) {
        if (tradeModel.getTradeManager().canChangeMeeting(tradeId, username)) {
            tradeModel.getTradeManager().confirmMeetingHappened(tradeId, username);
        } else {
            presenter.declineConfirm();
        }

        if (tradeModel.getTradeManager().needToAddMeeting(tradeId)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(tradeModel.getTradeManager().getLastConfirmedTime(tradeId));
            cal.add(Calendar.DATE, 30);
            Date newDate = cal.getTime();
            tradeModel.getTradeManager().addMeetingToTrade(tradeId,
                    tradeModel.getTradeManager().getTradeLastMeetingLocation(tradeId), newDate, username);
            presenter.displayNewDate(newDate.toString());
        }

        Map<String, List<String>> itemToUsers = tradeModel.getTradeManager().itemToUsers(tradeId);

        if (itemToUsers.size() == 1) {
            for (String item : itemToUsers.keySet()) {
                tradeModel.getUserManager().updateCreditByUsername(itemToUsers.get(item).get(0), true);
                tradeModel.getUserManager().updateCreditByUsername(itemToUsers.get(item).get(1), false);
            }
        }
    }
}