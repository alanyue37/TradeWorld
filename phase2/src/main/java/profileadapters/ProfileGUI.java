package profileadapters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import trademain.RunnableGUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileGUI implements RunnableGUI {
    private final Stage stage;
    private final TradeModel tradeModel;
    private final boolean ownProfile;
    private Scene scene;
    private final ProfileController profileController;
    private final int width;
    private final int height;
    private String userProfile;
    private VBox root;
    private ObservableList<String> friends;
    private ObservableList<String> reviews;
    private ObservableList<String> usernames;
    private HBox accountProfileContainer;
    private ComboBox<String> usernameSelector;
    private Gson gson;
    Map<String, String> profileInfo;

    /**
     * Creates an instance of ProfileGUI -- profile view for a user who is not logged in
     * Use the subclass LoggedInProfileGUI for logged in users; it displays more info and has more functionality
     * This is primaroly meant for demo users.
     * @param stage stage to display GUI on
     * @param width width of window
     * @param height height of window
     * @param tradeModel reference to TradeModel instance
     * @param ownProfile true iff looking at user is looking at own profile
     */
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
        gson = new Gson();
    }

    /**
     * Returns the parent node this GUI of which this GUI is composed
     * @return Parent node of all other nodes in this GUI
     */
    @Override
    public Parent getRoot() {
        initializeScreen();
        return root;
    }

    /**
     * Shows a new window for this GUI
     */
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

        // Rows
        HBox usernamesRow;

        // Set username selector if not own profile
        usernamesRow = getUsernamesRow();

        accountProfileContainer = getContainerForAccountProfile();

        root.getChildren().addAll(usernamesRow, accountProfileContainer);
    }

    private void setUserProfile() {
        if (ownProfile) {
            userProfile = tradeModel.getCurrentUser();
        }
        else if (!profileController.getOtherUsersWithProfiles().isEmpty()) {
            // Remains at null if no other userProfiles
            userProfile = profileController.getOtherUsersWithProfiles().get(0);
        }
        profileInfo = gson.fromJson(profileController.getProfileInfo(userProfile), new TypeToken<Map<String, String>>() {}.getType());
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
        Label titleLabel = new Label(profileInfo.get("name"));
        titleLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        Label usernameLabel = new Label("Username: " + userProfile);
        VBox titleColumn = new VBox(titleLabel, usernameLabel);
        titleRow.getChildren().add(titleColumn);

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
        Label averageRatingLabel = new Label("(Average Rating: " + profileInfo.get("averageRating") + ")");
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

    protected HBox getUsernamesRow() {
        HBox row = new HBox();
        row.setSpacing(20);
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
                profileInfo = gson.fromJson(profileController.getProfileInfo(userProfile), new TypeToken<Map<String, String>>() {}.getType());
                HBox oldAccountProfileContainer = accountProfileContainer;
                HBox newAccountProfileContainer = getContainerForAccountProfile();
                oldAccountProfileContainer.getChildren().setAll(newAccountProfileContainer.getChildren());
                }
        });
        row.getChildren().addAll(usernameSelector, viewButton);
        return row;
    }

    protected ListView<String> getFriendsListView() {
        ListView<String> list = new ListView<>();
        list.setPlaceholder(new Label("No friends"));
        list.prefWidthProperty().bind(stage.widthProperty());
        updateFriendsObservableList();
        list.setItems(friends);
        return list;
    }

    protected ListView<String> getReviewsListView() {
        updateReviewsObservableList();
        ListView<String> list = new ListView<>();
        list.setPlaceholder(new Label("No reviews"));
        list.prefWidthProperty().bind(stage.widthProperty());
        List<String> l = new ArrayList<>();
        list.setItems(reviews);
        return list;
    }

    protected HBox getAccountStandingRow() {
        HBox row = new HBox();
        Label standingLabel;
        if (profileInfo.get("frozen").equals("true")) {
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
        Label cityLabel = new Label("City: " + profileInfo.get("city"));
        Label rankLabel = new Label("User Rank: " + profileInfo.get("rank"));
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

    protected Map<String, String> getProfileInfo() {
        return profileInfo;
    }

    protected ProfileController getProfileController() {
        return profileController;
    }

    protected void updateFriendsObservableList() {
        friends.clear();
        List<String> friendsUsernames = profileController.getFriends(userProfile);
        for (String u : friendsUsernames) {
            Map<String, String> info = gson.fromJson(profileController.getProfileInfo(u), new TypeToken<Map<String, String>>() {}.getType());
            friends.add(info.get("name") + " (" + u + ")");
        }
    }

    protected void updateReviewsObservableList() {
        reviews.clear();
        reviews.addAll(profileController.getReviews(userProfile));
    }
}