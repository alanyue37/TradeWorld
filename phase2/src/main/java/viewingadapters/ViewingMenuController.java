package viewingadapters;

import tradegateway.TradeModel;
import trademisc.RunnableController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * A controller class using the RunnableController interface for viewing the menu options.
 */
public class ViewingMenuController implements RunnableController {
    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final ViewingMenuPresenter presenter;
    private final String username;

    public ViewingMenuController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ViewingMenuPresenter();
    }

    @Override
    public void run() {
        try {
            browseViewOptions();
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }

    private void browseViewOptions() throws IOException {
        presenter.showViewingOptions();
        boolean validInput = false;
        do {
            String input = br.readLine();
            switch (input) {
                case "1": // view all confirmed items in system
                    viewSystemInventory();
                    validInput = true;
                    break;
                case "2": // view your inventory
                    viewUserInventory();
                    validInput = true;
                    break;
                case "3": // view your wishlist
                    viewWishlist();
                    validInput = true;
                    break;
                case "4": // view a user's profile
                    viewUsersProfile();
                    validInput = true;
                    break;
                case "back":
                    return;
                default:
                    presenter.tryAgain();
            }
        } while (!validInput);
    }

    /**
     * View the system inventory (all confirmed items -- available and unavailable -- in the system)
     */
    public void viewSystemInventory(){
        if (tradeModel.getUserManager().getPrivateUser().contains(username)){
            viewPrivateInventory();
        } else{
            viewPublicInventory();
        }
    }

    public void viewPublicInventory(){
        Set<String> hiddenUser = tradeModel.getUserManager().getPrivateUser(); //  private users except friends
        hiddenUser.removeAll(tradeModel.getUserManager().getFriendList(username));
        hiddenUser.addAll(tradeModel.getUserManager().getOnVacation()); //  people on vacation mode
        Set<String> confirmedItems = tradeModel.getItemManager().getItemsByStage("common");
        if (tradeModel.getUserManager().getRankByUsername(username).equals("gold")) {
            confirmedItems.addAll(tradeModel.getItemManager().getItemsByStage("early"));
        }
        confirmedItems.removeIf(item -> hiddenUser.contains(tradeModel.getItemManager().getOwner(item)));
        List<String> confirmedItemsInfo = getItemsInfo(confirmedItems);
        presenter.printSystemInventory(confirmedItemsInfo);
    }

    public void viewPrivateInventory(){
        Set<String> allFriends = tradeModel.getUserManager().getFriendList(username); // gets friend list
        allFriends.removeAll(tradeModel.getUserManager().getOnVacation()); // remove friends on vacation
        Set<String> confirmedItems = tradeModel.getItemManager().getItemsByStage("common");
        if (tradeModel.getUserManager().getRankByUsername(username).equals("gold")) {
            confirmedItems.addAll(tradeModel.getItemManager().getItemsByStage("early"));
        }
        confirmedItems.removeIf(item -> !allFriends.contains(tradeModel.getItemManager().getOwner(item)));
        List<String> confirmedItemsInfo = getItemsInfo(confirmedItems);
        presenter.printSystemInventory(confirmedItemsInfo);
    }

    /**
     * View user inventory
     */
    public void viewUserInventory() {
        List<String> items = getItemsInfo(tradeModel.getItemManager().getInventory(username));
        presenter.printUserInventory(items);
    }

    /**
     * View user wishlist
     */
    public void viewWishlist(){
        List<String> items =  getItemsInfo(tradeModel.getUserManager().getWishlistByUsername(username));
        presenter.printUserWishlist(items);
    }

    private List<String> getItemsInfo(Collection<String> itemIds) {
        List <String> itemsInfo = new ArrayList<>();
        for (String itemId : itemIds) {
            itemsInfo.add(tradeModel.getItemManager().getItemInfo(itemId));
        }
        return itemsInfo;
    }

    public void viewUsersProfile() throws IOException {
        // TODO: DELETE AFTER MOVING TO PROFILECONTROLLER
        presenter.printEnterUsername();
        String user = br.readLine();
        if (!tradeModel.getUserManager().containsTradingUser(user)){
            presenter.printInvalidUsername();
        } else{
            presenter.printEnterNumReviews();
            String numReviews = br.readLine();
            int numLastReviews = Integer.parseInt(numReviews);
            List<String> reviews = tradeModel.getReviewManager().viewProfile(username, numLastReviews);
            presenter.printViewLastReviews(numLastReviews, reviews);
        }
    }

}
