package tradegateway;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import loginadapters.LogInController;
import loginadapters.LoginGUI;
import trademain.RunnableGUI;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * The entire trading system.
 */
public class TradeSystem {

    private final String tradeModelFile = "serializedobjects.ser";
    private final String logoFile = "logo.png";
    private DataManager dataManager;
    private TradeModel tradeModel;
    private RunnableGUI gui;

    /**
     * Run the trading system.
     */
    public void run(Stage stage) {
        try {
            dataManager = new DataManager(tradeModelFile);
            tradeModel = dataManager.readFromFile();
            Image logo = dataManager.readImage(logoFile);
            gui = new LoginGUI(stage, 500, 650, tradeModel, logo);
            gui.showScreen();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Persists required information to disk. Call when exiting.
     */
    public void persist() {
        try {
            tradeModel.clearState();
            dataManager.saveToFile(tradeModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
