package demoadapters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;

import tradegateway.TradeModel;
import trademain.RunnableGUI;
import useradapters.AddItemGUI;

/**
 * The GUI for a demo user's inventory tab.
 */
public class DemoAddItemGUI extends AddItemGUI implements RunnableGUI {
    private int newItemId;

    /**
     * Creates a new DemoAddItemGUI with a specified width and height and the current TradeModel.
     *
     * @param stage The stage to show the resulting scene on
     * @param width The width of the stage
     * @param height The height of the stage
     * @param model The current TradeModel
     */
    public DemoAddItemGUI(Stage stage, int width, int height, TradeModel model) {
        super(stage, width, height, model);
        setTableViewCreator(new DemoTableViewCreator(model));
        newItemId = 10;
    }

    /**
     * Sets the action listener for the createItemButton depending on user input.
     *
     * @param createItemButton the button for creating an item
     */
    @Override
    protected void configureButtons(Button createItemButton) {
        createItemButton.setOnAction(actionEvent -> {
            if (invalidInput()) {
                setMessage("Fields cannot be empty. Please try again.");
            } else {
                ObservableList<String> item = FXCollections.observableArrayList(String.valueOf(newItemId), getInputStrings().get(0), getInputStrings().get(0), "No");
                newItemId++;
                getInventoryTable().getItems().add(item);
                clearInputFields();
                setMessage("Item added");
            }
        });
    }
}