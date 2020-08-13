package adminadapters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;
import undocomponent.NoLongerUndoableException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A GUI class that displays admin responsibilities to the screen and listens for the admin input.
 *
 * Used code from LifeOnTheFarm.zip in week 10 for reference.
 * Also, used https://docs.oracle.com/javafx/2/ui_controls/list-view.htm.
 */
public class AdminGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final int width;
    private final int height;
    private final AdminController controller;
    private final TradeModel model;
    private TabPane root;

    /**
     * A constructor for AdminGUI class.
     *
     * @param stage  The stage of the screen.
     * @param width  The width of the screen.
     * @param height The height of the screen.
     * @param model  The TradeModel.
     */
    public AdminGUI(Stage stage, int width, int height, TradeModel model) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.controller = new AdminController(model);
        this.model = model;

    }

    @Override
    public void initialScreen() {
        initializeScreen();
        showScreen();
    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return root;
    }

    @Override
    public void showScreen() {
        initializeScreen();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setTitle("Administration"); // Admin Responsibilities ?
        stage.show();
    }
    /**
     * This method presents a list on the screen and asks the Admin user to select an option. The selected determines
     * which method to call in the AdminGUI. This screen is displayed again until the Admin selects an option
     * or logs out.
     */
    public void initializeScreen() {      // initialize screen is the initial screen?
        stage.setTitle("Admin Menu Options");

        Text title = new Text("What would you like to do?");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        // grid.add(tabPane(), 0, 0);

        Button addNewAdmins = new Button("Add new admins");
        Button freezeUsers = new Button("Freeze users");
        Button unfreezeUsers = new Button("Unfreeze users");
        Button reviewItems = new Button("Review items");
        Button lendingThreshold = new Button("Set lending threshold");
        Button weeklyThreshold = new Button("Set weekly threshold");
        Button incompleteThreshold = new Button("Set incomplete transactions threshold");
        Button editThreshold = new Button("Set edit threshold");
        Button goldThreshold = new Button("Set gold threshold");
        Button silverThreshold = new Button("Set silver threshold");
        Button undoOperations = new Button("Undo Actions");
        Button logOut = new Button("Log Out");


        grid.add(addNewAdmins, 0, 1, 2, 1);
        grid.add(freezeUsers, 0, 2, 2, 1);
        grid.add(unfreezeUsers, 0, 3, 2, 1);
        grid.add(reviewItems, 0, 4, 2, 1);
        grid.add(lendingThreshold, 0, 5, 2, 1);
        grid.add(weeklyThreshold, 0, 6, 2, 1);
        grid.add(incompleteThreshold, 0, 7, 2, 1);
        grid.add(editThreshold, 0, 8, 2, 1);
        grid.add(goldThreshold, 0, 9, 2, 1);
        grid.add(silverThreshold, 0, 10, 2, 1);
        grid.add(undoOperations, 0, 11, 2, 1);
        grid.add(logOut, 0, 12, 2, 1);

        addNewAdmins.setOnAction(actionEvent -> addNewAdmin());
        freezeUsers.setOnAction(actionEvent -> freezeUsers());
        unfreezeUsers.setOnAction(actionEvent -> unfreezeUsers());
        reviewItems.setOnAction(actionEvent -> reviewItems());
        lendingThreshold.setOnAction(actionEvent -> setLendingThreshold());
        weeklyThreshold.setOnAction(actionEvent -> setLimitOfTransactionsThreshold());
        incompleteThreshold.setOnAction(actionEvent -> setLimitOfIncompleteTrades());
        editThreshold.setOnAction(actionEvent -> setLimitOfEdits());
        goldThreshold.setOnAction(actionEvent -> setGoldThreshold());
        silverThreshold.setOnAction(actionEvent -> setSilverThreshold());
        undoOperations.setOnAction(actionEvent -> undoOperations());
        logOut.setOnAction(actionEvent -> {
            grid.getChildren().clear();
            logOutScreen();
        });

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method informs the admin that they have logout/ exited successfully.
     */
    public void logOutScreen() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("Logged out successfully.");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 3, 1, 1);
        }
    }

    /**
     * This method allows the Admin User to create a new Admin.
     */
    public void addNewAdmin() {
        stage.setTitle("Admin User");
        Text title = new Text("Create a New Admin");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);

        Label newNameLabel = new Label("Enter Name:");
        TextField nameField = new TextField();
        Label newUsernameLabel = new Label("Enter Username:");
        TextField usernameField = new TextField();
        Label newPasswordLabel = new Label("Enter Password:");
        TextField passwordField = new TextField();
        Button createButton = new Button("Create Admin");
        HBox hBoxCreateAdmin = new HBox(10);
        hBoxCreateAdmin.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxCreateAdmin.getChildren().add(createButton);
        Button mainMenuButton = new Button("Main Menu");
        HBox hBoxMainMenu = new HBox(10);
        hBoxMainMenu.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxMainMenu.getChildren().add(mainMenuButton);

        grid.add(newNameLabel, 0, 1);
        grid.add(nameField, 0, 2);
        grid.add(newUsernameLabel, 0, 3);
        grid.add(usernameField, 0, 4);
        grid.add(newPasswordLabel, 0, 5);
        grid.add(passwordField, 0, 6);
        grid.add(hBoxCreateAdmin, 0, 7);
        grid.add(hBoxMainMenu, 0, 9);


        createButton.setOnAction(actionEvent -> {
            if (nameField.getText().isEmpty() || usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                tryAgain();
            } else if (controller.askAdminToAddNewAdmin(nameField.getText(), usernameField.getText(), passwordField.getText())) {
                newAdminCreated(usernameField.getText());
            } else {
                usernameTaken(usernameField.getText());
            }
        });
        mainMenuButton.setOnAction(action -> initializeScreen());

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method informs that Admin user that a new Admin user is created.
     *
     * @param username The username of the new admin created.
     */
    public void newAdminCreated(String username) {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("New admin account created: " + username);
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 11, 1, 1);
        }
    }

    /**
     * This method informs that Admin user that the username entered is already exists.
     *
     * @param username The username of the new admin created.
     */
    public void usernameTaken(String username) {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(username + "username is already taken.");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 12, 1, 1);
        }
    }

    /**
     * This method tells the Admin User to try again for various reasons, such as the input was an empty string.
     */
    public void tryAgain() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("Please try again!");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 10, 1, 1);
        }
    }

    /**
     * This method allows the Admin user to freeze a given list of users. The selected users will have their status
     * changed to frozen.
     */
    public void freezeUsers() {
        stage.setTitle("Admin User");
        Text title = new Text("Freeze Users");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        ListView<String> list = new ListView<>();
        ObservableList<String> freezeAccounts = FXCollections.observableArrayList();

        Set<String> flaggedAccounts = new HashSet<>();
        List<String> incompleteUsers = model.getMeetingManager().getTradesIncompleteMeetings(model.getTradeManager().getAllTypeTrades("ongoing"));
        flaggedAccounts.addAll(model.getTradeManager().getExceedIncompleteLimitUser(incompleteUsers));
        flaggedAccounts.addAll(controller.getUsersExceedWeekly());
        flaggedAccounts.addAll(model.getUserManager().getUsersForFreezing());


        freezeAccounts.addAll(flaggedAccounts);
        list.setItems(freezeAccounts);
        list.setPlaceholder(new Label("There are no accounts to be frozen"));
        Text heading = new Text("These accounts have reached the limits.\nHold down shift to freeze multiple accounts.");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 1, 1);
        list.setPrefHeight(height - 50);
        list.setPrefWidth(width - 50);


        Button freezeButton = new Button("Freeze Accounts");
        HBox freezeHBox = new HBox(10);
        freezeHBox.setAlignment(Pos.BOTTOM_RIGHT);
        freezeHBox.getChildren().add(freezeButton);
        Button mainMenuButton = new Button("Main Menu");
        HBox hBoxMainMenu = new HBox(10);
        hBoxMainMenu.setAlignment(Pos.BOTTOM_LEFT);
        hBoxMainMenu.getChildren().add(mainMenuButton);

        grid.add(heading, 0, 1);
        grid.add(list, 0, 2);
        grid.add(freezeHBox, 0, 3);
        grid.add(hBoxMainMenu, 0, 4);


        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        freezeButton.setOnAction(actionEvent -> {
            ObservableList<String> selectedItems = list.getSelectionModel().getSelectedItems();
            ArrayList<String> selected = new ArrayList<>(selectedItems);
            if (selected.isEmpty()) {
                noAccountsSelectedToFreeze();
            } else {
                controller.askAdminToFreezeUsers(selected);
                accountsSelectedToFreeze();
            }
        });
        mainMenuButton.setOnAction(actionEvent -> initializeScreen());

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method informs the Admin user that they have not selected any accounts to be frozen.
     */
    public void noAccountsSelectedToFreeze() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("No accounts have been selected to be frozen.");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    /**
     * This method informs the Admin user that the selected accounts have been frozen.
     */
    public void accountsSelectedToFreeze() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("Selected accounts have been frozen");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    /**
     * This method allows the Admin user to unfreeze a given list of Users. The selected users will have their status
     * changed to unfrozen.
     */
    public void unfreezeUsers() {
        stage.setTitle("Admin User");
        Text title = new Text("Unfreeze Users");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        ListView<String> list = new ListView<>();
        ObservableList<String> unfreezeAccounts = FXCollections.observableArrayList();

        Set<String> accounts = model.getUserManager().getUnfreezeRequests();
        unfreezeAccounts.addAll(accounts);

        list.setItems(unfreezeAccounts);
        list.setPlaceholder(new Label("There are no accounts to be unfrozen"));
        Text heading = new Text("These users have their account frozen and are requesting to unfreeze.\nHold down shift to unfreeze multiple accounts.");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(35, 35, 35, 35));
        grid.add(title, 0, 0, 1, 1);
        list.setPrefHeight(height - 50);
        list.setPrefWidth(width - 50);

        Button unfreezeButton = new Button("Unfreeze Accounts");
        HBox unfreezeHBox = new HBox(10);
        unfreezeButton.setAlignment(Pos.BOTTOM_RIGHT);
        unfreezeHBox.getChildren().add(unfreezeButton);

        Button mainMenuButton = new Button("Main Menu");
        HBox hBoxMainMenu = new HBox(10);
        hBoxMainMenu.setAlignment(Pos.BOTTOM_LEFT);
        hBoxMainMenu.getChildren().add(mainMenuButton);

        grid.add(heading, 0, 1);
        grid.add(list, 0, 2);
        grid.add(unfreezeHBox, 0, 3);
        grid.add(hBoxMainMenu, 0, 4);

        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        unfreezeButton.setOnAction(EventHandler -> {
            ObservableList<String> selectedItems = list.getSelectionModel().getSelectedItems();
            ArrayList<String> selected = new ArrayList<>(selectedItems);
            if (selected.isEmpty()) {
                noAccountsSelectedToUnfreeze();
            } else {
                controller.askAdminToUnfreezeUsers(selectedItems);
                accountsSelectedToUnfreeze();
            }
        });
        mainMenuButton.setOnAction(actionEvent -> initializeScreen());

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * This method informs the Admin user that they have not selected any accounts to be unfrozen.
     */
    public void noAccountsSelectedToUnfreeze() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("No accounts are selected to be unfrozen");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 1, 1);
        }
    }

    /**
     * This method informs the Admin user that the selected accounts have been unfrozen.
     */
    public void accountsSelectedToUnfreeze() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("Selected accounts are now unfrozen.");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 8, 1, 1);
        }
    }

    /**
     * This method allows the Admin user to select items that should be added to the system.
     */
    public void reviewItems() {
        stage.setTitle("Admin User");
        Text title = new Text("Review Items");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        ListView<String> list = new ListView<>();
        ObservableList<String> reviewItems = FXCollections.observableArrayList();

        Set<String> items = model.getItemManager().getItemsByStage("pending");
        List<String> itemsInList = new ArrayList<>(items);

        for (String itemID : items) {
            String itemInfo = model.getItemManager().getItemInfo(itemID);
            reviewItems.addAll(itemInfo);
        }
        list.setItems(reviewItems);
        list.setPlaceholder(new Label("There are no items to be reviewed."));
        Text heading = new Text("These items are pending to be added. Unselected items will be deleted.\n Hold down shift to add multiple items to the system.");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        list.setPrefHeight(height - 50); // we could change this
        list.setPrefWidth(width - 50);   // we could change this

        Button addItemsButton = new Button("Add these Items");
        HBox hBoxAddItemsButton = new HBox(10);

        hBoxAddItemsButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxAddItemsButton.getChildren().add(addItemsButton);
        Button mainMenuButton = new Button("Main Menu");
        HBox hBoxMainMenu = new HBox(10);
        hBoxMainMenu.setAlignment(Pos.BOTTOM_LEFT);
        hBoxMainMenu.getChildren().add(mainMenuButton);

        grid.add(heading, 0, 1);
        grid.add(list, 0, 2);
        grid.add(hBoxAddItemsButton, 0, 3);
        grid.add(hBoxMainMenu, 0, 4);


        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        addItemsButton.setOnAction(actionEvent -> {
            ObservableList<Integer> selectedItems = list.getSelectionModel().getSelectedIndices();
            ArrayList<Integer> conversion = new ArrayList<>(selectedItems);
            ArrayList<String> selected = new ArrayList<>();
            ArrayList<String> notSelected = new ArrayList<>();
            for (Integer itemID : conversion) {
                selected.add(itemsInList.get(itemID));
            }
            for (String itemID : itemsInList) {
                if (!selected.contains(itemID)) {
                    notSelected.add(itemID);
                }
            }
            if (!selected.isEmpty() || !notSelected.isEmpty()) {
                try {
                    controller.askAdminToReviewItems(selected, notSelected);
                } catch (NoLongerUndoableException e) {
                    e.printStackTrace();
                }
                itemReviewed();
            } else {
                tryAgain();
            }
        });
        mainMenuButton.setOnAction(actionEvent -> initializeScreen());

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method informs the Admin that the selected items have been added to the system and the items not selected
     * are deleted.
     */
    public void itemReviewed() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("Selected items have been added to the system.");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    public void noItemsSelected() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("No items are added to the system.");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    /**
     * This method allows the Admin user to set a lending threshold and prompts the Admin user again if the input is
     * invalid.
     */
    public void setLendingThreshold() {
        stage.setTitle("Admin User");
        Text title = new Text("Set Lending Threshold");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));


        int currentLimit = model.getUserManager().getThreshold("trading");
        Text text = new Text();
        text.setText("Current threshold: " + currentLimit);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        grid.add(text, 0, 3, 1, 1);
        text.setX(70);
        text.setY(70);


        Text heading = new Text("How much does the user have to (at least) lend than \nthey borrow in order to make a non-lending transaction? \nEnter a whole " +
                "number (minimum 0) for the new limit:");
        TextField lendingThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);
        Button mainMenuButton = new Button("Main Menu");
        HBox hBoxMainMenu = new HBox(10);
        hBoxMainMenu.setAlignment(Pos.BOTTOM_LEFT);
        hBoxMainMenu.getChildren().add(mainMenuButton);

        grid.add(heading, 0, 1);
        grid.add(lendingThresholdField, 0, 2);
        grid.add(hBoxSetThreshold, 0, 3);
        grid.add(hBoxMainMenu, 0, 5);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrZero(lendingThresholdField.getText())) {
                controller.askAdminToSetLendingThreshold(lendingThresholdField.getText());
                thresholdSet();
            } else {
                enterAtLeastZero();
            }
        });
        mainMenuButton.setOnAction(actionEvent -> initializeScreen());
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method allows the Admin user to set a limit for weekly transactions and prompts the Admin user again if the input is
     * invalid.
     */
    public void setLimitOfTransactionsThreshold() {
        stage.setTitle("Admin User");
        Text title = new Text("Set Transactions Threshold");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));


        int currentLimit = model.getTradeManager().getLimitTransactionPerWeek();
        Text text = new Text();
        text.setText("Current threshold: " + currentLimit);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        grid.add(text, 0, 3, 1, 1);
        text.setX(70);
        text.setY(70);

        Text heading = new Text("What is the maximum number of transactions a user \ncan conduct in a week? \nEnter a whole number (minimum 1) for the new limit:");
        TextField limitThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);
        Button mainMenuButton = new Button("Main Menu");
        HBox hBoxMainMenu = new HBox(10);
        hBoxMainMenu.setAlignment(Pos.BOTTOM_LEFT);
        hBoxMainMenu.getChildren().add(mainMenuButton);

        grid.add(heading, 0, 1);
        grid.add(limitThresholdField, 0, 2);
        grid.add(hBoxSetThreshold, 0, 3);
        grid.add(hBoxMainMenu, 0, 5);


        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrOne(limitThresholdField.getText())) {
                controller.askAdminToSetLimitOfTransactions(limitThresholdField.getText());
                thresholdSet();
            } else {
                enterAtLeastOne();
            }
        });
        mainMenuButton.setOnAction(actionEvent -> initializeScreen());
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method allows the Admin user to set a threshold for having incomplete trades and prompts the Admin user again if the input is
     * invalid.
     */
    public void setLimitOfIncompleteTrades() {
        stage.setTitle("Admin User");
        Text title = new Text("Set Incomplete Trade Threshold");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        int currentLimit = model.getTradeManager().getLimitIncomplete();
        Text text = new Text();
        text.setText("Current threshold: " + currentLimit);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        grid.add(text, 0, 3, 1, 1);
        text.setX(70);
        text.setY(70);

        Text heading = new Text("After how many incomplete transactions should a \nuser be flagged for freezing? \nEnter a whole number (minimum 1) for the new limit:");
        TextField limitThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);
        Button mainMenuButton = new Button("Main Menu");
        HBox hBoxMainMenu = new HBox(10);
        hBoxMainMenu.setAlignment(Pos.BOTTOM_LEFT);
        hBoxMainMenu.getChildren().add(mainMenuButton);

        grid.add(heading, 0, 1);
        grid.add(limitThresholdField, 0, 2);
        grid.add(hBoxSetThreshold, 0, 3);
        grid.add(hBoxMainMenu, 0, 5);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrOne(limitThresholdField.getText())) {
                controller.askAdminToSetLimitOfIncompleteTrades(limitThresholdField.getText());
                thresholdSet();
            } else {
                enterAtLeastOne();
            }

        });
        mainMenuButton.setOnAction(actionEvent -> initializeScreen());
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method allows the Admin user to set a threshold for edits and prompts the Admin user again if the input is
     * invalid.
     */
    public void setLimitOfEdits() {
        stage.setTitle("Admin User");
        Text title = new Text("Set Edit Threshold");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        int currentLimit = model.getMeetingManager().getLimitEdits();
        Text text = new Text();
        text.setText("Current threshold: " + currentLimit);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        grid.add(text, 0, 3, 1, 1);
        text.setX(70);
        text.setY(70);

        Text heading = new Text("What is the maximum number of times the proposed \nmeeting time for a trade can be edited? \nEnter a whole number (minimum 0) for the new limit:");
        TextField limitThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);
        Button mainMenuButton = new Button("Main Menu");
        HBox hBoxMainMenu = new HBox(10);
        hBoxMainMenu.setAlignment(Pos.BOTTOM_LEFT);
        hBoxMainMenu.getChildren().add(mainMenuButton);

        grid.add(heading, 0, 1);
        grid.add(limitThresholdField, 0, 2);
        grid.add(hBoxSetThreshold, 0, 3);
        grid.add(hBoxMainMenu, 0, 5);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrZero(limitThresholdField.getText())) {
                controller.askAdminToSetLimitOfEdits(limitThresholdField.getText());
                thresholdSet();
            } else {
                enterAtLeastZero();
            }
        });
        mainMenuButton.setOnAction(actionEvent -> initializeScreen());
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void setGoldThreshold() {
        stage.setTitle("Admin User");
        Text title = new Text("Set Gold Threshold");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        int currentLimit = model.getUserManager().getThreshold("gold");
        Text text = new Text();
        text.setText("Current threshold: " + currentLimit);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        grid.add(text, 0, 3, 1, 1);
        text.setX(70);
        text.setY(70);

        Text heading = new Text("What is the credit limit that ranks users gold? \nEnter a whole number (minimum 0) for the new limit:");
        TextField limitThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);
        Button mainMenuButton = new Button("Main Menu");
        HBox hBoxMainMenu = new HBox(10);
        hBoxMainMenu.setAlignment(Pos.BOTTOM_LEFT);
        hBoxMainMenu.getChildren().add(mainMenuButton);

        grid.add(heading, 0, 1);
        grid.add(limitThresholdField, 0, 2);
        grid.add(hBoxSetThreshold, 0, 3);
        grid.add(hBoxMainMenu, 0, 5);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrZero(limitThresholdField.getText())) {
                controller.setGoldThreshold(limitThresholdField.getText());
                thresholdSet();
            } else {
                enterAtLeastZero();
            }
        });
        mainMenuButton.setOnAction(actionEvent -> initializeScreen());

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void setSilverThreshold() {
        stage.setTitle("Admin User");
        Text title = new Text("Set Silver Threshold");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        int currentLimit = model.getUserManager().getThreshold("silver");
        Text text = new Text();
        text.setText("Current threshold: " + currentLimit);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        grid.add(text, 0, 3, 1, 1);
        text.setX(70);
        text.setY(70);

        Text heading = new Text("What is the credit limit that ranks users silver? \nEnter a whole number (minimum 0) for the new limit:");
        TextField limitThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);
        Button mainMenuButton = new Button("Main Menu");
        HBox hBoxMainMenu = new HBox(10);
        hBoxMainMenu.setAlignment(Pos.BOTTOM_LEFT);
        hBoxMainMenu.getChildren().add(mainMenuButton);

        grid.add(heading, 0, 1);
        grid.add(limitThresholdField, 0, 2);
        grid.add(hBoxSetThreshold, 0, 3);
        grid.add(hBoxMainMenu, 0, 5);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrZero(limitThresholdField.getText())) {
                controller.setSilverThreshold(limitThresholdField.getText());
                thresholdSet();
            } else {
                enterAtLeastZero();
            }
        });
        mainMenuButton.setOnAction(actionEvent -> initializeScreen());
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method informs that Admin user that the number entered to set the threshold is not an integer or zero.
     */
    public void enterAtLeastZero() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("Not an integer or is a number less than zero. \nPlease try again");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 8, 2, 1);
        }
    }

    /**
     * This method informs that Admin user that the number entered to set the threshold is not an integer or one.
     */
    public void enterAtLeastOne() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("Not an integer or is a number less than one. \nPlease try again");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 8, 2, 1);
        }
    }

    /**
     * This method informs that Admin user that the threshold has been set.
     */
    public void thresholdSet() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("Threshold is set.");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    /**
     *
     */
    public void undoOperations() {
        stage.setTitle("Admin User");
        Text title = new Text("Undo Operations");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        ListView<String> list = new ListView<>();

        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<String> reviewItems = FXCollections.observableArrayList();
        Text heading = new Text("The following are outstanding undo actions. \nHold down shift to undo multiple actions.");

        // Set<String> items = model.getItemManager().getItemsByStage("pending");
        List<String> undoOperations = new ArrayList<>(controller.undoOperationsString());

        reviewItems.addAll(undoOperations);
        list.setItems(reviewItems);
        list.setPlaceholder(new Label("There are no undo operations."));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        // grid.getChildren().add(list);
        list.setPrefHeight(height - 50); // we could change this
        list.setPrefWidth(width - 50);   // we could change this

        Button confirmToUndo = new Button("Confirm to Undo");
        HBox hBoxConfirmToUndo = new HBox(10);

        hBoxConfirmToUndo.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxConfirmToUndo.getChildren().add(confirmToUndo);
        Button mainMenuButton = new Button("Main Menu");
        HBox hBoxMainMenu = new HBox(10);
        hBoxMainMenu.setAlignment(Pos.BOTTOM_LEFT);
        hBoxMainMenu.getChildren().add(mainMenuButton);

        grid.add(heading, 0, 1);
        grid.add(list, 0, 2);
        grid.add(hBoxConfirmToUndo, 0, 4);
        grid.add(hBoxMainMenu, 0, 5);

        confirmToUndo.setOnAction(actionEvent -> {
            ObservableList<String> selectedActions = list.getSelectionModel().getSelectedItems();
            ArrayList<String> selected = new ArrayList<>(selectedActions);
            if (!selected.isEmpty()) {
                try {
                    controller.undoOperations();
                } catch (NoLongerUndoableException e) {
                    e.printStackTrace();
                }
            } else {
                noActionsSelected();
            }
        });
        mainMenuButton.setOnAction(actionEvent -> initializeScreen());

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void noActionsSelected() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("No undo actions selected.");
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }
}
