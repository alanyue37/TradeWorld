package adminadapters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import tradegateway.TradeModel;

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
public class AdminGUI {
    private final Stage stage;
    private Scene scene;
    private final int width;
    private final int height;
    private final AdminPresenter presenter;
    private final AdminController controller;
    private final TradeModel model;

    // TODO: I still have to write java docs for this class and change method signatures in AdminController.

    /**
     * A constructor for AdminGUI class.
     * @param stage The stage of the screen.
     * @param width The width of the screen.
     * @param height    The height of the screen.
     * @param presenter The AdminPresenter.
     * @param controller    The AdminController.
     * @param model The TradeModel.
     */
    public AdminGUI(Stage stage, int width, int height, AdminPresenter presenter, AdminController controller, TradeModel model) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.presenter = presenter;
        this.controller = controller;
        this.model = model;
    }

    /**
     * This method presents a list on the screen and asks the Admin user to select one. The selected determines
     * which method to call in the AdminGUI. This screen is displayed again, if the Admin does not select an
     * option.
     */
    public void adminUserInitialScreen() {
        stage.setTitle("Admin Menu Options");

        Text title = new Text("What would you like to do?");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        ListView<String> list = new ListView<>();
        list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<String> options = FXCollections.observableArrayList(presenter.startMenu());

        list.setItems(options);

        Button goButton = new Button("Go");
        HBox hBoxGo = new HBox(10);
        hBoxGo.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxGo.getChildren().add(goButton);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        grid.getChildren().add(list);
        list.setPrefHeight(height - 50); // we could change this
        list.setPrefWidth(width - 50);   // we could change this
        grid.add(hBoxGo, 0, 1);

        ObservableList<Integer> selectedItem =  list.getSelectionModel().getSelectedIndices();
        ArrayList<Integer> conversion = new ArrayList<>(selectedItem);
        String selected = conversion.toString();

        // If the group suggests, I can also have these options presented in buttons (just like in UserGUI).

        goButton.setOnAction(actionEvent -> {
            switch (selected) {
                case "0": {
                    addNewAdmin();
                    break;
                }
                case "1": {
                    freezeUsers();
                    break;
                }
                case "2": {
                    unfreezeUsers();
                    break;
                }
                case "3": {
                    reviewItems();
                    break;
                }
                case "4": {
                    setLendingThreshold();
                    break;
                }
                case "5": {
                    setLimitOfTransactionsThreshold();
                    break;
                }
                case "6": {
                    setLimitOfIncompleteTrades();
                    break;
                }
                case "7": {
                    setLimitOfEdits();
                    break;
                } default: {
                    tryAgain();
                    adminUserInitialScreen();
                }
            }
        });
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
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

        Label newNameLabel = new Label(presenter.accountEnterName());
        TextField nameField = new TextField();
        Label newUsernameLabel = new Label(presenter.accountEnterUsername());
        TextField usernameField = new TextField();
        Label newPasswordLabel = new Label(presenter.accountEnterPassword());
        TextField passwordField = new TextField();
        Button createButton = new Button("Create Admin");
        HBox hBoxCreateAdmin = new HBox(10);
        hBoxCreateAdmin.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxCreateAdmin.getChildren().add(createButton);

        grid.add(newNameLabel, 0, 1);
        grid.add(nameField, 0, 1);
        grid.add(newUsernameLabel, 0, 1);
        grid.add(usernameField, 0, 1);
        grid.add(newPasswordLabel, 0, 1);
        grid.add(passwordField, 0, 1);
        grid.add(hBoxCreateAdmin, 0, 1);
        
        createButton.setOnAction(actionEvent -> {
            if (controller.askAdminToAddNewAdmin(nameField.getText(), usernameField.getText(), passwordField.getText())) {
                newAdminCreated(usernameField.getText());
            } else if (nameField.getText().equals("") || usernameField.getText().equals("") || passwordField.getText().equals("")) {
                tryAgain();
            } else {
                usernameTaken(usernameField.getText());
            }
        });
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method informs that Admin user that a new Admin user is created.
     * @param username The username of the new admin created.
     */
    public void newAdminCreated(String username) {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(presenter.newAccountCreated(username));
        if (!grid.getChildren().contains(message)){
            grid.add(message, 0, 6, 2, 1);
        }
    }

    /**
     * This method informs that Admin user that the username entered is already exists.
     * @param username The username of the new admin created.
     */
    public void usernameTaken(String username) {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(presenter.usernameTaken(username));
        if (!grid.getChildren().contains(message)){
            grid.add(message, 0, 6, 2, 1);
        }
    }

    /**
     * This method tells the Admin User to try again for various reasons, such as the input was an empty string.
     */
    public void tryAgain() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("Please try again!");
        if (!grid.getChildren().contains(message)){
            grid.add(message, 0, 6, 2, 1);
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

        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<String> freezeAccounts = FXCollections.observableArrayList();

        Set<String> flaggedAccounts = new HashSet<>();
        List<String> incompleteUsers = model.getMeetingManager().getTradesIncompleteMeetings(model.getTradeManager().getAllTypeTrades("ongoing"));
        flaggedAccounts.addAll(model.getTradeManager().getExceedIncompleteLimitUser(incompleteUsers));
        flaggedAccounts.addAll(controller.getUsersExceedWeekly());
        flaggedAccounts.addAll(model.getUserManager().getUsersForFreezing());


        freezeAccounts.addAll(flaggedAccounts);
        list.setItems(freezeAccounts);
        list.setPlaceholder(new Label("There are no accounts to be frozen"));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        grid.getChildren().add(list);
        list.setPrefHeight(height - 50); // we could change this
        list.setPrefWidth(width - 50);   // we could change this


        Button freezeButton = new Button("Freeze these Accounts");
        HBox freezeHBox = new HBox(10);

        freezeButton.setAlignment(Pos.BOTTOM_RIGHT);
        freezeHBox.getChildren().add(freezeButton);

        grid.add(freezeHBox, 0, 1);

        ObservableList<String> selectedItems =  list.getSelectionModel().getSelectedItems();
        ArrayList<String> selected = new ArrayList<>(selectedItems);

        freezeButton.setOnAction(actionEvent -> {
            if (selected.isEmpty()) {
                noAccountsSelectedToFreeze();
            } else {
                accountsSelectedToFreeze();
            }
        });
        scene = new Scene(grid, 300, 120);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method informs the Admin user that they have not selected any accounts to be frozen.
     */
    public void noAccountsSelectedToFreeze() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(presenter.freezeAccountsHeading(false));
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    /**
     * This method informs the Admin user that the selected accounts have been frozen.
     */
    public void accountsSelectedToFreeze() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(presenter.freezeAccountsHeading(true));
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

        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<String> unfreezeAccounts = FXCollections.observableArrayList();

        Set<String> accounts = model.getUserManager().getUnfreezeRequests();
        unfreezeAccounts.addAll(accounts);
        list.setItems(unfreezeAccounts);
        list.setPlaceholder(new Label("There are no accounts to be unfrozen"));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        grid.getChildren().add(list);
        list.setPrefHeight(height - 50); // we could change this
        list.setPrefWidth(width - 50);   // we could change this

        Button unfreezeButton = new Button("Unfreeze these Accounts");
        HBox unfreezeHBox = new HBox(10);

        unfreezeButton.setAlignment(Pos.BOTTOM_RIGHT);
        unfreezeHBox.getChildren().add(unfreezeButton);

        grid.add(unfreezeHBox, 0, 1);

        ObservableList<String> selectedItems =  list.getSelectionModel().getSelectedItems();
        ArrayList<String> selected = new ArrayList<>(selectedItems);
        unfreezeButton.setOnAction(actionEvent -> {
            if (selected.isEmpty()) {
                noAccountsSelectedToUnfreeze();
            } else {
                controller.askAdminToUnfreezeUsers(selected);
                accountsSelectedToUnfreeze();
            }
        });
        scene = new Scene(grid, 300, 120);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method informs the Admin user that they have not selected any accounts to be unfrozen.
     */
    public void noAccountsSelectedToUnfreeze() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(presenter.unfreezeAccountsHeading(false));
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    /**
     * This method informs the Admin user that the selected accounts have been unfrozen.
     */
    public void accountsSelectedToUnfreeze() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(presenter.unfreezeAccountsHeading(true));
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
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

        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<String> reviewItems =  FXCollections.observableArrayList();

        Set<String> items = model.getItemManager().getItemsByStage("pending");
        List<String> itemsInList = new ArrayList<>(items);

        for(String itemID : items) {
            String itemInfo = model.getItemManager().getItemInfo(itemID);
            reviewItems.addAll(itemInfo);
        }
        list.setItems(reviewItems);
        list.setPlaceholder(new Label ("There are no items to be reviewed."));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        grid.getChildren().add(list);
        list.setPrefHeight(height - 50); // we could change this
        list.setPrefWidth(width - 50);   // we could change this

        Button addItemsButton = new Button("Add these Items");
        HBox hBoxAddItemsButton = new HBox(10);

        hBoxAddItemsButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxAddItemsButton.getChildren().add(addItemsButton);

        grid.add(hBoxAddItemsButton, 1, 4);

        ObservableList<Integer> selectedItems =  list.getSelectionModel().getSelectedIndices();
        ArrayList<Integer> conversion = new ArrayList<>(selectedItems);
        ArrayList<String> selected = new ArrayList<>();
        ArrayList<String> notSelected = new ArrayList<>();

        for(Integer itemNum : conversion) {
            selected.add(itemsInList.get(itemNum));
        }

        for (String item : itemsInList) {
            if (!selected.contains(item)) {
                notSelected.add(item);
            }
        }

        addItemsButton.setOnAction(actionEvent -> {
            controller.askAdminToReviewItems(selected, notSelected);
            itemReviewed();
        });
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
        Text message = new Text(presenter.reviewItemsHeading(true));
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

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);

        Label lendingThresholdLabel = new Label(presenter.lendingThreshold());
        TextField lendingThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);

        grid.add(lendingThresholdLabel, 0, 1);
        grid.add(lendingThresholdField, 0, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrZero(lendingThresholdField.getText())) {
                controller.askAdminToSetLendingThreshold(lendingThresholdField.getText());
                thresholdSet();
            } else {
                enterAtLeastZero();
                setLendingThreshold();
            }
        });
        scene = new Scene(grid, 300, 120);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method allows the Admin user to set a limit for weekly transactions and prompts the Admin user again if the input is
     * invalid.
     */
    public void setLimitOfTransactionsThreshold() {
        stage.setTitle("Admin User");
        Text title = new Text("Set A Limit for Transactions");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);

        Label limitThresholdLabel = new Label(presenter.lendingThreshold());
        TextField limitThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);

        grid.add(limitThresholdLabel, 0, 1);
        grid.add(limitThresholdField, 0, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrOne(limitThresholdField.getText())) {
                controller.askAdminToSetLimitOfTransactions(limitThresholdField.getText());
                thresholdSet();
            } else {
                enterAtLeastOne();
                setLimitOfTransactionsThreshold();
            }
        });
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
        Text title = new Text("Set Lending Threshold");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);

        Label limitThresholdLabel = new Label(presenter.lendingThreshold());
        TextField limitThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);

        grid.add(limitThresholdLabel, 0, 1);
        grid.add(limitThresholdField, 0, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrOne(limitThresholdField.getText())) {
                controller.askAdminToSetLimitOfIncompleteTrades(limitThresholdField.getText());
                thresholdSet();
            } else {
                enterAtLeastOne();
                setLimitOfIncompleteTrades();
            }

        });
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
        Text title = new Text("Set Threshold");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);

        Label limitThresholdLabel = new Label(presenter.lendingThreshold());
        TextField limitThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);

        grid.add(limitThresholdLabel, 0, 1);
        grid.add(limitThresholdField, 0, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrZero(limitThresholdField.getText())) {
                controller.askAdminToSetLimitOfEdits(limitThresholdField.getText());
                thresholdSet();
            } else {
                enterAtLeastZero();
                setLimitOfEdits();
            }
        });
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method informs that Admin user that the number entered to set the threshold is not an integer or zero.
     */
    public void enterAtLeastZero() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(presenter.enterAtLeastZero());
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    /**
     * This method informs that Admin user that the number entered to set the threshold is not an integer or one.
     */
    public void enterAtLeastOne() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(presenter.enterAtLeastOne());
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    /**
     * This method informs that Admin user that the threshold has been set.
     */
    public void thresholdSet() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(presenter.confirmationOfThreshold());
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }
}
