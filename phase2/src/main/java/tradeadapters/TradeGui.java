package tradeadapters;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import trademisc.RunnableGUI;
import useradapters.ProfileController;
import viewingadapters.ViewingTradesController;

public class TradeGui implements RunnableGUI {
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


    public TradeGui(Stage stage, Scene scene, int width, int height, TradeModel tradeModel, String username) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.tradeModel = tradeModel;
        // this.username = username; if we remove username from the controller constructors
        this.initiateTradeController = new InitiateTradeController(tradeModel, username);
        this.confirmTradesController = new ConfirmTradesController(tradeModel, username);
        this.proposedTradesController = new ProposedTradesController(tradeModel, username);
        this.viewingTradesController = new ViewingTradesController(tradeModel, username);
        this.profileController = new ProfileController(tradeModel, username);


    }

    @Override
    public void initialScreen() {
        tradeInitialScreen();
    }

    public void tradeInitialScreen(){
        stage.setTitle("Trade Menu Options");

        Text title = new Text("What would you like to do?");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Button initiateTrade = new Button("Initiate trade");
        Button proposedTrades = new Button("Manage proposed trades");
        Button confirmTrades = new Button("Confirm real life meeting of trades");
        Button viewTrades = new Button("View ongoing and completed trades");
        Button backButton = new Button("Go back");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(title, 0, 0, 2, 1);
        grid.add(initiateTrade, 0, 1, 2, 1);
        grid.add(proposedTrades, 0, 2, 2, 1);
        grid.add(confirmTrades, 0, 3, 2, 1);
        grid.add(viewTrades, 0,4 , 2, 1);
        grid.add(backButton, 0, 5, 2, 1);

        scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();

    }

}
