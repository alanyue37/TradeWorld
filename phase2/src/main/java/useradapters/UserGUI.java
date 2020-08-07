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

import java.util.List;

public class UserGUI{
    private Stage stage;
    private Scene scene;
    private final UserController controller;
    private int width;
    private int height;
    protected String username;

    public UserGUI(String username, Stage stage, int width, int height, UserController controller) {
        this.stage = stage;
        this.controller = controller;
        this.width = width;
        this.height = height;
        this.username = username;
    }

    public void tradingUserInitialScreen(){
        stage.setTitle("Trading User - Options");

        Text title = new Text("Trading User");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Button addInventory = new Button("Add items to inventory");
        Button addWishlist = new Button("Add items to wishlist");
        Button viewUserItems = new Button("View inventory or wishlist");
        Button viewTradingHistory = new Button("View trading history");
        Button initiateTrades = new Button("Initiate trades");
        Button manageProposedTrades = new Button("Manage proposed trades");
        Button confirmTrades = new Button("Confirm trades");
        Button manageAccount = new Button("Manage/view account settings");

        addInventory.setOnAction(actionEvent -> createItem());
        addWishlist.setOnAction(actionEvent -> viewItemsToAddToWishlist());

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

        Label itemNameLabel = new Label("Enter item's name: ");
        TextField itemNameField = new TextField();
        Label itemDescriptionLabel = new Label("Enter item's description: ");
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

    private void viewItemsToAddToWishlist(){
        Text title = new Text("Welcome");
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

//        List<String> itemsToAddToWishlist = controller.viewItemsToAddToWishlist();
//        ScrollPane itemsScrollPane = new ScrollPane();
//        itemsScrollPane.setContent((Node) itemsToAddToWishlist);

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
                System.out.println("Success");
            }
        });

        scene = new Scene(grid, width, height);

        stage.setScene(scene);

    }
}



