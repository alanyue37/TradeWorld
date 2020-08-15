package demoadapters;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import trademain.RunnableGUI;
import useradapters.AddWishlistGUI;

/**
 * The GUI for a demo user's wishlist.
 */
public class DemoAddWishlistGUI extends AddWishlistGUI implements RunnableGUI {

    /**
     * Creates a new DemoAddWishlistGUI with a specified width and height and the current TradeModel.
     *
     * @param stage The stage to show the resulting scene on
     * @param width The width of the stage
     * @param height The height of the stage
     * @param model The current TradeModel
     */
    public DemoAddWishlistGUI(Stage stage, int width, int height, TradeModel model) {
        super(stage, width,height, model);
        setTableViewCreator(new DemoTableViewCreator(model));
    }

    /**
     * Sets the action listener for the buttons depending on items selected.
     *
     * @param addButton the button for adding an item to the wishlist
     * @param removeButton the button for removing an item from the wishlist
     */
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