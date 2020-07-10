import java.io.IOException;

/**
 * The entire trading system.
 */
public class TradeSystem {

    String serializedDataManagerInfo = "src/serializedobjects.ser";

    /**
     * Run the trading system.
     */
    public void run() {
        try {
            DataManager dataManager = new DataManager();
            TradeModel tradeModel = dataManager.readFromFile(serializedDataManagerInfo);
            LogInController controller = new LogInController(tradeModel);
            RunnableController mainController = controller.getNextController();
            mainController.run(); // This could be either UserController or AdminController
            dataManager.saveToFile(serializedDataManagerInfo, tradeModel);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
