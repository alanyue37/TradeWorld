package useradapters;

import javafx.beans.property.ReadOnlyObjectWrapper;
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
import java.util.ArrayList;
import java.util.List;

public class UserGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final UserController2 controller;
    private final UserPresenter2 presenter;
    private final int width;
    private final int height;
    protected String username;
    private final TableViewCreator creator;

    public UserGUI(String username, Stage stage, int width, int height, TradeModel model) {
        this.stage = stage;
        controller = new UserController2(model, username);
        presenter = new UserPresenter2(model, username);
        this.width = width;
        this.height = height;
        this.username = username;
        creator = new TableViewCreator(presenter);
    }

    @Override
    public void initialScreen(){
        System.out.println(username);
        stage.setTitle("Trading User - Options");

        Text title = new Text("Welcome " + presenter.getName());
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        List<String> options = presenter.startMenu();
        List<Button> buttons = new ArrayList<Button>();
        for (String op : options) {
            buttons.add(new Button(op));
        }
//        Button addInventory = new Button(options.get(0));
//        Button addWishlist = new Button(options.get(1));
//        Button viewUserItems = new Button(options.get(2));
//        Button viewTradingHistory = new Button(options.get(3));
//        Button initiateTrades = new Button(options.get(4));
//        Button manageProposedTrades = new Button(options.get(5));
//        Button confirmTrades = new Button(options.get(6));
//        Button manageAccount = new Button(options.get(7));

        buttons.get(0).setOnAction(actionEvent -> {
            try {
                viewInventoryAddItem();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        buttons.get(1).setOnAction(actionEvent -> {
            try {
                viewAllItemsAndWishlist();
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
        for (int i = 0; i < buttons.size(); i++) {
            grid.add(buttons.get(i), 0, i+1, 2, 1);
        }

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();

        if (controller.isFrozen()) {
            frozenAlert();
        }
    }

    private void frozenAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Your account is frozen.");
        alert.setContentText("You are currently unable to make trades.");

        alert.showAndWait();
    }

    private void viewInventoryAddItem() throws IOException {
        Text title = new Text("Current Inventory");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);

        TableView<ObservableList<String>> table = creator.create("own inventory");

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

        createItemButton.setOnAction(actionEvent -> {
            controller.createItem(username, itemNameField.getText(), itemDescriptionField.getText());
            itemNameField.clear();
            itemDescriptionField.clear();
            Text message = new Text("Item added!");
            grid.add(message, 0, 10, 2, 1);
        });

        Button backButton = new Button("Back");
        grid.add(backButton, 0, 11);
        backButton.setOnAction(actionEvent -> {
            initialScreen();
        });

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }

    private void viewAllItemsAndWishlist() throws IOException {

        GridPane grid = new GridPane();
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

        TableView<ObservableList<String>> itemTable = creator.create("all items");
        TableView<ObservableList<String>> wishlistTable = creator.create("wishlist");

        itemTable.setPlaceholder(new Label("No items available"));
        wishlistTable.setPlaceholder(new Label("No items in wishlist"));

        grid.add(itemTable, 0, 1, 5, 5);
        grid.add(wishlistTable, 6, 1, 5, 5);

        Button addButton = new Button("Add item");
        grid.add(addButton, 0, 6, 2, 1);
        Button removeButton = new Button("Remove item");
        grid.add(removeButton, 6, 6, 2, 1);

        TableView.TableViewSelectionModel<ObservableList<String>> itemSelection = itemTable.getSelectionModel();
        TableView.TableViewSelectionModel<ObservableList<String>> wishlistSelection = wishlistTable.getSelectionModel();

        addButton.setOnAction(actionEvent -> {
            ObservableList<ObservableList<String>> selectedItems = itemSelection.getSelectedItems();
            System.out.println(selectedItems.get(0).get(1));
            if (controller.addItemToWishlist(selectedItems.get(0).get(0))) { // new item added to wishlist
                wishlistTable.getItems().add(selectedItems.get(0));
            }
        });

        removeButton.setOnAction(actionEvent -> {
            ObservableList<ObservableList<String>> selectedItems = wishlistSelection.getSelectedItems();
            System.out.println(selectedItems.get(0).get(1));
            controller.removeItemFromWishlist(selectedItems.get(0).get(0));
            wishlistTable.getItems().remove(selectedItems.get(0));
        });

//        Text prompt = new Text("Add item to inventory");
//        prompt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//
//        Label itemNameLabel = new Label("Item name");
//        TextField itemNameField = new TextField();
//        Label itemDescriptionLabel = new Label("Item description");
//        TextArea itemDescriptionField = new TextArea();
//
//        Button createItemButton = new Button("Create Item");
//
//        grid.add(itemNameLabel, 0, 7);
//        grid.add(itemNameField, 1, 7);
//        grid.add(itemDescriptionLabel, 0, 8);
//        grid.add(itemDescriptionField, 1, 8);
//        grid.add(createItemButton, 1, 9);
//
//        createItemButton.setOnAction(actionEvent -> {
//            controller.createItem(username, itemNameField.getText(), itemDescriptionField.getText());
//            itemNameField.clear();
//            itemDescriptionField.clear();
//            Text message = new Text("Item added!");
//            grid.add(message, 0, 10, 2, 1);
//        });

        Button backButton = new Button("Back");
        grid.add(backButton, 0, 11);
        backButton.setOnAction(actionEvent -> {
            initialScreen();
        });

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }


//    private void createItem(){
//        Text title = new Text("Add item to inventory");
//        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//
//        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(25, 25, 25, 25));
//
//        grid.add(title, 0, 0, 2, 1);
//
//        Label itemNameLabel = new Label(presenter.printInputItemName());
//        TextField itemNameField = new TextField();
//        Label itemDescriptionLabel = new Label(presenter.printInputItemDescription());
//        TextArea itemDescriptionField = new TextArea();
//
//        Button createItemButton = new Button("Create Item");
//        HBox hBoxCreateItemButton = new HBox(10);
//        hBoxCreateItemButton.setAlignment(Pos.BOTTOM_RIGHT);
//        hBoxCreateItemButton.getChildren().add(createItemButton);
//
//        grid.add(itemNameLabel, 0, 1);
//        grid.add(itemNameField, 1, 1);
//        grid.add(itemDescriptionLabel, 0, 2);
//        grid.add(itemDescriptionField, 1, 2);
//        grid.add(hBoxCreateItemButton, 1, 4);
//
//        createItemButton.setOnAction(actionEvent -> controller.createItem(username, itemNameField.getText(), itemDescriptionField.getText())
//        );
//
//        scene = new Scene(grid, width, height);
//        stage.setScene(scene);
//    }

//    private void viewItemsToAddToWishlist() throws IOException {
//        Text title = new Text("Add items to Wishlist");
//        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//
//        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(25, 25, 25, 25));
//
//        grid.add(title, 0, 0, 2, 1);
//
//        ListView<String> list = new ListView<>();
//        ObservableList<String> items = FXCollections.observableArrayList (controller.viewItemsToAddToWishlist());
//        list.setItems(items);
//        list.setPrefWidth(150);
//        list.setPrefHeight(100);
//
//        grid.add(list, 0, 1);
//
//        Label itemIdLabel = new Label("Please enter the ID of the item you would like to add or \"back\" to go back: ");
//        TextField itemIdField = new TextField();
//
//        Button addItemButton = new Button("Add Item");
//        HBox hBoxCreateItemButton;
//        hBoxCreateItemButton = new HBox(10);
//        hBoxCreateItemButton.setAlignment(Pos.BOTTOM_RIGHT);
//        hBoxCreateItemButton.getChildren().add(addItemButton);
//
//        // grid.add(Add controller list of items);
//        grid.add(itemIdLabel, 0, 2);
//        grid.add(itemIdField, 1, 2);
//        grid.add(hBoxCreateItemButton, 1, 4);
//
//        addItemButton.setOnAction(actionEvent -> {
//            if(controller.addItemsToWishlist(itemIdField.getText())) {
//                itemAdded();
//            }
//        });
//
//        scene = new Scene(grid, width, height);
//
//        stage.setScene(scene);
//
//    }

    private void itemAdded() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("Item was added successfully");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }
}



