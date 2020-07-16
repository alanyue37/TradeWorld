package tradecomponent;

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
    private final Map<String, Trade> ongoingTrades;
    private final MeetingManager meetingManager;
    private final Map<String, Trade> completedTrades;
    private final Map<String, List<String>> userToTrades;
    private final AtomicInteger counter = new AtomicInteger();


    /**
     * Constructor for TradeManager.
     */
    public TradeManager() {
        this.limitOfEdits = 3;
        this.limitIncomplete = 3; //or null?
        this.limitTransactionPerWeek = 3; //or null?
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
     * Confirms that the meeting happened in real life
     *
     * @param tradeId  ID of the trade of which we want to confirm the meeting
     * @param username username of the user who confirms the meeting happened
     */
    public void confirmMeetingHappened(String tradeId, String username) {
        Trade trade = this.ongoingTrades.get(tradeId);
        Meeting meeting = getLastConfirmedMeeting(tradeId);
        assert meeting != null; // not really necessary? since the user can only confirm meeting happened after they set up a time for the meeting?
        meetingManager.meetingHappened(meeting, username);
        if (meetingManager.getMeetingStatus(meeting) == 2) {
            if (canClose(trade)) {
                this.ongoingTrades.get(tradeId).changeIsOpened();
                this.completedTrades.put(tradeId, trade);
                this.ongoingTrades.remove(tradeId);
            }
        }
    }

    /**
     * Returns the meeting in a trade that was last confirmed. If the trade does not have any
     * confirmed meeting (time, location), then it returns null.
     *
     * @param tradeId ID of the trade
     * @return meeting of trade that the meeting time and place was last confirmed. If no meeting's time/place has been
     * confirmed for the trade, returns null.
     */
    private Meeting getLastConfirmedMeeting(String tradeId) {
        Trade trade = getTrade(tradeId);
        int i = trade.getMeetingList().size() - 1;
        if (i == -1) { // shouldn't happen anyway, as a trade is always created with a meeting
            return null;
        }
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
     * Given the ID of a trade, returns the trade
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
     * Given a trade, returns whether the trade can be closed or not
     *
     * @param trade trade to see if it can be closed
     * @return true iff all meetings of the trade are completed (both users have confirmed the meeting happened in real
     * life). If the trade is permanent, one meeting should be completed. If the trade is temporary, two meetings are
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
     * Given a trade, returns a map of the ID of an item to list of usernames, where the first is the giver and the
     * second is the receiver
     *
     * @param tradeId ID of the trade
     * @return map of item to list of users, where the first user is the giver and second user is the receiver of the
     * item
     */
    public Map<String, List<String>> itemToUsers(String tradeId) {
        return getTrade(tradeId).itemToTrader();
    }


    /**
     * Adds a one way trade to the map of trades in TradeManager.
     *
     * @param type     permanent or temporary
     * @param giver    username of the person giving the object
     * @param receiver username of the person receiving the object
     * @param itemId   ID of the item
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
     * Adds a two way trade to the map of trades in TradeManager.
     *
     * @param type  permanent or temporary
     * @param user1 username of user1 in the trade
     * @param user2 username of user2 in the trade
     * @param item1 ID of the item of user1
     * @param item2 ID of the item of user2
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
     * @return the limit of edits to the meeting time/place
     */
    public int getLimitEdits() {
        return this.limitOfEdits;
    }

    /**
     * Change the limit of edit that users can suggest before the trade is cancelled
     *
     * @param newLimit  new limit for the edits allowed
     */
    public void changeLimitEdits(int newLimit) {
        this.limitOfEdits = newLimit;
    }

    /**
     * Gets the current limit for having incomplete transactions before the account is frozen
     * @return  the current limit of incomplete transactions
     */
    public int getLimitIncomplete() {
        return this.limitIncomplete;
    }

    /**
     * Gets the current limit of transactions per week before the account is frozen
     * @return  the current limit of transactions per week
     */
    public int getLimitTransactionPerWeek() {
        return this.limitTransactionPerWeek;
    }

    /**
     * Change the limit for incomplete transactions
     * @param newIncompleteLimit    new value of incomplete transactions
     */
    public void changeLimitIncomplete(int newIncompleteLimit) {
        this.limitIncomplete = newIncompleteLimit;
    }

    /**
     * Change the limit for transactions in one week
     * @param newLimitPerWeek   new value of transactions in one week
     */
    public void changeLimitTransactionPerWeek(int newLimitPerWeek) {
        this.limitTransactionPerWeek = newLimitPerWeek;
    }


    /**
     * Returns list of incomplete trades (trades that contains incomplete meetings)
     *
     * @return list of incomplete trades
     */
    public List<String> getIncompleteTrade() {
        List<Trade> openTrades = new ArrayList<>(this.ongoingTrades.values());
        List<String> incompleteTrades = new ArrayList<>();
        for (Trade openTrade : openTrades) {
            Meeting meeting = openTrade.getMeetingList().get(openTrade.getMeetingList().size() - 1);
            if (meetingManager.isIncompleteMeeting(meeting)){
                incompleteTrades.add(openTrade.getIdOfTrade());
            }
        }
        return incompleteTrades;
    }

    /**
     * Given a list of trades, returns a map with the usernames of users in the list who took part of at least one trade
     * mapped to the number of trades they took part in
     *
     * @param trades list of trades
     * @return a map of username to the number of trades that the user took part of
     */
    private Map<String, Integer> userToNumTradesInvolved(List<String> trades) {
        Map<String, Integer> usernameCount = new HashMap<>();
        for (String tradeId : trades) {
            Trade trade = getTrade(tradeId);
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
     * Returns list of usernames that have surpassed the limit of incomplete trades allowed
     * before the account is frozen
     *
     * @return list of usernames that passed limit of incomplete trades
     */
    public List<String> getExceedIncompleteLimitUser() {
        List<String> incompleteTrades = getIncompleteTrade();
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
     * Given a trade, returns the date of the last confirmed meeting if there is any confirmed meeting. If none of
     * the meetings of the trade are confirmed yet, then returns null.
     *
     * @param tradeId ID of trade
     * @return the date of the last confirmed meeting. If none of the meeting(s) inside trade have been confirmed
     * (time, location) yet, return null.
     */
    public Date getLastConfirmedMeetingTime(String tradeId) {
        if (getLastConfirmedMeeting(tradeId) == null) {
            return null;
        } else {
            return meetingManager.getConfirmedMeetingTime(Objects.requireNonNull(getLastConfirmedMeeting(tradeId)));
        }
    }

    /**
     * Given a trade, returns the location of the last confirmed meeting. If there are no confirmed meetings,
     * returns null.
     *
     * @param tradeId ID of the trade
     * @return location of the last confirmed meeting. If none of the meeting(s) inside trade have been confirmed,
     * return null
     */
    public String getLastConfirmedMeetingLocation(String tradeId) {
        if (getLastConfirmedMeeting(tradeId) == null) {
            return null;
        } else {
            return meetingManager.getLastLocation(Objects.requireNonNull(getLastConfirmedMeeting(tradeId)));
        }
    }

    /**
     * Returns a list of trades that have had trades completed in the past given number of days
     *
     * @param numDays number of days
     * @return list of trades that are completed in the past numDays
     */
    private List<String> getTradesPastDays(int numDays) {
        Calendar compare = Calendar.getInstance();
        compare.add(Calendar.DATE, -7);
        Date comparisonDate = compare.getTime();
        List<String> tradeInPastDays = new ArrayList<>();
        for (Trade trade : this.completedTrades.values()) {
            if (trade.getMeetingList().get(trade.getMeetingList().size() - 1).getTime().after(comparisonDate)) {
                tradeInPastDays.add(trade.getIdOfTrade());
            }
        }
        return tradeInPastDays;
    }

    /**
     * Gets the list of usernames of users who surpassed a given limit of trades in the past given number of days.
     *
     * @return list of usernames
     */
    public List<String> getExceedPerWeek() {
        List<String> pastTrades = getTradesPastDays(7);
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
     * Cancels the trade. Removes the trade from the map of trades
     *
     * @param tradeId ID of the trade to be removed from map of trades
     */
    public void cancelTrade(String tradeId) {
        Trade trade = getTrade(tradeId);
        for (String user : trade.getUsers()) {
            this.userToTrades.get(user).remove(tradeId);
        }
        this.ongoingTrades.remove(tradeId);
    }

    /**
     * Given a trade, returns whether this trade should be cancelled. For a permanent trade, a trade is cancelled if
     * they are still in disagreement with the meeting details and have attained the limit of edits allowed. For a
     * temporary trade, a trade is cancelled only if they cannot agree on the first meeting and have reached the limit
     * of edits.
     *
     * @param tradeId ID of trade
     * @return true if the trade need to be cancelled, false otherwise.
     */
    public boolean needCancelTrade(String tradeId) {
        Meeting meeting = this.ongoingTrades.get(tradeId).getMeetingList().get(0);
        if ((meetingManager.getMeetingStatus(meeting) != 2) && (meetingManager.getMeetingStatus(meeting) != 1)) {
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
     * Returns list of the top given number of most frequent trading partners of a user
     *
     * @param num  the number of most frequent trading partners wanted
     * @param user the username of the user to get their most frequent trading partners
     * @return list of usernames that are the most frequent trading partners of user "user." If "num" is greater
     * than the number of trading partners that "user" has, all of their trading partners are returned.
     */
    public List<String> getFrequentPartners(int num, String user) {
        List<String> completed = getTradesOfUser(user, "completed");
        Map<String, Integer> partners = userToNumTradesInvolved(completed);
        partners.remove(user);
        List<String> frequentPartners = sortPartnersList(partners); // assumes that sort partners give best partner first
        if (num >= frequentPartners.size()) {
            return frequentPartners;
        }
        return frequentPartners.subList(0, num);
    }
    // only looks at completed trades


    private List<String> sortTradesMeetingDate(List<String> trades)  {
        List<String> sorted = new ArrayList<>();
        if (trades.size() == 0) { return sorted; }
        sorted.add(trades.get(0));
        for (int i = 1; i < trades.size(); i++) {
            int ii = 0;
            while (ii < sorted.size() && getLastConfirmedMeetingTime(sorted.get(ii)).before(getLastConfirmedMeetingTime(trades.get(i)))) {
                ii++;
            }
            sorted.add(ii, trades.get(i));
        }
        return sorted;
    } // goes from oldest (index 0) to newest

    /**
     * Returns list of the IDs of the most recent given number of (completed) trades of a user
     *
     * @param num  the number of IDs of most recent trades wanted
     * @param user the username of the user to get IDs of most recent trades
     * @return list of IDs of most recent (completed) trades involving user "user." If "num" is greater
     * than the number of trades that "user" has been in, all of the trade IDs are returned.
     */
    public List<String> getRecentTrades(int num, String user) {
        List<String> sortedTrades = sortTradesMeetingDate(getTradesOfUser(user, "completed"));
        List<String> recentTrades = new ArrayList<>();
        int i = sortedTrades.size() - 1;
        while (i >= sortedTrades.size() - num && i >= 0) {
            recentTrades.add(sortedTrades.get(i));
            i--;
        }
        return recentTrades;
    }

    /**
     * Adds or edits a meeting in a trade
     *
     * @param tradeId ID of trade
     * @param location location of the meeting
     * @param time date and time of the meeting
     * @param username username of the user who suggests or edits the meeting
     * @param type whether to add or edit a meeting
     */
    public void editMeetingOfTrade(String tradeId, String location, Date time, String username, String type) {
        if (type.equals("add")) {
            Meeting meeting = meetingManager.createMeeting(location, time, username);
            getTrade(tradeId).incrementMeetingList(meeting);
        } else { // edit meeting time
            Trade trade = getTrade(tradeId);
            for (Meeting meeting : trade.getMeetingList()) {
                if (meetingManager.getMeetingStatus(meeting) == 0) {
                    meetingManager.changeMeeting(meeting, location, time, username);
                }
            }
        } // can only change if meeting is not completed and not confirmed
    }

    /**
     * Returns a string with all the information about the trade (type, status, number of meetings, creation date, items
     * and users involved) and the meeting details of all meetings of the trade
     *
     * @param tradeId ID of the trade
     * @return string with trade information
     */
    public String getTradeAllInfo(String tradeId) {
        Trade trade = getTrade(tradeId);
        StringBuilder meetingDetails = new StringBuilder();
        for (Meeting meeting: trade.getMeetingList()){
            meetingDetails.append("Meeting: \n").append(meetingManager.getMeetingsInfo(meeting)).append("\n");
        }
        return trade.toString() + "\n" + meetingDetails;
    }

    /**
     * Given a trade, checks whether a user can change the meeting details of the trade
     *
     * @param tradeId ID of the trade
     * @param username username of user
     * @return true iff user with username "username" can edit the meeting location/time of the trade
     */
    public boolean canChangeMeeting(String tradeId, String username) {
        Trade trade = getTrade(tradeId);
        if (trade.getMeetingList().size() == 0) {
            return false;
        } else {
            Meeting lastMeeting = trade.getMeetingList().get(trade.getMeetingList().size() - 1);
            return meetingManager.canEditMeeting(lastMeeting, username);
        }
    }

    /**
     * Given a user, returns a map with trade IDs of trades with meetings yet to be agreed upon that the user is a part of
     * mapped to the type of the trade
     *
     * @param username username of user
     * @return map of proposed trade IDs to the type
     */
    public Map<String, String> getProposedTrades(String username) {
        List<String> userOngoingTrade = getTradesOfUser(username, "ongoing");
        Map<String, String> proposedTrades = new HashMap<>();
        for (String tradeId : userOngoingTrade) {
            Trade trade = getTrade(tradeId);
            Meeting meeting = trade.getMeetingList().get(trade.getMeetingList().size() - 1);
            assert meeting != null;
            if (meetingManager.getMeetingStatus(meeting) != 1) {
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
            if (meetingManager.getMeetingStatus(meeting) != 1) {
                proposedTrade.add(trade);
            }
        }
        return proposedTrade;
    }

    /**
     * Deletes all proposed trades involving the item with "itemId" except the trade with "tradeID"
     *
     * @param itemId ID of the item
     * @param tradeId ID of the trade
     */
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

    /**
     * Returns whether the trade requires a new meeting to be added
     *
     * @param tradeId ID of the trade
     * @return true iff the trade with "tradeId" requires a new meeting to be added
     */
    public boolean needToAddMeeting(String tradeId){
        Trade trade = getTrade(tradeId);
        if (trade.getTradeType().equals("permanent")){
            return trade.getMeetingList().size() < 1;
        } else {
            return trade.getMeetingList().size() < 2;
        }
    }

    /**
     * Given a user, returns a map with trade IDs of trades with real-life meetings yet to be confirmed
     * that the user is a part of
     *
     * @param username username of user
     * @return map of to-be confirmed trade IDs
     */
    public Map<String, String> getToBeConfirmedTrades(String username) {
        List<String> userOngoingTrade = getTradesOfUser(username, "ongoing");
        Map<String, String> toBeConfirmed = new HashMap<>();
        for (String tradeId : userOngoingTrade) {
            Trade trade = getTrade(tradeId);
            Meeting meeting = trade.getMeetingList().get(trade.getMeetingList().size() - 1);
            if (meetingManager.getMeetingStatus(meeting) == 1) {
                Calendar cal = Calendar.getInstance();
                Date newDate = cal.getTime();
                if (meetingManager.getConfirmedMeetingTime(meeting).before(newDate)) {
                    toBeConfirmed.put(trade.getIdOfTrade(), trade.getTradeType());
                }
            }
        }
        return toBeConfirmed;
    }

    /**
     * Given a username and a type of trade (ongoing or completed), returns all trades of this user of the type
     *
     * @param username username of the user we want to get all trade
     * @param type the type of the trade, ongoing or completed
     * @return list of "type" trades of user with username "username"
     */
    public List<String> getTradesOfUser(String username, String type) {
        List<String> userTrades = this.userToTrades.get(username);
        List<String> returnList = new ArrayList<>();
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
}
