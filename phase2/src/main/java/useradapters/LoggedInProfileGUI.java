package useradapters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tradegateway.TradeModel;

import java.lang.reflect.Type;
import java.util.List;

public class LoggedInProfileGUI extends ProfileGUI {

    private final String currentUser;

    public LoggedInProfileGUI(Stage stage, int width, int height, TradeModel tradeModel, String userProfile) {
        super(stage, width, height, tradeModel, userProfile);
        currentUser = tradeModel.getCurrentUser();
    }

    @Override
    public void initialScreen() {
        initializeBasicView();

        // Additional rows
        HBox statusesRow;
        HBox friendsRow;

        // Set statusesRow
        // vacation + public/private
        statusesRow = getStatusesRow();
        addRow(statusesRow);

        if (getController().isOwnProfile(getUserProfile())) {
            // Add friends requests list row if own profile
            friendsRow = getFriendsRequestsRow();
        }
        else {
            // Add friend status / send friend request button if not own profile
            friendsRow = getFriendStatusRow();
        }
        addRow(friendsRow);

        showStage();
    }

    protected HBox getStatusesRow() {
        // TODO: add listener to process events for radio buttons
        HBox row = new HBox();
        VBox statusesColumn = new VBox();

        HBox vacationRow = new HBox();
        Label vacationLabel = new Label("Vacation Mode:\t");
        Label vacationValueLabel;
        ToggleGroup vacationGroup = new ToggleGroup();
        RadioButton onVacationButton = new RadioButton("On");
        RadioButton offVacationButton = new RadioButton("Off");
        onVacationButton.setToggleGroup(vacationGroup);
        offVacationButton.setToggleGroup(vacationGroup);
        if (getController().getVacationMode(getUserProfile())) {
            vacationValueLabel = new Label("On");
            onVacationButton.setSelected(true);
        }
        else {
            vacationValueLabel = new Label("Off");
            offVacationButton.setSelected(true);
        }

        HBox privacyRow = new HBox();
        Label privacyLabel = new Label("Private Mode:\t\t");
        Label privacyValueLabel;
        ToggleGroup privacyGroup = new ToggleGroup();
        RadioButton onPrivacyButton = new RadioButton("On");
        RadioButton offPrivacyButton = new RadioButton("Off");
        onPrivacyButton.setToggleGroup(privacyGroup);
        offPrivacyButton.setToggleGroup(privacyGroup);

        if (getController().getPrivacyMode(getUserProfile())) {
            privacyValueLabel = new Label("On");
            onPrivacyButton.setSelected(true);
        }
        else {
            privacyValueLabel = new Label("Off");
            offPrivacyButton.setSelected(true);
        }

        if (getController().isOwnProfile(getUserProfile())) {
            privacyRow.getChildren().addAll(privacyLabel, offPrivacyButton, onPrivacyButton);
            vacationRow.getChildren().addAll(vacationLabel, offVacationButton, onVacationButton);

        }
        else {
            privacyRow.getChildren().addAll(privacyLabel, privacyValueLabel);
            vacationRow.getChildren().addAll(vacationLabel, vacationValueLabel);
        }

        statusesColumn.getChildren().addAll(vacationRow, privacyRow);
        vacationRow.setSpacing(20);
        privacyRow.setSpacing(20);
        row.getChildren().add(statusesColumn);
        return row;
    }

    protected HBox getFriendsRequestsRow() {
        // TODO: complete
        HBox row = new HBox();
        VBox requestsColumn = new VBox();
        Label requestsLabel = new Label("Friend Requests");
        String json = getController().getFriendRequests();
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> friendsRequests = gson.fromJson(json, type);
        ListView<String> list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(friendsRequests);
        list.setItems(items);
        list.setPrefWidth(300);
        list.setPrefHeight(200);

        requestsColumn.getChildren().addAll(requestsLabel, list);
        row.getChildren().add(requestsColumn);
        return row;
    }

    protected HBox getFriendStatusRow() {
        //TODO: Complete
        // Show add friend button if not friends. Otherwise indicate friendship status
        HBox row = new HBox();
        return row;
    }


}
