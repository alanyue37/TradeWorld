package useradapters;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import tradegateway.TradeModel;
import trademain.RunnableGUI;

/**
 * The GUI for a trading user's wishlist tab.
 */
public class AddWishlistGUI implements RunnableGUI {
    private final Stage stage;
    private final UserController controller;
    private final int width;
    private final int height;
    private TableViewCreator creator;
    private GridPane grid;
    private TableView<ObservableList<String>> itemTable;
    private TableView<ObservableList<String>> wishlistTable;

    /**
     * Creates a new AddItemGUI.
     *
     * @param stage The stage to show the resulting scene on
     * @param width The width of the stage
     * @param height The height of the stage
     * @param model The current TradeModel
     */
    public AddWishlistGUI(Stage stage, int width, int height, TradeModel model) {
        this.stage = stage;
        controller = new UserController(model);
        this.width = width;
        this.height = height;
        creator = new TableViewCreator(model);
    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return grid;
    }

    @Override
    public void showScreen() {
        Scene scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }

    /**
     * Initializes a new GridPane with the objects to be displayed in the GUI.
     */
    private void initializeScreen() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Text title1 = new Text("All available items");
        title1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title1, 0, 0, 2, 1);

        Text title2 = new Text("Your wishlist");
        title2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title2, 6, 0, 2, 1);

        itemTable = creator.create("all items");
        wishlistTable = creator.create("wishlist");

//        itemTable.prefHeightProperty().bind(stage.heightProperty());
        itemTable.prefWidthProperty().bind(stage.widthProperty());
//        wishlistTable.prefHeightProperty().bind(stage.heightProperty());
        wishlistTable.prefWidthProperty().bind(stage.widthProperty());

        grid.add(itemTable, 0, 1, 5, 5);
        grid.add(wishlistTable, 6, 1, 5, 5);

        Button addButton = new Button("Add item");
        grid.add(addButton, 0, 6, 2, 1);
        Button removeButton = new Button("Remove item");
        grid.add(removeButton, 6, 6, 2, 1);

        itemTable.setPlaceholder(new Label("No items to display"));
        wishlistTable.setPlaceholder(new Label("No items in wishlist"));

        configureButtons(addButton, removeButton);
    }

    /**
     * Sets the action listener for the buttons depending on items selected.
     *
     * @param addButton the button for adding an item to the wishlist
     * @param removeButton the button for removing an item from the wishlist
     */
    protected void configureButtons(Button addButton, Button removeButton) {
        TableView.TableViewSelectionModel<ObservableList<String>> itemSelection = itemTable.getSelectionModel();
        TableView.TableViewSelectionModel<ObservableList<String>> wishlistSelection = wishlistTable.getSelectionModel();
        addButton.setOnAction(actionEvent -> {
            ObservableList<ObservableList<String>> selectedItems = itemSelection.getSelectedItems();
            if (selectedItems.size() > 0) {
                controller.addItemToWishlist(selectedItems.get(0).get(0)); // new item added to wishlist
            }
        });

        removeButton.setOnAction(actionEvent -> {
            ObservableList<ObservableList<String>> selectedItems = wishlistSelection.getSelectedItems();
            if (selectedItems.size() > 0) {
                controller.removeItemFromWishlist(selectedItems.get(0).get(0));
                wishlistTable.getItems().remove(selectedItems.get(0));
            }
        });
    }

    /**
     * Sets the TableViewCreator that will create the TableView.
     *
     * @param creator the TableViewCreator to use
     */
    protected void setTableViewCreator (TableViewCreator creator) {
        this.creator = creator;
    }

    /**
     * Gets the TableView with the current user's wishlist that should be displayed in the GUI.
     *
     * @return the TableView that should be displayed
     */
    protected TableView<ObservableList<String>> getWishlistTable() {
        return wishlistTable;
    }

    /**
     * Gets the TableView with the all items the current user can trade that should be displayed in the GUI.
     *
     * @return the TableView that should be displayed
     */
    protected TableView<ObservableList<String>> getItemTable() {
        return itemTable;
    }
}