package trademisc;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import tradeadapters.TradeGUI;
import tradegateway.TradeModel;
import useradapters.*;

public class UserMainGUI extends MainGUI implements RunnableGUI {

    private TabPane root;

//    TODO: remove username from constructor call?
    public UserMainGUI(int width, int height, TradeModel tradeModel) {
        super(width, height, tradeModel);
        this.root = new TabPane();
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
        Scene scene = new Scene(root, getWidth(), getHeight());
        getStage().setScene(scene);
        getStage().setTitle("TradeWorld");
        getStage().show();
    }

    private void initializeScreen() {
        //root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Update all the items that are no longer early access
        getTradeModel().getItemManager().updateEarlyItems();

        ProfileGUI profileGUI = new LoggedInProfileGUI(getStage(), 800, 800, getTradeModel(), true);
        Parent profileParent = profileGUI.getRoot();
        Tab profileTab = new Tab("My Profile", profileParent);

        AddItemGUI inventoryGUI = new AddItemGUI(getStage(), 800, 800, getTradeModel());
        Parent inventoryParent = inventoryGUI.getRoot();
        Tab inventoryTab = new Tab("Inventory", inventoryParent);

        AddWishlistGUI wishlistGUI = new AddWishlistGUI(getStage(), 800, 800, getTradeModel());
        Parent wishlistParent = wishlistGUI.getRoot();
        Tab wishlistTab = new Tab("Wishlist", wishlistParent);

        TradeGUI tradeGUI = new TradeGUI(getStage(), 800, 800, getTradeModel(), getTradeModel().getCurrentUser());
        Parent tradeParent = tradeGUI.getRoot();
        Tab tradeTab = new Tab("Trade", tradeParent);

        ProfileGUI otherProfilesGUI = new LoggedInProfileGUI(getStage(), 800, 800, getTradeModel(), false);
        Parent otherProfilesParent = otherProfilesGUI.getRoot();
        Tab otherProfilesTab = new Tab("View Profiles", otherProfilesParent);

        TradeHistoryGUI tradeHistoryGUI = new TradeHistoryGUI(getStage(), 800, 800, getTradeModel(), getTradeModel().getCurrentUser());
        Parent tradeHistoryParent = tradeHistoryGUI.getRoot();
        Tab tradeHistoryTab = new Tab("Trading History", tradeHistoryParent);

        root.getTabs().addAll(profileTab, inventoryTab, wishlistTab, tradeTab, otherProfilesTab, tradeHistoryTab);

        // Listener code below based on https://stackoverflow.com/questions/17522686/javafx-tabpane-how-to-listen-to-selection-changes
        root.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab) {
                        profileTab.setContent(profileGUI.getRoot());
                        inventoryTab.setContent(inventoryGUI.getRoot());
                        wishlistTab.setContent(wishlistGUI.getRoot());
                        tradeTab.setContent(tradeGUI.getRoot());
                        otherProfilesTab.setContent(otherProfilesGUI.getRoot());
                        tradeHistoryTab.setContent(tradeGUI.getRoot());
                    }
                }
        );

    }
}
