package tradecomponent;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the creation and handling of trades.
 */
public class TradeManager implements Serializable {
    private int limitIncomplete;
    private int limitTransactionPerWeek;
    private final Map<String, Trade> ongoingTrades;
    private final Map<String, Trade> completedTrades;
    private final Map<String, List<String>> userToTrades;
    private final AtomicInteger counter = new AtomicInteger();


    /**
     * Constructor for TradeManager.
     */
    public TradeManager() {
        this.limitIncomplete = 3; //or null?
        this.limitTransactionPerWeek = 3; //or null?
        this.ongoingTrades = new HashMap<>();
        this.completedTrades = new HashMap<>();
        this.userToTrades = new HashMap<>();
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
     * @return         the ID of the Trade
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
     * @return      the ID of the Trade
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
    public List<String> getExceedIncompleteLimitUser(List<String> tradeIds) {
        Map<String, Integer> usernamesMap = userToNumTradesInvolved(tradeIds);
        List<String> incompleteUsernames = new ArrayList<>();
        for (String user : usernamesMap.keySet()) {
            if (usernamesMap.get(user) > this.limitIncomplete) {
                incompleteUsernames.add(user);
            }
        }
        return incompleteUsernames;
    }

    /**
     * Gets the list of usernames of users who surpassed a given limit of trades in the past given number of days.
     *
     * @return list of usernames
     */
    public List<String> getExceedPerWeek(List<String> tradeIds) {
        Map<String, Integer> usernamesMap = userToNumTradesInvolved(tradeIds);
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

    /**
     * Returns list of the IDs of the most recent given number of (completed) trades of a user
     *
     * @param num  the number of IDs of most recent trades wanted
     * @return list of IDs of most recent (completed) trades involving user "user." If "num" is greater
     * than the number of trades that "user" has been in, all of the trade IDs are returned.
     */
    public List<String> getRecentTrades(int num, List<String> sortedCompletedTrades) {
        List<String> recentTrades = new ArrayList<>();
        int i = sortedCompletedTrades.size() - 1;
        while (i >= sortedCompletedTrades.size() - num && i >= 0) {
            recentTrades.add(sortedCompletedTrades.get(i));
            i--;
        }
        return recentTrades;
    }

    /**
     * Returns a string with all the information about the trade (type, status, number of meetings, creation date, items
     * and users involved) and the meeting details of all meetings of the trade
     *
     * @param tradeId ID of the trade
     * @return string with trade information
     */
    public String getTradeInfo(String tradeId) {
        Trade trade = getTrade(tradeId);
        return trade.toString();
    }

    /**
     * Given a user, returns a map with trade IDs of trades with meetings yet to be agreed upon that the user is a part of
     * mapped to the type of the trade
     *
     * @return map of proposed trade IDs to the type
     */
    public Map<String, String> getType(List<String> tradeIds) {
        Map<String, String> tradeIdToType = new HashMap<>();
        for (String tradeId : tradeIds) {
            Trade trade = getTrade(tradeId);
            tradeIdToType.put(tradeId, trade.getTradeType());
        }
        return tradeIdToType;
    }

    /**
     * Deletes all proposed trades involving the item with "itemId" except the trade with "tradeID"
     *
     * @param itemId ID of the item
     * @param tradeId ID of the trade
     */
    public void deleteCommonItemTrades(String itemId, String tradeId) {
        for (Trade trade: this.ongoingTrades.values()){
            if ((trade.containItem(itemId)) && (!trade.getIdOfTrade().equals(tradeId))){
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

    public void closeTrade(String tradeId){
        Trade trade = getTrade(tradeId);
        trade.changeIsOpened();
        this.completedTrades.put(tradeId, trade);
        this.ongoingTrades.remove(tradeId, trade);
    }

    public List<String> getMeetingOfTrade(String tradeId){
        Trade trade = getTrade(tradeId);
        return trade.getMeetingList();
    }
}
