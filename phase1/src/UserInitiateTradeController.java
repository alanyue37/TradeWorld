import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserInitiateTradeController implements RunnableController {
    private final TradeModel tradeModel;
    private final UserInitiateTradePresenter presenter;
    private final BufferedReader br;
    private final String username;

    public UserInitiateTradeController(TradeModel tradeModel, String username) {
        this.tradeModel = tradeModel;
        this.presenter = new UserInitiateTradePresenter();
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.username = username;
    }

    @Override
    public void run() {
        try {
            initiateTrade();
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        }
    }

    private boolean initiateTrade() throws IOException {
        if (tradeModel.getUserManager().isFrozen(username)) {
            presenter.frozenAccount();
            return false;
        }
        String itemId = getItemIdChoice();
        boolean success = createTrade(itemId);
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
            tradeId = tradeModel.getTradeManager().addOneWayTrade("temporary", otherUsername, username, itemId);
        }

        List<String> meetingDetails = getMeetingDetails();
        try {
            tradeModel.getTradeManager().addMeetingToTrade(tradeId, meetingDetails.get(0), parseDateString(meetingDetails.get(1)), username);
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
        // Show items available not owned by user
        List<String> itemsAvailable = tradeModel.getItemManager().getAvailableItems();
        List <String> itemsToShow = new ArrayList<>();
        for (String itemId : itemsAvailable) {
            if (!tradeModel.getItemManager().getOwner(itemId).equals(username)) {
                itemsToShow.add(tradeModel.getItemManager().getItemInfo(itemId));
            }
        }
        presenter.availableItemsMenu(itemsToShow);
        String itemId = br.readLine();
        while (!itemsAvailable.contains(itemId)) {
            // Validate input
            presenter.tryAgain();
            itemId = br.readLine();
        }
        return itemId;
    }

    private List<String> getItemsInfo(List<String> itemIds) {
        List <String> itemsInfo = new ArrayList<>();
        for (String itemId : itemIds) {
                itemsInfo.add(tradeModel.getItemManager().getItemInfo(itemId));
        }
        return itemsInfo;
    }
}
