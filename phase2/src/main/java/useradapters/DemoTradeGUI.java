package useradapters;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class DemoTradeGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final int width;
    private final int height;
    private final TradeModel tradeModel;
    private final String username;
    private DatePicker datePicker;
    private TabPane root;
    private ObservableList<String> proposedTradesIDObservableList;
    private ObservableList<String> proposedTradesInfoObservableList;
    private ObservableList<String> confirmTradesIDObservableList;
    private ObservableList<String> confirmTradesInfoObservableList;
    private ObservableList<String> userOngoingTradeList;
    private ObservableList<String> userCompletedTradeList;

    public DemoTradeGUI(Stage stage, int width, int height, TradeModel tradeModel, String username) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.tradeModel = tradeModel;
        this.username = username;
        this.datePicker = new DatePicker();
        this.proposedTradesIDObservableList = FXCollections.observableArrayList();
        this.proposedTradesInfoObservableList = FXCollections.observableArrayList();
        this.confirmTradesIDObservableList = FXCollections.observableArrayList();
        this.confirmTradesInfoObservableList = FXCollections.observableArrayList();
        this.userOngoingTradeList = FXCollections.observableArrayList();
        this.userCompletedTradeList = FXCollections.observableArrayList();
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

    private void initializeScreen() {
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

    public void updateScreen() {
        initializeScreen();
    }

    // Main tab methods

    private Parent viewInitiateTrades() {
        Text title = new Text("Demo available items");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));

        Map<String, String> infoToId = new HashMap<String, String>();
        infoToId.put("ItemId: 5 \nName: Demo Yellow Car \nOwner: DemoUser123 \nDescription: One Yellow Car for the Demo", "item1");
        infoToId.put("ItemId: 4 \nName: Demo Green Car \nOwner: DemoUser456 \nDescription: One Green Car for the Demo", "item2");

        //Vbox
        VBox mainLayout = new VBox();
        mainLayout.setPrefWidth(300);
        mainLayout.setSpacing(10);
        mainLayout.setPadding(new Insets(25, 25, 25, 25));
        mainLayout.getChildren().add(title);
        mainLayout.setAlignment(Pos.CENTER);

        ListView<String> list = new ListView<>();
        ObservableList<String> availableItems = FXCollections.observableArrayList();
        availableItems.addAll(infoToId.keySet());
        list.setItems(availableItems);
        list.setPlaceholder(new Label("There are no available items for trading."));

        ScrollPane scrollPane = new ScrollPane();

        VBox itembuttons = new VBox();
        itembuttons.setSpacing(20);
        itembuttons.setAlignment(Pos.CENTER);

        AtomicReference<String> selected = new AtomicReference<>("");
        ToggleGroup item = new ToggleGroup();
        for (String info : availableItems) {
            RadioButton radioBtn = new RadioButton(info);
            radioBtn.setToggleGroup(item);
            itembuttons.getChildren().add(radioBtn);
            radioBtn.setOnAction(actionEvent -> selected.set(info));
        }
        scrollPane.setContent(itembuttons);
        mainLayout.getChildren().add(scrollPane);


        Label selectType = new Label("Select the type of trade you'd like to make.");
        Button oneWayTemporary = new Button("One way temporary");
        Button oneWayPermanent = new Button("One way permanent");
        Button twoWayTemporary = new Button("Two way temporary");
        Button twoWayPermanent = new Button("Two way permanent");

        VBox typeButtons = new VBox();
        typeButtons.getChildren().addAll(selectType, oneWayPermanent,oneWayTemporary, twoWayPermanent, twoWayTemporary);
        typeButtons.setAlignment(Pos.CENTER);
        typeButtons.setSpacing(10);

        Label messageBox = new Label();
        messageBox.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        messageBox.setAlignment(Pos.CENTER);

        oneWayTemporary.setOnAction(actionEvent -> initiateTradeMeetingInfo());
        oneWayPermanent.setOnAction(actionEvent -> initiateTradeMeetingInfo());

        twoWayTemporary.setOnAction(actionEvent -> selectItemTwoWayPage());
        twoWayPermanent.setOnAction(actionEvent -> selectItemTwoWayPage());

        mainLayout.getChildren().addAll(messageBox, typeButtons);

        return mainLayout;
    }

    private Parent viewProposedTrades() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        ListView<String> proposedTradesListView = new ListView<>();
        proposedTradesListView.setPlaceholder(new Label("Demo \nThis is where you would view the proposed trades"));
        proposedTradesListView.setItems(proposedTradesInfoObservableList);
        Button confirmBtn = new Button("Confirm");
        Button editBtn = new Button("Edit");
        Button declineBtn = new Button("Decline");
        Label placeHolderConfirm = new Label();
        Label placeHolderEdit = new Label();
        Label placeHolderDelete = new Label();

        grid.add(proposedTradesListView, 0, 0, 2, 1);
        grid.add(confirmBtn, 0, 1, 1, 1);
        grid.add(editBtn, 0, 2, 1, 1);
        grid.add(declineBtn, 0, 3, 1, 1);
        grid.add(placeHolderConfirm, 1, 1, 2, 1);
        grid.add(placeHolderEdit, 1, 2, 2, 1);
        grid.add(placeHolderDelete, 1, 3, 2, 1);

        Label messageBox = new Label();
        messageBox.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));

        confirmBtn.setOnAction(actionEvent -> {
            Label message = new Label("demo confirms trade");
            grid.add(message, 1, 1, 2, 1);
        });
        editBtn.setOnAction(actionEvent -> {
            Label message = new Label("demo edits trade");
            grid.add(message, 1, 2, 2, 1);
        });
        declineBtn.setOnAction(actionEvent -> {
            Label message = new Label("demo declines trade");
            grid.add(message, 1, 3, 2, 1);
        });

        grid.add(messageBox, 0, 4, 2, 1);

        return grid;
    }

    private Parent viewConfirmTrades() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        ListView<String> confirmTradesListView = new ListView<>();
        confirmTradesListView.setPlaceholder(new Label("Demo \nThis is where you would view the confirmed trades"));
        confirmTradesListView.setItems(confirmTradesInfoObservableList);
        Button confirmBtn = new Button("Confirm");
        Label placeHolderConfirm = new Label();

        grid.add(confirmTradesListView, 0, 0, 2, 1);
        grid.add(confirmBtn, 0, 1, 1, 1);
        grid.add(placeHolderConfirm, 1, 1, 2, 1);

        Label messageBox = new Label();
        messageBox.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));

        confirmBtn.setOnAction(actionEvent -> {
            Label message = new Label("demo confirms trade");
            grid.add(message, 1, 1, 2, 1);
        });

        grid.add(messageBox, 0, 2, 2, 1);
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
        Label ongoingTrades = new Label("This is where you would view your ongoing trades");
        Label completedTrades = new Label("This is where you would view your completed trades");

        Button addReview = new Button("Add Review");

        Label placeHolder = new Label();

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

        ListView<String> listOngoingTrades = new ListView<>();
        listOngoingTrades.setPlaceholder(new Label("This is a demo. \nYou have no ongoing trades"));
        listOngoingTrades.setPrefWidth(300);
        listOngoingTrades.setPrefHeight(200);

        ListView<String> listCompletedTrades = new ListView<>();
        listCompletedTrades.setPlaceholder(new Label("This is a demo. \nYou have no ongoing trades"));
        listCompletedTrades.setPrefWidth(300);
        listCompletedTrades.setPrefHeight(200);

        hboxOngoing.getChildren().addAll(listOngoingTrades);
        hboxCompleted.getChildren().addAll(listCompletedTrades);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(hBox, placeHolder, messageBox, ongoingTrades, hboxOngoing, completedTrades, hboxCompleted);

        Label message = new Label("demo adds review trade");

        addReview.setOnAction(e -> {
            if (!vbox.getChildren().contains(message)){
                vbox.getChildren().remove(placeHolder);
                vbox.getChildren().add(1, message);
            }
        });

        return vbox;
    }

    // Helper methods used by different tabs


    public void selectItemTwoWayPage(){
        Stage stage2 = new Stage();
        Text title = new Text("Select an item to exchange.");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Map<String, String> itemsToOffer = new HashMap<>();
        itemsToOffer.put("ItemId: 1 \nName: Demo Green Apple \nOwner: DemoUser789 \nDescription: One  Green Apple for the Demo", "item3");
        itemsToOffer.put("ItemId: 2 \nName: Demo Red Apple \nOwner: DemoUser234 \nDescription: One Red Apple for the Demo", "item2");

        //Vbox
        VBox mainLayout = new VBox();
        mainLayout.setPrefWidth(300);
        mainLayout.setSpacing(20);
        mainLayout.setPadding(new Insets(25, 25, 25, 25));
        mainLayout.setAlignment(Pos.CENTER);

        ListView<String> list = new ListView<>();
        ObservableList<String> availableItems = FXCollections.observableArrayList();
        availableItems.addAll(itemsToOffer.keySet());
        list.setItems(availableItems);

        Button confirmBtn = new Button("Confirm");

        Label messageText = new Label();
        messageText.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

        ScrollPane scrollPane = new ScrollPane();

        VBox itemBtns = new VBox();
        itemBtns.setSpacing(20);
        itemBtns.setAlignment(Pos.CENTER);

        AtomicReference<String> selected = new AtomicReference<>("");
        ToggleGroup item = new ToggleGroup();
        for (String info : availableItems) {
            RadioButton radioBtn = new RadioButton(info);
            radioBtn.setToggleGroup(item);
            itemBtns.getChildren().add(radioBtn);
            radioBtn.setOnAction(actionEvent -> selected.set(info));
        }
        scrollPane.setContent(itemBtns);

        Text meetingTitle = new Text("Meeting details");
        meetingTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(meetingTitle, 0, 0, 2, 1);

        //location
        Label tradeLocation = new Label("Suggest a location of real life meeting.");
        TextField userInputLocation = new TextField();

        //date and time
        Label tradeDate = new Label("Pick a date for the real life meeting.");
        DatePicker pickDate = new DatePicker();
        pickDate.setMaxWidth(150);
        Label timeInput = new Label("Input time as hh:mm");
        TextField userInputHour = new TextField();
        userInputHour.setMaxWidth(150);

        //confirm button
        Button confirmBtnReturnItem = new Button("Confirm");
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(actionEvent -> {
//            getRoot();
            stage2.close();});

        Label messageLabel = new Label();

        grid.add(tradeDate, 0, 6, 2, 1);
        grid.add(timeInput, 4, 6, 2, 1);
        grid.add(pickDate, 0, 7, 2, 1);
        grid.add(userInputHour, 4, 7, 2, 1);
        grid.add(tradeLocation, 0, 9, 2, 1);
        grid.add(userInputLocation, 0, 10, 2, 1);
        grid.add(messageLabel, 0,12,4,1);
        grid.add(confirmBtnReturnItem, 4, 13, 2, 1);

        confirmBtnReturnItem.setOnAction(actionEvent -> {
            if (userInputLocation.getText().isEmpty()| userInputHour.getText().isEmpty()|
                    !userInputHour.getText().matches("^([0-1][0-9]|[2][0-3]):([0-5][0-9])$") | pickDate.getValue() == null) {
                messageLabel.setText("You have an invalid input. Please check your input again.");
            } else {
                Label message = new Label("demo confirms trade");
                grid.getChildren().removeAll(meetingTitle, tradeDate, timeInput, pickDate, userInputHour, tradeLocation, userInputLocation, messageLabel, confirmBtn, confirmBtnReturnItem);
                grid.add(message,0 , 1, 2, 1);
                grid.add(exitBtn, 1, 3, 2, 1);
            }

        });

        confirmBtn.setOnAction(actionEvent -> {
            if (selected.toString().equals("")){
                messageText.setText("Choose an item to trade.");
            } else {
                mainLayout.getChildren().removeAll(title, scrollPane, messageText, confirmBtn);
                mainLayout.getChildren().add(grid);
            }
        });

        mainLayout.getChildren().addAll(title, scrollPane, messageText, confirmBtn);

        Scene scene2 = new Scene(mainLayout, width, height);
        stage2.setScene(scene2);
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.showAndWait();
    }

    private void initiateTradeMeetingInfo() {
        Stage stage2 = new Stage();
        Text meetingTitle = new Text("Meeting details");
        meetingTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(meetingTitle, 0, 0, 2, 1);

        //demo consideration
        Label demoConsideration = new Label("Considering you have sufficient credit to borrow and your first trade (a two-way trade) has already occurred");

        //location
        Label tradeLocation = new Label("Suggest a location of real life meeting.");
        TextField userInputLocation = new TextField();

        //date and time
        Label tradeDate = new Label("Pick a date for the real life meeting.");
        DatePicker pickDate = new DatePicker();
        pickDate.setMaxWidth(150);
        Label timeInput = new Label("Input time as hh:mm");
        TextField userInputHour = new TextField();
        userInputHour.setMaxWidth(150);

        //confirm button
        Button confirmBtn = new Button("Confirm");
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(actionEvent -> {
            stage2.close();
        });

        Label messageLabel = new Label();

        grid.add(demoConsideration, 0, 2, 3, 3);
        grid.add(tradeDate, 0, 7, 2, 1);
        grid.add(pickDate, 0, 8, 2, 1);

        grid.add(timeInput, 4, 7, 2, 1);
        grid.add(userInputHour, 4, 8, 2, 1);

        grid.add(tradeLocation, 0, 10, 2, 1);
        grid.add(userInputLocation, 0, 11, 2, 1);

        grid.add(messageLabel, 0,13,4,1);
        grid.add(confirmBtn, 4, 14, 2, 1);

        confirmBtn.setOnAction(actionEvent -> {
            if (userInputLocation.getText().isEmpty()| userInputHour.getText().isEmpty()|
                    !userInputHour.getText().matches("^([0-1][0-9]|[2][0-3]):([0-5][0-9])$") | pickDate.getValue() == null) {
                messageLabel.setText("You have an invalid input. Please check your input again.");
            } else {
                Label createdTrade = new Label("Demo trade initiated successfully! ");
                grid.getChildren().removeAll(meetingTitle, demoConsideration, tradeDate, timeInput, pickDate, userInputHour, tradeLocation, userInputLocation, messageLabel, confirmBtn);
                grid.add(createdTrade,0 , 1, 2, 1);
                grid.add(exitBtn, 1, 3, 2, 1);
            }
        });

        Scene scene = new Scene(grid, width, height);
        stage2.setScene(scene);
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.showAndWait();
    }




}
