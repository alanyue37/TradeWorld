import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the creation and handling of trades.
 */
public class TradeManager implements Serializable {
    private int limitOfEdits;
    private int limitIncomplete;
    private int limitTransactionPerWeek;
    private Map<String, Trade> ongoingTrades;
    private MeetingManager meetingManager;
    private Map<String, Trade> completedTrades;
    private Map<String, List<String>> userToTrades;
    private final AtomicInteger counter = new AtomicInteger();


    /**
     * Constructor for TradeManager.
     */
    public TradeManager() {
        this.limitOfEdits = 3;
        this.limitIncomplete = 5; //or null?
        this.limitTransactionPerWeek = 10; //or null?
        this.ongoingTrades = new HashMap<>();
        this.meetingManager = new MeetingManager();
        this.completedTrades = new HashMap<>();
        this.userToTrades = new HashMap<>();
    }

    /**
     * Agrees to meeting details (time, location) of the trade with ID "tradeId"
     *
     * @param tradeId ID of the trade
     */
    public void agreeMeetingDetails(String tradeId) {
        Trade trade = ongoingTrades.get(tradeId);
        int i = 0;
        while (meetingManager.getMeetingStatus(trade.getMeetingList().get(i)) != 0) {
            i += 1;
        }
        meetingManager.confirmAgreement(trade.getMeetingList().get(i));
    }

    /**
     * Confirms that the meeting in real life happened.
     *
     * @param tradeId  ID of the trade of which we want to confirm the meeting
     * @param username username of the user who confirms the meeting happened
     */
    public void confirmMeetingHappened(String tradeId, String username) {
        Trade trade = this.ongoingTrades.get(tradeId);
        Meeting meeting = getLastConfirmedMeeting(tradeId);
        assert meeting != null; // not really necessary? since the user can only confirm meeting happened after they set up a time for the meeting?
        meetingManager.meetingHappened(meeting, username);
        if (meetingManager.getMeetingCompleted(meeting)) {
            if (canClose(trade)) {
                this.ongoingTrades.get(tradeId).changeIsOpened();
                this.completedTrades.put(tradeId, trade);
                this.ongoingTrades.remove(tradeId);
            }
        }
    }

    /**
     * Return the meeting in trade with trade ID "tradeId" that was last confirmed. If the trade does not have any
     * confirmed meeting (time, location), then it returns null.
     *
     * @param tradeId ID of the trade
     * @return meeting of trade that the meeting time and place was last confirmed. If no meeting's time/place has been
     * confirmed for the trade, returns null.
     */
    private Meeting getLastConfirmedMeeting(String tradeId) {
        Trade trade = getTrade(tradeId);
        int i = trade.getMeetingList().size() - 1;
        if (i == 0) {
            if (trade.getMeetingList().get(i).getIsConfirmed()) {
                return trade.getMeetingList().get(i);
            } else {
                return null;
            }
        } else { // i == 1
            if (trade.getMeetingList().get(i).getIsConfirmed()) {
                return trade.getMeetingList().get(i);
            } else {
                return trade.getMeetingList().get(0);
            }
        }
    }

    /**
     * Given tradeId, return the trade
     *
     * @param tradeId ID of the trade
     * @return trade with the ID "tradeId"
     */
    private Trade getTrade(String tradeId) {
        if (this.ongoingTrades.containsKey(tradeId)) {
            return ongoingTrades.get(tradeId);
        }
        return completedTrades.get(tradeId);
    }

    /**
     * Given the tradeId, returns whether this trade can be closed or not
     *
     * @param trade ID of the trade
     * @return true iff all meetings of the trade are completed (both user have confirmed the meeting happened in real
     * life). If the trade is permanent, one meeting should be completed. If the trade is temporary, both meeting are
     * completed.
     */
    private boolean canClose(Trade trade) {
        for (int i = 0; i < trade.getMeetingList().size(); i++) {
            if (!trade.getMeetingList().get(i).getIsCompleted()) {
                return false;
            }
        }
        if (trade.getTradeType().equals("permanent")) {
            return (trade.getMeetingList().size() == 1);
        }
        if (trade.getTradeType().equals("temporary")) {
            return trade.getMeetingList().size() == 2;
        }
        return false;
    }

