package trademain;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import profileadapters.LoggedInProfileGUI;
import profileadapters.ProfileGUI;
import tradeadapters.TradeGUI;
import tradegateway.GUIObserver;
import tradegateway.TradeModel;
import useradapters.AddItemGUI;
import useradapters.AddWishlistGUI;
import useradapters.TradeHistoryGUI;

import java.util.HashMap;

public class UserMainGUI extends MainGUI implements RunnableGUI, GUIObserver {

    private final HashMap<Tab, RunnableGUI> tabToGUI;
    private final TabPane root;

    public UserMainGUI(int width, int height, TradeModel tradeModel) {
        super(width, height, tradeModel);
        this.root = new TabPane();
        tabToGUI = new HashMap<>();
    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return root;
    }

    @Override
    public void showScreen() {
        initializeScreen();
        Scene scene = new Scene(root, getWidth(), getHeight());
        getStage().setScene(scene);
        getStage().setTitle("TradeWorld");
        getStage().show();
    }

    private void initializeScreen() {
        //root = new TabPane();
        getTradeModel().addObserver(this);
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Update all the items that are no longer early access
        getTradeModel().getItemManager().updateEarlyItems();

        ProfileGUI profileGUI = new LoggedInProfileGUI(getStage(), 800, 800, getTradeModel(), true);
        Parent profileParent = profileGUI.getRoot();
        Tab profileTab = new Tab("My Profile", profileParent);
        tabToGUI.put(profileTab, profileGUI);

        AddItemGUI inventoryGUI = new AddItemGUI(getStage(), 800, 800, getTradeModel());
        Parent inventoryParent = inventoryGUI.getRoot();
        Tab inventoryTab = new Tab("Inventory", inventoryParent);
        tabToGUI.put(inventoryTab, inventoryGUI);

        AddWishlistGUI wishlistGUI = new AddWishlistGUI(getStage(), 800, 800, getTradeModel());
        Parent wishlistParent = wishlistGUI.getRoot();
        Tab wishlistTab = new Tab("Wishlist", wishlistParent);
        tabToGUI.put(wishlistTab, wishlistGUI);

        TradeGUI tradeGUI = new TradeGUI(getStage(), 800, 800, getTradeModel());
        Parent tradeParent = tradeGUI.getRoot();
        Tab tradeTab = new Tab("Trade", tradeParent);
        tabToGUI.put(tradeTab, tradeGUI);

        TradeHistoryGUI tradeHistoryGUI = new TradeHistoryGUI(getStage(), 800, 800, getTradeModel(), getTradeModel().getCurrentUser());
        Parent tradeHistoryParent = tradeHistoryGUI.getRoot();
        Tab tradeHistoryTab = new Tab("Trading History", tradeHistoryParent);
        tabToGUI.put(tradeHistoryTab, tradeHistoryGUI);

        ProfileGUI otherProfilesGUI = new LoggedInProfileGUI(getStage(), 800, 800, getTradeModel(), false);
        Parent otherProfilesParent = otherProfilesGUI.getRoot();
        Tab otherProfilesTab = new Tab("View Profiles", otherProfilesParent);
        tabToGUI.put(otherProfilesTab, otherProfilesGUI);

        root.getTabs().addAll(profileTab, inventoryTab, wishlistTab, tradeTab, tradeHistoryTab, otherProfilesTab);

    }

    @Override
    public void update() {
        for (Tab t: tabToGUI.keySet()) {
            t.setContent(tabToGUI.get(t).getRoot());
        }
    }
}
