package useradapters;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private final UserController2 controller;
    private final UserPresenter2 presenter;
    private final TradeModel tradeModel;
    private final int width;
    private final int height;
    private String username;
    private final TableViewCreator creator;
    private GridPane grid;

    public AddItemGUI(Stage stage, int width, int height, TradeModel model, String username) {
        this.stage = stage;
        controller = new UserController2(model, username);
        presenter = new UserPresenter2(model, username);
        tradeModel = model;
        this.width = width;
        this.height = height;
        this.username = username;
        creator = new TableViewCreator(presenter);
    }

    @Override
    public void initialScreen(){
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TableView<ObservableList<String>> table = creator.create("own inventory");

        presenter.addItemScreen();
        Text title = new Text(presenter.next());
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 0, 0, 2, 1);

        grid.add(table, 0, 1, 5, 5);

        Text prompt = new Text(presenter.next());
        prompt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Label itemNameLabel = new Label(presenter.next());
        TextField itemNameField = new TextField();
        Label itemDescriptionLabel = new Label(presenter.next());
        TextArea itemDescriptionField = new TextArea();

        Button createItemButton = new Button(presenter.next());

        grid.add(itemNameLabel, 0, 7);
        grid.add(itemNameField, 1, 7);
        grid.add(itemDescriptionLabel, 0, 8);
        grid.add(itemDescriptionField, 1, 8);
        grid.add(createItemButton, 1, 9);

        presenter.placeholderText("inventory");
        table.setPlaceholder(new Label(presenter.next()));

        createItemButton.setOnAction(actionEvent -> {
            controller.createItem(username, itemNameField.getText(), itemDescriptionField.getText());
            itemNameField.clear();
            itemDescriptionField.clear();
            presenter.itemAdded();
            Text message = new Text(presenter.next());
            grid.add(message, 0, 10, 2, 1);
        });

        backButton();

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }

    private void backButton() {
        presenter.backButton();
        Button backButton = new Button(presenter.next());
        grid.add(backButton, 0, 11);
        backButton.setOnAction(actionEvent -> {
            new UserMenuGUI(stage, width, height, tradeModel, username).initialScreen();
        });
    }
}



