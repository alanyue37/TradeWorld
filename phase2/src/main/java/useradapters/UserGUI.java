package useradapters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;

import java.io.IOException;
import java.util.List;

public class UserGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final UserController controller;
    private final UserPresenter presenter;
    private final int width;
    private final int height;
    protected String username;

    public UserGUI(String username, Stage stage, int width, int height, TradeModel model) {
        this.stage = stage;
        controller = new UserController(model, username);
        presenter = new UserPresenter();
        this.width = width;
        this.height = height;
        this.username = username;
    }

    @Override
    public void initialScreen(){
        stage.setTitle("Trading User - Options");

        Text title = new Text("Trading User");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        List<String> options = presenter.startMenu();
        // for loop this maybe ?
        Button addInventory = new Button(options.get(0));
        Button addWishlist = new Button(options.get(1));
        Button viewUserItems = new Button(options.get(2));
        Button viewTradingHistory = new Button(options.get(3));
        Button initiateTrades = new Button(options.get(4));
        Button manageProposedTrades = new Button(options.get(5));
        Button confirmTrades = new Button(options.get(6));
        Button manageAccount = new Button(options.get(7));

        addInventory.setOnAction(actionEvent -> createItem());
        addWishlist.setOnAction(actionEvent -> {
            try {
                viewItemsToAddToWishlist();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);
        grid.add(addInventory, 0, 1, 2, 1);
        grid.add(addWishlist, 0, 2, 2, 1);
        grid.add(viewUserItems, 0, 3, 2, 1);
        grid.add(viewTradingHistory, 0, 4, 2, 1);
        grid.add(initiateTrades, 0, 5, 2, 1);
        grid.add(manageProposedTrades, 0, 6, 2, 1);
        grid.add(confirmTrades, 0, 7, 2, 1);
        grid.add(manageAccount, 0, 8, 2, 1);

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    private void createItem(){
        Text title = new Text("Welcome");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);

        Label itemNameLabel = new Label(presenter.printInputItemName());
        TextField itemNameField = new TextField();
        Label itemDescriptionLabel = new Label(presenter.printInputItemDescription());
        TextField itemDescriptionField = new TextField();

        Button createItemButton = new Button("Create Item");
        HBox hBoxCreateItemButton;
        hBoxCreateItemButton = new HBox(10);
        hBoxCreateItemButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxCreateItemButton.getChildren().add(createItemButton);

        grid.add(itemNameLabel, 0, 1);
        grid.add(itemNameField, 1, 1);
        grid.add(itemDescriptionLabel, 0, 2);
        grid.add(itemDescriptionField, 1, 2);
        grid.add(hBoxCreateItemButton, 1, 4);

        createItemButton.setOnAction(actionEvent -> controller.createItem(username, itemNameField.getText(), itemDescriptionField.getText())
        );

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }

    private void viewItemsToAddToWishlist() throws IOException {
        Text title = new Text("Add items to Wishlist");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);

        ListView<String> list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList (controller.viewItemsToAddToWishlist());
        list.setItems(items);
        list.setPrefWidth(150);
        list.setPrefHeight(100);

        grid.add(list, 0, 1);

        Label itemIdLabel = new Label("Please enter the ID of the item you would like to add or \"back\" to go back: ");
        TextField itemIdField = new TextField();

        Button addItemButton = new Button("Add Item");
        HBox hBoxCreateItemButton;
        hBoxCreateItemButton = new HBox(10);
        hBoxCreateItemButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxCreateItemButton.getChildren().add(addItemButton);

        // grid.add(Add controller list of items);
        grid.add(itemIdLabel, 0, 2);
        grid.add(itemIdField, 1, 2);
        grid.add(hBoxCreateItemButton, 1, 4);

        addItemButton.setOnAction(actionEvent -> {
            if(controller.addItemsToWishlist(itemIdField.getText())) {
                itemAdded();
            }
        });

        scene = new Scene(grid, width, height);

        stage.setScene(scene);

    }

    private void itemAdded() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("Item was added successfully");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }
}



