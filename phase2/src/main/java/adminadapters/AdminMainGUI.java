package adminadapters;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import tradegateway.GUIObserver;
import tradegateway.TradeModel;
import trademisc.MainGUI;
import trademisc.RunnableGUI;
import undocomponent.NoLongerUndoableException;
import useradapters.TableViewCreator;

import java.util.*;


/**
 * A GUI class that displays admin responsibilities to the screen and listens for the admin input.
 *
 * Used code from LifeOnTheFarm.zip in week 10 for reference.
 * Also, used https://docs.oracle.com/javafx/2/ui_controls/list-view.htm.
 */
public class AdminMainGUI extends MainGUI implements RunnableGUI, GUIObserver {
    private Stage stage;
    private Scene scene;
    private final int width;
    private final int height;
    private final AdminController controller;
    private final TradeModel model;
    private final TabPane root;
    private TabPane subRoot;

    /**
     * A constructor for AdminGUI class.
     * @param width The width of the screen.
     * @param height    The height of the screen.
     * @param model The TradeModel.
     */
    public AdminMainGUI(int width, int height, TradeModel model) {
        super(width, height, model);
        this.width = width;
        this.height = height;
        this.controller = new AdminController(model);
        this.model = model;
        this.root = new TabPane();
        this.subRoot = new TabPane();
    }