    /**
     * Given the tradeId, returns a hashmap that maps the user's Username to the item they own. If it is a one-way
     * trade, then one user will map to null, indicating they are not giving any item.
     *
     * @param tradeId ID of the trade
     * @return hashmap which maps user (of the trade) to their item
     */
    public Map<String, List<String>> itemToUsers(String tradeId) {
        return getTrade(tradeId).itemToTrader();
    }


    /**
     * Adds a one way trade to the hashmap of trades in TradeManager.
     *
     * @param type     permanent or temporary
     * @param giver    username of the person giving the object
     * @param receiver username of the person receiving the object
     * @param itemId   id of the item
     */
    public String addOneWayTrade(String type, String giver, String receiver, String itemId) {
        String id = String.valueOf(counter.getAndIncrement());
        Trade trade = new OneWayTrade(type, id, giver, receiver, itemId);
        this.ongoingTrades.put(trade.getIdOfTrade(), trade);
        for (String user : trade.getUsers()) {
            if (userToTrades.containsKey(user)) {
                userToTrades.get(user).add(trade.getIdOfTrade());
            } else {
                List<String> tradeIds = new ArrayList<>();
                tradeIds.add(trade.getIdOfTrade());
                userToTrades.put(user, tradeIds);
            }
        }
        return trade.getIdOfTrade();
    }

    /**
     * Adds a two way trade to the hashmap of trades in TradeManager.
     *
     * @param type  permanent or temporary
     * @param user1 username of user1 involved in trade
     * @param user2 username of the other user
     * @param item1 item of user1
     * @param item2 item of user2
     */
    public String addTwoWayTrade(String type, String user1, String user2, String item1, String item2) {
        String id = String.valueOf(counter.getAndIncrement());
        Trade trade = new TwoWayTrade(type, id, user1, user2, item1, item2);
        this.ongoingTrades.put(trade.getIdOfTrade(), trade);
        for (String user : trade.getUsers()) {
            if (userToTrades.containsKey(user)) {
                userToTrades.get(user).add(trade.getIdOfTrade());
            } else {
                List<String> tradeIds = new ArrayList<>();
                tradeIds.add(trade.getIdOfTrade());
                userToTrades.put(user, tradeIds);
            }
        }
        return trade.getIdOfTrade();
    }

    /**
     * Gets the limit of edits that users can suggest before the trade is cancelled
     *
     * @return The limit of edits to the meeting time/place
     */
    public int getLimitEdits() {
        return this.limitOfEdits;
    }

    /**
     * Change the limit of edit that users can suggest before the trade is cancelled
     *
     * @param newLimit  New limit for the edits allowed
     */
    public void changeLimitEdits(int newLimit) {
        this.limitOfEdits = newLimit;
    }

    /**
     * Gets the current limit for having incomplete transactions before the account is frozen
     * @return  The current limit of incomplete transactions
     */
    public int getLimitIncomplete() {
        return this.limitIncomplete;
    }

    /**
     * Gets the current limit of transactions per week before the account is frozen
     * @return  The current limit of transactions per week
     */
    public int getLimitTransactionPerWeek() {
        return this.limitTransactionPerWeek;
    }

    /**
     * Change the limit for incomplete transactions
     * @param newIncompleteLimit    New value of incomplete transactions
     */
    public void changeLimitIncomplete(int newIncompleteLimit) {
        this.limitIncomplete = newIncompleteLimit;
    }

    /**
     * Change the limit for transactions in one week
     * @param newLimitPerWeek   New value of transactions in one week
     */
    public void changeLimitTransactionPerWeek(int newLimitPerWeek) {
        this.limitTransactionPerWeek = newLimitPerWeek;
    }


