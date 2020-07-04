import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class TradeManager implements Serializable {
    private int limitOfEdits;
    private ArrayList<Trade> listOfTrades;
    private MeetingManager meetingManager;
    private int limitOfIncomplete; // should we put this here? or should we get it from somewhere else?

    // need constructor

    // getTrade(tradeid)

    public void addTrade(Trade newTrade){
        this.listOfTrades.add(newTrade);
    }

    public ArrayList<Trade> getAllTrades(){
        return this.listOfTrades;
    }

    public int getLimitEdits(){
        return this.limitOfEdits;
    }

    public void changeLimitEdits(int newLimit){
        this.limitOfEdits = newLimit;
    }

    public ArrayList<Trade> getOpenTrades(){
        ArrayList<Trade> openTrades = new ArrayList<Trade>();
        for (Trade listOfTrade : this.listOfTrades) {
            if (listOfTrade.getIsOpened()) {
                openTrades.add(listOfTrade);
            }
        }
        return openTrades;
    }

    private boolean isIncompleteTrade(Trade openTrade) {
        if (openTrade.getTradeType().equals("permanent")) {
            if (meetingManager.isIncompleteMeeting(openTrade.getMeetingList().get(0))) {
                return true;
            }
        }
        if (openTrade.getTradeType().equals("temporary")){
            if (meetingManager.isIncompleteMeeting(openTrade.getMeetingList().get(0))){
                return true;
            }
            if (openTrade.getMeetingList().get(0).getIsCompleted()){
                return meetingManager.isIncompleteMeeting(openTrade.getMeetingList().get(1));
            }
        }
        return false;
    }

    public ArrayList<Trade> getIncompleteTrade(){
        ArrayList<Trade> openTrades = getOpenTrades();
        ArrayList<Trade> incompleteTrades = new ArrayList<Trade>();
        for (Trade openTrade : openTrades) {
            if (isIncompleteTrade(openTrade)) {
                incompleteTrades.add(openTrade);
            }
        }
        return incompleteTrades;
    }

    // limit of edits?

    private HashMap<String, Integer> getIncompleteUsernamesOneWay(ArrayList<Trade> trades) {
        HashMap<String, Integer> usernameCount = new HashMap<>();
        for (Trade trade : trades) {
            if (trade instanceof OneWayTrade) {
                String giver = ((OneWayTrade) trade).getGiverUsername();
                String receiver = ((OneWayTrade) trade).getReceiverUsername();
                if (!(usernameCount.containsKey(giver))) {
                    usernameCount.put(giver, 1);
                } else {
                    usernameCount.replace(giver, usernameCount.get(giver) + 1);
                }
                if (!(usernameCount.containsKey(receiver))) {
                    usernameCount.put(receiver, 1);
                } else {
                    usernameCount.replace(receiver, usernameCount.get(receiver) + 1);
                }
            }
        } return usernameCount;
    }

    private HashMap<String, Integer> getIncompleteUsernamesTwoWay(ArrayList<Trade> trades) {
        HashMap<String, Integer> usernameCount = new HashMap<>();
        for (Trade trade : trades) {
            if (trade instanceof TwoWayTrade) {
                String user1 = ((TwoWayTrade) trade).getUser1();
                String user2 = ((TwoWayTrade) trade).getUser2();
                if (!(usernameCount.containsKey(user1))) {
                    usernameCount.put(user1, 1);
                } else {
                    usernameCount.replace(user1, usernameCount.get(user1) + 1);
                }
                if (!(usernameCount.containsKey(user2))) {
                    usernameCount.put(user2, 1);
                } else {
                    usernameCount.replace(user2, usernameCount.get(user2) + 1);
                }
            }
        } return usernameCount;
    }

    public ArrayList<String> getIncompleteUsernames() {
        ArrayList<Trade> incompleteTrades = getIncompleteTrade();
        HashMap<String, Integer> usernamesOneWay = getIncompleteUsernamesOneWay(incompleteTrades);
        HashMap<String, Integer> usernamesTwoWay = getIncompleteUsernamesTwoWay(incompleteTrades);
        ArrayList<String> incompleteUsernames = new ArrayList<>();
        for (String user : usernamesOneWay.keySet()) {
            if (usernamesOneWay.get(user) > limitOfIncomplete) {
                incompleteUsernames.add(user);
            }
        }
        for (String user : usernamesTwoWay.keySet()) {
            if (usernamesTwoWay.get(user) > limitOfIncomplete) {
                incompleteUsernames.add(user);
            }
        } return incompleteUsernames;
    }

    private Date getLastConfirmedMeetingTime(Trade trade){
        if (trade.getMeetingList().isEmpty()){
            return null;
        }
        for (int i= trade.getMeetingList().size() - 1; i >= 0; i--){
            if (meetingManager.getConfirmedMeetingTime(trade.getMeetingList().get(i)) != null){
                return meetingManager.getConfirmedMeetingTime(trade.getMeetingList().get(i));
            }
        }
        return meetingManager.getConfirmedMeetingTime(trade.getMeetingList().get(0));
    }


    public ArrayList<Trade> getTradesPastDays(int numDays){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime numDaysBefore = now.minusDays(numDays);
        Date comparisonDate = new Date(numDaysBefore.getYear() - 1900, numDaysBefore.getMonthValue() -1, numDaysBefore.getDayOfMonth());
        ArrayList<Trade> tradeInPastDays = new ArrayList<Trade>();
        for (Trade listOfTrade : this.listOfTrades) {
            if (getLastConfirmedMeetingTime(listOfTrade) != null) {
                if (getLastConfirmedMeetingTime(listOfTrade).after(comparisonDate)) {
                    tradeInPastDays.add(listOfTrade);
                }
            }
        }
        return tradeInPastDays;
    }

    // make it so that they both have to confirm the first meeting before going to the next one?
    // make it so that they have to enter a date for meeting when they create the trade?



    // do we want given # of days (i.e always 7 days from today) or like a time period

    // transaction = trade or meeting?

}