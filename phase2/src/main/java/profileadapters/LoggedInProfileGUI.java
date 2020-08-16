package profileadapters;

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

    /**
     * Creates an instance of LoggedInProfileGUI -- profile view for a user who is logged in
     * @param stage stage to show GUI on
     * @param width width of window
     * @param height height of window
     * @param tradeModel reference to TradeModel instance
     * @param ownProfile true iff looking at user is looking at own profile
     */
    public LoggedInProfileGUI(Stage stage, int width, int height, TradeModel tradeModel, boolean ownProfile) {
        super(stage, width, height, tradeModel, ownProfile);
        friendsRequests = FXCollections.observableArrayList();
    }


    @Override
    protected HBox getContainerForAccountProfile() {
        HBox row;
        row = super.getContainerForAccountProfile();
        if (noUsers()) {
            return row;
        }

        VBox container = new VBox();
        // Additional rows
        HBox statusesRow;
        HBox friendsRow;

        row.setSpacing(20);
        container.setSpacing(20);

        // Set statusesRow
        // vacation + public/private
        statusesRow = getStatusesRow();

        if (getProfileController().isOwnProfile(getUserProfile())) {
            // Add friends requests list row if own profile
            friendsRow = getFriendsRequestsRow();
        }
        else {
            // Add friend status / send friend request button if not own profile
            friendsRow = getFriendStatusRow();
        }

        container.getChildren().addAll(row, statusesRow, friendsRow);
        return new HBox(container);
    }

    @Override
    protected HBox getUsernamesRow() {
        // Only return if not own profile
        if (!getProfileController().isOwnProfile(getUserProfile())) {
            return super.getUsernamesRow();
        }
        return new HBox();
    }

    @Override
    protected HBox getAccountStandingRow() {
        HBox row =  super.getAccountStandingRow();
        // If frozen and own profile add Request Unfreeze link
        if (getProfileInfo().get("frozen").equals("true") && getProfileController().isOwnProfile(getUserProfile())) {
            Hyperlink requestUnfreeze = new Hyperlink("Request unfreeze");
            requestUnfreeze.setBorder(Border.EMPTY);
            requestUnfreeze.setPadding(new Insets(0, 0, 0, 20));
            requestUnfreeze.setOnAction(actionEvent -> getProfileController().requestUnfreeze());
            row.getChildren().add(requestUnfreeze);
        }
        return row;
    }

    protected HBox getVacationStatusRow() {
        HBox vacationRow = new HBox();
        Label vacationLabel = new Label("Vacation Mode:\t");
        Label vacationValueLabel;
        ToggleGroup vacationGroup = new ToggleGroup();
        RadioButton onVacationButton = new RadioButton("On");
        RadioButton offVacationButton = new RadioButton("Off");
        onVacationButton.setUserData(true);
        offVacationButton.setUserData(false);
        onVacationButton.setToggleGroup(vacationGroup);
        offVacationButton.setToggleGroup(vacationGroup);
        if (getProfileInfo().get("vacation").equals("true")) {
            vacationValueLabel = new Label("On");
            onVacationButton.setSelected(true);
        }
        else {
            vacationValueLabel = new Label("Off");
            offVacationButton.setSelected(true);
        }

        // Add event handlers for radio buttons
        // Based on sample code from: https://docs.oracle.com/javafx/2/ui_controls/radio-button.htm

        vacationGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (vacationGroup.getSelectedToggle() != null) {
                    boolean vacation = (boolean) vacationGroup.getSelectedToggle().getUserData();
                    getProfileController().setVacationMode(vacation);}

            }
        });

        if (getProfileController().isOwnProfile(getUserProfile())) {
            vacationRow.getChildren().addAll(vacationLabel, offVacationButton, onVacationButton);

        }
        else {
            vacationRow.getChildren().addAll(vacationLabel, vacationValueLabel);
        }

        vacationRow.setSpacing(20);
        return vacationRow;
    }

    protected HBox getFriendsRequestsRow() {
        HBox row = new HBox();
        VBox requestsColumn = new VBox();
        Label requestsLabel = new Label("Friend Requests");
        ListView<String> list = new ListView<>();
        list.setPlaceholder(new Label("No requests"));

        updateFriendsRequestsObservableList();

        list.setItems(friendsRequests);
        list.prefWidthProperty().bind(getStage().widthProperty().divide(2.2));

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
        String friendshipStatus = getProfileController().getFriendshipStatus(getUserProfile());
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
                requestFriendship.setOnAction(actionEvent -> getProfileController().sendFriendRequest(getUserProfile()));
                node = requestFriendship;
                break;
        }
        row.getChildren().add(node);
        return row;
    }

    protected void updateFriendsRequestsObservableList() {
        String json = getProfileController().getFriendRequests();
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> requests = gson.fromJson(json, type);
        friendsRequests.clear();
        friendsRequests.addAll(FXCollections.observableArrayList(requests));
    }

    private HBox getStatusesRow() {
        HBox row = new HBox();
        VBox statusesColumn = new VBox();

        HBox vacationRow = getVacationStatusRow();
        HBox privacyRow = getPrivacyRow();

        statusesColumn.getChildren().addAll(vacationRow, privacyRow);

        row.getChildren().add(statusesColumn);
        return row;
    }

    private HBox getPrivacyRow() {
        HBox privacyRow = new HBox();
        Label privacyLabel = new Label("Private Mode:\t\t");
        Label privacyValueLabel;
        ToggleGroup privacyGroup = new ToggleGroup();
        RadioButton onPrivacyButton = new RadioButton("On");
        RadioButton offPrivacyButton = new RadioButton("Off");
        onPrivacyButton.setUserData(true);
        offPrivacyButton.setUserData(false);

        onPrivacyButton.setToggleGroup(privacyGroup);
        offPrivacyButton.setToggleGroup(privacyGroup);

        if (getProfileInfo().get("private").equals("true")) {
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
                    getProfileController().setPrivacyMode(privacy);
                }

            }
        });

        if (getProfileController().isOwnProfile(getUserProfile())) {
            privacyRow.getChildren().addAll(privacyLabel, offPrivacyButton, onPrivacyButton);

        }
        else {
            privacyRow.getChildren().addAll(privacyLabel, privacyValueLabel);
        }

        privacyRow.setSpacing(20);
        return privacyRow;
    }

    private void acceptOrIgnoreFriendsRequest(ObservableList<Integer> selectedRequests, boolean accept) {
        List<String> usernames = new ArrayList<>();
        for (Integer i: selectedRequests) {
            usernames.add(friendsRequests.get(i));
        }
        getProfileController().acceptOrIgnoreFriendsRequests(usernames, accept);
        updateFriendsRequestsObservableList();
        updateFriendsObservableList();
    }
}