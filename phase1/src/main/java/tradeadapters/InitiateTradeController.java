package tradeadapters;

import trademisc.RunnableController;
import tradegateway.TradeModel;
import usercomponent.ItemSets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
     * It tries to initiateTrade and catches IOException if something goes wrong.
     */
    @Override
    public void run() {
        try {
            initiateTrade();
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        }
    }

    /**
     * This method prevents the User from initiating the trade if the account of the User is frozen and if the item
     * does not exist. Returns true iff the User account is not frozen and iff the item exists.
     * @return  false if the user account is frozen or if the item does not exist.
     * @throws IOException  If something goes wrong.
     */
    private boolean initiateTrade() throws IOException {
        boolean success = false;
        if (tradeModel.getUserManager().isFrozen(username)) {
            unfreezeRequest();
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

    /**
     * Returns true iff the trade could occur, false otherwise
     * @param itemId    The id of the item
     * @return  True iff the trade could be conducted, false otherwise
     * @throws IOException  If something goes wrong
     */
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
            tradeId = tradeModel.getTradeManager().addTwoWayTrade(permanentOrTemporary, username, otherUsername,
                    thisUserItemId, itemId);
        }
        else {
            tradeId = tradeModel.getTradeManager().addOneWayTrade(permanentOrTemporary, otherUsername, username, itemId);
        }

        List<String> meetingDetails = getMeetingDetails();
        try {
            tradeModel.getTradeManager().editMeetingOfTrade(tradeId, meetingDetails.get(0),
                    parseDateString(meetingDetails.get(1)), username, "add");
        } catch (ParseException e) {
            // This shouldn't happen because date parsing was already checked
            System.out.println("Invalid date and time!");
            return false;
        }

        return true;
    }

    /**
     * Returns an item from the User's inventory (i.e., this item could be in the wishlist of the other User or not)
     * which could be traded in a two way trade.
     * @param otherUsername The username of the other Trading User
     * @return  Returns an item from the User's inventory which could be traded in a two way trade
     * @throws IOException  If something goes wrong
     */
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

    /**
     * Returns a list of strings representing the details of the meeting (i.e., location and date).
     * @return  A list of strings representing the details of the meeting (i.e., location and date)
     * @throws IOException  If something goes wrong
     */
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

    /**
     * Converts the date which is a string into dd/MM/yyyy HH:mm.
     * @param dateString    The date in the string format
     * @return  Returns the date in this format: dd/MM/yyyy HH:mm
     * @throws ParseException   throws a ParseException if the date cannot be converted into dd/MM/yyyy HH:mm format
     */
    private Date parseDateString(String dateString) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return format.parse(dateString);
    }

    /**
     * Returns a list of strings of items that have the status as available and the User is the owner of the item.
     * @param username  The username of the User
     * @return  Returns a list of strings that are items that have the status as available in the inventory and the
     * User is the owner of the item
     */
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

    /**
     * Returns the item that the User wants to trade or borrow and is not the owner of the item.
     * @return  The item that the User wants to trade or borrow and is not the owner of the item
     * @throws IOException  If something goes wrong
     */
    private String getItemIdChoice() throws IOException {
        // Show items available not owned by user
        List<String> itemsAvailable = tradeModel.getItemManager().getAvailableItems();
        List <String> itemsToShow = new ArrayList<>();

        for (String itemId : itemsAvailable) {
            if (!tradeModel.getItemManager().getOwner(itemId).equals(username)) {
                itemsToShow.add(tradeModel.getItemManager().getItemInfo(itemId));
            }
        }
        if (itemsToShow.size() == 0) {
            presenter.noItemsToTrade();
            return null;
        }
        presenter.availableItemsMenu(itemsToShow);
        String itemId = br.readLine();
        while (!itemsAvailable.contains(itemId) && !itemId.equals("back")) {  // shouldn't itemsAvailable be itemsToShow?
            // Validate input
            presenter.tryAgain();
            itemId = br.readLine();
        }
        if (itemId.equals("back")) {
            itemId = null;
        }
        return itemId;
    }

    /**
     * The method allows the User to make a request to the admin to unfreeze their account.
     * @throws IOException  If something goes wrong
     */
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

    /**
     * Returns the information of all the items in the itemIds.
     * @param itemIds   The id of the items
     * @return  A list of strings which includes the information of the items.
     */
    private List<String> getItemsInfo(List<String> itemIds) {
        List <String> itemsInfo = new ArrayList<>();
        for (String itemId : itemIds) {
                itemsInfo.add(tradeModel.getItemManager().getItemInfo(itemId));
        }
        return itemsInfo;
    }
}
