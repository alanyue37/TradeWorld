package useradapters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tradegateway.TradeModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Factory responsible for the creation of TableView tables used in various GUIs.
 */
public class TableViewCreator {

    private final TradeModel tradeModel;
    private String[] colTitles;
    private double[] widths;
    private ArrayList<String[]> itemsToShow;
    private Gson gson;

    /**
     * Instantiates a new TableViewCreator.
     *
     * @param tm The current TradeModel
     */
    public TableViewCreator (TradeModel tm) {
        tradeModel = tm;
        itemsToShow = new ArrayList<>();
        gson = new Gson();
    }

    /**
     * Returns a new TableView that is filled with Strings.
     *
     * @param type The type of table to create: one of "own inventory", "all items", "wishlist", or "pending"
     * @return the created table
     */
    public TableView<ObservableList<String>> create(String type) {

        switch(type) {
            case "own inventory":
                viewInventory();
                colTitles = new String[]{"ID", "Name", "Description", "Available"};
                widths = new double[]{0.1, 0.25, 0.5, 0.15};
                break;
            case "all items":
                viewAllItems();
                colTitles = new String[]{"ID", "Name", "Owner", "Description"};
                widths = new double[]{0.1, 0.2, 0.2, 0.5};
                break;
            case "wishlist":
                viewWishlist();
                colTitles = new String[]{"ID", "Name", "Owner", "Description"};
                widths = new double[]{0.1, 0.2, 0.2, 0.5};
                break;
            case "pending":
                viewPending();
                colTitles = new String[]{"ID", "Name", "Owner", "Description"};
                widths = new double[]{0.1, 0.2, 0.2, 0.5};
                break;
        }

        TableView<ObservableList<String>> table = new TableView<>();
        ObservableList<ObservableList<String>> observableItemList = FXCollections.observableArrayList();
        for (String[] item : itemsToShow) {
            observableItemList.add(FXCollections.observableArrayList(item));
        }
        table.setItems(observableItemList);

        for (int i = 0; i < colTitles.length; i++) {
            final int colNum = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(colTitles[colNum]);
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(colNum))
            );
            table.getColumns().add(column);
            column.prefWidthProperty().bind(table.widthProperty().multiply(widths[colNum]));
        }

        return table;
    }

    /**
     * Sets itemsToShow to the current user's own inventory.
     */
    protected void viewInventory() {
        newItemsToShow();
        Set<String> userInventory = tradeModel.getItemManager().getInventory(tradeModel.getCurrentUser());
        for (String itemID : userInventory) {
            if (getItemInfo(itemID).get("available").equals("true")) {
                itemsToShow.add(new String[]{itemID,
                        getItemInfo(itemID).get("name"),
                        getItemInfo(itemID).get("description"),
                        "Yes"});
            } else {
                itemsToShow.add(new String[]{itemID,
                        getItemInfo(itemID).get("name"),
                        getItemInfo(itemID).get("description"),
                        "No"});
            }
        }
    }

    /**
     * Sets itemsToShow to the items that the current user can trade with.
     */
    protected void viewAllItems() {
        newItemsToShow();
        String userRank = tradeModel.getUserManager().getRankByUsername(tradeModel.getCurrentUser());
        Set<String> itemsAvailable = new HashSet<>();

        if (tradeModel.getUserManager().getPrivateUser().contains(tradeModel.getCurrentUser())) { // if private user
            itemsAvailable.addAll(getAvailableItemsPrivateAccount(tradeModel.getItemManager().getAvailableItems(userRank)));
        } else { // public user
            itemsAvailable.addAll(getAvailableItemsPublicAccount(tradeModel.getItemManager().getAvailableItems(userRank)));
        }

        for (String itemID : itemsAvailable) {
            itemsToShow.add(new String[]{itemID,
                getItemInfo(itemID).get("name"),
                getItemInfo(itemID).get("owner"),
                getItemInfo(itemID).get("description"),
                });
        }
    }

    private Set<String> getAvailableItemsPrivateAccount(Set<String> allItems){
        Set<String> couldTrade = tradeModel.getUserManager().getFriendList(tradeModel.getCurrentUser());
        couldTrade.removeIf(user -> tradeModel.getUserManager().getOnVacation().contains(user)); // remove users on vacation
        couldTrade.removeIf(friend -> !tradeModel.getUserManager().getCityByUsername(tradeModel.getCurrentUser()).equalsIgnoreCase(tradeModel.getUserManager().getCityByUsername(friend))); // remove different city
        allItems.removeIf(item -> !couldTrade.contains(getItemInfo(item).get("owner")));
        return allItems;
    }

    private Set<String> getAvailableItemsPublicAccount(Set<String> allItems){
        Set<String> couldNotTrade = tradeModel.getUserManager().getPrivateUser(); // add private users
        couldNotTrade.removeAll(tradeModel.getUserManager().getFriendList(tradeModel.getCurrentUser())); // remove private users who are friends
        couldNotTrade.addAll(tradeModel.getUserManager().getOnVacation()); // add users on vacation
        couldNotTrade.add(tradeModel.getCurrentUser()); // add the user itself
        String thisUserCity = tradeModel.getUserManager().getCityByUsername(tradeModel.getCurrentUser());
        allItems.removeIf(item -> couldNotTrade.contains(getItemInfo(item).get("owner")) |
                !thisUserCity.equalsIgnoreCase(tradeModel.getUserManager().getCityByUsername(getItemInfo(item).get("owner"))));
        return allItems;
    }

    /**
     * Sets itemsToShow to the current user's wishlist.
     */
    protected void viewWishlist() {
        newItemsToShow();
        Set<String> userWishlist = tradeModel.getUserManager().getWishlistByUsername(tradeModel.getCurrentUser());
        for (String itemID : userWishlist) {
            itemsToShow.add(new String[]{itemID,
                    getItemInfo(itemID).get("name"),
                    getItemInfo(itemID).get("owner"),
                    getItemInfo(itemID).get("description")});
        }
    }

    /**
     * Sets itemsToShow to the pending items.
     */
    protected void viewPending() {
        newItemsToShow();
        Set<String> userWishlist = tradeModel.getItemManager().getItemsByStage("pending");
        for (String itemID : userWishlist) {
            itemsToShow.add(new String[]{itemID,
                    getItemInfo(itemID).get("name"),
                    getItemInfo(itemID).get("owner"),
                    getItemInfo(itemID).get("description"),});
        }
    }

    /**
     * Sets itemsToShow to the pending items.
     *
     * @return the items to show in the table
     */
    protected ArrayList<String[]> getItemsToShow() {
        return itemsToShow;
    }

    /**
     * Clears all the items in itemsToShow.
     */
    protected void newItemsToShow() {
        itemsToShow = new ArrayList<>();
    }

    private Map<String, String> getItemInfo(String itemID) {
        return gson.fromJson(tradeModel.getItemManager().getItemInfoJSON(itemID), new TypeToken<Map<String, String>>() {}.getType());
    }
}