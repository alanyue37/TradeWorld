package tradegateway;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import loginadapters.LogInController;
import loginadapters.LoginGUI;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * The entire trading system.
 */
public class TradeSystem implements Observer {

    private final String tradeModelFile = "serializedobjects.ser";
    private final String logoFile = "logo.png";
    private LogInController controller;
    private DataManager dataManager;
    private TradeModel tradeModel;

    /**
     * Run the trading system.
     */
    public void run(Stage stage) {
        try {
            dataManager = new DataManager(tradeModelFile);
            tradeModel = dataManager.readFromFile();
            Image logo = dataManager.readImage(logoFile);
            LoginGUI gui = new LoginGUI(stage, 275, 300, tradeModel, logo);
            gui.showScreen();
            }
        catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        try {
            dataManager.saveToFile(tradeModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
