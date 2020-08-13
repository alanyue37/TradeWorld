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

public class DemoAddWishlistGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final UserController controller;
    private final TradeModel tradeModel;
    private final int width;
    private final int height;
    private String username;
    private final TableViewCreator creator;
    private GridPane grid;

    public DemoAddWishlistGUI(Stage stage, int width, int height, TradeModel model, String username) {
        this.stage = stage;
        controller = new UserController(model);
        tradeModel = model;
        this.width = width;
        this.height = height;
        this.username = username;
        creator = new TableViewCreator(tradeModel);
    }

    @Override
    public void initialScreen(){
        initializeScreen();
        showScreen();
    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return grid;
    }

    @Override
    public void showScreen() {
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
    }

    private void initializeScreen() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        TableView<ObservableList<String>> itemTable = creator.create("all items");
        TableView<ObservableList<String>> wishlistTable = creator.create("wishlist");

        Text title1 = new Text("All available items");
        title1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title1, 0, 0, 2, 1);

        Text title2 = new Text("Your wishlist");
        title2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title2, 6, 0, 2, 1);

        grid.add(itemTable, 0, 1, 5, 5);
        grid.add(wishlistTable, 6, 1, 5, 5);

        Button addButton = new Button("Add item");
        grid.add(addButton, 0, 6, 2, 1);
        Button removeButton = new Button("Remove item");
        grid.add(removeButton, 6, 6, 2, 1);

        itemTable.setPlaceholder(new Label("No items to display"));
        wishlistTable.setPlaceholder(new Label("No items in wishlist"));

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
    }
}