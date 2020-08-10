package tradeadapters;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.ListBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import viewingadapters.ViewingTradesController;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;

public class TradeGui implements RunnableGUI {
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

    public TradeGui(Stage stage, int width, int height, TradeModel tradeModel, String username) {
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
        tradeInitialScreen();
    }

    public void tradeInitialScreen(){
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
        grid.add(viewTradesBtn, 0,4 , 2, 1);
        grid.add(backButtonBtn, 0, 5, 2, 1);

        if (!initiateTradeController.initiateTrade()){
            initiateTradeBtn.setDisable(true);
        } else{initiateTradeBtn.setOnAction(actionEvent -> getAvailableItems());}

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
        done.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Button buttonFinal = new Button("Done");

        gridFinal.add(done, 0, 0, 2, 1);
        gridFinal.add(buttonFinal, 0, 1, 2, 1);

        buttonFinal.setOnAction(actionEvent -> tradeInitialScreen());

        Scene sceneFinal = new Scene(gridFinal);

        int i = trades.size() - 1;
        while (i >= 0) {
            List<JSONObject> allTrade = new ArrayList<>();
            allTrade.add(tradeModel.getTradeManager().getTradeInfo(trades.get(i)));
            allTrade.addAll(tradeModel.getMeetingManager().getMeetingsInfo(trades.get(i)));
            StringBuilder allTradeInfo = new StringBuilder();
            for (JSONObject details : allTrade) {
                allTradeInfo.append(details.toString(4));
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
            } else {
                confirmBtn.setOnAction(actionEvent -> proposedTradesController.confirmMeetingTime(tradeId));
                editBtn.setOnAction(actionEvent -> editTrade(tradeId));
            }
            declineBtn.setOnAction(actionEvent -> proposedTradesController.declineTrade(tradeId));

            int ii = i;
            if (ii + 1 > trades.size() - 1) {
                skipBtn.setOnAction(actionEvent -> stage.setScene(sceneFinal));
            } else {
                skipBtn.setOnAction(actionEvent -> stage.setScene(scenes.get(ii + 1)));
            }

            i--;
            scenes.add(0, new Scene(grid));
        }
        stage.setScene(scenes.get(0));
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

        Button dateBtn = new Button("Date");
        Button editBtn = new Button("Edit Meeting");

        grid.add(datePicker, 0, 1, 2, 1);
        grid.add(locationLabel, 0, 0);
        grid.add(locationField, 1, 0);

        if (locationField.getText().isEmpty()) { // while loop?
            editBtn.setDisable(true);
        }

        dateBtn.setOnAction(actionEvent -> {
            LocalDate date = datePicker.getValue();
            if (date == null) {
                editBtn.setDisable(true);
            }
        });

        // get time and combine with date in Date format
        // check if time is empty

        List<String> details = new ArrayList<>();
        details.add(locationField.getText());
        // details.add(date); // don't know how to save date value outside of dateBtn.setOnAction method

        editBtn.setOnAction(actionEvent -> proposedTradesController.editMeetingTime(tradeId, details));

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
        done.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Button buttonFinal = new Button("Done");

        gridFinal.add(done, 0, 0, 2, 1);
        gridFinal.add(buttonFinal, 0, 1, 2, 1);

        buttonFinal.setOnAction(actionEvent -> tradeInitialScreen());

        Scene sceneFinal = new Scene(gridFinal);

        int i = trades.size() - 1;
        for (String tradeId : trades.keySet()) {
            List<JSONObject> allTrade = new ArrayList<>();
            allTrade.add(tradeModel.getTradeManager().getTradeInfo(tradeId));
            allTrade.addAll(tradeModel.getMeetingManager().getMeetingsInfo(tradeId));
            StringBuilder allTradeInfo = new StringBuilder();
            for (JSONObject details : allTrade) {
                allTradeInfo.append(details.toString(4));
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
                confirmBtn.setDisable(true);
            } else {
                confirmBtn.setOnAction(actionEvent -> confirmTradesController.confirmTradeHappened(tradeId, trades.get(tradeId)));
            }

            int ii = i; // revisit later, might not work for buttons besides skip
            if (ii + 1 > trades.size() - 1) {
                skipBtn.setOnAction(actionEvent -> stage.setScene(sceneFinal));
            } else {
                skipBtn.setOnAction(actionEvent -> stage.setScene(scenes.get(ii + 1)));
            }

            i--;
            scenes.add(0, new Scene(grid));
        }
        stage.setScene(scenes.get(0));
        stage.show();
    }

    public void getAvailableItems(){
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

        ToggleGroup item = new ToggleGroup();
        for (String info: availableItems){
            RadioButton radioBtn = new RadioButton(info);
            radioBtn.setToggleGroup(item);
            mainLayout.getChildren().add(radioBtn);
        }

        //button

        Button chooseButton = new Button("Select this item to trade");
        HBox chooseBox = new HBox(10);
        Button backButton = new Button("Back");
        HBox backBox = new HBox(10);

        chooseButton.setAlignment(Pos.TOP_LEFT);
        chooseBox.getChildren().add(chooseButton);
        backButton.setAlignment(Pos.BOTTOM_LEFT);
        backBox.getChildren().add(backButton);

        mainLayout.getChildren().addAll(chooseButton, backButton);

        backButton.setOnAction(actionEvent -> tradeInitialScreen());

        RadioButton selectedRatioButton = (RadioButton) item.getSelectedToggle();
//        if (selectedRatioButton() == null) {
//            chooseButton.setDisable(true);
//        } else{
//            chooseButton.setOnAction(actionEvent -> initiateTradeInfo());}
//        chooseButton.setOnAction(actionEvent -> initiateTradeInfo());

        scene = new Scene(mainLayout, width, height);
        stage.setScene(scene);
        stage.show();

    }

    public void initiateTradeInfo(){
        Text title = new Text("hi");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

}
