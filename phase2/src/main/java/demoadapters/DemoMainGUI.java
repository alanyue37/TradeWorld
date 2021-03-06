package demoadapters;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import profileadapters.ProfileGUI;
import tradegateway.TradeModel;
import trademain.MainGUI;
import trademain.RunnableGUI;

public class DemoMainGUI extends MainGUI implements RunnableGUI {
    private TabPane root;

    /**
     * Creates an instance of DemoMainGUI -- the main screen for demo users.
     * @param width the width of the window
     * @param height the hieght of the window
     * @param model reference to TradeModel instance
     */
    public DemoMainGUI(int width, int height, TradeModel model) {
        super(width, height, model);
    }

    /**
     * Returns the parent node this GUI of which this GUI is composed
     * @return Parent node of all other nodes in this GUI
     */
    @Override
    public Parent getRoot() {
        initializeScreen();
        return root;
    }

    /**
     * Shows a new window for this GUI
     */
    @Override
    public void showScreen() {
        initializeScreen();
        Scene scene = new Scene(root, getWidth(), getHeight());
        getStage().setScene(scene);
        getStage().setTitle("TradeWorld - Demo");
        getStage().show();
    }

    private void initializeScreen(){
        root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

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

        root.getTabs().addAll(otherProfilesTab, inventoryTab, wishlistTab, tradeTab);
    }
}