package trademisc;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import tradeadapters.TradeGUI;
import tradegateway.TradeModel;
import useradapters.AddItemGUI;
import useradapters.AddWishlistGUI;
import useradapters.LoggedInProfileGUI;
import useradapters.ProfileGUI;

public class UserMainGUI implements RunnableGUI {

    private final Stage stage;
    private final TradeModel tradeModel;
    private final int width;
    private final int height;
    private final String username;
    private TabPane root;

    public UserMainGUI(Stage stage, int width, int height, TradeModel tradeModel, String username) {
        this.stage = stage;
        this.tradeModel = tradeModel;
        this.width = width;
        this.height = height;
        this.username = username;
    }


    @Override
    public void initialScreen() {
        initializeScreen();
        showScreen();
    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return root;
    }

    @Override
    public void showScreen() {
        initializeScreen();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setTitle("TradeWorld");
        stage.show();
    }

    private void initializeScreen() {
        root = new TabPane();

        ProfileGUI profileGUI = new LoggedInProfileGUI(stage, 800, 800, this.tradeModel, username);
        Parent profileParent = profileGUI.getRoot();
        Tab profileTab = new Tab("My Profile", profileParent);

        ProfileGUI otherProfilesGUI = new LoggedInProfileGUI(stage, 800, 800, this.tradeModel, "u2");
        Parent otherProfilesParent = otherProfilesGUI.getRoot();
        Tab otherProfilesTab = new Tab("View Profiles", otherProfilesParent);

        AddItemGUI inventoryGUI = new AddItemGUI(stage, 800, 800, this.tradeModel, username);
        Parent inventoryParent = inventoryGUI.getRoot();
        Tab inventoryTab = new Tab("Inventory", inventoryParent);

        AddWishlistGUI wishlistGUI = new AddWishlistGUI(stage, 800, 800, this.tradeModel, username);
        Parent wishlistParent = wishlistGUI.getRoot();
        Tab wishlistTab = new Tab("Wishlist", wishlistParent);

        TradeGUI tradeGUI = new TradeGUI(stage, 800, 800, this.tradeModel, username);
        Parent tradeParent = tradeGUI.getRoot();
        Tab tradeTab = new Tab("Trade", tradeParent);


        // add tab
        root.getTabs().addAll(profileTab, inventoryTab, wishlistTab, tradeTab, otherProfilesTab);
    }
}
