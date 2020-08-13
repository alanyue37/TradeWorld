package useradapters;


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

import tradegateway.TradeModel;
import trademisc.RunnableGUI;


public class DemoTradeGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final int width;
    private final int height;
    private final TradeModel tradeModel;
    private GridPane grid;
    private TabPane root;

    public DemoTradeGUI(Stage stage, int width, int height, TradeModel tradeModel, String username) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.tradeModel = tradeModel;
//        this.username = tradeModel.getCurrentUser();
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
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    private void initializeScreen() {
//        Text title = new Text("What would you like to do?");
//        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

//        Button initiateTradeBtn = new Button("Initiate trade");
//        Button proposedTradesBtn = new Button("Manage proposed trades");
//        Button confirmTradesBtn = new Button("Confirm real life meeting of trades");
//        Button viewTradesBtn = new Button("View ongoing and completed trades");
//        Button backButtonBtn = new Button("Go back");
//
//        initiateTradeBtn.setMaxWidth(500);
//        proposedTradesBtn.setMaxWidth(500);
//        confirmTradesBtn.setMaxWidth(500);
//        viewTradesBtn.setMaxWidth(500);
//        backButtonBtn.setMaxWidth(500);
//
//        grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(25, 25, 25, 25));
//
//        grid.add(title, 0, 0, 2, 1);
//        grid.add(initiateTradeBtn, 0, 1, 2, 1);
//        grid.add(proposedTradesBtn, 0, 2, 2, 1);
//        grid.add(confirmTradesBtn, 0, 3, 2, 1);
//        grid.add(viewTradesBtn, 0, 4, 2, 1);
//        grid.add(backButtonBtn, 0, 5, 2, 1);
//
//        backButtonBtn.setOnAction(actionEvent -> new UserMainGUI(800, 800, tradeModel, tradeModel.getCurrentUser()));

        root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Parent initiateParent = viewInitiateTrades();
        Tab initiateTab = new Tab("Initiate", initiateParent);

        Parent proposedParent = viewProposedTrades();
        Tab proposedTab = new Tab("Proposed Trades", proposedParent);

        Parent confirmParent = viewConfirmTrades();
        Tab confirmTab = new Tab("Confirm Trades", confirmParent);

        Parent viewParent = viewAllTrades();
        Tab viewTab = new Tab("View Trades", viewParent);

        root.getTabs().addAll(initiateTab, proposedTab, confirmTab, viewTab);

    }

    private Parent viewInitiateTrades() {
        Text title = new Text("Available items");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));

        //Vbox
        VBox mainLayout = new VBox();
        mainLayout.setPrefWidth(300);
        mainLayout.setSpacing(10);
        mainLayout.setPadding(new Insets(25, 25, 25, 25));
        mainLayout.getChildren().add(title);
        mainLayout.setAlignment(Pos.CENTER);

        ListView<String> list = new ListView<>();
        list.setPlaceholder(new Label("There are no available items for trading."));

        Label selectType = new Label("Select the type of trade you'd like to make.");
        Button oneWayTemporary = new Button("One way temporary");
        Button oneWayPermanent = new Button("One way permanent");
        Button twoWayTemporary = new Button("Two way temporary");
        Button twoWayPermanent = new Button("Two way permanent");

        oneWayPermanent.setDisable(true);
        oneWayTemporary.setDisable(true);
        twoWayPermanent.setDisable(true);
        twoWayTemporary.setDisable(true);

        VBox typeButtons = new VBox();
        typeButtons.getChildren().addAll(selectType, oneWayPermanent,oneWayTemporary, twoWayPermanent, twoWayTemporary);
        typeButtons.setAlignment(Pos.CENTER);
        typeButtons.setSpacing(10);

        mainLayout.getChildren().addAll(list, typeButtons);

        return mainLayout;
    }

    private Parent viewProposedTrades() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        ListView<String> proposedTradesListView = new ListView<>();
        proposedTradesListView.setPlaceholder(new Label("No proposed trades"));
        Button confirmBtn = new Button("Confirm");
        Button editBtn = new Button("Edit");
        Button declineBtn = new Button("Decline");
        confirmBtn.setDisable(true);
        editBtn.setDisable(true);
        declineBtn.setDisable(true);

        grid.add(proposedTradesListView, 0, 0, 2, 1);
        grid.add(confirmBtn, 0, 1, 2, 1);
        grid.add(editBtn, 0, 2, 2, 1);
        grid.add(declineBtn, 0, 3, 2, 1);

        return grid;
    }

    private Parent viewConfirmTrades() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        ListView<String> confirmTradesListView = new ListView<>();
        confirmTradesListView.setPlaceholder(new Label("No trades to confirm"));
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setDisable(true);

        grid.add(confirmTradesListView, 0, 0, 2, 1);
        grid.add(confirmBtn, 0, 1, 2, 1);

        return grid;
    }

    public Parent viewAllTrades() {
        Label addReviewLabel = new Label("Enter your review.");

        TextField commentInput = new TextField();
        commentInput.setPromptText("Leave a comment");
        TextField ratingInput = new TextField();
        ratingInput.setPromptText("Rating out of 5 (1-5)");
        TextField tradeIdInput = new TextField();
        tradeIdInput.setPromptText("Trade ID");

        Label messageBox = new Label();
        messageBox.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        Label ongoingTrades = new Label("Your ongoing trades");
        Label completedTrades = new Label("Your completed trades");

        Button addReview = new Button("Add Review");
        addReview.setDisable(true);


        HBox hBox = new HBox();
        hBox.setPadding(new Insets(25, 25, 25, 25));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(addReviewLabel, tradeIdInput, ratingInput, commentInput, addReview);

        HBox hboxOngoing = new HBox();
        HBox hboxCompleted = new HBox();
        hboxOngoing.setPadding(new Insets(25, 25, 25, 25));
        hboxOngoing.setSpacing(10);
        hboxCompleted.setPadding(new Insets(25, 25, 25, 25));
        hboxCompleted.setSpacing(10);
        hboxOngoing.getChildren().add(new Label("No ongoing trades."));
        hboxCompleted.getChildren().add(new Label("No completed trades."));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(hBox, messageBox, ongoingTrades, hboxOngoing, completedTrades, hboxCompleted);

        return vbox;
    }

}
