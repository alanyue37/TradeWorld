package useradapters;

import adminadapters.AdminGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sun.java2d.cmm.Profile;
import tradeadapters.InitiateTradeController;
import tradeadapters.TradeGUI;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafx.collections.FXCollections.observableArrayList;

public class DemoGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final DemoController controller;
    private final UserPresenter2 presenter;
    private final int width;
    private final int height;
    protected String username;
    private TradeModel model;
    private InitiateTradeController initiateTradeController;
    private GridPane grid;
    private TabPane root;

    private RunnableGUI nextGUI;

    public DemoGUI(String username, Stage stage, int width, int height, TradeModel model) {
        // do we really need the username for a demo user
        // we need it to access controller and presenter
        this.stage = stage;
        controller = new DemoController(model, username);
        this.initiateTradeController = new InitiateTradeController(model, username);
        presenter = new UserPresenter2(model, username);
        this.width = width;
        this.height = height;
        this.username = username;
        this.model = model;
    }

    @Override
    public void initialScreen() {
        showScreen();
    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return grid;
    }

    @Override
    public void showScreen() {
        initializeScreen();
        scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    // frozen account

    public void initializeScreen(){
        root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        ProfileGUI profileGUI = new ProfileGUI(stage, 800, 800, this.model, username);
        Parent profileParent = profileGUI.getRoot();
        Tab profileTab = new Tab("My Profile", profileParent);

        ProfileGUI otherProfilesGUI = new ProfileGUI(stage, 800, 800, this.model, "u2");
        Parent otherProfilesParent = otherProfilesGUI.getRoot();
        Tab otherProfilesTab = new Tab("View Profiles", otherProfilesParent);

        DemoAddItemGUI inventoryGUI = new DemoAddItemGUI(stage, 800, 800, this.model, username);
        Parent inventoryParent = inventoryGUI.getRoot();
        Tab inventoryTab = new Tab("Inventory", inventoryParent);

        DemoAddWishlistGUI wishlistGUI = new DemoAddWishlistGUI(stage, 800, 800, this.model, username);
        Parent wishlistParent = wishlistGUI.getRoot();
        Tab wishlistTab = new Tab("Wishlist", wishlistParent);

        DemoTradeGUI tradeGUI = new DemoTradeGUI(stage, 800, 800, this.model, username);
        Parent tradeParent = tradeGUI.getRoot();
        Tab tradeTab = new Tab("Trade", tradeParent);

        root.getTabs().addAll(profileTab, inventoryTab, wishlistTab, tradeTab, otherProfilesTab);
//        root.getTabs().addAll(profileTab, inventoryTab, wishlistTab, otherProfilesTab);
    }



}

