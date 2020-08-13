package useradapters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tradegateway.TradeModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoggedInProfileGUI extends ProfileGUI {

    private ObservableList<String> friendsRequests;

    public LoggedInProfileGUI(Stage stage, int width, int height, TradeModel tradeModel, String userProfile) {
        super(stage, width, height, tradeModel, userProfile);
        friendsRequests = FXCollections.observableArrayList();
    }

    @Override
    public void showScreen() {
        initializeScreen();
        Scene scene = new Scene(getRoot(), getWidth(), getHeight());
        getStage().setScene(scene);
        getStage().show();
    }

    @Override
    protected void initializeScreen() {
        super.initializeScreen();

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
    }

    @Override
    protected HBox getAccountStandingRow() {
        HBox row =  super.getAccountStandingRow();
        // If frozen and own profile add Request Unfreeze link
        if (getController().getFrozenStatus(getUserProfile()) && getController().isOwnProfile(getUserProfile())) {
            Hyperlink requestUnfreeze = new Hyperlink("Request unfreeze");
            requestUnfreeze.setBorder(Border.EMPTY);
            requestUnfreeze.setPadding(new Insets(0, 0, 0, 20));
            requestUnfreeze.setOnAction(actionEvent -> getController().requestUnfreeze());
            row.getChildren().add(requestUnfreeze);
        }
        return row;
    }

    protected HBox getStatusesRow() {
        // TODO: Refactor into two shorter methods time permitting
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
        onPrivacyButton.setUserData(true);
        offPrivacyButton.setUserData(false);
        onVacationButton.setUserData(true);
        offVacationButton.setUserData(false);
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

        // Add event handlers for radio buttons
        // Based on sample code from: https://docs.oracle.com/javafx/2/ui_controls/radio-button.htm

        privacyGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (privacyGroup.getSelectedToggle() != null) {
                    boolean privacy = (boolean) privacyGroup.getSelectedToggle().getUserData();
                    System.out.println(privacy);
                    getController().setPrivacyMode(privacy);
                }

            }
        });

        vacationGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (vacationGroup.getSelectedToggle() != null) {
                    boolean vacation = (boolean) vacationGroup.getSelectedToggle().getUserData();
                    System.out.println(vacation);
                    getController().setVacationMode(vacation);}

            }
        });

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
        HBox row = new HBox();
        VBox requestsColumn = new VBox();
        Label requestsLabel = new Label("Friend Requests");
        ListView<String> list = new ListView<>();
        list.setPlaceholder(new Label("No requests"));

        updateFriendsRequestsObservableList();

        list.setItems(friendsRequests);
        list.setPrefWidth(300);
        list.setPrefHeight(200);

        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<Integer> selectedItems =  list.getSelectionModel().getSelectedIndices();

        requestsColumn.getChildren().addAll(requestsLabel, list);


        VBox buttonsColumn = new VBox();
        buttonsColumn.setAlignment(Pos.CENTER);
        Button acceptButton = new Button("Accept");
        Button ignoreButton = new Button("Ignore");
        buttonsColumn.getChildren().add(acceptButton);
        buttonsColumn.getChildren().add(ignoreButton);
        acceptButton.setOnAction(actionEvent -> acceptOrIgnoreFriendsRequest(selectedItems, true));
        ignoreButton.setOnAction(actionEvent -> acceptOrIgnoreFriendsRequest(selectedItems, false));

        row.getChildren().addAll(requestsColumn, buttonsColumn);
        row.setSpacing(20);

        return row;
    }

    protected HBox getFriendStatusRow() {
        HBox row = new HBox();
        row.setSpacing(20);
        Label friendshipLabel = new Label("Friendship:");
        row.getChildren().add(friendshipLabel);
        String friendshipStatus = getController().getFriendshipStatus(getUserProfile());
        Node node;
        switch (friendshipStatus) {
            case "friends":
                node = new Label("Already friends");
                break;
            case "sent":
                node = new Label("Friend request sent");
                break;
            case "received":
                node = new Label("Friend request received");
                break;
            default:
                Hyperlink requestFriendship = new Hyperlink("Send friend request");
                requestFriendship.setBorder(Border.EMPTY);
                requestFriendship.setPadding(new Insets(0, 0, 0, 0));
                requestFriendship.setOnAction(actionEvent -> getController().sendFriendRequest(getUserProfile()));
                node = requestFriendship;
                break;
        }
        row.getChildren().add(node);
        return row;
    }

    private void acceptOrIgnoreFriendsRequest(ObservableList<Integer> selectedRequests, boolean accept) {
        List<String> usernames = new ArrayList<>();
        for (Integer i: selectedRequests) {
            usernames.add(friendsRequests.get(i));
        }
        getController().acceptOrIgnoreFriendsRequests(usernames, accept);
        updateFriendsRequestsObservableList();
        updateFriendsObservableList();
    }

    protected void updateFriendsRequestsObservableList() {
        String json = getController().getFriendRequests();
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> requests = gson.fromJson(json, type);
        friendsRequests.clear();
        friendsRequests.addAll(FXCollections.observableArrayList(requests));
    }


}
