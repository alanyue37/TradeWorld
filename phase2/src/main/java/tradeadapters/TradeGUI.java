package tradeadapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;
import useradapters.ProfileController;
import useradapters.UserGUI;
import viewingadapters.ViewingTradesController;

import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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


    }

    @Override
    public void initialScreen() {
        stage.setTitle("Trade Menu Options");

        Text title = new Text("What would you like to do?");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Button initiateTradeBtn = new Button("Initiate trade");
        Button proposedTradesBtn = new Button("Manage proposed trades");
        Button confirmTradesBtn = new Button("Confirm real life meeting of trades");
        Button viewTradesBtn = new Button("View ongoing and completed trades");
        Button backButtonBtn = new Button("Go back");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);
        grid.add(initiateTradeBtn, 0, 1, 2, 1);
        grid.add(proposedTradesBtn, 0, 2, 2, 1);
        grid.add(confirmTradesBtn, 0, 3, 2, 1);
        grid.add(viewTradesBtn, 0, 4, 2, 1);
        grid.add(backButtonBtn, 0, 5, 2, 1);

        if (!initiateTradeController.initiateTrade()) {
            initiateTradeBtn.setDisable(true);
        } else {
            initiateTradeBtn.setOnAction(actionEvent -> getAvailableItems());
        }

        proposedTradesBtn.setOnAction(actionEvent -> {
            try {
                viewProposedTrades(username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        confirmTradesBtn.setOnAction(actionEvent -> {
            try {
                viewConfirmTrades(username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        viewTradesBtn.setOnAction(actionEvent -> {
            try {
                viewAllTrades();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        backButtonBtn.setOnAction(actionEvent -> new UserGUI(stage, width, height, tradeModel, username).initialScreen());


        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void viewProposedTrades(String username) throws JSONException {
        List<Scene> scenes = new ArrayList<>();
        List<String> trades = tradeModel.getMeetingManager().getToCheckTrades(tradeModel.getTradeManager().getTradesOfUser(username, "ongoing"), "proposed");

        GridPane gridFinal = new GridPane();
        gridFinal.setAlignment(Pos.CENTER);
        gridFinal.setHgap(10);
        gridFinal.setVgap(10);
        gridFinal.setPadding(new Insets(25, 25, 25, 25));

        Text done = new Text("No more proposed trades.");
        done.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));

        Button buttonFinal = new Button("Done");

        gridFinal.add(done, 0, 0, 2, 1);
        gridFinal.add(buttonFinal, 0, 1, 2, 1);

        buttonFinal.setOnAction(actionEvent -> initialScreen());

        Scene sceneFinal = new Scene(gridFinal);

        int i = trades.size() - 1;
        while (i >= 0) {
            List<JSONObject> allTrade = new ArrayList<>();
            allTrade.add(tradeModel.getTradeManager().getTradeInfo(trades.get(i)));
            allTrade.addAll(tradeModel.getMeetingManager().getMeetingsInfo(trades.get(i)));
            StringBuilder allTradeInfo = new StringBuilder();
            for (JSONObject details : allTrade) {
                allTradeInfo.append(details.toString(3).replace("\"", ""));
            }

            String tradeId = allTrade.get(0).get("Trade ID").toString();

            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));

            Text trade = new Text(allTradeInfo.toString());
            trade.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

            Button confirmBtn = new Button("Confirm");
            Button editBtn = new Button("Edit");
            Button declineBtn = new Button("Decline");
            Button skipBtn = new Button("Skip");

            grid.add(trade, 0, 0, 2, 1);
            grid.add(confirmBtn, 0, 1, 2, 1);
            grid.add(editBtn, 0, 2, 2, 1);
            grid.add(declineBtn, 0, 3, 2, 1);
            grid.add(skipBtn, 0, 4, 2, 1);

            if (!tradeModel.getMeetingManager().canChangeMeeting(tradeId, username)) {
                confirmBtn.setDisable(true);
                editBtn.setDisable(true);
            }

            declineBtn.setOnAction(actionEvent -> proposedTradesController.declineTrade(tradeId));

            int ii = i;
            if (ii + 1 > trades.size() - 1) {
                skipBtn.setOnAction(actionEvent -> stage.setScene(sceneFinal));
                confirmBtn.setOnAction(actionEvent -> { proposedTradesController.confirmMeetingTime(tradeId);
                    stage.setScene(sceneFinal); });
                editBtn.setOnAction(actionEvent -> { editTrade(tradeId); stage.setScene(sceneFinal); });
                declineBtn.setOnAction(actionEvent -> { proposedTradesController.declineTrade(tradeId); stage.setScene(sceneFinal); });
            } else {
                skipBtn.setOnAction(actionEvent -> stage.setScene(scenes.get(ii + 1)));
                confirmBtn.setOnAction(actionEvent -> { proposedTradesController.confirmMeetingTime(tradeId);
                    stage.setScene(scenes.get(ii + 1)); });
                editBtn.setOnAction(actionEvent -> { editTrade(tradeId); stage.setScene(scenes.get(ii + 1)); });
                declineBtn.setOnAction(actionEvent -> { proposedTradesController.declineTrade(tradeId); stage.setScene(scenes.get(ii + 1)); });
            }

            i--;
            scenes.add(0, new Scene(grid));
        }
        if (trades.size() == 0) {
            stage.setScene(sceneFinal);
        } else { stage.setScene(scenes.get(0)); }
        stage.show();
    }

    public void editTrade(String tradeId) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label locationLabel = new Label("Location: ");
        TextField locationField = new TextField();
        Label timeLabel = new Label("Time (hh:mm): ");
        TextField timeField = new TextField();

        Button editBtn = new Button("Edit Meeting");

        grid.add(datePicker, 0, 1, 2, 1);
        grid.add(locationLabel, 0, 0);
        grid.add(locationField, 1, 0);
        grid.add(timeLabel, 0, 2);
        grid.add(timeField, 1, 2);

        List<String> details = new ArrayList<>();

        editBtn.setOnAction(actionEvent -> {
            LocalDate date = datePicker.getValue();
            String location = locationField.getText();
            String time = timeField.getText();
            if (date == null || location == null || time == null) {
                editBtn.setDisable(true);
            } else if (!time.matches("^([0-1][0-9]|[2][0-3]):([0-5][0-9])$")) {
                AlertBox.display("You have an incorrect field. Please review your inputs.");
            } else {
                details.add(location);
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String strDate = dateFormat.format(date);
                details.add(strDate + " " + time);
                proposedTradesController.editMeetingTime(tradeId, details);
            }});

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }

    public void viewConfirmTrades(String username) throws JSONException {
        List<Scene> scenes = new ArrayList<>();
        Map<String, String> trades = confirmTradesController.getToBeConfirmedTrades(username);

        GridPane gridFinal = new GridPane();
        gridFinal.setAlignment(Pos.CENTER);
        gridFinal.setHgap(10);
        gridFinal.setVgap(10);
        gridFinal.setPadding(new Insets(25, 25, 25, 25));

        Text done = new Text("No more trades to confirm.");
        done.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));

        Button buttonFinal = new Button("Done");

        gridFinal.add(done, 0, 0, 2, 1);
        gridFinal.add(buttonFinal, 0, 1, 2, 1);

        buttonFinal.setOnAction(actionEvent -> initialScreen());

        Scene sceneFinal = new Scene(gridFinal);

        int i = trades.size() - 1;
        for (String tradeId : trades.keySet()) {
            List<JSONObject> allTrade = new ArrayList<>();
            allTrade.add(tradeModel.getTradeManager().getTradeInfo(tradeId));
            allTrade.addAll(tradeModel.getMeetingManager().getMeetingsInfo(tradeId));
            StringBuilder allTradeInfo = new StringBuilder();
            for (JSONObject details : allTrade) {
                allTradeInfo.append(details.toString(3).replace("\"", ""));
            }

            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));

            Text trade = new Text(allTradeInfo.toString());
            trade.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

            Button confirmBtn = new Button("Confirm");
            Button skipBtn = new Button("Skip");

            grid.add(trade, 0, 0, 2, 1);
            grid.add(confirmBtn, 0, 1, 2, 1);
            grid.add(skipBtn, 0, 2, 2, 1);

            if (!tradeModel.getMeetingManager().canChangeMeeting(tradeId, username)) {
                confirmBtn.setDisable(true); }

            int ii = i;
            if (ii + 1 > trades.size() - 1) {
                skipBtn.setOnAction(actionEvent -> stage.setScene(sceneFinal));
                confirmBtn.setOnAction(actionEvent -> { confirmTradesController.confirmTradeHappened(tradeId, trades.get(tradeId));
                    stage.setScene(sceneFinal);});
            } else {
                skipBtn.setOnAction(actionEvent -> stage.setScene(scenes.get(ii + 1)));
                confirmBtn.setOnAction(actionEvent -> { confirmTradesController.confirmTradeHappened(tradeId, trades.get(tradeId));
                    stage.setScene(scenes.get(ii + 1));});
            }

            i--;
            scenes.add(0, new Scene(grid));
        }
        if (trades.size() == 0) {
            stage.setScene(sceneFinal);
        } else { stage.setScene(scenes.get(0)); }
        stage.show();
    }

    public void getAvailableItems() {
        Text title = new Text("Available items");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Map<String, String> infoToId = initiateTradeController.getAvailableItems();

        //Vbox
        VBox mainLayout = new VBox();
        mainLayout.setPrefWidth(300);
        mainLayout.setSpacing(20);
        mainLayout.setPadding(new Insets(25, 25, 25, 25));

        ListView<String> list = new ListView<>();
        ObservableList<String> availableItems = FXCollections.observableArrayList();
        availableItems.addAll(infoToId.keySet());
        list.setItems(availableItems);
        list.setPlaceholder(new Label("There are no available items for trading."));

        AtomicReference<String> selected = new AtomicReference<>("");
        ToggleGroup item = new ToggleGroup();
        for (String info : availableItems) {
            RadioButton radioBtn = new RadioButton(info);
            radioBtn.setToggleGroup(item);
            mainLayout.getChildren().add(radioBtn);
            radioBtn.setOnAction(actionEvent -> {
                selected.set(info);
            });

        }

        Label selectType = new Label("Select the type of trade you'd like to make.");
        Button oneWayTemporary = new Button("One way temporary");
        Button oneWayPermanent = new Button("One way permanent");
        Button twoWayTemporary = new Button("Two way temporary");
        Button twoWayPermanent = new Button("Two way permanent");
        Button backButton = new Button("Back");

        Label messageBox = new Label();

        backButton.setOnAction(actionEvent -> initialScreen());

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

        mainLayout.getChildren().addAll(messageBox, selectType, oneWayTemporary, oneWayPermanent,  twoWayTemporary, twoWayPermanent, backButton);
        backButton.setOnAction(actionEvent -> initialScreen());


        scene = new Scene(mainLayout, width, height);
        stage.setScene(scene);
        stage.show();
    }


    public void selectItemTwoWayPage(List<String> itemsToOffer, Map<String, String> initiateTradeInfo){
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

        ListView<String> list = new ListView<>();
        ObservableList<String> availableItems = FXCollections.observableArrayList();
        availableItems.addAll(infoToId.keySet());
        list.setItems(availableItems);

        Button confirmBtn = new Button("Confirm");

        Label messageText = new Label();

        AtomicReference<String> selected = new AtomicReference<>("");
        ToggleGroup item = new ToggleGroup();
        for (String info : availableItems) {
            RadioButton radioBtn = new RadioButton(info);
            radioBtn.setToggleGroup(item);
            mainLayout.getChildren().add(radioBtn);
            radioBtn.setOnAction(actionEvent -> {
                selected.set(info);
            });
        }

        confirmBtn.setOnAction(actionEvent -> {
            if (selected.toString().equals("")){
                messageText.setText("Choose an item to trade.");
            } else {
                initiateTradeInfo.put("giving", infoToId.get(selected.toString()));
                initiateTradeMeetingInfo(initiateTradeInfo);
            }
        });

        mainLayout.getChildren().addAll(messageText, confirmBtn);

        scene = new Scene(mainLayout, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void initiateTradeMeetingInfo(Map<String, String> initiateTradeInfo) {
        Text title = new Text("Meeting details");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);

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
        Button backButton = new Button("Back");
        backButton.setOnAction(actionEvent -> initialScreen());

        Label messageLabel = new Label();

        grid.add(tradeDate, 0, 6, 2, 1);
        grid.add(timeInput, 4, 6, 2, 1);
        grid.add(pickDate, 0, 7, 2, 1);
        grid.add(userInputHour, 4, 7, 2, 1);
        grid.add(tradeLocation, 0, 9, 2, 1);
        grid.add(userInputLocation, 0, 10, 2, 1);
        grid.add(messageLabel, 0,12,2,1);
        grid.add(backButton,0,13,2,1 );
        grid.add(confirmBtn, 4, 13, 2, 1);

        confirmBtn.setOnAction(actionEvent -> {
            if (userInputLocation.getText().isEmpty()| userInputHour.getText().isEmpty()|
                    !userInputHour.getText().matches("^([0-1][0-9]|[2][0-3]):([0-5][0-9])$") | pickDate.getValue() == null){
                messageLabel.setText("You have an invalid input. Please check your input again.");
            } else{
                initiateTradeInfo.put("location", userInputLocation.getText());
                LocalDate ld = pickDate.getValue();
                String dateString = String.valueOf(ld.getDayOfMonth()) +"/" +String.valueOf(ld.getMonthValue()) +"/" + String.valueOf(ld.getYear()) + " " + userInputHour.getText();
                initiateTradeInfo.put("date", dateString);
                try {
                    initiateTradeController.createTrade(initiateTradeInfo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                AlertBox.display("Trade initiated successfully! ");
                initialScreen();
            }
        });


        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void viewAllTrades() throws JSONException {

        TextField commentInput = new TextField();
        commentInput.setPromptText("Leave a comment");
        TextField ratingInput = new TextField();
        ratingInput.setPromptText("Rating you want to give (1-5)");
        TextField tradeIdInput = new TextField();
        tradeIdInput.setPromptText("Trade ID");

        Label messageBox = new Label();

        Button addReview = new Button("Add Review");

        HBox backBox = new HBox(10);
        Button backButton = new Button("Back");
        backButton.setOnAction(actionEvent -> initialScreen());
        backBox.getChildren().addAll(backButton);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(25, 25, 25, 25));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(tradeIdInput, ratingInput, commentInput,addReview, backBox);

        HBox hbox2 = new HBox();
        hbox2.setPadding(new Insets(25, 25, 25, 25));
        hbox2.setSpacing(10);
        hbox2.getChildren().addAll(getTradeListView());


        VBox vbox = new VBox();
        vbox.getChildren().addAll(hBox, messageBox, hbox2);


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


        scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    public ListView<String> getTradeListView() throws JSONException {

        //trades
        List<JSONObject> jsonInfo = new ArrayList<>();
        for (String id : tradeModel.getTradeManager().getTradesOfUser(username, "ongoing")) {
            jsonInfo.add(tradeModel.getTradeManager().getTradeInfo(id));
        }
        for (String id : tradeModel.getTradeManager().getTradesOfUser(username, "completed")){
            jsonInfo.add(tradeModel.getTradeManager().getTradeInfo(id));
        }
        List<String> tradeInfo = new ArrayList<>();
        for (JSONObject info : jsonInfo){
            String allInfo = info.toString(3).replace("\"", "");
            tradeInfo.add(allInfo);
        }

        ListView<String> list = new ListView<>();
        ObservableList<String> trades = FXCollections.observableArrayList(tradeInfo);
        list.setItems(trades);
        list.setPlaceholder(new Label("There are no trades to be viewed."));
        list.setPrefHeight(height - 50); // we could change this
        list.setPrefWidth(width - 50);   // we could change this


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
