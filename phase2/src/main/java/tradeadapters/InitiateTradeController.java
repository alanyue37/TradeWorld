package tradeadapters;

import tradegateway.TradeModel;
import undocomponent.UndoAddProposedTrade;
import undocomponent.UndoableOperation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InitiateTradeController {
    private final TradeModel tradeModel;
    private final String username;

    /**
     * Initiates the InitiateTradeController
     *
     * @param tradeModel The tradeModel
     */
    public InitiateTradeController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
        this.username = tradeModel.getCurrentUser();
    }

    /**
     * Returns whether a user can trade or not.
     * @return true iif a user can initiate a trade (i.e not on vacation or frozen). Otherwise, returns false.
     */
    protected boolean canTrade() {
        boolean success = true;
        if (tradeModel.getUserManager().isFrozen(username)) {
            success = false;
        }
        if (tradeModel.getUserManager().getOnVacation().contains(username)) {
            success = false;
        }
        return success;
    }

    /**
     * Returns whether a user is a new account.
     * @param username username of TradingUser
     * @return true iif the TradingUser account is a new account. Otherwise, returns false.
     */
    protected boolean isNewAccount(String username){
        int numOngoing = tradeModel.getTradeManager().getTradesOfUser(username, "ongoing").size();
        int numCompleted = tradeModel.getTradeManager().getTradesOfUser(username, "completed").size();
        return numOngoing == 0 && numCompleted == 0;
    }

    /**
     * Returns whether a user can initiate a one way trade.
     * @param username username of TradingUser
     * @return true iif user can initiate a one way trade. Otherwise, returns false.
     */
    protected boolean canOneWay(String username){
        int credit = tradeModel.getUserManager().getCreditByUsername(username);
        return credit >= 0;
    }

    /**
     * Returns a list of item ids to offer to the other TradingUser in a two way trade
     * @param otherUsername the other TradingUser's username
     * @return list of item Ids to offer. If there are overlapping between this TradingUser's inventory and the other
     * user's wishlist, returns the list of overlapping items. Otherwise, returns this user's inventory.
     */
    protected List<String> getItemsToOffer(String otherUsername) {
        // two way requires user to propose item from other user's wishlist
        Set<String> otherWishlist = tradeModel.getUserManager().getWishlistByUsername(otherUsername);
        List<String> userItemsAvailable = getUserAvailableItems(username);
        List<String> overlappingItems = new ArrayList<>();
        for (String s : otherWishlist) {
            if (userItemsAvailable.contains(s)) {
                overlappingItems.add(s);
            }
        }
        if (overlappingItems.size() > 0) {
            return overlappingItems; }
        else{ return userItemsAvailable;}
    }

    /**
     * Returns a map of items info to the id of the item that are available for this TradingUser to trade (i.e. filters
     * vacation, private/public, rank)
     * @return map of items' info to id of the item
     */
    protected Map<String, String> getAvailableItems(){
        Map<String, String> infoToId = new HashMap<>();
        String userRank = tradeModel.getUserManager().getRankByUsername(username);
        Set<String> itemsAvailable = new HashSet<>();
        if (tradeModel.getUserManager().getPrivateUser().contains(username)) { // if private user
            itemsAvailable.addAll(getAvailableItemsPrivateAccount(tradeModel.getItemManager().getAvailableItems(userRank)));
        } else {  //public user
            itemsAvailable.addAll(getAvailableItemsPublicAccount(tradeModel.getItemManager().getAvailableItems(userRank)));
        }
        for (String item: itemsAvailable){
            infoToId.put(tradeModel.getItemManager().getItemInfo(item), item);
        }
        return infoToId;
    }

    /**
     * Creates a trade.
     * @param otherInfo map of information for the trade (type of trade, items involved, date, location)
     * @throws ParseException exception for parsing date
     */
    protected void createTrade(Map<String, String> otherInfo) throws ParseException {
        String way = otherInfo.get("way");
        String type = otherInfo.get("term");
        List<String> details = new ArrayList<>();
        String itemId = otherInfo.get("chosen");
        if (way.equals("oneWay")){
            details.add(tradeModel.getItemManager().getOwner(itemId));
            details.add(username);
            details.add(itemId);
        } else{
            details.add(username);
            details.add(tradeModel.getItemManager().getOwner(itemId));
            details.add(otherInfo.get("giving"));
            details.add(itemId);
        }
        String dateString = otherInfo.get("date");
        Date time = parseDateString(dateString);
        String location = otherInfo.get("location");
        String tradeId = tradeModel.getTradeManager().addTrade(way, type, details);
        String meetingId = tradeModel.getMeetingManager().createMeeting(location, time, username, tradeId);
        tradeModel.getTradeManager().addMeetingToTrade(tradeId, meetingId);
        UndoableOperation undoableOperation = new UndoAddProposedTrade(this.tradeModel.getTradeManager(), tradeModel.getMeetingManager(), tradeId);
        this.tradeModel.getUndoManager().add(undoableOperation);

    }

    /**
     * Parse the string date to date format.
     * @param dateString date in a string format
     * @return Date
     * @throws ParseException exception for parsing date
     */
    private Date parseDateString(String dateString) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return format.parse(dateString);
    }

    /**
     * Get the available items of a user's inventory
     * @param username username of user
     * @return available items of a user's inventory (not filtered by other user's wishlist)
     */
    private List<String> getUserAvailableItems(String username) {
        String userRank = tradeModel.getUserManager().getRankByUsername(username);
        Set<String> itemsAvailable = tradeModel.getItemManager().getAvailableItems(userRank);
        List<String> userItemsAvailable = new ArrayList<>();
        for (String itemId: itemsAvailable) {
            if (tradeModel.getItemManager().getOwner(itemId).equals(username)) {
                userItemsAvailable.add(itemId);
            }
        }
        return userItemsAvailable;
    }

    /**
     * Returns a set of items that are available for this TradingUser to trade/borrow if they are private status.
     * @param allItems all items in the system available for trading
     * @return set of items available for this TradingUser to trade (filtered by city, vacation, privacy)
     */
    private Set<String> getAvailableItemsPrivateAccount(Set<String> allItems){
        Set<String> couldTrade = tradeModel.getUserManager().getFriendList(username);
        couldTrade.removeIf(user -> tradeModel.getUserManager().getOnVacation().contains(user)); // remove users on vacation
        couldTrade.removeIf(friend -> !tradeModel.getUserManager().getCityByUsername(username).equalsIgnoreCase(tradeModel.getUserManager().getCityByUsername(friend))); // remove different city
        allItems.removeIf(item -> !couldTrade.contains(tradeModel.getItemManager().getOwner(item)));
        return allItems;
    }

    /**
     * Returns a set of items that are available for this TradingUser to trade/borrow if they are public status.
     * @param allItems all items in the system available for trading
     * @return set of items available for this TradingUser to trade (filtered by city, vacation, privacy)
     */
    private Set<String> getAvailableItemsPublicAccount(Set<String> allItems){
        Set<String> couldNotTrade = tradeModel.getUserManager().getPrivateUser();
        couldNotTrade.removeAll(tradeModel.getUserManager().getFriendList(username));
        couldNotTrade.addAll(tradeModel.getUserManager().getOnVacation()); //vacation
        couldNotTrade.add(username);
        String thisUserCity = tradeModel.getUserManager().getCityByUsername(username);
        allItems.removeIf(item -> couldNotTrade.contains(tradeModel.getItemManager().getOwner(item)) |
                !thisUserCity.equalsIgnoreCase(tradeModel.getUserManager().getCityByUsername(tradeModel.getItemManager().getOwner(item))));
        return allItems;
    }

}
