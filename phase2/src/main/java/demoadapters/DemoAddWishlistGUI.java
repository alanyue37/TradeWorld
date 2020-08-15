package demoadapters;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;
import useradapters.AddWishlistGUI;

public class DemoAddWishlistGUI extends AddWishlistGUI implements RunnableGUI {

    public DemoAddWishlistGUI(Stage stage, int width, int height, TradeModel model) {
        super(stage, width,height, model);
        setTableViewCreator(new DemoTableViewCreator(model));
    }


    @Override
    protected void configureButtons(Button addButton, Button removeButton) {
        // switch items without making changes at backend
        TableView.TableViewSelectionModel<ObservableList<String>> itemSelection = getItemTable().getSelectionModel();
        TableView.TableViewSelectionModel<ObservableList<String>> wishlistSelection = getWishlistTable().getSelectionModel();
        addButton.setOnAction(actionEvent -> {
            ObservableList<ObservableList<String>> selectedItems = itemSelection.getSelectedItems();
            if (selectedItems.size() > 0) {
                if (!getWishlistTable().getItems().contains(selectedItems.get(0))) {
                    getWishlistTable().getItems().add(selectedItems.get(0));
                }
            }
        });

        removeButton.setOnAction(actionEvent -> {
            ObservableList<ObservableList<String>> selectedItems = wishlistSelection.getSelectedItems();
            if (selectedItems.size() > 0) {
                getWishlistTable().getItems().remove(selectedItems.get(0));
            }
        });
    }

}