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

import java.util.ArrayList;
import java.util.List;

/**
 * The GUI for a trading user's inventory tab.
 */
public class AddItemGUI implements RunnableGUI {
    private final Stage stage;
    private final UserController controller;
    private final TradeModel tradeModel;
    private final int width;
    private final int height;
    private TableViewCreator creator;
    private GridPane grid;
    private TextField itemNameField;
    private TextField itemDescriptionField;
    private Text message;
    private TableView<ObservableList<String>> inventoryTable;

    /**
     * Creates a new AddItemGUI.
     *
     * @param stage The stage to show the resulting scene on
     * @param width The width of the stage
     * @param height The height of the stage
     * @param model The current TradeModel
     */
    public AddItemGUI(Stage stage, int width, int height, TradeModel model) {
        this.stage = stage;
        controller = new UserController(model);
        tradeModel = model;
        this.width = width;
        this.height = height;
        creator = new TableViewCreator(tradeModel);
    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return grid;
    }

    @Override
    public void showScreen() {
        initializeScreen();
        Scene scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }

    /**
     * Initializes a new GridPane with the objects to be displayed in the GUI.
     */
    public void initializeScreen() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        inventoryTable = creator.create("own inventory");

        inventoryTable.prefWidthProperty().bind(stage.widthProperty());

        Text title = new Text("Current Inventory");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 0, 0, 2, 1);

        grid.add(inventoryTable, 0, 1, 4, 5);

        Text prompt = new Text("Add item to inventory");
        prompt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Label itemNameLabel = new Label("Item name:");
        itemNameField = new TextField();
        Label itemDescriptionLabel = new Label("Item description:");
        itemDescriptionField = new TextField();

        Button createItemButton = new Button("Create Item");

        grid.add(itemNameLabel, 0, 7);
        grid.add(itemNameField, 1, 7);
        grid.add(itemDescriptionLabel, 0, 8);
        grid.add(itemDescriptionField, 1, 8);
        grid.add(createItemButton, 1, 9);

        inventoryTable.setPlaceholder(new Label("No items in inventory"));

        message = new Text("");
        grid.add(message, 0, 10, 2, 1);

        configureButtons(createItemButton);

    }

    /**
     * Sets the action listener for the createItemButton depending on user input.
     *
     * @param createItemButton the button for creating an item
     */
    protected void configureButtons(Button createItemButton) {
        createItemButton.setOnAction(actionEvent -> {
            if (invalidInput()) {
                setMessage("Fields cannot be empty. Please try again.");
            } else {
                controller.createItem(itemNameField.getText(), itemDescriptionField.getText());
                clearInputFields();
                setMessage("Item added");
                // table.setItems(creator.create("own inventory").getItems());
            }
        });
    }

    /**
     * Gets whether one or both of the item name or descriptions fields are empty.
     *
     * @return whether the fields are empty
     */
    protected boolean invalidInput() {
        return itemNameField.getText().isEmpty() || itemDescriptionField.getText().isEmpty();
    }

    /**
     * Sets the message corresponding to whether an item was successfully added or not.
     */
    protected void setMessage(String text) {
        message.setText(text);
    }

    /**
     * Clears the item name and description fields.
     */
    protected void clearInputFields() {
        itemNameField.clear();
        itemDescriptionField.clear();
    }

    /**
     * Sets the TableViewCreator that will create the TableView.
     *
     * @param creator the TableViewCreator to use
     */
    protected void setTableViewCreator(TableViewCreator creator) {
        this.creator = creator;
    }

    /**
     * Gets the TableView with the user's inventory that should be displayed in the GUI.
     *
     * @return the TableView that should be displayed
     */
    protected TableView<ObservableList<String>> getInventoryTable() {
        return inventoryTable;
    }

    /**
     * Gets the inputs in the item name and description fields.
     *
     * @return a list with the inputs
     */
    protected List<String> getInputStrings() {
        List<String> inputStrings = new ArrayList<>();
        inputStrings.add(itemNameField.getText());
        inputStrings.add(itemDescriptionField.getText());
        return inputStrings;
    }
}