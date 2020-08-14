package useradapters;

import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import com.google.gson.Gson;
import trademisc.RunnableGUI;
import usercomponent.ReviewManager;
import usercomponent.UserManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class ProfileGUI implements RunnableGUI {
    private final Stage stage;
    private final TradeModel tradeModel;
    private final boolean ownProfile;
    private Scene scene;
    private final ProfileController profileController;
    private final int width;
    private final int height;
    private String userProfile;
    private Gson gson;
    private VBox root;
    private ObservableList<String> friends;
    private ObservableList<String> reviews;
    private ObservableList<String> usernames;
    private HBox accountProfileContainer;
    private ComboBox<String> usernameSelector;

    public ProfileGUI(Stage stage, int width, int height, TradeModel tradeModel, boolean ownProfile) {
        this.stage = stage;
        this.tradeModel = tradeModel;
        this.profileController = new ProfileController(tradeModel);
        this.width = width;
        this.height = height;
        this.ownProfile = ownProfile;
        this.userProfile = null;
        friends = FXCollections.observableArrayList();
        reviews = FXCollections.observableArrayList();
        usernames = FXCollections.observableArrayList();
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

    public void initializeScreen() {
        setUserProfile();
        root = new VBox();
        root.setSpacing(20);
        root.setPadding(new Insets(25, 25, 25, 25));

        stage.setTitle("Profile");

        // Rows
        HBox usernamesRow;
        //HBox accountProfileContainer;

        // Set username selector if not own profile
        usernamesRow = getUsernamesRow();

        accountProfileContainer = getContainerForAccountProfile();


        //HBox rankHBox = getUserRankBox();

        root.getChildren().addAll(usernamesRow, accountProfileContainer);
    }

    private void setUserProfile() {
        if (ownProfile) {
            userProfile = tradeModel.getCurrentUser();
        }
        else if (profileController.getOtherUsersWithProfiles().isEmpty()) {
            // Remains at null if no other userProfiles
            userProfile = profileController.getOtherUsersWithProfiles().get(0);
        }
    }

    protected boolean noUsers() {
        return userProfile == null;
    }

    protected HBox getContainerForAccountProfile() {
        HBox row = new HBox();

        if (noUsers()) {
            Label noUsersLabel = new Label("No user profiles to show");
            row.getChildren().add(noUsersLabel);
            return row;
        }

        VBox container = new VBox();
        row.setSpacing(20);
        container.setSpacing(20);
        HBox titleRow;
        HBox profileInfoRow;
        HBox accountStandingRow;
        HBox friendsAndReviewsRow;


        // Set title
        titleRow = new HBox();
        Label titleLabel = new Label(userProfile + "'s Profile");
        titleLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        titleRow.getChildren().add(titleLabel);

        // Set profileInfoRow
        // City + rank
        profileInfoRow = getProfileInfoRow();

        // Set accountStandingRow (frozen)
        accountStandingRow = getAccountStandingRow();

        // Set friendsAndReviewsRow
        // friends + reviews + labels
        VBox friendsColumn = new VBox();
        Label friendsLabel = new Label("Friends");
        friendsColumn.getChildren().addAll(friendsLabel, getFriendsListView());

        VBox reviewsColumn = new VBox();
        Label reviewsLabel = new Label("Reviews");
        Label averageRatingLabel = new Label("(Average Rating: " + profileController.getAverageRating(userProfile) + ")");
        averageRatingLabel.setAlignment(Pos.BOTTOM_RIGHT);
        HBox reviewsLabelsRow = new HBox(reviewsLabel, averageRatingLabel);

        reviewsLabelsRow.setSpacing(20);
        reviewsColumn.getChildren().addAll(reviewsLabelsRow, getReviewsListView());

        friendsAndReviewsRow = new HBox();
        friendsAndReviewsRow.getChildren().addAll(friendsColumn, reviewsColumn);
        friendsAndReviewsRow.setSpacing(20);

        container.getChildren().addAll(titleRow, profileInfoRow, accountStandingRow, friendsAndReviewsRow);
        row.getChildren().add(container);
        return row;

    }

    protected void updateScreen() {
        userProfile = usernameSelector.getSelectionModel().getSelectedItem();
        //initializeScreen();
        HBox oldAccountProfileContainer = accountProfileContainer;
        HBox newAccountProfileContainer = getContainerForAccountProfile();
        oldAccountProfileContainer.getChildren().setAll(newAccountProfileContainer.getChildren());
    }

    protected HBox getUsernamesRow() {
        HBox row = new HBox();
        usernames = FXCollections.observableArrayList(profileController.getOtherUsersWithProfiles());
        usernameSelector = new ComboBox(usernames);
        if (!noUsers()) {
            usernameSelector.getSelectionModel().select(userProfile);
        }
        usernameSelector.setPlaceholder(new Label("No other profiles to view"));
        Button viewButton = new Button("View Profile");
        viewButton.setOnAction(actionEvent -> {
            if (!usernames.isEmpty()) {
                userProfile = usernameSelector.getSelectionModel().getSelectedItem();
                //initializeScreen();
                HBox oldAccountProfileContainer = accountProfileContainer;
                HBox newAccountProfileContainer = getContainerForAccountProfile();
                oldAccountProfileContainer.getChildren().setAll(newAccountProfileContainer.getChildren());
                }
            else {
                System.out.println("empty");
            }
        });
        row.getChildren().addAll(usernameSelector, viewButton);
        return row;
    }

    protected ListView<String> getFriendsListView() {
        ListView<String> list = new ListView<>();
        list.setPlaceholder(new Label("No friends"));
        updateFriendsObservableList();
        list.setItems(friends);
        list.setPrefWidth(300);
        list.setPrefHeight(200);
        return list;
    }

    protected ListView<String> getReviewsListView() {
        updateReviewsObservableList();
        ListView<String> list = new ListView<>();
        list.setPlaceholder(new Label("No reviews"));
        List<String> l = new ArrayList<>();
        list.setItems(reviews);
        list.setPrefWidth(300);
        list.setPrefHeight(200);
        return list;
    }

    protected HBox getAccountStandingRow() {
        HBox row = new HBox();
        Label standingLabel;
        if (profileController.getFrozenStatus(userProfile)) {
            standingLabel = new Label("Account Standing: Frozen");
        }
        else {
            standingLabel = new Label("Account Standing: Good");
        }
        row.getChildren().add(standingLabel);
        return row;
    }

    protected HBox getProfileInfoRow() {
        HBox row = new HBox();
        VBox profileInfoColumn = new VBox();
        Label cityLabel = new Label("City: " + profileController.getCity(userProfile));
        Label rankLabel = new Label("User Rank: " + profileController.getRank(userProfile));
        profileInfoColumn.getChildren().addAll(cityLabel, rankLabel);
        row.getChildren().add(profileInfoColumn);
        return row;
    }

    protected Stage getStage() {
        return stage;
    }


    protected int getWidth() {
        return width;
    }

    protected int getHeight() {
        return height;
    }

    protected String getUserProfile() {
        return userProfile;
    }

    protected void addRow(HBox row) {
        root.getChildren().add(row);
    }

    protected ProfileController getProfileController() {
        return profileController;
    }

    protected void updateFriendsObservableList() {
        friends.clear();
        friends.addAll(profileController.getFriends(userProfile));
    }

    protected void updateReviewsObservableList() {
        reviews.clear();
        reviews.addAll(profileController.getReviews(userProfile));
    }

}



