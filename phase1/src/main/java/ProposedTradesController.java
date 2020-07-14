import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ProposedTradesController implements RunnableController {
    private final BufferedReader br;
    TradeModel tradeModel;
    ProposedTradesPresenter presenter;
    private String username;

    public ProposedTradesController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ProposedTradesPresenter();
    }

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
            presenter.showMeeting(tradeId, tradeModel.getTradeManager().getTradeAllInfo(tradeId));
            String input = br.readLine();
            switch(input) {
                case "1": // confirm meeting times
                    confirmMeetingTime(tradeId, trades.get(tradeId));
                    break;
                case "2": // edit meeting time
                    editMeetingTime(tradeId);
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

    private void confirmMeetingTime(String tradeId, String type) {
        if (tradeModel.getTradeManager().canChangeMeeting(tradeId, username)) {
            tradeModel.getTradeManager().agreeMeetingDetails(tradeId);
            changeItems(tradeId, type);
            presenter.confirmedMeeting();
        } else {
            presenter.declineConfirmMeeting();
        }
    }

    private void changeItems(String tradeId, String type) {
        Map<String, List<String>> itemToUsers = tradeModel.getTradeManager().itemToUsers(tradeId);
        if (type.equals("permanent")) {
            for (String item : itemToUsers.keySet()) {
                tradeModel.getUserManager().removeFromSet(itemToUsers.get(item).get(1), item, ItemSets.WISHLIST);
                tradeModel.getUserManager().removeFromSet(itemToUsers.get(item).get(0), item, ItemSets.INVENTORY);
                tradeModel.getUserManager().addToSet(itemToUsers.get(item).get(1), item, ItemSets.INVENTORY);
                tradeModel.getTradeManager().deleteCommonItemTrades(item, tradeId);
                tradeModel.getItemManager().setOwner(item, itemToUsers.get(item).get(1));
                tradeModel.getItemManager().setConfirmedItemAvailable(item, false);
            }
        } else {
            for (String item : itemToUsers.keySet()) {
                tradeModel.getUserManager().removeFromSet(itemToUsers.get(item).get(1), item, ItemSets.WISHLIST);
                tradeModel.getItemManager().setConfirmedItemAvailable(item, false);
                tradeModel.getTradeManager().deleteCommonItemTrades(item, tradeId); // please check this!
            } //itemToUsers(tradeId) -> map<itemId, List<String giver, String receiver>
        }
    }

    private void editMeetingTime(String tradeId) throws IOException {
        if (tradeModel.getTradeManager().needCancelTrade(tradeId)) {
            tradeModel.getTradeManager().cancelTrade(tradeId);
            presenter.canceledTrade();
        } else {
            if (tradeModel.getTradeManager().canChangeMeeting(tradeId, username)) {
                presenter.enterLocation();
                String location = br.readLine();
                presenter.enterDateTime();
                String dateString = br.readLine();

                Date convertedDate = null;
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                try {
                    convertedDate = format.parse(dateString);
                } catch (ParseException e) {
                    presenter.invalidDateTime();
                    editMeetingTime(tradeId);
                }
                tradeModel.getTradeManager().changeMeetingOfTrade(tradeId, location, convertedDate, username);
                presenter.editedMeeting();
            } else {
                presenter.declineEditMeeting();
            }
        }
    }
}
