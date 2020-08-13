package useradapters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import tradeadapters.AlertBox;
import tradeadapters.ConfirmTradesController;
import tradeadapters.InitiateTradeController;
import tradeadapters.ProposedTradesController;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;
import viewingadapters.ViewingTradesController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class DemoTradeGUI implements RunnableGUI {
    private final Stage stage;
    private Scene scene;
    private final int width;
    private final int height;
    private final TradeModel tradeModel;
    private final InitiateTradeController initiateTradeController;
    private final ConfirmTradesController confirmTradesController;
    private final ProposedTradesController proposedTradesController;
    private final ViewingTradesController viewingTradesController;
    private final ProfileController profileController;
    private final String username;
    private DatePicker datePicker;
    private GridPane grid;

    public DemoTradeGUI(Stage stage, int width, int height, TradeModel tradeModel, String username) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.tradeModel = tradeModel;
//        this.username = tradeModel.getCurrentUser();
        this.username = username;
        this.initiateTradeController = new InitiateTradeController(tradeModel, username);
        this.confirmTradesController = new ConfirmTradesController(tradeModel, username);
        this.proposedTradesController = new ProposedTradesController(tradeModel, username);
        this.viewingTradesController = new ViewingTradesController(tradeModel, username);
        this.profileController = new ProfileController(tradeModel, username);
        this.datePicker = new DatePicker();
    }

    @Override
    public void initialScreen() {

    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return grid;
    }

    @Override
    public void showScreen() {
        initializeScreen();
        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    private void initializeScreen() {
        Text title = new Text("What would you like to do?");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Button initiateTradeBtn = new Button("Initiate trade");
        Button proposedTradesBtn = new Button("Manage proposed trades");
        Button confirmTradesBtn = new Button("Confirm real life meeting of trades");
        Button viewTradesBtn = new Button("View ongoing and completed trades");
        Button backButtonBtn = new Button("Go back");

        initiateTradeBtn.setMaxWidth(500);
        proposedTradesBtn.setMaxWidth(500);
        confirmTradesBtn.setMaxWidth(500);
        viewTradesBtn.setMaxWidth(500);
        backButtonBtn.setMaxWidth(500);

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);
        grid.add(initiateTradeBtn, 0, 1, 2, 1);
        grid.add(proposedTradesBtn, 0, 2, 2, 1);
        grid.add(confirmTradesBtn, 0, 3, 2, 1);
        grid.add(viewTradesBtn, 0, 4, 2, 1);
        grid.add(backButtonBtn, 0, 5, 2, 1);

        backButtonBtn.setOnAction(actionEvent -> new UserMenuGUI(stage, width, height, tradeModel, username).initialScreen());
    }

}
