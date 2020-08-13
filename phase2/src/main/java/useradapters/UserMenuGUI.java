package useradapters;

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
import tradeadapters.TradeGUI;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;

import java.util.ArrayList;
import java.util.List;

public class UserMenuGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final UserController2 controller;
    private final UserPresenter2 presenter;
    private final TradeModel tradeModel;
    private final int width;
    private final int height;
    private String username;
    private RunnableGUI nextGUI;
    private GridPane grid;

    public UserMenuGUI(Stage stage, int width, int height, TradeModel model, String username) {
        this.stage = stage;
        controller = new UserController2(model, username);
        presenter = new UserPresenter2(model, username);
        tradeModel = model;
        this.width = width;
        this.height = height;
        this.username = username;
    }

    @Override
    public void initialScreen(){

    }

    @Override
    public void showScreen() {
        initializeScreen();
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return grid;
    }

    private void initializeScreen() {
        presenter.menuScreen();
        stage.setTitle(presenter.next());

        Text title = new Text(presenter.next());
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        presenter.startMenu();
        List<Button> buttons = new ArrayList<>();
        while (presenter.hasNext()) {
            buttons.add(new Button(presenter.next()));
        }
//        Button addInventory = new Button(options.get(0));
//        Button addWishlist = new Button(options.get(1));
//        Button manageTrades = new Button(options.get(2));
//        Button manageAccount = new Button(options.get(3));

        buttons.get(0).setOnAction(actionEvent -> {
            nextGUI = new AddItemGUI(stage, width, height, tradeModel, username);
            nextGUI.initialScreen();
        });
        buttons.get(1).setOnAction(actionEvent -> {
            nextGUI = new AddWishlistGUI(stage, width, height, tradeModel, username);
            nextGUI.initialScreen();
        });
        buttons.get(2).setOnAction(actionEvent -> {
            nextGUI = new TradeGUI(stage, width, height, tradeModel, username);
            nextGUI.initialScreen();
        });
        buttons.get(3).setOnAction(actionEvent -> {
            nextGUI = new ProfileGUI(stage, 800, 800, tradeModel, true);
            nextGUI.initialScreen();
        });

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);
        for (int i = 0; i < buttons.size(); i++) {
            grid.add(buttons.get(i), 0, i+1, 2, 1);
        }

        frozenAlert();
    }

    private void frozenAlert() {
        if (controller.isFrozen()) {
            presenter.frozenAlert();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(presenter.next());
            alert.setHeaderText(presenter.next());
            alert.setContentText(presenter.next());

            alert.showAndWait();
        }
    }

//    private void itemAdded() {
//        GridPane grid = (GridPane) scene.getRoot();
//        Text message = new Text("Item was added successfully");
//        if (!grid.getChildren().contains(message)) {
//            grid.add(message, 0, 6, 2, 1);
//        }
//    }

//    private void backButton() {
//        GridPane grid = (GridPane) scene.getRoot();
//        Button backButton = new Button("Back");
//        grid.add(backButton, 0, 11);
//        backButton.setOnAction(actionEvent -> {
//            initialScreen();
//        });
//    }
}



