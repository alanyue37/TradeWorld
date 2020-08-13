package useradapters;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tradegateway.TradeModel;

import java.util.ArrayList;
import java.util.Set;

public class TableViewCreator {

    private final TradeModel tradeModel;
    private String[] colTitles;
    private double[] widths;
    private ArrayList<String[]> itemsToShow;

    public TableViewCreator (TradeModel tm) {
        tradeModel = tm;
    }

    public TableView<ObservableList<String>> create(String type) {

        switch(type) {
            case "own inventory":
                viewInventory();
                colTitles = new String[]{"ID", "Name", "Description", "Available"};
                widths = new double[]{0.1, 0.3, 0.5, 0.1};
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
    public void viewInventory() {
        itemsToShow = new ArrayList<>();
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
    public void viewAllItems() {
        itemsToShow = new ArrayList<>();
        Set<String> items = tradeModel.getItemManager().getItemsByStage("common");
        if (tradeModel.getUserManager().getRankByUsername(tradeModel.getCurrentUser()).equals("gold")) {
            items.addAll(tradeModel.getItemManager().getItemsByStage("early"));
        }
        Set<String> userInventory = tradeModel.getItemManager().getInventory(tradeModel.getCurrentUser());

        for (String itemID : items) {
            String otherUsername = tradeModel.getItemManager().getOwner(itemID);
            String thisUserCity = tradeModel.getUserManager().getCityByUsername(tradeModel.getCurrentUser());
            String otherUserCity = tradeModel.getUserManager().getCityByUsername(otherUsername);
            if (!userInventory.contains(itemID) && thisUserCity.equals(otherUserCity)) {
                itemsToShow.add(new String[]{itemID,
                        tradeModel.getItemManager().getName(itemID),
                        tradeModel.getItemManager().getOwner(itemID),
                        tradeModel.getItemManager().getDescription(itemID)});
            }
        }
    }

    /**
     * Sets itemsToShow to the user's wishlist.
     */
    public void viewWishlist() {
        itemsToShow = new ArrayList<>();
        Set<String> userWishlist =  tradeModel.getUserManager().getWishlistByUsername(tradeModel.getCurrentUser());
        for (String itemID : userWishlist) {
            itemsToShow.add(new String[]{itemID,
                    tradeModel.getItemManager().getName(itemID),
                    tradeModel.getItemManager().getOwner(itemID),
                    tradeModel.getItemManager().getDescription(itemID)});
        }
    }
}
