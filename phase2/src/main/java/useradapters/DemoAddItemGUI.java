package useradapters;

import javafx.collections.FXCollections;
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
import trademisc.RunnableGUI;

public class DemoAddItemGUI extends AddItemGUI implements RunnableGUI {
    private GridPane grid;
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
                ObservableList<String> item = FXCollections.observableArrayList(String.valueOf(newItemId), getInputStrings().get(0), getInputStrings().get(0), "Yes");
                newItemId++;
                getInventoryTable().getItems().add(item);
                clearInputFields();
                setMessage("Item added");
                //table.setItems(creator.create("own inventory").getItems());
            }
        });
    }
}