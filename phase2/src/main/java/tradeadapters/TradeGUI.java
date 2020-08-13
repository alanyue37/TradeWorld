package tradeadapters;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import org.json.JSONException;
import org.json.JSONObject;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;
import useradapters.ProfileController;
import viewingadapters.ViewingTradesController;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class TradeGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final int width;
    private final int height;
    private final TradeModel tradeModel;
    private final InitiateTradeController initiateTradeController;
    private final ConfirmTradesController confirmTradesController;
    private final ProposedTradesController proposedTradesController;
    private final ViewingTradesController viewingTradesController;
    private final ProfileController profileController;
    private final String username;
    private DatePicker datePicker;
    private TabPane root;
    private ObservableList<String> proposedTradesIDObservableList;
    private ObservableList<String> proposedTradesInfoObservableList;
    private ObservableList<String> confirmTradesIDObservableList;
    private ObservableList<String> confirmTradesInfoObservableList;
    private ObservableList<String> userOngoingTradeList;
    private ObservableList<String> userCompletedTradeList;

    public TradeGUI(Stage stage, int width, int height, TradeModel tradeModel, String username) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.tradeModel = tradeModel;
//        this.username = tradeModel.getCurrentUser();
        this.username = username;
        this.initiateTradeController = new InitiateTradeController(tradeModel, username);
        this.confirmTradesController = new ConfirmTradesController(tradeModel, username);
        this.proposedTradesController = new ProposedTradesController(tradeModel, username);
        this.viewingTradesController = new ViewingTradesController(tradeModel, username);
        this.profileController = new ProfileController(tradeModel, username);
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
        updateProposedObservableLists();
        root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Parent initiateParent = viewInitiateTrades();
        Tab initiateTab = new Tab("Initiate", initiateParent);

        Parent proposedParent = viewProposedTrades();
        Tab proposedTab = new Tab("Proposed Trades", proposedParent);

        Parent confirmParent = viewConfirmTrades(username);
        Tab confirmTab = new Tab("Confirm Trades", confirmParent);

        Parent viewParent = viewAllTrades();
        Tab viewTab = new Tab("View Trades", viewParent);

        root.getTabs().addAll(initiateTab, proposedTab, confirmTab, viewTab);

        // Listener code below based on https://stackoverflow.com/questions/17522686/javafx-tabpane-how-to-listen-to-selection-changes
        root.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab) {
                        updateScreen();
                    }
                }
        );
    }

    public void updateScreen() {
        initializeScreen();
    }

    // Main tab methods

    private Parent viewInitiateTrades() {
        Text title = new Text("Available items");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));

        Map<String, String> infoToId = initiateTradeController.getAvailableItems();

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

        if (!initiateTradeController.canTrade()){
            oneWayPermanent.setDisable(true);
            oneWayTemporary.setDisable(true);
            twoWayPermanent.setDisable(true);
            twoWayTemporary.setDisable(true);
            Label cannotTrade = new Label("You cannot initiate a trade. Check your account status");
            mainLayout.getChildren().add(cannotTrade);
        }

        VBox typeButtons = new VBox();
        typeButtons.getChildren().addAll(selectType, oneWayPermanent,oneWayTemporary, twoWayPermanent, twoWayTemporary);
        typeButtons.setAlignment(Pos.CENTER);
        typeButtons.setSpacing(10);

        Label messageBox = new Label();
        messageBox.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        messageBox.setAlignment(Pos.CENTER);

        oneWayTemporary.setOnAction(actionEvent ->{
                    if (selected.toString().equals("")){
                        messageBox.setText("Choose an item to trade.");
                    } else if (initiateTradeController.isNewAccount(username)){
                        messageBox.setText("Your first trade must be Two-Way or Lending.");
                    } else if (!initiateTradeController.canOneWay(username)){
                        messageBox.setText("You have insufficient credit to borrow. You may not initiate a one-way " +
                                "trade until you have loaned enough.");
                    } else{
                        Map<String, String> tradeInfo = new HashMap<>();
                        tradeInfo.put("chosen", infoToId.get(selected.toString()));
                        tradeInfo.put("way", "oneWay");
                        tradeInfo.put("term", "temporary");
                        initiateTradeMeetingInfo(tradeInfo);
                    }
                }
        );
        oneWayPermanent.setOnAction(actionEvent ->{
                    if (selected.toString().equals("")){
                        messageBox.setText("Choose an item to trade.");
                    }
                    else if (initiateTradeController.isNewAccount(username)){
                        messageBox.setText("Your first trade must be Two-Way or Lending.");
                    } else if (!initiateTradeController.canOneWay(username)){
                        messageBox.setText("You have insufficient credit to borrow. You may not initiate a one-way " +
                                "trade until you have loaned enough.");
                    } else{
                        Map<String, String> tradeInfo = new HashMap<>();
                        tradeInfo.put("chosen", infoToId.get(selected.toString()));
                        tradeInfo.put("way", "oneWay");
                        tradeInfo.put("term", "permanent");
                        initiateTradeMeetingInfo(tradeInfo);
                    }
                }
        );

        twoWayTemporary.setOnAction(actionEvent -> {
                    if (selected.toString().equals("")){
                        messageBox.setText("Choose an item to trade.");
                    }
                    else if (initiateTradeController.getItemsToOffer(tradeModel.getItemManager().getOwner(infoToId.get(selected.toString()))).size() == 0){
                        messageBox.setText("You cannot make any two way trades. Your inventory is empty");}
                    else{
                        Map<String, String> tradeInfo = new HashMap<>();
                        tradeInfo.put("chosen", infoToId.get(selected.toString()));
                        tradeInfo.put("way", "twoWay");
                        tradeInfo.put("term", "temporary");
                        selectItemTwoWayPage(initiateTradeController.getItemsToOffer(tradeModel.getItemManager().getOwner(infoToId.get(selected.toString()))), tradeInfo);
                        //stage.hide();
                    }
                }
        );
        twoWayPermanent.setOnAction(actionEvent -> {
            if (selected.toString().equals("")){
                messageBox.setText("Choose an item to trade.");
            }
            else if (initiateTradeController.getItemsToOffer(tradeModel.getItemManager().getOwner(infoToId.get(selected.toString()))).size() == 0){
                messageBox.setText("You cannot make any two way trades. Your inventory is empty");
            } else{
                Map<String, String> tradeInfo = new HashMap<>();
                tradeInfo.put("chosen", infoToId.get(selected.toString()));
                tradeInfo.put("way", "twoWay");
                tradeInfo.put("term", "permanent");
                selectItemTwoWayPage(initiateTradeController.getItemsToOffer(tradeModel.getItemManager().getOwner(infoToId.get(selected.toString()))), tradeInfo);
            }
        });

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
        proposedTradesListView.setPlaceholder(new Label("No proposed trades"));
        proposedTradesListView.setItems(proposedTradesInfoObservableList);
        Button confirmBtn = new Button("Confirm");
        Button editBtn = new Button("Edit");
        Button declineBtn = new Button("Decline");

        grid.add(proposedTradesListView, 0, 0, 2, 1);
        grid.add(confirmBtn, 0, 1, 2, 1);
        grid.add(editBtn, 0, 2, 2, 1);
        grid.add(declineBtn, 0, 3, 2, 1);

        confirmBtn.setOnAction(actionEvent -> {
            String tradeId = proposedTradesIDObservableList.get(proposedTradesListView.getSelectionModel().getSelectedIndex());
            if (!tradeModel.getMeetingManager().canChangeMeeting(tradeId, username)) {
                AlertBox.display("Please wait for the other user to confirm the meeting.");
            } else {
            proposedTradesController.confirmMeetingTime(tradeId);
            updateProposedObservableLists();
            updateToBeConfirmedObservableLists();}
        });
        editBtn.setOnAction(actionEvent -> {
            String tradeId = proposedTradesIDObservableList.get(proposedTradesListView.getSelectionModel().getSelectedIndex());
            if (!tradeModel.getMeetingManager().canChangeMeeting(tradeId, username)) {
                AlertBox.display("Please wait for the other user to edit the meeting.");
            } else {
            editProposedTrade(tradeId);
            updateProposedObservableLists(); }
        });
        declineBtn.setOnAction(actionEvent -> {
            String tradeId = proposedTradesIDObservableList.get(proposedTradesListView.getSelectionModel().getSelectedIndex());
            proposedTradesController.declineTrade(tradeId);
            updateProposedObservableLists();
        });

        return grid;
    }

    private Parent viewConfirmTrades(String username) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        updateToBeConfirmedObservableLists();

        ListView<String> confirmTradesListView = new ListView<>();
        Map<String, String> trades = confirmTradesController.getToBeConfirmedTrades(username);
        confirmTradesListView.setPlaceholder(new Label("No trades to confirm"));
        confirmTradesListView.setItems(confirmTradesInfoObservableList);
        Button confirmBtn = new Button("Confirm");

        grid.add(confirmTradesListView, 0, 0, 2, 1);
        grid.add(confirmBtn, 0, 1, 2, 1);

        confirmBtn.setOnAction(actionEvent -> {
            String tradeId = confirmTradesIDObservableList.get(confirmTradesListView.getSelectionModel().getSelectedIndex());
            if (!tradeModel.getMeetingManager().canChangeMeeting(tradeId, username)) {
                AlertBox.display("Already confirmed. Please wait for the other user.");
            } else {
                confirmTradesController.confirmTradeHappened(tradeId, trades.get(tradeId));
            }
            updateToBeConfirmedObservableLists();
        });

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

        updateCompletedTradesObservableList();
        updateOngoingTradesObservableList();
        hboxOngoing.getChildren().addAll(getOngoingTradesListView());
        hboxCompleted.getChildren().addAll(getCompletedTradesListView());


        VBox vbox = new VBox();
        vbox.getChildren().addAll(hBox, messageBox, ongoingTrades, hboxOngoing, completedTrades, hboxCompleted);


        addReview.setOnAction(e -> {
            if (!ratingInput.getText().matches("[12345]")) {
                messageBox.setText("Invalid input. Please review your inputs.");
            } else if (!canAddReview(tradeIdInput.getText(), username)) {
                messageBox.setText("You cannot leave a review for this trade");
            } else {
                profileController.addReview(tradeIdInput.getText(), Integer.parseInt(ratingInput.getText()), commentInput.getText());
                messageBox.setText("Review for trade " + tradeIdInput.getText() + " added successfully!");
            }
        });

        return vbox;
    }

    // Helper methods used by different tabs

    private void updateProposedObservableLists() {
        proposedTradesIDObservableList.clear();
        proposedTradesInfoObservableList.clear();
        List<String> trades = tradeModel.getMeetingManager().getToCheckTrades(tradeModel.getTradeManager().getTradesOfUser(tradeModel.getCurrentUser(), "ongoing"), "proposed");
        proposedTradesIDObservableList.addAll(trades);
        List<String> removeChar = new ArrayList<>(
                Arrays.asList("\"", "{", "}", "[", ",", "]\n"));
        try {
            for (String tradeId : trades) {
                List<JSONObject> allTrade = new ArrayList<>();
                allTrade.add(tradeModel.getTradeManager().getTradeInfo(tradeId));
                allTrade.addAll(tradeModel.getMeetingManager().getMeetingsInfo(tradeId));
                StringBuilder allTradeInfo = new StringBuilder();
                for (JSONObject details : allTrade) {
                    String strTradeInfo = details.toString(0);
                    for (String ch : removeChar) {
                        strTradeInfo = strTradeInfo.replace(ch, "");
                    }
                    allTradeInfo.append(strTradeInfo);
                }

                proposedTradesInfoObservableList.add(allTradeInfo.toString());
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void editProposedTrade(String tradeId) {
        Stage stage2 = new Stage();
        GridPane grid = new GridPane();
        Scene scene = new Scene(grid, width, height);
        stage2.setScene(scene);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label locationLabel = new Label("Location:");
        TextField locationField = new TextField();
        Label timeLabel = new Label("Time (hh:mm):");
        TextField timeField = new TextField();

        Button editBtn = new Button("Edit Meeting");

        grid.add(datePicker, 0, 1, 2, 1);
        grid.add(locationLabel, 0, 0);
        grid.add(locationField, 1, 0);
        grid.add(timeLabel, 0, 2);
        grid.add(timeField, 1, 2);
        grid.add(editBtn, 1, 5);

        List<String> details = new ArrayList<>();

        editBtn.setOnAction(actionEvent -> {
            LocalDate date = datePicker.getValue();
            System.out.println(date);
            String location = locationField.getText();
            String time = timeField.getText();
            if (date == null || location == null || time == null) {
                AlertBox.display("You have an incorrect field. Please review your inputs.");
            } else if (!time.matches("^([0-1][0-9]|[2][0-3]):([0-5][0-9])$")) {
                AlertBox.display("You have an incorrect field. Please review your inputs.");
            } else {
                details.add(location);
                String dateString = (date.getDayOfMonth()) +"/" +(date.getMonthValue()) +"/" + (date.getYear()) + " " + time;
                details.add(dateString);
                proposedTradesController.editMeetingTime(tradeId, details);
                updateProposedObservableLists();
                stage2.close();
            }});

        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.showAndWait();
    }

    private void updateToBeConfirmedObservableLists() {
        confirmTradesIDObservableList.clear();
        confirmTradesInfoObservableList.clear();
        Map<String, String> trades = confirmTradesController.getToBeConfirmedTrades(username);
        confirmTradesIDObservableList.addAll(trades.keySet());
        List<String> removeChar = new ArrayList<>(Arrays.asList("\"", "{", "}", "[", ",", "]\n"));
        try {
            for (String tradeId : trades.keySet()) {
                List<JSONObject> allTrade = new ArrayList<>();
                allTrade.add(tradeModel.getTradeManager().getTradeInfo(tradeId));
                allTrade.addAll(tradeModel.getMeetingManager().getMeetingsInfo(tradeId));
                StringBuilder allTradeInfo = new StringBuilder();
                for (JSONObject details : allTrade) {
                    String strTradeInfo = details.toString(0);
                    for (String ch : removeChar) {
                        strTradeInfo = strTradeInfo.replace(ch, "");
                    }
                    allTradeInfo.append(strTradeInfo);
                }
                confirmTradesInfoObservableList.add(allTradeInfo.toString());
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void selectItemTwoWayPage(List<String> itemsToOffer, Map<String, String> initiateTradeInfo){
        Stage stage2 = new Stage();
        Text title = new Text("Select an item to exchange.");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Map<String, String> infoToId = new HashMap<>();
        for (String id: itemsToOffer){
            infoToId.put(tradeModel.getItemManager().getItemInfo(id), id);
        }

        //Vbox
        VBox mainLayout = new VBox();
        mainLayout.setPrefWidth(300);
        mainLayout.setSpacing(20);
        mainLayout.setPadding(new Insets(25, 25, 25, 25));
        mainLayout.setAlignment(Pos.CENTER);

        ListView<String> list = new ListView<>();
        ObservableList<String> availableItems = FXCollections.observableArrayList();
        availableItems.addAll(infoToId.keySet());
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
                    !userInputHour.getText().matches("^([0-1][0-9]|[2][0-3]):([0-5][0-9])$") | pickDate.getValue() == null){
                messageLabel.setText("You have an invalid input. Please check your input again.");
            } else{
                initiateTradeInfo.put("location", userInputLocation.getText());
                LocalDate ld = pickDate.getValue();
                String dateString = (ld.getDayOfMonth()) +"/" +(ld.getMonthValue()) +"/" + (ld.getYear()) + " " + userInputHour.getText();
                initiateTradeInfo.put("date", dateString);
                try {
                    initiateTradeController.createTrade(initiateTradeInfo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Label createdTrade = new Label("Trade initiated successfully! ");
                grid.getChildren().removeAll(tradeDate, timeInput, pickDate, userInputHour, tradeLocation, userInputLocation, messageLabel, confirmBtnReturnItem, meetingTitle);
                grid.add(createdTrade, 0, 1, 2, 1);
                grid.add(exitBtn, 0, 2, 2, 1);
            }
        });

        confirmBtn.setOnAction(actionEvent -> {
            if (selected.toString().equals("")){
                messageText.setText("Choose an item to trade.");
            } else {
                initiateTradeInfo.put("giving", infoToId.get(selected.toString()));
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

    private void initiateTradeMeetingInfo(Map<String, String> initiateTradeInfo) {
        Stage stage2 = new Stage();
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
        Button confirmBtn = new Button("Confirm");
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(actionEvent -> {
            stage2.close();
//            getRoot(); //TODO: not too sure
        });

        Label messageLabel = new Label();

        grid.add(tradeDate, 0, 6, 2, 1);
        grid.add(timeInput, 4, 6, 2, 1);
        grid.add(pickDate, 0, 7, 2, 1);
        grid.add(userInputHour, 4, 7, 2, 1);
        grid.add(tradeLocation, 0, 9, 2, 1);
        grid.add(userInputLocation, 0, 10, 2, 1);
        grid.add(messageLabel, 0,12,4,1);
        grid.add(confirmBtn, 4, 13, 2, 1);

        confirmBtn.setOnAction(actionEvent -> {
            if (userInputLocation.getText().isEmpty()| userInputHour.getText().isEmpty()|
                    !userInputHour.getText().matches("^([0-1][0-9]|[2][0-3]):([0-5][0-9])$") | pickDate.getValue() == null){
                messageLabel.setText("You have an invalid input. Please check your input again.");
            } else{
                initiateTradeInfo.put("location", userInputLocation.getText());
                LocalDate ld = pickDate.getValue();
                String dateString = (ld.getDayOfMonth()) +"/" +(ld.getMonthValue()) +"/" + (ld.getYear()) + " " + userInputHour.getText();
                initiateTradeInfo.put("date", dateString);
                try {
                    initiateTradeController.createTrade(initiateTradeInfo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Label createdTrade = new Label("Trade initiated successfully! ");
                grid.getChildren().removeAll(meetingTitle, tradeDate, timeInput, pickDate, userInputHour, tradeLocation, userInputLocation, messageLabel, confirmBtn);
                grid.add(createdTrade,0 , 1, 2, 1);
                grid.add(exitBtn, 1, 3, 2, 1);
            }
        });


        Scene scene = new Scene(grid, width, height);
        stage2.setScene(scene);
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.showAndWait();
    }

    private void updateOngoingTradesObservableList() {
        userOngoingTradeList.clear();
        List<String> trades = tradeModel.getTradeManager().getTradesOfUser(username, "ongoing");
        List<String> removeChar = new ArrayList<>(Arrays.asList("\"", "{", "}", "[", ",", "]\n"));
        try {
            for (String tradeId : trades) {
                List<JSONObject> allTrade = new ArrayList<>();
                allTrade.add(tradeModel.getTradeManager().getTradeInfo(tradeId));
                allTrade.addAll(tradeModel.getMeetingManager().getMeetingsInfo(tradeId));
                StringBuilder allTradeInfo = new StringBuilder();
                for (JSONObject details : allTrade) {
                    String strTradeInfo = details.toString(0);
                    for (String ch : removeChar) {
                        strTradeInfo = strTradeInfo.replace(ch, "");
                    }
                    allTradeInfo.append(strTradeInfo);
                }
                userOngoingTradeList.addAll(allTradeInfo.toString());
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ListView<String> getOngoingTradesListView() {
        updateOngoingTradesObservableList();
        ListView<String> list = new ListView<>();
        list.setPlaceholder(new Label("You have no ongoing trades"));
        list.setItems(userOngoingTradeList);
        list.setPrefWidth(300);
        list.setPrefHeight(200);
        return list;
    }

    private void updateCompletedTradesObservableList() {
        userCompletedTradeList.clear();
        List<String> trades = tradeModel.getTradeManager().getTradesOfUser(username, "completed");
        List<String> removeChar = new ArrayList<>(Arrays.asList("\"", "{", "}", "[", ",", "]\n"));
        try {
            for (String tradeId : trades) {
                List<JSONObject> allTrade = new ArrayList<>();
                allTrade.add(tradeModel.getTradeManager().getTradeInfo(tradeId));
                allTrade.addAll(tradeModel.getMeetingManager().getMeetingsInfo(tradeId));
                StringBuilder allTradeInfo = new StringBuilder();
                for (JSONObject details : allTrade) {
                    String strTradeInfo = details.toString(0);
                    for (String ch : removeChar) {
                        strTradeInfo = strTradeInfo.replace(ch, "");
                    }
                    allTradeInfo.append(strTradeInfo);
                }
                userCompletedTradeList.addAll(allTradeInfo.toString());
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ListView<String> getCompletedTradesListView() {
        updateCompletedTradesObservableList();
        ListView<String> list = new ListView<>();
        list.setPlaceholder(new Label("You have no completed trades"));
        list.setItems(userCompletedTradeList);
        list.setPrefWidth(300);
        list.setPrefHeight(200);
        return list;
    }

    private boolean canAddReview(String tradeId, String username) {
        List<String> userTrades = tradeModel.getTradeManager().getTradesOfUser(username, "completed");
        if (!userTrades.contains(tradeId)) {
            return false;
        } else {
            String receiver = "";
            for (List<String> users : tradeModel.getTradeManager().itemToUsers(tradeId).values()) {
                users.remove(username);
                receiver = users.get(0);
            }
            return !tradeModel.getReviewManager().alreadyWroteReview(username, receiver, tradeId);
        }
    }

}
