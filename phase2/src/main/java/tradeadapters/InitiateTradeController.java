package tradeadapters;

import tradegateway.TradeModel;
import trademisc.RunnableController;
import undocomponent.UndoAddProposedTrade;
import undocomponent.UndoableOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InitiateTradeController implements RunnableController {
    private final TradeModel tradeModel;
    private final BufferedReader br;
    private final String username;

    /**
     * Initiates the InitiateTradeController
     *
     * @param tradeModel The tradeModel
     */
    public InitiateTradeController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.username = tradeModel.getCurrentUser();
    }

    /**
     * The main run method to call when this controller is initiated.
     */
    @Override
    public void run() {
//        try {
//            initiateTrade();
//        } catch (IOException e) {
//            System.out.println("Something went wrong!");
//        }
    }

//    private boolean initiateTrade() throws IOException {
//        boolean success = false;
//        if (tradeModel.getUserManager().isFrozen(username)) {
//            unfreezeRequest();
//            return success;
//        }
//        if (tradeModel.getUserManager().getOnVacation().contains(username)){
//            presenter.onVacation();
//            return success;
//        }
//        String itemId = getItemIdChoice();
//        if (itemId == null) {
//            return success;
//        }
//        success = createTrade(itemId);
//        presenter.successful(success);
//        return success;
//    }

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