    /**
     * Returns arraylist of incomplete trades (trade that contains incomplete meetings)
     *
     * @return arraylist of incomplete trades
     */
    private List<Trade> getIncompleteTrade() {
        List<Trade> openTrades = new ArrayList<>(this.ongoingTrades.values());
        List<Trade> incompleteTrades = new ArrayList<>();
        for (Trade openTrade : openTrades) {
            Meeting meeting = openTrade.getMeetingList().get(openTrade.getMeetingList().size() - 1);
            if (meetingManager.isIncompleteMeeting(meeting)){
                incompleteTrades.add(openTrade);
            }
        }
        return incompleteTrades;
    }

    public boolean isIncompleteTrade(String tradeId){
        if (this.ongoingTrades.containsKey(tradeId)){
            Meeting meeting = getTrade(tradeId).getMeetingList().get(getTrade(tradeId).getMeetingList().size() - 1);
            return meetingManager.isIncompleteMeeting(meeting);
        }
        return false;
    }

    /**
     * Given a list of trades, returns a hashmap which maps all the users (who took part of at least one trade in the
     * list of trades) to the number of trade they took part in.
     *
     * @param trades list of trades
     * @return a hashmap that maps the username to the number of trades that the user took part of
     */
    private Map<String, Integer> userToNumTradesInvolved(List<Trade> trades) {
        Map<String, Integer> usernameCount = new HashMap<>();
        for (Trade trade : trades) {
            for (String user : trade.getUsers()) {
                if (usernameCount.containsKey(user)) {
                    usernameCount.replace(user, usernameCount.get(user) + 1);
                } else {
                    usernameCount.put(user, 1);
                }
            }
        }
        return usernameCount;
    }

    /**
     * Returns arraylist of usernames that have surpassed the limit of incomplete trades allowed
     * before the account is frozen
     *
     * @return arraylist of usernames that passed limit of incomplete trades
     */
    public List<String> getExceedIncompleteLimitUser() {
        List<Trade> incompleteTrades = getIncompleteTrade();
        Map<String, Integer> usernamesMap = userToNumTradesInvolved(incompleteTrades);
        List<String> incompleteUsernames = new ArrayList<>();
        for (String user : usernamesMap.keySet()) {
            if (usernamesMap.get(user) > this.limitIncomplete) {
                incompleteUsernames.add(user);
            }
        }
        return incompleteUsernames;
    }

    /**
     * Given the trade, returns the date of the last confirmed meeting (time, location is confirmed) if there is any
     * confirmed meeting. If none of the meeting of the trade is confirmed yet, then returns null.
     *
     * @param tradeId trade ID to get the date of the last confirmed meeting from
     * @return the date of the last confirmed meeting. If none of the meeting(s) inside trade have been confirmed
     * (time, location) yet, return null.
     */
    public Date getLastConfirmedMeetingTime(String tradeId) {
        Trade trade = getTrade(tradeId);
        if (trade.getMeetingList().isEmpty() || getLastConfirmedMeeting(tradeId) == null) {
            return null;
        } else {
            return meetingManager.getConfirmedMeetingTime(getLastConfirmedMeeting(tradeId));
        }
    }

    public String getLastConfirmedMeetingLocation(String tradeId) {
        Trade trade = getTrade(tradeId);
        if (trade.getMeetingList().isEmpty() || getLastConfirmedMeeting(tradeId) == null) {
            return null;
        } else {
            return meetingManager.getLastLocation(getLastConfirmedMeeting(tradeId));
            }
    }

    /**
     * Given the number of days, returns a list of trades that have had trades completed in the past numOfDays
     *
     * @param numDays number of days
     * @return arraylist of trades that are completed in the past numDays.
     */
    private List<Trade> getTradesPastDays(int numDays) {
        Calendar compare = Calendar.getInstance();
        compare.add(Calendar.DATE, -7);
        Date comparisonDate = compare.getTime();
        List<Trade> tradeInPastDays = new ArrayList<>();
        for (Trade trade : this.completedTrades.values()) {
            if (trade.getMeetingList().get(trade.getMeetingList().size() - 1).getTime().after(comparisonDate)) {
                tradeInPastDays.add(trade);
            }
        }
        return tradeInPastDays;
    }

