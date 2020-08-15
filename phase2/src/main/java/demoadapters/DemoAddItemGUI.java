package demoadapters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import trademain.RunnableGUI;
import useradapters.AddItemGUI;

public class DemoAddItemGUI extends AddItemGUI implements RunnableGUI {
    private int newItemId;

    public DemoAddItemGUI(Stage stage, int width, int height, TradeModel model) {
        super(stage, width, height, model);
        setTableViewCreator(new DemoTableViewCreator(model));
        newItemId = 10;
    }

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