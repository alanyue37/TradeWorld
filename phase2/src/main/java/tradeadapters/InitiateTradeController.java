package tradeadapters;

import tradegateway.TradeModel;
import trademisc.RunnableController;
import usercomponent.ItemSets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
            tradeId = tradeModel.getTradeManager().addTwoWayTrade(permanentOrTemporary, username, otherUsername, thisUserItemId, itemId);
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
            tradeId = tradeModel.getTradeManager().addOneWayTrade(permanentOrTemporary, otherUsername, username, itemId);
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
        Set<String> otherWishlist = tradeModel.getUserManager().getSetByUsername(otherUsername, ItemSets.WISHLIST);
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
        List<String> itemsAvailable = tradeModel.getItemManager().getAvailableItems();
        List<String> userItemsAvailable = new ArrayList<>();
        for (String itemId: itemsAvailable) {
            if (tradeModel.getItemManager().getOwner(itemId).equals(username)) {
                userItemsAvailable.add(itemId);
            }
        }
        return userItemsAvailable;
    }

    private String getItemIdChoice() throws IOException {
        // Show items available not owned by user and owned by users in same city
        // TODO: consider using a filter system with filter classes that implement interface
        List<String> itemsAvailable = tradeModel.getItemManager().getAvailableItems();
        List <String> itemsToShow = new ArrayList<>();

        for (String itemId : itemsAvailable) {
            String otherUsername = tradeModel.getItemManager().getOwner(itemId);
            String thisUserCity = tradeModel.getUserManager().getCityByUsername(username);
            String otherUserCity = tradeModel.getUserManager().getCityByUsername(otherUsername);
            if (!otherUsername.equals(username) && thisUserCity.equals(otherUserCity)) {
                itemsToShow.add(tradeModel.getItemManager().getItemInfo(itemId));
            }
        }
        if (itemsToShow.size() == 0) {
            presenter.noItemsToTrade();
            return null;
        }
        presenter.availableItemsMenu(itemsToShow);
        String itemId = br.readLine();
        while (!itemsAvailable.contains(itemId) && !itemId.equals("back")) {
            // Validate input
            presenter.tryAgain();
            itemId = br.readLine();
        }
        if (itemId.equals("back")) {
            itemId = null;
        }
        return itemId;
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
