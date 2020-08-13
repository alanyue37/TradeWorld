package tradegateway;

import javafx.stage.Stage;
import loginadapters.LogInController;
import loginadapters.LoginGUI;
import trademisc.RunnableController;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * The entire trading system.
 */
public class TradeSystem implements Observer {

    private final String tradeModelFile = "serializedobjects.ser";
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
            LoginGUI gui = new LoginGUI(stage, 275, 300, tradeModel);
            gui.initialScreen();
            } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        try {
//            RunnableController mainController = controller.getNextController();
//            if (mainController != null) {
//                mainController.run(); // This could be either UserController or AdminController
//            }
            dataManager.saveToFile(tradeModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