    /**
     * This is an Admin menu in the TabPane which provides options.
     */
    public void initializeScreen() {
        getTradeModel().addObserver(this);
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Parent newAdminParent = addNewAdmin();
        Tab addAdminTab = new Tab("Add Admin", newAdminParent);

        Parent freezeParent = freezeUsers();
        Tab freezeUsersTab = new Tab("Freeze Users", freezeParent);

        Parent unfreezeParent = unfreezeUsers();
        Tab unfreezeUsersTab = new Tab("Unfreeze Users", unfreezeParent);

        Parent reviewItemsTabParent = reviewItems();
        Tab reviewItemsTab = new Tab("Review Items", reviewItemsTabParent);

        Parent thresholdsTabParent = setThresholdMenu();
        Tab thresholdsTab = new Tab("Set Thresholds", thresholdsTabParent);

        Parent undoActionsParent = undoActions();
        Tab undoActionsTab = new Tab("Undo Actions", undoActionsParent);

        root.getTabs().addAll(addAdminTab, freezeUsersTab, unfreezeUsersTab, reviewItemsTab, thresholdsTab, undoActionsTab);

        // Listener code below based on https://stackoverflow.com/questions/17522686/javafx-tabpane-how-to-listen-to-selection-changes
        root.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab) {
                        /*addAdminTab.setContent(addNewAdmin());
                        freezeUsersTab.setContent(freezeUsers());
                        unfreezeUsersTab.setContent(unfreezeUsers());
                        reviewItemsTab.setContent(reviewItems());
                        thresholdsTab.setContent(setThresholdMenu());
                        undoActionsTab.setContent(undoActions());*/


                    }
                }
        );
    }

    /**
     * This is the initial screen. TODO: SHOULD BE DELETED?
     */
    @Override
    public void initialScreen() {
    }

    /**
     * Returns the TabPane consisting of the Admin menu options.
     * @return Returns the TabPane consisting of Admin menu options.
     */
    @Override
    public Parent getRoot() {
        initializeScreen();
        return root;
    }

    /**
     * This method sets up the screen.
     */
    @Override
    public void showScreen() {
        initializeScreen();
        scene = new Scene(root, width, height);
        getStage().setScene(scene);
        getStage().setTitle("TradeWorld - Admin");
        getStage().show();
    }

    /**
     * This method refreshes/ updates the all the Admin screens.
     */
    @Override
    public void update() {
        root.getTabs().get(0).setContent(addNewAdmin());
        root.getTabs().get(1).setContent(freezeUsers());
        root.getTabs().get(2).setContent(unfreezeUsers());
        root.getTabs().get(3).setContent(reviewItems());
        root.getTabs().get(4).setContent(setThresholdMenu());
        root.getTabs().get(5).setContent(undoActions());
    }

    /**
     * This method presents a list on the screen and asks the Admin user to select an option. The selected determines
     * which method to call in the AdminGUI. This screen is displayed again until the Admin selects an option
     * or logs out.
     * @return Returns a TabPane that provide options to set whichever threshold the Admin wants to set.
     */
    public Parent setThresholdMenu() {
        subRoot = new TabPane();
        subRoot.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Parent lendingLimitParent = setLendingThreshold();
        Tab lendingLimitTab = new Tab("Lending Threshold", lendingLimitParent);

        Parent weeklyLimitParent = setLimitOfTransactionsThreshold();
        Tab weeklyLimitTab = new Tab("Weekly Transactions Threshold", weeklyLimitParent);

        Parent incompleteLimitParent = setLimitOfIncompleteTrades();
        Tab incompleteTradeLimitTab = new Tab("Incomplete Trade Threshold", incompleteLimitParent);

        Parent editLimitParent = setLimitOfEdits();
        Tab editLimitTab = new Tab("Edit Threshold", editLimitParent);

        Parent goldLimitParent = setGoldThreshold();
        Tab goldLimitTab = new Tab("Gold Threshold", goldLimitParent);

        Parent silverLimitParent = setSilverThreshold();
        Tab silverLimitTab = new Tab("Silver Threshold", silverLimitParent);

        subRoot.getTabs().addAll(lendingLimitTab, weeklyLimitTab, incompleteTradeLimitTab, editLimitTab, goldLimitTab, silverLimitTab);

        // Listener code below based on https://stackoverflow.com/questions/17522686/javafx-tabpane-how-to-listen-to-selection-changes
        subRoot.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab) {
                        lendingLimitTab.setContent(setLendingThreshold());
                        weeklyLimitTab.setContent(setLimitOfTransactionsThreshold());
                        incompleteTradeLimitTab.setContent(setLimitOfIncompleteTrades());
                        editLimitTab.setContent(setLimitOfEdits());
                        goldLimitTab.setContent(setGoldThreshold());
                        silverLimitTab.setContent(setSilverThreshold());


                    }
                }
        );
        return subRoot;
    }

    /**
     * This method allows the Admin User to create a new Admin.
     * @return Returns a grid that allows the admin to add a new Admin.
     */
    public Parent addNewAdmin() {
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

        grid.add(newNameLabel, 0, 1);
        grid.add(nameField, 0, 2);
        grid.add(newUsernameLabel, 0, 3);
        grid.add(usernameField, 0, 4);
        grid.add(newPasswordLabel, 0, 5);
        grid.add(passwordField, 0, 6);
        grid.add(hBoxCreateAdmin, 0, 7);

        Label message = new Label();
        message.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        message.setAlignment(Pos.CENTER);
        grid.add(message, 0, 11, 1, 1);

        createButton.setOnAction(actionEvent -> {
            if (nameField.getText().isEmpty() || usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                message.setText("Please try again!");
            } else if (controller.askAdminToAddNewAdmin(nameField.getText(), usernameField.getText(), passwordField.getText())) {
                message.setText("New admin account created: " + usernameField.getText());
            } else {
                message.setText(usernameField.getText() + " username is already taken.");
            }
        });

       return grid;
    }

    /**
     * This method allows the Admin user to freeze a given list of users. The selected users will have their status
     * changed to frozen.
     * @return Returns a grid that allows the Admin to unfreeze users.
     */
    public Parent freezeUsers() {
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


        grid.add(heading, 0, 1);
        grid.add(list, 0, 2);
        grid.add(freezeHBox, 0, 3);

        Label message = new Label();
        message.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        message.setAlignment(Pos.CENTER);
        grid.add(message, 0, 11, 1, 1);

        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        freezeButton.setOnAction(actionEvent -> {
            ObservableList<String> selectedItems = list.getSelectionModel().getSelectedItems();
            ArrayList<String> selected = new ArrayList<>(selectedItems);
            if (selected.isEmpty()) {
                message.setText("No accounts have been selected to be frozen.");
            } else {
                controller.askAdminToFreezeUsers(selected);
                freezeAccounts.removeAll(selected);
                message.setText("Selected accounts have been frozen");
                list.refresh();
            }
        });
        return grid;
    }

    /**
     * This method allows the Admin user to unfreeze a given list of Users. The selected users will have their status
     * changed to unfrozen.
     * @return Returns a grid that allows the Admin to unfreeze users.
     */
    public Parent unfreezeUsers() {
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
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 1, 1);
        list.setPrefHeight(height - 50);
        list.setPrefWidth(width - 50);

        Button unfreezeButton = new Button("Unfreeze Accounts");
        HBox unfreezeHBox = new HBox(10);
        unfreezeButton.setAlignment(Pos.BOTTOM_RIGHT);
        unfreezeHBox.getChildren().add(unfreezeButton);

        grid.add(heading, 0, 1);
        grid.add(list, 0, 2);
        grid.add(unfreezeHBox, 0, 3);

        Label message = new Label();
        message.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        message.setAlignment(Pos.CENTER);
        grid.add(message, 0, 11, 1, 1);

        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        unfreezeButton.setOnAction(EventHandler -> {
            ObservableList<String> selectedItems = list.getSelectionModel().getSelectedItems();
            ArrayList<String> selected = new ArrayList<>(selectedItems);
            if (selected.isEmpty()) {
                message.setText("No accounts are selected to be unfrozen");
            } else {
                controller.askAdminToUnfreezeUsers(selectedItems);
                unfreezeAccounts.removeAll(selectedItems);
                message.setText("Selected accounts are now unfrozen.");
            }
        });

        return grid;
    }

    /**
     * This method allows the Admin user to select items that should be added to the system.
     * @return Returns a grid that allows the Admin to add items to the system.
     */
    public Parent reviewItems() {
        Text title = new Text("Review Items");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        TableViewCreator creator = new TableViewCreator(model);
        TableView<ObservableList<String>> table = creator.create("pending");
        table.setPlaceholder(new Label("There are no items to be reviewed."));

        Text heading = new Text("These items are pending to be added. Unselected items will be deleted.\n Hold down shift to add multiple items to the system.");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        table.setPrefHeight(height - 50); // we could change this
        table.setPrefWidth(width - 50);   // we could change this

        Button addItemsButton = new Button("Accept these items");
//        HBox hBoxAddItemsButton = new HBox(10);
        Button removeItemsButton = new Button("Reject these items");

//        hBoxAddItemsButton.setAlignment(Pos.BOTTOM_RIGHT);
//        hBoxAddItemsButton.getChildren().add(addItemsButton);

        grid.add(heading, 0, 1);
        grid.add(table, 0, 2, 2, 1);
//        grid.add(hBoxAddItemsButton, 0, 3);
        grid.add(addItemsButton, 0, 3);
        grid.add(removeItemsButton, 1, 3);

        Label message = new Label();
        message.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        message.setAlignment(Pos.CENTER);
        grid.add(message, 0, 11, 1, 1);

        TableView.TableViewSelectionModel<ObservableList<String>> itemSelection = table.getSelectionModel();
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        addItemsButton.setOnAction(actionEvent -> {
            ObservableList<ObservableList<String>> selectedItems = itemSelection.getSelectedItems();
            int size = selectedItems.size();
//            System.out.println(selectedItems.size());
            for (ObservableList<String> item : new ArrayList<>(selectedItems)) {
                controller.askAdminToAcceptItem(item.get(0));
                table.getItems().remove(item);
//                System.out.println(item.get(0));
            }
            if (size > 0) {
                message.setText("Selected items have been added to the system.");
            } else {
                message.setText("No items are added to the system.");
            }
        });

        removeItemsButton.setOnAction(actionEvent -> {
            ObservableList<ObservableList<String>> selectedItems = itemSelection.getSelectedItems();
            int size = selectedItems.size();
//            System.out.println(selectedItems.size());
            for (ObservableList<String> item : new ArrayList<>(selectedItems)) {
                try {
                    controller.askAdminToDeleteItem(item.get(0));
                } catch (NoLongerUndoableException e) {
                    e.printStackTrace();
                }
                table.getItems().remove(item);
//                System.out.println(item.get(0));
            }
            if (size > 0) {
                message.setText("Selected items have been removed from the system.");
            } else {
                message.setText("No items are removed to the system.");
            }
        });

        return grid;
    }

    /**
     * This method allows the Admin user to set a lending threshold and prompts the Admin user again if the input is
     * invalid.
     * @return Returns a grid that allows the Admin to set a lending thresholds.
     */
    public Parent setLendingThreshold() {
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

        grid.add(heading, 0, 1);
        grid.add(lendingThresholdField, 0, 2);
        grid.add(hBoxSetThreshold, 0, 3);

        Label message = new Label();
        message.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        message.setAlignment(Pos.CENTER);
        grid.add(message, 0, 11, 1, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrZero(lendingThresholdField.getText())) {
                controller.askAdminToSetLendingThreshold(lendingThresholdField.getText());
                message.setText("Threshold is set.");
                lendingThresholdField.clear();
                int updateLimit = model.getUserManager().getThreshold("trading");
                text.setText("Current threshold: " + updateLimit);
            } else {
                message.setText("Not an integer or is a number less than zero. \nPlease try again");
            }
        });
        return grid;
    }

    /**
     * This method allows the Admin user to set a limit for weekly transactions and prompts the Admin user again if the input is
     * invalid.
     * @return Returns a grid that allows the Admin to set a threshold for transactions.
     */
    public Parent setLimitOfTransactionsThreshold() {
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


        grid.add(heading, 0, 1);
        grid.add(limitThresholdField, 0, 2);
        grid.add(hBoxSetThreshold, 0, 3);

        Label message = new Label();
        message.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        message.setAlignment(Pos.CENTER);
        grid.add(message, 0, 11, 1, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrOne(limitThresholdField.getText())) {
                controller.askAdminToSetLimitOfTransactions(limitThresholdField.getText());
                message.setText("Threshold is set.");
                limitThresholdField.clear();
                int updateLimit = model.getTradeManager().getLimitTransactionPerWeek();
                text.setText("Current threshold: " + updateLimit);
            } else {
                message.setText("Not an integer or is a number less than one. \nPlease try again");
            }
        });
        return grid;
    }

    /**
     * This method allows the Admin user to set a threshold for having incomplete trades and prompts the Admin user again if the input is
     * invalid.
     * @return Returns a grid that allows the Admin to set a threshold for incomplete trades.
     */
    public Parent setLimitOfIncompleteTrades() {
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

        grid.add(heading, 0, 1);
        grid.add(limitThresholdField, 0, 2);
        grid.add(hBoxSetThreshold, 0, 3);

        Label message = new Label();
        message.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        message.setAlignment(Pos.CENTER);
        grid.add(message, 0, 11, 1, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrOne(limitThresholdField.getText())) {
                controller.askAdminToSetLimitOfIncompleteTrades(limitThresholdField.getText());
                message.setText("Threshold is set.");
                limitThresholdField.clear();
                int updateLimit = model.getTradeManager().getLimitIncomplete();
                text.setText("Current threshold: " + updateLimit);
            } else {
                message.setText("Not an integer or is a number less than one. \nPlease try again");
            }

        });
        return grid;
    }

    /**
     * This method allows the Admin user to set a threshold for edits and prompts the Admin user again if the input is
     * invalid.
     * @return Returns a grid that allows the Admin to set a threshold for edits.
     */
    public Parent setLimitOfEdits() {
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

        grid.add(heading, 0, 1);
        grid.add(limitThresholdField, 0, 2);
        grid.add(hBoxSetThreshold, 0, 3);

        Label message = new Label();
        message.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        message.setAlignment(Pos.CENTER);
        grid.add(message, 0, 11, 1, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrZero(limitThresholdField.getText())) {
                controller.askAdminToSetLimitOfEdits(limitThresholdField.getText());
                message.setText("Threshold is set.");
                limitThresholdField.clear();
                int updateLimit = model.getMeetingManager().getLimitEdits();
                text.setText("Current threshold: " + updateLimit);
            } else {
                message.setText("Not an integer or is a number less than zero. \nPlease try again");
            }
        });
        return grid;
    }

    /**
     * This method returns a grid that allows the admins to set a credit limit to rank users gold.
     * @return Returns a grid that allows the Admins to set a threshold for gold.
     */
    public Parent setGoldThreshold() {
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

        grid.add(heading, 0, 1);
        grid.add(limitThresholdField, 0, 2);
        grid.add(hBoxSetThreshold, 0, 3);

        Label message = new Label();
        message.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        message.setAlignment(Pos.CENTER);
        grid.add(message, 0, 11, 1, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrZero(limitThresholdField.getText())) {
                controller.setGoldThreshold(limitThresholdField.getText());
                message.setText("Threshold is set.");
                limitThresholdField.clear();
                int updateLimit = model.getUserManager().getThreshold("gold");
                text.setText("Current threshold: " + updateLimit);
            } else {
                message.setText("Not an integer or is a number less than zero. \nPlease try again");
            }
        });
        return grid;
    }

    /**
     * This method returns a grid that allows the admins to set a credit limit to rank users silver.
     * @return Returns a grid that allows the Admins to set a threshold for silver.
     */
    public Parent setSilverThreshold() {
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

        grid.add(heading, 0, 1);
        grid.add(limitThresholdField, 0, 2);
        grid.add(hBoxSetThreshold, 0, 3);

        Label message = new Label();
        message.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        message.setAlignment(Pos.CENTER);
        grid.add(message, 0, 11, 1, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (controller.IsAnIntegerOrZero(limitThresholdField.getText())) {
                controller.setSilverThreshold(limitThresholdField.getText());
                message.setText("Threshold is set.");
                limitThresholdField.clear();
                int updateLimit = model.getUserManager().getThreshold("silver");
                text.setText("Current threshold: " + updateLimit);
            } else {
                message.setText("Not an integer or is a number less than zero. \nPlease try again");
            }
        });
        return grid;
    }

    /**
     * This method allows the admin to undo actions such as undo the items added to the inventory, undo add proposed
     * trade, undo add reviews, and undo add wishlist items.
     * @return Returns a grid that allows the Admin users to undo an action.
     */
    public Parent undoActions() {
        Text title = new Text("Undo Operations");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        ListView<String> list = new ListView<>();

        list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<String> reviewItems = FXCollections.observableArrayList();
        Text heading = new Text("The following are outstanding undo actions.");

        List<String> undoOperations = new ArrayList<>(controller.undoOperationsString());

        reviewItems.addAll(undoOperations);
        list.setItems(reviewItems);
        list.setPlaceholder(new Label("There are no undo actions."));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);
        list.setPrefHeight(height - 50);
        list.setPrefWidth(width - 50);

        Button confirmToUndo = new Button("Confirm to Undo");
        HBox hBoxConfirmToUndo = new HBox(10);

        hBoxConfirmToUndo.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxConfirmToUndo.getChildren().add(confirmToUndo);

        grid.add(heading, 0, 1);
        grid.add(list, 0, 2);
        grid.add(hBoxConfirmToUndo, 0, 4);

        Label message = new Label();
        message.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        message.setAlignment(Pos.CENTER);
        grid.add(message, 0, 11, 1, 1);

        confirmToUndo.setOnAction(actionEvent -> {
            String selected = list.getSelectionModel().getSelectedItem();
            if (selected != null) { // Is this the right check? Should we check for null instead?
                try {
                    controller.undoOperation(selected);
                    reviewItems.remove(selected);
                } catch (NoLongerUndoableException e) {
                    e.printStackTrace();
                    message.setText("The selected operation can no longer be reversed");
                } message.setText("Reversed the selected actions.");
            } else {
                message.setText("No undo actions selected.");
            }
        });
       return grid;
    }
}