    /**
     * Get the list of usernames of users who had more then *limTrade* in the past *numDays*.
     *
     * @return arraylist of usernames
     */
    public List<String> getExceedPerWeek() {
        List<Trade> pastTrades = getTradesPastDays(7);
        Map<String, Integer> usernamesMap = userToNumTradesInvolved(pastTrades);
        List<String> exceedLimitOfTradeUsers = new ArrayList<>();
        for (String user : usernamesMap.keySet()) {
            if (usernamesMap.get(user) > this.limitTransactionPerWeek) {
                exceedLimitOfTradeUsers.add(user);
            }
        }
        return exceedLimitOfTradeUsers;
    }

    /**
     * Cancels the trade. Removes the trade from the hashmap of trades
     *
     * @param tradeId ID of the trade to be removed from hashmap of trade
     */
    public void cancelTrade(String tradeId) {
        Trade trade = getTrade(tradeId);
        for (String user : trade.getUsers()) {
            this.userToTrades.get(user).remove(tradeId);
        }
        this.ongoingTrades.remove(tradeId);
    }

    /**
     * Given an username, return all trades of this user
     *
     * @param username username of the user we want to get all trade
     * @return arraylist of trades of user with username "username"
     */

    private List<Trade> getUserTrades(String username, String type) {
        List<String> userTrades = this.userToTrades.get(username);
        List<Trade> returnList = new ArrayList<>();
        if (userTrades == null) {
            return returnList;
        }
        for (String id : userTrades) {
            if (type.equals("ongoing")) {
                if (this.ongoingTrades.containsKey(id)) {
                    returnList.add(this.ongoingTrades.get(id));
                }
            } else {
                if (this.completedTrades.containsKey(id)) {
                    returnList.add(this.ongoingTrades.get(id));
                }
            }
        } return returnList;
    }

    /**
     * Given the tradeId, return whether this trade should be cancelled. For a permanent trade, a trade is cancelled if
     * they are still in disagreement with the meeting details and have attained the limit of edits allowed. For a
     * temporary trade, a trade is cancelled only if they cannot agree on the first meeting and have reached the limit
     * of edits.
     *
     * @param tradeId id of trade
     * @return true if the trade need to be cancelled, false otherwise.
     */
    public boolean needCancelTrade(String tradeId) {
        Meeting meeting = this.ongoingTrades.get(tradeId).getMeetingList().get(0);
        if ((!meetingManager.getMeetingCompleted(meeting)) &&(!meetingManager.getExchangeConfirmed(meeting))) {
            return meetingManager.attainedThresholdEdits(meeting, this.limitOfEdits * 2);
        }
        return false;
    }
    // only looks at the first meeting

    private List<String> sortPartnersList(Map<String, Integer> partners) {
        List<String> sorted = new ArrayList<>();
        for (String partner : partners.keySet()) {
            if (sorted.size() == 0) {
                sorted.add(partner);
            } else {
                int i = 0;
                while ((i < sorted.size()) && partners.get(partner) < partners.get(sorted.get(i))) {
                    i++;
                }
                sorted.add(i, partner);
            }
        }
        return sorted;
    }
    // better sorting way? interface?

    /**
     * Returns arraylist of the top "num" most frequent trading partners of user "user"
     *
     * @param num  the number of most frequent trading partners wanted
     * @param user the username of the user to get their most frequent trading partners
     * @return arraylist of usernames that are the most frequent trading partners of user "user." If "num" is greater
     * than the number of trading partners that "user" has, all of their trading partners are returned.
     */
    public List<String> getFrequentPartners(int num, String user) {
        List<Trade> completed = getUserTrades(user, "completed");
        Map<String, Integer> partners = userToNumTradesInvolved(completed);
        partners.remove(user);
        List<String> frequentPartners = sortPartnersList(partners); // assumes that sort partners give best partner first
        if (num >= frequentPartners.size()) {
            return frequentPartners;
        }
        return frequentPartners.subList(0, num);
    }
    // only looks at completed trades


