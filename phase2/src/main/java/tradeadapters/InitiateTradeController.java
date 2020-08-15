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

    protected boolean isNewAccount(String username){
        int numOngoing = tradeModel.getTradeManager().getTradesOfUser(username, "ongoing").size();
        int numCompleted = tradeModel.getTradeManager().getTradesOfUser(username, "completed").size();
        return numOngoing == 0 && numCompleted == 0;
    }

    protected boolean cannotOneWay(String username){
        int credit = tradeModel.getUserManager().getCreditByUsername(username);
        return credit < 0;
    }

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
            else{return userItemsAvailable;}
    }

    protected Map<String, String> getAvailableItems(){
        Map<String, String> infoToId = new HashMap<>();
        String userRank = tradeModel.getUserManager().getRankByUsername(username);
        Set<String> itemsAvailable = new HashSet<>();
        if (tradeModel.getUserManager().getPrivateUser().contains(username)) { // if private user
            itemsAvailable.addAll(getAvailableItemsPrivateAccount(tradeModel.getItemManager().getAvailableItems(userRank)));
        } else { // public user
            itemsAvailable.addAll(getAvailableItemsPublicAccount(tradeModel.getItemManager().getAvailableItems(userRank)));
        }
        for (String item: itemsAvailable){
            infoToId.put(tradeModel.getItemManager().getItemInfo(item), item);
        }
        return infoToId;
    }

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

    private Date parseDateString(String dateString) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return format.parse(dateString);
    }

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

    private Set<String> getAvailableItemsPrivateAccount(Set<String> allItems){
        Set<String> couldTrade = tradeModel.getUserManager().getFriendList(username);
        couldTrade.removeIf(user -> tradeModel.getUserManager().getOnVacation().contains(user)); // remove users on vacation
        couldTrade.removeIf(friend -> !tradeModel.getUserManager().getCityByUsername(username).equalsIgnoreCase(tradeModel.getUserManager().getCityByUsername(friend))); // remove different city
        allItems.removeIf(item -> !couldTrade.contains(tradeModel.getItemManager().getOwner(item)));
        return allItems;
    }

    private Set<String> getAvailableItemsPublicAccount(Set<String> allItems){
        Set<String> couldNotTrade = tradeModel.getUserManager().getPrivateUser();
        couldNotTrade.removeAll(tradeModel.getUserManager().getFriendList(username));
        couldNotTrade.addAll(tradeModel.getUserManager().getOnVacation()); // vacation
        couldNotTrade.add(username);
        String thisUserCity = tradeModel.getUserManager().getCityByUsername(username);
        allItems.removeIf(item -> couldNotTrade.contains(tradeModel.getItemManager().getOwner(item)) |
                !thisUserCity.equalsIgnoreCase(tradeModel.getUserManager().getCityByUsername(tradeModel.getItemManager().getOwner(item))));
        return allItems;
    }
}