//    private boolean createTrade(String itemId) throws IOException {
//        String otherUsername = tradeModel.getItemManager().getOwner(itemId);
//        String tradeId;
//        String thisUserItemId;
//
//        // Ask for trade type
//        presenter.tradeTypesMenu();
//        String permanentOrTemporary = null;
//        boolean twoWay = false;
//        boolean validTradeType = false;
//        do {
//        String tradeType = br.readLine();
//            switch(tradeType) {
//                case "1":
//                    permanentOrTemporary = "temporary";
//                    twoWay = false;
//                    validTradeType = true;
//                    break;
//                case "2":
//                    permanentOrTemporary = "permanent";
//                    twoWay = false;
//                    validTradeType = true;
//                    break;
//                case "3":
//                    permanentOrTemporary = "temporary";
//                    twoWay = true;
//                    validTradeType = true;
//                    break;
//                case "4":
//                    permanentOrTemporary = "permanent";
//                    twoWay = true;
//                    validTradeType = true;
//                    break;
//                case "back":
//                    return false;
//                default:
//                    presenter.tryAgain();
//            }
//        } while (!validTradeType);
//
//        if (twoWay) {
//            // Need to offer an item in return if two way
//            thisUserItemId = getItemToOffer(otherUsername);
//            if (thisUserItemId == null) {
//                return false;
//            }
////            List<String> details = new ArrayList<>(Arrays.asList(username, otherUsername, thisUserItemId, itemId));
////            tradeId = tradeModel.getTradeManager().addTrade("twoWay", permanentOrTemporary, details);
//        }
//        else {
//            int credit = tradeModel.getUserManager().getCreditByUsername(username);
//            if (credit < 0) {
//                presenter.notEnoughCredits(Math.abs(credit));
//                return false;
//            }
//            int numOngoing = tradeModel.getTradeManager().getTradesOfUser(username, "ongoing").size();
//            int numCompleted = tradeModel.getTradeManager().getTradesOfUser(username, "completed").size();
//            if (numOngoing == 0 && numCompleted == 0) {
//                presenter.newAccount();
//                return false;
//            }
//            List<String> details = new ArrayList<>(Arrays.asList(otherUsername, username, itemId));
//            tradeId = tradeModel.getTradeManager().addTrade("oneWay", permanentOrTemporary, details);
//        }
//
//        List<String> meetingDetails = getMeetingDetails();
//        try {
//            String meetingId = tradeModel.getMeetingManager().createMeeting(meetingDetails.get(0), parseDateString(meetingDetails.get(1)), username, tradeId);
//            tradeModel.getTradeManager().addMeetingToTrade(tradeId, meetingId);
//        } catch (ParseException e) {
//            // This shouldn't happen because date parsing was already checked
//            System.out.println("Invalid date and time!");
//            return false;
//        }
//
//        UndoableOperation undoableOperation = new UndoAddProposedTrade(this.tradeModel.getTradeManager(), tradeModel.getMeetingManager(), tradeId);
//        this.tradeModel.getUndoManager().add(undoableOperation);
//        return true;
//    }

    protected boolean isNewAccount(String username){
        int numOngoing = tradeModel.getTradeManager().getTradesOfUser(username, "ongoing").size();
        int numCompleted = tradeModel.getTradeManager().getTradesOfUser(username, "completed").size();
        return numOngoing == 0 && numCompleted == 0;
    }

    protected boolean canOneWay(String username){
        int credit = tradeModel.getUserManager().getCreditByUsername(username);
        return credit >= 0;
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
//        String thisUserItemId = null;
        if (overlappingItems.size() > 0) {
            // if there are overlapping items, show those to choose from
//            presenter.itemsToOffer(getItemsInfo(overlappingItems), true);
//            thisUserItemId = br.readLine();
//            while (!overlappingItems.contains(thisUserItemId)) {
//                presenter.tryAgain();
//                thisUserItemId = br.readLine();
//            }
            return overlappingItems; }
//        else if (userItemsAvailable.size() > 0) {
            // if not let user choose any item from their available list
//            presenter.itemsToOffer(getItemsInfo(userItemsAvailable), false);
//            thisUserItemId = br.readLine();
//            while (!userItemsAvailable.contains(thisUserItemId)) {
//                presenter.tryAgain();
//                thisUserItemId = br.readLine();
            else{return userItemsAvailable;}
//            }
//        } else {
            // they have no items available so let them know this trade is not possible
//            presenter.noItemsToOffer();
//        }
    }

    private List<String> getMeetingDetails() throws IOException {
        List<String> meetingDetails = new ArrayList<>();

        // Get location
//        presenter.askMeetingLocation();
        String location = br.readLine();
        meetingDetails.add(location);

        // Get date
        String dateString = null;
        Date date = null;
        do {
            try {
//                presenter.askMeetingDate();
                dateString = br.readLine();
                date = parseDateString(dateString); // check formatting is valid
            }
            catch (ParseException e) {
//                presenter.tryAgain();
            }
        } while (date == null);
        meetingDetails.add(dateString);

        return meetingDetails;
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

//    private String getItemIdChoice() throws IOException {
//        String userRank = tradeModel.getUserManager().getRankByUsername(username);
//        Set<String> itemsAvailable = new HashSet<>();
//        if (tradeModel.getUserManager().getPrivateUser().contains(username)) { // if private user
//            itemsAvailable.addAll(getAvailableItemsPrivateAccount(tradeModel.getItemManager().getAvailableItems(userRank)));
//        } else {  //public user
//            itemsAvailable.addAll(getAvailableItemsPublicAccount(tradeModel.getItemManager().getAvailableItems(userRank)));
//        }
//        List<String> itemsToShow = new ArrayList<>(itemsAvailable);

//        if (itemsToShow.size() == 0) {
//            presenter.noItemsToTrade();
//            return null;
//        }
//        presenter.availableItemsMenu(itemsToShow);
//        String itemId = br.readLine();
//        while (!itemsToShow.contains(itemId) && !itemId.equals("back")) {
//            // Validate input
//            presenter.tryAgain();
//            itemId = br.readLine();
//        }
//        if (itemId.equals("back")) {
//            itemId = null;
//        }
//        return itemId;
//    }

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

    private Set<String> getAvailableItemsPrivateAccount(Set<String> allItems){
        Set<String> couldTrade = tradeModel.getUserManager().getFriendList(username);
        couldTrade.removeIf(user -> tradeModel.getUserManager().getOnVacation().contains(user)); // remove users on vacation
        couldTrade.removeIf(friend -> !tradeModel.getUserManager().getCityByUsername(username).equals(tradeModel.getUserManager().getCityByUsername(friend))); // remove different city
        allItems.removeIf(item -> !couldTrade.contains(tradeModel.getItemManager().getOwner(item)));
        return allItems;
    }

    private Set<String> getAvailableItemsPublicAccount(Set<String> allItems){
        Set<String> couldNotTrade = tradeModel.getUserManager().getPrivateUser();
        couldNotTrade.removeAll(tradeModel.getUserManager().getFriendList(username));
        couldNotTrade.addAll(tradeModel.getUserManager().getOnVacation()); //vacation
        couldNotTrade.add(username);
        String thisUserCity = tradeModel.getUserManager().getCityByUsername(username);
        allItems.removeIf(item -> couldNotTrade.contains(tradeModel.getItemManager().getOwner(item)) |
                !thisUserCity.equals(tradeModel.getUserManager().getCityByUsername(tradeModel.getItemManager().getOwner(item))));
        return allItems;
    }

    private void unfreezeRequest() throws IOException { //TODO: move to another gui
//        presenter.frozenAccount();
        String input = br.readLine();
        while (!input.equals("0") && !input.equals("1")){
//            presenter.tryAgain();
            input = br.readLine();
        }
        if (input.equals("1")) {
            tradeModel.getUserManager().markUserForUnfreezing(username);
        }
    }

    private List<String> getItemsInfo(List<String> itemIds) {
        List <String> itemsInfo = new ArrayList<>();
        for (String itemId : itemIds) {
                itemsInfo.add(tradeModel.getItemManager().getItemInfo(itemId));
        }
        return itemsInfo;
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

}
