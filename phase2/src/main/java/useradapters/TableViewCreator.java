package useradapters;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tradegateway.TradeModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TableViewCreator {

    private final TradeModel tradeModel;
    private String[] colTitles;
    private double[] widths;
    private ArrayList<String[]> itemsToShow;

    public TableViewCreator (TradeModel tm) {
        tradeModel = tm;
        itemsToShow = new ArrayList<>();
    }

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
     * Sets itemsToShow to the user's own inventory.
     */
    protected void viewInventory() {
        newItemsToShow();
        Set<String> userInventory = tradeModel.getItemManager().getInventory(tradeModel.getCurrentUser());
        for (String itemID : userInventory) {
            if (tradeModel.getItemManager().getAvailable(itemID)) {
                itemsToShow.add(new String[]{itemID,
                        tradeModel.getItemManager().getName(itemID),
                        tradeModel.getItemManager().getDescription(itemID),
                        "Yes"});
            } else {
                itemsToShow.add(new String[]{itemID,
                        tradeModel.getItemManager().getName(itemID),
                        tradeModel.getItemManager().getDescription(itemID),
                        "No"});
            }
        }
    }

    /**
     * Sets itemsToShow to the system inventory of users in same city (except items in current user's inventory).
     */
    protected void viewAllItems() {
        newItemsToShow();
        String userRank = tradeModel.getUserManager().getRankByUsername(tradeModel.getCurrentUser());
        Set<String> itemsAvailable = new HashSet<>();

        if (tradeModel.getUserManager().getPrivateUser().contains(tradeModel.getCurrentUser())) { // if private user
            itemsAvailable.addAll(getAvailableItemsPrivateAccount(tradeModel.getItemManager().getAvailableItems(userRank)));
        } else {  // public user
            itemsAvailable.addAll(getAvailableItemsPublicAccount(tradeModel.getItemManager().getAvailableItems(userRank)));
        }

        for (String itemID : itemsAvailable) {
            itemsToShow.add(new String[]{itemID,
                tradeModel.getItemManager().getName(itemID),
                tradeModel.getItemManager().getOwner(itemID),
                tradeModel.getItemManager().getDescription(itemID)});
        }
    }

    private Set<String> getAvailableItemsPrivateAccount(Set<String> allItems){
        Set<String> couldTrade = tradeModel.getUserManager().getFriendList(tradeModel.getCurrentUser());
        couldTrade.removeIf(user -> tradeModel.getUserManager().getOnVacation().contains(user)); // remove users on vacation
        couldTrade.removeIf(friend -> !tradeModel.getUserManager().getCityByUsername(tradeModel.getCurrentUser()).equals(tradeModel.getUserManager().getCityByUsername(friend))); // remove different city
        allItems.removeIf(item -> !couldTrade.contains(tradeModel.getItemManager().getOwner(item)));
        return allItems;
    }

    private Set<String> getAvailableItemsPublicAccount(Set<String> allItems){
        Set<String> couldNotTrade = tradeModel.getUserManager().getPrivateUser(); // add private users
        couldNotTrade.removeAll(tradeModel.getUserManager().getFriendList(tradeModel.getCurrentUser())); // remove private users who are friends
        couldNotTrade.addAll(tradeModel.getUserManager().getOnVacation()); // add users on vacation
        couldNotTrade.add(tradeModel.getCurrentUser()); // add the user itself
        String thisUserCity = tradeModel.getUserManager().getCityByUsername(tradeModel.getCurrentUser());
        allItems.removeIf(item -> couldNotTrade.contains(tradeModel.getItemManager().getOwner(item)) |
                !thisUserCity.equals(tradeModel.getUserManager().getCityByUsername(tradeModel.getItemManager().getOwner(item))));
        return allItems;
    }

    /**
     * Sets itemsToShow to the user's wishlist.
     */
    protected void viewWishlist() {
        newItemsToShow();
        Set<String> userWishlist = tradeModel.getUserManager().getWishlistByUsername(tradeModel.getCurrentUser());
        for (String itemID : userWishlist) {
            itemsToShow.add(new String[]{itemID,
                    tradeModel.getItemManager().getName(itemID),
                    tradeModel.getItemManager().getOwner(itemID),
                    tradeModel.getItemManager().getDescription(itemID)});
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
                    tradeModel.getItemManager().getName(itemID),
                    tradeModel.getItemManager().getOwner(itemID),
                    tradeModel.getItemManager().getDescription(itemID)});
        }
    }

    protected ArrayList<String[]> getItemsToShow() {
        return itemsToShow;
    }

    protected void newItemsToShow() {
        itemsToShow = new ArrayList<>();
    }

}
