package tradeadapters;

import tradegateway.TradeModel;
import trademisc.RunnableController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Manages input from the user to confirm or edit the meeting time(s) of proposed trades.
 */
public class ProposedTradesController implements RunnableController {
    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final ProposedTradesPresenter presenter;
    private final String username;

    /**
     * Initiates the ProposedTradesController.
     * @param tradeModel tradeModel
     * @param username *username* of user
     */
    public ProposedTradesController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ProposedTradesPresenter();
    }

    /**
     * Overrides run() method in the trademisc.RunnableController interface.
     */
    @Override
    public void run() {
        try {
            browseMeetings();
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }

    private boolean browseMeetings() throws IOException {
        Map<String, String> trades = tradeModel.getTradeManager().getProposedTrades(username);

        for (String tradeId : trades.keySet()) {
            presenter.showMeeting(tradeModel.getTradeManager().getTradeAllInfo(tradeId));
            String input = br.readLine();
            switch(input) {
                case "1": // confirm meeting times
                    confirmMeetingTime(tradeId);
                    break;
                case "2": // edit meeting time
                    if (tradeModel.getTradeManager().canChangeMeeting(tradeId, username)) {
                        List<String> details = getMeetingDetails();
                        editMeetingTime(tradeId, details);
                    } else { presenter.declineEditMeeting(); }
                    break;
                case "exit":
                    presenter.end();
                    return false;
                default:
                    presenter.tryAgain();
            }
        } presenter.endMeetings();
        return true;
    }

    /**
     * Allows user to confirm with the suggested meeting details (time, place)
     * @param tradeId id of the trade
     */
    private void confirmMeetingTime(String tradeId) {
        if (tradeModel.getTradeManager().canChangeMeeting(tradeId, username)) {
            tradeModel.getTradeManager().agreeMeetingDetails(tradeId);
            changeItemUnavailable(tradeId);
            presenter.confirmedMeeting();
        } else {
            presenter.declineConfirmMeeting();
        }
    }

    /**
     * Makes the changes that are necessary for availability of the items of the users when the trade
     * meeting time/place is confirmed.
     * @param tradeId id of the trade
     */
    private void changeItemUnavailable(String tradeId) {
        Map<String, List<String>> itemToUsers = tradeModel.getTradeManager().itemToUsers(tradeId);
        for (String item : itemToUsers.keySet()) {
            tradeModel.getItemManager().setConfirmedItemAvailable(item, false);
            tradeModel.getTradeManager().deleteCommonItemTrades(item, tradeId);
        }
    }

    private List<String> getMeetingDetails() throws IOException {
        List<String> details = new ArrayList<>();
        presenter.enterLocation();
        String location = br.readLine();
        details.add(location);

        String dateString = null;
        Date date = null;
        do {
            try {
                presenter.enterDateTime();
                dateString = br.readLine();
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                date = format.parse(dateString); // check formatting is valid
            }
            catch (ParseException e) {
                presenter.tryAgain();
            }
        } while (date == null);
        details.add(dateString);
        return details;
    }

    private void editMeetingTime(String tradeId, List<String> details) {
        if (tradeModel.getTradeManager().needCancelTrade(tradeId)) {
            tradeModel.getTradeManager().cancelTrade(tradeId);
            presenter.canceledTrade();
        } else {
            try {
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date time = format.parse(details.get(1));
                tradeModel.getTradeManager().editMeetingOfTrade(tradeId, details.get(0), time, username, "edit");
                presenter.editedMeeting();
            } catch (ParseException e) {
                System.out.println("Invalid date and time!");
            }
        }
    }
}
