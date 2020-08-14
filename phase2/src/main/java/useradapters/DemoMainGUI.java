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
    private final Stage stage;
    private Scene scene;
    // private final DemoController controller;
    private final int width;
    private final int height;
    protected String username;
    private TradeModel model;
    // private InitiateTradeController initiateTradeController;
    private GridPane grid;
    private TabPane root;

    private RunnableGUI nextGUI;

    public DemoMainGUI(int width, int height, TradeModel model) {
        super(width, height, model);
        // do we really need the username for a demo user
        // we need it to access controller and presenter
        // controller = new DemoController(model, username);
        // this.initiateTradeController = new InitiateTradeController(model, username);
        this.width = width;
        this.height = height;
        this.username = "username"; // TODO: get rid of username
        this.model = model;
        this.stage = new Stage();
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
        scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    // frozen account

    public void initializeScreen(){
        root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        ProfileGUI profileGUI = new ProfileGUI(stage, 800, 800, this.model, true);
        Parent profileParent = profileGUI.getRoot();
        Tab profileTab = new Tab("My Profile", profileParent);

        ProfileGUI otherProfilesGUI = new ProfileGUI(stage, 800, 800, this.model, false);
        Parent otherProfilesParent = otherProfilesGUI.getRoot();
        Tab otherProfilesTab = new Tab("View Profiles", otherProfilesParent);

        DemoAddItemGUI inventoryGUI = new DemoAddItemGUI(stage, 800, 800, this.model);
        Parent inventoryParent = inventoryGUI.getRoot();
        Tab inventoryTab = new Tab("Inventory", inventoryParent);

        DemoAddWishlistGUI wishlistGUI = new DemoAddWishlistGUI(stage, 800, 800, this.model);
        Parent wishlistParent = wishlistGUI.getRoot();
        Tab wishlistTab = new Tab("Wishlist", wishlistParent);

        DemoTradeGUI tradeGUI = new DemoTradeGUI(stage, 800, 800, this.model, username);
        Parent tradeParent = tradeGUI.getRoot();
        Tab tradeTab = new Tab("Trade", tradeParent);

        root.getTabs().addAll(profileTab, inventoryTab, wishlistTab, tradeTab, otherProfilesTab);
//        root.getTabs().addAll(profileTab, inventoryTab, wishlistTab, otherProfilesTab);
    }



}