    private List<Trade> sortTradesMeetingDate(List<Trade> trades)  {
        List<Trade> sorted = new ArrayList<>();
        if (trades.size() == 0) { return sorted; }
        sorted.add(trades.get(0));
        for (int i = 1; i < trades.size(); i++) {
            int ii = 0;
            while (ii < sorted.size() && getLastConfirmedMeetingTime(sorted.get(ii).getIdOfTrade()).before(getLastConfirmedMeetingTime(trades.get(i).getIdOfTrade()))) {
                ii++;
            }
            sorted.add(ii, trades.get(i));
        }
        return sorted;
    }

    private List<String> getItemsTraded(String user) {
        List<String> items = new ArrayList<>();
        List<Trade> trades = new ArrayList<>();
        if (this.userToTrades.get(user) == null) {
            return items;
        }
        for (String tradeId : this.userToTrades.get(user)) {
            if (this.completedTrades.containsKey(tradeId)) {
                trades.add(this.completedTrades.get(tradeId));
            }
        }
        for (Trade sortedTrade : sortTradesMeetingDate(trades)) {
            if (sortedTrade instanceof OneWayTrade && user.equals(((OneWayTrade) sortedTrade).getGiverUsername())) {
                items.add(((OneWayTrade) sortedTrade).getItemId());
            } else if (sortedTrade instanceof TwoWayTrade) {
                items.add(((TwoWayTrade) sortedTrade).getItemOfUser(user));
            }
        }
        return items;
    }
    // only looks at completed trades

    /**
     * Returns arraylist of the top "num" most recently traded item IDs of user "user"
     *
     * @param num  the number of most recently traded item IDs wanted
     * @param user the username of the user to get most recently traded item IDs
     * @return arraylist of item IDs that were the most recently traded by user "user." If "num" is greater
     * than the number of items that "user" has traded, all of the items traded are returned.
     */
    public List<String> getRecentItemsTraded(int num, String user) {
        List<String> tradedItems = getItemsTraded(user);
        List<String> recentItems = new ArrayList<>();
        int i = tradedItems.size() - 1;
        while (i >= tradedItems.size() - num && i >= 0) {
            recentItems.add(tradedItems.get(i));
            i--;
        }
        return recentItems;
    }
    // what if its temporary, and the returning meeting exceeds the limit?

    public void addMeetingToTrade(String tradeId, String location, Date time, String username) {
        Meeting meeting = meetingManager.createMeeting(location, time, username);
        getTrade(tradeId).incrementMeetingList(meeting);
    }

    public void changeMeetingOfTrade(String tradeId, String location, Date time, String username) {
        Trade trade = getTrade(tradeId);
        for (Meeting meeting : trade.getMeetingList()) {
            if (meetingManager.getMeetingStatus(meeting) == 0) {
                meetingManager.changeMeeting(meeting, location, time, username);
            }
        }
    }
    //can only change if meeting is not completed and not confirmed

    public String getTradeAllInfo(String tradeId) {
        Trade trade = getTrade(tradeId);
        StringBuilder meetingDetails = new StringBuilder();
        for (Meeting meeting: trade.getMeetingList()){
            meetingDetails.append("Meeting: \n").append(meetingManager.getMeetingsInfo(meeting)).append("\n");
        }
        return trade.toString() + "\n" + meetingDetails;
    }

    public boolean canChangeMeeting(String tradeId, String username) {
        Trade trade = getTrade(tradeId);
        if (trade.getMeetingList().size() == 0) {
            return false;
        } else {
            Meeting lastMeeting = trade.getMeetingList().get(trade.getMeetingList().size() - 1);
            return meetingManager.canEditMeeting(lastMeeting, username);
        }
    }


