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
 * Used the code from LifeOnTheFarm.zip in week 10.
 * https://docs.oracle.com/javafx/2/ui_controls/list-view.htm
 */
public class AdminGUI {
    private final Stage stage;
    private Scene scene;
    private final int width;
    private final int height;
    private final AdminPresenter adminPresenter;
    private final AdminController adminController;
    private final TradeModel model;


    public AdminGUI(Stage stage, int width, int height, AdminPresenter adminPresenter, AdminController adminController, TradeModel model) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.adminPresenter = adminPresenter;
        this.adminController = adminController;
        this.model = model;
    }

    public void addNewAdmin() {
        stage.setTitle("Admin User");
        Text title = new Text("Add a New Admin");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);

        Label newNameLabel = new Label(adminPresenter.accountEnterName());
        TextField nameField = new TextField();
        Label newUsernameLabel = new Label(adminPresenter.accountEnterUsername());
        TextField usernameField = new TextField();
        Label newPasswordLabel = new Label(adminPresenter.accountEnterPassword());
        TextField passwordField = new TextField();
        Button createButton = new Button("Create a New Admin");
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
            if (adminController.askAdminToAddNewAdmin(nameField.getText(), usernameField.getText(), passwordField.getText())) {
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

    public void newAdminCreated(String username) {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(adminPresenter.newAccountCreated(username));
        if(!grid.getChildren().contains(message)){
            grid.add(message, 0, 6, 2, 1);
        }
    }

    public void usernameTaken(String username) {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(adminPresenter.usernameTaken(username));
        if(!grid.getChildren().contains(message)){
            grid.add(message, 0, 6, 2, 1);
        }
    }

    public void tryAgain() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text("Please try again!");
        if(!grid.getChildren().contains(message)){
            grid.add(message, 0, 6, 2, 1);
        }
    }

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
        flaggedAccounts.addAll(adminController.getUsersExceedWeekly());
        flaggedAccounts.addAll(model.getUserManager().getUsersForFreezing());


        freezeAccounts.addAll(flaggedAccounts);
        list.setItems(freezeAccounts);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);


        Button freezeButton = new Button("Freeze these Accounts");
        HBox hBox = new HBox(list);
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

    public void noAccountsSelectedToFreeze() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(adminPresenter.freezeAccountsHeading(false));
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    public void accountsSelectedToFreeze() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(adminPresenter.freezeAccountsHeading(true));
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

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

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);

        Button unfreezeButton = new Button("Unfreeze these Accounts");
        HBox hBox = new HBox(list);
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
                accountsSelectedToUnfreeze();
            }
        });

        scene = new Scene(hBox, 300, 120);
        stage.setScene(scene);
        stage.show();
    }

    public void noAccountsSelectedToUnfreeze() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(adminPresenter.unfreezeAccountsHeading(false));
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    public void accountsSelectedToUnfreeze() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(adminPresenter.unfreezeAccountsHeading(true));
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

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

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(title, 0, 0, 2, 1);

        Button addItemsButton = new Button("Add these Items");
        HBox hBox = new HBox(list);
        HBox itemsHBox = new HBox(10);

        addItemsButton.setAlignment(Pos.BOTTOM_RIGHT);
        itemsHBox.getChildren().add(addItemsButton);

        grid.add(itemsHBox, 0, 1);

        ObservableList<Integer> selectedItems =  list.getSelectionModel().getSelectedIndices();
        ArrayList<Integer> conversion = new ArrayList<>(selectedItems);
        ArrayList<String> selected = new ArrayList<>();
        ArrayList<String> notSelected = new ArrayList<>();

        for(Integer item : conversion) {
            selected.add(itemsInList.get(item));
        }

        for (String item : itemsInList) {
            if (!selected.contains(item)) {
                notSelected.add(item);
            }
        }

        addItemsButton.setOnAction(actionEvent -> {
            adminController.askAdminToReviewItems(selected, notSelected);
            itemReviewed();
        });
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void itemReviewed() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(adminPresenter.reviewItemsHeading(true));
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

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

        Label lendingThresholdLabel = new Label(adminPresenter.lendingThreshold());
        TextField lendingThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);

        grid.add(lendingThresholdLabel, 0, 1);
        grid.add(lendingThresholdField, 0, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (adminController.IsAnIntegerOrZero(lendingThresholdField.getText())) {
                adminController.askAdminToSetLendingThreshold(lendingThresholdField.getText());
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

        Label limitThresholdLabel = new Label(adminPresenter.lendingThreshold());
        TextField limitThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);

        grid.add(limitThresholdLabel, 0, 1);
        grid.add(limitThresholdField, 0, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (adminController.IsAnIntegerOrOne(limitThresholdField.getText())) {
                adminController.askAdminToSetLimitOfTransactions(limitThresholdField.getText());
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

        Label limitThresholdLabel = new Label(adminPresenter.lendingThreshold());
        TextField limitThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);

        grid.add(limitThresholdLabel, 0, 1);
        grid.add(limitThresholdField, 0, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (adminController.IsAnIntegerOrOne(limitThresholdField.getText())) {
                adminController.askAdminToSetLimitOfIncompleteTrades(limitThresholdField.getText());
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

        Label limitThresholdLabel = new Label(adminPresenter.lendingThreshold());
        TextField limitThresholdField = new TextField();

        Button setThresholdButton = new Button("Set Threshold");
        HBox hBoxSetThreshold = new HBox(10);
        hBoxSetThreshold.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxSetThreshold.getChildren().add(setThresholdButton);

        grid.add(limitThresholdLabel, 0, 1);
        grid.add(limitThresholdField, 0, 1);

        setThresholdButton.setOnAction(actionEvent -> {
            if (adminController.IsAnIntegerOrZero(limitThresholdField.getText())) {
                adminController.askAdminToSetLimitOfEdits(limitThresholdField.getText());
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

    public void enterAtLeastZero() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(adminPresenter.enterAtLeastZero());
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    public void enterAtLeastOne() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(adminPresenter.enterAtLeastOne());
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }

    public void thresholdSet() {
        GridPane grid = (GridPane) scene.getRoot();
        Text message = new Text(adminPresenter.confirmationOfThreshold());
        if (!grid.getChildren().contains(message)) {
            grid.add(message, 0, 6, 2, 1);
        }
    }
}
