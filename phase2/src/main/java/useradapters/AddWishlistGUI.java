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
import sun.java2d.cmm.Profile;
import tradeadapters.TradeGUI;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddWishlistGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final UserController2 controller;
    private final UserPresenter2 presenter;
    private final TradeModel tradeModel;
    private final int width;
    private final int height;
    protected String username;
    private final TableViewCreator creator;
    private RunnableGUI nextGUI;
    private final GridPane grid;

    public AddWishlistGUI(Stage stage, int width, int height, TradeModel model, String username) {
        this.stage = stage;
        controller = new UserController2(model, username);
        presenter = new UserPresenter2(model, username);
        tradeModel = model;
        this.width = width;
        this.height = height;
        this.username = username;
        creator = new TableViewCreator(presenter);
        grid = new GridPane();
    }

    @Override
    public void initialScreen(){

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

        backButton();

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }

    private void backButton() {
        Button backButton = new Button("Back");
        grid.add(backButton, 0, 11);
        backButton.setOnAction(actionEvent -> {
            new UserGUI(stage, width, height, tradeModel, username).initialScreen();
        });
    }
}
