package useradapters;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import trademisc.MainGUI;
import trademisc.RunnableGUI;

import static javafx.collections.FXCollections.observableArrayList;

public class DemoMainGUI extends MainGUI implements RunnableGUI {
    private Scene scene;
    private TradeModel model;
    private GridPane grid;
    private TabPane root;

    private RunnableGUI nextGUI;

    public DemoMainGUI(int width, int height, TradeModel model) {
        super(width, height, model);
    }

    @Override
    public void initialScreen() {

    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return root;
    }

    @Override
    public void showScreen() {
        initializeScreen();
        scene = new Scene(root, getWidth(), getHeight());
        getStage().setScene(scene);
        getStage().show();
    }

    // frozen account

    public void initializeScreen(){
        root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        // Should we create a demo user account?
        getTradeModel().getUserManager().createTradingUser("Demo User", "DemoUser", "demo", "Montreal");
        getTradeModel().setCurrentUser("DemoUser");

        ProfileGUI profileGUI = new ProfileGUI(getStage(), 800, 800, getTradeModel(), true);
        Parent profileParent = profileGUI.getRoot();
        Tab profileTab = new Tab("My Profile", profileParent);

        ProfileGUI otherProfilesGUI = new ProfileGUI(getStage(), 800, 800, getTradeModel(), false);
        Parent otherProfilesParent = otherProfilesGUI.getRoot();
        Tab otherProfilesTab = new Tab("View Profiles", otherProfilesParent);

        DemoAddItemGUI inventoryGUI = new DemoAddItemGUI(getStage(), 800, 800, getTradeModel());
        Parent inventoryParent = inventoryGUI.getRoot();
        Tab inventoryTab = new Tab("Inventory", inventoryParent);

        DemoAddWishlistGUI wishlistGUI = new DemoAddWishlistGUI(getStage(), 800, 800, getTradeModel());
        Parent wishlistParent = wishlistGUI.getRoot();
        Tab wishlistTab = new Tab("Wishlist", wishlistParent);

        DemoTradeGUI tradeGUI = new DemoTradeGUI(getStage(), 800, 800, getTradeModel());
        Parent tradeParent = tradeGUI.getRoot();
        Tab tradeTab = new Tab("Trade", tradeParent);

        root.getTabs().addAll(profileTab, inventoryTab, wishlistTab, tradeTab, otherProfilesTab);
    }



}

