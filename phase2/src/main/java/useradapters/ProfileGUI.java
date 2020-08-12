package useradapters;

import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import com.google.gson.Gson;
import trademisc.RunnableGUI;
import usercomponent.UserManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileGUI implements RunnableGUI {
    private final Stage stage;
    private final TradeModel tradeModel;
    private Scene scene;
    private final ProfileController controller;
    private final int width;
    private final int height;
    private String userProfile;
    private Gson gson;
    private VBox root;
    private ObservableList<String> friends;
    private ObservableList<String> reviews;

    public ProfileGUI(Stage stage, int width, int height, TradeModel tradeModel, String userProfile) {
        this.stage = stage;
        this.tradeModel = tradeModel;
        this.controller = new ProfileController(tradeModel, "u1"); // TODO: delete username once controller constructor is changed
        this.width = width;
        this.height = height;
        this.userProfile = userProfile;
        this.root = new VBox();
        friends = FXCollections.observableArrayList();
        reviews = FXCollections.observableArrayList();
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

    protected void initializeScreen() {
        root.setSpacing(20);
        root.setPadding(new Insets(25, 25, 25, 25));

        stage.setTitle("Profile");

        // Rows
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
        reviewsColumn.getChildren().addAll(reviewsLabel, getReviewsListView());

        friendsAndReviewsRow = new HBox();
        friendsAndReviewsRow.getChildren().addAll(friendsColumn, reviewsColumn);
        friendsAndReviewsRow.setSpacing(20);

        //HBox rankHBox = getUserRankBox();

        root.getChildren().addAll(titleRow, profileInfoRow, accountStandingRow, friendsAndReviewsRow);
    }



    protected ListView<String> getFriendsListView() {
        // TODO: TESTING CODE - UNCOMMENT IF NECESSARY - DELETE LATER
        UserManager userManager = tradeModel.getUserManager();
        userManager.sendFriendRequest("u1", "u2");
        userManager.sendFriendRequest("u1", "u3");
        //userManager.sendFriendRequest("u3", "u1");
        //userManager.setFriendRequest("u1", "u2", true);
        //userManager.setFriendRequest("u3", "u1", true);

        ListView<String> list = new ListView<>();
        list.setPlaceholder(new Label("No friends"));
        updateFriendsObservableList();
        list.setItems(friends);
        list.setPrefWidth(300);
        list.setPrefHeight(200);
        return list;
    }

    protected ListView<String> getReviewsListView() {
        // TODO: TESTING CODE - UNCOMMENT IF NECESSARY - DELETE LATER
        // ReviewManager reviewManager = tradeModel.getReviewManager();
        // reviewManager.addReview(3, "Amazing guy", "3", "u1", "u2");

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
        if (controller.getFrozenStatus(userProfile)) {
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
        Label cityLabel = new Label("City: " + controller.getCity(userProfile));
        Label rankLabel = new Label("User Rank: " + controller.getRank(userProfile));
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

    protected ProfileController getController() {
        return controller;
    }

    protected void updateFriendsObservableList() {
        String json = controller.getFriends(userProfile);
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> friendsList = gson.fromJson(json, type);
        friends.clear();
        friends.addAll(friendsList);
    }

    protected void updateReviewsObservableList() {
        String json = controller.getReviews(userProfile);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
        List<Map<String, String>> reviewMaps = gson.fromJson(json, type);
        List<String> reviewStrings = new ArrayList<>();

        for (Map<String, String> reviewMap : reviewMaps) {
            String r = "Author: " + reviewMap.get("author") + "\nRating: " + reviewMap.get("rating") + "\n" + reviewMap.get("comment");
            reviewStrings.add(r);
        }
        reviews.clear();
        reviews.addAll(reviewStrings);
    }

}



