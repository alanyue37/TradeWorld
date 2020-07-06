import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TradeManager implements Serializable {
    private int limitOfEdits;
    private HashMap<Integer, Trade> allTrades;
    private MeetingManager meetingManager;
    private int limitOfIncomplete; // should we put this here? or should we get it from somewhere else?
    private HashMap<Integer, Trade> completedTrades;

    /**
     * Agree to meeting details (time, location) of the trade with id "tradeId"
     * @param tradeId id of the trade
     */
    public void agreeMeeting(int tradeId) {
        Trade trade = allTrades.get(tradeId);
        int i = 0;
        while (meetingManager.getExchangeConfirmed(trade.getMeetingList().get(i))){
            i += 1;
        }
        Meeting meeting = trade.getMeetingList().get(i);
        meetingManager.confirmAgreement(meeting);
    }

    /**
     * Confirms that the meeting in real life happened.
     * @param tradeId id of the trade of which we want to confirm the meeting
     */
    public void confirmMeetingHappened(int tradeId){
        Trade trade = this.allTrades.get(tradeId);
        Meeting meeting = getLastConfirmedMeeting(tradeId);
        assert meeting != null; // not really necessary? since the user can only confirm meeting happened after they set up a time for the meeting?
        meetingManager.meetingHappened(meeting);
        if (meetingManager.getMeetingCompleted(meeting)){
            if (canClose(tradeId)){
                this.allTrades.get(tradeId).changeIsOpened();
            }
        }
    }

    /**
     * Return the meeting in trade with trade id "tradeId" that was last confirmed. If the trade does not have any
     * confirmed meeting (time, location), then it returns null.
     * @param tradeId id of the trade
     * @return meeting of trade that the meeting time and place was last confirmed. If no meeting's time/place has been
     * confirmed for the trade, returns null.
     */
    private Meeting getLastConfirmedMeeting(int tradeId){
        Trade trade = this.allTrades.get(tradeId);
        for (int i = 0; i < trade.getMeetingList().size(); i++){
            if (getLastConfirmedMeetingTime(trade) == trade.getMeetingList().get(i).getTime()){
                return trade.getMeetingList().get(i);
            }
        }
        return null;
    }

    /**
     * Given the tradeId, returns whether this trade can be closed or not.
     * @param tradeId id of the trade
     * @return true iff all meetings of the trade are completed (both user have confirmed the meeting happened in real
     * life). If the trade is permanent, one meeting should be completed. If the trade is temporary, both meeting are
     * completed.
     */
    private boolean canClose(int tradeId){
        Trade trade = this.allTrades.get(tradeId);
        for (int i =0; i < trade.getMeetingList().size(); i++){
            if (!trade.getMeetingList().get(i).getIsCompleted()){
                return false;
            }
        }
        if (trade.getTradeType().equals("permanent")){
            return (trade.getMeetingList().size() == 1);
        }
        if (trade.getTradeType().equals("temporary")){
            return trade.getMeetingList().size() == 2;
        }
        return false;
    }

    /**
     * Given the tradeId, returns a hashmap that maps the user's Username to the item they own. If it is a one-way
     * trade, then one user will map to null, indicating they are not borrowing any item.
     * @param tradeId id of the trade
     * @return hashmap which maps user (of the trade) to their item.
     */
    public HashMap<String, String> getUserAndItem(int tradeId){
        return this.allTrades.get(tradeId).userToItem();
    }


    /**
     * Add a trade to the Hashmap of trades in TradeManager.
     * @param newTrade trade to be added
     */
    public void addTrade(Trade newTrade) {
        this.allTrades.put(newTrade.getIdOfTrade(), newTrade);
    }
     //create trade??????

    /**
     * get a map of all the trades
     * @return hashmap of all trades
     */
    public HashMap<Integer, Trade> getAllTrades() {
        return this.allTrades;
    }

    /**
     * getter for the limit of edit that a user can suggest before the trade cancels
     * @return the limit of edits to the meeting time/place for each user
     */
    public int getLimitEdits() {
        return this.limitOfEdits;
    }

    /**
     * Change the limit of edit that a user can suggest before the trade cancels
     * @param newLimit new value to limit of edits
     */
    public void changeLimitEdits(int newLimit) {
        this.limitOfEdits = newLimit;
    }

    /**
     * get all the open trades.
     * @return arrayList of open trades.
     */
    public ArrayList<Trade> getOpenTrades() {
        ArrayList<Trade> openTrades = new ArrayList<Trade>();
        for (Trade trade : this.allTrades.values()) {
            if (trade.getIsOpened()) {
                openTrades.add(trade);
            }
        }
        return openTrades;
    }

    /**
     * Given an open trade, return whether an trade is incomplete.
     * @param openTrade open trade input
     * @return true if the trade is open and has incomplete meeting, return false if the trade does not contain
     * any incomplete meeting
     */
    private boolean isIncompleteTrade(Trade openTrade) {
        if (openTrade.getTradeType().equals("permanent")) {
            if (meetingManager.isIncompleteMeeting(openTrade.getMeetingList().get(0))) {
                return true;
            }
        }
        if (openTrade.getTradeType().equals("temporary")) {
            if (meetingManager.isIncompleteMeeting(openTrade.getMeetingList().get(0))) {
                return true;
            }
            if (openTrade.getMeetingList().get(0).getIsCompleted()) {
                return meetingManager.isIncompleteMeeting(openTrade.getMeetingList().get(1));
            }
        }
        return false;
    }

    /**
     * Return arraylist of incomplete trades (trade that contains incomplete meetings)
     * @return arraylist of incomplete trades
     */
    public ArrayList<Trade> getIncompleteTrade() {
        ArrayList<Trade> openTrades = getOpenTrades();
        ArrayList<Trade> incompleteTrades = new ArrayList<>();
        for (Trade openTrade : openTrades) {
            if (isIncompleteTrade(openTrade)) {
                incompleteTrades.add(openTrade);
            }
        }
        return incompleteTrades;
    }

     //limit of edits?

//    private HashMap<String, Integer> getIncompleteUsernamesOneWay(ArrayList<Trade> trades) {
//        HashMap<String, Integer> usernameCount = new HashMap<>();
//        for (Trade trade : trades) {
//            if (trade instanceof OneWayTrade) {
//                String giver = ((OneWayTrade) trade).getGiverUsername();
//                String receiver = ((OneWayTrade) trade).getReceiverUsername();
//                if (!(usernameCount.containsKey(giver))) {
//                    usernameCount.put(giver, 1);
//                } else {
//                    usernameCount.replace(giver, usernameCount.get(giver) + 1);
//                }
//                if (!(usernameCount.containsKey(receiver))) {
//                    usernameCount.put(receiver, 1);
//                } else {
//                    usernameCount.replace(receiver, usernameCount.get(receiver) + 1);
//                }
//            }
//        }
//        return usernameCount;
//    }
//
//    private HashMap<String, Integer> getIncompleteUsernamesTwoWay(ArrayList<Trade> trades) {
//        HashMap<String, Integer> usernameCount = new HashMap<>();
//        for (Trade trade : trades) {
//            if (trade instanceof TwoWayTrade) {
//                String user1 = ((TwoWayTrade) trade).getUser1();
//                String user2 = ((TwoWayTrade) trade).getUser2();
//                if (!(usernameCount.containsKey(user1))) {
//                    usernameCount.put(user1, 1);
//                } else {
//                    usernameCount.replace(user1, usernameCount.get(user1) + 1);
//                }
//                if (!(usernameCount.containsKey(user2))) {
//                    usernameCount.put(user2, 1);
//                } else {
//                    usernameCount.replace(user2, usernameCount.get(user2) + 1);
//                }
//            }
//        }
//        return usernameCount;
//    }

//    public ArrayList<String> getIncompleteUsernames() {
//        ArrayList<Trade> incompleteTrades = getIncompleteTrade();
//        HashMap<String, Integer> usernamesOneWay = getIncompleteUsernamesOneWay(incompleteTrades);
//        HashMap<String, Integer> usernamesTwoWay = getIncompleteUsernamesTwoWay(incompleteTrades);
//        ArrayList<String> incompleteUsernames = new ArrayList<>();
//        for (String user : usernamesOneWay.keySet()) {
//            if (usernamesOneWay.get(user) > limitOfIncomplete) {
//                incompleteUsernames.add(user);
//            }
//        }
//        for (String user : usernamesTwoWay.keySet()) {
//            if (usernamesTwoWay.get(user) > limitOfIncomplete) {
//                incompleteUsernames.add(user);
//            }
//        }
//        return incompleteUsernames;
//    }

    /**
     * Given the trade, return the date of the last confirmed meeting (time, location is confirmed) if there is any
     * confirmed meeting. If none of the meeting of the trade is confirmed yet, then return null.
     * @param trade trade to get the date of the last confirmed meeting from
     * @return the date of the last confirmed meeting. If none of the meeting inside trade have been confirmed
     * (time, location) yet, return null.
     */
    private Date getLastConfirmedMeetingTime(Trade trade) {
        if (trade.getMeetingList().isEmpty()) {
            return null;
        }
        for (int i = trade.getMeetingList().size() - 1; i >= 0; i--) {
            if (meetingManager.getConfirmedMeetingTime(trade.getMeetingList().get(i)) != null) {
                return meetingManager.getConfirmedMeetingTime(trade.getMeetingList().get(i));
            }
        }
        return meetingManager.getConfirmedMeetingTime(trade.getMeetingList().get(0));
    }

    /**
     * Given the number of days, returns a list of trades that have had meetings happened in the past numOfDays.
     * @param numDays number of days
     * @return arraylist of trades that are from past numDays (trades that have meetings that user confirmed happened
     * in real life and the date is in the past few numOfDays)
     */
    public ArrayList<Trade> getTradesPastDays(int numDays) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime numDaysBefore = now.minusDays(numDays);
        Date comparisonDate = new Date(numDaysBefore.getYear() - 1900, numDaysBefore.getMonthValue() - 1, numDaysBefore.getDayOfMonth());
        ArrayList<Trade> tradeInPastDays = new ArrayList<>();
        for (Trade trade : this.allTrades.values()) {
            if (getLastConfirmedMeetingTime(trade) != null) {
                if (getLastConfirmedMeetingTime(trade).after(comparisonDate)) {
                    tradeInPastDays.add(trade);
                }
            }
        }
        return tradeInPastDays;
    }

    // make it so that they both have to confirm the first meeting before going to the next one?
    // make it so that they have to enter a date for meeting when they create the trade?

    /**
     * cancel the trade. Remove the trade from the hashmap of trades
     * @param tradeId id of the trade to be removed from hashmap of trade
     */
    public void cancelTrade(int tradeId) {
        this.allTrades.remove(tradeId);
    }
    // or should we have something that keeps track of all the cancelled trades?

    /**
     * Given an username, return all trades of this user.
     * @param username username of the user we want to get all trade
     * @return arraylist of trades of user with username "username"
     */
    public ArrayList<Trade> getTradeOfUser(String username) {
        ArrayList<Trade> userTrades = new ArrayList<>();
        for (Trade trade : this.allTrades.values()) {
            if (trade.getUsers().contains(username)) {
                userTrades.add(trade);
            }
        }
        return userTrades;
    }

    /**
     * Given the tradeId, return whether this trade should be cancelled. For a permanent trade, a trade is cancelled if
     * they are still in disagreement with the meeting details and have attained the limit of edits allowed. For a
     * temporary trade, a trade is cancelled only if they cannot agree on the first meeting and have reached the limit
     * of edits.
     * @param tradeId
     * @return
     */
    public boolean needCancelTrade(int tradeId){
        Meeting meeting = this.allTrades.get(tradeId).getMeetingList().get(0);
        if (!(meetingManager.getExchangeConfirmed(meeting))){
            return meetingManager.attainedThresholdEdits(meeting, this.limitOfEdits*2);
        }
        return false;
    }
    // what if its temporary, and the returning meeting exceeds the limit?


    // do we want given # of days (i.e always 7 days from today) or like a time period

    // transaction = trade or meeting?

}