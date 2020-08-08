package tradeadapters;

import tradegateway.TradeModel;
import trademisc.RunnableController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InitiateTradeController implements RunnableController {
    private final TradeModel tradeModel;
    private final InitiateTradePresenter presenter;
    private final BufferedReader br;
    private final String username;

    /**
     * Initiates the InitiateTradeController
     * @param tradeModel    The tradeModel
     * @param username  The name of the User
     */
    public InitiateTradeController(TradeModel tradeModel, String username) {
        this.tradeModel = tradeModel;
        this.presenter = new InitiateTradePresenter();
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.username = username;
    }

    /**
     * The main run method to call when this controller is initiated.
     */
    @Override
    public void run() {
        try {
            initiateTrade();
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        }
    }

    private boolean initiateTrade() throws IOException {
        boolean success = false;
        if (tradeModel.getUserManager().isFrozen(username)) {
            unfreezeRequest();
            return success;
        }
        if (tradeModel.getUserManager().getOnVacation().contains(username)){
            presenter.onVacation();
            return success;
        }
        String itemId = getItemIdChoice();
        if (itemId == null) {
            return success;
        }
        success = createTrade(itemId);
        presenter.successful(success);
        return success;
    }

    private boolean createTrade(String itemId) throws IOException {
        String otherUsername = tradeModel.getItemManager().getOwner(itemId);
        String tradeId;
        String thisUserItemId;

        // Ask for trade type
        presenter.tradeTypesMenu();
        String permanentOrTemporary = null;
        boolean twoWay = false;
        boolean validTradeType = false;
        do {
        String tradeType = br.readLine();
            switch(tradeType) {
                case "1":
                    permanentOrTemporary = "temporary";
                    twoWay = false;
                    validTradeType = true;
                    break;
                case "2":
                    permanentOrTemporary = "permanent";
                    twoWay = false;
                    validTradeType = true;
                    break;
                case "3":
                    permanentOrTemporary = "temporary";
                    twoWay = true;
                    validTradeType = true;
                    break;
                case "4":
                    permanentOrTemporary = "permanent";
                    twoWay = true;
                    validTradeType = true;
                    break;
                case "back":
                    return false;
                default:
                    presenter.tryAgain();
            }
        } while (!validTradeType);

        if (twoWay) {
            // Need to offer an item in return if two way
            thisUserItemId = getItemToOffer(otherUsername);
            if (thisUserItemId == null) {
                return false;
            }
            List<String> details = new ArrayList<String>(Arrays.asList(username, otherUsername, thisUserItemId, itemId));
            tradeId = tradeModel.getTradeManager().addTrade("twoWay", permanentOrTemporary, details);
        }
        else {
            int credit = tradeModel.getUserManager().getCreditByUsername(username);
            if (credit < 0) {
                presenter.notEnoughCredits(Math.abs(credit));
                return false;
            }
            int numOngoing = tradeModel.getTradeManager().getTradesOfUser(username, "ongoing").size();
            int numCompleted = tradeModel.getTradeManager().getTradesOfUser(username, "completed").size();
            if (numOngoing == 0 && numCompleted == 0) {
                presenter.newAccount();
                return false;
            }
            List<String> details = new ArrayList<String>(Arrays.asList(otherUsername, username, itemId));
            tradeId = tradeModel.getTradeManager().addTrade("oneWay", permanentOrTemporary, details);
        }

        List<String> meetingDetails = getMeetingDetails();
        try {
            String meetingId = tradeModel.getMeetingManager().createMeeting(meetingDetails.get(0), parseDateString(meetingDetails.get(1)), username, tradeId);
            tradeModel.getTradeManager().addMeetingToTrade(tradeId, meetingId);
        } catch (ParseException e) {
            // This shouldn't happen because date parsing was already checked
            System.out.println("Invalid date and time!");
            return false;
        }

        return true;
    }

    private String getItemToOffer(String otherUsername) throws IOException {
        // two way requires user to propose item from other user's wishlist
        Set<String> otherWishlist = tradeModel.getUserManager().getWishlistByUsername(otherUsername);
        List<String> userItemsAvailable = getUserAvailableItems(username);
        List<String> overlappingItems = new ArrayList<>();
        for (String s : otherWishlist) {
            if (userItemsAvailable.contains(s)) {
                overlappingItems.add(s);
            }
        }
        String thisUserItemId = null;
        if (overlappingItems.size() > 0) {
            // if there are overlapping items, show those to choose from
            presenter.itemsToOffer(getItemsInfo(overlappingItems), true);
            thisUserItemId = br.readLine();
            while (!overlappingItems.contains(thisUserItemId)) {
                presenter.tryAgain();
                thisUserItemId = br.readLine();
            }
        } else if (userItemsAvailable.size() > 0) {
            // if not let user choose any item from their available list
            presenter.itemsToOffer(getItemsInfo(userItemsAvailable), false);
            thisUserItemId = br.readLine();
            while (!userItemsAvailable.contains(thisUserItemId)) {
                presenter.tryAgain();
                thisUserItemId = br.readLine();
            }
        } else {
            // they have no items available so let them know this trade is not possible
            presenter.noItemsToOffer();
        }
        return thisUserItemId;
    }
    private List<String> getMeetingDetails() throws IOException {
        List<String> meetingDetails = new ArrayList<>();

        // Get location
        presenter.askMeetingLocation();
        String location = br.readLine();
        meetingDetails.add(location);

        // Get date
        String dateString = null;
        Date date = null;
        do {
            try {
                presenter.askMeetingDate();
                dateString = br.readLine();
                date = parseDateString(dateString); // check formatting is valid
            }
            catch (ParseException e) {
                presenter.tryAgain();
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
//        // Show items available not owned by user and owned by users in same city
//        // TODO: consider using a filter system with filter classes that implement interface (check method under)
//        String userRank = tradeModel.getUserManager().getRankByUsername(username);
//        Set<String> itemsAvailable = tradeModel.getItemManager().getAvailableItems(userRank);
//
//        if (tradeModel.getUserManager().getPrivateUser().contains(username)) { // if private user
//            Set<String> privateAccounts = tradeModel.getUserManager().getFriendList(username); // get friend not on vacation
//            privateAccounts.removeIf(user -> tradeModel.getUserManager().getOnVacation().contains(user));
//            itemsAvailable.removeIf(item -> !privateAccounts.contains(tradeModel.getItemManager().getOwner(item)));
//        } else {  //public user
//            Set<String> privateAccounts = tradeModel.getUserManager().getPrivateUser();
//            privateAccounts.removeAll(tradeModel.getUserManager().getFriendList(username));
//            privateAccounts.addAll(tradeModel.getUserManager().getOnVacation()); // remove not friend & vacation
//            itemsAvailable.removeIf(item -> privateAccounts.contains(tradeModel.getItemManager().getOwner(item)));
//        }
//        List<String> itemsToShow = new ArrayList<>();
//
//        for (String itemId : itemsAvailable) {
//            String otherUsername = tradeModel.getItemManager().getOwner(itemId);
//            String thisUserCity = tradeModel.getUserManager().getCityByUsername(username);
//            String otherUserCity = tradeModel.getUserManager().getCityByUsername(otherUsername);
//            if (!otherUsername.equals(username) && thisUserCity.equals(otherUserCity)) {
//                itemsToShow.add(tradeModel.getItemManager().getItemInfo(itemId));
//            }
//        }
//        if (itemsToShow.size() == 0) {
//            presenter.noItemsToTrade();
//            return null;
//        }
//        presenter.availableItemsMenu(itemsToShow);
//        String itemId = br.readLine();
//        while (!itemsAvailable.contains(itemId) && !itemId.equals("back")) { //what if they enter available item not shown
//            // Validate input
//            presenter.tryAgain();
//            itemId = br.readLine();
//        }
//        if (itemId.equals("back")) {
//            itemId = null;
//        }
//        return itemId;
//    }

    private String getItemIdChoice() throws IOException {
        String userRank = tradeModel.getUserManager().getRankByUsername(username);
        Set<String> itemsAvailable = new HashSet<>();
        if (tradeModel.getUserManager().getPrivateUser().contains(username)) { // if private user
            itemsAvailable.addAll(getAvailableItemsPrivateAccount(tradeModel.getItemManager().getAvailableItems(userRank)));
        } else {  //public user
            itemsAvailable.addAll(getAvailableItemsPublicAccount(tradeModel.getItemManager().getAvailableItems(userRank)));
        }
        List<String> itemsToShow = new ArrayList<>(itemsAvailable);

        if (itemsToShow.size() == 0) {
            presenter.noItemsToTrade();
            return null;
        }
        presenter.availableItemsMenu(itemsToShow);
        String itemId = br.readLine();
        while (!itemsToShow.contains(itemId) && !itemId.equals("back")) {
            // Validate input
            presenter.tryAgain();
            itemId = br.readLine();
        }
        if (itemId.equals("back")) {
            itemId = null;
        }
        return itemId;
    }

    private Set<String> getAvailableItemsPrivateAccount(Set<String> allItems){
        Set<String> couldTrade = tradeModel.getUserManager().getFriendList(username);
        couldTrade.removeIf(user -> tradeModel.getUserManager().getOnVacation().contains(user)); //remove vacation and different city
        couldTrade.removeIf(friend -> !tradeModel.getUserManager().getCityByUsername(username).equals(tradeModel.getUserManager().getCityByUsername(friend)));
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

    private void unfreezeRequest() throws IOException {
        presenter.frozenAccount();
        String input = br.readLine();
        while (!input.equals("0") && !input.equals("1")){
            presenter.tryAgain();
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
}
