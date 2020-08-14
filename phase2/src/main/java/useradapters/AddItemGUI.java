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
import trademisc.RunnableGUI;

public class AddItemGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final UserController controller;
    private final TradeModel tradeModel;
    private final int width;
    private final int height;
    private final TableViewCreator creator;
    private GridPane grid;

    public AddItemGUI(Stage stage, int width, int height, TradeModel model) {
        this.stage = stage;
        controller = new UserController(model);
        tradeModel = model;
        this.width = width;
        this.height = height;
        creator = new TableViewCreator(tradeModel);
    }

    @Override
    public void initialScreen(){

    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return grid;
    }

    @Override
    public void showScreen() {
        initializeScreen();
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }

    public void initializeScreen() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TableView<ObservableList<String>> table = creator.create("own inventory");

        Text title = new Text("Current Inventory");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 0, 0, 2, 1);

        grid.add(table, 0, 1, 5, 5);

        Text prompt = new Text("Add item to inventory");
        prompt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Label itemNameLabel = new Label("Item name");
        TextField itemNameField = new TextField();
        Label itemDescriptionLabel = new Label("Item description");
        TextArea itemDescriptionField = new TextArea();

        Button createItemButton = new Button("Create Item");

        grid.add(itemNameLabel, 0, 7);
        grid.add(itemNameField, 1, 7);
        grid.add(itemDescriptionLabel, 0, 8);
        grid.add(itemDescriptionField, 1, 8);
        grid.add(createItemButton, 1, 9);

        table.setPlaceholder(new Label("No items in inventory"));

        createItemButton.setOnAction(actionEvent -> {
            controller.createItem(tradeModel.getCurrentUser(), itemNameField.getText(), itemDescriptionField.getText());
            itemNameField.clear();
            itemDescriptionField.clear();
            Text message = new Text("Item added");
            grid.add(message, 0, 10, 2, 1);
            table.setItems(creator.create("own inventory").getItems());
        });
    }
}