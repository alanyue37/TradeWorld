package tradeadapters;

import org.json.JSONException;
import org.json.JSONObject;
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
     *
     * @param tradeModel tradeModel
     * @param username   *username* of user
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
        } catch (IOException | JSONException e) {
            System.out.println("Something bad happened.");
        }
    }

    private boolean browseMeetings() throws IOException, JSONException {
        List<String> trades = tradeModel.getMeetingManager().getToCheckTrades(tradeModel.getTradeManager().getTradesOfUser(username, "ongoing"), "proposed");
        for (String tradeId : trades) {
            List<JSONObject> allTrade = new ArrayList<>();
            allTrade.add(tradeModel.getTradeManager().getTradeInfo(tradeId));
            allTrade.addAll(tradeModel.getMeetingManager().getMeetingsInfo(tradeId));
            StringBuilder allTradeInfo = new StringBuilder();
            for (JSONObject details : allTrade) {
                allTradeInfo.append(details.toString(4));
            }
            presenter.showMeeting(allTradeInfo.toString());
            String input = br.readLine();
            switch (input) {
                case "1": // confirm meeting times
                    confirmMeetingTime(tradeId);
                    break;
                case "2": // edit meeting time
                    if (tradeModel.getMeetingManager().canChangeMeeting(tradeId, username)) {
                        List<String> details = getMeetingDetails(tradeId);
                        if (details.size() > 0) {
                            editMeetingTime(tradeId, details); }
                    } else {
                        presenter.declineEditMeeting();
                    }
                    break;
                case "exit":
                    presenter.end();
                    return false;
                default:
                    presenter.tryAgain();
            }
        }
        presenter.endMeetings();
        return true;
    }

    /**
     * Allows user to confirm with the suggested meeting details (time, place)
     *
     * @param tradeId id of the trade
     */
    private void confirmMeetingTime(String tradeId) {
        if (tradeModel.getMeetingManager().canChangeMeeting(tradeId, username)) {
            String meetingId = tradeModel.getTradeManager().getMeetingOfTrade(tradeId).get(tradeModel.getTradeManager().getMeetingOfTrade(tradeId).size() - 1);
            tradeModel.getMeetingManager().confirmAgreement(meetingId);
            changeItemUnavailable(tradeId);
            presenter.confirmedMeeting();
        } else {
            presenter.declineConfirmMeeting();
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


//    private StringBuilder formatTradeInfo(List<JSONObject> allTrade) throws JSONException {
//        StringBuilder allTradeInfo = new StringBuilder();
//        int i = 0;
//        while (i < 1) {
//            allTradeInfo.append("Trade ID: ").append(allTrade.get(i).get("Trade ID"));
//            allTradeInfo.append("\nType: ").append(allTrade.get(i).get("Type"));
//            allTradeInfo.append("\nStatus: ").append(allTrade.get(i).get("Status"));
//            allTradeInfo.append("\nNumber of meetings: ").append(allTrade.get(i).get("Number of meetings"));
//            allTradeInfo.append("\nCreation Date: ").append(allTrade.get(i).get("Creation Date"));
//            allTradeInfo.append("\nUsers involved: ").append(allTrade.get(i).get("Users involved"));
//            allTradeInfo.append("\nItems involved: ").append(allTrade.get(i).get("Items involved"));
//            i += 1;
//        }
//        while (i < allTrade.size()) {
//            allTradeInfo.append("\nMeeting\nMeeting ID: ").append(allTrade.get(i).get("Meeting ID"));
//            allTradeInfo.append("\nStatus: ").append(allTrade.get(i).get("Status"));
//            allTradeInfo.append("\nLocation: ").append(allTrade.get(i).get("Location"));
//            allTradeInfo.append("\nTime: ").append(allTrade.get(i).get("Time"));
//            allTradeInfo.append("\nNumber of Edits: ").append(allTrade.get(i).get("Number of Edits"));
//            allTradeInfo.append("\nNumber of Confirmations: ").append(allTrade.get(i).get("Number of Confirmations"));
//            allTradeInfo.append("\nLast user who modified the meeting: ").append(allTrade.get(i).get("Last user who modified the meeting"));
//            i += 1;
//        }
//        return allTradeInfo;
//    }

    private List<String> getMeetingDetails(String tradeId) throws IOException {
        if (tradeModel.getMeetingManager().attainedThresholdEdits(tradeModel.getTradeManager().getMeetingOfTrade(tradeId).get(0))) {
            tradeModel.getTradeManager().cancelTrade(tradeId);
            tradeModel.getMeetingManager().cancelMeetingsOfTrade(tradeId);
            presenter.canceledTrade();
            return new ArrayList<>();
        } else {
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
                } catch (ParseException e) {
                    presenter.tryAgain();
                }
            } while (date == null);
            details.add(dateString);
            return details;
        }
    }


    private void editMeetingTime(String tradeId, List<String> details) {
        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date time = format.parse(details.get(1));
            String meetingId = tradeModel.getTradeManager().getMeetingOfTrade(tradeId).get(tradeModel.getTradeManager().getMeetingOfTrade(tradeId).size() - 1);
            tradeModel.getMeetingManager().changeMeeting(meetingId, details.get(0), time, username);
            presenter.editedMeeting();
        } catch (ParseException e) {
            System.out.println("Invalid date and time!");
        }
    }
}