    public Map<String, String> getProposedTrades(String username) {
        List<Trade> userOngoingTrade = getUserTrades(username, "ongoing");
        Map<String, String> proposedTrades = new HashMap<>();
        for (Trade trade : userOngoingTrade) {
            Meeting meeting = trade.getMeetingList().get(trade.getMeetingList().size() - 1);
            assert meeting != null;
            if (!(meetingManager.getExchangeConfirmed(meeting))) {
                proposedTrades.put(trade.getIdOfTrade(), trade.getTradeType());
            }
        }
        return proposedTrades;
    }

    private List<Trade> getProposedTrades() {
        List<Trade> OngoingTrade = new ArrayList<>(this.ongoingTrades.values());
        List<Trade> proposedTrade = new ArrayList<>();
        for (Trade trade : OngoingTrade) {
            Meeting meeting = trade.getMeetingList().get(trade.getMeetingList().size() - 1);
            assert meeting != null;
            if (!(meetingManager.getExchangeConfirmed(meeting))) {
                proposedTrade.add(trade);
            }
        }
        return proposedTrade;
    }

    public void deleteCommonItemTrades(String itemId, String tradeId) {
        List<Trade> proposedTrade = getProposedTrades();
        proposedTrade.remove(getTrade(tradeId));
        for (Trade trade : proposedTrade) {
            if (trade.containItem(itemId)) {
                this.ongoingTrades.remove(trade.getIdOfTrade());
                for (String user: trade.getUsers()){
                    this.userToTrades.get(user).remove(tradeId);
                }
            }
        }
    }


    public boolean needToAddMeeting(String tradeId){
        Trade trade = getTrade(tradeId);
        if (trade.getTradeType().equals("permanent")){
            return trade.getMeetingList().size() < 1;
        } else {
            return trade.getMeetingList().size() < 2;
        }
    }

    public List<String> getToBeConfirmedTrades(String username) {
        List<Trade> userOngoingTrade = getUserTrades(username, "ongoing");
        List<String> tobeConfirmed = new ArrayList<>();
        for (Trade trade : userOngoingTrade) {
            Meeting meeting = trade.getMeetingList().get(trade.getMeetingList().size() - 1);
            if (trade.getTradeType().equals("permanent")) {
                if (meetingManager.getExchangeConfirmed(meeting)) {
                    Calendar cal = Calendar.getInstance();
                    Date newDate = cal.getTime();
                    if (meetingManager.getConfirmedMeetingTime(meeting).before(newDate)) {
                        tobeConfirmed.add(trade.getIdOfTrade());
                    }
                }
            } else { // trade is temporary
                if (meetingManager.getExchangeConfirmed(meeting)) {
                    Calendar cal = Calendar.getInstance();
                    Date newDate = cal.getTime();
                    if (meetingManager.getConfirmedMeetingTime(meeting).before(newDate)) {
                        tobeConfirmed.add(trade.getIdOfTrade());
                    }
                } else if (trade.getMeetingList().size() == 2 && !(meeting.getIsCompleted())) {
                    tobeConfirmed.add(trade.getIdOfTrade());
                }
            }
        }
        return tobeConfirmed;
    }

    public boolean tradeCompleted(String tradeId){
        return this.completedTrades.containsKey(tradeId);
    }

    public List<String> getTradesOfUser(String username, String type) {
        List<String> userTrades = this.userToTrades.get(username);
        List<String > returnList = new ArrayList<>();
        if (userTrades == null) {
            return returnList;
        }
        for (String id : userTrades) {
            if (type.equals("ongoing")) {
                if (this.ongoingTrades.containsKey(id)) {
                    returnList.add(id);
                }
            } else {
                if (this.completedTrades.containsKey(id)) {
                    returnList.add(id);
                }
            }
        } return returnList;
    }

    //  or confirmed: waiting to confirm real life // hello hello hello
}